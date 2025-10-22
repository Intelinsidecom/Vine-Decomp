package com.google.android.exoplayer;

/* loaded from: classes.dex */
public final class DummyTrackRenderer extends TrackRenderer {
    @Override // com.google.android.exoplayer.TrackRenderer
    protected boolean doPrepare(long positionUs) throws ExoPlaybackException {
        return true;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected int getTrackCount() {
        return 0;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected MediaFormat getFormat(int track) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected boolean isEnded() {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected boolean isReady() {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void seekTo(long positionUs) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void doSomeWork(long positionUs, long elapsedRealtimeUs) {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void maybeThrowError() {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected long getDurationUs() {
        throw new IllegalStateException();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected long getBufferedPositionUs() {
        throw new IllegalStateException();
    }
}
