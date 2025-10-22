package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public final class AdtsExtractor implements Extractor {
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private AdtsReader adtsReader;
    private final long firstSampleTimestampUs;
    private final ParsableByteArray packetBuffer;
    private boolean startedPacket;

    public AdtsExtractor() {
        this(0L);
    }

    public AdtsExtractor(long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        this.packetBuffer = new ParsableByteArray(HttpResponseCode.OK);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        ParsableByteArray scratch = new ParsableByteArray(10);
        ParsableBitArray scratchBits = new ParsableBitArray(scratch.data);
        int startPosition = 0;
        while (true) {
            input.peekFully(scratch.data, 0, 10);
            scratch.setPosition(0);
            if (scratch.readUnsignedInt24() != ID3_TAG) {
                break;
            }
            int length = ((scratch.data[6] & 127) << 21) | ((scratch.data[7] & 127) << 14) | ((scratch.data[8] & 127) << 7) | (scratch.data[9] & 127);
            startPosition += length + 10;
            input.advancePeekPosition(length);
        }
        input.resetPeekPosition();
        input.advancePeekPosition(startPosition);
        int headerPosition = startPosition;
        int validFramesSize = 0;
        int validFramesCount = 0;
        while (true) {
            input.peekFully(scratch.data, 0, 2);
            scratch.setPosition(0);
            int syncBytes = scratch.readUnsignedShort();
            if ((65526 & syncBytes) != 65520) {
                validFramesCount = 0;
                validFramesSize = 0;
                input.resetPeekPosition();
                headerPosition++;
                if (headerPosition - startPosition >= 8192) {
                    return false;
                }
                input.advancePeekPosition(headerPosition);
            } else {
                validFramesCount++;
                if (validFramesCount >= 4 && validFramesSize > 188) {
                    return true;
                }
                input.peekFully(scratch.data, 0, 4);
                scratchBits.setPosition(14);
                int frameSize = scratchBits.readBits(13);
                input.advancePeekPosition(frameSize - 6);
                validFramesSize += frameSize;
            }
        }
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.adtsReader = new AdtsReader(output.track(0), output.track(1));
        output.endTracks();
        output.seekMap(SeekMap.UNSEEKABLE);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void seek() {
        this.startedPacket = false;
        this.adtsReader.seek();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        int bytesRead = input.read(this.packetBuffer.data, 0, HttpResponseCode.OK);
        if (bytesRead == -1) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesRead);
        if (!this.startedPacket) {
            this.adtsReader.packetStarted(this.firstSampleTimestampUs, true);
            this.startedPacket = true;
        }
        this.adtsReader.consume(this.packetBuffer);
        return 0;
    }
}
