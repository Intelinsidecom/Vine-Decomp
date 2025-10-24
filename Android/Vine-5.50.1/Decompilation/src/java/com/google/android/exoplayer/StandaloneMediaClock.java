package com.google.android.exoplayer;

import android.os.SystemClock;

/* loaded from: classes.dex */
final class StandaloneMediaClock implements MediaClock {
    private long deltaUs;
    private long positionUs;
    private boolean started;

    StandaloneMediaClock() {
    }

    public void start() {
        if (!this.started) {
            this.started = true;
            this.deltaUs = elapsedRealtimeMinus(this.positionUs);
        }
    }

    public void stop() {
        if (this.started) {
            this.positionUs = elapsedRealtimeMinus(this.deltaUs);
            this.started = false;
        }
    }

    public void setPositionUs(long timeUs) {
        this.positionUs = timeUs;
        this.deltaUs = elapsedRealtimeMinus(timeUs);
    }

    @Override // com.google.android.exoplayer.MediaClock
    public long getPositionUs() {
        return this.started ? elapsedRealtimeMinus(this.deltaUs) : this.positionUs;
    }

    private long elapsedRealtimeMinus(long toSubtractUs) {
        return (SystemClock.elapsedRealtime() * 1000) - toSubtractUs;
    }
}
