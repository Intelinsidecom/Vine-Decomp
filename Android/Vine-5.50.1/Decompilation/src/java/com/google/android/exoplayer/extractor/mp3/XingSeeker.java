package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

/* loaded from: classes.dex */
final class XingSeeker implements Mp3Extractor.Seeker {
    private final long durationUs;
    private final long firstFramePosition;
    private final int headerSize;
    private final long inputLength;
    private final long sizeBytes;
    private final long[] tableOfContents;

    public static XingSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength) {
        int frameCount;
        int samplesPerFrame = mpegAudioHeader.samplesPerFrame;
        int sampleRate = mpegAudioHeader.sampleRate;
        long firstFramePosition = position + mpegAudioHeader.frameSize;
        int flags = frame.readInt();
        if ((flags & 1) != 1 || (frameCount = frame.readUnsignedIntToInt()) == 0) {
            return null;
        }
        long durationUs = Util.scaleLargeTimestamp(frameCount, samplesPerFrame * 1000000, sampleRate);
        if ((flags & 6) != 6) {
            return new XingSeeker(firstFramePosition, durationUs, inputLength);
        }
        long sizeBytes = frame.readUnsignedIntToInt();
        frame.skipBytes(1);
        long[] tableOfContents = new long[99];
        for (int i = 0; i < 99; i++) {
            tableOfContents[i] = frame.readUnsignedByte();
        }
        return new XingSeeker(firstFramePosition, durationUs, inputLength, tableOfContents, sizeBytes, mpegAudioHeader.frameSize);
    }

    private XingSeeker(long firstFramePosition, long durationUs, long inputLength) {
        this(firstFramePosition, durationUs, inputLength, null, 0L, 0);
    }

    private XingSeeker(long firstFramePosition, long durationUs, long inputLength, long[] tableOfContents, long sizeBytes, int headerSize) {
        this.firstFramePosition = firstFramePosition;
        this.durationUs = durationUs;
        this.inputLength = inputLength;
        this.tableOfContents = tableOfContents;
        this.sizeBytes = sizeBytes;
        this.headerSize = headerSize;
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public boolean isSeekable() {
        return this.tableOfContents != null;
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public long getPosition(long timeUs) {
        float fa;
        float fb;
        float fx;
        if (!isSeekable()) {
            return this.firstFramePosition;
        }
        float percent = (timeUs * 100.0f) / this.durationUs;
        if (percent <= 0.0f) {
            fx = 0.0f;
        } else if (percent >= 100.0f) {
            fx = 256.0f;
        } else {
            int a = (int) percent;
            if (a == 0) {
                fa = 0.0f;
            } else {
                fa = this.tableOfContents[a - 1];
            }
            if (a < 99) {
                fb = this.tableOfContents[a];
            } else {
                fb = 256.0f;
            }
            fx = fa + ((fb - fa) * (percent - a));
        }
        long position = Math.round(0.00390625d * fx * this.sizeBytes) + this.firstFramePosition;
        long maximumPosition = this.inputLength != -1 ? this.inputLength - 1 : ((this.firstFramePosition - this.headerSize) + this.sizeBytes) - 1;
        return Math.min(position, maximumPosition);
    }

    @Override // com.google.android.exoplayer.extractor.mp3.Mp3Extractor.Seeker
    public long getTimeUs(long position) {
        if (!isSeekable() || position < this.firstFramePosition) {
            return 0L;
        }
        double offsetByte = (256.0d * (position - this.firstFramePosition)) / this.sizeBytes;
        int previousTocPosition = Util.binarySearchFloor(this.tableOfContents, (long) offsetByte, true, false) + 1;
        long previousTime = getTimeUsForTocPosition(previousTocPosition);
        long previousByte = previousTocPosition == 0 ? 0L : this.tableOfContents[previousTocPosition - 1];
        long nextByte = previousTocPosition == 99 ? 256L : this.tableOfContents[previousTocPosition];
        long nextTime = getTimeUsForTocPosition(previousTocPosition + 1);
        long timeOffset = nextByte == previousByte ? 0L : (long) (((nextTime - previousTime) * (offsetByte - previousByte)) / (nextByte - previousByte));
        return previousTime + timeOffset;
    }

    @Override // com.google.android.exoplayer.extractor.mp3.Mp3Extractor.Seeker
    public long getDurationUs() {
        return this.durationUs;
    }

    private long getTimeUsForTocPosition(int tocPosition) {
        return (this.durationUs * tocPosition) / 100;
    }
}
