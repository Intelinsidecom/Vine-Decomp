package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.Hex;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class UnknownEntry extends GroupEntry {
    private ByteBuffer content;
    private String type;

    public UnknownEntry(String type) {
        this.type = type;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public String getType() {
        return this.type;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public void parse(ByteBuffer byteBuffer) {
        this.content = (ByteBuffer) byteBuffer.duplicate().rewind();
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public ByteBuffer get() {
        return this.content.duplicate();
    }

    public String toString() {
        ByteBuffer bb = this.content.duplicate();
        bb.rewind();
        byte[] b = new byte[bb.limit()];
        bb.get(b);
        return "UnknownEntry{content=" + Hex.encodeHex(b) + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnknownEntry that = (UnknownEntry) o;
        if (this.content != null) {
            if (this.content.equals(that.content)) {
                return true;
            }
        } else if (that.content == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.content != null) {
            return this.content.hashCode();
        }
        return 0;
    }
}
