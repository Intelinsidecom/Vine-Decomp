package co.vine.android.embed.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/* loaded from: classes.dex */
public abstract class VideoViewInterface extends TextureView implements TextureView.SurfaceTextureListener {
    protected static boolean sLogsOn;
    protected boolean mAttached;
    protected SilentExceptionHandler mExceptionHandler;
    protected int mHeight;
    protected OnCompletionListener mOnCompleteListener;
    protected OnErrorListener mOnErrorListener;
    protected OnPreparedListener mOnPreparedListener;
    private OnTextureReadinessChangedListener mOnTextureReadinessChangedListener;
    protected String mPath;
    protected int mPlayingPosition;
    private boolean mResumed;
    protected int mSurfaceHeight;
    protected SurfaceUpdatedListener mSurfaceUpdatedListener;
    protected int mSurfaceWidth;
    protected Uri mUri;
    protected int mVideoHeight;
    protected int mVideoWidth;
    protected int mWidth;

    public interface OnCompletionListener {
        void onCompletion(VideoViewInterface videoViewInterface);
    }

    public interface OnErrorListener {
        boolean onError(VideoViewInterface videoViewInterface, int i, int i2);
    }

    public interface OnPreparedListener {
        void onPrepared(VideoViewInterface videoViewInterface);
    }

    public interface OnTextureReadinessChangedListener {
        void onTextureReadinessChanged(VideoViewInterface videoViewInterface, SurfaceTexture surfaceTexture, boolean z);
    }

    public interface SilentExceptionHandler {
        void onExceptionDuringOpening(String str, Exception exc);

        void onExpectedException(String str, Exception exc);

        void onProgrammingErrorException(Exception exc);

        void onUnexpectedException(String str, Exception exc);
    }

    public interface SurfaceUpdatedListener {
        void onSurfaceUpdated();
    }

    public abstract int getCurrentPosition();

    public abstract int getDuration();

    public abstract boolean hasStarted();

    public abstract boolean isInPlaybackState();

    public abstract boolean isPaused();

    public abstract boolean isPlaying();

    public abstract void pause();

    public abstract void resume();

    public abstract boolean retryOpenVideo(boolean z);

    public abstract void seekTo(int i);

    public abstract void setAutoPlayOnPrepared(boolean z);

    public abstract void setMute(boolean z);

    public abstract void start();

    public abstract void startOpenVideo();

    public abstract void suspend();

    public VideoViewInterface(Context context) {
        this(context, null);
    }

    public VideoViewInterface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoViewInterface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPlayingPosition = -1;
        setSurfaceTextureListener(this);
    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void onResume() {
        this.mResumed = true;
    }

    public void onPause() {
        this.mResumed = false;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mWidth > 0 && this.mHeight > 0) {
            setMeasuredDimension(this.mWidth, this.mHeight);
            return;
        }
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            if (this.mVideoWidth * height > this.mVideoHeight * width) {
                height = (this.mVideoHeight * width) / this.mVideoWidth;
            } else if (this.mVideoWidth * height < this.mVideoHeight * width) {
                width = (this.mVideoWidth * height) / this.mVideoHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(getClass().getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(getClass().getName());
    }

    public boolean isPathPlaying(String newPath) {
        return this.mPath != null && this.mPath.equals(newPath);
    }

    public String getPath() {
        return this.mPath;
    }

    public void setVideoPath(String path) {
        this.mPath = path;
    }

    public void setVideoPathDirect(String path) {
        this.mPath = path;
    }

    public int getPlayingPosition() {
        return this.mPlayingPosition;
    }

    public void setPlayingPosition(int position) {
        this.mPlayingPosition = position;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setSurfaceUpdatedListener(SurfaceUpdatedListener listener) {
        this.mSurfaceUpdatedListener = listener;
    }

    public OnCompletionListener getOnCompletionListener() {
        return this.mOnCompleteListener;
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompleteListener = listener;
    }

    public SilentExceptionHandler getSilentExceptionHandler() {
        return this.mExceptionHandler;
    }

    public void setSilentExceptionHandler(SilentExceptionHandler handler) {
        this.mExceptionHandler = handler;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported) {
            if (keyCode == 79 || keyCode == 85) {
                if (isPlaying()) {
                    pause();
                    return true;
                }
                start();
                return true;
            }
            if (keyCode == 126) {
                if (isPlaying()) {
                    return true;
                }
                start();
                return true;
            }
            if (keyCode == 86 || keyCode == 127) {
                if (!isPlaying()) {
                    return true;
                }
                pause();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void setLogsOn(boolean logsOn) {
        sLogsOn = logsOn;
    }

    public void setSurfaceReadyListener(OnTextureReadinessChangedListener listener) {
        this.mOnTextureReadinessChangedListener = listener;
    }

    public OnTextureReadinessChangedListener getSurfaceReadyListener() {
        return this.mOnTextureReadinessChangedListener;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i("TextureVideoView", "Surface is ready.");
        if (this.mOnTextureReadinessChangedListener != null) {
            this.mOnTextureReadinessChangedListener.onTextureReadinessChanged(this, surface, true);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        this.mSurfaceWidth = width;
        this.mSurfaceHeight = height;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (this.mOnTextureReadinessChangedListener != null) {
            this.mOnTextureReadinessChangedListener.onTextureReadinessChanged(this, surface, false);
            return true;
        }
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (this.mSurfaceUpdatedListener != null) {
            this.mSurfaceUpdatedListener.onSurfaceUpdated();
        }
    }

    @Override // android.view.TextureView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttached = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {
            SurfaceTexture tx = null;
            boolean success = true;
            try {
                tx = getSurfaceTexture();
                if (tx != null) {
                    tx.release();
                }
            } catch (Exception e2) {
                success = false;
                if (this.mExceptionHandler != null) {
                    this.mExceptionHandler.onExpectedException("Failed to release.", e2);
                }
            }
            if (this.mExceptionHandler != null) {
                this.mExceptionHandler.onUnexpectedException("Failed to detach from window, but it's ok, since we won't use this anyways: " + success + " " + tx, e);
            }
        }
        this.mAttached = false;
    }
}
