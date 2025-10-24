package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.List;

/* loaded from: classes2.dex */
public class DefaultMp4SampleList extends AbstractList<Sample> {
    ByteBuffer[] cache;
    int[] chunkNumsStartSampleNum;
    long[] chunkOffsets;
    int[] chunkSizes;
    int lastChunk = 0;
    SampleSizeBox ssb;
    Container topLevel;
    TrackBox trackBox;

    public DefaultMp4SampleList(long track, Container topLevel) {
        int s2cIndex;
        int currentChunkNo;
        this.trackBox = null;
        this.cache = null;
        this.topLevel = topLevel;
        MovieBox movieBox = (MovieBox) topLevel.getBoxes(MovieBox.class).get(0);
        List<TrackBox> trackBoxes = movieBox.getBoxes(TrackBox.class);
        for (TrackBox tb : trackBoxes) {
            if (tb.getTrackHeaderBox().getTrackId() == track) {
                this.trackBox = tb;
            }
        }
        if (this.trackBox == null) {
            throw new RuntimeException("This MP4 does not contain track " + track);
        }
        this.chunkOffsets = this.trackBox.getSampleTableBox().getChunkOffsetBox().getChunkOffsets();
        this.chunkSizes = new int[this.chunkOffsets.length];
        this.cache = new ByteBuffer[this.chunkOffsets.length];
        this.ssb = this.trackBox.getSampleTableBox().getSampleSizeBox();
        List<SampleToChunkBox.Entry> s2chunkEntries = this.trackBox.getSampleTableBox().getSampleToChunkBox().getEntries();
        SampleToChunkBox.Entry[] entries = (SampleToChunkBox.Entry[]) s2chunkEntries.toArray(new SampleToChunkBox.Entry[s2chunkEntries.size()]);
        int s2cIndex2 = 0 + 1;
        SampleToChunkBox.Entry next = entries[0];
        int currentChunkNo2 = 0;
        int currentSamplePerChunk = 0;
        long nextFirstChunk = next.getFirstChunk();
        int nextSamplePerChunk = CastUtils.l2i(next.getSamplesPerChunk());
        int currentSampleNo = 1;
        int lastSampleNo = size();
        while (true) {
            currentChunkNo2++;
            if (currentChunkNo2 == nextFirstChunk) {
                currentSamplePerChunk = nextSamplePerChunk;
                if (entries.length > s2cIndex2) {
                    s2cIndex = s2cIndex2 + 1;
                    SampleToChunkBox.Entry next2 = entries[s2cIndex2];
                    nextSamplePerChunk = CastUtils.l2i(next2.getSamplesPerChunk());
                    nextFirstChunk = next2.getFirstChunk();
                } else {
                    nextSamplePerChunk = -1;
                    nextFirstChunk = Long.MAX_VALUE;
                    s2cIndex = s2cIndex2;
                }
            } else {
                s2cIndex = s2cIndex2;
            }
            currentSampleNo += currentSamplePerChunk;
            if (currentSampleNo > lastSampleNo) {
                break;
            } else {
                s2cIndex2 = s2cIndex;
            }
        }
        this.chunkNumsStartSampleNum = new int[currentChunkNo2 + 1];
        int s2cIndex3 = 0 + 1;
        SampleToChunkBox.Entry next3 = entries[0];
        int currentChunkNo3 = 0;
        int currentSamplePerChunk2 = 0;
        long nextFirstChunk2 = next3.getFirstChunk();
        int nextSamplePerChunk2 = CastUtils.l2i(next3.getSamplesPerChunk());
        int currentSampleNo2 = 1;
        int s2cIndex4 = s2cIndex3;
        while (true) {
            currentChunkNo = currentChunkNo3 + 1;
            this.chunkNumsStartSampleNum[currentChunkNo3] = currentSampleNo2;
            if (currentChunkNo == nextFirstChunk2) {
                currentSamplePerChunk2 = nextSamplePerChunk2;
                if (entries.length > s2cIndex4) {
                    SampleToChunkBox.Entry next4 = entries[s2cIndex4];
                    nextSamplePerChunk2 = CastUtils.l2i(next4.getSamplesPerChunk());
                    nextFirstChunk2 = next4.getFirstChunk();
                    s2cIndex4++;
                } else {
                    nextSamplePerChunk2 = -1;
                    nextFirstChunk2 = Long.MAX_VALUE;
                }
            }
            currentSampleNo2 += currentSamplePerChunk2;
            if (currentSampleNo2 > lastSampleNo) {
                break;
            } else {
                currentChunkNo3 = currentChunkNo;
            }
        }
        this.chunkNumsStartSampleNum[currentChunkNo] = Integer.MAX_VALUE;
        int currentChunkNo4 = 0;
        for (int i = 1; i <= this.ssb.getSampleCount(); i++) {
            if (i == this.chunkNumsStartSampleNum[currentChunkNo4]) {
                currentChunkNo4++;
            }
            this.chunkSizes[currentChunkNo4 - 1] = (int) (r0[r22] + this.ssb.getSampleSizeAtIndex(i - 1));
        }
    }

    synchronized int getChunkForSample(int index) {
        int i;
        int sampleNum = index + 1;
        if (sampleNum >= this.chunkNumsStartSampleNum[this.lastChunk] && sampleNum < this.chunkNumsStartSampleNum[this.lastChunk + 1]) {
            i = this.lastChunk;
        } else if (sampleNum < this.chunkNumsStartSampleNum[this.lastChunk]) {
            this.lastChunk = 0;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            i = this.lastChunk;
        } else {
            this.lastChunk++;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            i = this.lastChunk;
        }
        return i;
    }

    @Override // java.util.AbstractList, java.util.List
    public Sample get(int index) {
        if (index >= this.ssb.getSampleCount()) {
            throw new IndexOutOfBoundsException();
        }
        int currentChunkNoZeroBased = getChunkForSample(index);
        int currentSampleNo = this.chunkNumsStartSampleNum[currentChunkNoZeroBased];
        long offset = this.chunkOffsets[CastUtils.l2i(currentChunkNoZeroBased)];
        ByteBuffer chunk = this.cache[CastUtils.l2i(currentChunkNoZeroBased)];
        if (chunk == null) {
            try {
                chunk = this.topLevel.getByteBuffer(offset, this.chunkSizes[CastUtils.l2i(currentChunkNoZeroBased)]);
                this.cache[CastUtils.l2i(currentChunkNoZeroBased)] = chunk;
            } catch (IOException e) {
                throw new IndexOutOfBoundsException(e.getMessage());
            }
        }
        int offsetWithinChunk = 0;
        while (true) {
            int currentSampleNo2 = currentSampleNo;
            if (currentSampleNo2 < index + 1) {
                currentSampleNo = currentSampleNo2 + 1;
                offsetWithinChunk = (int) (offsetWithinChunk + this.ssb.getSampleSizeAtIndex(currentSampleNo2 - 1));
            } else {
                final long sampleSize = this.ssb.getSampleSizeAtIndex(currentSampleNo2 - 1);
                final ByteBuffer finalChunk = chunk;
                final int finalOffsetWithinChunk = offsetWithinChunk;
                return new Sample() { // from class: com.googlecode.mp4parser.authoring.samples.DefaultMp4SampleList.1
                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public void writeTo(WritableByteChannel channel) throws IOException {
                        channel.write(asByteBuffer());
                    }

                    @Override // com.googlecode.mp4parser.authoring.Sample
                    public long getSize() {
                        return sampleSize;
                    }

                    public ByteBuffer asByteBuffer() {
                        return (ByteBuffer) ((ByteBuffer) finalChunk.position(finalOffsetWithinChunk)).slice().limit(CastUtils.l2i(sampleSize));
                    }

                    public String toString() {
                        return "DefaultMp4Sample(size:" + sampleSize + ")";
                    }
                };
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return CastUtils.l2i(this.trackBox.getSampleTableBox().getSampleSizeBox().getSampleCount());
    }
}
