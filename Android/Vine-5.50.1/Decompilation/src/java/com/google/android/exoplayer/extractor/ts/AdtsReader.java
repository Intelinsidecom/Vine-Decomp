package com.google.android.exoplayer.extractor.ts;

import android.util.Pair;
import com.flurry.android.Constants;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Arrays;
import java.util.Collections;

/* loaded from: classes.dex */
final class AdtsReader extends ElementaryStreamReader {
    private static final byte[] ID3_IDENTIFIER = {73, 68, 51};
    private final ParsableBitArray adtsScratch;
    private int bytesRead;
    private TrackOutput currentOutput;
    private long currentSampleDuration;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private final ParsableByteArray id3HeaderBuffer;
    private final TrackOutput id3Output;
    private int matchState;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;

    public AdtsReader(TrackOutput output, TrackOutput id3Output) {
        super(output);
        this.id3Output = id3Output;
        id3Output.format(MediaFormat.createId3Format());
        this.adtsScratch = new ParsableBitArray(new byte[7]);
        this.id3HeaderBuffer = new ParsableByteArray(Arrays.copyOf(ID3_IDENTIFIER, 10));
        setFindingSampleState();
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void seek() {
        setFindingSampleState();
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    findNextSample(data);
                    break;
                case 1:
                    if (!continueRead(data, this.id3HeaderBuffer.data, 10)) {
                        break;
                    } else {
                        parseId3Header();
                        break;
                    }
                case 2:
                    int targetLength = this.hasCrc ? 7 : 5;
                    if (!continueRead(data, this.adtsScratch.data, targetLength)) {
                        break;
                    } else {
                        parseAdtsHeader();
                        break;
                    }
                case 3:
                    readSample(data);
                    break;
            }
        }
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        source.readBytes(target, this.bytesRead, bytesToRead);
        this.bytesRead += bytesToRead;
        return this.bytesRead == targetLength;
    }

    private void setFindingSampleState() {
        this.state = 0;
        this.bytesRead = 0;
        this.matchState = 256;
    }

    private void setReadingId3HeaderState() {
        this.state = 1;
        this.bytesRead = ID3_IDENTIFIER.length;
        this.sampleSize = 0;
        this.id3HeaderBuffer.setPosition(0);
    }

    private void setReadingSampleState(TrackOutput outputToUse, long currentSampleDuration, int priorReadBytes, int sampleSize) {
        this.state = 3;
        this.bytesRead = priorReadBytes;
        this.currentOutput = outputToUse;
        this.currentSampleDuration = currentSampleDuration;
        this.sampleSize = sampleSize;
    }

    private void setReadingAdtsHeaderState() {
        this.state = 2;
        this.bytesRead = 0;
    }

    private void findNextSample(ParsableByteArray pesBuffer) {
        byte[] adtsData = pesBuffer.data;
        int position = pesBuffer.getPosition();
        int endOffset = pesBuffer.limit();
        int position2 = position;
        while (position2 < endOffset) {
            int position3 = position2 + 1;
            int data = adtsData[position2] & Constants.UNKNOWN;
            if (this.matchState == 512 && data >= 240 && data != 255) {
                this.hasCrc = (data & 1) == 0;
                setReadingAdtsHeaderState();
                pesBuffer.setPosition(position3);
                return;
            }
            switch (this.matchState | data) {
                case 329:
                    this.matchState = 768;
                    break;
                case 511:
                    this.matchState = 512;
                    break;
                case 836:
                    this.matchState = 1024;
                    break;
                case 1075:
                    setReadingId3HeaderState();
                    pesBuffer.setPosition(position3);
                    return;
                default:
                    if (this.matchState == 256) {
                        break;
                    } else {
                        this.matchState = 256;
                        position3--;
                        break;
                    }
            }
            position2 = position3;
        }
        pesBuffer.setPosition(position2);
    }

    private void parseId3Header() {
        this.id3Output.sampleData(this.id3HeaderBuffer, 10);
        this.id3HeaderBuffer.setPosition(6);
        setReadingSampleState(this.id3Output, 0L, 10, this.id3HeaderBuffer.readSynchSafeInt() + 10);
    }

    private void parseAdtsHeader() {
        this.adtsScratch.setPosition(0);
        if (!this.hasOutputFormat) {
            int audioObjectType = this.adtsScratch.readBits(2) + 1;
            int sampleRateIndex = this.adtsScratch.readBits(4);
            this.adtsScratch.skipBits(1);
            int channelConfig = this.adtsScratch.readBits(3);
            byte[] audioSpecificConfig = CodecSpecificDataUtil.buildAacAudioSpecificConfig(audioObjectType, sampleRateIndex, channelConfig);
            Pair<Integer, Integer> audioParams = CodecSpecificDataUtil.parseAacAudioSpecificConfig(audioSpecificConfig);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(null, "audio/mp4a-latm", -1, -1, -1L, ((Integer) audioParams.second).intValue(), ((Integer) audioParams.first).intValue(), Collections.singletonList(audioSpecificConfig), null);
            this.sampleDurationUs = 1024000000 / mediaFormat.sampleRate;
            this.output.format(mediaFormat);
            this.hasOutputFormat = true;
        } else {
            this.adtsScratch.skipBits(10);
        }
        this.adtsScratch.skipBits(4);
        int sampleSize = (this.adtsScratch.readBits(13) - 2) - 5;
        if (this.hasCrc) {
            sampleSize -= 2;
        }
        setReadingSampleState(this.output, this.sampleDurationUs, 0, sampleSize);
    }

    private void readSample(ParsableByteArray data) {
        int bytesToRead = Math.min(data.bytesLeft(), this.sampleSize - this.bytesRead);
        this.currentOutput.sampleData(data, bytesToRead);
        this.bytesRead += bytesToRead;
        if (this.bytesRead == this.sampleSize) {
            this.currentOutput.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, null);
            this.timeUs += this.currentSampleDuration;
            setFindingSampleState();
        }
    }
}
