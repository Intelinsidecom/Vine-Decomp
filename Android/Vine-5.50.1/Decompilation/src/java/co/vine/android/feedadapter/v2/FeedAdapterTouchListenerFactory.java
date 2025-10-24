package co.vine.android.feedadapter.v2;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import co.vine.android.api.VinePost;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.player.HasVideoPlayerAdapter;
import co.vine.android.player.OnListVideoClickListener;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public final class FeedAdapterTouchListenerFactory {
    private final TimelineClickListenerFactory.Callback mCallback;
    private final boolean mDoubleTapToLikeOnPause;
    private final HasVideoPlayerAdapter mVideoHelper;

    public FeedAdapterTouchListenerFactory(HasVideoPlayerAdapter videoHelper, Context context, TimelineClickListenerFactory.Callback callback) {
        this.mVideoHelper = videoHelper;
        this.mDoubleTapToLikeOnPause = ClientFlagsHelper.doubleTapToLikeOnPause(context);
        this.mCallback = callback;
    }

    public VideoContainerTouchListener newVideoTouchListener(TimelineItemVideoViewHolder tag, VinePost post) {
        return new VideoContainerTouchListener(tag, post, this.mVideoHelper, this.mDoubleTapToLikeOnPause, this.mCallback);
    }

    public static class VideoContainerTouchListener extends OnListVideoClickListener implements View.OnTouchListener {
        private final boolean mDoubleTapToLikeOnPause;
        private long mLastTapTime;
        private float mLastX;
        private float mLastY;
        private final TimelineClickListenerFactory.Callback mOnClickCallback;
        private final VinePost mPost;
        private final TimelineItemVideoViewHolder mVideoHolder;

        private VideoContainerTouchListener(TimelineItemVideoViewHolder tag, VinePost post, HasVideoPlayerAdapter videoHelper, boolean doubleTapToLikeOnPause, TimelineClickListenerFactory.Callback onClickCallback) {
            super(videoHelper);
            this.mVideoHolder = tag;
            this.mPost = post;
            this.mDoubleTapToLikeOnPause = doubleTapToLikeOnPause;
            this.mOnClickCallback = onClickCallback;
        }

        @Override // co.vine.android.player.OnListVideoClickListener, android.view.View.OnClickListener
        public void onClick(View v) {
            super.onClick(v);
            if (System.currentTimeMillis() - this.mLastTapTime <= 450) {
                if (this.mVideoHolder.video.isInPlaybackState() && (this.mDoubleTapToLikeOnPause || !this.mVideoHolder.video.isPaused())) {
                    if (this.mVideoHolder.doubleTap.getVisibility() != 0) {
                        this.mVideoHolder.doubleTap.setVisibility(0);
                    }
                    this.mVideoHolder.doubleTap.addLikeAt((int) this.mLastX, (int) this.mLastY, 0);
                    FlurryUtils.trackDoubleTap(this.mPost.postId);
                    this.mOnClickCallback.onLikePost(this.mPost, this.mPosition);
                }
                this.mLastTapTime = 0L;
                return;
            }
            this.mLastTapTime = System.currentTimeMillis();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 0) {
                this.mLastX = event.getX();
                this.mLastY = event.getY();
                return false;
            }
            return false;
        }
    }
}
