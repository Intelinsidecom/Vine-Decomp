package co.vine.android.feedadapter;

import android.content.Context;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineVideoUrlTier;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public final class RequestKeyGetter {
    private final SLogger mLogger;
    private final boolean mNormalVideoPlayable;

    public RequestKeyGetter(Context context, SLogger logger) {
        this.mLogger = logger;
        this.mNormalVideoPlayable = SystemUtil.isNormalVideoPlayable(context) != SystemUtil.PrefBooleanState.FALSE;
    }

    public VideoKey getRequestKey(VinePost post, boolean forceLowKey) {
        int avgSpeed = VideoCache.getCurrentAverageSpeed();
        if (this.mLogger != null) {
            this.mLogger.d("Getting HQ Video: speed {} && can play normal vid {}", Integer.valueOf(avgSpeed), Boolean.valueOf(this.mNormalVideoPlayable));
        }
        return Util.getVideoRequestKey(post, forceLowKey, avgSpeed);
    }

    public void onInvalidRequestKey(VinePost post) {
        if (post.getUrls() != null) {
            for (VineVideoUrlTier tier : post.getUrls()) {
                if (tier != null) {
                    CrashUtil.log("postId " + post.postId + " tier rate " + tier.rate + " format " + tier.format + " url " + tier.url);
                }
            }
        }
        CrashUtil.logException(new IllegalArgumentException(), "Invalid request key for post " + post.postId, new Object[0]);
    }
}
