package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class SyncSampleEntry extends GroupEntry {
    int nalUnitType;
    int reserved;

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public void parse(ByteBuffer byteBuffer) {
        int a = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved = (a & 192) >> 6;
        this.nalUnitType = a & 63;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public ByteBuffer get() {
        ByteBuffer b = ByteBuffer.allocate(1);
        IsoTypeWriter.writeUInt8(b, this.nalUnitType + (this.reserved << 6));
        return (ByteBuffer) b.rewind();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SyncSampleEntry that = (SyncSampleEntry) o;
        return this.nalUnitType == that.nalUnitType && this.reserved == that.reserved;
    }

    public int hashCode() {
        int result = this.reserved;
        return (result * 31) + this.nalUnitType;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public String getType() {
        return "sync";
    }

    public String toString() {
        return "SyncSampleEntry{reserved=" + this.reserved + ", nalUnitType=" + this.nalUnitType + '}';
    }
}
