package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;

/* loaded from: classes.dex */
public class CompositionTimeToSample extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TYPE = "ctts";
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ JoinPoint.StaticPart ajc$tjp_1 = null;
    List<Entry> entries;

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("CompositionTimeToSample.java", CompositionTimeToSample.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "", "", "", "java.util.List"), 57);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "java.util.List", "entries", "", "void"), 61);
    }

    static {
        ajc$preClinit();
        $assertionsDisabled = !CompositionTimeToSample.class.desiredAssertionStatus();
    }

    public CompositionTimeToSample() {
        super(TYPE);
        this.entries = Collections.emptyList();
    }

    @Override // com.googlecode.mp4parser.AbstractBox
    protected long getContentSize() {
        return (this.entries.size() * 8) + 8;
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> list) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this, list));
        this.entries = list;
    }

    @Override // com.googlecode.mp4parser.AbstractBox
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int numberOfEntries = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.entries = new ArrayList(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            Entry e = new Entry(CastUtils.l2i(IsoTypeReader.readUInt32(content)), content.getInt());
            this.entries.add(e);
        }
    }

    @Override // com.googlecode.mp4parser.AbstractBox
    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getCount());
            byteBuffer.putInt(entry.getOffset());
        }
    }

    public static class Entry {
        int count;
        int offset;

        public Entry(int count, int offset) {
            this.count = count;
            this.offset = offset;
        }

        public int getCount() {
            return this.count;
        }

        public int getOffset() {
            return this.offset;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String toString() {
            return "Entry{count=" + this.count + ", offset=" + this.offset + '}';
        }
    }

    public static int[] blowupCompositionTimes(List<Entry> list) {
        long numOfSamples = 0;
        while (list.iterator().hasNext()) {
            numOfSamples += r5.next().getCount();
        }
        if (!$assertionsDisabled && numOfSamples > 2147483647L) {
            throw new AssertionError();
        }
        int[] decodingTime = new int[(int) numOfSamples];
        int current = 0;
        for (Entry entry : list) {
            int i = 0;
            while (i < entry.getCount()) {
                decodingTime[current] = entry.getOffset();
                i++;
                current++;
            }
        }
        return decodingTime;
    }
}
