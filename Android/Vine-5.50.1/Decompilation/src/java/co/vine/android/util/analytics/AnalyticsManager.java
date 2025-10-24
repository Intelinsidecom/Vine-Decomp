package co.vine.android.util.analytics;

import android.content.Context;
import co.vine.android.analytics.TimeTracker;
import co.vine.android.cache.CacheKey;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrossConstants;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class AnalyticsManager {
    public static void onApplicationInit() {
        TimeTracker.onEventHappened("application_start");
    }

    public static void onStart(Context context) {
        FlurryUtils.start(context, CommonUtil.getPackageVersionName(context), BuildUtil.isProduction(), AppController.getInstance(context).getActiveId());
        TimeTracker.onEventHappened("application_start");
    }

    public static void onStop(Context context) {
        FlurryUtils.stop(context);
    }

    public static void onVideoLoaded(VideoViewInterface view) {
        Context context = view.getContext();
        if (TimeTracker.onFirstEventHappened("video_prepared")) {
            long applicationStartToVideoPrepare = TimeTracker.timeBetween("application_start", "video_prepared");
            SLog.b("applicationStartToVideoPrepare {}ms", Long.valueOf(applicationStartToVideoPrepare));
            long activityStartToVideoPrepare = TimeTracker.timeBetween("application_start", "video_prepared");
            SLog.b("activityStartToVideoPrepare {}ms", Long.valueOf(activityStartToVideoPrepare));
        }
        Object key = view.getTag(CrossConstants.RES_CACHE_TAG_KEY);
        if (key instanceof CacheKey) {
            boolean wasSyncedRecently = System.currentTimeMillis() - PrefetchManager.getInstance(context).getLastSync() < 28800000;
            FlurryUtils.trackVideoLoadHits(wasSyncedRecently, ((CacheKey) key).getCacheState());
        }
    }
}
