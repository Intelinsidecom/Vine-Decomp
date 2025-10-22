package com.google.android.exoplayer.extractor.mp4;

import android.util.Pair;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.mp4.Atom;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import com.googlecode.javacv.cpp.avcodec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class AtomParsers {
    public static Track parseTrak(Atom.ContainerAtom trak, Atom.LeafAtom mvhd, boolean isQuickTime) {
        long durationUs;
        Atom.ContainerAtom mdia = trak.getContainerAtomOfType(Atom.TYPE_mdia);
        int trackType = parseHdlr(mdia.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (trackType != Track.TYPE_soun && trackType != Track.TYPE_vide && trackType != Track.TYPE_text && trackType != Track.TYPE_sbtl && trackType != Track.TYPE_subt) {
            return null;
        }
        TkhdData tkhdData = parseTkhd(trak.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        long duration = tkhdData.duration;
        long movieTimescale = parseMvhd(mvhd.data);
        if (duration == -1) {
            durationUs = -1;
        } else {
            durationUs = Util.scaleLargeTimestamp(duration, 1000000L, movieTimescale);
        }
        Atom.ContainerAtom stbl = mdia.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair<Long, String> mdhdData = parseMdhd(mdia.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData stsdData = parseStsd(stbl.getLeafAtomOfType(Atom.TYPE_stsd).data, tkhdData.id, durationUs, tkhdData.rotationDegrees, (String) mdhdData.second, isQuickTime);
        Pair<long[], long[]> edtsData = parseEdts(trak.getContainerAtomOfType(Atom.TYPE_edts));
        if (stsdData.mediaFormat == null) {
            return null;
        }
        return new Track(tkhdData.id, trackType, ((Long) mdhdData.first).longValue(), movieTimescale, durationUs, stsdData.mediaFormat, stsdData.trackEncryptionBoxes, stsdData.nalUnitLengthFieldLength, (long[]) edtsData.first, (long[]) edtsData.second);
    }

    public static TrackSampleTable parseStbl(Track track, Atom.ContainerAtom stblAtom) {
        long offsetBytes;
        ParsableByteArray stsz = stblAtom.getLeafAtomOfType(Atom.TYPE_stsz).data;
        Atom.LeafAtom chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stco);
        if (chunkOffsetsAtom == null) {
            chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_co64);
        }
        ParsableByteArray chunkOffsets = chunkOffsetsAtom.data;
        ParsableByteArray stsc = stblAtom.getLeafAtomOfType(Atom.TYPE_stsc).data;
        ParsableByteArray stts = stblAtom.getLeafAtomOfType(Atom.TYPE_stts).data;
        Atom.LeafAtom stssAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray stss = stssAtom != null ? stssAtom.data : null;
        Atom.LeafAtom cttsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray ctts = cttsAtom != null ? cttsAtom.data : null;
        stsz.setPosition(12);
        int fixedSampleSize = stsz.readUnsignedIntToInt();
        int sampleCount = stsz.readUnsignedIntToInt();
        long[] offsets = new long[sampleCount];
        int[] sizes = new int[sampleCount];
        int maximumSize = 0;
        long[] timestamps = new long[sampleCount];
        int[] flags = new int[sampleCount];
        if (sampleCount == 0) {
            return new TrackSampleTable(offsets, sizes, 0, timestamps, flags);
        }
        chunkOffsets.setPosition(12);
        int chunkCount = chunkOffsets.readUnsignedIntToInt();
        stsc.setPosition(12);
        int remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt() - 1;
        Assertions.checkState(stsc.readInt() == 1, "stsc first chunk must be 1");
        int samplesPerChunk = stsc.readUnsignedIntToInt();
        stsc.skipBytes(4);
        int nextSamplesPerChunkChangeChunkIndex = remainingSamplesPerChunkChanges > 0 ? stsc.readUnsignedIntToInt() - 1 : -1;
        int chunkIndex = 0;
        int remainingSamplesInChunk = samplesPerChunk;
        stts.setPosition(12);
        int remainingTimestampDeltaChanges = stts.readUnsignedIntToInt() - 1;
        int remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
        int timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
        int remainingSamplesAtTimestampOffset = 0;
        int remainingTimestampOffsetChanges = 0;
        int timestampOffset = 0;
        if (ctts != null) {
            ctts.setPosition(12);
            remainingTimestampOffsetChanges = ctts.readUnsignedIntToInt() - 1;
            remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
            timestampOffset = ctts.readInt();
        }
        int nextSynchronizationSampleIndex = -1;
        int remainingSynchronizationSamples = 0;
        if (stss != null) {
            stss.setPosition(12);
            remainingSynchronizationSamples = stss.readUnsignedIntToInt();
            nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
        }
        if (chunkOffsetsAtom.type == Atom.TYPE_stco) {
            offsetBytes = chunkOffsets.readUnsignedInt();
        } else {
            offsetBytes = chunkOffsets.readUnsignedLongToLong();
        }
        long timestampTimeUnits = 0;
        for (int i = 0; i < sampleCount; i++) {
            offsets[i] = offsetBytes;
            sizes[i] = fixedSampleSize == 0 ? stsz.readUnsignedIntToInt() : fixedSampleSize;
            if (sizes[i] > maximumSize) {
                maximumSize = sizes[i];
            }
            timestamps[i] = timestampOffset + timestampTimeUnits;
            flags[i] = stss == null ? 1 : 0;
            if (i == nextSynchronizationSampleIndex) {
                flags[i] = 1;
                remainingSynchronizationSamples--;
                if (remainingSynchronizationSamples > 0) {
                    nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
                }
            }
            timestampTimeUnits += timestampDeltaInTimeUnits;
            remainingSamplesAtTimestampDelta--;
            if (remainingSamplesAtTimestampDelta == 0 && remainingTimestampDeltaChanges > 0) {
                remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
                timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
                remainingTimestampDeltaChanges--;
            }
            if (ctts != null && remainingSamplesAtTimestampOffset - 1 == 0 && remainingTimestampOffsetChanges > 0) {
                remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
                timestampOffset = ctts.readInt();
                remainingTimestampOffsetChanges--;
            }
            remainingSamplesInChunk--;
            if (remainingSamplesInChunk == 0) {
                chunkIndex++;
                if (chunkIndex < chunkCount) {
                    if (chunkOffsetsAtom.type == Atom.TYPE_stco) {
                        offsetBytes = chunkOffsets.readUnsignedInt();
                    } else {
                        offsetBytes = chunkOffsets.readUnsignedLongToLong();
                    }
                }
                if (chunkIndex == nextSamplesPerChunkChangeChunkIndex) {
                    samplesPerChunk = stsc.readUnsignedIntToInt();
                    stsc.skipBytes(4);
                    remainingSamplesPerChunkChanges--;
                    if (remainingSamplesPerChunkChanges > 0) {
                        nextSamplesPerChunkChangeChunkIndex = stsc.readUnsignedIntToInt() - 1;
                    }
                }
                if (chunkIndex < chunkCount) {
                    remainingSamplesInChunk = samplesPerChunk;
                }
            } else {
                offsetBytes += sizes[i];
            }
        }
        Assertions.checkArgument(remainingSynchronizationSamples == 0);
        Assertions.checkArgument(remainingSamplesAtTimestampDelta == 0);
        Assertions.checkArgument(remainingSamplesInChunk == 0);
        Assertions.checkArgument(remainingTimestampDeltaChanges == 0);
        Assertions.checkArgument(remainingTimestampOffsetChanges == 0);
        if (track.editListDurations == null) {
            Util.scaleLargeTimestampsInPlace(timestamps, 1000000L, track.timescale);
            return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
        }
        if (track.editListDurations.length == 1 && track.editListDurations[0] == 0) {
            for (int i2 = 0; i2 < timestamps.length; i2++) {
                timestamps[i2] = Util.scaleLargeTimestamp(timestamps[i2] - track.editListMediaTimes[0], 1000000L, track.timescale);
            }
            return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
        }
        int editedSampleCount = 0;
        int nextSampleIndex = 0;
        boolean copyMetadata = false;
        for (int i3 = 0; i3 < track.editListDurations.length; i3++) {
            long mediaTime = track.editListMediaTimes[i3];
            if (mediaTime != -1) {
                long duration = Util.scaleLargeTimestamp(track.editListDurations[i3], track.timescale, track.movieTimescale);
                int startIndex = Util.binarySearchCeil(timestamps, mediaTime, true, true);
                int endIndex = Util.binarySearchCeil(timestamps, mediaTime + duration, true, false);
                editedSampleCount += endIndex - startIndex;
                copyMetadata |= nextSampleIndex != startIndex;
                nextSampleIndex = endIndex;
            }
        }
        boolean copyMetadata2 = copyMetadata | (editedSampleCount != sampleCount);
        long[] editedOffsets = copyMetadata2 ? new long[editedSampleCount] : offsets;
        int[] editedSizes = copyMetadata2 ? new int[editedSampleCount] : sizes;
        int editedMaximumSize = copyMetadata2 ? 0 : maximumSize;
        int[] editedFlags = copyMetadata2 ? new int[editedSampleCount] : flags;
        long[] editedTimestamps = new long[editedSampleCount];
        long pts = 0;
        int sampleIndex = 0;
        for (int i4 = 0; i4 < track.editListDurations.length; i4++) {
            long mediaTime2 = track.editListMediaTimes[i4];
            long duration2 = track.editListDurations[i4];
            if (mediaTime2 != -1) {
                long endMediaTime = mediaTime2 + Util.scaleLargeTimestamp(duration2, track.timescale, track.movieTimescale);
                int startIndex2 = Util.binarySearchCeil(timestamps, mediaTime2, true, true);
                int endIndex2 = Util.binarySearchCeil(timestamps, endMediaTime, true, false);
                if (copyMetadata2) {
                    int count = endIndex2 - startIndex2;
                    System.arraycopy(offsets, startIndex2, editedOffsets, sampleIndex, count);
                    System.arraycopy(sizes, startIndex2, editedSizes, sampleIndex, count);
                    System.arraycopy(flags, startIndex2, editedFlags, sampleIndex, count);
                }
                for (int j = startIndex2; j < endIndex2; j++) {
                    long ptsUs = Util.scaleLargeTimestamp(pts, 1000000L, track.movieTimescale);
                    long timeInSegmentUs = Util.scaleLargeTimestamp(timestamps[j] - mediaTime2, 1000000L, track.timescale);
                    editedTimestamps[sampleIndex] = ptsUs + timeInSegmentUs;
                    if (copyMetadata2 && editedSizes[sampleIndex] > editedMaximumSize) {
                        editedMaximumSize = sizes[j];
                    }
                    sampleIndex++;
                }
            }
            pts += duration2;
        }
        return new TrackSampleTable(editedOffsets, editedSizes, editedMaximumSize, editedTimestamps, editedFlags);
    }

    private static long parseMvhd(ParsableByteArray mvhd) {
        mvhd.setPosition(8);
        int fullAtom = mvhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        mvhd.skipBytes(version != 0 ? 16 : 8);
        return mvhd.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray tkhd) {
        long duration;
        int rotationDegrees;
        tkhd.setPosition(8);
        int fullAtom = tkhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        tkhd.skipBytes(version == 0 ? 8 : 16);
        int trackId = tkhd.readInt();
        tkhd.skipBytes(4);
        boolean durationUnknown = true;
        int durationPosition = tkhd.getPosition();
        int durationByteCount = version == 0 ? 4 : 8;
        int i = 0;
        while (true) {
            if (i >= durationByteCount) {
                break;
            }
            if (tkhd.data[durationPosition + i] == -1) {
                i++;
            } else {
                durationUnknown = false;
                break;
            }
        }
        if (durationUnknown) {
            tkhd.skipBytes(durationByteCount);
            duration = -1;
        } else {
            duration = version == 0 ? tkhd.readUnsignedInt() : tkhd.readUnsignedLongToLong();
        }
        tkhd.skipBytes(16);
        int a00 = tkhd.readInt();
        int a01 = tkhd.readInt();
        tkhd.skipBytes(4);
        int a10 = tkhd.readInt();
        int a11 = tkhd.readInt();
        if (a00 == 0 && a01 == 65536 && a10 == (-65536) && a11 == 0) {
            rotationDegrees = 90;
        } else if (a00 == 0 && a01 == (-65536) && a10 == 65536 && a11 == 0) {
            rotationDegrees = 270;
        } else if (a00 == (-65536) && a01 == 0 && a10 == 0 && a11 == (-65536)) {
            rotationDegrees = 180;
        } else {
            rotationDegrees = 0;
        }
        return new TkhdData(trackId, duration, rotationDegrees);
    }

    private static int parseHdlr(ParsableByteArray hdlr) {
        hdlr.setPosition(16);
        return hdlr.readInt();
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray mdhd) {
        mdhd.setPosition(8);
        int fullAtom = mdhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        mdhd.skipBytes(version == 0 ? 8 : 16);
        long timescale = mdhd.readUnsignedInt();
        mdhd.skipBytes(version == 0 ? 4 : 8);
        int languageCode = mdhd.readUnsignedShort();
        String language = "" + ((char) (((languageCode >> 10) & 31) + 96)) + ((char) (((languageCode >> 5) & 31) + 96)) + ((char) ((languageCode & 31) + 96));
        return Pair.create(Long.valueOf(timescale), language);
    }

    private static StsdData parseStsd(ParsableByteArray stsd, int trackId, long durationUs, int rotationDegrees, String language, boolean isQuickTime) {
        stsd.setPosition(12);
        int numberOfEntries = stsd.readInt();
        StsdData out = new StsdData(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            int childStartPosition = stsd.getPosition();
            int childAtomSize = stsd.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = stsd.readInt();
            if (childAtomType == Atom.TYPE_avc1 || childAtomType == Atom.TYPE_avc3 || childAtomType == Atom.TYPE_encv || childAtomType == Atom.TYPE_mp4v || childAtomType == Atom.TYPE_hvc1 || childAtomType == Atom.TYPE_hev1 || childAtomType == Atom.TYPE_s263) {
                parseVideoSampleEntry(stsd, childStartPosition, childAtomSize, trackId, durationUs, rotationDegrees, out, i);
            } else if (childAtomType == Atom.TYPE_mp4a || childAtomType == Atom.TYPE_enca || childAtomType == Atom.TYPE_ac_3 || childAtomType == Atom.TYPE_ec_3 || childAtomType == Atom.TYPE_dtsc || childAtomType == Atom.TYPE_dtse || childAtomType == Atom.TYPE_dtsh || childAtomType == Atom.TYPE_dtsl || childAtomType == Atom.TYPE_samr || childAtomType == Atom.TYPE_sawb) {
                parseAudioSampleEntry(stsd, childAtomType, childStartPosition, childAtomSize, trackId, durationUs, language, isQuickTime, out, i);
            } else if (childAtomType == Atom.TYPE_TTML) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), "application/ttml+xml", -1, durationUs, language);
            } else if (childAtomType == Atom.TYPE_tx3g) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), "application/x-quicktime-tx3g", -1, durationUs, language);
            } else if (childAtomType == Atom.TYPE_wvtt) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), "application/x-mp4vtt", -1, durationUs, language);
            } else if (childAtomType == Atom.TYPE_stpp) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), "application/ttml+xml", -1, durationUs, language, 0L);
            }
            stsd.setPosition(childStartPosition + childAtomSize);
        }
        return out;
    }

    private static void parseVideoSampleEntry(ParsableByteArray parent, int position, int size, int trackId, long durationUs, int rotationDegrees, StsdData out, int entryIndex) {
        parent.setPosition(position + 8);
        parent.skipBytes(24);
        int width = parent.readUnsignedShort();
        int height = parent.readUnsignedShort();
        boolean pixelWidthHeightRatioFromPasp = false;
        float pixelWidthHeightRatio = 1.0f;
        parent.skipBytes(50);
        List<byte[]> initializationData = null;
        int childPosition = parent.getPosition();
        String mimeType = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childStartPosition = parent.getPosition();
            int childAtomSize = parent.readInt();
            if (childAtomSize == 0 && parent.getPosition() - position == size) {
                break;
            }
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_avcC) {
                Assertions.checkState(mimeType == null);
                mimeType = "video/avc";
                AvcCData avcCData = parseAvcCFromParent(parent, childStartPosition);
                initializationData = avcCData.initializationData;
                out.nalUnitLengthFieldLength = avcCData.nalUnitLengthFieldLength;
                if (!pixelWidthHeightRatioFromPasp) {
                    pixelWidthHeightRatio = avcCData.pixelWidthAspectRatio;
                }
            } else if (childAtomType == Atom.TYPE_hvcC) {
                Assertions.checkState(mimeType == null);
                mimeType = "video/hevc";
                Pair<List<byte[]>, Integer> hvcCData = parseHvcCFromParent(parent, childStartPosition);
                initializationData = (List) hvcCData.first;
                out.nalUnitLengthFieldLength = ((Integer) hvcCData.second).intValue();
            } else if (childAtomType == Atom.TYPE_d263) {
                Assertions.checkState(mimeType == null);
                mimeType = "video/3gpp";
            } else if (childAtomType == Atom.TYPE_esds) {
                Assertions.checkState(mimeType == null);
                Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parent, childStartPosition);
                mimeType = (String) mimeTypeAndInitializationData.first;
                initializationData = Collections.singletonList(mimeTypeAndInitializationData.second);
            } else if (childAtomType == Atom.TYPE_sinf) {
                out.trackEncryptionBoxes[entryIndex] = parseSinfFromParent(parent, childStartPosition, childAtomSize);
            } else if (childAtomType == Atom.TYPE_pasp) {
                pixelWidthHeightRatio = parsePaspFromParent(parent, childStartPosition);
                pixelWidthHeightRatioFromPasp = true;
            }
            childPosition += childAtomSize;
        }
        if (mimeType != null) {
            out.mediaFormat = MediaFormat.createVideoFormat(Integer.toString(trackId), mimeType, -1, -1, durationUs, width, height, initializationData, rotationDegrees, pixelWidthHeightRatio);
        }
    }

    private static AvcCData parseAvcCFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8 + 4);
        int nalUnitLengthFieldLength = (parent.readUnsignedByte() & 3) + 1;
        if (nalUnitLengthFieldLength == 3) {
            throw new IllegalStateException();
        }
        List<byte[]> initializationData = new ArrayList<>();
        float pixelWidthAspectRatio = 1.0f;
        int numSequenceParameterSets = parent.readUnsignedByte() & 31;
        for (int j = 0; j < numSequenceParameterSets; j++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }
        int numPictureParameterSets = parent.readUnsignedByte();
        for (int j2 = 0; j2 < numPictureParameterSets; j2++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }
        if (numSequenceParameterSets > 0) {
            ParsableBitArray spsDataBitArray = new ParsableBitArray(initializationData.get(0));
            spsDataBitArray.setPosition((nalUnitLengthFieldLength + 1) * 8);
            pixelWidthAspectRatio = CodecSpecificDataUtil.parseSpsNalUnit(spsDataBitArray).pixelWidthAspectRatio;
        }
        return new AvcCData(initializationData, nalUnitLengthFieldLength, pixelWidthAspectRatio);
    }

    private static Pair<List<byte[]>, Integer> parseHvcCFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8 + 21);
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
    }

    private static Pair<long[], long[]> parseEdts(Atom.ContainerAtom edtsAtom) {
        Atom.LeafAtom elst;
        if (edtsAtom == null || (elst = edtsAtom.getLeafAtomOfType(Atom.TYPE_elst)) == null) {
            return Pair.create(null, null);
        }
        ParsableByteArray elstData = elst.data;
        elstData.setPosition(8);
        int fullAtom = elstData.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        int entryCount = elstData.readUnsignedIntToInt();
        long[] editListDurations = new long[entryCount];
        long[] editListMediaTimes = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            editListDurations[i] = version == 1 ? elstData.readUnsignedLongToLong() : elstData.readUnsignedInt();
            editListMediaTimes[i] = version == 1 ? elstData.readLong() : elstData.readInt();
            int mediaRateInteger = elstData.readShort();
            if (mediaRateInteger != 1) {
                throw new IllegalArgumentException("Unsupported media rate.");
            }
            elstData.skipBytes(2);
        }
        return Pair.create(editListDurations, editListMediaTimes);
    }

    private static TrackEncryptionBox parseSinfFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        TrackEncryptionBox trackEncryptionBox = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_frma) {
                parent.readInt();
            } else if (childAtomType == Atom.TYPE_schm) {
                parent.skipBytes(4);
                parent.readInt();
                parent.readInt();
            } else if (childAtomType == Atom.TYPE_schi) {
                trackEncryptionBox = parseSchiFromParent(parent, childPosition, childAtomSize);
            }
            childPosition += childAtomSize;
        }
        return trackEncryptionBox;
    }

    private static float parsePaspFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8);
        int hSpacing = parent.readUnsignedIntToInt();
        int vSpacing = parent.readUnsignedIntToInt();
        return hSpacing / vSpacing;
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_tenc) {
                parent.skipBytes(4);
                int firstInt = parent.readInt();
                boolean defaultIsEncrypted = (firstInt >> 8) == 1;
                int defaultInitVectorSize = firstInt & 255;
                byte[] defaultKeyId = new byte[16];
                parent.readBytes(defaultKeyId, 0, defaultKeyId.length);
                return new TrackEncryptionBox(defaultIsEncrypted, defaultInitVectorSize, defaultKeyId);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static void parseAudioSampleEntry(ParsableByteArray parent, int atomType, int position, int size, int trackId, long durationUs, String language, boolean isQuickTime, StsdData out, int entryIndex) {
        parent.setPosition(position + 8);
        int quickTimeSoundDescriptionVersion = 0;
        if (isQuickTime) {
            parent.skipBytes(8);
            quickTimeSoundDescriptionVersion = parent.readUnsignedShort();
            parent.skipBytes(6);
        } else {
            parent.skipBytes(16);
        }
        int channelCount = parent.readUnsignedShort();
        int sampleSize = parent.readUnsignedShort();
        parent.skipBytes(4);
        int sampleRate = parent.readUnsignedFixedPoint1616();
        if (quickTimeSoundDescriptionVersion > 0) {
            parent.skipBytes(16);
            if (quickTimeSoundDescriptionVersion == 2) {
                parent.skipBytes(20);
            }
        }
        String mimeType = null;
        if (atomType == Atom.TYPE_ac_3) {
            mimeType = "audio/ac3";
        } else if (atomType == Atom.TYPE_ec_3) {
            mimeType = "audio/eac3";
        } else if (atomType == Atom.TYPE_dtsc) {
            mimeType = "audio/vnd.dts";
        } else if (atomType == Atom.TYPE_dtsh || atomType == Atom.TYPE_dtsl) {
            mimeType = "audio/vnd.dts.hd";
        } else if (atomType == Atom.TYPE_dtse) {
            mimeType = "audio/vnd.dts.hd;profile=lbr";
        } else if (atomType == Atom.TYPE_samr) {
            mimeType = "audio/3gpp";
        } else if (atomType == Atom.TYPE_sawb) {
            mimeType = "audio/amr-wb";
        }
        byte[] initializationData = null;
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (atomType == Atom.TYPE_mp4a || atomType == Atom.TYPE_enca) {
                int esdsAtomPosition = -1;
                if (childAtomType == Atom.TYPE_esds) {
                    esdsAtomPosition = childAtomPosition;
                } else if (isQuickTime && childAtomType == Atom.TYPE_wave) {
                    esdsAtomPosition = findEsdsPosition(parent, childAtomPosition, childAtomSize);
                }
                if (esdsAtomPosition != -1) {
                    Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parent, esdsAtomPosition);
                    mimeType = (String) mimeTypeAndInitializationData.first;
                    initializationData = (byte[]) mimeTypeAndInitializationData.second;
                    if ("audio/mp4a-latm".equals(mimeType)) {
                        Pair<Integer, Integer> audioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData);
                        sampleRate = ((Integer) audioSpecificConfig.first).intValue();
                        channelCount = ((Integer) audioSpecificConfig.second).intValue();
                    }
                } else if (childAtomType == Atom.TYPE_sinf) {
                    out.trackEncryptionBoxes[entryIndex] = parseSinfFromParent(parent, childAtomPosition, childAtomSize);
                }
            } else {
                if (atomType == Atom.TYPE_ac_3 && childAtomType == Atom.TYPE_dac3) {
                    parent.setPosition(childAtomPosition + 8);
                    out.mediaFormat = Ac3Util.parseAc3AnnexFFormat(parent, Integer.toString(trackId), durationUs, language);
                    return;
                }
                if (atomType == Atom.TYPE_ec_3 && childAtomType == Atom.TYPE_dec3) {
                    parent.setPosition(childAtomPosition + 8);
                    out.mediaFormat = Ac3Util.parseEAc3AnnexFFormat(parent, Integer.toString(trackId), durationUs, language);
                    return;
                } else if ((atomType == Atom.TYPE_dtsc || atomType == Atom.TYPE_dtse || atomType == Atom.TYPE_dtsh || atomType == Atom.TYPE_dtsl) && childAtomType == Atom.TYPE_ddts) {
                    out.mediaFormat = MediaFormat.createAudioFormat(Integer.toString(trackId), mimeType, -1, -1, durationUs, channelCount, sampleRate, null, language);
                    return;
                }
            }
            childAtomPosition += childAtomSize;
        }
        if (mimeType != null) {
            out.mediaFormat = MediaFormat.createAudioFormat(Integer.toString(trackId), mimeType, -1, sampleSize, durationUs, channelCount, sampleRate, initializationData == null ? null : Collections.singletonList(initializationData), language);
        }
    }

    private static int findEsdsPosition(ParsableByteArray parent, int position, int size) {
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childType = parent.readInt();
            if (childType != Atom.TYPE_esds) {
                childAtomPosition += childAtomSize;
            } else {
                return childAtomPosition;
            }
        }
        return -1;
    }

    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parent, int position) {
        String mimeType;
        parent.setPosition(position + 8 + 4);
        parent.skipBytes(1);
        int varIntByte = parent.readUnsignedByte();
        while (varIntByte > 127) {
            varIntByte = parent.readUnsignedByte();
        }
        parent.skipBytes(2);
        int flags = parent.readUnsignedByte();
        if ((flags & 128) != 0) {
            parent.skipBytes(2);
        }
        if ((flags & 64) != 0) {
            parent.skipBytes(parent.readUnsignedShort());
        }
        if ((flags & 32) != 0) {
            parent.skipBytes(2);
        }
        parent.skipBytes(1);
        int varIntByte2 = parent.readUnsignedByte();
        while (varIntByte2 > 127) {
            varIntByte2 = parent.readUnsignedByte();
        }
        int objectTypeIndication = parent.readUnsignedByte();
        switch (objectTypeIndication) {
            case 32:
                mimeType = "video/mp4v-es";
                break;
            case 33:
                mimeType = "video/avc";
                break;
            case 35:
                mimeType = "video/hevc";
                break;
            case 64:
            case 102:
            case 103:
            case 104:
                mimeType = "audio/mp4a-latm";
                break;
            case 107:
                return Pair.create("audio/mpeg", null);
            case avcodec.AV_CODEC_ID_TSCC2 /* 165 */:
                mimeType = "audio/ac3";
                break;
            case avcodec.AV_CODEC_ID_MTS2 /* 166 */:
                mimeType = "audio/eac3";
                break;
            case avcodec.AV_CODEC_ID_VP9 /* 169 */:
            case avcodec.AV_CODEC_ID_G2M_DEPRECATED /* 172 */:
                return Pair.create("audio/vnd.dts", null);
            case avcodec.AV_CODEC_ID_AIC /* 170 */:
            case avcodec.AV_CODEC_ID_ESCAPE130_DEPRECATED /* 171 */:
                return Pair.create("audio/vnd.dts.hd", null);
            default:
                mimeType = null;
                break;
        }
        parent.skipBytes(12);
        parent.skipBytes(1);
        int varIntByte3 = parent.readUnsignedByte();
        int varInt = varIntByte3 & 127;
        while (varIntByte3 > 127) {
            varIntByte3 = parent.readUnsignedByte();
            varInt = (varInt << 8) | (varIntByte3 & 127);
        }
        byte[] initializationData = new byte[varInt];
        parent.readBytes(initializationData, 0, varInt);
        return Pair.create(mimeType, initializationData);
    }

    private static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int id, long duration, int rotationDegrees) {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }
    }

    private static final class StsdData {
        public MediaFormat mediaFormat;
        public int nalUnitLengthFieldLength = -1;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int numberOfEntries) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[numberOfEntries];
        }
    }

    private static final class AvcCData {
        public final List<byte[]> initializationData;
        public final int nalUnitLengthFieldLength;
        public final float pixelWidthAspectRatio;

        public AvcCData(List<byte[]> initializationData, int nalUnitLengthFieldLength, float pixelWidthAspectRatio) {
            this.initializationData = initializationData;
            this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }
    }
}
