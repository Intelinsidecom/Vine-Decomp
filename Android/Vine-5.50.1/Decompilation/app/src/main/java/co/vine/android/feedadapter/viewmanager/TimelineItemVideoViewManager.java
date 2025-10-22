package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import co.vine.android.R;
import co.vine.android.TimelineItemScribeActionsListener;
import co.vine.android.api.VinePost;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.embed.player.VineVideoView;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedAdapterTouchListenerFactory;
import co.vine.android.feedadapter.v2.FeedVideoController;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.v2.LoopIncrementer;
import co.vine.android.feedadapter.v2.VideoImageTopHideAnimation;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.feedadapter.viewholder.PostViewHolder;
import co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder;
import co.vine.android.player.SdkVideoView;
import co.vine.android.player.VideoSensitiveImageClickListener;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.ResourceLoader;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.AnalyticsManager;
import co.vine.android.util.analytics.BehaviorManager;
import co.vine.android.widget.OnTopViewBoundListener;
import co.vine.android.widget.SensitiveAcknowledgments;
import com.edisonwang.android.slog.SLogger;
import java.util.HashMap;

/* loaded from: classes.dex */
public class TimelineItemVideoViewManager implements ViewManager {
    private static Bitmap sDoubleTapBitmap;
    protected final AppController mAppController;
    public final TimelineClickListenerFactory.Callback mCallback;
    public final Context mContext;
    protected final FeedAdapterItems mItems;
    private final FeedAdapterTouchListenerFactory mListenerFactory;
    private final SLogger mLogger;
    private final LoopIncrementer mLoopIncrementer;
    private final OnTopViewBoundListener mOnTopViewBoundListener;
    private final TimelineItemScribeActionsListener mPostPlayedListener;
    private final SensitiveAcknowledgments mSensitiveAcknowledgments;
    protected final FeedVideoController mVideoController;
    private final FeedViewHolderCollection mViewHolders;
    private boolean mFirstPlayed = false;
    private boolean mWasResumed = false;

    public TimelineItemVideoViewManager(Context context, AppController appController, TimelineClickListenerFactory.Callback callback, FeedAdapterItems items, FeedViewHolderCollection viewHolders, FeedVideoController videoController, SensitiveAcknowledgments sensitiveAcknowledgments, LoopIncrementer incrementer, FeedAdapterTouchListenerFactory listenerFactory, OnTopViewBoundListener onTopViewBoundListener, TimelineItemScribeActionsListener postPlayedListener, SLogger logger) {
        this.mContext = context;
        this.mAppController = appController;
        this.mCallback = callback;
        this.mItems = items;
        this.mViewHolders = viewHolders;
        this.mVideoController = videoController;
        this.mSensitiveAcknowledgments = sensitiveAcknowledgments;
        this.mLoopIncrementer = incrementer;
        this.mListenerFactory = listenerFactory;
        this.mOnTopViewBoundListener = onTopViewBoundListener;
        this.mPostPlayedListener = postPlayedListener;
        this.mLogger = logger;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_VIDEO;
    }

    public void init(TimelineItemVideoViewHolder h, VinePost data) {
        int dimen = SystemUtil.getDisplaySize(this.mContext).x;
        ViewGroup.LayoutParams params = h.container.getLayoutParams();
        params.height = dimen;
        params.width = dimen;
        h.container.setLayoutParams(params);
        ViewGroup.LayoutParams params2 = h.bottomThumbnail.getLayoutParams();
        params2.width = dimen;
        params2.height = dimen;
        h.bottomThumbnail.setLayoutParams(params2);
        h.topThumbnail.setLayoutParams(params2);
        h.listener = this.mListenerFactory.newVideoTouchListener(h, data);
        h.container.setOnClickListener(h.listener);
        h.container.setOnTouchListener(h.listener);
    }

    public void bind(TimelineItemVideoViewHolder h, VinePost data, int position) {
        if (data != null) {
            if (position == 0 && this.mOnTopViewBoundListener != null) {
                this.mOnTopViewBoundListener.onTopViewBound();
            }
            if (h.topThumbnailAnimation == null) {
                h.topThumbnailAnimation = new VideoImageTopHideAnimation(h.topThumbnail);
            }
            boolean hideVideoView = false;
            h.hideThumbnail(false);
            if (this.mVideoController.shouldShowThumbnail(position)) {
                h.spinner.setVisibility(0);
                hideVideoView = true;
            }
            if (hideVideoView) {
                h.video.setVisibility(8);
            }
            bindVideoImage(h, data, position);
            initVideoView(h, data, position);
            if (!this.mFirstPlayed && position == 0 && !data.isExplicit()) {
                this.mVideoController.getVideoPathAndPlayForPosition(0, false);
                this.mFirstPlayed = true;
            }
        }
    }

    private synchronized void bindVideoImage(final TimelineItemVideoViewHolder h, VinePost post, int position) {
        synchronized (this) {
            h.thumbnailKey = null;
            h.hasThumbnail = post.thumbnailUrl != null;
            boolean isSensitiveImage = this.mSensitiveAcknowledgments.isExplicit(post);
            if (isSensitiveImage) {
                h.sensitiveTextContainer.setVisibility(0);
                h.imageListener = new VideoSensitiveImageClickListener(this.mVideoController, this.mSensitiveAcknowledgments);
                h.sensitiveTextContainer.setOnClickListener(h.imageListener);
                h.sensitiveTextContainer.setClickable(true);
            } else {
                h.sensitiveTextContainer.setVisibility(8);
                h.sensitiveTextContainer.setOnClickListener(null);
                h.sensitiveTextContainer.setClickable(false);
                if (h.hasThumbnail) {
                    ImageKey videoImageKey = new ImageKey(post.thumbnailUrl);
                    h.thumbnailKey = videoImageKey;
                    Bitmap thumbnail = this.mAppController.getPhotoBitmap(videoImageKey);
                    if (thumbnail == null) {
                        h.bottomThumbnail.setBackgroundColor(this.mContext.getResources().getColor(R.color.solid_light_gray));
                    } else {
                        ViewUtil.setBackground(h.bottomThumbnail, new RecyclableBitmapDrawable(this.mContext.getResources(), thumbnail));
                    }
                }
            }
            h.topThumbnail.setBackground(h.bottomThumbnail.getBackground());
            if (!this.mSensitiveAcknowledgments.isExplicit(post)) {
                h.bottomThumbnail.setVisibility(0);
            } else {
                h.bottomThumbnail.setVisibility(8);
            }
            h.listener.setPosition(position);
            if (h.imageListener != null) {
                h.imageListener.setPosition(position);
                h.imageListener.setPostId(post.postId);
            }
            h.doubleTap.setVisibility(8);
            h.doubleTap.reset();
            if (sDoubleTapBitmap == null) {
                sDoubleTapBitmap = BitmapFactory.decodeResource(h.doubleTap.getResources(), R.drawable.double_tap);
            }
            h.doubleTap.setDrawable(new RecyclableBitmapDrawable(h.doubleTap.getResources(), sDoubleTapBitmap));
            String customLikeIconUrl = post.customLikeIconUrl;
            if (customLikeIconUrl != null) {
                new ResourceLoader(this.mContext, this.mAppController).setImageWhenLoaded(new ResourceLoader.ImageSetter() { // from class: co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager.1
                    @Override // co.vine.android.util.ResourceLoader.ImageSetter
                    public void setImage(RecyclableBitmapDrawable drawable) {
                        h.doubleTap.setDrawable(drawable);
                    }

                    @Override // co.vine.android.util.ResourceLoader.ImageSetter
                    public View getControllingView() {
                        return h.doubleTap;
                    }

                    @Override // co.vine.android.util.ResourceLoader.ImageSetter
                    public void startAnimation(Animation animation) {
                    }
                }, customLikeIconUrl);
            }
        }
    }

    protected void initVideoView(TimelineItemVideoViewHolder h, final VinePost post, final int position) {
        h.spinner.setVisibility(8);
        final VideoViewInterface view = h.video;
        view.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager.2
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view2) {
                if (view2.getPlayingPosition() == TimelineItemVideoViewManager.this.mVideoController.getCurrentPosition()) {
                    TimelineItemScribeActionsListener postPlayerListener = TimelineItemVideoViewManager.this.mPostPlayedListener;
                    if (postPlayerListener != null) {
                        postPlayerListener.onPostPlayed(post, position);
                    }
                    TimelineItemVideoViewManager.this.countedMediaPlayerStart(view2, TimelineItemVideoViewManager.this.mItems);
                } else {
                    TimelineItemVideoViewManager.this.mLogger.e("This video should not be played: {}, {}.", Integer.valueOf(view2.getPlayingPosition()), Integer.valueOf(TimelineItemVideoViewManager.this.mVideoController.getCurrentPosition()));
                }
                AnalyticsManager.onVideoLoaded(view2);
                BehaviorManager.getInstance(TimelineItemVideoViewManager.this.mContext).onVideoLoaded();
            }
        });
        if (view instanceof VineVideoView) {
            ((VineVideoView) view).setOnGLErrorListener(new VineVideoView.GLErrorListener() { // from class: co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager.3
                @Override // co.vine.android.embed.player.VineVideoView.GLErrorListener
                public void onGlError(VineVideoView vineVideoView, RuntimeException e) {
                    CrashUtil.logException(e, "GL Error happened " + vineVideoView.getSurfaceTexture() + " " + e.getMessage(), new Object[0]);
                }
            });
        }
        view.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager.4
            @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
            public boolean onError(VideoViewInterface videoView, int what, int extra) {
                TimelineItemVideoViewManager.this.mLogger.d("Got error, try recycling it more aggressively: {}, {}", Integer.valueOf(what), Integer.valueOf(extra));
                TimelineItemVideoViewManager.this.mVideoController.onVideoError(view);
                if (!view.retryOpenVideo(false)) {
                    TimelineItemVideoViewManager.this.mLogger.e("Replay video using videoLowUrl.");
                    TimelineItemVideoViewManager.this.mItems.removePath(post.postId);
                    final VideoViewInterface.SilentExceptionHandler handler = view.getSilentExceptionHandler();
                    if (handler != null) {
                        view.setSilentExceptionHandler(new VideoViewInterface.SilentExceptionHandler() { // from class: co.vine.android.feedadapter.viewmanager.TimelineItemVideoViewManager.4.1
                            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
                            public void onUnexpectedException(String message, Exception exception) {
                                handler.onUnexpectedException(message, exception);
                            }

                            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
                            public void onExpectedException(String message, Exception exception) {
                                CrashUtil.logException(exception, message, new Object[0]);
                            }

                            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
                            public void onProgrammingErrorException(Exception exception) {
                                handler.onProgrammingErrorException(exception);
                            }

                            @Override // co.vine.android.embed.player.VideoViewInterface.SilentExceptionHandler
                            public void onExceptionDuringOpening(String message, Exception ex) {
                                handler.onExpectedException(message, ex);
                            }
                        });
                    }
                    if (TimelineItemVideoViewManager.this.mVideoController.getCurrentPosition() == position) {
                        TimelineItemVideoViewManager.this.mVideoController.stopCurrentPlayer();
                    }
                    TimelineItemVideoViewManager.this.mVideoController.getVideoPathAndPlayForPosition(position, true);
                }
                return true;
            }
        });
        if (view instanceof SdkVideoView) {
            view.setSurfaceReadyListener(this.mVideoController);
        }
    }

    protected void resetStates(boolean hasDataSetChanged) {
        boolean currentPlayerChanged = true;
        VideoViewInterface lastPlayer = this.mVideoController.getLastPlayer();
        if (lastPlayer != null && this.mVideoController.getCurrentPosition() >= 0) {
            Object tag = lastPlayer.getTag();
            long postId = -1;
            if (tag instanceof Long) {
                postId = ((Long) tag).longValue();
            }
            if (this.mItems.getItemId(this.mVideoController.getCurrentPosition()) == postId) {
                currentPlayerChanged = false;
            }
        }
        if (currentPlayerChanged) {
            this.mVideoController.resetStates();
            this.mVideoController.resetShouldBePlaying();
            this.mFirstPlayed = false;
        }
        if (hasDataSetChanged) {
            this.mViewHolders.invalidateAllViewHolderPositions();
        }
    }

    protected void onPause() {
        this.mVideoController.pauseVideoViews();
    }

    protected void onFocusChanged(boolean focused) {
        this.mVideoController.onFocusChanged(focused);
        this.mWasResumed = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onResume(boolean focused) {
        if (!this.mWasResumed) {
            this.mWasResumed = true;
            this.mVideoController.resetShouldBePlaying();
            this.mVideoController.toggleMute(MuteUtil.isMuted(this.mContext));
        }
        CardViewHolder cardViewHolderOnResume = this.mVideoController.onResume(focused);
        if (cardViewHolderOnResume != 0 && cardViewHolderOnResume.getType() == ViewType.POST && cardViewHolderOnResume.post.isPresent()) {
            bindVideoImage(((PostViewHolder) cardViewHolderOnResume).getVideoHolder(), cardViewHolderOnResume.post.get(), cardViewHolderOnResume.position);
        }
    }

    protected void onDestroy() {
        this.mVideoController.stopCurrentPlayer();
        this.mVideoController.releaseOtherPlayers(null);
    }

    protected void onDestroyView() {
        this.mVideoController.releaseOtherPlayers(null);
    }

    protected void onScrollIdle() {
        this.mVideoController.postNewPlayCurrentPositionRunnable();
    }

    protected void onVideoImageObtained() {
        this.mVideoController.postNewPlayCurrentPositionRunnable();
    }

    protected void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        this.mLogger.d("CALLBACK onVideoPathObtained");
        if (this.mVideoController.onVideoPathObtained(videos)) {
            this.mVideoController.postNewPlayCurrentPositionRunnable();
        }
    }

    protected boolean isPlaying() {
        return this.mVideoController.hasPlayerPlaying();
    }

    protected CardViewHolder getValidViewHolder(int position) {
        return (CardViewHolder) this.mVideoController.getViewGroupHelper().getValidViewHolder(position);
    }

    protected void pausePlayer() {
        this.mVideoController.pauseCurrentPlayer();
    }

    protected void countedMediaPlayerStart(VideoViewInterface view, FeedAdapterItems items) {
        if (view.getPlayingPosition() == this.mVideoController.getCurrentPosition()) {
            view.start();
            this.mLoopIncrementer.incrementFor(this.mVideoController.getCurrentPosition(), items.getItemId(this.mVideoController.getCurrentPosition()), null);
        } else {
            this.mLogger.e("This video should not be played: {}, {}.", Integer.valueOf(view.getPlayingPosition()), Integer.valueOf(this.mVideoController.getCurrentPosition()));
        }
    }

    protected void toggleMute(boolean mute) {
        this.mVideoController.toggleMute(mute);
    }

    public int getCurrentlyPlayingPosition() {
        return this.mVideoController.getCurrentPosition();
    }

    public void playCurrentPosition() {
        this.mVideoController.postNewPlayCurrentPositionRunnable();
    }
}
