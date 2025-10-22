package co.vine.android.feedadapter.v2;

import android.content.Context;
import android.os.Handler;
import co.vine.android.player.HasVideoPlayerAdapter;
import co.vine.android.util.LoopManager;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public final class LoopIncrementer {
    private final FeedAdapterItems mItems;
    private final LoopManager mLoopManager;
    private final HasVideoPlayerAdapter mVideoHelper;
    private Handler mHandler = new Handler();
    private final LoopIncrementRunnable mRunnable = new LoopIncrementRunnable();

    public interface LoopIncrementerListener {
        void onLoopIncremented();
    }

    public LoopIncrementer(Context context, HasVideoPlayerAdapter videoHelper, FeedAdapterItems postHolder) {
        this.mVideoHelper = videoHelper;
        this.mLoopManager = LoopManager.get(context);
        this.mItems = postHolder;
    }

    public void incrementFor(int currentPosition, long postId, LoopIncrementerListener loopIncrementerListener) {
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mRunnable.currentPosition = currentPosition;
        this.mRunnable.postId = postId;
        this.mRunnable.listener = loopIncrementerListener;
        this.mHandler.postDelayed(this.mRunnable, 500L);
    }

    private class LoopIncrementRunnable implements Runnable {
        public int currentPosition;
        public LoopIncrementerListener listener;
        public long postId;

        LoopIncrementRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (stillValid()) {
                FlurryUtils.trackVineLoopWatched();
                LoopIncrementer.this.mLoopManager.increment(this.postId);
                if (this.listener != null) {
                    this.listener.onLoopIncremented();
                }
            }
        }

        private boolean stillValid() {
            return this.postId != 0 && LoopIncrementer.this.mItems.getItemId(LoopIncrementer.this.mVideoHelper.getCurrentPosition()) == this.postId && LoopIncrementer.this.mVideoHelper.getCurrentPosition() == this.currentPosition;
        }
    }
}
