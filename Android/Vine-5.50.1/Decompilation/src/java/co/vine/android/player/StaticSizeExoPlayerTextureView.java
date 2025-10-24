package co.vine.android.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import java.lang.Thread;

@TargetApi(14)
/* loaded from: classes.dex */
public class StaticSizeExoPlayerTextureView extends TextureView implements TextureView.SurfaceTextureListener, ExoPlayer.Listener, MediaCodecVideoTrackRenderer.EventListener {
    private MediaCodecAudioTrackRenderer mAudioRenderer;
    private ExoPlayerErrorListener mErrorListener;
    private Uri mFileUri;
    private Handler mHandler;
    private int mHeight;
    private Thread mInitThread;
    private int mLastState;
    private boolean mLooping;
    private boolean mMuted;
    private OnPreparedListener mOnPreparedListener;
    private OnReadyListener mOnReadyListener;
    private PlaybackEndedListener mPlaybackEndedListener;
    private ExoPlayer mPlayer;
    private final int[] mPlayerInitLock;
    private Runnable mPlayerInitRunnable;
    private VideoSizeChangedListener mSizeListener;
    private SurfaceTexture mSurfaceTexture;
    private MediaCodecVideoTrackRenderer mVideoRenderer;
    private int mWidth;

    public interface ExoPlayerErrorListener {
        void onError(ExoPlaybackException exoPlaybackException);
    }

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnReadyListener {
        void onReady();
    }

    public interface PlaybackEndedListener {
        void onPlaybackEnded();
    }

    public interface VideoSizeChangedListener {
        void onVideoSizeChanged(ExoPlayer exoPlayer, int i, int i2, int i3);
    }

    public void setPlaybackEndedListener(PlaybackEndedListener playbackEndedListener) {
        this.mPlaybackEndedListener = playbackEndedListener;
    }

    public void setVideoSizeChangedListener(VideoSizeChangedListener videoSizeChangedListener) {
        this.mSizeListener = videoSizeChangedListener;
    }

    public void setExoPlayerErrorListener(ExoPlayerErrorListener errorListener) {
        this.mErrorListener = errorListener;
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setOnReadyListener(OnReadyListener listener) {
        this.mOnReadyListener = listener;
    }

    public StaticSizeExoPlayerTextureView(Context context) {
        super(context);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFileUri = null;
        this.mLooping = false;
        this.mMuted = false;
        this.mLastState = -1;
        this.mPlayerInitLock = new int[0];
        this.mPlayerInitRunnable = new Runnable() { // from class: co.vine.android.player.StaticSizeExoPlayerTextureView.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (StaticSizeExoPlayerTextureView.this.mPlayerInitLock) {
                    while (StaticSizeExoPlayerTextureView.this.mFileUri == null) {
                        try {
                            StaticSizeExoPlayerTextureView.this.mPlayerInitLock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    StaticSizeExoPlayerTextureView.this.setRenderersAndPreparePlayer();
                }
            }
        };
        init();
    }

    public StaticSizeExoPlayerTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFileUri = null;
        this.mLooping = false;
        this.mMuted = false;
        this.mLastState = -1;
        this.mPlayerInitLock = new int[0];
        this.mPlayerInitRunnable = new Runnable() { // from class: co.vine.android.player.StaticSizeExoPlayerTextureView.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (StaticSizeExoPlayerTextureView.this.mPlayerInitLock) {
                    while (StaticSizeExoPlayerTextureView.this.mFileUri == null) {
                        try {
                            StaticSizeExoPlayerTextureView.this.mPlayerInitLock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    StaticSizeExoPlayerTextureView.this.setRenderersAndPreparePlayer();
                }
            }
        };
        init();
    }

    public StaticSizeExoPlayerTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFileUri = null;
        this.mLooping = false;
        this.mMuted = false;
        this.mLastState = -1;
        this.mPlayerInitLock = new int[0];
        this.mPlayerInitRunnable = new Runnable() { // from class: co.vine.android.player.StaticSizeExoPlayerTextureView.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (StaticSizeExoPlayerTextureView.this.mPlayerInitLock) {
                    while (StaticSizeExoPlayerTextureView.this.mFileUri == null) {
                        try {
                            StaticSizeExoPlayerTextureView.this.mPlayerInitLock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    StaticSizeExoPlayerTextureView.this.setRenderersAndPreparePlayer();
                }
            }
        };
        init();
    }

    private void init() {
        this.mInitThread = new Thread(this.mPlayerInitRunnable);
        this.mHandler = new Handler(Looper.getMainLooper());
        setSurfaceTextureListener(this);
    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public void openVideo(Uri uri) {
        ExoPlayer player = this.mPlayer;
        if (player != null) {
            player.release();
        }
        ExoPlayer player2 = ExoPlayer.Factory.newInstance(2, 1000, 5000);
        player2.addListener(this);
        this.mPlayer = player2;
        this.mFileUri = uri;
        synchronized (this.mPlayerInitLock) {
            if (this.mInitThread.isAlive()) {
                this.mPlayerInitLock.notify();
            } else if (this.mInitThread.getState() != Thread.State.NEW) {
                this.mInitThread = new Thread(this.mPlayerInitRunnable);
                this.mInitThread.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setRenderersAndPreparePlayer() {
        FrameworkSampleSource sampleSource = new FrameworkSampleSource(getContext(), this.mFileUri, null);
        final MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(getContext(), sampleSource, MediaCodecSelector.DEFAULT, 1, 0L, this.mHandler, this, 50);
        final MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);
        this.mVideoRenderer = videoRenderer;
        this.mAudioRenderer = audioRenderer;
        post(new Runnable() { // from class: co.vine.android.player.StaticSizeExoPlayerTextureView.1
            @Override // java.lang.Runnable
            public void run() {
                StaticSizeExoPlayerTextureView.this.setPlayerSurface();
                if (StaticSizeExoPlayerTextureView.this.mPlayer != null) {
                    StaticSizeExoPlayerTextureView.this.mPlayer.prepare(videoRenderer, audioRenderer);
                    StaticSizeExoPlayerTextureView.this.mPlayer.seekTo(0L);
                    StaticSizeExoPlayerTextureView.this.mPlayer.setPlayWhenReady(true);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayerSurface() {
        if (this.mPlayer != null && this.mVideoRenderer != null && this.mSurfaceTexture != null) {
            this.mPlayer.sendMessage(this.mVideoRenderer, 1, new Surface(this.mSurfaceTexture));
        }
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (this.mOnPreparedListener != null && this.mLastState == 2 && playbackState == 3) {
            SLog.d("Notify onPrepare");
            this.mOnPreparedListener.onPrepared();
        }
        if (this.mOnReadyListener != null && this.mLastState == 3 && playbackState == 4) {
            SLog.d("Notify onReady");
            this.mOnReadyListener.onReady();
        }
        if (this.mPlaybackEndedListener != null && this.mLastState == 4 && playbackState == 5) {
            SLog.d("Notify onPlaybackEnded()");
            this.mPlaybackEndedListener.onPlaybackEnded();
        }
        this.mLastState = playbackState;
        SLog.d("mjama  player state changed, playbackState={}", Integer.valueOf(playbackState));
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayWhenReadyCommitted() {
    }

    @Override // com.google.android.exoplayer.ExoPlayer.Listener
    public void onPlayerError(ExoPlaybackException error) {
        if (this.mErrorListener != null) {
            this.mErrorListener.onError(error);
        }
        CrashUtil.logException(error);
        SLog.e(error.getMessage(), (Throwable) error);
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onDroppedFrames(int count, long elapsed) {
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (this.mSizeListener != null) {
            this.mSizeListener.onVideoSizeChanged(this.mPlayer, width, height, unappliedRotationDegrees);
        }
    }

    @Override // com.google.android.exoplayer.MediaCodecVideoTrackRenderer.EventListener
    public void onDrawnToSurface(Surface surface) {
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onCryptoError(MediaCodec.CryptoException e) {
    }

    @Override // com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        SLog.d("ryango surface available");
        this.mSurfaceTexture = surfaceTexture;
        Thread thread = this.mInitThread;
        if (thread != null && !thread.isAlive() && thread.getState() == Thread.State.NEW) {
            thread.start();
        } else {
            setPlayerSurface();
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
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (this.mSurfaceTexture != surfaceTexture) {
            this.mSurfaceTexture = surfaceTexture;
            setPlayerSurface();
        }
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        if (this.mPlayer != null) {
            this.mPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    public boolean getPlayWhenReady() {
        if (this.mPlayer != null) {
            return this.mPlayer.getPlayWhenReady();
        }
        return false;
    }

    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }

    public void seekTo(int ms) {
        if (this.mPlayer != null) {
            this.mPlayer.seekTo(ms);
        }
    }

    public int getCurrentPosition() {
        if (this.mPlayer != null) {
            return (int) this.mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void setSurfaceRotation(int rotation) {
        Matrix m = new Matrix();
        m.postRotate(rotation, this.mWidth / 2, this.mHeight / 2);
        if (rotation == 90 || rotation == 270) {
            float ratio = this.mWidth / this.mHeight;
            if (ratio > 0.0f) {
                m.postScale(ratio, 1.0f / ratio, this.mWidth / 2, this.mHeight / 2);
            }
        }
        setTransform(m);
    }

    public void release() {
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer = null;
        }
        Thread initThread = this.mInitThread;
        if (initThread != null) {
            Thread.State state = initThread.getState();
            if (state == Thread.State.WAITING || state == Thread.State.BLOCKED) {
                initThread.interrupt();
            }
            this.mInitThread = null;
        }
    }

    public void setMute(boolean mute) {
        if (this.mPlayer != null && this.mAudioRenderer != null) {
            this.mPlayer.sendMessage(this.mAudioRenderer, 1, Float.valueOf(mute ? 0.0f : 1.0f));
        }
        this.mMuted = mute;
    }
}
