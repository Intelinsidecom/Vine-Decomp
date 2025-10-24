package co.vine.android.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.StartActivity;
import co.vine.android.VineLoggingException;
import co.vine.android.api.PostInfo;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineParsers;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineSource;
import co.vine.android.api.VineUpload;
import co.vine.android.api.VineUploadParsers;
import co.vine.android.client.AppController;
import co.vine.android.client.SessionManager;
import co.vine.android.network.TransferProgressEvent;
import co.vine.android.provider.VineUploads;
import co.vine.android.provider.VineUploadsDatabaseSQL;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.service.VineServiceConnection;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.UploadManager;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class VineUploadService extends Service implements VineServiceConnection.ServiceResponseHandler {
    private static boolean sShowProgress;
    private static Messenger sUploadResultReceiver;
    private HashMap<String, Bitmap> mBitmaps;
    private ServiceAsyncTask mCurrentTask;
    private Bitmap mDefaultNotificationIcon;
    private boolean mIsBinded;
    private Messenger mMessenger;
    private NotificationManager mNotificationManager;
    private LinkedHashSet<ServiceAsyncTask> mPendingTasks;
    private LinkedHashSet<ServiceAsyncTask> mPendingVMTasks;
    private LinkedList<Integer> mStartIds;
    private UploadManager mUploadManager;
    private final int[] mLock = new int[0];
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();
    private int mNotificationPanelSize = -1;
    private VineServiceConnection mVineServiceConnection = new VineServiceConnection(this, this);

    public static Intent getUploadIntent(Context context, String path, String thumbnail, String reference, boolean isPrivate, long conversationRowId) {
        sShowProgress = false;
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("aUpload");
        intent.putExtra("path", path);
        intent.putExtra("thumbnail", thumbnail);
        intent.putExtra("reference", reference);
        intent.putExtra("is_private", isPrivate);
        intent.putExtra("show_notifications", true);
        intent.putExtra("conversation_row_id", conversationRowId);
        return intent;
    }

    public static Intent getUploadIntent(Context context, String path, String thumbnail, String reference, boolean isPrivate, long conversationRowId, ArrayList<VineSource> sources) {
        sShowProgress = false;
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("aUpload");
        intent.putExtra("path", path);
        intent.putExtra("thumbnail", thumbnail);
        intent.putExtra("reference", reference);
        intent.putExtra("is_private", isPrivate);
        intent.putExtra("show_notifications", true);
        intent.putExtra("conversation_row_id", conversationRowId);
        intent.putExtra("sources", sources);
        return intent;
    }

    public static Intent getDiscardIntent(Context context, String path) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("discard");
        intent.putExtra("path", path);
        return intent;
    }

    public static Intent getDiscardAllIntent(Context context) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("discard_all");
        return intent;
    }

    public static Intent getClearNotificationsIntent(Context context) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("clear_notifications");
        return intent;
    }

    public static Intent getShowProgressIntent(Context context) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("show_notifications");
        return intent;
    }

    public static Intent getPostIntent(Context context, VineUpload upload, boolean isRetry) {
        if (!TextUtils.isEmpty(upload.postInfo)) {
            try {
                JsonParser parser = VineParsers.createParser(upload.postInfo);
                PostInfo postInfo = VineUploadParsers.parsePostInfo(parser);
                Intent result = getPostIntent(context, upload.path, postInfo.caption, postInfo.postToTwitter, postInfo.postToFacebook, postInfo.postToTumblr, postInfo.foursquareVenueId, postInfo.channelId, isRetry, postInfo.entities, postInfo.recipients, postInfo.message, upload.conversationRowId, postInfo.hidden, postInfo.sources);
                result.putExtra("is_private", upload.isPrivate);
                result.putExtra("upload", upload);
                return result;
            } catch (IOException e) {
                throw new RuntimeException("This should never happen.", e);
            }
        }
        return null;
    }

    public static Intent getPostIntent(Context context, String path, String caption, boolean postToTwitter, boolean postToFacebook, boolean postToTumblr, String foursquareVenueId, long channelId, boolean isRetry, ArrayList<VineEntity> entities, ArrayList<VineRecipient> recipients, String message, long conversationRowId, boolean hidden, ArrayList<VineSource> sources) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("bPost");
        intent.putExtra("is_retry", isRetry);
        intent.putExtra("path", path);
        intent.putExtra("description", caption);
        intent.putExtra("postToTwitter", postToTwitter);
        intent.putExtra("postToFacebook", postToFacebook);
        intent.putExtra("postToTumblr", postToTumblr);
        intent.putExtra("channelId", channelId);
        intent.putExtra("foursquareVenueId", foursquareVenueId);
        intent.putExtra("entities", entities);
        intent.putExtra("recipients", recipients);
        intent.putExtra("message", message);
        intent.putExtra("created", System.currentTimeMillis());
        intent.putExtra("conversation_row_id", conversationRowId);
        intent.putExtra("hidden", hidden);
        intent.putExtra("sources", sources);
        return intent;
    }

    public static Intent getVMPostIntent(Context context, String path, boolean isRetry, long conversationRowId, ArrayList<VineRecipient> recipients, String message) {
        return getVMPostIntent(context, path, isRetry, -1L, conversationRowId, recipients, message, -1L, null, null);
    }

    public static Intent getVMPostIntent(Context context, String path, boolean isRetry, long mergedMessageIdForRetry, long conversationRowId, ArrayList<VineRecipient> recipients, String message, long postId, String videoUrl, String thumbUrl) {
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        if (isRetry) {
            intent.setAction("bPost");
            if (TextUtils.isEmpty(path)) {
                VineUpload upload = new VineUpload();
                upload.mergedMessageId = mergedMessageIdForRetry;
                upload.isPrivate = true;
                upload.status = 1;
                upload.conversationRowId = conversationRowId;
                intent.putExtra("upload", upload);
            }
        } else {
            intent.setAction("bMergeAndPost");
        }
        intent.putExtra("is_private", true);
        intent.putExtra("is_retry", isRetry);
        intent.putExtra("path", path);
        intent.putExtra("recipients", recipients);
        intent.putExtra("message", message);
        intent.putExtra("postId", postId);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("thumbUrl", thumbUrl);
        intent.putExtra("created", System.currentTimeMillis());
        intent.putExtra("conversation_row_id", conversationRowId);
        return intent;
    }

    public static Intent getNotifyFailedIntent(Context context) {
        String path = null;
        String[] selArgs = {String.valueOf(2), "0"};
        long ownerId = AppController.getInstance(context).getActiveSessionReadOnly().getUserId();
        Uri contentUri = ContentUris.withAppendedId(VineUploads.Uploads.CONTENT_URI, ownerId);
        Cursor c = context.getContentResolver().query(contentUri, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, "status=? AND is_private=?", selArgs, "_id ASC");
        if (c != null) {
            if (c.moveToLast()) {
                path = c.getString(1);
            }
            c.close();
        } else {
            SLog.e("Content provider didn't return a valid cursor.");
        }
        Intent intent = new Intent(context, (Class<?>) VineUploadService.class);
        intent.setAction("cNotify");
        intent.putExtra("path", path);
        return intent;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mPendingTasks = new LinkedHashSet<>();
        this.mPendingVMTasks = new LinkedHashSet<>();
        this.mUploadManager = new UploadManager(this);
        this.mStartIds = new LinkedList<>();
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.mSessionManager.resetSessions(this);
        this.mBitmaps = new HashMap<>();
        this.mMessenger = new Messenger(new IncomingHandler());
        this.mIsBinded = bindService(new Intent(this, (Class<?>) VineService.class), this.mVineServiceConnection, 1);
    }

    private class IncomingHandler extends Handler {
        private IncomingHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) throws RemoteException {
            SLog.dWithTag("VineUploadService", "Message received, what=" + msg.what + ", replyTo=" + msg.replyTo);
            int what = msg.what;
            switch (what) {
                case 1:
                    Messenger replyTo = msg.replyTo;
                    if (replyTo != null) {
                        Messenger unused = VineUploadService.sUploadResultReceiver = replyTo;
                        Bundle data = new Bundle();
                        ServiceAsyncTask currentTask = VineUploadService.this.mCurrentTask;
                        if (currentTask != null) {
                            data.putLong("conversation_row_id", currentTask.conversationRowId);
                            data.putString("thumbnail", currentTask.thumbnail);
                            boolean isActive = !AsyncTask.Status.FINISHED.equals(currentTask.getStatus());
                            data.putBoolean("is_active", isActive);
                        }
                        try {
                            replyTo.send(Message.obtain(null, 3, data));
                            break;
                        } catch (RemoteException e) {
                            SLog.e("Failed to reply.", (Throwable) e);
                            return;
                        }
                    }
                    break;
                case 2:
                    Messenger unused2 = VineUploadService.sUploadResultReceiver = null;
                    break;
            }
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (this.mIsBinded) {
            unbindService(this.mVineServiceConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void showNotification(NotificationCompat.Builder nb, int id) {
        if (sShowProgress) {
            try {
                this.mNotificationManager.notify("upload_notification", id, nb.build());
            } catch (RuntimeException e) {
                nb.setLargeIcon(null);
                this.mNotificationManager.notify("upload_notification", id, nb.build());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void clearNotification(int id) {
        this.mNotificationManager.cancel("upload_notification", id);
    }

    private synchronized void cancelFailedNofitication() {
        if (UploadManager.uploadListIsEmpty(this)) {
            clearNotification(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopIfNoTasksLeft() {
        if (this.mPendingVMTasks.isEmpty() && this.mPendingTasks.isEmpty() && !isRunning(this.mCurrentTask)) {
            stopService();
        }
    }

    private boolean isRunning(ServiceAsyncTask task) {
        return task != null && task.getStatus().equals(AsyncTask.Status.RUNNING);
    }

    private void stopService() {
        synchronized (this.mLock) {
            Iterator<Integer> it = this.mStartIds.iterator();
            while (it.hasNext()) {
                Integer key = it.next();
                stopSelf(key.intValue());
            }
            this.mStartIds.clear();
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.d("onStartCommand, intent={}", intent);
        synchronized (this.mLock) {
            this.mStartIds.add(Integer.valueOf(startId));
            if (intent == null) {
                return 2;
            }
            String action = intent.getAction();
            String path = intent.getStringExtra("path");
            try {
                if (action == null) {
                    throw new IllegalArgumentException("VineUploadService must be started with an explicit intent; set an action.");
                }
                if ("discard_all".equals(action)) {
                    LinkedHashMap<String, Integer> paths = UploadManager.getAllPaths(this);
                    for (String pathToDiscard : paths.keySet()) {
                        discardTask(pathToDiscard);
                    }
                    cancelFailedNofitication();
                } else if ("show_notifications".equals(action)) {
                    sShowProgress = true;
                } else if ("clear_notifications".equals(action)) {
                    sShowProgress = false;
                    clearNotification(0);
                    clearNotification(1);
                } else {
                    UploadProgressListener listener = null;
                    if (!TextUtils.isEmpty(path)) {
                        boolean showNotifications = intent.getBooleanExtra("show_notifications", false);
                        if (showNotifications) {
                            listener = new NotificationProgressListener(path);
                        } else {
                            listener = new IPCProgressListener(path);
                        }
                    }
                    processCommand(path, action, intent, listener);
                }
                return 1;
            } catch (Throwable e) {
                CrashUtil.log("Upload Service crash on action {} and path {}", action, path);
                throw e;
            }
        }
    }

    private void processCommand(String path, String action, Intent intent, UploadProgressListener listener) {
        VineUpload upload;
        SLog.d("Process command for action={}, path={}", action, path);
        ServiceAsyncTask newTask = new ServiceAsyncTask(path, action, intent, listener);
        if ("aUpload".equals(action)) {
            if (!this.mPendingTasks.contains(newTask) && !this.mPendingVMTasks.contains(newTask)) {
                boolean isPrivate = intent.getBooleanExtra("is_private", false);
                String reference = intent.getStringExtra("reference");
                long conversationRowId = intent.getLongExtra("conversation_row_id", 0L);
                intent.getParcelableArrayListExtra("sources");
                UploadManager.addOrUpdateUpload(this, path, reference, isPrivate, conversationRowId, -1L);
                LinkedHashSet<ServiceAsyncTask> taskQueue = isPrivate ? this.mPendingVMTasks : this.mPendingTasks;
                if (taskQueue.add(newTask)) {
                    SLog.d("Upload task added for path={}", path);
                }
            } else {
                SLog.d("Upload task is already in queue for path={}", path);
            }
        } else if ("bMergeAndPost".equals(action)) {
            PostInfo info = getPostInfoFromIntent(intent);
            if (!TextUtils.isEmpty(path)) {
                upload = UploadManager.getUpload(this, path);
                UploadManager.setPostInfo(this, upload, info);
                updateUploadPrivacy(intent, upload);
            } else {
                upload = new VineUpload();
                upload.postInfo = info.toString();
                upload.isPrivate = true;
                upload.status = 1;
                upload.conversationRowId = intent.getLongExtra("conversation_row_id", -1L);
            }
            preMergeMessage(upload);
        } else if ("bPost".equals(action)) {
            VineUpload upload2 = UploadManager.getUpload(this, path);
            if (upload2 == null) {
                upload2 = (VineUpload) intent.getParcelableExtra("upload");
            }
            if (upload2 == null) {
                SLog.e("Error posting. No upload via path nor via argument: {}.", path);
                return;
            }
            upload2.sources = intent.getParcelableArrayListExtra("sources");
            updateUploadPrivacy(intent, upload2);
            if (TextUtils.isEmpty(upload2.postInfo)) {
                UploadManager.setPostInfo(this, upload2, getPostInfoFromIntent(intent));
            }
            int status = upload2.status;
            switch (status) {
                case 0:
                    ServiceAsyncTask uploadTask = new ServiceAsyncTask(path, "aUpload", intent, listener);
                    LinkedHashSet<ServiceAsyncTask> taskQueue2 = upload2.isPrivate ? this.mPendingVMTasks : this.mPendingTasks;
                    if (!uploadTask.equals(this.mCurrentTask) && taskQueue2.add(uploadTask)) {
                        SLog.d("Upload task added for path={}", path, uploadTask.action);
                    }
                    if (taskQueue2.add(newTask)) {
                        SLog.d("Post task added for path={}", path, newTask.action);
                        break;
                    }
                    break;
                case 1:
                case 2:
                    if (!TextUtils.isEmpty(upload2.path) && upload2.videoUrl == null) {
                        UploadManager.setStatus(this, upload2, 0);
                        startService(getPostIntent(this, upload2, true));
                        return;
                    } else if (!newTask.equals(this.mCurrentTask)) {
                        LinkedHashSet<ServiceAsyncTask> taskQueue3 = upload2.isPrivate ? this.mPendingVMTasks : this.mPendingTasks;
                        newTask.mUpload = upload2;
                        if (taskQueue3.add(newTask)) {
                            SLog.d("Post task added for path={}", path, newTask.action);
                            break;
                        }
                    }
                    break;
                default:
                    SLog.d("Invalid state={} for path={}", Integer.valueOf(upload2.status), path);
                    break;
            }
        } else if ("cNotify".equals(action)) {
            if (!TextUtils.isEmpty(path)) {
                sShowProgress = true;
                SLog.d("Notify failed upload for path={}.", path);
                showFailedNotification(path);
            } else {
                SLog.e("Notify failed upload, but path is null.");
            }
        } else if ("discard".equals(action)) {
            discardTask(path);
            return;
        }
        executeNext();
    }

    private void updateUploadPrivacy(Intent intent, VineUpload upload) {
        upload.isPrivate = intent.getBooleanExtra("is_private", false);
        UploadManager.addOrUpdateUpload(this, upload.path, upload.reference, upload.isPrivate, upload.conversationRowId, -1L);
    }

    public PostInfo getPostInfoFromIntent(Intent intent) {
        String caption = intent.getStringExtra("description");
        boolean postToTwitter = intent.getBooleanExtra("postToTwitter", false);
        boolean postToFacebook = intent.getBooleanExtra("postToFacebook", false);
        boolean postToTumblr = intent.getBooleanExtra("postToTumblr", false);
        long channelId = intent.getLongExtra("channelId", -1L);
        String foursquareVenueId = intent.getStringExtra("foursquareVenueId");
        ArrayList<VineEntity> entities = intent.getParcelableArrayListExtra("entities");
        String message = intent.getStringExtra("message");
        long postId = intent.getLongExtra("postId", -1L);
        String videoUrl = intent.getStringExtra("videoUrl");
        String thumbUrl = intent.getStringExtra("thumbUrl");
        long created = intent.getLongExtra("created", 0L);
        boolean hidden = intent.getBooleanExtra("hidden", false);
        ArrayList<VineRecipient> recipients = (ArrayList) intent.getSerializableExtra("recipients");
        ArrayList<VineSource> sources = intent.getParcelableArrayListExtra("sources");
        return new PostInfo(caption, postToTwitter, postToFacebook, postToTumblr, foursquareVenueId, channelId, entities, message, postId, videoUrl, thumbUrl, created, recipients, hidden, sources);
    }

    private void preMergeMessage(VineUpload upload) {
        Bundle b = new Bundle();
        AppController.injectServiceBundle(b, this.mSessionManager.getCurrentSession());
        b.putParcelable("upload", upload);
        this.mVineServiceConnection.queueAndExecute(93, b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markPreMergedMessageAsUploadFailed(String uploadPath) {
        Bundle b = new Bundle();
        AppController.injectServiceBundle(b, this.mSessionManager.getCurrentSession());
        b.putString("upload_path", uploadPath);
        this.mVineServiceConnection.queueAndExecute(100, b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeNext() {
        LinkedHashSet<ServiceAsyncTask> taskQueue;
        SLog.d("Pending tasks: {}", this.mPendingTasks);
        SLog.d("Pending vm tasks: {}", this.mPendingVMTasks);
        if (this.mCurrentTask != null) {
            SLog.d("mCurrentTask is not null, will not execute next");
            return;
        }
        synchronized (this.mLock) {
            if (!this.mPendingVMTasks.isEmpty()) {
                taskQueue = this.mPendingVMTasks;
            } else {
                taskQueue = this.mPendingTasks;
            }
            if (!taskQueue.isEmpty()) {
                Iterator<ServiceAsyncTask> iterator = taskQueue.iterator();
                ServiceAsyncTask currentTask = iterator.next();
                if (!isRunning(currentTask)) {
                    currentTask.execute(new Void[0]);
                    SLog.d("Started executing task: action={}, path={}", currentTask.action, currentTask.path);
                }
                this.mCurrentTask = currentTask;
                iterator.remove();
            } else {
                new Handler().post(new Runnable() { // from class: co.vine.android.service.VineUploadService.1
                    @Override // java.lang.Runnable
                    public void run() {
                        VineUploadService.this.stopIfNoTasksLeft();
                    }
                });
            }
        }
    }

    private void showFailedNotification(String path) {
        VineUpload upload = UploadManager.getUpload(this, path);
        if (upload != null) {
            SLog.d("Show failed notification for {}, upload expired? {}.", path, Boolean.valueOf(upload.isExpired()));
            NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
            nb.setSmallIcon(R.drawable.ic_vine_status_bar);
            nb.setLargeIcon(setScaledNotificationIcon(upload.thumbnailPath));
            Intent intent = StartActivity.getStartIntent(this, "co.vine.android.UPLOAD_LIST");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            nb.setContentIntent(pendingIntent);
            nb.setContentTitle(getText(R.string.post_failed));
            nb.setContentText(getText(R.string.tap_to_retry_or_cancel));
            nb.setAutoCancel(false);
            showNotification(nb, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap setScaledNotificationIcon(String thumbnailPath) {
        Bitmap bm;
        if (!TextUtils.isEmpty(thumbnailPath)) {
            Bitmap bm2 = this.mBitmaps.get(thumbnailPath);
            if (bm2 == null) {
                if (this.mNotificationPanelSize == -1) {
                    this.mNotificationPanelSize = getResources().getDimensionPixelSize(R.dimen.notification_panel_height);
                }
                Bitmap thumbnailBitmap = BitmapFactory.decodeFile(thumbnailPath);
                if (thumbnailBitmap != null && (bm = Bitmap.createScaledBitmap(thumbnailBitmap, this.mNotificationPanelSize, this.mNotificationPanelSize, true)) != null) {
                    if (this.mBitmaps.size() >= 10) {
                        this.mBitmaps.clear();
                    }
                    this.mBitmaps.put(thumbnailPath, bm);
                    return bm;
                }
            } else {
                return bm2;
            }
        }
        if (this.mDefaultNotificationIcon == null) {
            this.mDefaultNotificationIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        }
        this.mBitmaps.put(thumbnailPath, this.mDefaultNotificationIcon);
        return this.mDefaultNotificationIcon;
    }

    private void discardTask(String path) {
        if (!TextUtils.isEmpty(path)) {
            SLog.d("Discard task for path={}", path);
            ServiceAsyncTask currentTask = this.mCurrentTask;
            if (currentTask != null && TextUtils.equals(currentTask.path, path)) {
                currentTask.setIsDiscarded(true);
                currentTask.cancel(true);
                SLog.d("In-progress task has been cancelled, path={}", path);
            }
            ArrayList<ServiceAsyncTask> tasks = new ArrayList<>();
            Iterator<ServiceAsyncTask> it = this.mPendingTasks.iterator();
            while (it.hasNext()) {
                ServiceAsyncTask task = it.next();
                if (task != null && TextUtils.equals(task.path, path)) {
                    tasks.add(task);
                }
            }
            Iterator<ServiceAsyncTask> it2 = this.mPendingVMTasks.iterator();
            while (it2.hasNext()) {
                ServiceAsyncTask task2 = it2.next();
                if (task2 != null && TextUtils.equals(task2.path, path)) {
                    tasks.add(task2);
                }
            }
            if (!tasks.isEmpty()) {
                SLog.d("Queued task(s) will be cancelled.");
                this.mPendingTasks.removeAll(tasks);
                this.mPendingVMTasks.removeAll(tasks);
            }
            discardUpload(path);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void discardUpload(String path) {
        SLog.d("Discard video: {}", Boolean.valueOf(new File(path).delete()));
        SLog.d("Discard encoded video: {}", Boolean.valueOf(new File(path + UploadManager.getVersionFromPath(path).videoOutputExtension).delete()));
        SLog.d("Discard thumbnail: {}", Boolean.valueOf(new File(RecordConfigUtils.getThumbnailPath(path)).delete()));
        SLog.d("Discard metadata: {}", Boolean.valueOf(new File(RecordConfigUtils.getMetadataPath(path)).delete()));
        UploadManager.deleteUploadRecord(this, path);
        cancelFailedNofitication();
    }

    private class NotificationProgressListener extends UploadProgressListener {
        public NotificationProgressListener(String path) {
            super(path);
        }

        @Override // co.vine.android.network.FileNetworkEntity.ProgressListener
        public void progressChanged(TransferProgressEvent progressEvent) {
            this.currentSize += progressEvent.getBytesTransfered();
            int p = (int) ((this.currentSize * 100) / this.size);
            if (Math.abs(this.lastProgress - p) > 2) {
                this.lastProgress = p;
                updateUploadNotification(this.thumbnail);
            }
        }

        @Override // co.vine.android.service.UploadProgressListener
        public void showPostNotification(String thumbnail) {
            updateNotification(thumbnail, R.string.posting, R.string.almost_done, true);
        }

        private void updateUploadNotification(String thumbnail) {
            updateNotification(thumbnail, R.string.uploading, R.string.uploading_text, true);
        }

        private void updateNotification(String thumbnail, int title, int text, boolean showProgress) {
            SLog.d("Update notification: {}, {}.", Boolean.valueOf(showProgress), Integer.valueOf(this.lastProgress));
            NotificationCompat.Builder nb = new NotificationCompat.Builder(VineUploadService.this);
            nb.setLargeIcon(VineUploadService.this.setScaledNotificationIcon(thumbnail));
            nb.setSmallIcon(R.drawable.ic_vine_status_bar);
            nb.setContentTitle(VineUploadService.this.getText(title));
            nb.setContentText(VineUploadService.this.getText(text));
            nb.setWhen(0L);
            nb.setOngoing(true);
            if (showProgress && this.lastProgress > 0) {
                nb.setProgress(100, this.lastProgress, false);
            } else {
                nb.setProgress(0, 0, true);
            }
            VineUploadService.this.showNotification(nb, 1);
        }
    }

    public class IPCProgressListener extends UploadProgressListener {
        public IPCProgressListener(String path) {
            super(path);
        }

        @Override // co.vine.android.network.FileNetworkEntity.ProgressListener
        public void progressChanged(TransferProgressEvent progressEvent) throws RemoteException {
            this.currentSize += progressEvent.getBytesTransfered();
            int p = (int) ((this.currentSize * 100) / this.size);
            if (VineUploadService.sUploadResultReceiver != null && Math.abs(this.lastProgress - p) > 2) {
                this.lastProgress = p;
                Bundle data = new Bundle();
                data.putDouble("upload_progress", p);
                sendMessageToActivity(Message.obtain(null, 6, data));
            }
        }

        private void sendMessageToActivity(Message msg) throws RemoteException {
            try {
                VineUploadService.sUploadResultReceiver.send(msg);
            } catch (RemoteException e) {
            }
        }
    }

    public class ServiceAsyncTask extends AsyncTask<Void, Void, Boolean> {
        public final String action;
        public final long conversationRowId;
        private int mExecutionStatus;
        private boolean mIsDiscarded;
        private boolean mIsPrivate;
        private boolean mIsRetry;
        private UploadProgressListener mListener;
        public VineUpload mUpload;
        public final String path;
        public final String thumbnail;

        public ServiceAsyncTask(String path, String action, Intent intent, UploadProgressListener listener) {
            this.path = path;
            this.action = action;
            this.conversationRowId = intent.getLongExtra("conversation_row_id", -1L);
            this.mListener = listener;
            this.thumbnail = RecordConfigUtils.getThumbnailPath(path);
            this.mIsRetry = intent.getBooleanExtra("is_retry", false);
            this.mIsPrivate = intent.getBooleanExtra("is_private", false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... params) {
            boolean z;
            synchronized (ServiceAsyncTask.class) {
                try {
                    if (this.mUpload == null) {
                        this.mUpload = UploadManager.getUpload(getContext(), this.path);
                    }
                } catch (Exception e) {
                    SLog.e("Failed to upload.", (Throwable) e);
                }
                if (this.mUpload == null || this.mUpload.status == -1) {
                    z = false;
                } else {
                    if (this.mUpload.path == null) {
                        this.mUpload.path = this.path;
                    }
                    if ("bPost".equals(this.action)) {
                        if (this.mUpload.path == null && !this.mUpload.isPrivate) {
                            CrashUtil.logException(new VineLoggingException("Invalid upload, but we know " + this.thumbnail + " " + this.path));
                            z = false;
                        } else {
                            Bundle b = new Bundle();
                            b.putParcelable("upload", this.mUpload);
                            VineUploadService.this.mVineServiceConnection.queueAndExecute(98, b);
                            if (!this.mIsRetry && this.mListener != null) {
                                this.mListener.showPostNotification(this.thumbnail);
                            }
                            z = true;
                        }
                    } else if ("aUpload".equals(this.action)) {
                        File original = new File(this.path);
                        RecordSessionVersion version = UploadManager.getVersionFromPath(this.path);
                        String finalPath = original.getAbsolutePath();
                        if (this.mListener != null) {
                            this.mListener.size = original.length();
                        }
                        String uri = VineUploadService.this.mUploadManager.upload(this, this.mListener, finalPath, this.path, this.mIsPrivate, version);
                        if (isCancelled()) {
                            z = false;
                        } else if (uri != null) {
                            UploadManager.setUploadTime(getContext(), this.mUpload);
                            UploadManager.setUri(getContext(), this.mUpload, uri);
                            UploadManager.setStatus(getContext(), this.mUpload, 1);
                            if (!TextUtils.isEmpty(this.mUpload.postInfo)) {
                                VineUploadService.this.startService(VineUploadService.getPostIntent(getContext(), this.mUpload, this.mIsRetry));
                            }
                            z = true;
                        } else {
                            SLog.d("Current status: {}", Integer.valueOf(this.mUpload.status));
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                }
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean success) {
            boolean taskSuccessful = success.booleanValue();
            if (!this.mIsDiscarded) {
                if (taskSuccessful) {
                    this.mExecutionStatus = 2;
                    if ("aUpload".equals(this.action) && TextUtils.isEmpty(this.mUpload.postInfo)) {
                        this.mExecutionStatus = 3;
                    }
                } else {
                    this.mExecutionStatus = 1;
                    VineUploadService.this.startService(VineUploadService.getNotifyFailedIntent(getContext()));
                    if (this.mUpload != null) {
                        UploadManager.setStatus(getContext(), this.mUpload, 2, null);
                        if ("aUpload".equals(this.action) && this.mUpload.isPrivate) {
                            VineUploadService.this.markPreMergedMessageAsUploadFailed(this.mUpload.path);
                        }
                    }
                }
                onComplete();
            }
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            SLog.d("Task cancelled. {}, {}.", this.path, this.action);
            if (!this.mIsDiscarded) {
                VineUploadService.this.discardUpload(this.path);
            }
            onComplete();
        }

        private void onComplete() {
            synchronized (VineUploadService.class) {
                VineUploadService.this.clearNotification(1);
                VineUploadService.this.mCurrentTask = null;
                Intent intent = new Intent("co.vine.android.UPLOAD_RESULT");
                intent.putExtra("upload_status", this.mExecutionStatus);
                intent.putExtra("path", this.path);
                intent.putExtra("thumbnail", this.thumbnail);
                VineUploadService.this.sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
                VineUploadService.this.executeNext();
            }
        }

        public Context getContext() {
            return VineUploadService.this;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ServiceAsyncTask)) {
                return false;
            }
            ServiceAsyncTask that = (ServiceAsyncTask) o;
            if (this.path == null ? that.path != null : !this.path.equals(that.path)) {
                return false;
            }
            if (this.action != null) {
                if (this.action.equals(that.action)) {
                    return true;
                }
            } else if (that.action == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = this.path != null ? this.path.hashCode() : 0;
            return (result * 31) + (this.action != null ? this.action.hashCode() : 0);
        }

        public void setIsDiscarded(boolean isDiscarded) {
            this.mIsDiscarded = isDiscarded;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        sUploadResultReceiver = null;
        stopIfNoTasksLeft();
        return false;
    }

    @Override // co.vine.android.service.VineServiceConnection.ServiceResponseHandler
    public void handleServiceResponse(int what, int responseCode, String reasonPhrase, Bundle bundle) {
        switch (what) {
            case 93:
                VineUpload upload = (VineUpload) bundle.getParcelable("upload");
                long messageRowId = bundle.getLong("premerged_message_id");
                upload.mergedMessageId = messageRowId;
                PostInfo info = upload.getPostInfo();
                SLog.d("Pre merge message complete. Path {} Row {} {}", upload.path, Long.valueOf(messageRowId), info.toString());
                if (upload.path != null) {
                    UploadManager.setPostInfo(this, upload, info);
                    UploadManager.setUploadMessageRowId(this, upload.path, messageRowId);
                }
                startService(getPostIntent(this, upload, false));
                break;
            case 98:
                VineUpload upload2 = (VineUpload) bundle.getParcelable("upload");
                if (responseCode == 200) {
                    SLog.d("Post successful.");
                    cancelFailedNofitication();
                    if (!upload2.isPrivate) {
                        SharedPreferences prefs = Util.getDefaultSharedPrefs(this);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putInt("profile_post_count", prefs.getInt("profile_post_count", 0) + 1);
                        e.apply();
                    } else {
                        String videoUrl = bundle.getString("post_url");
                        if (videoUrl != null) {
                            UploadManager.prepopulateCache(this, upload2.path, videoUrl);
                        }
                    }
                    boolean saveUpload = bundle.getBoolean("should_delete_upload", false);
                    if (!TextUtils.isEmpty(upload2.path) && !saveUpload) {
                        discardUpload(upload2.path);
                        break;
                    }
                } else {
                    if (upload2 != null) {
                        UploadManager.setStatus(this, upload2, 2, upload2.captchaUrl);
                    }
                    startService(getNotifyFailedIntent(this));
                    break;
                }
                break;
        }
    }
}
