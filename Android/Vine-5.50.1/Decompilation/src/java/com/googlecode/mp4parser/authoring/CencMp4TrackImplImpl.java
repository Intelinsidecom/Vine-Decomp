package com.googlecode.mp4parser.authoring;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.googlecode.mp4parser.authoring.tracks.CencEncyprtedTrack;
import com.googlecode.mp4parser.util.Path;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationOffsetsBox;
import com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import com.mp4parser.iso23001.part7.TrackEncryptionBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* loaded from: classes2.dex */
public class CencMp4TrackImplImpl extends Mp4TrackImpl implements CencEncyprtedTrack {
    static final /* synthetic */ boolean $assertionsDisabled;
    private UUID defaultKeyId;
    private List<CencSampleAuxiliaryDataFormat> sampleEncryptionEntries;

    static {
        $assertionsDisabled = !CencMp4TrackImplImpl.class.desiredAssertionStatus();
    }

    public CencMp4TrackImplImpl(String name, TrackBox trackBox, IsoFile... fragments) throws IOException {
        Container base;
        long baseOffset;
        super(name, trackBox, fragments);
        SchemeTypeBox schm = (SchemeTypeBox) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schm[0]");
        if (!$assertionsDisabled && (schm == null || (!schm.getSchemeType().equals("cenc") && !schm.getSchemeType().equals("cbc1")))) {
            throw new AssertionError("Track must be CENC (cenc or cbc1) encrypted");
        }
        this.sampleEncryptionEntries = new ArrayList();
        long trackId = trackBox.getTrackHeaderBox().getTrackId();
        if (trackBox.getParent().getBoxes(MovieExtendsBox.class).size() > 0) {
            for (MovieFragmentBox movieFragmentBox : ((Box) trackBox.getParent()).getParent().getBoxes(MovieFragmentBox.class)) {
                List<TrackFragmentBox> trafs = movieFragmentBox.getBoxes(TrackFragmentBox.class);
                for (TrackFragmentBox traf : trafs) {
                    if (traf.getTrackFragmentHeaderBox().getTrackId() == trackId) {
                        TrackEncryptionBox tenc = (TrackEncryptionBox) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schi[0]/tenc[0]");
                        this.defaultKeyId = tenc.getDefault_KID();
                        if (traf.getTrackFragmentHeaderBox().hasBaseDataOffset()) {
                            base = ((Box) trackBox.getParent()).getParent();
                            baseOffset = traf.getTrackFragmentHeaderBox().getBaseDataOffset();
                        } else {
                            base = movieFragmentBox;
                            baseOffset = 0;
                        }
                        FindSaioSaizPair saizSaioPair = new FindSaioSaizPair(traf).invoke();
                        SampleAuxiliaryInformationOffsetsBox saio = saizSaioPair.getSaio();
                        SampleAuxiliaryInformationSizesBox saiz = saizSaioPair.getSaiz();
                        if (!$assertionsDisabled && saio == null) {
                            throw new AssertionError();
                        }
                        long[] saioOffsets = saio.getOffsets();
                        if (!$assertionsDisabled && saioOffsets.length != traf.getBoxes(TrackRunBox.class).size()) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && saiz == null) {
                            throw new AssertionError();
                        }
                        List<TrackRunBox> truns = traf.getBoxes(TrackRunBox.class);
                        int sampleNo = 0;
                        for (int i = 0; i < saioOffsets.length; i++) {
                            int numSamples = truns.get(i).getEntries().size();
                            long offset = saioOffsets[i];
                            long length = 0;
                            for (int j = sampleNo; j < sampleNo + numSamples; j++) {
                                length += saiz.getSize(j);
                            }
                            ByteBuffer trunsCencSampleAuxData = base.getByteBuffer(baseOffset + offset, length);
                            for (int j2 = sampleNo; j2 < sampleNo + numSamples; j2++) {
                                int auxInfoSize = saiz.getSize(j2);
                                this.sampleEncryptionEntries.add(parseCencAuxDataFormat(tenc.getDefaultIvSize(), trunsCencSampleAuxData, auxInfoSize));
                            }
                            sampleNo += numSamples;
                        }
                    }
                }
            }
            return;
        }
        TrackEncryptionBox tenc2 = (TrackEncryptionBox) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stsd[0]/enc.[0]/sinf[0]/schi[0]/tenc[0]");
        this.defaultKeyId = tenc2.getDefault_KID();
        ChunkOffsetBox chunkOffsetBox = (ChunkOffsetBox) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/stco[0]");
        long[] chunkSizes = trackBox.getSampleTableBox().getSampleToChunkBox().blowup((chunkOffsetBox == null ? (ChunkOffsetBox) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]/co64[0]") : chunkOffsetBox).getChunkOffsets().length);
        FindSaioSaizPair saizSaioPair2 = new FindSaioSaizPair((Container) Path.getPath(trackBox, "mdia[0]/minf[0]/stbl[0]")).invoke();
        SampleAuxiliaryInformationOffsetsBox saio2 = saizSaioPair2.saio;
        SampleAuxiliaryInformationSizesBox saiz2 = saizSaioPair2.saiz;
        Container topLevel = ((MovieBox) trackBox.getParent()).getParent();
        if (saio2.getOffsets().length == 1) {
            long offset2 = saio2.getOffsets()[0];
            int sizeInTotal = 0;
            if (saiz2.getDefaultSampleInfoSize() > 0) {
                sizeInTotal = 0 + (saiz2.getSampleCount() * saiz2.getDefaultSampleInfoSize());
            } else {
                for (int i2 = 0; i2 < saiz2.getSampleCount(); i2++) {
                    sizeInTotal += saiz2.getSampleInfoSizes()[i2];
                }
            }
            ByteBuffer chunksCencSampleAuxData = topLevel.getByteBuffer(offset2, sizeInTotal);
            for (int i3 = 0; i3 < saiz2.getSampleCount(); i3++) {
                this.sampleEncryptionEntries.add(parseCencAuxDataFormat(tenc2.getDefaultIvSize(), chunksCencSampleAuxData, saiz2.getSize(i3)));
            }
            return;
        }
        if (saio2.getOffsets().length == chunkSizes.length) {
            int currentSampleNo = 0;
            for (int i4 = 0; i4 < chunkSizes.length; i4++) {
                long offset3 = saio2.getOffsets()[i4];
                long size = 0;
                if (saiz2.getDefaultSampleInfoSize() > 0) {
                    size = 0 + (saiz2.getSampleCount() * chunkSizes[i4]);
                } else {
                    for (int j3 = 0; j3 < chunkSizes[i4]; j3++) {
                        size += saiz2.getSize(currentSampleNo + j3);
                    }
                }
                ByteBuffer chunksCencSampleAuxData2 = topLevel.getByteBuffer(offset3, size);
                for (int j4 = 0; j4 < chunkSizes[i4]; j4++) {
                    long auxInfoSize2 = saiz2.getSize(currentSampleNo + j4);
                    this.sampleEncryptionEntries.add(parseCencAuxDataFormat(tenc2.getDefaultIvSize(), chunksCencSampleAuxData2, auxInfoSize2));
                }
                currentSampleNo = (int) (currentSampleNo + chunkSizes[i4]);
            }
            return;
        }
        throw new RuntimeException("Number of saio offsets must be either 1 or number of chunks");
    }

    private CencSampleAuxiliaryDataFormat parseCencAuxDataFormat(int ivSize, ByteBuffer chunksCencSampleAuxData, long auxInfoSize) {
        CencSampleAuxiliaryDataFormat cadf = new CencSampleAuxiliaryDataFormat();
        if (auxInfoSize > 0) {
            cadf.iv = new byte[ivSize];
            chunksCencSampleAuxData.get(cadf.iv);
            if (auxInfoSize > ivSize) {
                int numOfPairs = IsoTypeReader.readUInt16(chunksCencSampleAuxData);
                cadf.pairs = new CencSampleAuxiliaryDataFormat.Pair[numOfPairs];
                for (int i = 0; i < cadf.pairs.length; i++) {
                    cadf.pairs[i] = cadf.createPair(IsoTypeReader.readUInt16(chunksCencSampleAuxData), IsoTypeReader.readUInt32(chunksCencSampleAuxData));
                }
            }
        }
        return cadf;
    }

    @Override // com.googlecode.mp4parser.authoring.tracks.CencEncyprtedTrack
    public boolean hasSubSampleEncryption() {
        return false;
    }

    @Override // com.googlecode.mp4parser.authoring.tracks.CencEncyprtedTrack
    public List<CencSampleAuxiliaryDataFormat> getSampleEncryptionEntries() {
        return this.sampleEncryptionEntries;
    }

    public String toString() {
        return "CencMp4TrackImpl{handler='" + getHandler() + "'}";
    }

    private class FindSaioSaizPair {
        static final /* synthetic */ boolean $assertionsDisabled;
        private Container container;
        private SampleAuxiliaryInformationOffsetsBox saio;
        private SampleAuxiliaryInformationSizesBox saiz;

        static {
            $assertionsDisabled = !CencMp4TrackImplImpl.class.desiredAssertionStatus();
        }

        public FindSaioSaizPair(Container container) {
            this.container = container;
        }

        public SampleAuxiliaryInformationSizesBox getSaiz() {
            return this.saiz;
        }

        public SampleAuxiliaryInformationOffsetsBox getSaio() {
            return this.saio;
        }

        public FindSaioSaizPair invoke() {
            List<SampleAuxiliaryInformationSizesBox> saizs = this.container.getBoxes(SampleAuxiliaryInformationSizesBox.class);
            List<SampleAuxiliaryInformationOffsetsBox> saios = this.container.getBoxes(SampleAuxiliaryInformationOffsetsBox.class);
            if (!$assertionsDisabled && saizs.size() != saios.size()) {
                throw new AssertionError();
            }
            this.saiz = null;
            this.saio = null;
            for (int i = 0; i < saizs.size(); i++) {
                if ((this.saiz == null && saizs.get(i).getAuxInfoType() == null) || "cenc".equals(saizs.get(i).getAuxInfoType())) {
                    this.saiz = saizs.get(i);
                } else if (this.saiz != null && this.saiz.getAuxInfoType() == null && "cenc".equals(saizs.get(i).getAuxInfoType())) {
                    this.saiz = saizs.get(i);
                } else {
                    throw new RuntimeException("Are there two cenc labeled saiz?");
                }
                if ((this.saio == null && saios.get(i).getAuxInfoType() == null) || "cenc".equals(saios.get(i).getAuxInfoType())) {
                    this.saio = saios.get(i);
                } else if (this.saio != null && this.saio.getAuxInfoType() == null && "cenc".equals(saios.get(i).getAuxInfoType())) {
                    this.saio = saios.get(i);
                } else {
                    throw new RuntimeException("Are there two cenc labeled saio?");
                }
            }
            return this;
        }
    }
}
