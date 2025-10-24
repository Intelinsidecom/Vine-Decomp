package com.google.android.exoplayer.extractor.ts;

import com.googlecode.javacv.cpp.avutil;

/* loaded from: classes.dex */
public final class PtsTimestampAdjuster {
    private final long firstSampleTimestampUs;
    private volatile long lastPts = Long.MIN_VALUE;
    private long timestampOffsetUs;

    public PtsTimestampAdjuster(long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
    }

    public void reset() {
        this.lastPts = Long.MIN_VALUE;
    }

    public boolean isInitialized() {
        return this.lastPts != Long.MIN_VALUE;
    }

    public long adjustTimestamp(long pts) {
        if (this.lastPts != Long.MIN_VALUE) {
            long closestWrapCount = (this.lastPts + avutil.AV_CH_WIDE_RIGHT) / avutil.AV_CH_SURROUND_DIRECT_LEFT;
            long ptsWrapBelow = pts + (avutil.AV_CH_SURROUND_DIRECT_LEFT * (closestWrapCount - 1));
            long ptsWrapAbove = pts + (avutil.AV_CH_SURROUND_DIRECT_LEFT * closestWrapCount);
            pts = Math.abs(ptsWrapBelow - this.lastPts) < Math.abs(ptsWrapAbove - this.lastPts) ? ptsWrapBelow : ptsWrapAbove;
        }
        long timeUs = ptsToUs(pts);
        if (this.firstSampleTimestampUs != Long.MAX_VALUE && this.lastPts == Long.MIN_VALUE) {
            this.timestampOffsetUs = this.firstSampleTimestampUs - timeUs;
        }
        this.lastPts = pts;
        return this.timestampOffsetUs + timeUs;
    }

    public static long ptsToUs(long pts) {
        return (1000000 * pts) / 90000;
    }

    public static long usToPts(long us) {
        return (90000 * us) / 1000000;
    }
}
