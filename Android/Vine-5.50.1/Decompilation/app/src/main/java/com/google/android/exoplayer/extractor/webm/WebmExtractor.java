package com.google.android.exoplayer.extractor.webm;

import android.util.Pair;
import android.util.SparseArray;
import com.flurry.android.Constants;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.ChunkIndex;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.LongArray;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public final class WebmExtractor implements Extractor {
    private static final byte[] SUBRIP_PREFIX = {49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};
    private static final byte[] SUBRIP_TIMECODE_EMPTY = {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
    private long blockDurationUs;
    private int blockFlags;
    private int blockLacingSampleCount;
    private int blockLacingSampleIndex;
    private int[] blockLacingSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleRead;
    private boolean sampleSeenReferenceBlock;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentDrmInitData;
    private boolean sentSeekMap;
    private final ParsableByteArray subripSample;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;

    public WebmExtractor() {
        this(new DefaultEbmlReader());
    }

    WebmExtractor(EbmlReader reader) {
        this.segmentContentPosition = -1L;
        this.segmentContentSize = -1L;
        this.timecodeScale = -1L;
        this.durationTimecode = -1L;
        this.durationUs = -1L;
        this.cuesContentPosition = -1L;
        this.seekPositionAfterBuildingCues = -1L;
        this.clusterTimecodeUs = -1L;
        this.reader = reader;
        this.reader.init(new InnerEbmlReaderOutput());
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray<>();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subripSample = new ParsableByteArray();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return new Sniffer().sniff(input);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void seek() {
        this.clusterTimecodeUs = -1L;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetSample();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        this.sampleRead = false;
        boolean continueReading = true;
        while (continueReading && !this.sampleRead) {
            continueReading = this.reader.read(input);
            if (continueReading && maybeSeekForCues(seekPosition, input.getPosition())) {
                return 1;
            }
        }
        return !continueReading ? -1 : 0;
    }

    int getElementType(int id) {
        switch (id) {
            case 131:
            case avcodec.AV_CODEC_ID_BMV_VIDEO /* 155 */:
            case avcodec.AV_CODEC_ID_XWD /* 159 */:
            case 176:
            case 179:
            case 186:
            case 215:
            case 231:
            case 241:
            case 251:
            case 16980:
            case 17029:
            case 17143:
            case 18401:
            case 18408:
            case 20529:
            case 20530:
            case 21420:
            case 22186:
            case 22203:
            case 2352003:
            case 2807729:
                return 2;
            case 134:
            case 17026:
            case 2274716:
                return 3;
            case avcodec.AV_CODEC_ID_CDXL /* 160 */:
            case 174:
            case 183:
            case 187:
            case 224:
            case 225:
            case 18407:
            case 19899:
            case 20532:
            case 20533:
            case 25152:
            case 28032:
            case 290298740:
            case 357149030:
            case 374648427:
            case 408125543:
            case 440786851:
            case 475249515:
            case 524531317:
                return 1;
            case avcodec.AV_CODEC_ID_XBM /* 161 */:
            case avcodec.AV_CODEC_ID_MSS1 /* 163 */:
            case 16981:
            case 18402:
            case 21419:
            case 25506:
                return 4;
            case 181:
            case 17545:
                return 5;
            default:
                return 0;
        }
    }

    boolean isLevel1Element(int id) {
        return id == 357149030 || id == 524531317 || id == 475249515 || id == 374648427;
    }

    void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
        switch (id) {
            case avcodec.AV_CODEC_ID_CDXL /* 160 */:
                this.sampleSeenReferenceBlock = false;
                return;
            case 174:
                this.currentTrack = new Track();
                return;
            case 187:
                this.seenClusterPositionForCurrentCuePoint = false;
                return;
            case 19899:
                this.seekEntryId = -1;
                this.seekEntryPosition = -1L;
                return;
            case 20533:
                this.currentTrack.hasContentEncryption = true;
                return;
            case 25152:
            default:
                return;
            case 408125543:
                if (this.segmentContentPosition != -1 && this.segmentContentPosition != contentPosition) {
                    throw new ParserException("Multiple Segment elements not supported");
                }
                this.segmentContentPosition = contentPosition;
                this.segmentContentSize = contentSize;
                return;
            case 475249515:
                this.cueTimesUs = new LongArray();
                this.cueClusterPositions = new LongArray();
                return;
            case 524531317:
                if (this.sentSeekMap) {
                    return;
                }
                if (this.cuesContentPosition != -1) {
                    this.seekForCues = true;
                    return;
                } else {
                    this.extractorOutput.seekMap(SeekMap.UNSEEKABLE);
                    this.sentSeekMap = true;
                    return;
                }
        }
    }

    void endMasterElement(int id) throws ParserException {
        switch (id) {
            case avcodec.AV_CODEC_ID_CDXL /* 160 */:
                if (this.blockState == 2) {
                    if (!this.sampleSeenReferenceBlock) {
                        this.blockFlags |= 1;
                    }
                    commitSampleToOutput(this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
                    this.blockState = 0;
                    return;
                }
                return;
            case 174:
                if (this.tracks.get(this.currentTrack.number) == null && isCodecSupported(this.currentTrack.codecId)) {
                    this.currentTrack.initializeOutput(this.extractorOutput, this.currentTrack.number, this.durationUs);
                    this.tracks.put(this.currentTrack.number, this.currentTrack);
                }
                this.currentTrack = null;
                return;
            case 19899:
                if (this.seekEntryId == -1 || this.seekEntryPosition == -1) {
                    throw new ParserException("Mandatory element SeekID or SeekPosition not found");
                }
                if (this.seekEntryId == 475249515) {
                    this.cuesContentPosition = this.seekEntryPosition;
                    return;
                }
                return;
            case 25152:
                if (this.currentTrack.hasContentEncryption) {
                    if (this.currentTrack.encryptionKeyId == null) {
                        throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                    }
                    if (!this.sentDrmInitData) {
                        this.extractorOutput.drmInitData(new DrmInitData.Universal(new DrmInitData.SchemeInitData("video/webm", this.currentTrack.encryptionKeyId)));
                        this.sentDrmInitData = true;
                        return;
                    }
                    return;
                }
                return;
            case 28032:
                if (this.currentTrack.hasContentEncryption && this.currentTrack.sampleStrippedBytes != null) {
                    throw new ParserException("Combining encryption and compression is not supported");
                }
                return;
            case 357149030:
                if (this.timecodeScale == -1) {
                    this.timecodeScale = 1000000L;
                }
                if (this.durationTimecode != -1) {
                    this.durationUs = scaleTimecodeToUs(this.durationTimecode);
                    return;
                }
                return;
            case 374648427:
                if (this.tracks.size() == 0) {
                    throw new ParserException("No valid tracks were found");
                }
                this.extractorOutput.endTracks();
                return;
            case 475249515:
                if (!this.sentSeekMap) {
                    this.extractorOutput.seekMap(buildSeekMap());
                    this.sentSeekMap = true;
                    return;
                }
                return;
            default:
                return;
        }
    }

    void integerElement(int id, long value) throws ParserException {
        switch (id) {
            case 131:
                this.currentTrack.type = (int) value;
                return;
            case avcodec.AV_CODEC_ID_BMV_VIDEO /* 155 */:
                this.blockDurationUs = scaleTimecodeToUs(value);
                return;
            case avcodec.AV_CODEC_ID_XWD /* 159 */:
                this.currentTrack.channelCount = (int) value;
                return;
            case 176:
                this.currentTrack.width = (int) value;
                return;
            case 179:
                this.cueTimesUs.add(scaleTimecodeToUs(value));
                return;
            case 186:
                this.currentTrack.height = (int) value;
                return;
            case 215:
                this.currentTrack.number = (int) value;
                return;
            case 231:
                this.clusterTimecodeUs = scaleTimecodeToUs(value);
                return;
            case 241:
                if (!this.seenClusterPositionForCurrentCuePoint) {
                    this.cueClusterPositions.add(value);
                    this.seenClusterPositionForCurrentCuePoint = true;
                    return;
                }
                return;
            case 251:
                this.sampleSeenReferenceBlock = true;
                return;
            case 16980:
                if (value != 3) {
                    throw new ParserException("ContentCompAlgo " + value + " not supported");
                }
                return;
            case 17029:
                if (value < 1 || value > 2) {
                    throw new ParserException("DocTypeReadVersion " + value + " not supported");
                }
                return;
            case 17143:
                if (value != 1) {
                    throw new ParserException("EBMLReadVersion " + value + " not supported");
                }
                return;
            case 18401:
                if (value != 5) {
                    throw new ParserException("ContentEncAlgo " + value + " not supported");
                }
                return;
            case 18408:
                if (value != 1) {
                    throw new ParserException("AESSettingsCipherMode " + value + " not supported");
                }
                return;
            case 20529:
                if (value != 0) {
                    throw new ParserException("ContentEncodingOrder " + value + " not supported");
                }
                return;
            case 20530:
                if (value != 1) {
                    throw new ParserException("ContentEncodingScope " + value + " not supported");
                }
                return;
            case 21420:
                this.seekEntryPosition = this.segmentContentPosition + value;
                return;
            case 22186:
                this.currentTrack.codecDelayNs = value;
                return;
            case 22203:
                this.currentTrack.seekPreRollNs = value;
                return;
            case 2352003:
                this.currentTrack.defaultSampleDurationNs = (int) value;
                return;
            case 2807729:
                this.timecodeScale = value;
                return;
            default:
                return;
        }
    }

    void floatElement(int id, double value) {
        switch (id) {
            case 181:
                this.currentTrack.sampleRate = (int) value;
                break;
            case 17545:
                this.durationTimecode = (long) value;
                break;
        }
    }

    void stringElement(int id, String value) throws ParserException {
        switch (id) {
            case 134:
                this.currentTrack.codecId = value;
                return;
            case 17026:
                if (!"webm".equals(value) && !"matroska".equals(value)) {
                    throw new ParserException("DocType " + value + " not supported");
                }
                return;
            case 2274716:
                this.currentTrack.language = value;
                return;
            default:
                return;
        }
    }

    void binaryElement(int id, int contentSize, ExtractorInput input) throws InterruptedException, IOException {
        int byteValue;
        switch (id) {
            case avcodec.AV_CODEC_ID_XBM /* 161 */:
            case avcodec.AV_CODEC_ID_MSS1 /* 163 */:
                if (this.blockState == 0) {
                    this.blockTrackNumber = (int) this.varintReader.readUnsignedVarint(input, false, true, 8);
                    this.blockTrackNumberLength = this.varintReader.getLastLength();
                    this.blockDurationUs = -1L;
                    this.blockState = 1;
                    this.scratch.reset();
                }
                Track track = this.tracks.get(this.blockTrackNumber);
                if (track == null) {
                    input.skipFully(contentSize - this.blockTrackNumberLength);
                    this.blockState = 0;
                    return;
                }
                if (this.blockState == 1) {
                    readScratch(input, 3);
                    int lacing = (this.scratch.data[2] & 6) >> 1;
                    if (lacing == 0) {
                        this.blockLacingSampleCount = 1;
                        this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, 1);
                        this.blockLacingSampleSizes[0] = (contentSize - this.blockTrackNumberLength) - 3;
                    } else {
                        if (id != 163) {
                            throw new ParserException("Lacing only supported in SimpleBlocks.");
                        }
                        readScratch(input, 4);
                        this.blockLacingSampleCount = (this.scratch.data[3] & Constants.UNKNOWN) + 1;
                        this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, this.blockLacingSampleCount);
                        if (lacing == 2) {
                            int blockLacingSampleSize = ((contentSize - this.blockTrackNumberLength) - 4) / this.blockLacingSampleCount;
                            Arrays.fill(this.blockLacingSampleSizes, 0, this.blockLacingSampleCount, blockLacingSampleSize);
                        } else if (lacing == 1) {
                            int totalSamplesSize = 0;
                            int headerSize = 4;
                            for (int sampleIndex = 0; sampleIndex < this.blockLacingSampleCount - 1; sampleIndex++) {
                                this.blockLacingSampleSizes[sampleIndex] = 0;
                                do {
                                    headerSize++;
                                    readScratch(input, headerSize);
                                    byteValue = this.scratch.data[headerSize - 1] & Constants.UNKNOWN;
                                    int[] iArr = this.blockLacingSampleSizes;
                                    iArr[sampleIndex] = iArr[sampleIndex] + byteValue;
                                } while (byteValue == 255);
                                totalSamplesSize += this.blockLacingSampleSizes[sampleIndex];
                            }
                            this.blockLacingSampleSizes[this.blockLacingSampleCount - 1] = ((contentSize - this.blockTrackNumberLength) - headerSize) - totalSamplesSize;
                        } else if (lacing == 3) {
                            int totalSamplesSize2 = 0;
                            int headerSize2 = 4;
                            for (int sampleIndex2 = 0; sampleIndex2 < this.blockLacingSampleCount - 1; sampleIndex2++) {
                                this.blockLacingSampleSizes[sampleIndex2] = 0;
                                headerSize2++;
                                readScratch(input, headerSize2);
                                if (this.scratch.data[headerSize2 - 1] == 0) {
                                    throw new ParserException("No valid varint length mask found");
                                }
                                long readValue = 0;
                                int i = 0;
                                while (true) {
                                    if (i < 8) {
                                        int lengthMask = 1 << (7 - i);
                                        if ((this.scratch.data[headerSize2 - 1] & lengthMask) == 0) {
                                            i++;
                                        } else {
                                            int readPosition = headerSize2 - 1;
                                            headerSize2 += i;
                                            readScratch(input, headerSize2);
                                            readValue = this.scratch.data[readPosition] & Constants.UNKNOWN & (lengthMask ^ (-1));
                                            for (int readPosition2 = readPosition + 1; readPosition2 < headerSize2; readPosition2++) {
                                                readValue = (readValue << 8) | (this.scratch.data[readPosition2] & Constants.UNKNOWN);
                                            }
                                            if (sampleIndex2 > 0) {
                                                readValue -= (1 << ((i * 7) + 6)) - 1;
                                            }
                                        }
                                    }
                                }
                                if (readValue < -2147483648L || readValue > 2147483647L) {
                                    throw new ParserException("EBML lacing sample size out of range.");
                                }
                                int intReadValue = (int) readValue;
                                int[] iArr2 = this.blockLacingSampleSizes;
                                if (sampleIndex2 != 0) {
                                    intReadValue += this.blockLacingSampleSizes[sampleIndex2 - 1];
                                }
                                iArr2[sampleIndex2] = intReadValue;
                                totalSamplesSize2 += this.blockLacingSampleSizes[sampleIndex2];
                            }
                            this.blockLacingSampleSizes[this.blockLacingSampleCount - 1] = ((contentSize - this.blockTrackNumberLength) - headerSize2) - totalSamplesSize2;
                        } else {
                            throw new ParserException("Unexpected lacing value: " + lacing);
                        }
                    }
                    int timecode = (this.scratch.data[0] << 8) | (this.scratch.data[1] & Constants.UNKNOWN);
                    this.blockTimeUs = this.clusterTimecodeUs + scaleTimecodeToUs(timecode);
                    boolean isInvisible = (this.scratch.data[2] & 8) == 8;
                    boolean isKeyframe = track.type == 2 || (id == 163 && (this.scratch.data[2] & 128) == 128);
                    this.blockFlags = (isInvisible ? 134217728 : 0) | (isKeyframe ? 1 : 0);
                    this.blockState = 2;
                    this.blockLacingSampleIndex = 0;
                }
                if (id == 163) {
                    while (this.blockLacingSampleIndex < this.blockLacingSampleCount) {
                        writeSampleData(input, track, this.blockLacingSampleSizes[this.blockLacingSampleIndex]);
                        long sampleTimeUs = this.blockTimeUs + ((this.blockLacingSampleIndex * track.defaultSampleDurationNs) / 1000);
                        commitSampleToOutput(track, sampleTimeUs);
                        this.blockLacingSampleIndex++;
                    }
                    this.blockState = 0;
                    return;
                }
                writeSampleData(input, track, this.blockLacingSampleSizes[0]);
                return;
            case 16981:
                this.currentTrack.sampleStrippedBytes = new byte[contentSize];
                input.readFully(this.currentTrack.sampleStrippedBytes, 0, contentSize);
                return;
            case 18402:
                this.currentTrack.encryptionKeyId = new byte[contentSize];
                input.readFully(this.currentTrack.encryptionKeyId, 0, contentSize);
                return;
            case 21419:
                Arrays.fill(this.seekEntryIdBytes.data, (byte) 0);
                input.readFully(this.seekEntryIdBytes.data, 4 - contentSize, contentSize);
                this.seekEntryIdBytes.setPosition(0);
                this.seekEntryId = (int) this.seekEntryIdBytes.readUnsignedInt();
                return;
            case 25506:
                this.currentTrack.codecPrivate = new byte[contentSize];
                input.readFully(this.currentTrack.codecPrivate, 0, contentSize);
                return;
            default:
                throw new ParserException("Unexpected id: " + id);
        }
    }

    private void commitSampleToOutput(Track track, long timeUs) {
        if ("S_TEXT/UTF8".equals(track.codecId)) {
            writeSubripSample(track);
        }
        track.output.sampleMetadata(timeUs, this.blockFlags, this.sampleBytesWritten, 0, track.encryptionKeyId);
        this.sampleRead = true;
        resetSample();
    }

    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleStrippedBytes.reset();
    }

    private void readScratch(ExtractorInput input, int requiredLength) throws InterruptedException, IOException {
        if (this.scratch.limit() < requiredLength) {
            if (this.scratch.capacity() < requiredLength) {
                this.scratch.reset(Arrays.copyOf(this.scratch.data, Math.max(this.scratch.data.length * 2, requiredLength)), this.scratch.limit());
            }
            input.readFully(this.scratch.data, this.scratch.limit(), requiredLength - this.scratch.limit());
            this.scratch.setLimit(requiredLength);
        }
    }

    private void writeSampleData(ExtractorInput input, Track track, int size) throws InterruptedException, IOException {
        if ("S_TEXT/UTF8".equals(track.codecId)) {
            int sizeWithPrefix = SUBRIP_PREFIX.length + size;
            if (this.subripSample.capacity() < sizeWithPrefix) {
                this.subripSample.data = Arrays.copyOf(SUBRIP_PREFIX, sizeWithPrefix + size);
            }
            input.readFully(this.subripSample.data, SUBRIP_PREFIX.length, size);
            this.subripSample.setPosition(0);
            this.subripSample.setLimit(sizeWithPrefix);
            return;
        }
        TrackOutput output = track.output;
        if (!this.sampleEncodingHandled) {
            if (track.hasContentEncryption) {
                this.blockFlags &= -3;
                input.readFully(this.scratch.data, 0, 1);
                this.sampleBytesRead++;
                if ((this.scratch.data[0] & 128) == 128) {
                    throw new ParserException("Extension bit is set in signal byte");
                }
                if ((this.scratch.data[0] & 1) == 1) {
                    this.scratch.data[0] = 8;
                    this.scratch.setPosition(0);
                    output.sampleData(this.scratch, 1);
                    this.sampleBytesWritten++;
                    this.blockFlags |= 2;
                }
            } else if (track.sampleStrippedBytes != null) {
                this.sampleStrippedBytes.reset(track.sampleStrippedBytes, track.sampleStrippedBytes.length);
            }
            this.sampleEncodingHandled = true;
        }
        int size2 = size + this.sampleStrippedBytes.limit();
        if ("V_MPEG4/ISO/AVC".equals(track.codecId) || "V_MPEGH/ISO/HEVC".equals(track.codecId)) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.nalUnitLengthFieldLength;
            while (this.sampleBytesRead < size2) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    readToTarget(input, nalLengthData, nalUnitLengthFieldLengthDiff, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    output.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                } else {
                    this.sampleCurrentNalBytesRemaining -= readToOutput(input, output, this.sampleCurrentNalBytesRemaining);
                }
            }
        } else {
            while (this.sampleBytesRead < size2) {
                readToOutput(input, output, size2 - this.sampleBytesRead);
            }
        }
        if ("A_VORBIS".equals(track.codecId)) {
            this.vorbisNumPageSamples.setPosition(0);
            output.sampleData(this.vorbisNumPageSamples, 4);
            this.sampleBytesWritten += 4;
        }
    }

    private void writeSubripSample(Track track) {
        setSubripSampleEndTimecode(this.subripSample.data, this.blockDurationUs);
        track.output.sampleData(this.subripSample, this.subripSample.limit());
        this.sampleBytesWritten += this.subripSample.limit();
    }

    private static void setSubripSampleEndTimecode(byte[] subripSampleData, long timeUs) {
        byte[] timeCodeData;
        if (timeUs == -1) {
            timeCodeData = SUBRIP_TIMECODE_EMPTY;
        } else {
            int hours = (int) (timeUs / 3600000000L);
            long timeUs2 = timeUs - (hours * 3600000000L);
            int minutes = (int) (timeUs2 / 60000000);
            long timeUs3 = timeUs2 - (60000000 * minutes);
            int seconds = (int) (timeUs3 / 1000000);
            int milliseconds = (int) ((timeUs3 - (avutil.AV_TIME_BASE * seconds)) / 1000);
            timeCodeData = String.format(Locale.US, "%02d:%02d:%02d,%03d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds), Integer.valueOf(milliseconds)).getBytes();
        }
        System.arraycopy(timeCodeData, 0, subripSampleData, 19, 12);
    }

    private void readToTarget(ExtractorInput input, byte[] target, int offset, int length) throws InterruptedException, IOException {
        int pendingStrippedBytes = Math.min(length, this.sampleStrippedBytes.bytesLeft());
        input.readFully(target, offset + pendingStrippedBytes, length - pendingStrippedBytes);
        if (pendingStrippedBytes > 0) {
            this.sampleStrippedBytes.readBytes(target, offset, pendingStrippedBytes);
        }
        this.sampleBytesRead += length;
    }

    private int readToOutput(ExtractorInput input, TrackOutput output, int length) throws InterruptedException, IOException {
        int bytesRead;
        int strippedBytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (strippedBytesLeft > 0) {
            bytesRead = Math.min(length, strippedBytesLeft);
            output.sampleData(this.sampleStrippedBytes, bytesRead);
        } else {
            bytesRead = output.sampleData(input, length, false);
        }
        this.sampleBytesRead += bytesRead;
        this.sampleBytesWritten += bytesRead;
        return bytesRead;
    }

    private SeekMap buildSeekMap() {
        if (this.segmentContentPosition == -1 || this.durationUs == -1 || this.cueTimesUs == null || this.cueTimesUs.size() == 0 || this.cueClusterPositions == null || this.cueClusterPositions.size() != this.cueTimesUs.size()) {
            this.cueTimesUs = null;
            this.cueClusterPositions = null;
            return SeekMap.UNSEEKABLE;
        }
        int cuePointsSize = this.cueTimesUs.size();
        int[] sizes = new int[cuePointsSize];
        long[] offsets = new long[cuePointsSize];
        long[] durationsUs = new long[cuePointsSize];
        long[] timesUs = new long[cuePointsSize];
        for (int i = 0; i < cuePointsSize; i++) {
            timesUs[i] = this.cueTimesUs.get(i);
            offsets[i] = this.segmentContentPosition + this.cueClusterPositions.get(i);
        }
        for (int i2 = 0; i2 < cuePointsSize - 1; i2++) {
            sizes[i2] = (int) (offsets[i2 + 1] - offsets[i2]);
            durationsUs[i2] = timesUs[i2 + 1] - timesUs[i2];
        }
        sizes[cuePointsSize - 1] = (int) ((this.segmentContentPosition + this.segmentContentSize) - offsets[cuePointsSize - 1]);
        durationsUs[cuePointsSize - 1] = this.durationUs - timesUs[cuePointsSize - 1];
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return new ChunkIndex(sizes, offsets, durationsUs, timesUs);
    }

    private boolean maybeSeekForCues(PositionHolder seekPosition, long currentPosition) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = currentPosition;
            seekPosition.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        }
        if (!this.sentSeekMap || this.seekPositionAfterBuildingCues == -1) {
            return false;
        }
        seekPosition.position = this.seekPositionAfterBuildingCues;
        this.seekPositionAfterBuildingCues = -1L;
        return true;
    }

    private long scaleTimecodeToUs(long unscaledTimecode) throws ParserException {
        if (this.timecodeScale == -1) {
            throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
        }
        return Util.scaleLargeTimestamp(unscaledTimecode, this.timecodeScale, 1000L);
    }

    private static boolean isCodecSupported(String codecId) {
        return "V_VP8".equals(codecId) || "V_VP9".equals(codecId) || "V_MPEG2".equals(codecId) || "V_MPEG4/ISO/SP".equals(codecId) || "V_MPEG4/ISO/ASP".equals(codecId) || "V_MPEG4/ISO/AP".equals(codecId) || "V_MPEG4/ISO/AVC".equals(codecId) || "V_MPEGH/ISO/HEVC".equals(codecId) || "A_OPUS".equals(codecId) || "A_VORBIS".equals(codecId) || "A_AAC".equals(codecId) || "A_MPEG/L3".equals(codecId) || "A_AC3".equals(codecId) || "A_EAC3".equals(codecId) || "A_TRUEHD".equals(codecId) || "A_DTS".equals(codecId) || "A_DTS/EXPRESS".equals(codecId) || "A_DTS/LOSSLESS".equals(codecId) || "S_TEXT/UTF8".equals(codecId);
    }

    private static int[] ensureArrayCapacity(int[] array, int length) {
        if (array == null) {
            return new int[length];
        }
        return array.length < length ? new int[Math.max(array.length * 2, length)] : array;
    }

    private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
        private InnerEbmlReaderOutput() {
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public int getElementType(int id) {
            return WebmExtractor.this.getElementType(id);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public boolean isLevel1Element(int id) {
            return WebmExtractor.this.isLevel1Element(id);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
            WebmExtractor.this.startMasterElement(id, contentPosition, contentSize);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void endMasterElement(int id) throws ParserException {
            WebmExtractor.this.endMasterElement(id);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void integerElement(int id, long value) throws ParserException {
            WebmExtractor.this.integerElement(id, value);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void floatElement(int id, double value) throws ParserException {
            WebmExtractor.this.floatElement(id, value);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void stringElement(int id, String value) throws ParserException {
            WebmExtractor.this.stringElement(id, value);
        }

        @Override // com.google.android.exoplayer.extractor.webm.EbmlReaderOutput
        public void binaryElement(int id, int contentsSize, ExtractorInput input) throws InterruptedException, IOException {
            WebmExtractor.this.binaryElement(id, contentsSize, input);
        }
    }

    private static final class Track {
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int defaultSampleDurationNs;
        public byte[] encryptionKeyId;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int nalUnitLengthFieldLength;
        public int number;
        public TrackOutput output;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int type;
        public int width;

        private Track() {
            this.width = -1;
            this.height = -1;
            this.channelCount = 1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0L;
            this.seekPreRollNs = 0L;
            this.language = "eng";
        }

        public void initializeOutput(ExtractorOutput output, int trackId, long durationUs) throws ParserException {
            int maxInputSize;
            List<byte[]> initializationData;
            String mimeType;
            MediaFormat format;
            maxInputSize = -1;
            initializationData = null;
            switch (this.codecId) {
                case "V_VP8":
                    mimeType = "video/x-vnd.on2.vp8";
                    break;
                case "V_VP9":
                    mimeType = "video/x-vnd.on2.vp9";
                    break;
                case "V_MPEG2":
                    mimeType = "video/mpeg2";
                    break;
                case "V_MPEG4/ISO/SP":
                case "V_MPEG4/ISO/ASP":
                case "V_MPEG4/ISO/AP":
                    mimeType = "video/mp4v-es";
                    if (this.codecPrivate != null) {
                        initializationData = Collections.singletonList(this.codecPrivate);
                        break;
                    } else {
                        initializationData = null;
                        break;
                    }
                case "V_MPEG4/ISO/AVC":
                    mimeType = "video/avc";
                    Pair<List<byte[]>, Integer> h264Data = parseAvcCodecPrivate(new ParsableByteArray(this.codecPrivate));
                    initializationData = (List) h264Data.first;
                    this.nalUnitLengthFieldLength = ((Integer) h264Data.second).intValue();
                    break;
                case "V_MPEGH/ISO/HEVC":
                    mimeType = "video/hevc";
                    Pair<List<byte[]>, Integer> hevcData = parseHevcCodecPrivate(new ParsableByteArray(this.codecPrivate));
                    initializationData = (List) hevcData.first;
                    this.nalUnitLengthFieldLength = ((Integer) hevcData.second).intValue();
                    break;
                case "A_VORBIS":
                    mimeType = "audio/vorbis";
                    maxInputSize = 8192;
                    initializationData = parseVorbisCodecPrivate(this.codecPrivate);
                    break;
                case "A_OPUS":
                    mimeType = "audio/opus";
                    maxInputSize = 5760;
                    initializationData = new ArrayList<>(3);
                    initializationData.add(this.codecPrivate);
                    initializationData.add(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.codecDelayNs).array());
                    initializationData.add(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.seekPreRollNs).array());
                    break;
                case "A_AAC":
                    mimeType = "audio/mp4a-latm";
                    initializationData = Collections.singletonList(this.codecPrivate);
                    break;
                case "A_MPEG/L3":
                    mimeType = "audio/mpeg";
                    maxInputSize = 4096;
                    break;
                case "A_AC3":
                    mimeType = "audio/ac3";
                    break;
                case "A_EAC3":
                    mimeType = "audio/eac3";
                    break;
                case "A_TRUEHD":
                    mimeType = "audio/true-hd";
                    break;
                case "A_DTS":
                case "A_DTS/EXPRESS":
                    mimeType = "audio/vnd.dts";
                    break;
                case "A_DTS/LOSSLESS":
                    mimeType = "audio/vnd.dts.hd";
                    break;
                case "S_TEXT/UTF8":
                    mimeType = "application/x-subrip";
                    break;
                default:
                    throw new ParserException("Unrecognized codec identifier.");
            }
            if (MimeTypes.isAudio(mimeType)) {
                format = MediaFormat.createAudioFormat(Integer.toString(trackId), mimeType, -1, maxInputSize, durationUs, this.channelCount, this.sampleRate, initializationData, this.language);
            } else if (MimeTypes.isVideo(mimeType)) {
                format = MediaFormat.createVideoFormat(Integer.toString(trackId), mimeType, -1, maxInputSize, durationUs, this.width, this.height, initializationData);
            } else if ("application/x-subrip".equals(mimeType)) {
                format = MediaFormat.createTextFormat(Integer.toString(trackId), mimeType, -1, durationUs, this.language);
            } else {
                throw new ParserException("Unexpected MIME type.");
            }
            this.output = output.track(this.number);
            this.output.format(format);
        }

        private static Pair<List<byte[]>, Integer> parseAvcCodecPrivate(ParsableByteArray buffer) throws ParserException {
            try {
                buffer.setPosition(4);
                int nalUnitLengthFieldLength = (buffer.readUnsignedByte() & 3) + 1;
                if (nalUnitLengthFieldLength == 3) {
                    throw new ParserException();
                }
                List<byte[]> initializationData = new ArrayList<>();
                int numSequenceParameterSets = buffer.readUnsignedByte() & 31;
                for (int i = 0; i < numSequenceParameterSets; i++) {
                    initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
                }
                int numPictureParameterSets = buffer.readUnsignedByte();
                for (int j = 0; j < numPictureParameterSets; j++) {
                    initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
                }
                return Pair.create(initializationData, Integer.valueOf(nalUnitLengthFieldLength));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing AVC codec private");
            }
        }

        private static Pair<List<byte[]>, Integer> parseHevcCodecPrivate(ParsableByteArray parent) throws ParserException {
            try {
                parent.setPosition(21);
                int lengthSizeMinusOne = parent.readUnsignedByte() & 3;
                int numberOfArrays = parent.readUnsignedByte();
                int csdLength = 0;
                int csdStartPosition = parent.getPosition();
                for (int i = 0; i < numberOfArrays; i++) {
                    parent.skipBytes(1);
                    int numberOfNalUnits = parent.readUnsignedShort();
                    for (int j = 0; j < numberOfNalUnits; j++) {
                        int nalUnitLength = parent.readUnsignedShort();
                        csdLength += nalUnitLength + 4;
                        parent.skipBytes(nalUnitLength);
                    }
                }
                parent.setPosition(csdStartPosition);
                byte[] buffer = new byte[csdLength];
                int bufferPosition = 0;
                for (int i2 = 0; i2 < numberOfArrays; i2++) {
                    parent.skipBytes(1);
                    int numberOfNalUnits2 = parent.readUnsignedShort();
                    for (int j2 = 0; j2 < numberOfNalUnits2; j2++) {
                        int nalUnitLength2 = parent.readUnsignedShort();
                        System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, buffer, bufferPosition, NalUnitUtil.NAL_START_CODE.length);
                        int bufferPosition2 = bufferPosition + NalUnitUtil.NAL_START_CODE.length;
                        System.arraycopy(parent.data, parent.getPosition(), buffer, bufferPosition2, nalUnitLength2);
                        bufferPosition = bufferPosition2 + nalUnitLength2;
                        parent.skipBytes(nalUnitLength2);
                    }
                }
                List<byte[]> initializationData = csdLength == 0 ? null : Collections.singletonList(buffer);
                return Pair.create(initializationData, Integer.valueOf(lengthSizeMinusOne + 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing HEVC codec private");
            }
        }

        private static List<byte[]> parseVorbisCodecPrivate(byte[] codecPrivate) throws ParserException {
            try {
                if (codecPrivate[0] != 2) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                int vorbisInfoLength = 0;
                int offset = 1;
                while (codecPrivate[offset] == -1) {
                    vorbisInfoLength += 255;
                    offset++;
                }
                int vorbisInfoLength2 = vorbisInfoLength + codecPrivate[offset];
                int vorbisSkipLength = 0;
                int offset2 = offset + 1;
                while (codecPrivate[offset2] == -1) {
                    vorbisSkipLength += 255;
                    offset2++;
                }
                int offset3 = offset2 + 1;
                int vorbisSkipLength2 = vorbisSkipLength + codecPrivate[offset2];
                if (codecPrivate[offset3] != 1) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisInfo = new byte[vorbisInfoLength2];
                System.arraycopy(codecPrivate, offset3, vorbisInfo, 0, vorbisInfoLength2);
                int offset4 = offset3 + vorbisInfoLength2;
                if (codecPrivate[offset4] != 3) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                int offset5 = offset4 + vorbisSkipLength2;
                if (codecPrivate[offset5] != 5) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisBooks = new byte[codecPrivate.length - offset5];
                System.arraycopy(codecPrivate, offset5, vorbisBooks, 0, codecPrivate.length - offset5);
                List<byte[]> initializationData = new ArrayList<>(2);
                initializationData.add(vorbisInfo);
                initializationData.add(vorbisBooks);
                return initializationData;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing vorbis codec private");
            }
        }
    }
}
