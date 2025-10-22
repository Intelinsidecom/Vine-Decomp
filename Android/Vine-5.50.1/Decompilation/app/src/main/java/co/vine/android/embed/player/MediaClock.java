package co.vine.android.embed.player;

import android.os.SystemClock;

/* loaded from: classes.dex */
final class MediaClock {
    private long mAudioLatencyUs;
    private long mAudioPresentationTimeUs;
    private long mCurrentOperationStartMs;
    private long mLastOperationStartMs;
    private long mLastPositionSetTimeMs;
    private long mLastPositionUs;
    private long mLastResetTimeMs;
    private long mPositionSetTimeMs;
    private long mPositionUs;

    MediaClock() {
    }

    public void reset() {
        this.mCurrentOperationStartMs = 0L;
        this.mLastOperationStartMs = 0L;
        this.mAudioPresentationTimeUs = 0L;
        this.mPositionSetTimeMs = 0L;
        this.mPositionUs = 0L;
    }

    public void setPosition(long positionUs) {
        if (positionUs != this.mPositionUs) {
            this.mPositionUs = positionUs;
            this.mPositionSetTimeMs = SystemClock.elapsedRealtime();
            if (positionUs - this.mAudioLatencyUs > 0) {
                this.mLastPositionUs = positionUs - this.mAudioLatencyUs;
                this.mLastPositionSetTimeMs = this.mPositionSetTimeMs;
            }
        }
    }

    public boolean hasNegativePosition() {
        return 1000 * (SystemClock.elapsedRealtime() - this.mLastResetTimeMs) < this.mAudioLatencyUs + 110000;
    }

    public long positionUs() {
        return this.mPositionUs;
    }

    public long getLatencyAdjustedPositionUs() {
        return this.mLastPositionUs;
    }

    public void operationStart() {
        this.mCurrentOperationStartMs = SystemClock.elapsedRealtime();
    }

    public void operationEnd() {
        this.mLastOperationStartMs = this.mCurrentOperationStartMs;
    }

    public long getOperationStartMs() {
        return this.mCurrentOperationStartMs;
    }

    public boolean isFirstOperation() {
        return this.mLastOperationStartMs <= 0;
    }

    public long timeSinceLastOperationStartUs() {
        return (SystemClock.elapsedRealtime() - this.mLastOperationStartMs) * 1000;
    }

    public long getPositionSetTimeMs() {
        return this.mPositionSetTimeMs;
    }

    public long getAudioPresentationTimeUs() {
        return this.mAudioPresentationTimeUs;
    }

    public void incrementAudioPresentationTimeUs(long timeUs) {
        this.mAudioPresentationTimeUs += timeUs;
    }

    public void setAudioLatency(long latencyUs) {
        this.mAudioLatencyUs = latencyUs;
    }

    public long getLatencyAdjustedPositionSetMs() {
        return this.mLastPositionSetTimeMs;
    }

    public void updateAudioResetTime() {
        this.mLastResetTimeMs = this.mPositionSetTimeMs;
    }
}
