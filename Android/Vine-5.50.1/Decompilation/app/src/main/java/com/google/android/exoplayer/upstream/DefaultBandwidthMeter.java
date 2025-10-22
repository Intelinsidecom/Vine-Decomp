package com.google.android.exoplayer.upstream;

import android.os.Handler;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Clock;
import com.google.android.exoplayer.util.SlidingPercentile;
import com.google.android.exoplayer.util.SystemClock;

/* loaded from: classes.dex */
public final class DefaultBandwidthMeter implements BandwidthMeter {
    private long bitrateEstimate;
    private long bytesAccumulator;
    private final Clock clock;
    private final Handler eventHandler;
    private final BandwidthMeter.EventListener eventListener;
    private final SlidingPercentile slidingPercentile;
    private long startTimeMs;
    private int streamCount;

    public DefaultBandwidthMeter() {
        this(null, null);
    }

    public DefaultBandwidthMeter(Handler eventHandler, BandwidthMeter.EventListener eventListener) {
        this(eventHandler, eventListener, new SystemClock());
    }

    public DefaultBandwidthMeter(Handler eventHandler, BandwidthMeter.EventListener eventListener, Clock clock) {
        this(eventHandler, eventListener, clock, 2000);
    }

    public DefaultBandwidthMeter(Handler eventHandler, BandwidthMeter.EventListener eventListener, Clock clock, int maxWeight) {
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.clock = clock;
        this.slidingPercentile = new SlidingPercentile(maxWeight);
        this.bitrateEstimate = -1L;
    }

    @Override // com.google.android.exoplayer.upstream.BandwidthMeter
    public synchronized long getBitrateEstimate() {
        return this.bitrateEstimate;
    }

    @Override // com.google.android.exoplayer.upstream.TransferListener
    public synchronized void onTransferStart() {
        if (this.streamCount == 0) {
            this.startTimeMs = this.clock.elapsedRealtime();
        }
        this.streamCount++;
    }

    @Override // com.google.android.exoplayer.upstream.TransferListener
    public synchronized void onBytesTransferred(int bytes) {
        this.bytesAccumulator += bytes;
    }

    @Override // com.google.android.exoplayer.upstream.TransferListener
    public synchronized void onTransferEnd() {
        Assertions.checkState(this.streamCount > 0);
        long nowMs = this.clock.elapsedRealtime();
        int elapsedMs = (int) (nowMs - this.startTimeMs);
        if (elapsedMs > 0) {
            float bitsPerSecond = (this.bytesAccumulator * 8000) / elapsedMs;
            this.slidingPercentile.addSample((int) Math.sqrt(this.bytesAccumulator), bitsPerSecond);
            float bandwidthEstimateFloat = this.slidingPercentile.getPercentile(0.5f);
            this.bitrateEstimate = Float.isNaN(bandwidthEstimateFloat) ? -1L : (long) bandwidthEstimateFloat;
            notifyBandwidthSample(elapsedMs, this.bytesAccumulator, this.bitrateEstimate);
        }
        this.streamCount--;
        if (this.streamCount > 0) {
            this.startTimeMs = nowMs;
        }
        this.bytesAccumulator = 0L;
    }

    private void notifyBandwidthSample(final int elapsedMs, final long bytes, final long bitrate) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.upstream.DefaultBandwidthMeter.1
                @Override // java.lang.Runnable
                public void run() {
                    DefaultBandwidthMeter.this.eventListener.onBandwidthSample(elapsedMs, bytes, bitrate);
                }
            });
        }
    }
}
