package co.vine.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.StartActivity;
import co.vine.android.VineApplication;
import co.vine.android.api.PostInfo;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.TimelineItemWrapper;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineConversation;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePost;
import co.vine.android.api.VinePrivateMessage;
import co.vine.android.api.VinePrivateMessagePostResponseWithUsers;
import co.vine.android.api.VinePrivateMessageResponse;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineTypeAhead;
import co.vine.android.api.VineUpload;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.api.response.PagedDataResponse;
import co.vine.android.api.response.VineActivityCounts;
import co.vine.android.api.response.VineCommentsResponse;
import co.vine.android.client.AppController;
import co.vine.android.client.SessionManager;
import co.vine.android.client.VineAPI;
import co.vine.android.model.VineTag;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.network.VineNetworkUtils;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.service.components.VineServiceActionHelper;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.FileLogger;
import co.vine.android.util.FileLoggers;
import co.vine.android.util.MediaUtility;
import co.vine.android.util.SMSUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.BehaviorManager;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import com.mobileapptracker.MATEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import twitter4j.Twitter;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class VineService extends Service implements VineServiceInterface {
    private VineAPI mApi;
    protected ExecutorService mExecutor;
    private MediaUtility mMediaUtility;
    private LinkedHashMap<Integer, Boolean> mStartIds;
    private Twitter mTwitter;
    public static final IntentFilter SERVICE_INTENT_FILTER = new IntentFilter("co.vine.android.service.mergePost");
    public static final IntentFilter SHOW_POST_FILTER = new IntentFilter("co.vine.android.service.mergePostFeed");
    private static final Uri SAMSUNG_BADGER_URI = Uri.parse("content://com.sec.badge/apps");
    private static final String START_CLASS_NAME = StartActivity.class.getCanonicalName();
    private static final String[] BADGER_ARGS = {"co.vine.android"};
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private final NetworkOperationFactory<VineAPI> mNetOpFactory = VineNetworkUtils.getDefaultNetworkOperationFactory();
    private final Handler mServiceHandler = new VineServiceHandler();
    private final Messenger mMessenger = new Messenger(this.mServiceHandler);
    private final ConcurrentHashMap<Integer, NetworkOperation.CancelableRequest> mHttpRequests = new ConcurrentHashMap<>();
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();
    private final SLogger mLogger = ConsoleLoggers.VINE_SERVICE.get();
    private final FileLogger mFileLogger = FileLoggers.VINE_SERVICE.get();
    private final BroadcastReceiver mServiceBroadCastReceiver = new BroadcastReceiver() { // from class: co.vine.android.service.VineService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "co.vine.android.service.mergePost".equals(intent.getAction())) {
                VineService.this.mLogger.d("Received broadcasted intent to merge a single post.");
                AppController.getInstance(VineService.this).mergeSinglePost(intent.getExtras());
            }
        }
    };

    class VineServiceHandler extends Handler {
        VineServiceHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            msg.getData().setClassLoader(VineService.this.getClassLoader());
            VineService.this.mExecutor.execute(new ExecutionRunnable(VineService.this, msg.arg1, msg.getData(), msg.replyTo));
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mExecutor = Executors.newCachedThreadPool();
        this.mStartIds = new LinkedHashMap<>(50, 50.0f);
        registerReceiver(this.mServiceBroadCastReceiver, SERVICE_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mServiceBroadCastReceiver);
        this.mFileLogger.closeOutput();
    }

    private synchronized void init() {
        if (this.mApi == null) {
            this.mApi = VineAPI.getInstance(this);
        }
        if (this.mMediaUtility == null) {
            this.mMediaUtility = new MediaUtility(this);
        }
        if (SLog.sLogsOn) {
            this.mFileLogger.prepareOutput();
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        VineServiceResponder responder;
        if (intent != null) {
            init();
            Bundle bundle = intent.getExtras();
            VineServiceCallback cb = (VineServiceCallback) intent.getParcelableExtra("ibinder");
            if (cb != null) {
                responder = cb.getResponder();
            } else {
                responder = null;
            }
            VineApplication application = VineApplication.getInstance();
            VineServiceAction action = null;
            if (application != null) {
                action = application.getServiceActionProvider().getAction(intent.getAction());
            } else {
                CrashUtil.logOrThrowInDebug(new IllegalStateException("Provider not ready."));
            }
            if (action == null) {
                if (this.mLogger.isActive()) {
                    this.mLogger.d("Unknown action or provider is not ready: {}", intent.getAction());
                }
            } else {
                if (bundle == null) {
                    throw new IllegalArgumentException("Intent must contain extras: " + intent);
                }
                this.mExecutor.execute(new ExecutionRunnable(this, startId, bundle, responder, action));
                this.mStartIds.put(Integer.valueOf(startId), false);
            }
        }
        return 2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0c21  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0c34  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0c6d  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0d14  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x1590  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x15e3  */
    /* JADX WARN: Removed duplicated region for block: B:360:0x1623  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x1664  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x167a  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x167d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x024e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.os.Bundle executeAction(int r258, android.os.Bundle r259, co.vine.android.service.VineServiceAction r260) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 13414
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.service.VineService.executeAction(int, android.os.Bundle, co.vine.android.service.VineServiceAction):android.os.Bundle");
    }

    private String getStringAnchorValue(PagedDataResponse pagedDataResponse) {
        if (pagedDataResponse.anchor == null || pagedDataResponse.anchor.isEmpty()) {
            return null;
        }
        return pagedDataResponse.anchor;
    }

    @Override // co.vine.android.service.VineServiceInterface
    public VineServiceActionResult fetchActivity(long sessionOwnerId, Context context, Bundle b) {
        int page = b.getInt("page", 1);
        String anchor = b.getString("anchor");
        boolean getFollowRequests = b.getBoolean("follow_reqs", false);
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "users", Long.valueOf(sessionOwnerId), "notifications", "grouped");
        VineAPI.addParam(url, "clear", b.getBoolean("clear") ? 1 : 0);
        VineAPI.addParam(url, "page", page);
        VineAPI.addAnchor(url, anchor);
        VineAPI.addParam(url, "size", 20);
        VineParserReader vp = VineParserReader.createParserReader(28);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(context, url, this.mApi, vp);
        VineServiceActionHelper.assignPollingHeader(op, b);
        VineServiceActionHelper.assignCachePolicy(op, b, UrlCachePolicy.CACHE_THEN_NETWORK);
        op.execute();
        if (op.isOK()) {
            VinePagedData<VineEverydayNotification> results = (VinePagedData) vp.getParsedObject();
            b.putParcelable("notifications", results);
        }
        if (getFollowRequests) {
            StringBuilder followUrl = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "users", Long.valueOf(sessionOwnerId), "notifications", "followRequests");
            VineAPI.addParam(followUrl, "page", 1);
            VineAPI.addParam(followUrl, "size", 20);
            VineParserReader followVp = VineParserReader.createParserReader(9);
            NetworkOperation followOp = this.mNetOpFactory.createBasicAuthGetRequest(context, followUrl, this.mApi, followVp).execute();
            if (followOp.isOK()) {
                PagedActivityResponse.Data followNotifs = (PagedActivityResponse.Data) followVp.getParsedObject();
                b.putParcelable("follow_requests", Parcels.wrap(followNotifs));
            }
        }
        return new VineServiceActionResult(vp, op);
    }

    @Override // co.vine.android.service.VineServiceInterface
    public VineServiceActionResult fetchActivityCounts(long sessionId, Context context, Bundle b) {
        VineActivityCounts activityCounts;
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "users", Long.valueOf(sessionId), "activityCounts");
        VineAPI.addParam(url, "grouped", 1);
        VineParserReader vp = VineParserReader.createParserReader(26);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(context, url, this.mApi, vp);
        VineServiceActionHelper.assignPollingHeader(op, b);
        VineServiceActionHelper.assignCachePolicy(op, b, UrlCachePolicy.CACHE_THEN_NETWORK);
        op.execute();
        if (op.isOK() && (activityCounts = (VineActivityCounts) vp.getParsedObject()) != null) {
            b.putInt("messages_count", activityCounts.messages);
            b.putInt("notifications_count", activityCounts.notifications);
            Intent countIntent = new Intent();
            countIntent.putExtra("messages_count", activityCounts.messages);
            countIntent.putExtra("notifications_count", activityCounts.notifications);
            countIntent.setAction("co.vine.android.service.activityCounts");
            sendBroadcast(countIntent, CrossConstants.BROADCAST_PERMISSION);
            updateBadgeCount(activityCounts.messages + activityCounts.notifications);
        }
        return new VineServiceActionResult(vp, op);
    }

    private StringBuilder getUserUrl(long userId) {
        return VineServiceActionHelper.getUserUrl(this.mApi, userId);
    }

    @Override // co.vine.android.service.VineServiceInterface
    public Collection<NetworkOperation.CancelableRequest> getActiveRequests() {
        return this.mHttpRequests.values();
    }

    private void updateBadgeCount(int count) {
        ContentResolver resolver = getContentResolver();
        try {
            Cursor c = resolver.query(SAMSUNG_BADGER_URI, new String[]{"package", "badgecount"}, null, null, null);
            if (c != null) {
                ContentValues cv = new ContentValues();
                cv.put("package", "co.vine.android");
                cv.put("class", START_CLASS_NAME);
                cv.put("badgecount", Integer.valueOf(count));
                if (c.getCount() > 0) {
                    resolver.update(SAMSUNG_BADGER_URI, cv, "package=?", BADGER_ARGS);
                } else {
                    resolver.insert(SAMSUNG_BADGER_URI, cv);
                }
                c.close();
            }
        } catch (Exception e) {
            SLog.e("Ignore samsung error.", (Throwable) e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x0321 A[Catch: all -> 0x0158, TryCatch #3 {, blocks: (B:3:0x0001, B:4:0x0022, B:6:0x0028, B:8:0x0036, B:9:0x003e, B:11:0x0063, B:14:0x0072, B:15:0x0079, B:46:0x015c, B:37:0x0148, B:17:0x0094, B:20:0x00ae, B:22:0x00b4, B:23:0x00bd, B:25:0x00c3, B:48:0x0161, B:49:0x0168, B:51:0x0170, B:53:0x018a, B:55:0x0192, B:56:0x019d, B:58:0x01a5, B:59:0x01b0, B:61:0x01b8, B:62:0x01bf, B:65:0x01c7, B:68:0x01d2, B:71:0x01dd, B:73:0x01e4, B:74:0x01fc, B:76:0x0200, B:78:0x0208, B:79:0x0214, B:82:0x021c, B:83:0x0221, B:28:0x0123, B:29:0x0126, B:32:0x013c, B:119:0x02eb, B:120:0x0313, B:121:0x0321, B:123:0x0329, B:125:0x0348, B:127:0x0356, B:128:0x036e, B:130:0x0374, B:131:0x038e, B:133:0x0394, B:134:0x03a9, B:89:0x0238, B:91:0x025c, B:92:0x026e, B:94:0x0276, B:96:0x027e, B:97:0x0285, B:99:0x028d, B:100:0x0294, B:102:0x029f, B:104:0x02a7, B:117:0x02e1, B:105:0x02b3, B:108:0x02bb, B:109:0x02c0, B:111:0x02c4, B:113:0x02ce, B:40:0x0153), top: B:144:0x0001, inners: #1, #2, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0094 A[Catch: all -> 0x0158, TRY_ENTER, TRY_LEAVE, TryCatch #3 {, blocks: (B:3:0x0001, B:4:0x0022, B:6:0x0028, B:8:0x0036, B:9:0x003e, B:11:0x0063, B:14:0x0072, B:15:0x0079, B:46:0x015c, B:37:0x0148, B:17:0x0094, B:20:0x00ae, B:22:0x00b4, B:23:0x00bd, B:25:0x00c3, B:48:0x0161, B:49:0x0168, B:51:0x0170, B:53:0x018a, B:55:0x0192, B:56:0x019d, B:58:0x01a5, B:59:0x01b0, B:61:0x01b8, B:62:0x01bf, B:65:0x01c7, B:68:0x01d2, B:71:0x01dd, B:73:0x01e4, B:74:0x01fc, B:76:0x0200, B:78:0x0208, B:79:0x0214, B:82:0x021c, B:83:0x0221, B:28:0x0123, B:29:0x0126, B:32:0x013c, B:119:0x02eb, B:120:0x0313, B:121:0x0321, B:123:0x0329, B:125:0x0348, B:127:0x0356, B:128:0x036e, B:130:0x0374, B:131:0x038e, B:133:0x0394, B:134:0x03a9, B:89:0x0238, B:91:0x025c, B:92:0x026e, B:94:0x0276, B:96:0x027e, B:97:0x0285, B:99:0x028d, B:100:0x0294, B:102:0x029f, B:104:0x02a7, B:117:0x02e1, B:105:0x02b3, B:108:0x02bb, B:109:0x02c0, B:111:0x02c4, B:113:0x02ce, B:40:0x0153), top: B:144:0x0001, inners: #1, #2, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0238 A[Catch: all -> 0x0158, TRY_ENTER, TryCatch #3 {, blocks: (B:3:0x0001, B:4:0x0022, B:6:0x0028, B:8:0x0036, B:9:0x003e, B:11:0x0063, B:14:0x0072, B:15:0x0079, B:46:0x015c, B:37:0x0148, B:17:0x0094, B:20:0x00ae, B:22:0x00b4, B:23:0x00bd, B:25:0x00c3, B:48:0x0161, B:49:0x0168, B:51:0x0170, B:53:0x018a, B:55:0x0192, B:56:0x019d, B:58:0x01a5, B:59:0x01b0, B:61:0x01b8, B:62:0x01bf, B:65:0x01c7, B:68:0x01d2, B:71:0x01dd, B:73:0x01e4, B:74:0x01fc, B:76:0x0200, B:78:0x0208, B:79:0x0214, B:82:0x021c, B:83:0x0221, B:28:0x0123, B:29:0x0126, B:32:0x013c, B:119:0x02eb, B:120:0x0313, B:121:0x0321, B:123:0x0329, B:125:0x0348, B:127:0x0356, B:128:0x036e, B:130:0x0374, B:131:0x038e, B:133:0x0394, B:134:0x03a9, B:89:0x0238, B:91:0x025c, B:92:0x026e, B:94:0x0276, B:96:0x027e, B:97:0x0285, B:99:0x028d, B:100:0x0294, B:102:0x029f, B:104:0x02a7, B:117:0x02e1, B:105:0x02b3, B:108:0x02bb, B:109:0x02c0, B:111:0x02c4, B:113:0x02ce, B:40:0x0153), top: B:144:0x0001, inners: #1, #2, #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized co.vine.android.network.NetworkOperation post(android.content.Context r36, co.vine.android.api.VineUpload r37, android.os.Bundle r38) {
        /*
            Method dump skipped, instructions count: 953
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.service.VineService.post(android.content.Context, co.vine.android.api.VineUpload, android.os.Bundle):co.vine.android.network.NetworkOperation");
    }

    private void handleVinePrivateMessageResponses(Context context, VineUpload upload, PostInfo info, VineParserReader vp, Bundle bundle) {
        String text;
        VinePrivateMessagePostResponseWithUsers serverResponse = (VinePrivateMessagePostResponseWithUsers) vp.getParsedObject();
        ArrayList<VinePrivateMessageResponse> messageResponses = serverResponse.responses;
        ArrayList<VineUser> users = serverResponse.users;
        ArrayList<VineRecipient> recipients = serverResponse.recipients;
        VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(context);
        VinePrivateMessage premergedMessage = dbHelper.getMessageFromMessageRow(upload.mergedMessageId);
        if (upload.mergedMessageId <= 0 || premergedMessage == null) {
            CrashUtil.log("No local message to merge with.");
            return;
        }
        if (users != null) {
            dbHelper.mergeRecipientsWithUsersAndRemoveUnusedRecipients(premergedMessage.conversationRowId, recipients, users);
        }
        long messageIdToMerge = -1;
        if (messageResponses == null || messageResponses.size() < 1) {
            Object[] objArr = new Object[1];
            objArr[0] = Boolean.valueOf(messageResponses == null);
            CrashUtil.log("No vine private message responses, deleting the conversation and bailing. Response is null: {}", objArr);
            dbHelper.deleteConversation(-1L, premergedMessage.conversationRowId);
            return;
        }
        if (messageResponses.size() == 1) {
            if (premergedMessage.conversationId <= 0) {
                premergedMessage.conversationRowId = dbHelper.mergeConversationWithLocalId(premergedMessage.conversationRowId, messageResponses.get(0).conversationId, premergedMessage.networkType);
            }
            messageIdToMerge = upload.mergedMessageId;
        } else {
            dbHelper.deleteConversation(-1L, premergedMessage.conversationRowId);
        }
        String smsFormatter = null;
        String smsFooter = null;
        String videoUrl = null;
        String thumbnailUrl = null;
        Iterator<VinePrivateMessageResponse> it = messageResponses.iterator();
        while (it.hasNext()) {
            VinePrivateMessageResponse vpmr = it.next();
            if (vpmr != null) {
                if (vpmr.recipient != null && vpmr.recipient.key.equals("phoneNumber") && vpmr.shareUrl != null) {
                    if (smsFormatter == null) {
                        smsFormatter = getString(R.string.sms_text);
                    }
                    if (smsFooter == null) {
                        smsFooter = getString(R.string.sms_footer);
                    }
                    if (TextUtils.isEmpty(info.message)) {
                        text = String.format(smsFormatter, vpmr.shareUrl);
                    } else {
                        text = info.message + ": " + vpmr.shareUrl;
                    }
                    String text2 = text + smsFooter;
                    this.mLogger.d("Send SMS to {} with text {}", vpmr.recipient.value, text2);
                    SMSUtil.sendSMS(vpmr.recipient.value, text2);
                }
                if (vpmr.videoUrl != null) {
                    videoUrl = vpmr.videoUrl;
                }
                if (vpmr.thumbnailUrl != null) {
                    thumbnailUrl = vpmr.thumbnailUrl;
                }
                handleSingleVinePrivateMessageResponse(context, dbHelper, vpmr, info, messageIdToMerge, premergedMessage.conversationRowId);
                if (vpmr.error != null) {
                    bundle.putBoolean("should_delete_upload", true);
                }
            }
        }
        bundle.putString("post_url", videoUrl);
        bundle.putString("thumbnail_url", thumbnailUrl);
    }

    private void handleSingleVinePrivateMessageResponse(Context context, VineDatabaseHelper dbHelper, VinePrivateMessageResponse vpmr, PostInfo info, long messageIdToMerge, long conversationRowIdToMerge) {
        int errorCode;
        String errorReason;
        long conversationRowId;
        long userRowId = dbHelper.getUserRowIdForUserRemoteId(AppController.getInstance(context).getActiveSessionReadOnly().getUserId());
        if (vpmr.error != null) {
            errorCode = vpmr.error.getErrorCode();
            errorReason = vpmr.error.getMessage();
        } else {
            errorCode = 0;
            errorReason = null;
        }
        VinePrivateMessage vpm = new VinePrivateMessage(-1L, vpmr.conversationId, vpmr.messageId, userRowId, info.created, info.message, vpmr.videoUrl, vpmr.thumbnailUrl, 1, false, info.postId, errorCode, errorReason);
        if (messageIdToMerge > 0) {
            long finalMessageRowId = dbHelper.mergeMessageWithMessageRow(messageIdToMerge, conversationRowIdToMerge, vpm);
            dbHelper.updateConversationWithLastMessage(conversationRowIdToMerge, finalMessageRowId, vpm.created);
        } else {
            HashSet<Long> userRowIds = new HashSet<>();
            userRowIds.add(Long.valueOf(vpmr.recipient.recipientId));
            if (vpmr.conversationId > 0) {
                conversationRowId = dbHelper.getConversationRowId(vpmr.conversationId);
            } else {
                conversationRowId = dbHelper.determineBestConversationRowIdForUserRowId(vpmr.recipient.recipientId);
            }
            if (conversationRowId <= 0) {
                conversationRowId = dbHelper.createConversationRowId(userRowIds, 1);
                dbHelper.mergeConversationWithLocalId(conversationRowId, vpmr.conversationId, 1);
            }
            long messageId = dbHelper.mergeMessage(conversationRowId, vpm);
            dbHelper.updateConversationWithLastMessage(conversationRowId, messageId, vpm.created);
        }
        this.mLogger.d("VM post successful, sending merge broadcast now");
        Intent intent = new Intent("co.vine.android.service.mergeSelfNewMessage");
        intent.putExtra("conversation_id", vpm.conversationId);
        intent.putExtra("message_id", vpm.messageId);
        context.sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
    }

    public void limitCommentAndLikesIfNeeded(StringBuilder url) {
        if (BuildUtil.isOldDeviceOrLowEndDevice(this)) {
            VineAPI.addParam(url, "c_max", 0);
            VineAPI.addParam(url, "l_max", 0);
        }
    }

    private StringBuilder generatePostFetchUrl(int type, long userId, int page, Bundle b, boolean prefetch) {
        StringBuilder url;
        String base = this.mApi.getBaseUrl();
        switch (type) {
            case 1:
                url = VineAPI.buildUponUrl(base, "timelines", "graph");
                break;
            case 2:
                url = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId));
                VineAPI.addParam(url, "reposts", 1);
                VineAPI.addParam(url, "sort", b.getString("sort", "recent"));
                VineAPI.addParam(url, "c_overflow", "trunc");
                break;
            case 3:
                url = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId), "likes");
                break;
            case 4:
                url = VineAPI.buildUponUrl(base, "timelines", "users", "trending");
                break;
            case 5:
                url = VineAPI.buildUponUrl(base, "timelines", "popular");
                break;
            case 6:
            case 16:
                String tag = b.getString("tag_name");
                url = VineAPI.buildUponUrl(base, "timelines", "tags", tag);
                VineAPI.addParam(url, "sort", type == 16 ? "recent" : "top");
                break;
            case 7:
            case 12:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            default:
                throw new IllegalArgumentException("Tried to fetch timeline with unsupported type " + type);
            case 8:
            case 9:
                String channelId = b.getString("tag_name");
                String sort = b.getString("sort");
                url = VineAPI.buildUponUrl(base, "timelines", "channels", channelId, sort);
                break;
            case 10:
                url = VineAPI.buildUponUrl(base, "timelines", "users", Long.valueOf(userId));
                VineAPI.addParam(url, "sort", b.getString("sort", "recent"));
                VineAPI.addParam(url, "reposts", 0);
                break;
            case 11:
                url = VineAPI.buildUponUrl(base, "timelines");
                Uri data = (Uri) b.getParcelable("data");
                for (String pathSegment : data.getPathSegments()) {
                    url = VineAPI.buildUponUrl(url.toString(), pathSegment);
                }
                break;
            case 13:
                StringBuilder url2 = VineAPI.buildUponUrl(base, "timelines");
                url = VineAPI.buildUponUrl(url2.toString(), "venues");
                Uri data2 = (Uri) b.getParcelable("data");
                for (String pathSegment2 : data2.getPathSegments()) {
                    url = VineAPI.buildUponUrl(url.toString(), pathSegment2);
                }
                break;
            case 14:
            case 15:
                String query = b.getString("tag_name");
                url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), MATEvent.SEARCH, "posts");
                VineAPI.addParam(url, "q", query);
                VineAPI.addParam(url, "sort", type == 15 ? "recent" : "top");
                break;
            case 17:
            case 18:
                url = VineAPI.buildUponUrl(base, "timelines");
                Uri data3 = (Uri) b.getParcelable("data");
                for (String pathSegment3 : data3.getPathSegments()) {
                    url = VineAPI.buildUponUrl(url.toString(), pathSegment3);
                }
                for (String param : data3.getQueryParameterNames()) {
                    VineAPI.addParam(url, param, data3.getQueryParameter(param));
                }
                VineAPI.addParam(url, "sort", type == 18 ? "recent" : "top");
                break;
            case 30:
                url = VineAPI.buildUponUrl(base, "timelines", "welcome");
                break;
        }
        if (!prefetch) {
            BehaviorManager.getInstance(this).onFetchPosts(url.toString(), page);
        }
        VineAPI.addParam(url, "page", page);
        b.putString("timeline_url", url.toString());
        return url;
    }

    @Override // co.vine.android.service.VineServiceInterface
    public NetworkOperation getPosts(StringBuilder url, int type, int size, VineDatabaseHelperInterface dbHelper, String anchor, String backAnchor, Bundle b, boolean prefetch) {
        VinePagedData<TimelineItem> pagedData;
        VineAPI.addAnchor(url, anchor);
        VineAPI.addBackAnchor(url, backAnchor);
        VineAPI.addParam(url, "size", size);
        limitCommentAndLikesIfNeeded(url);
        this.mLogger.d("Fetching timeline: {}", url);
        VineParserReader vp = VineParserReader.createParserReader(36);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(this, url, this.mApi, vp);
        VineServiceActionHelper.assignPollingHeader(op, b);
        UrlCachePolicy resolvedCachePolicy = VineServiceActionHelper.assignCachePolicy(op, b, UrlCachePolicy.CACHE_THEN_NETWORK);
        op.execute();
        NetworkOperation.NetworkOperationResult result = op.getLastExecuteResult();
        PrefetchManager.getInstance(this).onPostFetchOperationComplete(prefetch, op.getLastExecuteResult(), resolvedCachePolicy);
        if (op.isOK() && (pagedData = (VinePagedData) vp.getParsedObject()) != null) {
            if (type >= 0) {
                if (Util.isPopularTimeline(type)) {
                    int count = 1;
                    Cursor c = dbHelper.getOldestSortId(type);
                    if (c != null) {
                        if (c.moveToFirst()) {
                            count = c.getInt(0) + 1;
                        }
                        c.close();
                    }
                    Iterator<TimelineItem> it = pagedData.items.iterator();
                    while (it.hasNext()) {
                        TimelineItem item = it.next();
                        if (item.getType() == TimelineItemType.POST) {
                            ((VinePost) item).orderId = String.valueOf(count);
                            count++;
                        }
                    }
                }
                b.putInt("count", pagedData.items == null ? 0 : pagedData.items.size());
                b.putInt("size", pagedData.count);
                TimelineItemWrapper.bundleTimelineItemList(b, pagedData.items, "timeline_items");
                b.putBoolean("in_memory", true);
                b.putBoolean("network", result == NetworkOperation.NetworkOperationResult.NETWORK);
                b.putInt("next_page", pagedData.nextPage);
                b.putInt("previous_page", pagedData.previousPage);
                b.putString("anchor", pagedData.anchor);
                b.putString("back_anchor", pagedData.backAnchor);
                b.putString("title", pagedData.title);
                if (type == 8 || type == 9) {
                    b.putParcelable("channels", Parcels.wrap(pagedData.channel));
                }
            } else {
                TimelineItemWrapper.bundleTimelineItemList(b, pagedData.items, "timeline_items");
            }
        }
        return op;
    }

    @Override // co.vine.android.service.VineServiceInterface
    public void setTwitter(Twitter twitter) {
        this.mTwitter = twitter;
    }

    @Override // co.vine.android.service.VineServiceInterface
    public Twitter getTwitter() {
        return this.mTwitter;
    }

    private VineConversation getConversationWithRemoteId(long sessionOwnerId, long conversationId, int pageType, VineDatabaseHelper dbHelper, boolean prefetch, int networkTYpe) {
        VinePagedData<VinePrivateMessage> data;
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "conversations", Long.valueOf(conversationId));
        switch (pageType) {
            case 1:
            case 2:
                VineAPI.addParam(url, "page", 1);
                break;
            case 3:
                long page = dbHelper.getNextPageCursor(7, 0, String.valueOf(conversationId), false);
                if (page > 0) {
                    VineAPI.addParam(url, "page", page);
                    break;
                }
                break;
        }
        VineAPI.addParam(url, "prefetch", prefetch ? 1 : 0);
        VineParserReader vp = VineParserReader.createParserReader(22);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(this, url, this.mApi, vp).execute();
        if (op.isOK() && (data = (VinePagedData) vp.getParsedObject()) != null && data.items != null && data.items.size() > 0) {
            VineConversation conversation = new VineConversation(conversationId, data.lastMessage, data.items, data.unreadMessageCount);
            try {
                long conversationLocalObjectId = dbHelper.mergeConversation(sessionOwnerId, conversation, networkTYpe, data.nextPage, data.previousPage, data.anchor);
                if (data.nextPage <= 0) {
                    dbHelper.markLastMessage(conversationLocalObjectId);
                }
                conversation.conversationObjectId = conversationLocalObjectId;
                return conversation;
            } catch (IOException e) {
                this.mLogger.e("failed to execute", (Throwable) e);
            }
        }
        return null;
    }

    private NetworkOperation getInbox(long sessionOwnerId, int pageType, int actionCode, String key, VineDatabaseHelper dbHelper, Bundle b) throws IOException {
        int userGroup;
        int networkType = b.getInt("network", -1);
        int page = 1;
        switch (pageType) {
            case 1:
            case 2:
                page = 1;
                break;
            case 3:
                int previous = dbHelper.getPreviousPageCursor(6, networkType, null, true);
                if (previous > 0) {
                    page = previous;
                    break;
                }
                break;
        }
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "conversations");
        if (networkType == -1) {
            throw new IllegalArgumentException("Invalid network type.");
        }
        VineParserReader vp = VineParserReader.createParserReader(25);
        if (1 == networkType) {
            userGroup = 10;
        } else {
            VineAPI.addParam(url, "inbox", "other");
            userGroup = 11;
        }
        VineAPI.addParam(url, "page", page);
        VineAPI.addParam(url, "size", 20);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(this, url, this.mApi, vp, key).execute();
        if (op.isOK()) {
            VinePagedData<VineConversation> data = (VinePagedData) vp.getParsedObject();
            if (data.items != null && !data.items.isEmpty()) {
                dbHelper.mergeInbox(sessionOwnerId, data, data.nextPage, data.previousPage, data.anchor, networkType, userGroup);
                b.putInt("count", data.items.size());
                if (data.nextPage <= 0) {
                    dbHelper.markLastConversation(networkType);
                }
            }
        }
        return op;
    }

    public void addCountryParam(ArrayList<BasicNameValuePair> params) {
        params.add(new BasicNameValuePair("locale", Util.getCountryCode()));
    }

    public void addCountryParam(JSONObject params) throws JSONException {
        params.put("locale", Util.getCountryCode());
    }

    private NetworkOperation postComment(long postId, long repostId, long userId, String username, String comment, String avatarUrl, ArrayList<VineEntity> entities, VineDatabaseHelper dbHelper, Bundle b) throws JSONException {
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "posts", Long.valueOf(postId), "comments");
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("comment", comment);
            JSONArray jsonEntities = new JSONArray();
            Iterator<VineEntity> it = entities.iterator();
            while (it.hasNext()) {
                VineEntity entity = it.next();
                entity.generateEntityLinkForComment();
                jsonEntities.put(entity.toJsonObject());
            }
            if (jsonEntities.length() > 0) {
                postBody.put("entities", jsonEntities);
            }
            if (repostId > 0) {
                postBody.put("repostId", repostId);
            }
        } catch (JSONException e) {
            CrashUtil.logOrThrowInDebug(e);
        }
        VineParserReader vp = VineParserReader.createParserReader(7);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthJsonPostRequest((Context) this, url, (StringBuilder) this.mApi, postBody, (NetworkOperationReader) vp).execute();
        if (op.isOK()) {
            VineComment vc = (VineComment) vp.getParsedObject();
            vc.userId = userId;
            vc.username = username;
            vc.avatarUrl = avatarUrl;
            int offsetCount = 0;
            Iterator<VineEntity> it2 = entities.iterator();
            while (it2.hasNext()) {
                VineEntity entity2 = it2.next();
                entity2.start -= offsetCount;
                if ("tag".equals(entity2.type)) {
                    offsetCount++;
                    String plainTitle = VineTypeAhead.getPlainTag(entity2.title);
                    comment = comment.replaceFirst(entity2.title, plainTitle);
                    entity2.title = plainTitle;
                    dbHelper.mergeSuggestedTag(VineTag.create(plainTitle, entity2.id, 0L));
                }
                entity2.end -= offsetCount;
            }
            vc.comment = comment;
            vc.entities = entities;
            vc.timestamp = System.currentTimeMillis();
            b.putParcelable("comment_obj", vc);
        }
        return op;
    }

    private NetworkOperation deleteComment(long postId, long repostId, String commentId, VineDatabaseHelper dbHelper) {
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "posts", Long.valueOf(postId), "comments", commentId);
        if (repostId > 0) {
            VineAPI.addParam(url, "repostId", repostId);
        }
        VineParserReader vp = VineParserReader.createParserReader(7);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthDeleteRequest(this, url, this.mApi, vp).execute();
        return op;
    }

    private NetworkOperation getComments(long postId, int page, String anchor, int size, String key, Bundle b, VineDatabaseHelper dbHelper) {
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "posts", Long.valueOf(postId), "comments");
        VineAPI.addParam(url, "page", page);
        VineAPI.addAnchor(url, anchor);
        VineAPI.addParam(url, "size", size);
        VineParserReader vp = VineParserReader.createParserReader(6);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(this, url, this.mApi, vp).execute();
        if (op.isOK()) {
            VineCommentsResponse.Data comments = (VineCommentsResponse.Data) vp.getParsedObject();
            b.putInt("next_page", comments.nextPage);
            b.putString("anchor", getStringAnchorValue(comments));
            b.putInt("previous_page", comments.previousPage);
            b.putParcelableArrayList("comments", comments.items);
        }
        return op;
    }

    @Override // co.vine.android.service.VineServiceInterface
    public NetworkOperation fetchUsersMe(Bundle b) {
        VineUser user;
        StringBuilder url = VineAPI.buildUponUrl(this.mApi.getBaseUrl(), "users", "me");
        VineParserReader vp = VineParserReader.createParserReader(2);
        NetworkOperation op = this.mNetOpFactory.createBasicAuthGetRequest(this, url, this.mApi, vp);
        VineServiceActionHelper.assignPollingHeader(op, b);
        VineServiceActionHelper.assignCachePolicy(op, b, UrlCachePolicy.CACHE_THEN_NETWORK);
        op.execute();
        if (op.isOK() && (user = (VineUser) vp.getParsedObject()) != null) {
            VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(this);
            user.id = dbHelper.mergeUserAndGetResultingRowId(user);
            b.putParcelable(PropertyConfiguration.USER, user);
            this.mSessionManager.updateLocalSessionData(user);
        }
        return op;
    }

    void attemptStop(int startId) {
        this.mStartIds.put(Integer.valueOf(startId), true);
        for (Map.Entry<Integer, Boolean> entry : this.mStartIds.entrySet()) {
            if (!entry.getValue().booleanValue()) {
                return;
            }
        }
        for (Integer key : this.mStartIds.keySet()) {
            stopSelf(key.intValue());
        }
        this.mStartIds.clear();
    }

    private class ResponderRunnable implements Runnable {
        private final int mActionCode;
        private final Bundle mBundle;
        private final String mReasonPhrase;
        private final VineServiceResponder mResponder;
        private final int mStartId;
        private final int mStatusCode;

        public ResponderRunnable(VineServiceResponder responder, int actionCode, int statusCode, String reasonPhrase, Bundle bundle, int startId) {
            this.mResponder = responder;
            this.mActionCode = actionCode;
            this.mStatusCode = statusCode;
            this.mReasonPhrase = reasonPhrase;
            this.mBundle = bundle;
            this.mStartId = startId;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mResponder != null) {
                this.mResponder.onServiceResponse(this.mActionCode, this.mStatusCode, this.mReasonPhrase, this.mBundle);
            }
            if (this.mStartId > 0) {
                VineService.this.attemptStop(this.mStartId);
            }
        }
    }

    private class MessengerResponderRunnable implements Runnable {
        private final int mActionCode;
        private final Bundle mBundle;
        private final String mReasonPhrase;
        private final Messenger mResponder;
        private final int mStartId;
        private final int mStatusCode;

        public MessengerResponderRunnable(Messenger responder, int actionCode, int statusCode, String reasonPhrase, Bundle bundle, int startId) {
            this.mResponder = responder;
            this.mActionCode = actionCode;
            this.mStatusCode = statusCode;
            this.mReasonPhrase = reasonPhrase;
            this.mBundle = bundle;
            this.mStartId = startId;
        }

        @Override // java.lang.Runnable
        public void run() throws RemoteException {
            if (this.mResponder != null) {
                Message msg = new Message();
                msg.what = this.mActionCode;
                msg.arg1 = this.mStatusCode;
                Bundle b = msg.getData();
                b.putString("reason_phrase", this.mReasonPhrase);
                b.putAll(this.mBundle);
                try {
                    this.mResponder.send(msg);
                } catch (Exception e) {
                    CrashUtil.logException(e, "Error sending service response", new Object[0]);
                }
            }
            if (this.mStartId > 0) {
                VineService.this.attemptStop(this.mStartId);
            }
        }
    }

    private class ExecutionRunnable implements Runnable {
        private final VineServiceAction mAction;
        private final int mActionCode;
        private final Bundle mBundle;
        private final Messenger mMessenger;
        private final VineServiceResponder mResponder;
        private final int mStartId;

        public ExecutionRunnable(VineService vineService, int startId, Bundle bundle, VineServiceResponder responder, VineServiceAction action) {
            this(startId, -1, bundle, responder, null, action);
        }

        public ExecutionRunnable(VineService vineService, int actionCode, Bundle bundle, Messenger messenger) {
            this(0, actionCode, bundle, null, messenger, null);
        }

        public ExecutionRunnable(int startId, int actionCode, Bundle bundle, VineServiceResponder responder, Messenger messenger, VineServiceAction action) {
            this.mStartId = startId;
            this.mActionCode = actionCode;
            this.mBundle = bundle;
            this.mResponder = responder;
            this.mMessenger = messenger;
            this.mAction = action;
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            Runnable responderRunnable;
            Process.setThreadPriority(10);
            Bundle result = VineService.this.executeAction(this.mActionCode, this.mBundle, this.mAction);
            switch (result.getInt("executionCode")) {
                case -999:
                    CrashUtil.log("Service is not ready, {} needs to retry later.", Integer.valueOf(this.mActionCode));
                    break;
                case 2:
                    VineService.this.mLogger.d("Session key was invalid. Refreshing session key and then will try again");
                    result = VineService.this.executeAction(1000, this.mBundle, this.mAction);
                    switch (result.getInt("executionCode")) {
                        case 2:
                            VineService.this.mLogger.d("Session key could not be refreshed. Aborting.");
                            this.mBundle.putBoolean("logged_out", true);
                            break;
                        case 3:
                            VineService.this.mLogger.d("Session was logged out.");
                            this.mBundle.putBoolean("logged_out", true);
                            break;
                        default:
                            VineService.this.mLogger.d("Session key successfully refreshed. Trying original action of " + this.mActionCode + " again");
                            result = VineService.this.executeAction(this.mActionCode, this.mBundle, this.mAction);
                            switch (result.getInt("executionCode")) {
                                case 2:
                                    VineService.this.mLogger.d("Session key is still invalid. Aborting.");
                                    this.mBundle.putBoolean("logged_out", true);
                                    break;
                                case 3:
                                    VineService.this.mLogger.d("Session was logged out.");
                                    this.mBundle.putBoolean("logged_out", true);
                                    break;
                                default:
                                    if (SLog.sLogsOn) {
                                        this.mBundle.putBoolean("refresh_session", true);
                                        VineService.this.mLogger.d("Session key refresh and retry complete.");
                                        break;
                                    }
                                    break;
                            }
                    }
                case 3:
                    VineService.this.mLogger.d("Session was logged out.");
                    this.mBundle.putBoolean("logged_out", true);
                    break;
                case 4:
                    VineService.this.mLogger.d("Captcha triggered");
                    this.mBundle.putString("captcha_url", result.getString("captcha_url"));
                    break;
                case 5:
                    VineService.this.mLogger.d("Phone verification challenge triggered");
                    this.mBundle.putString("phone_verification", result.getString("reasonPhrase"));
                    break;
            }
            int statusCode = result.getInt("statusCode");
            String reasonPhrase = result.getString("reasonPhrase");
            if (this.mResponder != null) {
                responderRunnable = VineService.this.new ResponderRunnable(this.mResponder, this.mActionCode, statusCode, reasonPhrase, this.mBundle, this.mStartId);
            } else if (this.mMessenger != null) {
                responderRunnable = VineService.this.new MessengerResponderRunnable(this.mMessenger, this.mActionCode, statusCode, reasonPhrase, this.mBundle, this.mStartId);
            } else {
                responderRunnable = null;
            }
            if (responderRunnable != null) {
                VineService.this.mMainHandler.post(responderRunnable);
            }
        }
    }
}
