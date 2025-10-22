package com.googlecode.mp4parser.h264.read;

import com.googlecode.mp4parser.h264.CharCache;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class BitstreamReader {
    protected static int bitsRead;
    private int curByte;
    protected CharCache debugBits = new CharCache(50);
    private InputStream is;
    int nBit;
    private int nextByte;

    public BitstreamReader(InputStream is) throws IOException {
        this.is = is;
        this.curByte = is.read();
        this.nextByte = is.read();
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int read1Bit() throws java.io.IOException {
        /*
            r3 = this;
            r0 = -1
            int r1 = r3.nBit
            r2 = 8
            if (r1 != r2) goto Lf
            r3.advance()
            int r1 = r3.curByte
            if (r1 != r0) goto Lf
        Le:
            return r0
        Lf:
            int r1 = r3.curByte
            int r2 = r3.nBit
            int r2 = 7 - r2
            int r1 = r1 >> r2
            r0 = r1 & 1
            int r1 = r3.nBit
            int r1 = r1 + 1
            r3.nBit = r1
            com.googlecode.mp4parser.h264.CharCache r2 = r3.debugBits
            if (r0 != 0) goto L2e
            r1 = 48
        L24:
            r2.append(r1)
            int r1 = com.googlecode.mp4parser.h264.read.BitstreamReader.bitsRead
            int r1 = r1 + 1
            com.googlecode.mp4parser.h264.read.BitstreamReader.bitsRead = r1
            goto Le
        L2e:
            r1 = 49
            goto L24
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.h264.read.BitstreamReader.read1Bit():int");
    }

    public long readNBit(int n) throws IOException {
        if (n > 64) {
            throw new IllegalArgumentException("Can not readByte more then 64 bit");
        }
        long val = 0;
        for (int i = 0; i < n; i++) {
            val = (val << 1) | read1Bit();
        }
        return val;
    }

    private void advance() throws IOException {
        this.curByte = this.nextByte;
        this.nextByte = this.is.read();
        this.nBit = 0;
    }

    public boolean moreRBSPData() throws IOException {
        if (this.nBit == 8) {
            advance();
        }
        int tail = 1 << ((8 - this.nBit) - 1);
        int mask = (tail << 1) - 1;
        boolean hasTail = (this.curByte & mask) == tail;
        return (this.curByte == -1 || (this.nextByte == -1 && hasTail)) ? false : true;
    }

    public long readRemainingByte() throws IOException {
        return readNBit(8 - this.nBit);
    }
}
