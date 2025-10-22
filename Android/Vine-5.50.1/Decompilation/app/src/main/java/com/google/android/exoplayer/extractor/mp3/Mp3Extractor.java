package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.GaplessInfo;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import com.googlecode.javacv.cpp.avcodec;
import java.io.EOFException;
import java.io.IOException;

/* loaded from: classes.dex */
public final class Mp3Extractor implements Extractor {
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final long forcedFirstSampleTimestampUs;
    private GaplessInfo gaplessInfo;
    private int sampleBytesRemaining;
    private int samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;
    private static final int XING_HEADER = Util.getIntegerCodeForString("Xing");
    private static final int INFO_HEADER = Util.getIntegerCodeForString("Info");
    private static final int VBRI_HEADER = Util.getIntegerCodeForString("VBRI");

    interface Seeker extends SeekMap {
        long getDurationUs();

        long getTimeUs(long j);
    }

    public Mp3Extractor() {
        this(-1L);
    }

    public Mp3Extractor(long forcedFirstSampleTimestampUs) {
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.scratch = new ParsableByteArray(4);
        this.synchronizedHeader = new MpegAudioHeader();
        this.basisTimeUs = -1L;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return synchronize(input, true);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0);
        extractorOutput.endTracks();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void seek() {
        this.synchronizedHeaderData = 0;
        this.samplesRead = 0;
        this.basisTimeUs = -1L;
        this.sampleBytesRemaining = 0;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        if (this.synchronizedHeaderData == 0 && !synchronizeCatchingEndOfInput(input)) {
            return -1;
        }
        if (this.seeker == null) {
            setupSeeker(input);
            this.extractorOutput.seekMap(this.seeker);
            this.trackOutput.format(MediaFormat.createAudioFormat(null, this.synchronizedHeader.mimeType, -1, 4096, this.seeker.getDurationUs(), this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, null, null));
        }
        return readSample(input);
    }

    private int readSample(ExtractorInput extractorInput) throws InterruptedException, IOException {
        if (this.sampleBytesRemaining == 0) {
            if (!maybeResynchronize(extractorInput)) {
                return -1;
            }
            if (this.basisTimeUs == -1) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != -1) {
                    long embeddedFirstSampleTimestampUs = this.seeker.getTimeUs(0L);
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - embeddedFirstSampleTimestampUs;
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        int bytesAppended = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (bytesAppended == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= bytesAppended;
        if (this.sampleBytesRemaining > 0) {
            return 0;
        }
        long timeUs = this.basisTimeUs + ((this.samplesRead * 1000000) / this.synchronizedHeader.sampleRate);
        this.trackOutput.sampleMetadata(timeUs, 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    private boolean maybeResynchronize(ExtractorInput extractorInput) throws InterruptedException, IOException {
        extractorInput.resetPeekPosition();
        if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
            return false;
        }
        this.scratch.setPosition(0);
        int sampleHeaderData = this.scratch.readInt();
        if ((sampleHeaderData & (-128000)) == (this.synchronizedHeaderData & (-128000))) {
            int frameSize = MpegAudioHeader.getFrameSize(sampleHeaderData);
            if (frameSize != -1) {
                MpegAudioHeader.populateHeader(sampleHeaderData, this.synchronizedHeader);
                return true;
            }
        }
        this.synchronizedHeaderData = 0;
        extractorInput.skipFully(1);
        return synchronizeCatchingEndOfInput(extractorInput);
    }

    private boolean synchronizeCatchingEndOfInput(ExtractorInput input) throws InterruptedException, IOException {
        try {
            return synchronize(input, false);
        } catch (EOFException e) {
            return false;
        }
    }

    private boolean synchronize(ExtractorInput input, boolean sniffing) throws InterruptedException, IOException {
        int frameSize;
        int searched = 0;
        int validFrameCount = 0;
        int candidateSynchronizedHeaderData = 0;
        int peekedId3Bytes = 0;
        input.resetPeekPosition();
        if (input.getPosition() == 0) {
            this.gaplessInfo = Id3Util.parseId3(input);
            peekedId3Bytes = (int) input.getPeekPosition();
        }
        while (true) {
            if (sniffing && searched == 4096) {
                return false;
            }
            if (!sniffing && searched == 131072) {
                throw new ParserException("Searched too many bytes.");
            }
            if (!input.peekFully(this.scratch.data, 0, 4, true)) {
                return false;
            }
            this.scratch.setPosition(0);
            int headerData = this.scratch.readInt();
            if ((candidateSynchronizedHeaderData != 0 && ((-128000) & headerData) != ((-128000) & candidateSynchronizedHeaderData)) || (frameSize = MpegAudioHeader.getFrameSize(headerData)) == -1) {
                validFrameCount = 0;
                candidateSynchronizedHeaderData = 0;
                searched++;
                if (sniffing) {
                    input.resetPeekPosition();
                    input.advancePeekPosition(peekedId3Bytes + searched);
                } else {
                    input.skipFully(1);
                }
            } else {
                validFrameCount++;
                if (validFrameCount == 1) {
                    MpegAudioHeader.populateHeader(headerData, this.synchronizedHeader);
                    candidateSynchronizedHeaderData = headerData;
                } else if (validFrameCount == 4) {
                    if (sniffing) {
                        input.skipFully(peekedId3Bytes + searched);
                    } else {
                        input.resetPeekPosition();
                    }
                    this.synchronizedHeaderData = candidateSynchronizedHeaderData;
                    return true;
                }
                input.advancePeekPosition(frameSize - 4);
            }
        }
    }

    private void setupSeeker(ExtractorInput input) throws InterruptedException, IOException {
        int xingBase = 21;
        ParsableByteArray frame = new ParsableByteArray(this.synchronizedHeader.frameSize);
        input.peekFully(frame.data, 0, this.synchronizedHeader.frameSize);
        long position = input.getPosition();
        long length = input.getLength();
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                xingBase = 36;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            xingBase = 13;
        }
        frame.setPosition(xingBase);
        int headerData = frame.readInt();
        if (headerData == XING_HEADER || headerData == INFO_HEADER) {
            this.seeker = XingSeeker.create(this.synchronizedHeader, frame, position, length);
            if (this.seeker != null && this.gaplessInfo == null) {
                input.resetPeekPosition();
                input.advancePeekPosition(xingBase + avcodec.AV_CODEC_ID_VP8);
                input.peekFully(this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                this.gaplessInfo = GaplessInfo.createFromXingHeaderValue(this.scratch.readUnsignedInt24());
            }
            input.skipFully(this.synchronizedHeader.frameSize);
        } else {
            frame.setPosition(36);
            if (frame.readInt() == VBRI_HEADER) {
                this.seeker = VbriSeeker.create(this.synchronizedHeader, frame, position, length);
                input.skipFully(this.synchronizedHeader.frameSize);
            }
        }
        if (this.seeker == null) {
            input.resetPeekPosition();
            input.peekFully(this.scratch.data, 0, 4);
            this.scratch.setPosition(0);
            MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
            this.seeker = new ConstantBitrateSeeker(input.getPosition(), this.synchronizedHeader.bitrate, length);
        }
    }
}
