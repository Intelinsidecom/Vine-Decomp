package co.vine.android.service.components.prefetch;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.network.apache.HttpOperation;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.service.VineDatabaseHelperInterface;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.service.VineServiceInterface;
import co.vine.android.service.components.VineServiceActionHelper;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.FileLoggers;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.BehaviorManager;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.Parcels;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class PrefetchAction extends VineServiceAction {
    private static int sLastRequestCount;
    private static long sLastRequestEnd;

    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) throws InterruptedException {
        int requestCount = HttpOperation.getRequestCount();
        PrefetchManager manager = PrefetchManager.getInstance(request.context);
        if (manager.isClientPrefetchEnabled()) {
            boolean manual = request.b.getBoolean("manual", false);
            manager.notifySyncStart(request.context);
            if (SLog.sLogsOn) {
                FileLoggers.PREFETCH.get().write(ConsoleLoggers.PREFETCH.get(), "Sync was requested.", new Object[0]);
            }
            if (!manual && !manager.isSuitableToSync()) {
                manager.notifySyncEnd(request.context, "Device not ready.", false, false);
            } else {
                manager.onStartActualSyncAction();
                AppController ctr = AppController.getInstance(request.context);
                long startNetworkUsed = NetworkOperation.sNetworkDataUsed;
                long userId = ctr.getActiveId();
                if (userId > 0) {
                    PrefetchManager.injectPrefetchArguments(request.b, PrefetchManager.getMinimumDelay(manager.getInterval()));
                    performTimeLinePrefetch(request.context, request.service, new Bundle(request.b), request.dbHelper, request.networkFactory, request.api, BehaviorManager.getInstance(request.context).getTopUsagesTimelineEndpoints());
                    performActivityPrefetch(request.service, request.context, new Bundle(request.b), userId);
                    performGetUsersMePrefetch(request.service, request.context, new Bundle(request.b));
                    request.service.fetchActivityCounts(userId, request.context, request.b);
                    long waited = 0;
                    while (ctr.hasPendingCacheRequests() && waited < 600000) {
                        try {
                            Thread.sleep(1000L);
                            waited += 1000;
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if (SLog.sLogsOn) {
                    FileLoggers.PREFETCH.get().write(ConsoleLoggers.PREFETCH.get(), "Prefetch used: {} kb", Long.valueOf((NetworkOperation.sNetworkDataUsed - startNetworkUsed) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
                }
                manager.notifySyncEnd(request.context, null, true, manual);
            }
        }
        checkForBursts(HttpOperation.getRequestCount() - requestCount);
        HttpOperation.resetRequestCount();
        return null;
    }

    private void checkForBursts(int requestCount) {
        SLog.i("Prefetch had {} requests", Integer.valueOf(requestCount));
        if (requestCount < 0) {
            CrashUtil.logOrThrowInDebug(new IllegalStateException("There were two requests happening at the same time."));
        }
        if (sLastRequestCount > 50 && SystemClock.elapsedRealtime() - sLastRequestEnd < 60000) {
            CrashUtil.logOrThrowInDebug(new IllegalStateException("You just did 50 requests and now did it again"));
        }
        sLastRequestCount = requestCount;
        sLastRequestEnd = SystemClock.elapsedRealtime();
    }

    private void performTimeLinePrefetch(Context context, VineServiceInterface vineService, Bundle b, VineDatabaseHelperInterface dbHelper, NetworkOperationFactory<VineAPI> networkFactory, VineAPI api, String[] topUsagesTimelineEndpoints) {
        int length = topUsagesTimelineEndpoints.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < length) {
                String endpoint = topUsagesTimelineEndpoints[i2];
                NetworkOperation op = vineService.getPosts(new StringBuilder(VineAPI.getInstance(context).buildUrl(endpoint)), -1, 20, dbHelper, null, null, b, true);
                if (op.isOK()) {
                    performTimeLinePrefetch(context, b, networkFactory, api);
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    private void performTimeLinePrefetch(Context context, Bundle b, NetworkOperationFactory<VineAPI> networkFactory, VineAPI api) {
        ArrayList<VinePost> posts = b.getParcelableArrayList("posts");
        AppController appController = AppController.getInstance(context);
        if (posts != null) {
            ArrayList<ImageKey> images = new ArrayList<>();
            ArrayList<VideoKey> videos = new ArrayList<>();
            ArrayList<Long> users = new ArrayList<>();
            Iterator<VinePost> it = posts.iterator();
            while (it.hasNext()) {
                VinePost post = it.next();
                if (post.avatarUrl != null) {
                    images.add(ImageKey.newDownloadOnlyKey(post.avatarUrl));
                }
                if (post.thumbnailUrl != null) {
                    images.add(ImageKey.newDownloadOnlyKey(post.thumbnailUrl));
                }
                VideoKey videoKey = Util.getVideoRequestKey(post, false, Integer.MAX_VALUE);
                if (videoKey != null) {
                    videoKey.setDownloadOnly(true);
                    videos.add(videoKey);
                }
                users.add(Long.valueOf(post.userId));
            }
            Iterator<ImageKey> it2 = images.iterator();
            while (it2.hasNext()) {
                ImageKey key = it2.next();
                appController.getPhotoBitmap(key);
            }
            Iterator<VideoKey> it3 = videos.iterator();
            while (it3.hasNext()) {
                VideoKey key2 = it3.next();
                appController.getVideoFilePath(key2);
            }
            Iterator<Long> it4 = users.iterator();
            while (it4.hasNext()) {
                Long userId = it4.next();
                StringBuilder url = VineServiceActionHelper.getUserUrl(api, userId.longValue());
                VineParserReader vp = VineParserReader.createParserReader(2);
                NetworkOperation op = networkFactory.createBasicAuthGetRequest(context, url, api, vp);
                VineServiceActionHelper.assignPollingHeader(op, b);
                VineServiceActionHelper.assignCachePolicy(op, b, UrlCachePolicy.NETWORK_ONLY);
                op.execute();
                if (op.isOK()) {
                    VineUser user = (VineUser) vp.getParsedObject();
                    b.putParcelable(PropertyConfiguration.USER, user);
                }
            }
        }
    }

    private static void performGetUsersMePrefetch(VineServiceInterface vineService, Context context, Bundle bundle) {
        vineService.fetchUsersMe(bundle);
        VineUser user = (VineUser) bundle.getParcelable(PropertyConfiguration.USER);
        if (user != null && user.avatarUrl != null) {
            AppController.getInstance(context).getPhotoBitmap(ImageKey.newDownloadOnlyKey(user.avatarUrl));
        }
    }

    private void performActivityPrefetch(VineServiceInterface vineService, Context context, Bundle b, long sessionOwnerId) {
        ArrayList<VineSingleNotification> items;
        ArrayList<VineEverydayNotification> items2;
        b.putBoolean("follow_reqs", true);
        vineService.fetchActivity(sessionOwnerId, context, b);
        VinePagedData<VineEverydayNotification> notifications = (VinePagedData) b.getParcelable("notifications");
        PagedActivityResponse.Data followRequests = (PagedActivityResponse.Data) Parcels.unwrap(b.getParcelable("follow_requests"));
        AppController ctr = AppController.getInstance(context);
        ArrayList<ImageKey> images = new ArrayList<>();
        if (notifications != null && (items2 = notifications.items) != null) {
            Iterator<VineEverydayNotification> it = items2.iterator();
            while (it.hasNext()) {
                VineEverydayNotification notification = it.next();
                if (notification.user != null && notification.user.avatarUrl != null) {
                    images.add(ImageKey.newDownloadOnlyKey(notification.user.avatarUrl));
                }
                if (notification.post != null && notification.post.thumbnailUrl != null) {
                    images.add(ImageKey.newDownloadOnlyKey(notification.post.thumbnailUrl));
                }
            }
        }
        if (followRequests != null && (items = followRequests.items) != null) {
            Iterator<VineSingleNotification> it2 = items.iterator();
            while (it2.hasNext()) {
                VineSingleNotification notification2 = it2.next();
                if (notification2.thumbnailUrl != null) {
                    images.add(ImageKey.newDownloadOnlyKey(notification2.thumbnailUrl));
                }
            }
        }
        Iterator<ImageKey> it3 = images.iterator();
        while (it3.hasNext()) {
            ImageKey key = it3.next();
            ctr.getPhotoBitmap(key);
        }
    }
}
