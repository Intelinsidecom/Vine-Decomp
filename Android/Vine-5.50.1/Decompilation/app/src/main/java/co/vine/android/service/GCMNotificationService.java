package co.vine.android.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.StartActivity;
import co.vine.android.api.VineParsers;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.network.HttpResult;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SparseArray;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import com.edisonwang.android.slog.SLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class GCMNotificationService extends Service {
    private static final IntentFilter sConversationStateFilter = new IntentFilter();
    private AppController mAppController;
    private AppSessionListener mAppSessionListener;
    private Messenger mCameraMessenger;
    private Handler mHandler;
    private boolean mIsCameraServiceConnected;
    private ArrayList<Integer> mStartIds;
    private final SparseArray<NotificationGroup> mPendingNotificationGroups = new SparseArray<>();
    private final HashMap<Long, BuildableNotification> mNotificationsToFire = new HashMap<>();
    private final ServiceConnection mCameraConnection = new ServiceConnection() { // from class: co.vine.android.service.GCMNotificationService.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            GCMNotificationService.this.mCameraMessenger = new Messenger(service);
            GCMNotificationService.this.mIsCameraServiceConnected = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            GCMNotificationService.this.mCameraMessenger = null;
            GCMNotificationService.this.mIsCameraServiceConnected = false;
        }
    };
    private final Runnable mFetchActivityCountRunnable = new Runnable() { // from class: co.vine.android.service.GCMNotificationService.2
        @Override // java.lang.Runnable
        public void run() {
            GCMNotificationService.this.mAppController.fetchActivityCounts();
        }
    };
    private final Runnable mFetchConversationsRunnable = new Runnable() { // from class: co.vine.android.service.GCMNotificationService.3
        @Override // java.lang.Runnable
        public void run() {
            GCMNotificationService.this.mAppController.fetchConversations(1, false, 1);
        }
    };
    private final BroadcastReceiver mConversationStateReceiver = new BroadcastReceiver() { // from class: co.vine.android.service.GCMNotificationService.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            synchronized (GCMNotificationService.this.mNotificationsToFire) {
                SLog.d("Received camera request result: {}.", intent);
                if ("co.vine.android.camera.request.conversation.state.on".equals(intent.getAction())) {
                    long conversationRowId = intent.getLongExtra("co.vine.android.camera.request.conversation.state.id", -1L);
                    GCMNotificationService.this.mNotificationsToFire.remove(Long.valueOf(conversationRowId));
                    GCMNotificationService.this.updateNotification(intent.getIntExtra("notification_group_id", 2), conversationRowId);
                }
                for (BuildableNotification buildableNotification : GCMNotificationService.this.mNotificationsToFire.values()) {
                    buildableNotification.build();
                }
                GCMNotificationService.this.mNotificationsToFire.clear();
            }
        }
    };

    static {
        sConversationStateFilter.addAction("co.vine.android.camera.request.conversation.state.on");
        sConversationStateFilter.addAction("co.vine.android.camera.request.conversation.state.off");
    }

    private static class NotificationGroup {
        public ImageKey avatarImageKey;
        public NotificationCompat.Builder builder;
        public Bitmap icon;
        public VineSingleNotification newestNotification;
        public final int notificationGroupId;
        public long notificationObjectId;
        public ArrayList<VineSingleNotification> notifications;
        public final HashMap<ImageKey, Boolean> thumbnailImageKeys = new HashMap<>();
        public final HashMap<VideoKey, Boolean> videoKeys = new HashMap<>();

        public NotificationGroup(int notificationGroupId, VineSingleNotification notification) {
            this.notificationGroupId = notificationGroupId;
            this.newestNotification = notification;
        }

        public boolean isReady() {
            if (this.builder == null) {
                SLog.d("Builder is not ready, this group has been reset by notification merge.");
                return false;
            }
            if (this.icon == null) {
                SLog.d("Notification not ready because avatar is pending.");
                return false;
            }
            for (ImageKey key : this.thumbnailImageKeys.keySet()) {
                if (!this.thumbnailImageKeys.get(key).booleanValue()) {
                    SLog.d("Notification not ready because thumbnails are pending.");
                    return false;
                }
            }
            for (VideoKey key2 : this.videoKeys.keySet()) {
                if (!this.videoKeys.get(key2).booleanValue()) {
                    SLog.d("Notification not ready because videos are pending.");
                    return false;
                }
            }
            return true;
        }

        public void reset() {
            this.builder = null;
            this.notifications = null;
            this.avatarImageKey = null;
            this.thumbnailImageKeys.clear();
            this.videoKeys.clear();
            this.icon = null;
        }
    }

    public NotificationGroup getNotificationGroup(VineSingleNotification notification) {
        int groupId;
        if (notification.isMessaging()) {
            groupId = 2;
        } else if (!TextUtils.isEmpty(notification.onboard)) {
            groupId = 3;
        } else {
            groupId = 1;
        }
        NotificationGroup group = this.mPendingNotificationGroups.get(groupId);
        if (group == null) {
            NotificationGroup group2 = new NotificationGroup(groupId, notification);
            this.mPendingNotificationGroups.put(groupId, group2);
            return group2;
        }
        return group;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        SLog.dWithTag("GCMNotifService", "Service created.");
        this.mAppController = AppController.getInstance(this);
        this.mAppSessionListener = new GCMNotificationServiceListener();
        this.mAppController.addListener(this.mAppSessionListener);
        this.mStartIds = new ArrayList<>();
        this.mHandler = new Handler();
        registerReceiver(this.mConversationStateReceiver, sConversationStateFilter, CrossConstants.BROADCAST_PERMISSION, null);
        bindService(new Intent(this, (Class<?>) ResourceService.class), this.mCameraConnection, 1);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.dWithTag("GCMNotifService", "onStartCommand, startId=" + startId + ", startIds=" + this.mStartIds.toString());
        this.mStartIds.add(Integer.valueOf(startId));
        this.mHandler.removeCallbacksAndMessages(null);
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                postStop();
            } else {
                switch (action) {
                    case "co.vine.android.notifications.ACTION_NEW_NOTIFICATION":
                        if (this.mAppController.isLoggedInReadOnly()) {
                            processNewNotification(intent);
                            break;
                        }
                        break;
                    case "co.vine.android.notifications.ACTION_CLEAR_NOTIFICATIONS":
                        clearNotification(intent.getIntExtra("notification_group_id", 1));
                        break;
                    case "co.vine.android.notifications.ACTION_UPDATE_NOTIFICATIONS":
                        int notificationId = intent.getIntExtra("notification_group_id", 2);
                        long conversationRowId = intent.getLongExtra("conversation_row_id", -1L);
                        updateNotification(notificationId, conversationRowId);
                        break;
                    default:
                        postStop();
                        break;
                }
            }
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotification(int notificationId, long conversationRowId) {
        this.mAppController.removeNotification(notificationId, conversationRowId);
    }

    private void postStop() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendMessageDelayed(Message.obtain((Handler) null, 1), 5000L);
    }

    private void processNewNotification(Intent intent) {
        String payload = intent.getStringExtra("notifications");
        if (!TextUtils.isEmpty(payload)) {
            try {
                VineSingleNotification notification = VineParsers.parsePushNotification(payload);
                if (notification == null) {
                    CrashUtil.log("Invalid GCM notification payload, payload=" + payload);
                } else if (notification.recipientUserId != this.mAppController.getActiveId()) {
                    SLog.e("This message is intended for someone else {}.", Long.valueOf(notification.recipientUserId));
                } else {
                    FlurryUtils.trackNotificationReceived(notification.toString());
                    if (notification.isMessaging()) {
                        this.mHandler.removeCallbacks(this.mFetchConversationsRunnable);
                        this.mHandler.postDelayed(this.mFetchConversationsRunnable, 2500L);
                    }
                    this.mAppController.mergePushNotification(notification);
                    this.mHandler.removeCallbacks(this.mFetchActivityCountRunnable);
                    this.mHandler.postDelayed(this.mFetchActivityCountRunnable, 2500L);
                    return;
                }
                postStop();
            } catch (IOException e) {
                CrashUtil.logException(e, "Exception while parsing GCM push notification payload.", new Object[0]);
                postStop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildNotification(VineSingleNotification notification, NotificationGroup group) throws Resources.NotFoundException {
        String message;
        String message2;
        int w = getResources().getDimensionPixelSize(R.dimen.notification_panel_height);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_vine_status_bar);
        Intent notificationIntent = new Intent(this, (Class<?>) StartActivity.class);
        notificationIntent.putExtra("notification", Parcels.wrap(notification));
        switch (group.notificationGroupId) {
            case 2:
                notificationIntent.setAction("co.vine.android.MESSAGE_NOTIFICATION_PRESSED");
                break;
            case 3:
                notificationIntent.setAction("co.vine.android.ONBOARD_NOTIFICATION_PRESSED");
                notificationIntent.putExtra("notification_type_id", notification.notificationTypeId);
                builder.setContentTitle(notification.title);
                builder.setContentText(Util.toPrettyComment(notification));
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_intro);
                group.icon = Bitmap.createScaledBitmap(icon, w, w, true);
                if (icon != group.icon) {
                    icon.recycle();
                    break;
                }
                break;
            default:
                notificationIntent.setAction("co.vine.android.ACTIVITY_NOTIFICATION_PRESSED");
                if (!TextUtils.isEmpty(notification.url)) {
                    notificationIntent.setData(Uri.parse(notification.url));
                }
                builder.setContentTitle(getString(R.string.activity_notification_big_title));
                builder.setContentText(Util.toPrettyComment(notification));
                break;
        }
        if (!TextUtils.isEmpty(notification.avatarUrl)) {
            group.avatarImageKey = new ImageKey(notification.avatarUrl, w, w, true);
            group.icon = this.mAppController.getPhotoBitmap(group.avatarImageKey);
        } else {
            group.icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        }
        if (group.notificationGroupId == 2) {
            Iterator<ImageKey> it = notification.imageKeys.iterator();
            while (it.hasNext()) {
                ImageKey imageKey = it.next();
                if (group.thumbnailImageKeys.get(imageKey) == null) {
                    group.thumbnailImageKeys.put(imageKey, false);
                    if (this.mAppController.getPhotoBitmap(imageKey) != null) {
                        group.thumbnailImageKeys.put(imageKey, true);
                    }
                }
            }
            Iterator<VideoKey> it2 = notification.videoKeys.iterator();
            while (it2.hasNext()) {
                VideoKey videoKey = it2.next();
                if (group.videoKeys.get(videoKey) == null) {
                    group.videoKeys.put(videoKey, false);
                    if (this.mAppController.getVideoFilePath(videoKey) != null) {
                        group.videoKeys.put(videoKey, true);
                    }
                }
            }
            if (notification.messageCount > 1) {
                String username = Util.getUsernameFromActivity(notification.getComment());
                if (username != null) {
                    message2 = getString(R.string.message_notification_title_plural, new Object[]{username, Integer.valueOf(notification.messageCount)});
                } else {
                    message2 = notification.getComment();
                }
            } else {
                message2 = Util.stripUsernameEntities(notification.getComment());
            }
            builder.setContentTitle(message2);
            SLog.d("Notification - " + message2);
            builder.setSubText(getString(R.string.message_notification_subtext));
        }
        ArrayList<VineSingleNotification> unclearedNotifications = group.notifications;
        if (unclearedNotifications == null) {
            unclearedNotifications = new ArrayList<>();
        }
        if (unclearedNotifications.size() > 1) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            if (group.notificationGroupId == 1) {
                inboxStyle.setBigContentTitle(getString(R.string.activity_notification_big_title));
            } else {
                inboxStyle.setBigContentTitle(getString(R.string.message_notification_big_title));
            }
            Iterator<VineSingleNotification> it3 = unclearedNotifications.iterator();
            while (it3.hasNext()) {
                VineSingleNotification unclearedNotification = it3.next();
                if (group.notificationGroupId == 2) {
                    String username2 = Util.getUsernameFromActivity(unclearedNotification.getComment());
                    if (username2 != null) {
                        if (notification.messageCount > 1) {
                            message = getString(R.string.message_notification_title_plural, new Object[]{username2, Integer.valueOf(unclearedNotification.messageCount)});
                        } else {
                            message = getString(R.string.message_notification_title_single, new Object[]{username2});
                        }
                    } else {
                        message = unclearedNotification.getComment();
                    }
                } else {
                    message = Util.toPrettyComment(unclearedNotification);
                }
                inboxStyle.addLine(message);
            }
            builder.setStyle(inboxStyle);
            builder.setNumber(unclearedNotifications.size());
            builder.setSubText(getString(R.string.activity_notification_subtext));
        }
        if (group.notificationGroupId == 2 && unclearedNotifications.size() == 1) {
            SLog.d("Notification - converstaion.");
            notificationIntent.setData(Uri.parse(String.valueOf(notification.conversationRowId)));
            notificationIntent.putExtra("username", notification.username);
            notificationIntent.putExtra("user_id", notification.userId);
        } else {
            SLog.d("Notification - home - " + unclearedNotifications.size());
        }
        SharedPreferences sp = Util.getDefaultSharedPrefs(this);
        int defaults = 0;
        if (sp.getBoolean("pref_notifications_sound", true)) {
            if (group.notificationGroupId == 2) {
                builder.setSound(Util.getUriFromResouce(this, R.raw.vm_notification));
            } else {
                String ringtonePath = sp.getString("pref_notifications_ringtone", "");
                if (!TextUtils.isEmpty(ringtonePath)) {
                    builder.setSound(Uri.parse(ringtonePath));
                } else {
                    defaults = 0 | 1;
                }
            }
        }
        if (sp.getBoolean("pref_notifications_vibrate", false)) {
            defaults |= 2;
        }
        if (sp.getBoolean("pref_notifications_light", true)) {
            defaults |= 4;
        }
        builder.setDefaults(defaults);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 134217728));
        group.builder = builder;
        if (group.icon != null) {
            showNotificationIfReady(group);
            this.mPendingNotificationGroups.remove(group.notificationGroupId);
        }
    }

    private void clearNotification(int notificationId) {
        NotificationManager manager = (NotificationManager) getSystemService("notification");
        manager.cancel("gcmGenericNotification", notificationId);
        this.mAppController.removeNotification(notificationId, -1L);
        postStop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean showNotificationIfReady(NotificationGroup group) {
        if (!group.isReady()) {
            return false;
        }
        group.builder.setLargeIcon(group.icon);
        NotificationManager manager = (NotificationManager) getSystemService("notification");
        try {
            manager.notify("gcmGenericNotification", group.notificationGroupId, group.builder.build());
            FlurryUtils.trackNotificationShown(group.newestNotification.toString());
        } catch (NullPointerException e) {
        } catch (SecurityException e2) {
        }
        return true;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (this.mIsCameraServiceConnected) {
            unbindService(this.mCameraConnection);
        }
        unregisterReceiver(this.mConversationStateReceiver);
        if (this.mAppSessionListener != null) {
            this.mAppController.removeListener(this.mAppSessionListener);
        }
    }

    public static Intent getUpdateNotificationIntent(Context context, int notificationId, long conversationRowId) {
        Intent intent = new Intent(context, (Class<?>) GCMNotificationService.class);
        intent.setAction("co.vine.android.notifications.ACTION_UPDATE_NOTIFICATIONS");
        intent.putExtra("notification_group_id", notificationId);
        intent.putExtra("conversation_row_id", conversationRowId);
        return intent;
    }

    public static Intent getClearNotificationIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, (Class<?>) GCMNotificationService.class);
        intent.setAction("co.vine.android.notifications.ACTION_CLEAR_NOTIFICATIONS");
        intent.putExtra("notification_group_id", notificationId);
        return intent;
    }

    private class GCMNotificationServiceListener extends AppSessionListener {
        private GCMNotificationServiceListener() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathError(VideoKey erroredVideoKey, HttpResult result) {
            ArrayList<Integer> toRemove = new ArrayList<>();
            Iterator<Integer> it = GCMNotificationService.this.mPendingNotificationGroups.iterator();
            while (it.hasNext()) {
                int groupId = it.next().intValue();
                NotificationGroup group = (NotificationGroup) GCMNotificationService.this.mPendingNotificationGroups.get(groupId);
                if (group.videoKeys.containsKey(erroredVideoKey)) {
                    group.videoKeys.put(erroredVideoKey, true);
                }
                if (GCMNotificationService.this.showNotificationIfReady(group)) {
                    toRemove.add(Integer.valueOf(groupId));
                }
            }
            GCMNotificationService.this.mPendingNotificationGroups.removeAll(toRemove);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageError(ImageKey erroredImageKey, HttpResult result) {
            ArrayList<Integer> toRemove = new ArrayList<>();
            Iterator<Integer> it = GCMNotificationService.this.mPendingNotificationGroups.iterator();
            while (it.hasNext()) {
                int groupId = it.next().intValue();
                NotificationGroup group = (NotificationGroup) GCMNotificationService.this.mPendingNotificationGroups.get(groupId);
                if (group.avatarImageKey != null && erroredImageKey.equals(group.avatarImageKey)) {
                    group.avatarImageKey = null;
                }
                if (group.thumbnailImageKeys.containsKey(erroredImageKey)) {
                    group.thumbnailImageKeys.put(erroredImageKey, true);
                }
                if (GCMNotificationService.this.showNotificationIfReady(group)) {
                    toRemove.add(Integer.valueOf(groupId));
                }
            }
            GCMNotificationService.this.mPendingNotificationGroups.removeAll(toRemove);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            ArrayList<Integer> toRemove = new ArrayList<>();
            Iterator<Integer> it = GCMNotificationService.this.mPendingNotificationGroups.iterator();
            while (it.hasNext()) {
                int groupId = it.next().intValue();
                NotificationGroup group = (NotificationGroup) GCMNotificationService.this.mPendingNotificationGroups.get(groupId);
                for (VideoKey videoKey : group.videoKeys.keySet()) {
                    if (videos.containsKey(videoKey)) {
                        group.videoKeys.put(videoKey, true);
                    }
                }
                if (GCMNotificationService.this.showNotificationIfReady(group)) {
                    toRemove.add(Integer.valueOf(groupId));
                }
            }
            GCMNotificationService.this.mPendingNotificationGroups.removeAll(toRemove);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            UrlImage image;
            ArrayList<Integer> toRemove = new ArrayList<>();
            if (!images.isEmpty()) {
                Iterator<Integer> it = GCMNotificationService.this.mPendingNotificationGroups.iterator();
                while (it.hasNext()) {
                    int groupId = it.next().intValue();
                    NotificationGroup group = (NotificationGroup) GCMNotificationService.this.mPendingNotificationGroups.get(groupId);
                    if (group.avatarImageKey != null && images.containsKey(group.avatarImageKey) && (image = images.get(group.avatarImageKey)) != null && image.isValid()) {
                        group.icon = image.bitmap;
                    }
                    for (ImageKey thumbnailKey : group.thumbnailImageKeys.keySet()) {
                        if (images.containsKey(thumbnailKey)) {
                            group.thumbnailImageKeys.put(thumbnailKey, true);
                        }
                    }
                    if (GCMNotificationService.this.showNotificationIfReady(group)) {
                        toRemove.add(Integer.valueOf(groupId));
                    }
                }
            }
            GCMNotificationService.this.mPendingNotificationGroups.removeAll(toRemove);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onMergeNotificationComplete(VineSingleNotification lastNotification, ArrayList<VineSingleNotification> notifications) throws Resources.NotFoundException, RemoteException {
            if (lastNotification != null) {
                NotificationGroup group = GCMNotificationService.this.getNotificationGroup(lastNotification);
                if (group.notificationObjectId > 0 && lastNotification.conversationRowId != group.notificationObjectId) {
                    group.reset();
                    group.notificationObjectId = lastNotification.conversationRowId;
                }
                group.notifications = notifications;
                if (lastNotification.isMessaging()) {
                    GCMNotificationService.this.checkWhetherToShowMessagingNotification(lastNotification, group);
                } else {
                    GCMNotificationService.this.buildNotification(lastNotification, group);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkWhetherToShowMessagingNotification(VineSingleNotification lastNotification, NotificationGroup event) throws RemoteException {
        if (this.mCameraMessenger != null) {
            try {
                this.mCameraMessenger.send(Message.obtain((Handler) null, 101));
                synchronized (this.mNotificationsToFire) {
                    BuildableNotification buildable = new BuildableNotification(lastNotification, event);
                    this.mNotificationsToFire.put(Long.valueOf(lastNotification.conversationRowId), buildable);
                }
                return;
            } catch (RemoteException e) {
                SLog.e("Failed to send request to camera service.");
            }
        }
        postStop();
    }

    private class BuildableNotification {
        final NotificationGroup group;
        final VineSingleNotification notification;

        public BuildableNotification(VineSingleNotification notification, NotificationGroup group) {
            this.notification = notification;
            this.group = group;
        }

        public void build() throws Resources.NotFoundException {
            GCMNotificationService.this.buildNotification(this.notification, this.group);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
