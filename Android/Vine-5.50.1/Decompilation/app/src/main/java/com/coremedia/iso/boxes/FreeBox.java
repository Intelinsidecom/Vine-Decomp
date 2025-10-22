package com.coremedia.iso.boxes;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class FreeBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TYPE = "free";
    ByteBuffer data;
    private long offset;
    private Container parent;
    List<Box> replacers;

    static {
        $assertionsDisabled = !FreeBox.class.desiredAssertionStatus();
    }

    public FreeBox() {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.wrap(new byte[0]);
    }

    public FreeBox(int size) {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.allocate(size);
    }

    public long getOffset() {
        return this.offset;
    }

    public ByteBuffer getData() {
        if (this.data != null) {
            return (ByteBuffer) this.data.duplicate().rewind();
        }
        return null;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void getBox(WritableByteChannel os) throws IOException {
        for (Box replacer : this.replacers) {
            replacer.getBox(os);
        }
        ByteBuffer header = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(header, this.data.limit() + 8);
        header.put(TYPE.getBytes());
        header.rewind();
        os.write(header);
        header.rewind();
        this.data.rewind();
        os.write(this.data);
        this.data.rewind();
    }

    @Override // com.coremedia.iso.boxes.Box
    public Container getParent() {
        return this.parent;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override // com.coremedia.iso.boxes.Box
    public long getSize() {
        long size = 8;
        for (Box replacer : this.replacers) {
            size += replacer.getSize();
        }
        return size + this.data.limit();
    }

    @Override // com.coremedia.iso.boxes.Box
    public String getType() {
        return TYPE;
    }

    @Override // com.coremedia.iso.boxes.Box
    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.offset = dataSource.position() - header.remaining();
        if (contentSize > 1048576) {
            this.data = dataSource.map(dataSource.position(), contentSize);
            dataSource.position(dataSource.position() + contentSize);
        } else {
            if (!$assertionsDisabled && contentSize >= 2147483647L) {
                throw new AssertionError();
            }
            this.data = ByteBuffer.allocate(CastUtils.l2i(contentSize));
            dataSource.read(this.data);
        }
    }

    public void addAndReplace(Box box) {
        this.data.position(CastUtils.l2i(box.getSize()));
        this.data = this.data.slice();
        this.replacers.add(box);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FreeBox freeBox = (FreeBox) o;
        if (getData() != null) {
            if (getData().equals(freeBox.getData())) {
                return true;
            }
        } else if (freeBox.getData() == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.data != null) {
            return this.data.hashCode();
        }
        return 0;
    }
}
