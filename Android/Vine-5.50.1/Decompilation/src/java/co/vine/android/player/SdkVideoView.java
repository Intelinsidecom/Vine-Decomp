package co.vine.android.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationManagerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import co.vine.android.embed.player.VideoViewInterface;
import java.lang.ref.WeakReference;
import java.util.Map;

/* loaded from: classes.dex */
public class SdkVideoView extends VideoViewInterface {
    private static final boolean DISABLE_OPEN_ON_SEPARATE_THREAD;
    private static final boolean IS_DEFECTIVE_ON_THREADED_MEDIA_PLAYERS;
    private static final int[] LOCK = new int[0];
    private static boolean OPEN_ON_MAIN_THREAD;
    private static int sPlayingCount;
    private static boolean sRunMediaOpsOnSeparateThread;
    private static boolean sSinglePlayerMode;
    private AttributeSet mAttributes;
    private int mAudioSession;
    private boolean mAutoPlayOnPrepared;
    private boolean mAutoShow;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private boolean mEnsureVisibilityWhenStart;
    private MediaPlayer.OnErrorListener mErrorListener;
    private boolean mHasNotRetriedBefore;
    private Map<String, String> mHeaders;
    private boolean mIsPrepared;
    private boolean mIsSeeking;
    private boolean mLooping;
    private Handler mMediaOpHandler;
    private HandlerThread mMediaOpThread;
    private boolean mMuted;
    private int mNextSeek;
    private final Runnable mOnOpenErrorListener;
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private String mPath;
    private final Runnable mPrepareNotifyRunnable;
    private long mPrepareStart;
    MediaPlayer.OnPreparedListener mPreparedListener;
    private volatile String mReleaseTag;
    private final SdkVideoViewState mSdkVideoViewState;
    private int mSeekWhenPrepared;
    private volatile boolean mShouldClearState;
    private int mTargetState;
    private final Handler mUiOpsHandler;
    public Uri mUri;
    protected int mVideoHeight;
    protected int mVideoWidth;
    private final Runnable mVisibleStartRunnable;
    private boolean mWasMuted;

    static {
        IS_DEFECTIVE_ON_THREADED_MEDIA_PLAYERS = Build.VERSION.SDK_INT < 21;
        DISABLE_OPEN_ON_SEPARATE_THREAD = IS_DEFECTIVE_ON_THREADED_MEDIA_PLAYERS;
        sRunMediaOpsOnSeparateThread = IS_DEFECTIVE_ON_THREADED_MEDIA_PLAYERS ? false : true;
        OPEN_ON_MAIN_THREAD = false;
    }

    public SdkVideoView(Context context) {
        this(context, null);
    }

    public SdkVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SdkVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mUiOpsHandler = new Handler(Looper.getMainLooper());
        this.mVisibleStartRunnable = new Runnable() { // from class: co.vine.android.player.SdkVideoView.1
            @Override // java.lang.Runnable
            public void run() {
                SdkVideoView.this.setVisibility(0);
            }
        };
        this.mPrepareNotifyRunnable = new Runnable() { // from class: co.vine.android.player.SdkVideoView.2
            @Override // java.lang.Runnable
            public void run() {
                if (SdkVideoView.this.mOnPreparedListener != null) {
                    SdkVideoView.this.mOnPreparedListener.onPrepared(SdkVideoView.this);
                }
            }
        };
        this.mTargetState = 1;
        this.mMuted = false;
        this.mWasMuted = false;
        this.mAudioSession = 0;
        this.mCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: co.vine.android.player.SdkVideoView.3
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                SdkVideoView.this.onComplete();
            }
        };
        this.mErrorListener = new MediaPlayer.OnErrorListener() { // from class: co.vine.android.player.SdkVideoView.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                SdkVideoView.this.onError(framework_err, impl_err);
                return true;
            }
        };
        this.mOnOpenErrorListener = new Runnable() { // from class: co.vine.android.player.SdkVideoView.5
            @Override // java.lang.Runnable
            public void run() {
                SdkVideoView.this.mErrorListener.onError(SdkVideoView.this.mSdkVideoViewState.getPlayer(), 1, 0);
            }
        };
        this.mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: co.vine.android.player.SdkVideoView.6
            @Override // android.media.MediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                SdkVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() { // from class: co.vine.android.player.SdkVideoView.7
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer mp) throws IllegalStateException {
                SdkVideoView.this.mIsSeeking = false;
                if (SdkVideoView.this.mNextSeek >= 0) {
                    int seek = SdkVideoView.this.mNextSeek;
                    SdkVideoView.this.mNextSeek = -1;
                    mp.seekTo(seek);
                }
            }
        };
        this.mReleaseTag = "";
        this.mPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: co.vine.android.player.SdkVideoView.8
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                if (SdkVideoView.sLogsOn) {
                    Log.d("TextureVideoView", "Prepare took " + String.valueOf(System.currentTimeMillis() - SdkVideoView.this.mPrepareStart));
                }
                try {
                    mp.seekTo(0);
                } catch (Exception e) {
                }
                SdkVideoView.this.setCurrentState(4);
                SdkVideoView.this.mIsPrepared = true;
                if (SdkVideoView.this.mAutoPlayOnPrepared) {
                    SdkVideoView.this.postMediaOp(2);
                }
                try {
                    SdkVideoView.this.onVideoSizeChanged(mp.getVideoWidth(), mp.getVideoHeight());
                } catch (IllegalStateException e2) {
                    if (SdkVideoView.sLogsOn) {
                        Log.e("TextureVideoView", "IllegalState happened. ", e2);
                    }
                }
                SdkVideoView.this.onPlayerPrepared();
                SdkVideoView.this.mUiOpsHandler.post(SdkVideoView.this.mPrepareNotifyRunnable);
            }
        };
        this.mSdkVideoViewState = sSinglePlayerMode ? SinglePlayerState.getInstance() : new MultiPlayerState();
        this.mAttributes = attrs;
        initVideoView();
    }

    public static void setSinglePlayerMode(boolean singlePlayerMode) {
        sSinglePlayerMode = singlePlayerMode;
    }

    private static synchronized void onPlay() {
        sPlayingCount++;
        if (sPlayingCount > 1) {
            Log.e("TextureVideoView", "Playing count > 1");
        }
    }

    public static void setRunMediaOpsOnSeparateThread(boolean enabled) {
        sRunMediaOpsOnSeparateThread = enabled;
    }

    private boolean hasPlayer() {
        return this.mSdkVideoViewState.getPlayer() != null;
    }

    public int getCurrentState() {
        return this.mSdkVideoViewState.getCurrentState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentState(int state) {
        this.mSdkVideoViewState.setCurrentState(state);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) throws IllegalStateException {
        if (getSurfaceReadyListener() == null) {
            openVideo();
        } else {
            super.onSurfaceTextureAvailable(surface, width, height);
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) throws IllegalStateException {
        super.onSurfaceTextureSizeChanged(surface, width, height);
        boolean isValidState = this.mTargetState == 6;
        boolean hasValidSize = this.mVideoWidth == width && this.mVideoHeight == height;
        if (hasPlayer() && isValidState && hasValidSize) {
            if (this.mSeekWhenPrepared != 0) {
                seekTo(this.mSeekWhenPrepared);
            }
            start();
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) throws IllegalStateException {
        release("surface destroyed", true, true);
        return true;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mMediaOpThread = new HandlerThread("MediaOp");
        this.mMediaOpThread.start();
        this.mMediaOpHandler = new Handler(this.mMediaOpThread.getLooper(), new MediaOpCallback(this));
    }

    private void initVideoView() {
        this.mContext = getContext();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setScaleX(1.00001f);
        requestFocus();
        this.mTargetState = 1;
    }

    public void scaleVideoAndAlign(int curHeight, int newHeight, boolean alignBottom) {
        float scaleY = newHeight / curHeight;
        int pivotPointY = alignBottom ? curHeight : 0;
        Matrix matrix = new Matrix();
        matrix.setScale(1.0f, scaleY, 0, pivotPointY);
        setTransform(matrix);
        setLayoutParams(new FrameLayout.LayoutParams(newHeight, curHeight));
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setVideoPath(String path) throws IllegalStateException {
        setVideoPathDirect(path);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setVideoPathDirect(String path) throws IllegalStateException {
        super.setVideoPathDirect(path);
        setVideoURI(path != null ? Uri.parse(path) : null);
    }

    public void setVideoURI(Uri uri) throws IllegalStateException {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) throws IllegalStateException {
        this.mUri = uri;
        this.mSdkVideoViewState.setUri(uri);
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        postInvalidate();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean retryOpenVideo(boolean forced) throws IllegalStateException {
        if (sLogsOn) {
            Log.d("TextureVideoView", "Retry open video: " + (!this.mHasNotRetriedBefore));
        }
        if (!forced && this.mHasNotRetriedBefore) {
            return false;
        }
        this.mHasNotRetriedBefore = true;
        openVideo();
        return true;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void startOpenVideo() throws IllegalStateException {
        openVideo();
    }

    private void openVideo() throws IllegalStateException {
        if (OPEN_ON_MAIN_THREAD) {
            mediaOpOpen();
        } else {
            postMediaOp(1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void openMediaPlayer(android.view.Surface r14) throws java.lang.IllegalStateException, java.io.IOException, java.lang.SecurityException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 296
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.player.SdkVideoView.openMediaPlayer(android.view.Surface):void");
    }

    private MediaPlayer makePlayerOnMainThread(PlayerMaker holder) throws InterruptedException {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.mUiOpsHandler.postAtFrontOfQueue(holder);
            long waitTime = 0;
            while (!holder.isReady()) {
                try {
                    Thread.sleep(10L);
                    waitTime += 10;
                } catch (InterruptedException e) {
                    Log.i("TextureVideoView", "Waiting failed for player holder.");
                }
                if (waitTime >= 1000) {
                    return null;
                }
            }
        } else {
            holder.run();
        }
        return holder.getPlayer();
    }

    private String getErrorMessageFromFrameworkError(int frameworkError) {
        if (frameworkError == -1004) {
            return "File or network related operation errors.";
        }
        if (frameworkError == -1007) {
            return "Bitstream is not conforming to the related coding standard or file spec.";
        }
        if (frameworkError == 100) {
            return "TextureVideoView error. Media server died. In this case, the application must release the MediaPlayer object and instantiate a new one.";
        }
        if (frameworkError == -110) {
            return "TextureVideoView error. Some operation takes too long to complete, usually more than 3-5 seconds.";
        }
        if (frameworkError == 1) {
            return "TextureVideoView error. Unspecified media player error.";
        }
        if (frameworkError == -1010) {
            return "TextureVideoView error. Bitstream is conforming to the related coding standard or file spec, but the media framework does not support the feature.";
        }
        if (frameworkError == 200) {
            return "The video is streamed and its container is not valid for progressive playback i.e the video's index (e.g moov atom) is not at the start of the file.";
        }
        return "Unknown Error";
    }

    @Override // android.view.View
    public void setKeepScreenOn(final boolean keepScreenOn) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            super.setKeepScreenOn(keepScreenOn);
        } else {
            post(new Runnable() { // from class: co.vine.android.player.SdkVideoView.9
                @Override // java.lang.Runnable
                public void run() {
                    SdkVideoView.super.setKeepScreenOn(keepScreenOn);
                }
            });
        }
    }

    public void setAutoShow(boolean autoShow) {
        this.mAutoShow = autoShow;
    }

    private void checkForFrameDrops(String op, long start) {
        if (sLogsOn && Looper.getMainLooper() == Looper.myLooper()) {
            long time = System.currentTimeMillis() - start;
            if (time > 15) {
                Log.w("TextureVideoView", "Frame drops caused by " + op + " took " + time);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPlayerPrepared() throws IllegalStateException {
        int seekToPosition = this.mSeekWhenPrepared;
        if (seekToPosition != 0) {
            mediaOpSeekTo(seekToPosition);
        }
        if (this.mVideoWidth != 0 && this.mVideoHeight != 0) {
            if (this.mSurfaceWidth == this.mVideoWidth && this.mSurfaceHeight == this.mVideoHeight) {
                if (this.mTargetState == 6) {
                    start();
                    return;
                }
                if (isPlaying() || seekToPosition != 0 || getCurrentPosition() > 0) {
                }
                return;
            }
            return;
        }
        if (this.mTargetState == 6) {
            start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onComplete() {
        setCurrentState(5);
        this.mTargetState = 5;
        if (this.mOnCompleteListener != null) {
            this.mOnCompleteListener.onCompletion(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onError(int framework_err, int impl_err) {
        if (sLogsOn) {
            Log.d("TextureVideoView", "Error: " + framework_err + " " + impl_err + " " + this + " " + this.mPath);
            Log.d("TextureVideoView", "Framework Error: " + getErrorMessageFromFrameworkError(framework_err));
        }
        setCurrentState(-1);
        this.mTargetState = -1;
        this.mPath = null;
        return this.mOnErrorListener != null && this.mOnErrorListener.onError(this, framework_err, impl_err);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setAutoPlayOnPrepared(boolean autoPlay) {
        this.mAutoPlayOnPrepared = autoPlay;
    }

    public void stopPlayback() throws IllegalStateException {
        if (this.mSdkVideoViewState.requiresLockOnStopPlayback()) {
            synchronized (LOCK) {
                stopPlaybackRelease();
            }
            return;
        }
        stopPlaybackRelease();
    }

    void stopPlaybackRelease() throws IllegalStateException {
        release("stop playback", true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVideoSizeChanged(int width, int height) {
        if (this.mVideoHeight != height || this.mVideoWidth != width) {
            this.mVideoWidth = width;
            this.mVideoHeight = height;
            if (this.mVideoWidth != 0 && this.mVideoHeight != 0) {
                this.mUiOpsHandler.post(new Runnable() { // from class: co.vine.android.player.SdkVideoView.10
                    @Override // java.lang.Runnable
                    public void run() {
                        SdkVideoView.this.requestLayout();
                    }
                });
            }
        }
    }

    private void release(String tag, boolean cleartargetstate, boolean runNow) throws IllegalStateException {
        this.mReleaseTag = tag;
        this.mShouldClearState = cleartargetstate;
        if (runNow) {
            mediaOpRelease();
        } else {
            removePendingMediaOps();
            postMediaOp(0);
        }
        setKeepScreenOn(false);
    }

    public boolean isPlayerPlaying() {
        long start = System.currentTimeMillis();
        boolean r = this.mSdkVideoViewState.getPlayer() != null && this.mSdkVideoViewState.getPlayer().isPlaying();
        checkForFrameDrops("isPlayerPlaying", start);
        return r;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void start() throws IllegalStateException {
        start(true);
    }

    private void mediaOpStart() {
        synchronized (LOCK) {
            if (isInPlaybackState()) {
                long start = System.currentTimeMillis();
                try {
                    MediaPlayer player = this.mSdkVideoViewState.getPlayer();
                    if (player != null) {
                        player.start();
                    }
                } catch (IllegalStateException e) {
                    if (this.mExceptionHandler != null) {
                        this.mExceptionHandler.onProgrammingErrorException(e);
                    }
                }
                checkForFrameDrops("start", start);
                setCurrentState(6);
            }
        }
        if (this.mEnsureVisibilityWhenStart && getVisibility() != 0) {
            this.mUiOpsHandler.postDelayed(this.mVisibleStartRunnable, 40L);
        }
        this.mTargetState = 6;
    }

    public void start(boolean ensureVisibility) throws IllegalStateException {
        this.mEnsureVisibilityWhenStart = ensureVisibility;
        postMediaOp(2);
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        this.mUiOpsHandler.removeCallbacks(this.mVisibleStartRunnable);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void pause() throws IllegalStateException {
        if (isInPlaybackState()) {
            long start = System.currentTimeMillis();
            if (this.mSdkVideoViewState.getPlayer().isPlaying()) {
                this.mSdkVideoViewState.getPlayer().pause();
                sPlayingCount--;
                setCurrentState(7);
            }
            checkForFrameDrops("pause", start);
        }
        this.mTargetState = 7;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void suspend() throws IllegalStateException {
        release("suspend", false, true);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean hasStarted() {
        return isInPlaybackState();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isPaused() {
        return getCurrentState() == 7;
    }

    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void resume() throws IllegalStateException {
        this.mWasMuted = !this.mMuted;
        setMute(this.mMuted);
        start();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public int getDuration() {
        MediaPlayer player;
        try {
            if (!isInPlaybackState() || (player = this.mSdkVideoViewState.getPlayer()) == null) {
                return -1;
            }
            return player.getDuration();
        } catch (Exception e) {
            if (this.mExceptionHandler == null) {
                return -1;
            }
            this.mExceptionHandler.onUnexpectedException("It's ok. probably a race condition.", e);
            return -1;
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public int getCurrentPosition() {
        try {
            if (isInPlaybackState() && this.mSdkVideoViewState.getPlayer() != null) {
                return this.mSdkVideoViewState.getPlayer().getCurrentPosition();
            }
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void seekTo(int msec) throws IllegalStateException {
        postMediaOp(msec + 1000);
    }

    public void seekToDelayed(final int msec, int delay) {
        new Handler().postDelayed(new Runnable() { // from class: co.vine.android.player.SdkVideoView.11
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException {
                SdkVideoView.this.seekTo(msec);
            }
        }, delay);
    }

    public void mediaOpSeekTo(int msec) throws IllegalStateException {
        if (isInPlaybackState()) {
            if (!this.mIsSeeking) {
                this.mIsSeeking = true;
                MediaPlayer player = this.mSdkVideoViewState.getPlayer();
                if (player != null) {
                    player.seekTo(msec);
                }
            } else {
                this.mNextSeek = msec;
            }
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isPlaying() {
        try {
            if (isInPlaybackState()) {
                return isPlayerPlaying();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getBufferPercentage() {
        if (hasPlayer()) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public boolean hasControlOfPlayer() {
        return this.mSdkVideoViewState.hasControlOfPlayer(this, this.mUri);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isInPlaybackState() {
        return (!hasPlayer() || getCurrentState() == -1 || getCurrentState() == 1 || getCurrentState() == 2) ? false : true;
    }

    public boolean canPause() {
        return this.mIsPrepared;
    }

    public AttributeSet getAttributes() {
        return this.mAttributes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postMediaOp(int what) throws IllegalStateException {
        if (sRunMediaOpsOnSeparateThread) {
            Handler handler = this.mMediaOpHandler;
            if (handler != null) {
                handler.sendEmptyMessage(what);
                return;
            }
            return;
        }
        runMediaOps(what);
        if (what == 2) {
            onPlay();
        }
    }

    private void removePendingMediaOps() {
        Handler handler = this.mMediaOpHandler;
        this.mIsSeeking = false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.View
    protected void onDetachedFromWindow() throws IllegalStateException {
        this.mUiOpsHandler.removeCallbacks(this.mVisibleStartRunnable);
        release("onDetachedFromWindow", true, false);
        super.onDetachedFromWindow();
        this.mMediaOpHandler = null;
        if (this.mMediaOpThread != null) {
            this.mMediaOpThread.quit();
            this.mMediaOpThread = null;
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setMute(boolean mute) throws IllegalStateException {
        this.mMuted = mute;
        postMediaOp(this.mMuted ? 3 : 4);
    }

    private void mediaOpChangeVolume(float volume) {
        synchronized (LOCK) {
            if (isInPlaybackState() && this.mWasMuted != this.mMuted) {
                MediaPlayer player = this.mSdkVideoViewState.getPlayer();
                if (player != null) {
                    try {
                        player.setVolume(volume, volume);
                    } catch (IllegalStateException e) {
                        if (this.mExceptionHandler != null) {
                            this.mExceptionHandler.onUnexpectedException(null, e);
                        }
                    }
                }
                this.mWasMuted = this.mMuted;
            }
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isPathPlaying(String newPath) {
        return hasControlOfPlayer() && super.isPathPlaying(newPath);
    }

    private void mediaOpOpen() {
        Handler handler;
        synchronized (LOCK) {
            this.mSdkVideoViewState.setCurrentView(this);
            if (this.mUri == null) {
                Log.d("TextureVideoView", "mUri  is not ready yet.");
                return;
            }
            SurfaceTexture texture = null;
            if (isAvailable()) {
                texture = getSurfaceTexture();
            }
            if (texture == null) {
                boolean show = this.mAutoShow && getVisibility() != 0;
                Log.d("TextureVideoView", "Surface is not ready yet, will autoshow ? " + this.mAutoShow + " & " + getVisibility());
                if (show && (handler = getHandler()) != null) {
                    handler.post(new Runnable() { // from class: co.vine.android.player.SdkVideoView.12
                        @Override // java.lang.Runnable
                        public void run() {
                            SdkVideoView.this.setVisibility(0);
                        }
                    });
                }
                return;
            }
            Surface surface = new Surface(texture);
            Log.d("TextureVideoView", "Opening video: " + this.mUri);
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            this.mContext.sendBroadcast(i);
            release("open", false, true);
            openMediaPlayer(surface);
        }
    }

    private void mediaOpRelease() throws IllegalStateException {
        MediaPlayer player = this.mSdkVideoViewState.getPlayer();
        if (player != null) {
            try {
                long start = System.currentTimeMillis();
                if (player.isPlaying()) {
                    player.stop();
                    sPlayingCount--;
                }
                checkForFrameDrops(this.mReleaseTag + " stop", start);
            } catch (IllegalStateException e) {
            }
            try {
                long start2 = System.currentTimeMillis();
                player.reset();
                checkForFrameDrops(this.mReleaseTag + " reset", start2);
            } catch (IllegalStateException e2) {
            } catch (NullPointerException e3) {
            }
            long start3 = System.currentTimeMillis();
            this.mSdkVideoViewState.onMediaOpRelease(player);
            checkForFrameDrops(this.mReleaseTag + " release", start3);
            this.mIsPrepared = false;
            setCurrentState(1);
            if (this.mShouldClearState) {
                this.mTargetState = 1;
                this.mPath = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runMediaOps(int what) throws IllegalStateException {
        if (what >= 1000) {
            mediaOpSeekTo(what + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
        }
        switch (what) {
            case 0:
                mediaOpRelease();
                break;
            case 1:
                mediaOpOpen();
                break;
            case 2:
                mediaOpStart();
                break;
            case 3:
                mediaOpChangeVolume(0.0f);
                break;
            case 4:
                mediaOpChangeVolume(1.0f);
                break;
        }
    }

    public static void releaseStaticIfNeeded() {
        synchronized (LOCK) {
            SinglePlayerState.getInstance().release();
        }
    }

    public static Uri getCurrentUri() {
        return SinglePlayerState.getInstance().getCurrentUri();
    }

    private static class MediaOpCallback implements Handler.Callback {
        private final WeakReference<SdkVideoView> mObject;

        public MediaOpCallback(SdkVideoView view) {
            this.mObject = new WeakReference<>(view);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) throws IllegalStateException {
            SdkVideoView object = this.mObject.get();
            if (object != null) {
                object.runMediaOps(msg.what);
                return true;
            }
            throw new IllegalStateException("Media Op ISE: " + msg.what);
        }
    }

    private static class PlayerMaker implements Runnable {
        private MediaPlayer mMp;

        private PlayerMaker() {
        }

        synchronized MediaPlayer getPlayer() {
            return this.mMp;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mMp = new MediaPlayer();
        }

        synchronized boolean isReady() {
            return getPlayer() != null;
        }
    }

    static abstract class SdkVideoViewState {
        private MediaPlayer mPlayer = null;
        private int mCurrentState = 1;

        abstract boolean hasControlOfPlayer(View view, Uri uri);

        abstract void onMediaOpRelease(MediaPlayer mediaPlayer);

        abstract boolean requiresLockOnStopPlayback();

        abstract void setCurrentView(View view);

        abstract void setUri(Uri uri);

        SdkVideoViewState() {
        }

        MediaPlayer getPlayer() {
            return this.mPlayer;
        }

        void setPlayer(MediaPlayer player) {
            this.mPlayer = player;
        }

        void setCurrentState(int state) {
            this.mCurrentState = state;
        }

        int getCurrentState() {
            return this.mCurrentState;
        }
    }
}
