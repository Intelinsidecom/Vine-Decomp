package com.google.android.exoplayer.extractor.flv;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
final class VideoTagPayloadReader extends TagPayloadReader {
    private int frameType;
    private boolean hasOutputFormat;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private int nalUnitLengthFieldLength;

    public VideoTagPayloadReader(TrackOutput output) {
        super(output);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
    }

    @Override // com.google.android.exoplayer.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray data) throws TagPayloadReader.UnsupportedFormatException {
        int header = data.readUnsignedByte();
        int frameType = (header >> 4) & 15;
        int videoCodec = header & 15;
        if (videoCodec != 7) {
            throw new TagPayloadReader.UnsupportedFormatException("Video format not supported: " + videoCodec);
        }
        this.frameType = frameType;
        return frameType != 5;
    }

    @Override // com.google.android.exoplayer.extractor.flv.TagPayloadReader
    protected void parsePayload(ParsableByteArray data, long timeUs) throws ParserException {
        int packetType = data.readUnsignedByte();
        int compositionTimeMs = data.readUnsignedInt24();
        long timeUs2 = timeUs + (compositionTimeMs * 1000);
        if (packetType == 0 && !this.hasOutputFormat) {
            ParsableByteArray videoSequence = new ParsableByteArray(new byte[data.bytesLeft()]);
            data.readBytes(videoSequence.data, 0, data.bytesLeft());
            AvcSequenceHeaderData avcData = parseAvcCodecPrivate(videoSequence);
            this.nalUnitLengthFieldLength = avcData.nalUnitLengthFieldLength;
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(null, "video/avc", -1, -1, getDurationUs(), avcData.width, avcData.height, avcData.initializationData, -1, avcData.pixelWidthAspectRatio);
            this.output.format(mediaFormat);
            this.hasOutputFormat = true;
            return;
        }
        if (packetType == 1) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLengthDiff = 4 - this.nalUnitLengthFieldLength;
            int bytesWritten = 0;
            while (data.bytesLeft() > 0) {
                data.readBytes(this.nalLength.data, nalUnitLengthFieldLengthDiff, this.nalUnitLengthFieldLength);
                this.nalLength.setPosition(0);
                int bytesToWrite = this.nalLength.readUnsignedIntToInt();
                this.nalStartCode.setPosition(0);
                this.output.sampleData(this.nalStartCode, 4);
                this.output.sampleData(data, bytesToWrite);
                bytesWritten = bytesWritten + 4 + bytesToWrite;
            }
            this.output.sampleMetadata(timeUs2, this.frameType == 1 ? 1 : 0, bytesWritten, 0, null);
        }
    }

    private AvcSequenceHeaderData parseAvcCodecPrivate(ParsableByteArray buffer) throws ParserException {
        buffer.setPosition(4);
        int nalUnitLengthFieldLength = (buffer.readUnsignedByte() & 3) + 1;
        Assertions.checkState(nalUnitLengthFieldLength != 3);
        List<byte[]> initializationData = new ArrayList<>();
        int numSequenceParameterSets = buffer.readUnsignedByte() & 31;
        for (int i = 0; i < numSequenceParameterSets; i++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
        }
        int numPictureParameterSets = buffer.readUnsignedByte();
        for (int j = 0; j < numPictureParameterSets; j++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
        }
        float pixelWidthAspectRatio = 1.0f;
        int width = -1;
        int height = -1;
        if (numSequenceParameterSets > 0) {
            ParsableBitArray spsDataBitArray = new ParsableBitArray(initializationData.get(0));
            spsDataBitArray.setPosition((nalUnitLengthFieldLength + 1) * 8);
            CodecSpecificDataUtil.SpsData sps = CodecSpecificDataUtil.parseSpsNalUnit(spsDataBitArray);
            width = sps.width;
            height = sps.height;
            pixelWidthAspectRatio = sps.pixelWidthAspectRatio;
        }
        return new AvcSequenceHeaderData(initializationData, nalUnitLengthFieldLength, width, height, pixelWidthAspectRatio);
    }

    private static final class AvcSequenceHeaderData {
        public final int height;
        public final List<byte[]> initializationData;
        public final int nalUnitLengthFieldLength;
        public final float pixelWidthAspectRatio;
        public final int width;

        public AvcSequenceHeaderData(List<byte[]> initializationData, int nalUnitLengthFieldLength, int width, int height, float pixelWidthAspectRatio) {
            this.initializationData = initializationData;
            this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
            this.width = width;
            this.height = height;
        }
    }
}
