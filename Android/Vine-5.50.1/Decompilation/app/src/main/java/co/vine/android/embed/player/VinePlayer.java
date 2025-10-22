package co.vine.android.embed.player;

import android.content.Context;
import android.net.Uri;
import co.vine.android.embed.player.VinePlayerInternal;

/* loaded from: classes.dex */
public class VinePlayer {
    private final EventListener mListener;
    private final VinePlayerInternal mPlayer;

    public interface EventListener {
        void onError(PlayerState playerState, int i, Throwable th);

        void onGLError(RuntimeException runtimeException);

        void onLoopCompleted();

        void onPastError(Exception exc);

        void onPrepared();

        void onRendererReadinessChanged(boolean z);

        void onSurfaceUpdated();

        void onVideoSizeChanged(int i, int i2);
    }

    public VinePlayer(Context context, EventListener listener) {
        this.mPlayer = new VinePlayerInternal(context, new VineInternalListener());
        this.mListener = listener;
    }

    public void prepareAsync(Uri uri, boolean playWhenReady) {
        this.mPlayer.setUri(uri);
        this.mPlayer.setPlayWhenReady(playWhenReady);
        this.mPlayer.prepareAsync();
    }

    public boolean isPlaying() {
        return this.mPlayer.isPlaying();
    }

    public boolean isPaused() {
        return this.mPlayer.getState() == PlayerState.PLAYABLE && !this.mPlayer.playWhenReady();
    }

    public int getCurrentPosition() {
        return this.mPlayer.getCurrentPositionMs();
    }

    public boolean isSurfaceReady() {
        return this.mPlayer.isSurfaceReady();
    }

    public void resume() {
        this.mPlayer.setPlayWhenReady(true);
    }

    public void reset() {
        this.mPlayer.reset();
    }

    public boolean isIdle() {
        return this.mPlayer.isIdle();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.mPlayer.setPlayWhenReady(playWhenReady);
    }

    public TextureViewRenderer createRenderer(String tag) {
        return this.mPlayer.createRenderer(tag);
    }

    public int getDuration() {
        return (int) this.mPlayer.getDurationMs();
    }

    public void pause() {
        this.mPlayer.setPlayWhenReady(false);
    }

    public void setVolume(float gain) {
        this.mPlayer.setVolume(gain);
    }

    public boolean isPlayable() {
        return this.mPlayer.isPlayable();
    }

    private class VineInternalListener implements VinePlayerInternal.EventListener {
        private VineInternalListener() {
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onError(PlayerState state, int operation, Throwable e) {
            VinePlayer.this.mListener.onError(state, operation, e);
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onVideoSizeChanged(int newWidth, int newHeight) {
            VinePlayer.this.mListener.onVideoSizeChanged(newWidth, newHeight);
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onLoopCompleted() {
            VinePlayer.this.mListener.onLoopCompleted();
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onPrepared() {
            VinePlayer.this.mListener.onPrepared();
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onSurfaceUpdated() {
            VinePlayer.this.mListener.onSurfaceUpdated();
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onPastError(Exception error) {
            VinePlayer.this.mListener.onPastError(error);
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onRendererReadinessChanged(boolean isReady) {
            VinePlayer.this.mListener.onRendererReadinessChanged(isReady);
        }

        @Override // co.vine.android.embed.player.VinePlayerInternal.EventListener
        public void onGLError(RuntimeException e) {
            VinePlayer.this.mListener.onGLError(e);
        }
    }
}
