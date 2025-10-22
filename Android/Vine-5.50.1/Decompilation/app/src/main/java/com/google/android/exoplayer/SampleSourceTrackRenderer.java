package com.google.android.exoplayer;

import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.SampleSource;
import java.io.IOException;
import java.util.Arrays;

/* loaded from: classes.dex */
public abstract class SampleSourceTrackRenderer extends TrackRenderer {
    private long durationUs;
    private SampleSource.SampleSourceReader enabledSource;
    private int enabledSourceTrackIndex;
    private int[] handledSourceIndices;
    private int[] handledSourceTrackIndices;
    private final SampleSource.SampleSourceReader[] sources;

    protected abstract void doSomeWork(long j, long j2, boolean z) throws ExoPlaybackException;

    protected abstract boolean handlesTrack(MediaFormat mediaFormat) throws MediaCodecUtil.DecoderQueryException;

    protected abstract void onDiscontinuity(long j) throws ExoPlaybackException;

    public SampleSourceTrackRenderer(SampleSource... sources) {
        this.sources = new SampleSource.SampleSourceReader[sources.length];
        for (int i = 0; i < sources.length; i++) {
            this.sources[i] = sources[i].register();
        }
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected final boolean doPrepare(long positionUs) throws ExoPlaybackException {
        boolean allSourcesPrepared = true;
        for (int i = 0; i < this.sources.length; i++) {
            allSourcesPrepared &= this.sources[i].prepare(positionUs);
        }
        if (!allSourcesPrepared) {
            return false;
        }
        int totalSourceTrackCount = 0;
        for (int i2 = 0; i2 < this.sources.length; i2++) {
            totalSourceTrackCount += this.sources[i2].getTrackCount();
        }
        long durationUs = 0;
        int handledTrackCount = 0;
        int[] handledSourceIndices = new int[totalSourceTrackCount];
        int[] handledTrackIndices = new int[totalSourceTrackCount];
        int sourceCount = this.sources.length;
        for (int sourceIndex = 0; sourceIndex < sourceCount; sourceIndex++) {
            SampleSource.SampleSourceReader source = this.sources[sourceIndex];
            int sourceTrackCount = source.getTrackCount();
            for (int trackIndex = 0; trackIndex < sourceTrackCount; trackIndex++) {
                MediaFormat format = source.getFormat(trackIndex);
                try {
                    boolean handlesTrack = handlesTrack(format);
                    if (handlesTrack) {
                        handledSourceIndices[handledTrackCount] = sourceIndex;
                        handledTrackIndices[handledTrackCount] = trackIndex;
                        handledTrackCount++;
                        if (durationUs != -1) {
                            long trackDurationUs = format.durationUs;
                            if (trackDurationUs == -1) {
                                durationUs = -1;
                            } else if (trackDurationUs != -2) {
                                durationUs = Math.max(durationUs, trackDurationUs);
                            }
                        }
                    }
                } catch (MediaCodecUtil.DecoderQueryException e) {
                    throw new ExoPlaybackException(e);
                }
            }
        }
        this.durationUs = durationUs;
        this.handledSourceIndices = Arrays.copyOf(handledSourceIndices, handledTrackCount);
        this.handledSourceTrackIndices = Arrays.copyOf(handledTrackIndices, handledTrackCount);
        return true;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        this.enabledSource = this.sources[this.handledSourceIndices[track]];
        this.enabledSourceTrackIndex = this.handledSourceTrackIndices[track];
        this.enabledSource.enable(this.enabledSourceTrackIndex, positionUs2);
        onDiscontinuity(positionUs2);
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected final void seekTo(long positionUs) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        this.enabledSource.seekToUs(positionUs2);
        checkForDiscontinuity(positionUs2);
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected final void doSomeWork(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        long positionUs2 = shiftInputPosition(positionUs);
        boolean sourceIsReady = this.enabledSource.continueBuffering(this.enabledSourceTrackIndex, positionUs2);
        doSomeWork(checkForDiscontinuity(positionUs2), elapsedRealtimeUs, sourceIsReady);
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected long getBufferedPositionUs() {
        return this.enabledSource.getBufferedPositionUs();
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void maybeThrowError() throws ExoPlaybackException {
        if (this.enabledSource != null) {
            maybeThrowError(this.enabledSource);
            return;
        }
        int sourceCount = this.sources.length;
        for (int i = 0; i < sourceCount; i++) {
            maybeThrowError(this.sources[i]);
        }
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void onDisabled() throws ExoPlaybackException {
        this.enabledSource.disable(this.enabledSourceTrackIndex);
        this.enabledSource = null;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected void onReleased() throws ExoPlaybackException {
        int sourceCount = this.sources.length;
        for (int i = 0; i < sourceCount; i++) {
            this.sources[i].release();
        }
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected final int getTrackCount() {
        return this.handledSourceTrackIndices.length;
    }

    @Override // com.google.android.exoplayer.TrackRenderer
    protected final MediaFormat getFormat(int track) {
        SampleSource.SampleSourceReader source = this.sources[this.handledSourceIndices[track]];
        return source.getFormat(this.handledSourceTrackIndices[track]);
    }

    protected long shiftInputPosition(long positionUs) {
        return positionUs;
    }

    protected final int readSource(long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder) {
        return this.enabledSource.readData(this.enabledSourceTrackIndex, positionUs, formatHolder, sampleHolder);
    }

    private long checkForDiscontinuity(long positionUs) throws ExoPlaybackException {
        long discontinuityPositionUs = this.enabledSource.readDiscontinuity(this.enabledSourceTrackIndex);
        if (discontinuityPositionUs == Long.MIN_VALUE) {
            return positionUs;
        }
        onDiscontinuity(discontinuityPositionUs);
        return discontinuityPositionUs;
    }

    private void maybeThrowError(SampleSource.SampleSourceReader source) throws ExoPlaybackException {
        try {
            source.maybeThrowError();
        } catch (IOException e) {
            throw new ExoPlaybackException(e);
        }
    }
}
