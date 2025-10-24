package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class TemporalLevelEntry extends GroupEntry {
    private boolean levelIndependentlyDecodable;
    private short reserved;

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public String getType() {
        return "tele";
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public void parse(ByteBuffer byteBuffer) {
        byte b = byteBuffer.get();
        this.levelIndependentlyDecodable = (b & 128) == 128;
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry
    public ByteBuffer get() {
        ByteBuffer content = ByteBuffer.allocate(1);
        content.put((byte) (this.levelIndependentlyDecodable ? 128 : 0));
        content.rewind();
        return content;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemporalLevelEntry that = (TemporalLevelEntry) o;
        return this.levelIndependentlyDecodable == that.levelIndependentlyDecodable && this.reserved == that.reserved;
    }

    public int hashCode() {
        int result = this.levelIndependentlyDecodable ? 1 : 0;
        return (result * 31) + this.reserved;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TemporalLevelEntry");
        sb.append("{levelIndependentlyDecodable=").append(this.levelIndependentlyDecodable);
        sb.append('}');
        return sb.toString();
    }
}
