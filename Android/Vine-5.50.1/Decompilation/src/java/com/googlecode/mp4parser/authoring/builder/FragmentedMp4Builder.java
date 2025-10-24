package com.googlecode.mp4parser.authoring.builder;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.logging.Logger;

/* loaded from: classes2.dex */
public class FragmentedMp4Builder {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Logger LOG;

    static {
        $assertionsDisabled = !FragmentedMp4Builder.class.desiredAssertionStatus();
        LOG = Logger.getLogger(FragmentedMp4Builder.class.getName());
    }

    /* renamed from: com.googlecode.mp4parser.authoring.builder.FragmentedMp4Builder$1Mdat, reason: invalid class name */
    class C1Mdat implements Box {
        Container parent;
        long size_ = -1;
        private final /* synthetic */ long val$endSample;
        private final /* synthetic */ int val$i;
        private final /* synthetic */ long val$startSample;
        private final /* synthetic */ Track val$track;

        C1Mdat(long j, long j2, Track track, int i) {
            this.val$startSample = j;
            this.val$endSample = j2;
            this.val$track = track;
            this.val$i = i;
        }

        @Override // com.coremedia.iso.boxes.Box
        public Container getParent() {
            return this.parent;
        }

        @Override // com.coremedia.iso.boxes.Box
        public void setParent(Container parent) {
            this.parent = parent;
        }

        public long getOffset() {
            throw new RuntimeException("Doesn't have any meaning for programmatically created boxes");
        }

        @Override // com.coremedia.iso.boxes.Box
        public long getSize() {
            if (this.size_ != -1) {
                return this.size_;
            }
            long size = 8;
            for (Sample sample : FragmentedMp4Builder.this.getSamples(this.val$startSample, this.val$endSample, this.val$track, this.val$i)) {
                size += sample.getSize();
            }
            this.size_ = size;
            return size;
        }

        @Override // com.coremedia.iso.boxes.Box
        public String getType() {
            return MediaDataBox.TYPE;
        }

        @Override // com.coremedia.iso.boxes.Box
        public void getBox(WritableByteChannel writableByteChannel) throws IOException {
            ByteBuffer header = ByteBuffer.allocate(8);
            IsoTypeWriter.writeUInt32(header, CastUtils.l2i(getSize()));
            header.put(IsoFile.fourCCtoBytes(getType()));
            header.rewind();
            writableByteChannel.write(header);
            List<Sample> samples = FragmentedMp4Builder.this.getSamples(this.val$startSample, this.val$endSample, this.val$track, this.val$i);
            for (Sample sample : samples) {
                sample.writeTo(writableByteChannel);
            }
        }

        @Override // com.coremedia.iso.boxes.Box
        public void parse(DataSource fileChannel, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        }
    }

    protected List<Sample> getSamples(long startSample, long endSample, Track track, int sequenceNumber) {
        return track.getSamples().subList(CastUtils.l2i(startSample) - 1, CastUtils.l2i(endSample) - 1);
    }
}
