package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
final class H264Reader extends ElementaryStreamReader {
    private boolean foundFirstSample;
    private boolean hasOutputFormat;
    private final IfrParserBuffer ifrParserBuffer;
    private boolean isKeyframe;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags;
    private long samplePosition;
    private long sampleTimeUs;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;

    public H264Reader(TrackOutput output, SeiReader seiReader, boolean idrKeyframesOnly) {
        super(output);
        this.seiReader = seiReader;
        this.prefixFlags = new boolean[3];
        this.ifrParserBuffer = idrKeyframesOnly ? null : new IfrParserBuffer();
        this.sps = new NalUnitTargetBuffer(7, 128);
        this.pps = new NalUnitTargetBuffer(8, 128);
        this.sei = new NalUnitTargetBuffer(6, 128);
        this.seiWrapper = new ParsableByteArray();
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.reset();
        }
        this.foundFirstSample = false;
        this.totalBytesWritten = 0L;
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.pesTimeUs = pesTimeUs;
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray data) throws Throwable {
        if (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            while (true) {
                int nalUnitOffset = NalUnitUtil.findNalUnit(dataArray, offset, limit, this.prefixFlags);
                if (nalUnitOffset == limit) {
                    feedNalUnitTargetBuffersData(dataArray, offset, limit);
                    return;
                }
                int nalUnitType = NalUnitUtil.getNalUnitType(dataArray, nalUnitOffset);
                int lengthToNalUnit = nalUnitOffset - offset;
                if (lengthToNalUnit > 0) {
                    feedNalUnitTargetBuffersData(dataArray, offset, nalUnitOffset);
                }
                switch (nalUnitType) {
                    case 5:
                        this.isKeyframe = true;
                        break;
                    case 9:
                        int bytesWrittenPastNalUnit = limit - nalUnitOffset;
                        if (this.foundFirstSample) {
                            if (this.ifrParserBuffer != null && this.ifrParserBuffer.isCompleted()) {
                                int sliceType = this.ifrParserBuffer.getSliceType();
                                this.isKeyframe = (sliceType == 2 || sliceType == 7) | this.isKeyframe;
                                this.ifrParserBuffer.reset();
                            }
                            if (this.isKeyframe && !this.hasOutputFormat && this.sps.isCompleted() && this.pps.isCompleted()) {
                                this.output.format(parseMediaFormat(this.sps, this.pps));
                                this.hasOutputFormat = true;
                            }
                            int flags = this.isKeyframe ? 1 : 0;
                            int size = ((int) (this.totalBytesWritten - this.samplePosition)) - bytesWrittenPastNalUnit;
                            this.output.sampleMetadata(this.sampleTimeUs, flags, size, bytesWrittenPastNalUnit, null);
                        }
                        this.foundFirstSample = true;
                        this.samplePosition = this.totalBytesWritten - bytesWrittenPastNalUnit;
                        this.sampleTimeUs = this.pesTimeUs;
                        this.isKeyframe = false;
                        break;
                }
                feedNalUnitTargetEnd(this.pesTimeUs, lengthToNalUnit < 0 ? -lengthToNalUnit : 0);
                feedNalUnitTargetBuffersStart(nalUnitType);
                offset = nalUnitOffset + 3;
            }
        }
    }

    @Override // com.google.android.exoplayer.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    private void feedNalUnitTargetBuffersStart(int nalUnitType) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.startNalUnit(nalUnitType);
        }
        if (!this.hasOutputFormat) {
            this.sps.startNalUnit(nalUnitType);
            this.pps.startNalUnit(nalUnitType);
        }
        this.sei.startNalUnit(nalUnitType);
    }

    private void feedNalUnitTargetBuffersData(byte[] dataArray, int offset, int limit) {
        if (this.ifrParserBuffer != null) {
            this.ifrParserBuffer.appendToNalUnit(dataArray, offset, limit);
        }
        if (!this.hasOutputFormat) {
            this.sps.appendToNalUnit(dataArray, offset, limit);
            this.pps.appendToNalUnit(dataArray, offset, limit);
        }
        this.sei.appendToNalUnit(dataArray, offset, limit);
    }

    private void feedNalUnitTargetEnd(long pesTimeUs, int discardPadding) throws Throwable {
        this.sps.endNalUnit(discardPadding);
        this.pps.endNalUnit(discardPadding);
        if (this.sei.endNalUnit(discardPadding)) {
            int unescapedLength = NalUnitUtil.unescapeStream(this.sei.nalData, this.sei.nalLength);
            this.seiWrapper.reset(this.sei.nalData, unescapedLength);
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(pesTimeUs, this.seiWrapper);
        }
    }

    private static MediaFormat parseMediaFormat(NalUnitTargetBuffer sps, NalUnitTargetBuffer pps) throws Throwable {
        List<byte[]> initializationData = new ArrayList<>();
        initializationData.add(Arrays.copyOf(sps.nalData, sps.nalLength));
        initializationData.add(Arrays.copyOf(pps.nalData, pps.nalLength));
        NalUnitUtil.unescapeStream(sps.nalData, sps.nalLength);
        ParsableBitArray bitArray = new ParsableBitArray(sps.nalData);
        bitArray.skipBits(32);
        CodecSpecificDataUtil.SpsData parsedSpsData = CodecSpecificDataUtil.parseSpsNalUnit(bitArray);
        return MediaFormat.createVideoFormat(null, "video/avc", -1, -1, -1L, parsedSpsData.width, parsedSpsData.height, initializationData, -1, parsedSpsData.pixelWidthAspectRatio);
    }

    private static final class IfrParserBuffer {
        private int ifrLength;
        private boolean isFilling;
        private int sliceType;
        private byte[] ifrData = new byte[128];
        private final ParsableBitArray scratchSliceType = new ParsableBitArray(this.ifrData);

        public IfrParserBuffer() {
            reset();
        }

        public void reset() {
            this.isFilling = false;
            this.ifrLength = 0;
            this.sliceType = -1;
        }

        public boolean isCompleted() {
            return this.sliceType != -1;
        }

        public void startNalUnit(int nalUnitType) {
            if (nalUnitType == 1) {
                reset();
                this.isFilling = true;
            }
        }

        public void appendToNalUnit(byte[] data, int offset, int limit) {
            if (this.isFilling) {
                int readLength = limit - offset;
                if (this.ifrData.length < this.ifrLength + readLength) {
                    this.ifrData = Arrays.copyOf(this.ifrData, (this.ifrLength + readLength) * 2);
                }
                System.arraycopy(data, offset, this.ifrData, this.ifrLength, readLength);
                this.ifrLength += readLength;
                this.scratchSliceType.reset(this.ifrData, this.ifrLength);
                this.scratchSliceType.skipBits(8);
                int len = this.scratchSliceType.peekExpGolombCodedNumLength();
                if (len != -1 && len <= this.scratchSliceType.bitsLeft()) {
                    this.scratchSliceType.skipBits(len);
                    int len2 = this.scratchSliceType.peekExpGolombCodedNumLength();
                    if (len2 != -1 && len2 <= this.scratchSliceType.bitsLeft()) {
                        this.sliceType = this.scratchSliceType.readUnsignedExpGolombCodedInt();
                        this.isFilling = false;
                    }
                }
            }
        }

        public int getSliceType() {
            return this.sliceType;
        }
    }
}
