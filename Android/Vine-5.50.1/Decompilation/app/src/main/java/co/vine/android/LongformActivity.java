package co.vine.android;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import co.vine.android.FloatingLikesRenderer;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.api.VineEndlessLikesPostRecord;
import co.vine.android.api.VineEndlessLikesRecord;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineRepost;
import co.vine.android.client.AppController;
import co.vine.android.client.VineAPI;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ImageViewHolder;
import co.vine.android.feedadapter.viewmanager.AvatarViewManager;
import co.vine.android.feedadapter.viewmanager.ImmersiveViewsPostInfoViewManager;
import co.vine.android.player.ExoPlayerWrapper;
import co.vine.android.player.ExtractorRendererBuilder;
import co.vine.android.player.HLSRendererBuilder;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.LongformPostEventLogger;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.longform.LongformActionsListener;
import co.vine.android.service.components.postactions.PostActionsListener;
import co.vine.android.share.activities.PostShareActivity;
import co.vine.android.storage.RealmManager;
import co.vine.android.storage.model.LongformData;
import co.vine.android.storage.operation.QueryOperation;
import co.vine.android.storage.operation.WriteOperation;
import co.vine.android.util.Util;
import co.vine.android.widget.VideoControllerView;
import io.realm.Realm;
import java.util.List;

/* loaded from: classes.dex */
public class LongformActivity extends Activity implements ExoPlayerWrapper.Listener {
    private FloatingLikesRenderer mHeartsRenderer;
    private LongformViewHolder mHolder;
    private LongformPostEventLogger mLongformPostEventLogger;
    private ExoPlayerWrapper mPlayer;
    private VinePost mPost;
    private ScribeState mScribeState;
    private float mVideoAspectRatio;
    private VideoControllerView mVideoController;
    private int mOrientation = 1;
    private AppController mAppController = AppController.getInstance(this);
    private PendingRequestHelper mPendingRequestHelper = new PendingRequestHelper();
    private boolean mShareClicked = false;
    private boolean mHasReachedEnd = false;
    private boolean mPostLiked = false;
    private boolean mLongformPlayLogged = false;
    private PostActionsListener mPostActionListener = new PostActionsListener() { // from class: co.vine.android.LongformActivity.1
        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onLikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
            if (postId == LongformActivity.this.mPost.postId && statusCode == 200) {
                LongformActivity.this.mPostLiked = true;
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnlikePost(String reqId, int statusCode, String reasonPhrase, long postId) {
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onRevine(String reqId, int statusCode, String reasonPhrase, long postId, VineRepost repost) {
            PendingRequest req = LongformActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200) {
                    LongformActivity.this.mPost.updateRevined(repost, true);
                } else {
                    LongformActivity.this.mPost.setFlagRevined(false);
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onUnrevine(String reqId, int statusCode, String reasonPhrase, long postId) {
            PendingRequest req = LongformActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200) {
                    LongformActivity.this.mPost.updateRevined(null, false);
                } else {
                    LongformActivity.this.mPost.setFlagRevined(true);
                }
            }
        }

        @Override // co.vine.android.service.components.postactions.PostActionsListener
        public void onPostEditCaption(String reqId, int statusCode, String reasonPhrase) {
        }
    };
    private Runnable mHideOverlayRunnable = new Runnable() { // from class: co.vine.android.LongformActivity.2
        @Override // java.lang.Runnable
        public void run() {
            LongformActivity.this.hideOverlay();
        }
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: co.vine.android.LongformActivity.3
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (LongformActivity.this.mHeartsRenderer != null) {
                LongformActivity.this.mHeartsRenderer.disable();
            }
            LongformActivity.this.hideResolveState();
            LongformActivity.this.removeHideCallback();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (LongformActivity.this.mHeartsRenderer != null) {
                LongformActivity.this.mHeartsRenderer.enable();
            }
        }
    };
    private Runnable mLongformPlaybackLogRunnable = new Runnable() { // from class: co.vine.android.LongformActivity.17
        @Override // java.lang.Runnable
        public void run() {
            LongformActivity.this.mLongformPostEventLogger.longformPlayed(LongformActivity.this.mPost);
            LongformActivity.this.mLongformPlayLogged = true;
        }
    };
    private Animation mPulseAnimation = new Animation() { // from class: co.vine.android.LongformActivity.18
        @Override // android.view.animation.Animation
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float scale = (float) (1.0d - ((0.4f * ((-interpolatedTime) + 1.0f)) * Math.sin(9.42477796076938d * interpolatedTime)));
            t.getMatrix().postScale(scale, scale, LongformActivity.this.mHolder.likeImage.getWidth() / 2, LongformActivity.this.mHolder.likeImage.getHeight() / 2);
        }
    };

    private enum State {
        BUFFERING,
        PLAYING,
        PAUSED
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longform);
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
        new FullScreenVideoOrientationManager(this).enable();
        Intent i = getIntent();
        this.mPost = (VinePost) i.getParcelableExtra("arg_post");
        this.mHolder = new LongformViewHolder(findViewById(R.id.root_view));
        Components.longformComponent().addListener(new LongformActionsListener() { // from class: co.vine.android.LongformActivity.4
            @Override // co.vine.android.service.components.longform.LongformActionsListener
            public void onFetchEndlessLikesComplete(String reqId, int statusCode, String reasonPhrase, List<VineEndlessLikesRecord> endlessLikes) {
                PendingRequest request = LongformActivity.this.mPendingRequestHelper.removeRequest(reqId);
                if (request != null && statusCode == 200 && endlessLikes != null) {
                    Components.longformComponent().removeListener(this);
                    LongformActivity.this.setupHeartsRenderer(endlessLikes);
                }
            }
        });
        this.mPendingRequestHelper.addRequest(Components.longformComponent().fetchEndlessLikes(this.mAppController, this.mPost.longform.longformId));
        this.mLongformPostEventLogger = new LongformPostEventLogger(ScribeLoggerSingleton.getInstance(this), AppStateProviderSingleton.getInstance(this), AppNavigationProviderSingleton.getInstance());
        this.mScribeState = new ScribeState();
        bindView();
        postDelayedHideCallback();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupHeartsRenderer(List<VineEndlessLikesRecord> endlessLikes) {
        FloatingLikesRenderer.FloatingLikesPlaybackInfoProvider provider = new FloatingLikesRenderer.FloatingLikesPlaybackInfoProvider() { // from class: co.vine.android.LongformActivity.5
            @Override // co.vine.android.FloatingLikesRenderer.FloatingLikesPlaybackInfoProvider
            public boolean isInPlaybackState() {
                return LongformActivity.this.isInPlaybackState();
            }

            @Override // co.vine.android.FloatingLikesRenderer.FloatingLikesPlaybackInfoProvider
            public long getCurrentPosition() {
                if (LongformActivity.this.mPlayer == null) {
                    return 0L;
                }
                return LongformActivity.this.mPlayer.getCurrentPosition();
            }
        };
        FloatingLikesRenderer.FloatingLikesListener listener = new FloatingLikesRenderer.FloatingLikesListener() { // from class: co.vine.android.LongformActivity.6
            @Override // co.vine.android.FloatingLikesRenderer.FloatingLikesListener
            public void showFloatingLike() {
                LongformActivity.this.showFloatingLike(true);
            }
        };
        this.mHeartsRenderer = new FloatingLikesRenderer(endlessLikes, this.mHolder.heartsView.getHandler(), provider, listener);
        this.mHeartsRenderer.enable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInPlaybackState() {
        return this.mPlayer != null && (this.mPlayer.getPlayWhenReady() || this.mPlayer.getPlaybackState() != 4);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 20:
                if (resultCode == -1 && data != null) {
                    this.mPendingRequestHelper.addRequest(ShareRequestHandler.handlePostShareResult(data, this.mAppController));
                    break;
                }
                break;
            case 30:
                if (data != null && data.getBooleanExtra("reported", false)) {
                    setResult(-1, data);
                    finish();
                    break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void bindView() throws Resources.NotFoundException {
        this.mHolder.fullscreenButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LongformActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (LongformActivity.this.mOrientation != 1) {
                    if (LongformActivity.this.mOrientation == 2) {
                        LongformActivity.this.setRequestedOrientation(1);
                        return;
                    }
                    return;
                }
                LongformActivity.this.setRequestedOrientation(0);
            }
        });
        final GestureDetectorCompat rootDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() { // from class: co.vine.android.LongformActivity.8
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!LongformActivity.this.isMotionEventInVideoView(e) || LongformActivity.this.shouldShowPlayPauseButtonWithOverlay()) {
                    LongformActivity.this.toggleOverlay();
                    return true;
                }
                LongformActivity.this.mVideoController.togglePlayPause();
                LongformActivity.this.showOverlay();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent e) {
                LongformActivity.this.addMeLike();
                LongformActivity.this.showOverlay();
                return false;
            }
        });
        this.mHolder.rootView.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.LongformActivity.9
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                return rootDetector.onTouchEvent(event);
            }
        });
        this.mHolder.playPauseButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LongformActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LongformActivity.this.mVideoController.togglePlayPause();
                LongformActivity.this.updatePlayPauseIcon();
            }
        });
        this.mHolder.likesContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LongformActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LongformActivity.this.addMeLike();
                LongformActivity.this.removeHideCallback();
            }
        });
        if (this.mPost.isRTL == null) {
            this.mPost.isRTL = Boolean.valueOf(Util.isRtlLanguage(Util.addDirectionalMarkers(this.mPost.description)));
        }
        new ImmersiveViewsPostInfoViewManager(findViewById(R.id.post_info_container)).bindPostData(this, this.mPost, null, new ImmersiveViewsPostInfoViewManager.UsernameClickListener() { // from class: co.vine.android.LongformActivity.12
            @Override // co.vine.android.feedadapter.viewmanager.ImmersiveViewsPostInfoViewManager.UsernameClickListener
            public void onUsernameClicked(long userId) {
                ChannelActivity.startProfile(LongformActivity.this, userId, "Longform");
            }
        });
        new AvatarViewManager(this, this.mAppController).bind(new ImageViewHolder(this.mHolder.resolveStateContainer, ViewType.AVATAR, R.id.resolve_user_image), this.mPost.avatarUrl, false, 0, true);
        this.mHolder.resolveUsername.setText(this.mPost.username);
        this.mHolder.watchAgain.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LongformActivity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LongformActivity.this.hideResolveState();
                LongformActivity.this.watchAgain();
            }
        });
        this.mHolder.share.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LongformActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LongformActivity.this.hideResolveState();
                LongformActivity.this.startActivityForResult(PostShareActivity.getPostShareIntent(LongformActivity.this, LongformActivity.this.mPost, LongformActivity.this.mAppController.getActiveId() == LongformActivity.this.mPost.userId), 20);
                LongformActivity.this.mShareClicked = true;
            }
        });
        setupToolbar();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePlayPauseIcon() {
        int drawable = isInPlaybackState() ? R.drawable.longform_pause : R.drawable.longform_play;
        this.mHolder.playPauseButton.setImageResource(drawable);
    }

    private void setupToolbar() {
        if (this.mPost.userId != this.mAppController.getActiveId()) {
            this.mHolder.toolbar.inflateMenu(R.menu.longform);
            this.mHolder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // from class: co.vine.android.LongformActivity.15
                @Override // android.support.v7.widget.Toolbar.OnMenuItemClickListener
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.block_or_report) {
                        LongformActivity.this.startActivityForResult(ReportingActivity.getReportPostIntent(LongformActivity.this, LongformActivity.this.mPost.postId, LongformActivity.this.mPost.userId, LongformActivity.this.mPost.username), 30);
                        return false;
                    }
                    return false;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMotionEventInVideoView(MotionEvent e) {
        return e.getY() >= ((float) this.mHolder.videoFrame.getTop()) && e.getY() <= ((float) this.mHolder.videoFrame.getBottom());
    }

    private void preparePlayer(long playbackPosition) {
        ExoPlayerWrapper.RendererBuilder rendererBuilder;
        this.mHolder.videoFrame.setAspectRatio(1.7777778f);
        if (this.mPost.longform.videoUrl.contains(".m3u8")) {
            rendererBuilder = new HLSRendererBuilder(this, VineAPI.getInstance(this).getVineClientHeader(), this.mPost.longform.videoUrl);
        } else {
            rendererBuilder = new ExtractorRendererBuilder(this, VineAPI.getInstance(this).getVineClientHeader(), this.mPost.longform.videoUrl);
        }
        this.mPlayer = new ExoPlayerWrapper(rendererBuilder);
        this.mPlayer.addListener(this);
        this.mPlayer.seekTo(playbackPosition);
        this.mVideoController = new VideoControllerView(this);
        this.mVideoController.setAnchorView(this.mHolder.videoControllerContainer);
        this.mVideoController.setMediaPlayerController(this.mPlayer.getPlayerControl());
        this.mVideoController.setEnabled(true);
        this.mVideoController.setSeekbarChangeListener(this.mOnSeekBarChangeListener);
        postDelayedHideCallback();
        this.mPlayer.prepare();
        this.mPlayer.setPlayWhenReady(true);
        if (!this.mLongformPlayLogged) {
            this.mHolder.videoView.postDelayed(this.mLongformPlaybackLogRunnable, 5000L);
        }
        if (this.mHolder.videoView.getSurfaceTexture() != null) {
            this.mPlayer.setSurface(new Surface(this.mHolder.videoView.getSurfaceTexture()));
        } else {
            this.mHolder.videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: co.vine.android.LongformActivity.16
                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    if (LongformActivity.this.mPlayer != null) {
                        LongformActivity.this.mPlayer.setSurface(new Surface(surface));
                    }
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFloatingLike(boolean otherUserLike) {
        this.mHolder.heartsView.addLikeAt(this.mVideoController.getSeekThumbXPosition() + this.mHolder.likesContainer.getMeasuredWidth(), this.mHolder.heartsView.getMeasuredHeight() - (this.mHolder.heartsView.getIconHeight() / 2), otherUserLike ? -39322 : -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMeLike() {
        showFloatingLike(false);
        pulseLikeButton();
        VineEndlessLikesPostRecord record = new VineEndlessLikesPostRecord();
        record.time = this.mPlayer.getCurrentPosition() / 1000.0f;
        Components.longformComponent().postEndlessLikes(this.mAppController, this.mPost.longform.longformId, record);
        if (!this.mPostLiked) {
            this.mPendingRequestHelper.addRequest(Components.postActionsComponent().likePost(AppController.getInstance(this), "Longform", this.mPost.postId, this.mPost.repost != null ? this.mPost.repost.repostId : 0L, false));
        }
    }

    private void pulseLikeButton() {
        this.mPulseAnimation.setDuration(400L);
        this.mHolder.likeImage.startAnimation(this.mPulseAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleOverlay() {
        if (this.mHolder.controlsContainer.getVisibility() == 0) {
            hideOverlay();
            return;
        }
        showOverlay();
        if (shouldShowPlayPauseButtonWithOverlay()) {
            showPlayPauseButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOverlay() {
        removeHideCallback();
        if (shouldShowPostInfoWithOverlay()) {
            animateShowView(this.mHolder.postInfoContainer);
        } else {
            this.mHolder.postInfoContainer.setVisibility(8);
        }
        if (!shouldShowPlayPauseButtonWithOverlay()) {
            this.mHolder.playPauseButton.setVisibility(8);
        }
        animateShowView(this.mHolder.toolbar);
        animateShowView(this.mHolder.controlsContainer);
    }

    private void showPlayPauseButton() {
        updatePlayPauseIcon();
        animateShowView(this.mHolder.playPauseButton);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldShowPlayPauseButtonWithOverlay() {
        return (this.mOrientation == 2 || ((double) this.mVideoAspectRatio) < 1.0d) && this.mHolder.resolveStateContainer.getVisibility() != 0;
    }

    private boolean shouldShowPostInfoWithOverlay() {
        return this.mOrientation == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideOverlay() {
        animateHideView(this.mHolder.postInfoContainer);
        animateHideView(this.mHolder.controlsContainer);
        animateHideView(this.mHolder.playPauseButton);
        animateHideView(this.mHolder.toolbar);
    }

    private void animateHideView(final View v) {
        v.animate().setListener(null).cancel();
        v.animate().alpha(0.0f).setListener(new SimpleAnimatorListener() { // from class: co.vine.android.LongformActivity.19
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(8);
                v.setAlpha(1.0f);
            }
        });
    }

    private void animateShowView(View v) {
        if (v.getVisibility() != 0) {
            v.animate().setListener(null).cancel();
            v.setAlpha(0.0f);
            v.setVisibility(0);
            v.animate().alpha(1.0f);
            return;
        }
        v.setAlpha(1.0f);
    }

    private void postDelayedHideCallback() {
        removeHideCallback();
        this.mHolder.rootView.postDelayed(this.mHideOverlayRunnable, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHideCallback() {
        this.mHolder.rootView.removeCallbacks(this.mHideOverlayRunnable);
    }

    public static Intent getIntent(Context context, VinePost post) {
        Intent i = new Intent(context, (Class<?>) LongformActivity.class);
        i.putExtra("arg_post", post);
        return i;
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.Listener
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        this.mScribeState.update(playWhenReady, playbackState);
        if (playbackState == 5) {
            if (!this.mHasReachedEnd) {
                this.mLongformPostEventLogger.longformPlayCompleted(this.mPost);
            }
            this.mHasReachedEnd = true;
            showResolveState();
            animateShowView(this.mHolder.controlsContainer);
        }
    }

    private void showResolveState() {
        if (this.mHolder.resolveStateContainer.getVisibility() != 0) {
            this.mHolder.resolveStateContainer.setVisibility(0);
            this.mHolder.resolveStateContainer.setAlpha(0.0f);
            this.mHolder.resolveStateContainer.animate().alpha(1.0f).start();
            this.mHolder.playPauseButton.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideResolveState() {
        this.mHolder.resolveStateContainer.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void watchAgain() {
        hideResolveState();
        this.mPlayer.seekTo(0L);
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.Listener
    public void onError(Exception e) {
    }

    @Override // co.vine.android.player.ExoPlayerWrapper.Listener
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        this.mVideoAspectRatio = height == 0 ? 1.0f : (width * pixelWidthHeightRatio) / height;
        layoutVideoAndControls();
    }

    private void layoutVideoAndControls() {
        RelativeLayout.LayoutParams videoFrameParams = (RelativeLayout.LayoutParams) this.mHolder.videoFrame.getLayoutParams();
        this.mHolder.videoFrame.setAspectRatio(this.mVideoAspectRatio);
        if (this.mOrientation == 1) {
            this.mHolder.fullScreenImage.setImageResource(R.drawable.btn_fullscreen);
            RelativeLayout.LayoutParams postInfoParams = (RelativeLayout.LayoutParams) this.mHolder.postInfoContainer.getLayoutParams();
            if (this.mVideoAspectRatio >= 1.0f) {
                postInfoParams.addRule(2, 0);
                postInfoParams.addRule(3, R.id.video_frame);
                alignTopAndAddMargin(videoFrameParams);
            } else {
                postInfoParams.addRule(3, 0);
                postInfoParams.addRule(2, R.id.controls_container);
                centerInParentAndRemoveTopMargin(videoFrameParams);
                setRequestedOrientation(14);
                this.mHolder.fullscreenButtonContainer.setVisibility(4);
            }
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(0);
        } else {
            this.mHolder.fullScreenImage.setImageResource(R.drawable.btn_exit_fullscreen);
            centerInParentAndRemoveTopMargin(videoFrameParams);
            View decorView2 = getWindow().getDecorView();
            decorView2.setSystemUiVisibility(4);
        }
        if (this.mHolder.controlsContainer.getVisibility() == 0) {
            showOverlay();
        }
    }

    private void centerInParentAndRemoveTopMargin(RelativeLayout.LayoutParams params) {
        params.addRule(10, 0);
        params.addRule(13, 1);
        params.topMargin = 0;
    }

    private void alignTopAndAddMargin(RelativeLayout.LayoutParams params) {
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.theater_actionbar_height);
        params.addRule(13, 0);
        params.addRule(10, 1);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mOrientation = newConfig.orientation;
        layoutVideoAndControls();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        long playbackPosition = this.mPlayer != null ? this.mPlayer.getCurrentPosition() : 0L;
        saveLongformData(playbackPosition);
        this.mScribeState.update(State.PAUSED);
        releasePlayer();
        this.mLongformPostEventLogger.longformPlaybackEnded(this.mPost, this.mScribeState.getInitialPosition(), playbackPosition, this.mScribeState.getBufferTime(), this.mScribeState.getPlayTime(), this.mScribeState.getPauseime(), this.mScribeState.getInterruptions());
        Components.postActionsComponent().removeListener(this.mPostActionListener);
        this.mHolder.videoView.removeCallbacks(this.mLongformPlaybackLogRunnable);
    }

    private void saveLongformData(final long playbackPosition) {
        RealmManager.executeOperation(new WriteOperation() { // from class: co.vine.android.LongformActivity.20
            @Override // co.vine.android.storage.operation.RealmOperation
            public Void execute(Realm realm) {
                LongformData longform = (LongformData) realm.where(LongformData.class).equalTo("longformId", String.valueOf(LongformActivity.this.mPost.longform.longformId)).findFirst();
                if (longform == null) {
                    longform = (LongformData) realm.createObject(LongformData.class);
                    longform.setLongformId(String.valueOf(LongformActivity.this.mPost.longform.longformId));
                }
                longform.setCursorTime(playbackPosition);
                longform.setWatched(true);
                float windowStart = (LongformActivity.this.mPost.longform.duration * 1000.0f) - 500.0f;
                longform.setReachedEnd(((float) playbackPosition) > windowStart);
                return null;
            }
        });
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mPendingRequestHelper.onResume();
        this.mHolder.rootView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mOrientation = getResources().getConfiguration().orientation;
        long initialPosition = restoreSavedLongformPlaybackPosition();
        this.mScribeState.init(initialPosition);
        preparePlayer(initialPosition);
        Components.postActionsComponent().addListener(this.mPostActionListener);
    }

    private long restoreSavedLongformPlaybackPosition() {
        Long playbackPosition = (Long) RealmManager.executeOperation(new QueryOperation<Long>() { // from class: co.vine.android.LongformActivity.21
            @Override // co.vine.android.storage.operation.RealmOperation
            public Long execute(Realm realm) {
                LongformData longform = (LongformData) realm.where(LongformData.class).equalTo("longformId", String.valueOf(LongformActivity.this.mPost.longform.longformId)).findFirst();
                long playbackPosition2 = longform == null ? 0L : longform.getCursorTime();
                if (longform != null && longform.isReachedEnd() && !LongformActivity.this.mShareClicked) {
                    playbackPosition2 = 0;
                }
                return Long.valueOf(playbackPosition2);
            }
        });
        this.mShareClicked = false;
        if (playbackPosition != null) {
            return playbackPosition.longValue();
        }
        return 0L;
    }

    private void releasePlayer() {
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer = null;
            this.mVideoController.detachFromAnchor();
            this.mVideoController = null;
        }
    }

    private class ScribeState {
        private long mBufferTime;
        private long mInitialPosition;
        private int mInterruptions;
        private State mLastRecordedState;
        private long mLastStateChangePosition;
        private long mLastStateChangeTime;
        private long mPauseTime;
        private long mPlayTime;

        private ScribeState() {
            this.mBufferTime = 0L;
            this.mPlayTime = 0L;
            this.mPauseTime = 0L;
            this.mInterruptions = -1;
        }

        public void init(long position) {
            this.mInitialPosition = position;
            this.mLastRecordedState = State.PAUSED;
            this.mLastStateChangeTime = System.currentTimeMillis();
            this.mLastStateChangePosition = this.mInitialPosition;
        }

        public void update(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case 3:
                    update(State.BUFFERING);
                    break;
                case 4:
                    if (!playWhenReady) {
                        update(State.PAUSED);
                        break;
                    } else {
                        update(State.PLAYING);
                        break;
                    }
                default:
                    update(State.PAUSED);
                    break;
            }
        }

        public void update(State newState) {
            long currentTime = System.currentTimeMillis();
            long lastTimeSegment = currentTime - this.mLastStateChangeTime;
            this.mLastStateChangeTime = currentTime;
            long currentPosition = LongformActivity.this.mPlayer.getCurrentPosition();
            long positionDiff = currentPosition - this.mLastStateChangePosition;
            this.mLastStateChangePosition = currentPosition;
            switch (this.mLastRecordedState) {
                case BUFFERING:
                    this.mBufferTime += lastTimeSegment;
                    if (newState == State.PLAYING && Math.abs(positionDiff) < 200) {
                        this.mInterruptions++;
                        break;
                    }
                    break;
                case PLAYING:
                    this.mPlayTime += lastTimeSegment;
                    break;
                case PAUSED:
                    this.mPauseTime += lastTimeSegment;
                    break;
            }
            this.mLastRecordedState = newState;
        }

        public long getInitialPosition() {
            return this.mInitialPosition;
        }

        public long getBufferTime() {
            return this.mBufferTime;
        }

        public long getPlayTime() {
            return this.mPlayTime;
        }

        public long getPauseime() {
            return this.mPauseTime;
        }

        public int getInterruptions() {
            return this.mInterruptions;
        }
    }
}
