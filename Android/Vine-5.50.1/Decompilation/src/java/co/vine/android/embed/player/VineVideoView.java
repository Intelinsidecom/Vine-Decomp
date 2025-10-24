package co.vine.android.embed.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.embed.player.VinePlayer;
import java.io.File;
import java.lang.Thread;

/* loaded from: classes.dex */
public class VineVideoView extends VideoViewInterface implements VideoViewInterface.OnTextureReadinessChangedListener {
    private GLErrorListener mOnGlErrorListener;
    private final VinePlayer mPlayer;
    private TextureViewRenderer mRenderer;
    private final int[] mRendererAccessLock;

    public interface GLErrorListener {
        void onGlError(VineVideoView vineVideoView, RuntimeException runtimeException);
    }

    public VineVideoView(Context context) {
        this(context, null);
    }

    public VineVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRendererAccessLock = new int[0];
        this.mPlayer = new VinePlayer(context, new VineVideoViewEventListener());
        setSurfaceReadyListener(this);
    }

    public void setOnGLErrorListener(GLErrorListener listener) {
        this.mOnGlErrorListener = listener;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void onPause() {
        super.onPause();
        releaseRenderer();
    }

    private void releaseRenderer() {
        synchronized (this.mRendererAccessLock) {
            try {
                if (this.mRenderer != null) {
                    this.mRenderer.halt();
                    this.mRenderer.join();
                    this.mRenderer = null;
                }
            } catch (InterruptedException e) {
            }
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        synchronized (this.mRendererAccessLock) {
            TextureViewRenderer renderer = this.mRenderer;
            if (renderer != null) {
                renderer.queueSetSizeEvent(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setSize(int width, int height) {
        super.setSize(width, height);
        synchronized (this.mRendererAccessLock) {
            TextureViewRenderer renderer = this.mRenderer;
            if (renderer != null) {
                renderer.queueSetSizeEvent(width, height);
            }
        }
    }

    public VinePlayer getPlayer() {
        return this.mPlayer;
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void seekTo(int ms) {
        throw new UnsupportedOperationException("Seek is not yet supported in Vine Player");
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean retryOpenVideo(boolean forced) {
        if (isAvailable()) {
            startOpenVideo();
        }
        return !this.mPlayer.isIdle();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.View
    protected void onDetachedFromWindow() {
        stopPlayback();
        releaseRenderer();
        super.onDetachedFromWindow();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void startOpenVideo() {
        Uri uri = Uri.fromFile(new File(this.mPath));
        if (uri.equals(this.mUri)) {
            if (this.mPlayer.isIdle()) {
                prepareAsyncIfReady();
                return;
            } else {
                Log.w("TextureVideoView", "Player was not idle, open cancelled.");
                return;
            }
        }
        if (this.mPlayer.isPlayable()) {
            this.mPlayer.reset();
        }
        this.mUri = uri;
        prepareAsyncIfReady();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setVideoPath(String path) {
        super.setVideoPath(path);
        startOpenVideo();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setVideoPathDirect(String path) {
        super.setVideoPathDirect(path);
        startOpenVideo();
    }

    @Override // android.view.TextureView
    public boolean isAvailable() {
        TextureViewRenderer renderer = this.mRenderer;
        return renderer != null && super.isAvailable() && renderer.isSurfaceCreated() && this.mAttached && this.mPlayer.isSurfaceReady();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setAutoPlayOnPrepared(boolean autoPlay) {
        this.mPlayer.setPlayWhenReady(autoPlay);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void start() {
        this.mPlayer.resume();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void resume() {
        ensureRenderer();
        this.mPlayer.resume();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public int getCurrentPosition() {
        return this.mPlayer.getCurrentPosition();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void pause() {
        this.mPlayer.pause();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void suspend() {
        this.mPlayer.reset();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isPlaying() {
        return this.mPlayer.isPlaying();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isPaused() {
        return this.mPlayer.isPaused();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean hasStarted() {
        return !this.mPlayer.isIdle();
    }

    public void stopPlayback() {
        this.mPlayer.reset();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public boolean isInPlaybackState() {
        return this.mPlayer.isPlayable();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public int getDuration() {
        return this.mPlayer.getDuration();
    }

    @Override // co.vine.android.embed.player.VideoViewInterface
    public void setMute(boolean mute) {
        this.mPlayer.setVolume(mute ? 0.0f : 1.0f);
    }

    private void prepareAsyncIfReady() {
        if (sLogsOn) {
            Log.i("TextureVideoView", "Prepare: ");
            Log.i("TextureVideoView", "Avaialable? " + super.isAvailable());
            Log.i("TextureVideoView", "Running? " + (this.mRenderer != null && this.mRenderer.isAlive()));
        }
        ensureRenderer();
        if (this.mUri != null) {
            if (isAvailable() && !this.mPlayer.isPlayable()) {
                this.mPlayer.reset();
            }
            this.mPlayer.prepareAsync(this.mUri, true);
            if (sLogsOn) {
                Log.i("TextureVideoView", this + "Preparing...");
                return;
            }
            return;
        }
        throw new IllegalStateException("Uri is not set.");
    }

    private void ensureRenderer() {
        synchronized (this.mRendererAccessLock) {
            if (this.mRenderer == null || this.mRenderer.getState() == Thread.State.TERMINATED) {
                if (sLogsOn) {
                    Log.i("TextureVideoView", "Creating renderer thread.");
                }
                this.mRenderer = this.mPlayer.createRenderer("Renderer_" + (hashCode() % 10));
                this.mRenderer.queueSetSizeEvent(getMeasuredWidth(), getMeasuredHeight());
                this.mRenderer.onTextureReadinessChanged(this, getSurfaceTexture(), getSurfaceTexture() != null);
                this.mRenderer.start();
            }
        }
    }

    @Override // co.vine.android.embed.player.VideoViewInterface.OnTextureReadinessChangedListener
    public void onTextureReadinessChanged(VideoViewInterface videoView, SurfaceTexture surface, boolean isReady) {
        synchronized (this.mRendererAccessLock) {
            if (this.mRenderer != null) {
                this.mRenderer.onTextureReadinessChanged(videoView, surface, isReady);
                if (!isReady) {
                    this.mPlayer.reset();
                }
            }
        }
    }

    private class VineVideoViewEventListener implements VinePlayer.EventListener {
        private VineVideoViewEventListener() {
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onSurfaceUpdated() {
            if (VineVideoView.this.mSurfaceUpdatedListener != null) {
                VineVideoView.this.mSurfaceUpdatedListener.onSurfaceUpdated();
            }
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onError(PlayerState state, int operation, Throwable e) {
            if (VineVideoView.this.mOnErrorListener != null) {
                VineVideoView.this.mOnErrorListener.onError(VineVideoView.this, state.ordinal(), operation);
            }
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onPastError(Exception error) {
            if (VineVideoView.this.mOnErrorListener != null) {
                VineVideoView.this.mOnErrorListener.onError(VineVideoView.this, -1, 0);
            }
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onVideoSizeChanged(int newWidth, int newHeight) {
            VineVideoView.this.mVideoWidth = newWidth;
            VineVideoView.this.mVideoHeight = newHeight;
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onLoopCompleted() {
            if (VineVideoView.this.mOnCompleteListener != null) {
                VineVideoView.this.mOnCompleteListener.onCompletion(VineVideoView.this);
            }
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onPrepared() {
            if (VineVideoView.this.mOnPreparedListener != null) {
                VineVideoView.this.mOnPreparedListener.onPrepared(VineVideoView.this);
            }
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onRendererReadinessChanged(boolean isReady) {
        }

        @Override // co.vine.android.embed.player.VinePlayer.EventListener
        public void onGLError(RuntimeException e) {
            Log.e("TextureVideoView", "Error occured with " + VineVideoView.this.getSurfaceTexture());
            if (VineVideoView.this.mOnGlErrorListener != null) {
                VineVideoView.this.mOnGlErrorListener.onGlError(VineVideoView.this, e);
            }
        }
    }
}
