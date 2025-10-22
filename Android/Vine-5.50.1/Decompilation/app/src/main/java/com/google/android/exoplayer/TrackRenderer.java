package com.google.android.exoplayer;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.util.Assertions;

/* loaded from: classes.dex */
public abstract class TrackRenderer implements ExoPlayer.ExoPlayerComponent {
    private int state;

    protected abstract boolean doPrepare(long j) throws ExoPlaybackException;

    protected abstract void doSomeWork(long j, long j2) throws ExoPlaybackException;

    protected abstract long getBufferedPositionUs();

    protected abstract long getDurationUs();

    protected abstract MediaFormat getFormat(int i);

    protected abstract int getTrackCount();

    protected abstract boolean isEnded();

    protected abstract boolean isReady();

    protected abstract void maybeThrowError() throws ExoPlaybackException;

    protected abstract void seekTo(long j) throws ExoPlaybackException;

    protected MediaClock getMediaClock() {
        return null;
    }

    protected final int getState() {
        return this.state;
    }

    final int prepare(long positionUs) throws ExoPlaybackException {
        Assertions.checkState(this.state == 0);
        this.state = doPrepare(positionUs) ? 1 : 0;
        return this.state;
    }

    final void enable(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        Assertions.checkState(this.state == 1);
        this.state = 2;
        onEnabled(track, positionUs, joining);
    }

    protected void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
    }

    final void start() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 3;
        onStarted();
    }

    protected void onStarted() throws ExoPlaybackException {
    }

    final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 3);
        this.state = 2;
        onStopped();
    }

    protected void onStopped() throws ExoPlaybackException {
    }

    final void disable() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        onDisabled();
    }

    protected void onDisabled() throws ExoPlaybackException {
    }

    final void release() throws ExoPlaybackException {
        Assertions.checkState((this.state == 2 || this.state == 3 || this.state == -1) ? false : true);
        this.state = -1;
        onReleased();
    }

    protected void onReleased() throws ExoPlaybackException {
    }

    @Override // com.google.android.exoplayer.ExoPlayer.ExoPlayerComponent
    public void handleMessage(int what, Object object) throws ExoPlaybackException {
    }
}
