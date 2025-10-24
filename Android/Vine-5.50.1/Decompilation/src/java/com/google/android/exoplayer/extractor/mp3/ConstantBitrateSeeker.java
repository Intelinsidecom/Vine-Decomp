package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;

/* loaded from: classes.dex */
final class ConstantBitrateSeeker implements Mp3Extractor.Seeker {
    private final int bitrate;
    private final long durationUs;
    private final long firstFramePosition;

    public ConstantBitrateSeeker(long firstFramePosition, int bitrate, long inputLength) {
        this.firstFramePosition = firstFramePosition;
        this.bitrate = bitrate;
        this.durationUs = inputLength != -1 ? getTimeUs(inputLength) : -1L;
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public boolean isSeekable() {
        return this.durationUs != -1;
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public long getPosition(long timeUs) {
        if (this.durationUs == -1) {
            return 0L;
        }
        return this.firstFramePosition + ((this.bitrate * timeUs) / 8000000);
    }

    @Override // com.google.android.exoplayer.extractor.mp3.Mp3Extractor.Seeker
    public long getTimeUs(long position) {
        return ((Math.max(0L, position - this.firstFramePosition) * 1000000) * 8) / this.bitrate;
    }

    @Override // com.google.android.exoplayer.extractor.mp3.Mp3Extractor.Seeker
    public long getDurationUs() {
        return this.durationUs;
    }
}
