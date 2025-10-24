package co.vine.android.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import co.vine.android.AppStateProviderSingleton;
import co.vine.android.Settings;
import co.vine.android.StandalonePreference;
import co.vine.android.VineApplication;
import co.vine.android.api.ComplaintMenuOption;
import co.vine.android.api.FoursquareVenue;
import co.vine.android.api.SearchResult;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemWrapper;
import co.vine.android.api.TwitterUser;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VineNotificationSetting;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineSolicitor;
import co.vine.android.api.VineUrlAction;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.api.response.ServerStatus;
import co.vine.android.api.response.VineEditions;
import co.vine.android.cache.CacheFactory;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.PhotoImagesCache;
import co.vine.android.cache.image.PhotoImagesListener;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.cache.video.VideoListener;
import co.vine.android.model.VineTag;
import co.vine.android.network.HttpResult;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.nux.LoginTwitterActivity;
import co.vine.android.player.SaveVideoClicker;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.scribe.VideoDownloadDurationScribeLogger;
import co.vine.android.service.PendingAction;
import co.vine.android.service.VineService;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceConnection;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.loopreporting.LoopReportingComponent;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.ImageUtils;
import co.vine.android.util.RenderscriptUtils;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;
import com.mobileapptracker.MATEvent;
import com.twitter.android.sdk.Twitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.parceler.Parcels;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public abstract class AbstractAppController implements PhotoImagesListener, VideoListener, VineServiceConnection.ServiceResponseHandler {
    public static final IntentFilter ACTION_UPDATED_FILTER = new IntentFilter("action_edition_updated");
    private static final ConcurrentHashMap<Integer, ListenerNotifier> sNotifiers;
    final Context mContext;
    private final PhotoImagesCache mPhotoImagesCache;
    private ServerStatusRunnable mServerStatusRunnable;
    protected final VineServiceConnection mServiceConnection;
    private final Twitter mTwitter;
    private final VideoCache<VineAPI> mVideoCache;
    protected final SessionManager mSessionManager = SessionManager.getSharedInstance();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private ArrayList<AppSessionListener> mListeners = new ArrayList<>();

    static {
        if (SLog.sLogsOn) {
            RecordConfigUtils.FOLDER_ROOT_DIRECT_UPLOAD.mkdirs();
            SaveVideoClicker.setLongPressSaveDir(RecordConfigUtils.FOLDER_ROOT_DIRECT_UPLOAD);
        }
        sNotifiers = new ConcurrentHashMap<>();
    }

    protected AbstractAppController(Context context) {
        this.mContext = context;
        Point p = SystemUtil.getDisplaySize(context);
        int maxImageSize = Math.max(p.x, p.y);
        this.mPhotoImagesCache = CacheFactory.newImageCache(context, maxImageSize, 31457280, RenderscriptUtils.newBlurTool());
        this.mPhotoImagesCache.addListener(this);
        this.mVideoCache = CacheFactory.newVideoCache(context);
        this.mVideoCache.addDownloadDurationListener(new VideoDownloadDurationScribeLogger(context, AppStateProviderSingleton.getInstance(this.mContext)));
        this.mVideoCache.addListener(this);
        this.mTwitter = new Twitter(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET);
        this.mServerStatusRunnable = new ServerStatusRunnable();
        SLog.i("App Controller pid: {}", Integer.valueOf(Process.myPid()));
        this.mServiceConnection = new VineServiceConnection(this.mContext, this);
        context.bindService(new Intent(context, (Class<?>) VineService.class), this.mServiceConnection, 1);
        LoopReportingComponent.registerReceiver(context);
        Components.loopReportingComponent().refreshLoopSendingAlarm(context, System.currentTimeMillis());
        context.registerReceiver(new BroadcastReceiver() { // from class: co.vine.android.client.AbstractAppController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                SLog.d("Received invalidate cache broadcast");
                VideoCache.invalidateCache();
                PhotoImagesCache.invalidateCache();
            }
        }, new IntentFilter("co.vine.android.invalidateCache"), CrossConstants.BROADCAST_PERMISSION, null);
    }

    public Twitter getTwitter() {
        return this.mTwitter;
    }

    public String clearAccount() {
        Bundle b = createServiceBundle();
        return executeServiceAction(115, b);
    }

    public static synchronized void clearNotifiers() {
        sNotifiers.clear();
    }

    public static synchronized void register(int actionCode, ListenerNotifier notifier) {
        ListenerNotifier previous = sNotifiers.put(Integer.valueOf(actionCode), notifier);
        if (previous != null && previous != notifier) {
            throw new IllegalArgumentException("ActionCode already registered");
        }
    }

    void notifyListeners(String reqId, int actionCode, int statusCode, String reasonPhrase, Bundle b) throws NumberFormatException {
        ListenerNotifier notifier;
        Context context = this.mContext;
        int componentActionCode = b.getInt("component_action_code", -1);
        if (componentActionCode != -1 && (notifier = sNotifiers.get(Integer.valueOf(componentActionCode))) != null) {
            notifier.notifyListeners(new ServiceNotification(context, reqId, componentActionCode, statusCode, reasonPhrase, b, this.mListeners));
        }
        switch (actionCode) {
            case 7:
                Iterator<AppSessionListener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    AppSessionListener listener = it.next();
                    listener.onGetTwitterUserComplete(reqId, statusCode, reasonPhrase, (TwitterUser) Parcels.unwrap(b.getParcelable("t_user")), (VineLogin) b.getParcelable(MATEvent.LOGIN));
                }
                break;
            case 9:
                Iterator<AppSessionListener> it2 = this.mListeners.iterator();
                while (it2.hasNext()) {
                    AppSessionListener listener2 = it2.next();
                    listener2.onResetPasswordComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 10:
                VineUser meUser = (VineUser) b.getParcelable(PropertyConfiguration.USER);
                long ownerId = b.getLong("s_owner_id");
                UrlCachePolicy policy = (UrlCachePolicy) b.getParcelable("cache_policy");
                if (meUser != null) {
                    SharedPreferences prefs = Util.getDefaultSharedPrefs(this.mContext);
                    SharedPreferences.Editor e = prefs.edit();
                    e.putString("settings_profile_name", meUser.username);
                    e.putString("settings_profile_description", meUser.description);
                    e.putString("settings_profile_location", meUser.location);
                    e.putString("settings_profile_phone", meUser.phoneNumber);
                    e.putString("settings_profile_email", meUser.email);
                    e.putString("settings_profile_avatar_url", meUser.avatarUrl);
                    e.putInt("profile_follow_count", meUser.followingCount);
                    e.putInt("profile_follower_count", meUser.followerCount);
                    e.putInt("profile_authored_post_count", meUser.authoredPostCount);
                    e.putInt("profile_post_count", meUser.postCount);
                    e.putInt("profile_like_count", meUser.likeCount);
                    e.putLong("profile_loop_count", meUser.loopCount);
                    e.putBoolean("settings_twitter_connected", meUser.twitterConnected == 1);
                    e.putBoolean("settings_follow_editors_picks", meUser.includePromoted == 1);
                    e.putBoolean("settings_private", meUser.isPrivate());
                    e.putBoolean("settings_explicit", meUser.isExplicit());
                    e.putBoolean("profile_email_verified", meUser.isEmailVerified());
                    e.putBoolean("profile_phone_verified", meUser.isPhoneVerified());
                    e.putLong("pref_user_row_id", meUser.id);
                    if (meUser.edition != null) {
                        e.putString("settings_edition", meUser.edition);
                    }
                    e.putBoolean("accept_out_of_network_messages", meUser.acceptsOutOfNetworkConversations);
                    e.putBoolean("enable_address_book", !meUser.disableAddressBook);
                    e.putBoolean("email_discoverable", !meUser.hiddenEmail);
                    e.putBoolean("phone_discoverable", !meUser.hiddenPhoneNumber);
                    e.putBoolean("twitter_discoverable", !meUser.hiddenTwitter);
                    int profileBackground = meUser.profileBackground < 0 ? Settings.DEFAULT_PROFILE_COLOR : meUser.profileBackground;
                    e.putInt("profile_background", profileBackground);
                    e.apply();
                }
                Iterator<AppSessionListener> it3 = this.mListeners.iterator();
                while (it3.hasNext()) {
                    AppSessionListener listener3 = it3.next();
                    listener3.onGetUsersMeComplete(reqId, statusCode, reasonPhrase, ownerId, meUser, policy);
                }
                break;
            case 11:
            case 22:
            case 23:
            case 56:
            case 82:
            case 110:
            case avcodec.AV_CODEC_ID_BINKVIDEO /* 136 */:
            case avcodec.AV_CODEC_ID_IFF_ILBM /* 137 */:
                int count = b.getInt("count", 0);
                int nextPage = b.getInt("next_page", 0);
                int previousPage = b.getInt("previous_page", 0);
                String anchor = b.getString("anchor");
                ArrayList<VineUser> users = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it4 = this.mListeners.iterator();
                while (it4.hasNext()) {
                    AppSessionListener listener4 = it4.next();
                    listener4.onGetUsersComplete(this.mSessionManager.getCurrentSession(), reqId, statusCode, reasonPhrase, count, users, nextPage, previousPage, anchor);
                }
                break;
            case 12:
                int next = b.getInt("next_page");
                String anchor2 = b.getString("anchor");
                ArrayList<VineComment> comments = b.getParcelableArrayList("comments");
                Iterator<AppSessionListener> it5 = this.mListeners.iterator();
                while (it5.hasNext()) {
                    AppSessionListener listener5 = it5.next();
                    listener5.onGetCommentsComplete(reqId, statusCode, reasonPhrase, next, anchor2, comments);
                }
                break;
            case 13:
                onGetPostComplete(reqId, b, statusCode, reasonPhrase);
                break;
            case 14:
                long id = b.getLong("post_id");
                VineComment comment = (VineComment) b.getParcelable("comment_obj");
                String commentText = b.getString("comment");
                Iterator<AppSessionListener> it6 = this.mListeners.iterator();
                while (it6.hasNext()) {
                    AppSessionListener listener6 = it6.next();
                    listener6.onPostCommentComplete(reqId, statusCode, reasonPhrase, id, commentText, comment);
                }
                break;
            case 15:
                int count2 = b.getInt("count", 0);
                ArrayList<VineUser> users2 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it7 = this.mListeners.iterator();
                while (it7.hasNext()) {
                    AppSessionListener listener7 = it7.next();
                    listener7.onGetTwitterFriendsComplete(reqId, statusCode, reasonPhrase, count2, users2);
                }
                break;
            case 16:
                int count3 = b.getInt("count", 0);
                ArrayList<VineUser> users3 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it8 = this.mListeners.iterator();
                while (it8.hasNext()) {
                    AppSessionListener listener8 = it8.next();
                    listener8.onGetAddressFriendsComplete(reqId, statusCode, reasonPhrase, count3, users3);
                }
                break;
            case 17:
                Iterator<AppSessionListener> it9 = this.mListeners.iterator();
                while (it9.hasNext()) {
                    AppSessionListener listener9 = it9.next();
                    listener9.onRemoveUsersComplete(reqId);
                }
                break;
            case 18:
                int count4 = b.getInt("count", 0);
                ArrayList<VineUser> users4 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it10 = this.mListeners.iterator();
                while (it10.hasNext()) {
                    AppSessionListener listener10 = it10.next();
                    listener10.onGetFacebookFriendsComplete(reqId, statusCode, reasonPhrase, count4, users4);
                }
                break;
            case 20:
                VinePagedData<VineEverydayNotification> notifications = (VinePagedData) b.getParcelable("notifications");
                PagedActivityResponse.Data followRequests = (PagedActivityResponse.Data) Parcels.unwrap(b.getParcelable("follow_requests"));
                UrlCachePolicy policyUsed = (UrlCachePolicy) b.getParcelable("cache_policy");
                Iterator<AppSessionListener> it11 = this.mListeners.iterator();
                while (it11.hasNext()) {
                    AppSessionListener listener11 = it11.next();
                    listener11.onGetActivityComplete(reqId, statusCode, reasonPhrase, notifications, followRequests, policyUsed);
                }
                break;
            case 21:
                VineUser user = (VineUser) b.getParcelable(PropertyConfiguration.USER);
                UrlCachePolicy policy2 = (UrlCachePolicy) b.getParcelable("cache_policy");
                Iterator<AppSessionListener> it12 = this.mListeners.iterator();
                while (it12.hasNext()) {
                    AppSessionListener listener12 = it12.next();
                    listener12.onGetUserComplete(reqId, statusCode, reasonPhrase, user, policy2);
                }
                break;
            case 26:
                String avatarUrl = b.getString("avatar_url");
                Iterator<AppSessionListener> it13 = this.mListeners.iterator();
                while (it13.hasNext()) {
                    AppSessionListener listener13 = it13.next();
                    listener13.onUpdateProfileComplete(reqId, statusCode, reasonPhrase, avatarUrl);
                }
                break;
            case 27:
                long postId = b.getLong("post_id");
                Iterator<AppSessionListener> it14 = this.mListeners.iterator();
                while (it14.hasNext()) {
                    AppSessionListener listener14 = it14.next();
                    listener14.onReportPostComplete(reqId, statusCode, reasonPhrase, postId);
                }
                break;
            case 28:
                Iterator<AppSessionListener> it15 = this.mListeners.iterator();
                while (it15.hasNext()) {
                    AppSessionListener listener15 = it15.next();
                    listener15.onGetSinglePostComplete(reqId, statusCode, reasonPhrase, (VinePost) b.getParcelable("post"));
                }
                break;
            case 29:
                SharedPreferences prefs2 = StandalonePreference.LAST_CLEANUP.getPref(this.mContext);
                SharedPreferences.Editor editor = prefs2.edit();
                long lastCleanupTime = System.currentTimeMillis();
                editor.putLong("last_cleanup", lastCleanupTime);
                editor.apply();
                break;
            case 30:
                long commentId = Long.parseLong(b.getString("comment_id"));
                Iterator<AppSessionListener> it16 = this.mListeners.iterator();
                while (it16.hasNext()) {
                    AppSessionListener listener16 = it16.next();
                    listener16.onDeleteCommentComplete(reqId, statusCode, reasonPhrase, commentId);
                }
                break;
            case 31:
                SharedPreferences prefs3 = Util.getDefaultSharedPrefs(this.mContext);
                SharedPreferences.Editor e2 = prefs3.edit();
                int oldCount = prefs3.getInt("profile_post_count", 0);
                long postId2 = b.getLong("post_id");
                e2.putInt("profile_post_count", oldCount > 0 ? oldCount - 1 : 0);
                e2.commit();
                Iterator<AppSessionListener> it17 = this.mListeners.iterator();
                while (it17.hasNext()) {
                    AppSessionListener listener17 = it17.next();
                    listener17.onDeletePostComplete(reqId, postId2, statusCode, reasonPhrase);
                }
                break;
            case 32:
                String username = b.getString("username");
                String token = b.getString("key");
                String secret = b.getString("secret");
                long userId = b.getLong("user_id");
                Iterator<AppSessionListener> it18 = this.mListeners.iterator();
                while (it18.hasNext()) {
                    AppSessionListener listener18 = it18.next();
                    listener18.onConnectTwitterComplete(reqId, statusCode, reasonPhrase, username, token, secret, userId);
                }
                break;
            case 33:
                Iterator<AppSessionListener> it19 = this.mListeners.iterator();
                while (it19.hasNext()) {
                    AppSessionListener listener19 = it19.next();
                    listener19.onDisconnectTwitterComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 36:
                long postId3 = b.getLong("post_id");
                Iterator<AppSessionListener> it20 = this.mListeners.iterator();
                while (it20.hasNext()) {
                    AppSessionListener listener20 = it20.next();
                    listener20.onGetPostIdComplete(reqId, statusCode, reasonPhrase, postId3);
                }
                break;
            case 37:
                Iterator<AppSessionListener> it21 = this.mListeners.iterator();
                while (it21.hasNext()) {
                    AppSessionListener listener21 = it21.next();
                    listener21.onSendFacebookTokenComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 38:
                ServerStatus ss = (ServerStatus) Parcels.unwrap(b.getParcelable("server_status"));
                if (statusCode == 200 && ss != null && !TextUtils.isEmpty(ss.uploadType)) {
                    SharedPreferences prefs4 = Util.getDefaultSharedPrefs(this.mContext);
                    SharedPreferences.Editor e3 = prefs4.edit();
                    e3.putString("server_upload_type", ss.uploadType);
                    e3.commit();
                    break;
                }
                break;
            case 41:
                int count5 = b.getInt("count", 0);
                int nextPage2 = b.getInt("next_page", 0);
                int previousPage2 = b.getInt("previous_page", 0);
                ArrayList<VineUser> users5 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it22 = this.mListeners.iterator();
                while (it22.hasNext()) {
                    AppSessionListener listener22 = it22.next();
                    listener22.onUserSearchComplete(reqId, statusCode, reasonPhrase, count5, nextPage2, previousPage2, users5);
                }
                break;
            case 42:
                int nextPage3 = b.getInt("next_page", 0);
                String anchor3 = b.getString("anchor", null);
                ArrayList<VineTag> tags = b.getParcelableArrayList("tags");
                Iterator<AppSessionListener> it23 = this.mListeners.iterator();
                while (it23.hasNext()) {
                    AppSessionListener listener23 = it23.next();
                    listener23.onTagSearchComplete(reqId, statusCode, reasonPhrase, nextPage3, anchor3, tags);
                }
                break;
            case 44:
                boolean shouldFollow = b.getBoolean("should_follow_editors_picks");
                if (!shouldFollow) {
                    expireTimeline(true, 1, String.valueOf(this.mSessionManager.getCurrentSession().getUserId()));
                } else {
                    fetchNewPosts(getActiveSession(), 20, 0L, 1, 1, null, null, true, String.valueOf(getActiveId()), null, null);
                }
                Iterator<AppSessionListener> it24 = this.mListeners.iterator();
                while (it24.hasNext()) {
                    AppSessionListener listener24 = it24.next();
                    listener24.onUpdateFollowEditorsPicksComplete(reqId, statusCode, reasonPhrase, shouldFollow);
                }
                break;
            case 45:
                int type = b.getInt("type");
                String tag = b.getString("tag_name");
                boolean alsoFetchPosts = b.getBoolean("also_fetch_posts", false);
                if (alsoFetchPosts) {
                    fetchNewPosts(getActiveSession(), 20, getActiveId(), type, 1, null, null, true, tag, null, null);
                    break;
                }
                break;
            case 47:
                boolean userBlocked = b.getBoolean("response_success", false);
                String username2 = b.getString("username");
                Iterator<AppSessionListener> it25 = this.mListeners.iterator();
                while (it25.hasNext()) {
                    AppSessionListener listener25 = it25.next();
                    listener25.onBlockUserComplete(reqId, statusCode, reasonPhrase, userBlocked, username2);
                }
                break;
            case 48:
                boolean userUnblocked = b.getBoolean("response_success", false);
                String username3 = b.getString("username");
                Iterator<AppSessionListener> it26 = this.mListeners.iterator();
                while (it26.hasNext()) {
                    AppSessionListener listener26 = it26.next();
                    listener26.onUnblockUserComplete(reqId, statusCode, reasonPhrase, userUnblocked, username3);
                }
                break;
            case 49:
                Iterator<AppSessionListener> it27 = this.mListeners.iterator();
                while (it27.hasNext()) {
                    AppSessionListener listener27 = it27.next();
                    listener27.onReportUserComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 50:
                long userId2 = b.getLong("user_id");
                boolean accept = b.getBoolean("accept");
                Iterator<AppSessionListener> it28 = this.mListeners.iterator();
                while (it28.hasNext()) {
                    AppSessionListener listener28 = it28.next();
                    listener28.onRespondFollowRequestComplete(reqId, statusCode, reasonPhrase, accept, userId2);
                }
                break;
            case 51:
                boolean explicit = b.getBoolean("explicit");
                Iterator<AppSessionListener> it29 = this.mListeners.iterator();
                while (it29.hasNext()) {
                    AppSessionListener listener29 = it29.next();
                    listener29.onUpdateExplicitComplete(reqId, statusCode, reasonPhrase, explicit);
                }
                break;
            case 52:
                boolean priv = b.getBoolean("priv");
                Iterator<AppSessionListener> it30 = this.mListeners.iterator();
                while (it30.hasNext()) {
                    AppSessionListener listener30 = it30.next();
                    listener30.onUpdatePrivateComplete(reqId, statusCode, reasonPhrase, priv);
                }
                break;
            case 53:
                ArrayList<VineChannel> channels = (ArrayList) Parcels.unwrap(b.getParcelable("channels"));
                Iterator<AppSessionListener> it31 = this.mListeners.iterator();
                while (it31.hasNext()) {
                    AppSessionListener listener31 = it31.next();
                    listener31.onGetChannelsComplete(reqId, statusCode, reasonPhrase, channels);
                }
                break;
            case 58:
                clearFileCache();
                Iterator<AppSessionListener> it32 = this.mListeners.iterator();
                while (it32.hasNext()) {
                    AppSessionListener listener32 = it32.next();
                    listener32.onClearCacheComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 60:
                long userId3 = b.getLong("user_id");
                Iterator<AppSessionListener> it33 = this.mListeners.iterator();
                while (it33.hasNext()) {
                    AppSessionListener listener33 = it33.next();
                    listener33.onGcmRegistrationComplete(reqId, statusCode, reasonPhrase, userId3);
                }
                break;
            case 61:
                deleteSession(b.getString("s_key"));
                break;
            case 67:
                boolean success = b.getBoolean("response_success", false);
                Iterator<AppSessionListener> it34 = this.mListeners.iterator();
                while (it34.hasNext()) {
                    AppSessionListener listener34 = it34.next();
                    listener34.onDeactivateAccountComplete(reqId, statusCode, reasonPhrase, success);
                }
                break;
            case 68:
                boolean success2 = b.getBoolean("response_success", false);
                Iterator<AppSessionListener> it35 = this.mListeners.iterator();
                while (it35.hasNext()) {
                    AppSessionListener listener35 = it35.next();
                    listener35.onEnableUserRepostsComplete(reqId, statusCode, reasonPhrase, success2);
                }
                break;
            case 69:
                boolean success3 = b.getBoolean("response_success", false);
                Iterator<AppSessionListener> it36 = this.mListeners.iterator();
                while (it36.hasNext()) {
                    AppSessionListener listener36 = it36.next();
                    listener36.onDisableUserRepostsComplete(reqId, statusCode, reasonPhrase, success3);
                }
                break;
            case 70:
                Iterator<AppSessionListener> it37 = this.mListeners.iterator();
                while (it37.hasNext()) {
                    AppSessionListener listener37 = it37.next();
                    listener37.onGetEditionsComplete(statusCode, (VineEditions) Parcels.unwrap(b.getParcelable("editions")));
                }
                break;
            case 71:
                String edition = b.getString("edition");
                SLog.d("Edition update success. New edition is {}", edition);
                if (statusCode == 200) {
                    SharedPreferences.Editor editor2 = Util.getDefaultSharedPrefs(this.mContext).edit();
                    editor2.putString("settings_edition", edition);
                    editor2.commit();
                }
                Iterator<AppSessionListener> it38 = this.mListeners.iterator();
                while (it38.hasNext()) {
                    AppSessionListener listener38 = it38.next();
                    listener38.onUpdateEditionComplete(reqId, statusCode, reasonPhrase, edition);
                }
                this.mContext.sendBroadcast(new Intent("action_edition_updated"), CrossConstants.BROADCAST_PERMISSION);
                break;
            case 74:
                int count6 = b.getInt("count", 0);
                Iterator<AppSessionListener> it39 = this.mListeners.iterator();
                while (it39.hasNext()) {
                    AppSessionListener listener39 = it39.next();
                    listener39.onGetMessageInboxComplete(reqId, statusCode, reasonPhrase, count6);
                }
                break;
            case 76:
                Iterator<AppSessionListener> it40 = this.mListeners.iterator();
                while (it40.hasNext()) {
                    AppSessionListener listener40 = it40.next();
                    listener40.onGetConversationComplete(reqId, statusCode, reasonPhrase, b.getLong("conversation_row_id"), b.getInt("resp_code"), b.getBoolean("empty"));
                }
                break;
            case 77:
                long conversationId = b.getLong("conversation_id");
                Iterator<AppSessionListener> it41 = this.mListeners.iterator();
                while (it41.hasNext()) {
                    AppSessionListener listener41 = it41.next();
                    listener41.onGetConversationRemoteIdComplete(conversationId);
                }
                break;
            case 79:
                Iterator<AppSessionListener> it42 = this.mListeners.iterator();
                while (it42.hasNext()) {
                    AppSessionListener listener42 = it42.next();
                    listener42.onIgnoreConversationComplete(reqId, statusCode, reasonPhrase, b.getLong("conversation_row_id"));
                }
                break;
            case 80:
                Iterator<AppSessionListener> it43 = this.mListeners.iterator();
                while (it43.hasNext()) {
                    AppSessionListener listener43 = it43.next();
                    listener43.onDeleteConversationComplete(reqId, statusCode, reasonPhrase, b.getLong("conversation_row_id"));
                }
                break;
            case 81:
                int count7 = b.getInt("count");
                int messagesCount = b.getInt("messages_count");
                int followRequestCount = b.getInt("follow_reqs_c", 0);
                SharedPreferences.Editor editor3 = Util.getDefaultSharedPrefs(this.mContext).edit();
                editor3.putInt("settings_follow_pref_count", followRequestCount);
                editor3.putInt("settings_unread_messages_count", messagesCount);
                UrlCachePolicy policyUsed2 = (UrlCachePolicy) b.getParcelable("cache_policy");
                editor3.apply();
                Iterator<AppSessionListener> it44 = this.mListeners.iterator();
                while (it44.hasNext()) {
                    AppSessionListener listener44 = it44.next();
                    listener44.onGetActivityCountComplete(reqId, statusCode, reasonPhrase, count7, followRequestCount, messagesCount, policyUsed2);
                }
                break;
            case 86:
                long userId4 = b.getLong("user_id");
                Iterator<AppSessionListener> it45 = this.mListeners.iterator();
                while (it45.hasNext()) {
                    AppSessionListener listener45 = it45.next();
                    listener45.onGetUserIdComplete(reqId, statusCode, reasonPhrase, userId4);
                }
                break;
            case 88:
                String avatarUrl2 = b.getString("avatar_url");
                Iterator<AppSessionListener> it46 = this.mListeners.iterator();
                while (it46.hasNext()) {
                    AppSessionListener listener46 = it46.next();
                    listener46.onProfilePhotoUpdatedComplete(reqId, statusCode, reasonPhrase, avatarUrl2);
                }
                break;
            case 89:
                String phone = b.getString("phone");
                Iterator<AppSessionListener> it47 = this.mListeners.iterator();
                while (it47.hasNext()) {
                    AppSessionListener listener47 = it47.next();
                    listener47.onRequestPhoneVerificationComplete(reqId, statusCode, reasonPhrase, phone);
                }
                break;
            case 90:
                Iterator<AppSessionListener> it48 = this.mListeners.iterator();
                while (it48.hasNext()) {
                    AppSessionListener listener48 = it48.next();
                    listener48.onVerifyPhoneNumberComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 91:
                long objectId = b.getLong("conversation_row_id");
                VineRecipient recipient = (VineRecipient) b.getParcelable("recipient");
                long recipientId = b.getLong("recipient_id");
                String username4 = b.getString("username");
                boolean amFollowing = b.getBoolean("am_following");
                Iterator<AppSessionListener> it49 = this.mListeners.iterator();
                while (it49.hasNext()) {
                    AppSessionListener listener49 = it49.next();
                    listener49.onGetConversationRowIdComplete(recipientId, !recipient.isFromUser(), objectId, username4, amFollowing);
                }
                break;
            case 92:
                String email = b.getString("email");
                Iterator<AppSessionListener> it50 = this.mListeners.iterator();
                while (it50.hasNext()) {
                    AppSessionListener listener50 = it50.next();
                    listener50.onRequestEmailVerificationComplete(reqId, statusCode, reasonPhrase, email);
                }
                break;
            case 97:
                ArrayList<VineSingleNotification> notifications2 = (ArrayList) Parcels.unwrap(b.getParcelable("notifications"));
                VineSingleNotification lastNotification = (VineSingleNotification) Parcels.unwrap(b.getParcelable("notification"));
                Iterator<AppSessionListener> it51 = this.mListeners.iterator();
                while (it51.hasNext()) {
                    AppSessionListener listener51 = it51.next();
                    listener51.onMergeNotificationComplete(lastNotification, notifications2);
                }
                break;
            case 98:
                Iterator<AppSessionListener> it52 = this.mListeners.iterator();
                while (it52.hasNext()) {
                    AppSessionListener listener52 = it52.next();
                    listener52.onPostVideoComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 105:
                boolean acceptOon = b.getBoolean("accept_oon");
                Iterator<AppSessionListener> it53 = this.mListeners.iterator();
                while (it53.hasNext()) {
                    AppSessionListener listener53 = it53.next();
                    listener53.onUpdateAcceptOonComplete(reqId, statusCode, reasonPhrase, acceptOon);
                }
                break;
            case 106:
                boolean enableAddressBook = b.getBoolean("enable");
                Iterator<AppSessionListener> it54 = this.mListeners.iterator();
                while (it54.hasNext()) {
                    AppSessionListener listener54 = it54.next();
                    listener54.onUpdateEnableAddressBookComplete(reqId, statusCode, reasonPhrase, enableAddressBook);
                }
                break;
            case 108:
                boolean enable = b.getBoolean("enable");
                int type2 = b.getInt("type");
                Iterator<AppSessionListener> it55 = this.mListeners.iterator();
                while (it55.hasNext()) {
                    AppSessionListener listener55 = it55.next();
                    listener55.onUpdateDiscoverability(reqId, statusCode, reasonPhrase, type2, enable);
                }
                break;
            case 109:
                Iterator<AppSessionListener> it56 = this.mListeners.iterator();
                while (it56.hasNext()) {
                    AppSessionListener listener56 = it56.next();
                    listener56.onVerifyEmailComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case avcodec.AV_CODEC_ID_INDEO5 /* 113 */:
                ArrayList<VineNotificationSetting> notificationSettings = (ArrayList) Parcels.unwrap(b.getParcelable("notificationSettings"));
                Iterator<AppSessionListener> it57 = this.mListeners.iterator();
                while (it57.hasNext()) {
                    AppSessionListener listener57 = it57.next();
                    listener57.onGetNotificationSettingsComplete(reqId, notificationSettings);
                }
                break;
            case avcodec.AV_CODEC_ID_MIMIC /* 114 */:
                Iterator<AppSessionListener> it58 = this.mListeners.iterator();
                while (it58.hasNext()) {
                    AppSessionListener listener58 = it58.next();
                    listener58.onSaveNotificationSettingsComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 116:
                Iterator<AppSessionListener> it59 = this.mListeners.iterator();
                while (it59.hasNext()) {
                    AppSessionListener listener59 = it59.next();
                    listener59.onFollowChannelComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 119:
                Iterator<AppSessionListener> it60 = this.mListeners.iterator();
                while (it60.hasNext()) {
                    AppSessionListener listener60 = it60.next();
                    listener60.onFavoriteUserComplete(reqId, statusCode, reasonPhrase);
                }
                break;
            case 120:
                int nextPage4 = b.getInt("next_page", 0);
                int prevPage = b.getInt("previous_page", 0);
                String anchor4 = b.getString("anchor");
                ArrayList<VineUser> users6 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it61 = this.mListeners.iterator();
                while (it61.hasNext()) {
                    AppSessionListener listener61 = it61.next();
                    listener61.onFetchFavoritePeopleComplete(reqId, statusCode, reasonPhrase, users6, nextPage4, prevPage, anchor4);
                }
                break;
            case 121:
                ArrayList<VineUser> users7 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it62 = this.mListeners.iterator();
                while (it62.hasNext()) {
                    AppSessionListener listener62 = it62.next();
                    listener62.onFetchSuggestedFavoritesComplete(reqId, statusCode, reasonPhrase, users7);
                }
                break;
            case 122:
                ArrayList<VineUser> users8 = b.getParcelableArrayList("users");
                Iterator<AppSessionListener> it63 = this.mListeners.iterator();
                while (it63.hasNext()) {
                    AppSessionListener listener63 = it63.next();
                    listener63.onFetchOnboardingSuggestedFavoritesComplete(reqId, statusCode, reasonPhrase, users8);
                }
                break;
            case 124:
                String oauthToken = b.getString("token");
                String oauthTokenSecret = b.getString("secret");
                Iterator<AppSessionListener> it64 = this.mListeners.iterator();
                while (it64.hasNext()) {
                    AppSessionListener listener64 = it64.next();
                    listener64.onTumblrLoginComplete(reqId, statusCode, reasonPhrase, oauthToken, oauthTokenSecret);
                }
                break;
            case 126:
                long commentId2 = Long.parseLong(b.getString("comment_id"));
                Iterator<AppSessionListener> it65 = this.mListeners.iterator();
                while (it65.hasNext()) {
                    AppSessionListener listener65 = it65.next();
                    listener65.onSpamCommentComplete(reqId, statusCode, reasonPhrase, commentId2);
                }
                break;
            case 130:
                ArrayList<ComplaintMenuOption> options = (ArrayList) b.getSerializable("complaint_menu");
                Iterator<AppSessionListener> it66 = this.mListeners.iterator();
                while (it66.hasNext()) {
                    AppSessionListener listener66 = it66.next();
                    listener66.onComplaintMenu(reqId, statusCode, options);
                }
                break;
            case 131:
                ArrayList<VinePost> posts = b.getParcelableArrayList("posts");
                String anchor5 = b.getString("anchor");
                String searchUrl = b.getString("search_url");
                Iterator<AppSessionListener> it67 = this.mListeners.iterator();
                while (it67.hasNext()) {
                    AppSessionListener listener67 = it67.next();
                    listener67.onSearchPostsComplete(reqId, statusCode, reasonPhrase, posts, anchor5, searchUrl);
                }
                break;
            case 132:
                SearchResult results = (SearchResult) b.getParcelable("search_suggestions");
                String searchUrl2 = b.getString("search_url");
                Iterator<AppSessionListener> it68 = this.mListeners.iterator();
                while (it68.hasNext()) {
                    AppSessionListener listener68 = it68.next();
                    listener68.onFetchSearchSuggestionsComplete(reqId, statusCode, reasonPhrase, results, searchUrl2);
                }
                break;
            case 133:
                SearchResult results2 = (SearchResult) b.getParcelable("search_suggestions");
                String query = b.getString("q");
                String searchUrl3 = b.getString("search_url");
                Iterator<AppSessionListener> it69 = this.mListeners.iterator();
                while (it69.hasNext()) {
                    AppSessionListener listener69 = it69.next();
                    listener69.onFetchSearchTypeaheadComplete(reqId, statusCode, reasonPhrase, query, results2, searchUrl3);
                }
                break;
            case 134:
                SearchResult results3 = (SearchResult) b.getParcelable("search_suggestions");
                String query2 = b.getString("q");
                b.getString("anchor");
                String searchUrl4 = b.getString("search_url");
                Iterator<AppSessionListener> it70 = this.mListeners.iterator();
                while (it70.hasNext()) {
                    AppSessionListener listener70 = it70.next();
                    listener70.onFetchSearchResultsComplete(reqId, statusCode, reasonPhrase, query2, results3, searchUrl4);
                }
                break;
            case 135:
                int respCode = b.getInt("extra_foursquare_resp_code");
                ArrayList<FoursquareVenue> resp = b.getParcelableArrayList("extra_foursquare_resp");
                Iterator<AppSessionListener> it71 = this.mListeners.iterator();
                while (it71.hasNext()) {
                    AppSessionListener listener71 = it71.next();
                    listener71.onFoursquareLocationFetchComplete(respCode, resp);
                }
                break;
            case HttpResponseCode.OK /* 200 */:
            case 201:
                long postId4 = b.getLong("post_id");
                Iterator<AppSessionListener> it72 = this.mListeners.iterator();
                while (it72.hasNext()) {
                    AppSessionListener listener72 = it72.next();
                    listener72.onHidePostComplete(reqId, postId4, statusCode, reasonPhrase);
                }
                break;
            case 3000:
                Iterator<AppSessionListener> it73 = this.mListeners.iterator();
                while (it73.hasNext()) {
                    AppSessionListener listener73 = it73.next();
                    listener73.onRequestEmailDownload(reqId, statusCode, reasonPhrase);
                }
                break;
        }
    }

    public void onGetPostComplete(String reqId, Bundle b, int statusCode, String reasonPhrase) {
        ArrayList<TimelineItem> items;
        int type = b.getInt("type", -1);
        int count = b.getInt("count", 0);
        int size = b.getInt("size", 0);
        String tag = b.getString("tag_name");
        int pageType = b.getInt("page_type");
        int next = b.getInt("next_page");
        int previous = b.getInt("previous_page");
        String anchor = b.getString("anchor");
        String backAnchor = b.getString("back_anchor");
        String title = b.getString("title");
        boolean userInitiated = b.getBoolean("user_init");
        boolean memory = b.getBoolean("in_memory", false);
        boolean network = b.getBoolean("network", false);
        UrlCachePolicy cachePolicy = (UrlCachePolicy) b.getParcelable("cache_policy");
        String timelineUrl = b.getString("timeline_url");
        if (memory) {
            items = TimelineItemWrapper.unbundleTimelineItemList(b, "timeline_items");
        } else {
            items = null;
        }
        if (reqId != null) {
            if (items != null) {
                setOriginUrls(items, timelineUrl);
            }
            List<AppSessionListener> listeners = this.mListeners;
            for (AppSessionListener listener : listeners) {
                listener.onGetTimeLineComplete(reqId, statusCode, reasonPhrase, type, count, memory, items, tag, pageType, next, previous, anchor, backAnchor, userInitiated, size, title, cachePolicy, network, b);
            }
        }
        if (statusCode == 200) {
            determineCleanup(getActiveSession());
        }
    }

    private void setOriginUrls(ArrayList<TimelineItem> items, String timelineUrl) {
        Iterator<TimelineItem> it = items.iterator();
        while (it.hasNext()) {
            TimelineItem item = it.next();
            switch (item.getType()) {
                case POST:
                    ((VinePost) item).originUrl = timelineUrl;
                    break;
                case POST_MOSAIC:
                case USER_MOSAIC:
                    ((VineMosaic) item).originUrl = timelineUrl;
                    break;
                case URL_ACTION:
                    ((VineUrlAction) item).originUrl = timelineUrl;
                    break;
                case SOLICITOR:
                    ((VineSolicitor) item).originUrl = timelineUrl;
                    break;
            }
        }
    }

    @Override // co.vine.android.service.VineServiceConnection.ServiceResponseHandler
    public void handleServiceResponse(int actionCode, int statusCode, String reasonPhrase, Bundle b) throws NumberFormatException {
        String reqId = b.getString("rid");
        if (reqId != null) {
            if (Components.authComponent().isLoggedOut(actionCode, b)) {
                this.mSessionManager.clearLocalDataForSessionLogout(this.mContext);
                return;
            }
            PendingAction action = this.mServiceConnection.remove(reqId);
            if (b.getBoolean("refresh_session", false)) {
                refreshSessionKey(b.getString("s_key"));
            }
            String captchaUrl = b.getString("captcha_url");
            b.remove("captcha_url");
            if (captchaUrl != null) {
                Iterator<AppSessionListener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    AppSessionListener listener = it.next();
                    listener.onCaptchaRequired(reqId, captchaUrl, action);
                }
                return;
            }
            String verifyMsg = b.getString("phone_verification");
            b.remove("phone_verification");
            if (verifyMsg != null) {
                Iterator<AppSessionListener> it2 = this.mListeners.iterator();
                while (it2.hasNext()) {
                    AppSessionListener listener2 = it2.next();
                    listener2.onPhoneVerificationRequired(reqId, verifyMsg, action);
                }
            }
            notifyListeners(reqId, actionCode, statusCode, reasonPhrase, b);
        }
    }

    public Bitmap getPhotoBitmap(ImageKey key) {
        if (!Util.isUrlLocal(key.url)) {
            return this.mPhotoImagesCache.getBitmap(getActiveId(), key);
        }
        String path = Uri.parse(key.url).getPath();
        return BitmapFactory.decodeFile(path);
    }

    public String getVideoFilePath(VideoKey key) {
        return !Util.isUrlLocal(key.url) ? this.mVideoCache.getFile(getActiveId(), key, false) : Uri.parse(key.url).getPath();
    }

    public String fetchUsersMe(long userId, UrlCachePolicy cachePolicy) {
        Bundle b = createServiceBundle();
        b.putLong("s_owner_id", userId);
        b.putParcelable("cache_policy", cachePolicy);
        return executeServiceAction(10, b);
    }

    public String updateProfile(Session session, String name, String description, String location, String email, String phone, Uri avatar, int profileBackgroundColor) {
        Bundle b = createServiceBundle(session);
        b.putString("a_name", name);
        b.putString("desc", Util.cleanse(description));
        b.putString("location", Util.cleanse(location));
        b.putString("email", email);
        b.putString("phone", phone);
        b.putInt("color_int", profileBackgroundColor);
        if (avatar != null) {
            b.putParcelable("uri", avatar);
        }
        return executeServiceAction(26, b);
    }

    public String updateAcceptOon(boolean acceptOon) {
        Bundle b = createServiceBundle();
        b.putBoolean("accept_oon", acceptOon);
        return executeServiceAction(105, b);
    }

    public String updateEnableAddressBook(boolean enable) {
        Bundle b = createServiceBundle();
        b.putBoolean("enable", enable);
        return executeServiceAction(106, b);
    }

    public String updateDiscoverability(int type, boolean enable) {
        Bundle b = createServiceBundle();
        b.putBoolean("enable", enable);
        b.putInt("type", type);
        return executeServiceAction(108, b);
    }

    public String updateProfilePhoto(Session session, Uri avatar) {
        Bundle b = createServiceBundle(session);
        b.putParcelable("uri", avatar);
        return executeServiceAction(88, b);
    }

    public String fetchUser(long userId, UrlCachePolicy cachePolicy) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userId);
        b.putParcelable("cache_policy", cachePolicy);
        return executeServiceAction(21, b);
    }

    public String fetchFollowers(long userId, int page, String anchor) {
        Bundle b = createServiceBundle();
        b.putLong("profile_id", userId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(22, b);
    }

    public String fetchFollowing(long userId, int page, String anchor) {
        Bundle b = createServiceBundle();
        b.putLong("profile_id", userId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(23, b);
    }

    public String fetchNotificationSettings() {
        Bundle b = createServiceBundle();
        return executeServiceAction(avcodec.AV_CODEC_ID_INDEO5, b);
    }

    public String fetchFriends(int pageType, int type) {
        Bundle b = createServiceBundle();
        b.putInt("page_type", pageType);
        b.putInt("type", type);
        return executeServiceAction(82, b);
    }

    public String fetchPostLikers(Session session, long postId, int page, String anchor) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(11, b);
    }

    public String fetchNotificationUsers(Session session, long notificationId, int page, String anchor) {
        Bundle b = createServiceBundle(session);
        b.putLong("notification_id", notificationId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(110, b);
    }

    public String fetchReviners(Session session, long postId, int page, String anchor) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(56, b);
    }

    public String fetchActivity(Session session, int page, String anchor, boolean followRequests, boolean clearPendingCount, UrlCachePolicy cachePolicy) {
        Bundle b = createServiceBundle(session);
        b.putInt("page", page);
        b.putBoolean("follow_reqs", followRequests);
        b.putBoolean("clear", clearPendingCount);
        b.putString("anchor", anchor);
        b.putParcelable("cache_policy", cachePolicy);
        return executeServiceAction(20, b);
    }

    public String fetchActivityCounts() {
        Bundle b = createServiceBundle();
        b.putLong("s_owner_id", getActiveId());
        b.putParcelable("cache_policy", UrlCachePolicy.FORCE_REFRESH);
        return executeServiceAction(81, b);
    }

    public String removeFollowPosts(Session session, long userId) {
        Bundle b = createServiceBundle(session);
        b.putLong("user_id", userId);
        return executeServiceAction(34, b);
    }

    public String fetchServerStatus() {
        Bundle b = createServiceBundle();
        return executeServiceAction(38, b);
    }

    public Session getActiveSession() {
        return this.mSessionManager.getCurrentSession();
    }

    public Session getActiveSessionReadOnly() {
        return this.mSessionManager.getCurrentSession();
    }

    public long getActiveId() {
        return getActiveSessionReadOnly().getUserId();
    }

    public boolean isPendingRequest(String reqId) {
        return this.mServiceConnection.isPending(reqId);
    }

    public void followVineOnTwitter() {
        executeServiceRequest(123, new Bundle());
    }

    public String resetPassword(String email) {
        Bundle b = createServiceBundle();
        b.putString("email", email);
        return executeServiceAction(9, b);
    }

    public String fetchTwitterUser(VineLogin mLogin) {
        Bundle b = createServiceBundle();
        b.putParcelable(MATEvent.LOGIN, mLogin);
        return executeServiceAction(7, b);
    }

    public String fetchNewPosts(Session session, int size, long userId, int type, int page, String anchor, String backAnchor, boolean userInitiated, String tag, String sort, Uri data) {
        return fetchPosts(session, size, userId, type, page, anchor, backAnchor, userInitiated, tag, sort, data, UrlCachePolicy.FORCE_REFRESH, false);
    }

    public String fetchPosts(Session session, int size, long userId, int type, int page, String anchor, String backAnchor, boolean userInitiated, String tag, String sort, Uri data, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
        return fetchPosts(session, size, userId, type, page, anchor, backAnchor, userInitiated, tag, sort, data, cachePolicy, forceReplacePosts, -1L);
    }

    public String fetchPosts(Session session, int size, long userId, int type, int page, String anchor, String backAnchor, boolean userInitiated, String tag, String sort, Uri data, UrlCachePolicy cachePolicy, boolean forceReplacePosts, long postId) {
        Bundle b = createServiceBundle(session);
        b.putInt("size", size);
        b.putInt("type", type);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        b.putString("back_anchor", backAnchor);
        b.putLong("profile_id", userId);
        b.putParcelable("cache_policy", cachePolicy);
        b.putBoolean("user_init", userInitiated);
        b.putBoolean("replace_posts", forceReplacePosts);
        b.putLong("post_id", postId);
        if (tag != null) {
            b.putString("tag_name", tag);
        }
        if (sort != null) {
            b.putString("sort", sort);
        }
        b.putParcelable("data", data);
        return executeServiceAction(13, b);
    }

    public String fetchPost(Session session, long postId, UrlCachePolicy cachePolicy) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        b.putParcelable("cache_policy", cachePolicy);
        return executeServiceAction(28, b);
    }

    public String fetchPostId(Session session, String shareId) {
        Bundle b = createServiceBundle(session);
        b.putString("post_share_id", shareId);
        return executeServiceAction(36, b);
    }

    public String fetchComments(long postId, int page, String anchor, int size) {
        Bundle b = createServiceBundle();
        b.putLong("post_id", postId);
        b.putInt("page", page);
        b.putString("anchor", anchor);
        b.putInt("size", size);
        return executeServiceAction(12, b);
    }

    public String postComment(long postId, long repostId, Session session, String comment, ArrayList<VineEntity> entities) {
        Bundle b = createServiceBundle();
        b.putLong("post_id", postId);
        b.putLong("repost_id", repostId);
        b.putLong("user_id", session.getUserId());
        b.putString("username", session.getScreenName());
        b.putString("comment", Util.cleanse(comment));
        b.putString("avatar_url", session.getAvatarUrl());
        b.putParcelableArrayList("entities", entities);
        return executeServiceAction(14, b);
    }

    public String deleteComment(long postId, long repostId, String commentId) {
        Bundle b = createServiceBundle();
        b.putLong("post_id", postId);
        b.putLong("repost_id", repostId);
        b.putString("comment_id", commentId);
        return executeServiceAction(30, b);
    }

    public String spamComment(String commentId, String postId, String event) {
        Bundle b = createServiceBundle();
        b.putString("comment_id", commentId);
        b.putString("post_id", postId);
        if (!TextUtils.isEmpty(event)) {
            b.putString("event", event);
        }
        return executeServiceAction(126, b);
    }

    public String reportPost(Session session, long postId, String event) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        if (!TextUtils.isEmpty(event)) {
            b.putString("event", event);
        }
        return executeServiceAction(27, b);
    }

    public String reportPost(Session session, long postId) {
        return reportPost(session, postId, null);
    }

    public String deletePost(Session session, long postId) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        return executeServiceAction(31, b);
    }

    public String hidePost(Session session, long postId) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        return executeServiceAction(HttpResponseCode.OK, b);
    }

    public String unhidePost(Session session, long postId) {
        Bundle b = createServiceBundle(session);
        b.putLong("post_id", postId);
        return executeServiceAction(201, b);
    }

    public String sendFacebookToken(String token) {
        Bundle b = createServiceBundle();
        b.putString("facebook_token", token);
        return executeServiceAction(37, b);
    }

    public String sendGcmRegId(String regId, long userId) {
        Bundle b = createServiceBundle();
        b.putString("gcmRegId", regId);
        b.putLong("user_id", userId);
        return executeServiceAction(60, b);
    }

    public String clearGcmRegId(String regId, long userId, String key) {
        Bundle b = createServiceBundle();
        b.putString("gcmRegId", regId);
        b.putLong("user_id", userId);
        b.putString("s_key", key);
        return executeServiceAction(61, b);
    }

    public void mergePushNotification(VineSingleNotification notification) {
        Bundle b = new Bundle();
        b.putParcelable("notification", Parcels.wrap(notification));
        executeServiceAction(97, b);
    }

    public void clearPushNotifications(int notificationDisplayId) {
        removeNotification(notificationDisplayId, -1L);
    }

    public String deleteSession(String key) {
        Bundle b = createServiceBundle();
        b.putString("s_key", key);
        return executeServiceAction(64, b);
    }

    public static void startTwitterAuthWithFinish(Twitter twitter, Activity activity, Integer loginMode) {
        try {
            if (!twitter.startAuthActivityForResult(activity, 1)) {
                if (loginMode == null) {
                    activity.startActivityForResult(LoginTwitterActivity.getIntentWithFinish(activity), 2);
                } else {
                    activity.startActivityForResult(LoginTwitterActivity.getIntentWithFinishAndMode(activity, loginMode.intValue()), 2);
                }
            }
        } catch (SecurityException e) {
            activity.startActivityForResult(LoginTwitterActivity.getIntentWithFinish(activity), 2);
        }
    }

    public static void startTwitterAuthWithFinish(Twitter twitter, Activity activity) {
        startTwitterAuthWithFinish(twitter, activity, null);
    }

    public String fetchTwitterConnectedFollowing(int page, String anchor) {
        Bundle b = createServiceBundle();
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(avcodec.AV_CODEC_ID_IFF_ILBM, b);
    }

    public String fetchTwitterFriends(Session session, String key, String secret) {
        Bundle b = createServiceBundle(session);
        b.putString("key", key);
        b.putString("secret", secret);
        return executeServiceAction(15, b);
    }

    public String fetchFacebookFriends(Session session) {
        Bundle b = createServiceBundle(session);
        return executeServiceAction(18, b);
    }

    public String fetchAddressFriends(Session session) {
        Bundle b = createServiceBundle(session);
        return executeServiceAction(16, b);
    }

    public String sendAddressBook() {
        Bundle b = createServiceBundle();
        return executeServiceAction(101, b);
    }

    public String removeUsers(Session session, int type) {
        Bundle b = createServiceBundle(session);
        b.putInt("type", type);
        return executeServiceAction(17, b);
    }

    public String connectTwitter(Session session, String username, String token, String secret, long userId) {
        Bundle b = createServiceBundle(session);
        b.putString("username", username);
        b.putString("key", token);
        b.putString("secret", secret);
        b.putLong("user_id", userId);
        return executeServiceAction(32, b);
    }

    public String disconnectTwitter(Session session) {
        Bundle b = createServiceBundle(session);
        return executeServiceAction(33, b);
    }

    public String acceptFollowRequest(Session session, long userId) {
        Bundle b = createServiceBundle(session);
        b.putLong("user_id", userId);
        b.putBoolean("accept", true);
        return executeServiceAction(50, b);
    }

    public String rejectFollowRequest(Session session, long userId) {
        Bundle b = createServiceBundle(session);
        b.putLong("user_id", userId);
        b.putBoolean("accept", false);
        return executeServiceAction(50, b);
    }

    public String updatePrivate(Session session, boolean priv) {
        Bundle b = createServiceBundle(session);
        b.putBoolean("priv", priv);
        return executeServiceAction(52, b);
    }

    public String updateExplicit(Session session, boolean explicit) {
        Bundle b = createServiceBundle(session);
        b.putBoolean("explicit", explicit);
        return executeServiceAction(51, b);
    }

    public String updateEdition(Session session, String editionCode) {
        Bundle b = createServiceBundle(session);
        b.putString("edition", editionCode);
        SLog.d("Updating edition code to {}", editionCode);
        return executeServiceAction(71, b);
    }

    public void determineServerStatus() {
        SharedPreferences prefs = Util.getDefaultSharedPrefs(this.mContext);
        long lastRefresh = prefs.getLong("server_upload_last_refresh", 0L);
        long elapsedTime = System.currentTimeMillis() - lastRefresh;
        long remainingTime = System.currentTimeMillis() - elapsedTime;
        if (lastRefresh <= 0 || elapsedTime > 3600000) {
            fetchServerStatus();
            prefs.edit().putLong("server_upload_last_refresh", System.currentTimeMillis()).apply();
            this.mHandler.removeCallbacks(this.mServerStatusRunnable);
            this.mHandler.postDelayed(this.mServerStatusRunnable, 3600000L);
            return;
        }
        this.mHandler.removeCallbacks(this.mServerStatusRunnable);
        Handler handler = this.mHandler;
        ServerStatusRunnable serverStatusRunnable = this.mServerStatusRunnable;
        if (remainingTime <= 0) {
            remainingTime = 3600000;
        }
        handler.postDelayed(serverStatusRunnable, remainingTime);
    }

    public void mergeSinglePost(Bundle bundle) {
        if (isLoggedInReadOnly()) {
            executeServiceAction(57, injectServiceBundle(bundle, getActiveSession()));
        }
    }

    public void getEditions() {
        Bundle b = createServiceBundle();
        executeServiceAction(70, b);
    }

    public void onLowMemory() {
        Iterator<AppSessionListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            AppSessionListener listener = it.next();
            listener.onLowMemory();
        }
    }

    public void onTrimMemory(int level) {
        Iterator<AppSessionListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            AppSessionListener listener = it.next();
            listener.onTrimMemory(level);
        }
    }

    public void fetchConversationRowIdFromUserRemoteId(long userId, int networkType) {
        Bundle b = createServiceBundle();
        b.putInt("network", networkType);
        b.putParcelable("recipient", VineRecipient.fromUser(userId));
        b.putLong("recipient_id", userId);
        executeServiceAction(91, b);
    }

    public void fetchConversationRowIdFromSingleRecipient(VineRecipient recipient, int networkType) {
        Bundle b = createServiceBundle();
        b.putInt("network", networkType);
        b.putParcelable("recipient", recipient);
        b.putLong("recipient_id", recipient.contactId);
        executeServiceAction(91, b);
    }

    public void deleteMessage(long id) {
        Bundle b = createServiceBundle();
        b.putLong("message_id", id);
        executeServiceAction(95, b);
    }

    public void removeNotification(int notificationId, long conversationRowId) {
        Bundle b = createServiceBundle();
        b.putInt("notification_id", notificationId);
        b.putLong("conversation_row_id", conversationRowId);
        executeServiceAction(102, b);
    }

    public void retryMessagesInConversationRowId(long conversationRowId) {
        Bundle b = createServiceBundle();
        b.putLong("conversation_row_id", conversationRowId);
        executeServiceAction(103, b);
    }

    public void clearInboxPageCursors() {
        Bundle b = createServiceBundle();
        executeServiceAction(104, b);
    }

    public void clearImageCacheFromMemory() {
        this.mPhotoImagesCache.clearMemory();
    }

    public void executeServiceRequest(int actionCode, Bundle b) {
        executeServiceAction(actionCode, b);
    }

    public String saveNotificationSettings(HashMap<String, String> map) {
        Bundle b = createServiceBundle();
        b.putSerializable("notificationSettings", map);
        return executeServiceAction(avcodec.AV_CODEC_ID_MIMIC, b);
    }

    public boolean hasPendingCacheRequests() {
        return this.mPhotoImagesCache.hasPendingItems() || this.mVideoCache.hasPendingItems();
    }

    public void cancelAllPendingRequests() {
        this.mServiceConnection.cancelAll();
    }

    class ServerStatusRunnable implements Runnable {
        ServerStatusRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AbstractAppController.this.determineServerStatus();
        }
    }

    public void determineCleanup(Session session) {
        SharedPreferences prefs = StandalonePreference.LAST_CLEANUP.getPref(this.mContext);
        long lastCleanupTime = prefs.getLong("last_cleanup", 0L);
        if (lastCleanupTime > 0) {
            if (System.currentTimeMillis() - lastCleanupTime > 3600000) {
                performCleanup(session, Long.toString(lastCleanupTime));
            }
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            long cleanupTime = System.currentTimeMillis();
            editor.putLong("last_cleanup", cleanupTime);
            editor.apply();
        }
    }

    private String performCleanup(Session session, String timeAnchor) {
        Bundle b = createServiceBundle(session);
        b.putString("anchor", timeAnchor);
        return executeServiceAction(29, b);
    }

    public boolean isLoggedIn() {
        return this.mSessionManager.isLoggedIn();
    }

    public boolean isLoggedInReadOnly() {
        return this.mSessionManager.isLoggedIn();
    }

    public void addListener(AppSessionListener listener) {
        listener.setEnabled(true);
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void removeListener(AppSessionListener listener) {
        listener.setEnabled(false);
        this.mListeners.remove(listener);
    }

    public void refreshSessionKey(String newSessionKey) {
        Session activeSession = this.mSessionManager.getCurrentSession();
        if (activeSession != null) {
            activeSession.setSessionKey(newSessionKey);
        }
    }

    public static void clearFileCache(Context context) {
        Util.removeCache(context);
        ImageUtils.removeFiles(context);
    }

    public void clearFileCache() {
        clearFileCache(this.mContext);
    }

    public String clearDbCache(boolean notify) {
        Bundle bundle = createServiceBundle();
        bundle.putBoolean("notify", notify);
        return executeServiceAction(58, bundle);
    }

    public String clearDbAll() {
        Bundle bundle = createServiceBundle();
        return executeServiceAction(59, bundle);
    }

    public String expireTimeline(boolean fetchPosts, int type, String tag) {
        Bundle bundle = createServiceBundle();
        bundle.putBoolean("also_fetch_posts", fetchPosts);
        bundle.putInt("type", type);
        bundle.putString("tag_name", tag);
        return executeServiceAction(45, bundle);
    }

    public String deactivateAccount() {
        Bundle bundle = createServiceBundle();
        return executeServiceAction(67, bundle);
    }

    public String fetchChannels(int pageType) {
        Bundle b = createServiceBundle();
        b.putInt("page_type", pageType);
        return executeServiceAction(53, b);
    }

    public void updateCredentials(String key) {
        getActiveSession().setSessionKey(key);
    }

    public String executeServiceAction(int actionCode, Bundle bundle) {
        CrashUtil.set("Last Service Action Code", actionCode);
        return this.mServiceConnection.queueAndExecute(actionCode, bundle);
    }

    public String executeComponentServiceAction(int actionCode, Bundle bundle) {
        VineApplication app = VineApplication.getInstance();
        if (app != null) {
            VineServiceAction action = app.getServiceActionProvider().getAction(actionCode);
            CrashUtil.set("Last Component Service Action", action.getClass().getSimpleName());
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("component_action_code", actionCode);
        bundle.putLong("s_owner_id", getActiveId());
        return this.mServiceConnection.queueAndExecute(2000, bundle);
    }

    public Bundle createServiceBundle() {
        return createServiceBundle(getActiveSession());
    }

    private Bundle createServiceBundleReadOnly() {
        return createServiceBundle(getActiveSessionReadOnly());
    }

    public static Bundle injectServiceBundle(Bundle bundle, Session session) {
        bundle.putString("s_key", session.getSessionKey());
        bundle.putLong("s_owner_id", session.getUserId());
        bundle.putString("a_name", session.getScreenName());
        bundle.putString("email", session.getUsername());
        return bundle;
    }

    public Bundle createServiceBundle(Session session) {
        Bundle bundle = new Bundle();
        return injectServiceBundle(bundle, session);
    }

    @Override // co.vine.android.cache.image.PhotoImagesListener
    public void onPhotoImageLoaded(PhotoImagesCache cache, HashMap<ImageKey, UrlImage> image) {
        ArrayList<AppSessionListener> listeners = new ArrayList<>(this.mListeners);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            AppSessionListener listener = listeners.get(i);
            if (listener.isEnabled()) {
                listener.onPhotoImageLoaded(image);
            }
        }
    }

    @Override // co.vine.android.cache.image.PhotoImagesListener
    public void onPhotoImageError(PhotoImagesCache cache, ImageKey key, HttpResult result) {
        ArrayList<AppSessionListener> listeners = new ArrayList<>(this.mListeners);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            AppSessionListener listener = listeners.get(i);
            if (listener.isEnabled()) {
                listener.onPhotoImageError(key, result);
            }
        }
    }

    @Override // co.vine.android.cache.video.VideoListener
    public void onVideoPathObtained(VideoCache cache, HashMap<VideoKey, UrlVideo> videos) {
        ArrayList<AppSessionListener> listeners = new ArrayList<>(this.mListeners);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            AppSessionListener listener = listeners.get(i);
            if (listener.isEnabled()) {
                listener.onVideoPathObtained(videos);
            }
        }
    }

    @Override // co.vine.android.cache.video.VideoListener
    public void onVideoPathError(VideoCache cache, VideoKey key, HttpResult result) {
        ArrayList<AppSessionListener> listeners = new ArrayList<>(this.mListeners);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            AppSessionListener listener = listeners.get(i);
            if (listener.isEnabled()) {
                listener.onVideoPathError(key, result);
            }
        }
    }

    public String searchUsersSectioned(String query, int page) {
        Bundle b = createServiceBundle();
        b.putString("q", query);
        b.putInt("page", page);
        return executeServiceAction(avcodec.AV_CODEC_ID_BINKVIDEO, b);
    }

    public String searchTags(String query, String anchor) {
        Bundle b = createServiceBundle();
        b.putString("q", query);
        b.putString("anchor", anchor);
        return executeServiceAction(42, b);
    }

    public String blockUser(long userToBlock, String username) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", getActiveId());
        b.putLong("block_user_id", userToBlock);
        if (!TextUtils.isEmpty(username)) {
            b.putString("username", username);
        }
        return executeServiceAction(47, b);
    }

    public String unblockUser(long userToUnblock, String username) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", getActiveId());
        b.putLong("block_user_id", userToUnblock);
        if (!TextUtils.isEmpty(username)) {
            b.putString("username", username);
        }
        return executeServiceAction(48, b);
    }

    public String reportPerson(long userToReport, String event) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userToReport);
        if (!TextUtils.isEmpty(event)) {
            b.putString("event", event);
        }
        return executeServiceAction(49, b);
    }

    public String enableReposts(long userId) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userId);
        return executeServiceAction(68, b);
    }

    public String disableReposts(long userId) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userId);
        return executeServiceAction(69, b);
    }

    public String fetchConversations(int pageType, boolean userInitiated, int networkType) {
        Bundle b = createServiceBundleReadOnly();
        b.putInt("page_type", pageType);
        b.putBoolean("user_init", userInitiated);
        b.putInt("network", networkType);
        return executeServiceAction(74, b);
    }

    public String fetchConversation(int pageType, boolean userInitiated, long conversationId, long conversationRowId, boolean prefetch) {
        Bundle b = createServiceBundleReadOnly();
        b.putInt("page_type", pageType);
        b.putBoolean("user_init", userInitiated);
        b.putLong("conversation_id", conversationId);
        b.putLong("conversation_row_id", conversationRowId);
        b.putBoolean("prefetch", prefetch);
        return executeServiceAction(76, b);
    }

    public String getConversationRemoteId(long conversationRowId) {
        Bundle b = new Bundle();
        b.putLong("conversation_row_id", conversationRowId);
        return executeServiceAction(77, b);
    }

    public String ignoreConversation(long conversationRowId) {
        Bundle b = createServiceBundle();
        b.putLong("conversation_row_id", conversationRowId);
        return executeServiceAction(79, b);
    }

    public String deleteConversation(long conversationRowId) {
        Bundle b = createServiceBundle();
        b.putLong("conversation_row_id", conversationRowId);
        return executeServiceAction(80, b);
    }

    public String fetchUserId(ArrayList<String> vanityUrlSegments) {
        Bundle b = createServiceBundle();
        b.putStringArrayList("vanity_url", vanityUrlSegments);
        return executeServiceAction(86, b);
    }

    public String requestPhoneVerification(Session session, String phone, long id) {
        Bundle b = createServiceBundle(session);
        b.putString("phone", phone);
        b.putLong("user_id", id);
        return executeServiceAction(89, b);
    }

    public String requestEmailVerification(Session session, String email, long id) {
        Bundle b = createServiceBundle(session);
        b.putString("email", email);
        b.putLong("user_id", id);
        return executeServiceAction(92, b);
    }

    public String verifyPhoneNumber(Session session, String code) {
        Bundle b = createServiceBundle(session);
        b.putString("key", code);
        return executeServiceAction(90, b);
    }

    public String verifyEmail(Session session, String code) {
        Bundle b = createServiceBundle(session);
        b.putString("key", code);
        return executeServiceAction(109, b);
    }

    public String retryRequest(int actionCode, Bundle b) {
        injectServiceBundle(b, getActiveSession());
        b.remove("captcha_url");
        return executeServiceAction(actionCode, b);
    }

    public String generateReqIdForCanceledCaptcha() {
        return this.mServiceConnection.generateRequestId();
    }

    public void failRequest(String requestId, int actionCode, Bundle b) throws NumberFormatException {
        b.putString("rid", requestId);
        notifyListeners(requestId, actionCode, 455, null, b);
    }

    public String setHideProfileReposts(long userId, boolean block) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userId);
        b.putBoolean("block_profile_reposts", block);
        return executeServiceAction(87, b);
    }

    public void clearUnreadMessageCount(long conversationRowId) {
        Bundle b = new Bundle();
        b.putLong("conversation_row_id", conversationRowId);
        executeServiceAction(94, b);
        fetchActivityCounts();
    }

    public void followChannel(long channelId, boolean following) {
        Bundle b = createServiceBundle();
        b.putLong("channelId", channelId);
        b.putBoolean("following", following);
        executeServiceAction(116, b);
    }

    public void markChannelLastUsed(long channelId) {
        Bundle b = new Bundle();
        b.putLong("channelId", channelId);
        executeServiceAction(118, b);
    }

    public void favoriteUser(long userId, boolean favorite, boolean isFollowing) {
        Bundle b = createServiceBundle();
        b.putLong("user_id", userId);
        b.putBoolean("favorite", favorite);
        b.putBoolean("following", isFollowing);
        executeServiceAction(119, b);
    }

    public String fetchFavoriteUsers(int page, String anchor) {
        Bundle b = createServiceBundle();
        b.putInt("page", page);
        b.putString("anchor", anchor);
        return executeServiceAction(120, b);
    }

    public String fetchOnboardingSuggestedFavoriteUsers() {
        return executeServiceAction(122, createServiceBundle());
    }

    public String tumblrXauthLogin(String username, String password) {
        Bundle b = createServiceBundle();
        b.putString("username", username);
        b.putString("pass", password);
        return executeServiceAction(124, b);
    }

    public String fetchComplaintsMenu() {
        return executeServiceAction(130, createServiceBundle());
    }

    public void fetchNearbyLocations(double latitude, double longitude, String query) {
        Bundle b = new Bundle();
        b.putDouble("foursquare_latitude", latitude);
        b.putDouble("foursquare_longitude", longitude);
        b.putString("foursquare_location_query", query);
        executeServiceRequest(135, b);
    }

    public String fetchPostSearchResults(String query, String anchor) {
        Bundle b = createServiceBundle();
        b.putString("q", query);
        b.putString("anchor", anchor);
        return executeServiceAction(131, b);
    }

    public String fetchSearchSuggestions() {
        return executeServiceAction(132, createServiceBundle());
    }

    public String fetchUnifiedSearchAutocomplete(String query) {
        Bundle bundle = createServiceBundle();
        bundle.putString("q", query);
        return executeServiceAction(133, bundle);
    }

    public String fetchUnifiedSearchResults(String query) {
        Bundle bundle = createServiceBundle();
        bundle.putString("q", query);
        return executeServiceAction(134, bundle);
    }

    public String requestDownloadEmail(String email) {
        Bundle b = createServiceBundle();
        b.putString("email_download", email);
        return executeServiceAction(3000, b);
    }
}
