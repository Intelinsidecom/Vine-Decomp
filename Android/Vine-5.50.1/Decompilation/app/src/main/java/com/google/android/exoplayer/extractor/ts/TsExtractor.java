package com.google.android.exoplayer.extractor.ts;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.flurry.android.Constants;
import com.google.android.exoplayer.extractor.DummyTrackOutput;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import com.googlecode.javacv.cpp.avcodec;
import java.io.IOException;

/* loaded from: classes.dex */
public final class TsExtractor implements Extractor {
    private static final long AC3_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("AC-3");
    private static final long E_AC3_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("EAC3");
    private static final long HEVC_FORMAT_IDENTIFIER = Util.getIntegerCodeForString("HEVC");
    Id3Reader id3Reader;
    private final boolean idrKeyframesOnly;
    private ExtractorOutput output;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;
    final SparseBooleanArray streamTypes;
    private final ParsableByteArray tsPacketBuffer;
    final SparseArray<TsPayloadReader> tsPayloadReaders;
    private final ParsableBitArray tsScratch;

    public TsExtractor() {
        this(new PtsTimestampAdjuster(0L));
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster) {
        this(ptsTimestampAdjuster, true);
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster, boolean idrKeyframesOnly) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster;
        this.idrKeyframesOnly = idrKeyframesOnly;
        this.tsPacketBuffer = new ParsableByteArray(188);
        this.tsScratch = new ParsableBitArray(new byte[3]);
        this.tsPayloadReaders = new SparseArray<>();
        this.tsPayloadReaders.put(0, new PatReader());
        this.streamTypes = new SparseBooleanArray();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        byte[] scratch = new byte[1];
        for (int i = 0; i < 5; i++) {
            input.peekFully(scratch, 0, 1);
            if ((scratch[0] & Constants.UNKNOWN) != 71) {
                return false;
            }
            input.advancePeekPosition(187);
        }
        return true;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.output = output;
        output.seekMap(SeekMap.UNSEEKABLE);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void seek() {
        this.ptsTimestampAdjuster.reset();
        for (int i = 0; i < this.tsPayloadReaders.size(); i++) {
            this.tsPayloadReaders.valueAt(i).seek();
        }
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        TsPayloadReader payloadReader;
        if (!input.readFully(this.tsPacketBuffer.data, 0, 188, true)) {
            return -1;
        }
        this.tsPacketBuffer.setPosition(0);
        this.tsPacketBuffer.setLimit(188);
        int syncByte = this.tsPacketBuffer.readUnsignedByte();
        if (syncByte != 71) {
            return 0;
        }
        this.tsPacketBuffer.readBytes(this.tsScratch, 3);
        this.tsScratch.skipBits(1);
        boolean payloadUnitStartIndicator = this.tsScratch.readBit();
        this.tsScratch.skipBits(1);
        int pid = this.tsScratch.readBits(13);
        this.tsScratch.skipBits(2);
        boolean adaptationFieldExists = this.tsScratch.readBit();
        boolean payloadExists = this.tsScratch.readBit();
        if (adaptationFieldExists) {
            int adaptationFieldLength = this.tsPacketBuffer.readUnsignedByte();
            this.tsPacketBuffer.skipBytes(adaptationFieldLength);
        }
        if (!payloadExists || (payloadReader = this.tsPayloadReaders.get(pid)) == null) {
            return 0;
        }
        payloadReader.consume(this.tsPacketBuffer, payloadUnitStartIndicator, this.output);
        return 0;
    }

    private static abstract class TsPayloadReader {
        public abstract void consume(ParsableByteArray parsableByteArray, boolean z, ExtractorOutput extractorOutput);

        public abstract void seek();

        private TsPayloadReader() {
        }
    }

    private class PatReader extends TsPayloadReader {
        private final ParsableBitArray patScratch;

        public PatReader() {
            super();
            this.patScratch = new ParsableBitArray(new byte[4]);
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void seek() {
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                int pointerField = data.readUnsignedByte();
                data.skipBytes(pointerField);
            }
            data.readBytes(this.patScratch, 3);
            this.patScratch.skipBits(12);
            int sectionLength = this.patScratch.readBits(12);
            data.skipBytes(5);
            int programCount = (sectionLength - 9) / 4;
            for (int i = 0; i < programCount; i++) {
                data.readBytes(this.patScratch, 4);
                int programNumber = this.patScratch.readBits(16);
                this.patScratch.skipBits(3);
                if (programNumber == 0) {
                    this.patScratch.skipBits(13);
                } else {
                    int pid = this.patScratch.readBits(13);
                    TsExtractor.this.tsPayloadReaders.put(pid, TsExtractor.this.new PmtReader());
                }
            }
        }
    }

    private class PmtReader extends TsPayloadReader {
        private final ParsableBitArray pmtScratch;
        private int sectionBytesRead;
        private final ParsableByteArray sectionData;
        private int sectionLength;

        public PmtReader() {
            super();
            this.pmtScratch = new ParsableBitArray(new byte[5]);
            this.sectionData = new ParsableByteArray();
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void seek() {
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            ElementaryStreamReader pesPayloadReader;
            if (payloadUnitStartIndicator) {
                int pointerField = data.readUnsignedByte();
                data.skipBytes(pointerField);
                data.readBytes(this.pmtScratch, 3);
                this.pmtScratch.skipBits(12);
                this.sectionLength = this.pmtScratch.readBits(12);
                if (this.sectionData.capacity() < this.sectionLength) {
                    this.sectionData.reset(new byte[this.sectionLength], this.sectionLength);
                } else {
                    this.sectionData.reset();
                    this.sectionData.setLimit(this.sectionLength);
                }
            }
            int bytesToRead = Math.min(data.bytesLeft(), this.sectionLength - this.sectionBytesRead);
            data.readBytes(this.sectionData.data, this.sectionBytesRead, bytesToRead);
            this.sectionBytesRead += bytesToRead;
            if (this.sectionBytesRead >= this.sectionLength) {
                this.sectionData.skipBytes(7);
                this.sectionData.readBytes(this.pmtScratch, 2);
                this.pmtScratch.skipBits(4);
                int programInfoLength = this.pmtScratch.readBits(12);
                this.sectionData.skipBytes(programInfoLength);
                if (TsExtractor.this.id3Reader == null) {
                    TsExtractor.this.id3Reader = new Id3Reader(output.track(21));
                }
                int remainingEntriesLength = ((this.sectionLength - 9) - programInfoLength) - 4;
                while (remainingEntriesLength > 0) {
                    this.sectionData.readBytes(this.pmtScratch, 5);
                    int streamType = this.pmtScratch.readBits(8);
                    this.pmtScratch.skipBits(3);
                    int elementaryPid = this.pmtScratch.readBits(13);
                    this.pmtScratch.skipBits(4);
                    int esInfoLength = this.pmtScratch.readBits(12);
                    if (streamType == 6) {
                        streamType = readPrivateDataStreamType(this.sectionData, esInfoLength);
                    } else {
                        this.sectionData.skipBytes(esInfoLength);
                    }
                    remainingEntriesLength -= esInfoLength + 5;
                    if (!TsExtractor.this.streamTypes.get(streamType)) {
                        switch (streamType) {
                            case 2:
                                pesPayloadReader = new H262Reader(output.track(2));
                                break;
                            case 3:
                                pesPayloadReader = new MpegAudioReader(output.track(3));
                                break;
                            case 4:
                                pesPayloadReader = new MpegAudioReader(output.track(4));
                                break;
                            case 15:
                                pesPayloadReader = new AdtsReader(output.track(15), new DummyTrackOutput());
                                break;
                            case 21:
                                pesPayloadReader = TsExtractor.this.id3Reader;
                                break;
                            case 27:
                                pesPayloadReader = new H264Reader(output.track(27), new SeiReader(output.track(256)), TsExtractor.this.idrKeyframesOnly);
                                break;
                            case 36:
                                pesPayloadReader = new H265Reader(output.track(36), new SeiReader(output.track(256)));
                                break;
                            case 129:
                                pesPayloadReader = new Ac3Reader(output.track(129), false);
                                break;
                            case 130:
                            case avcodec.AV_CODEC_ID_IFF_BYTERUN1 /* 138 */:
                                pesPayloadReader = new DtsReader(output.track(avcodec.AV_CODEC_ID_IFF_BYTERUN1));
                                break;
                            case 135:
                                pesPayloadReader = new Ac3Reader(output.track(135), true);
                                break;
                            default:
                                pesPayloadReader = null;
                                break;
                        }
                        if (pesPayloadReader != null) {
                            TsExtractor.this.streamTypes.put(streamType, true);
                            TsExtractor.this.tsPayloadReaders.put(elementaryPid, TsExtractor.this.new PesReader(pesPayloadReader));
                        }
                    }
                }
                output.endTracks();
            }
        }

        private int readPrivateDataStreamType(ParsableByteArray data, int length) {
            int streamType = -1;
            int descriptorsEndPosition = data.getPosition() + length;
            while (true) {
                if (data.getPosition() >= descriptorsEndPosition) {
                    break;
                }
                int descriptorTag = data.readUnsignedByte();
                int descriptorLength = data.readUnsignedByte();
                if (descriptorTag == 5) {
                    long formatIdentifier = data.readUnsignedInt();
                    if (formatIdentifier == TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        streamType = 129;
                    } else if (formatIdentifier == TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                        streamType = 135;
                    } else if (formatIdentifier == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                        streamType = 36;
                    }
                } else {
                    if (descriptorTag == 106) {
                        streamType = 129;
                    } else if (descriptorTag == 122) {
                        streamType = 135;
                    } else if (descriptorTag == 123) {
                        streamType = avcodec.AV_CODEC_ID_IFF_BYTERUN1;
                    }
                    data.skipBytes(descriptorLength);
                }
            }
            data.setPosition(descriptorsEndPosition);
            return streamType;
        }
    }

    private class PesReader extends TsPayloadReader {
        private int bytesRead;
        private boolean dataAlignmentIndicator;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private int payloadSize;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch;
        private boolean ptsFlag;
        private boolean seenFirstDts;
        private int state;
        private long timeUs;

        public PesReader(ElementaryStreamReader pesPayloadReader) {
            super();
            this.pesPayloadReader = pesPayloadReader;
            this.pesScratch = new ParsableBitArray(new byte[10]);
            this.state = 0;
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void seek() {
            this.state = 0;
            this.bytesRead = 0;
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }

        @Override // com.google.android.exoplayer.extractor.ts.TsExtractor.TsPayloadReader
        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                switch (this.state) {
                    case 2:
                        Log.w("TsExtractor", "Unexpected start indicator reading extended header");
                        break;
                    case 3:
                        if (this.payloadSize != -1) {
                            Log.w("TsExtractor", "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
                        }
                        this.pesPayloadReader.packetFinished();
                        break;
                }
                setState(1);
            }
            while (data.bytesLeft() > 0) {
                switch (this.state) {
                    case 0:
                        data.skipBytes(data.bytesLeft());
                        break;
                    case 1:
                        if (!continueRead(data, this.pesScratch.data, 9)) {
                            break;
                        } else {
                            setState(parseHeader() ? 2 : 0);
                            break;
                        }
                    case 2:
                        if (!continueRead(data, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) || !continueRead(data, null, this.extendedHeaderLength)) {
                            break;
                        } else {
                            parseHeaderExtension();
                            this.pesPayloadReader.packetStarted(this.timeUs, this.dataAlignmentIndicator);
                            setState(3);
                            break;
                        }
                        break;
                    case 3:
                        int readLength = data.bytesLeft();
                        int padding = this.payloadSize == -1 ? 0 : readLength - this.payloadSize;
                        if (padding > 0) {
                            readLength -= padding;
                            data.setLimit(data.getPosition() + readLength);
                        }
                        this.pesPayloadReader.consume(data);
                        if (this.payloadSize == -1) {
                            break;
                        } else {
                            this.payloadSize -= readLength;
                            if (this.payloadSize != 0) {
                                break;
                            } else {
                                this.pesPayloadReader.packetFinished();
                                setState(1);
                                break;
                            }
                        }
                }
            }
        }

        private void setState(int state) {
            this.state = state;
            this.bytesRead = 0;
        }

        private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
            int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
            if (bytesToRead <= 0) {
                return true;
            }
            if (target == null) {
                source.skipBytes(bytesToRead);
            } else {
                source.readBytes(target, this.bytesRead, bytesToRead);
            }
            this.bytesRead += bytesToRead;
            return this.bytesRead == targetLength;
        }

        private boolean parseHeader() {
            this.pesScratch.setPosition(0);
            int startCodePrefix = this.pesScratch.readBits(24);
            if (startCodePrefix != 1) {
                Log.w("TsExtractor", "Unexpected start code prefix: " + startCodePrefix);
                this.payloadSize = -1;
                return false;
            }
            this.pesScratch.skipBits(8);
            int packetLength = this.pesScratch.readBits(16);
            this.pesScratch.skipBits(5);
            this.dataAlignmentIndicator = this.pesScratch.readBit();
            this.pesScratch.skipBits(2);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
            if (packetLength == 0) {
                this.payloadSize = -1;
            } else {
                this.payloadSize = ((packetLength + 6) - 9) - this.extendedHeaderLength;
            }
            return true;
        }

        private void parseHeaderExtension() {
            this.pesScratch.setPosition(0);
            this.timeUs = 0L;
            if (this.ptsFlag) {
                this.pesScratch.skipBits(4);
                long pts = this.pesScratch.readBits(3) << 30;
                this.pesScratch.skipBits(1);
                this.pesScratch.skipBits(1);
                long pts2 = pts | (this.pesScratch.readBits(15) << 15) | this.pesScratch.readBits(15);
                this.pesScratch.skipBits(1);
                if (!this.seenFirstDts && this.dtsFlag) {
                    this.pesScratch.skipBits(4);
                    long dts = this.pesScratch.readBits(3) << 30;
                    this.pesScratch.skipBits(1);
                    this.pesScratch.skipBits(1);
                    this.pesScratch.skipBits(1);
                    TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(dts | (this.pesScratch.readBits(15) << 15) | this.pesScratch.readBits(15));
                    this.seenFirstDts = true;
                }
                this.timeUs = TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(pts2);
            }
        }
    }
}
