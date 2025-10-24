package com.google.android.exoplayer;

/* loaded from: classes.dex */
public interface ExoPlayer {

    public interface ExoPlayerComponent {
        void handleMessage(int i, Object obj) throws ExoPlaybackException;
    }

    public interface Listener {
        void onPlayWhenReadyCommitted();

        void onPlayerError(ExoPlaybackException exoPlaybackException);

        void onPlayerStateChanged(boolean z, int i);
    }

    void addListener(Listener listener);

    void blockingSendMessage(ExoPlayerComponent exoPlayerComponent, int i, Object obj);

    int getBufferedPercentage();

    long getCurrentPosition();

    long getDuration();

    boolean getPlayWhenReady();

    int getPlaybackState();

    void prepare(TrackRenderer... trackRendererArr);

    void release();

    void seekTo(long j);

    void sendMessage(ExoPlayerComponent exoPlayerComponent, int i, Object obj);

    void setPlayWhenReady(boolean z);

    void stop();

    public static final class Factory {
        public static ExoPlayer newInstance(int rendererCount, int minBufferMs, int minRebufferMs) {
            return new ExoPlayerImpl(rendererCount, minBufferMs, minRebufferMs);
        }
    }
}
