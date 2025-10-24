package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.mp4.Atom;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes.dex */
public final class Mp4Extractor implements Extractor, SeekMap {
    private static final int BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
    private ParsableByteArray atomData;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private ExtractorOutput extractorOutput;
    private boolean isQuickTime;
    private int parserState;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private int sampleSize;
    private Mp4Track[] tracks;
    private final ParsableByteArray atomHeader = new ParsableByteArray(16);
    private final Stack<Atom.ContainerAtom> containerAtoms = new Stack<>();
    private final ParsableByteArray nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
    private final ParsableByteArray nalLength = new ParsableByteArray(4);

    public Mp4Extractor() {
        enterReadingAtomHeaderState();
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return Sniffer.sniffUnfragmented(input);
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public void seek() {
        this.containerAtoms.clear();
        this.atomHeaderBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.parserState = 0;
    }

    @Override // com.google.android.exoplayer.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        while (true) {
            switch (this.parserState) {
                case 0:
                    if (input.getPosition() == 0) {
                        enterReadingAtomHeaderState();
                        break;
                    } else {
                        this.parserState = 3;
                        break;
                    }
                case 1:
                    if (!readAtomHeader(input)) {
                        return -1;
                    }
                    break;
                case 2:
                    if (!readAtomPayload(input, seekPosition)) {
                        break;
                    } else {
                        return 1;
                    }
                default:
                    return readSample(input, seekPosition);
            }
        }
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // com.google.android.exoplayer.extractor.SeekMap
    public long getPosition(long timeUs) {
        long earliestSamplePosition = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < this.tracks.length; trackIndex++) {
            TrackSampleTable sampleTable = this.tracks[trackIndex].sampleTable;
            int sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(timeUs);
            if (sampleIndex == -1) {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(timeUs);
            }
            this.tracks[trackIndex].sampleIndex = sampleIndex;
            long offset = sampleTable.offsets[sampleIndex];
            if (offset < earliestSamplePosition) {
                earliestSamplePosition = offset;
            }
        }
        return earliestSamplePosition;
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 1;
        this.atomHeaderBytesRead = 0;
    }

    private boolean readAtomHeader(ExtractorInput input) throws InterruptedException, IOException {
        if (this.atomHeaderBytesRead == 0) {
            if (!input.readFully(this.atomHeader.data, 0, 8, true)) {
                return false;
            }
            this.atomHeaderBytesRead = 8;
            this.atomHeader.setPosition(0);
            this.atomSize = this.atomHeader.readUnsignedInt();
            this.atomType = this.atomHeader.readInt();
        }
        if (this.atomSize == 1) {
            input.readFully(this.atomHeader.data, 8, 8);
            this.atomHeaderBytesRead += 8;
            this.atomSize = this.atomHeader.readUnsignedLongToLong();
        }
        if (shouldParseContainerAtom(this.atomType)) {
            long endPosition = (input.getPosition() + this.atomSize) - this.atomHeaderBytesRead;
            this.containerAtoms.add(new Atom.ContainerAtom(this.atomType, endPosition));
            enterReadingAtomHeaderState();
        } else if (shouldParseLeafAtom(this.atomType)) {
            Assertions.checkState(this.atomHeaderBytesRead == 8);
            Assertions.checkState(this.atomSize <= 2147483647L);
            this.atomData = new ParsableByteArray((int) this.atomSize);
            System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
            this.parserState = 2;
        } else {
            this.atomData = null;
            this.parserState = 2;
        }
        return true;
    }

    private boolean readAtomPayload(ExtractorInput input, PositionHolder positionHolder) throws InterruptedException, IOException {
        long atomPayloadSize = this.atomSize - this.atomHeaderBytesRead;
        long atomEndPosition = input.getPosition() + atomPayloadSize;
        boolean seekRequired = false;
        if (this.atomData != null) {
            input.readFully(this.atomData.data, this.atomHeaderBytesRead, (int) atomPayloadSize);
            if (this.atomType == Atom.TYPE_ftyp) {
                this.isQuickTime = processFtypAtom(this.atomData);
            } else if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(new Atom.LeafAtom(this.atomType, this.atomData));
            }
        } else if (atomPayloadSize < 262144) {
            input.skipFully((int) atomPayloadSize);
        } else {
            positionHolder.position = input.getPosition() + atomPayloadSize;
            seekRequired = true;
        }
        while (!this.containerAtoms.isEmpty() && this.containerAtoms.peek().endPosition == atomEndPosition) {
            Atom.ContainerAtom containerAtom = this.containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov) {
                processMoovAtom(containerAtom);
                this.containerAtoms.clear();
                this.parserState = 3;
                return false;
            }
            if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(containerAtom);
            }
        }
        enterReadingAtomHeaderState();
        return seekRequired;
    }

    private static boolean processFtypAtom(ParsableByteArray atomData) {
        atomData.setPosition(8);
        int majorBrand = atomData.readInt();
        if (majorBrand == BRAND_QUICKTIME) {
            return true;
        }
        atomData.skipBytes(4);
        while (atomData.bytesLeft() > 0) {
            if (atomData.readInt() == BRAND_QUICKTIME) {
                return true;
            }
        }
        return false;
    }

    private void processMoovAtom(Atom.ContainerAtom moov) {
        Track track;
        List<Mp4Track> tracks = new ArrayList<>();
        long earliestSampleOffset = Long.MAX_VALUE;
        for (int i = 0; i < moov.containerChildren.size(); i++) {
            Atom.ContainerAtom atom = moov.containerChildren.get(i);
            if (atom.type == Atom.TYPE_trak && (track = AtomParsers.parseTrak(atom, moov.getLeafAtomOfType(Atom.TYPE_mvhd), this.isQuickTime)) != null) {
                Atom.ContainerAtom stblAtom = atom.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
                TrackSampleTable trackSampleTable = AtomParsers.parseStbl(track, stblAtom);
                if (trackSampleTable.sampleCount != 0) {
                    Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, this.extractorOutput.track(i));
                    int maxInputSize = trackSampleTable.maximumSize + 30;
                    mp4Track.trackOutput.format(track.mediaFormat.copyWithMaxInputSize(maxInputSize));
                    tracks.add(mp4Track);
                    long firstSampleOffset = trackSampleTable.offsets[0];
                    if (firstSampleOffset < earliestSampleOffset) {
                        earliestSampleOffset = firstSampleOffset;
                    }
                }
            }
        }
        this.tracks = (Mp4Track[]) tracks.toArray(new Mp4Track[0]);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
    }

    private int readSample(ExtractorInput input, PositionHolder positionHolder) throws InterruptedException, IOException {
        int trackIndex = getTrackIndexOfEarliestCurrentSample();
        if (trackIndex == -1) {
            return -1;
        }
        Mp4Track track = this.tracks[trackIndex];
        TrackOutput trackOutput = track.trackOutput;
        int sampleIndex = track.sampleIndex;
        long position = track.sampleTable.offsets[sampleIndex];
        long skipAmount = (position - input.getPosition()) + this.sampleBytesWritten;
        if (skipAmount < 0 || skipAmount >= 262144) {
            positionHolder.position = position;
            return 1;
        }
        input.skipFully((int) skipAmount);
        this.sampleSize = track.sampleTable.sizes[sampleIndex];
        if (track.track.nalUnitLengthFieldLength != -1) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.track.nalUnitLengthFieldLength;
            while (this.sampleBytesWritten < this.sampleSize) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    input.readFully(this.nalLength.data, nalUnitLengthFieldLengthDiff, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    trackOutput.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                    this.sampleSize += nalUnitLengthFieldLengthDiff;
                } else {
                    int writtenBytes = trackOutput.sampleData(input, this.sampleCurrentNalBytesRemaining, false);
                    this.sampleBytesWritten += writtenBytes;
                    this.sampleCurrentNalBytesRemaining -= writtenBytes;
                }
            }
        } else {
            while (this.sampleBytesWritten < this.sampleSize) {
                int writtenBytes2 = trackOutput.sampleData(input, this.sampleSize - this.sampleBytesWritten, false);
                this.sampleBytesWritten += writtenBytes2;
                this.sampleCurrentNalBytesRemaining -= writtenBytes2;
            }
        }
        trackOutput.sampleMetadata(track.sampleTable.timestampsUs[sampleIndex], track.sampleTable.flags[sampleIndex], this.sampleSize, 0, null);
        track.sampleIndex++;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        return 0;
    }

    private int getTrackIndexOfEarliestCurrentSample() {
        int earliestSampleTrackIndex = -1;
        long earliestSampleOffset = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < this.tracks.length; trackIndex++) {
            Mp4Track track = this.tracks[trackIndex];
            int sampleIndex = track.sampleIndex;
            if (sampleIndex != track.sampleTable.sampleCount) {
                long trackSampleOffset = track.sampleTable.offsets[sampleIndex];
                if (trackSampleOffset < earliestSampleOffset) {
                    earliestSampleOffset = trackSampleOffset;
                    earliestSampleTrackIndex = trackIndex;
                }
            }
        }
        return earliestSampleTrackIndex;
    }

    private static boolean shouldParseLeafAtom(int atom) {
        return atom == Atom.TYPE_mdhd || atom == Atom.TYPE_mvhd || atom == Atom.TYPE_hdlr || atom == Atom.TYPE_stsd || atom == Atom.TYPE_stts || atom == Atom.TYPE_stss || atom == Atom.TYPE_ctts || atom == Atom.TYPE_elst || atom == Atom.TYPE_stsc || atom == Atom.TYPE_stsz || atom == Atom.TYPE_stco || atom == Atom.TYPE_co64 || atom == Atom.TYPE_tkhd || atom == Atom.TYPE_ftyp || atom == Atom.TYPE_meta;
    }

    private static boolean shouldParseContainerAtom(int atom) {
        return atom == Atom.TYPE_moov || atom == Atom.TYPE_trak || atom == Atom.TYPE_mdia || atom == Atom.TYPE_minf || atom == Atom.TYPE_stbl || atom == Atom.TYPE_edts || atom == Atom.TYPE_udta;
    }

    private static final class Mp4Track {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;

        public Mp4Track(Track track, TrackSampleTable sampleTable, TrackOutput trackOutput) {
            this.track = track;
            this.sampleTable = sampleTable;
            this.trackOutput = trackOutput;
        }
    }
}
