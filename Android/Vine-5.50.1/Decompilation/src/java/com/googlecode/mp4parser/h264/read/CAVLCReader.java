package com.googlecode.mp4parser.h264.read;

import com.googlecode.mp4parser.h264.Debug;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class CAVLCReader extends BitstreamReader {
    public CAVLCReader(InputStream is) throws IOException {
        super(is);
    }

    public long readNBit(int n, String message) throws IOException {
        long val = readNBit(n);
        trace(message, String.valueOf(val));
        return val;
    }

    private int readUE() throws IOException {
        int cnt = 0;
        while (read1Bit() == 0) {
            cnt++;
        }
        if (cnt <= 0) {
            return 0;
        }
        long val = readNBit(cnt);
        int res = (int) (((1 << cnt) - 1) + val);
        return res;
    }

    public int readUE(String message) throws IOException {
        int res = readUE();
        trace(message, String.valueOf(res));
        return res;
    }

    public int readSE(String message) throws IOException {
        int val = readUE();
        int sign = ((val & 1) << 1) - 1;
        int val2 = ((val >> 1) + (val & 1)) * sign;
        trace(message, String.valueOf(val2));
        return val2;
    }

    public boolean readBool(String message) throws IOException {
        boolean res = read1Bit() != 0;
        trace(message, res ? "1" : "0");
        return res;
    }

    public int readU(int i, String string) throws IOException {
        return (int) readNBit(i, string);
    }

    public void readTrailingBits() throws IOException {
        read1Bit();
        readRemainingByte();
    }

    private void trace(String message, String val) {
        StringBuilder traceBuilder = new StringBuilder();
        String pos = String.valueOf(BitstreamReader.bitsRead - this.debugBits.length());
        int spaces = 8 - pos.length();
        traceBuilder.append("@" + pos);
        for (int i = 0; i < spaces; i++) {
            traceBuilder.append(' ');
        }
        traceBuilder.append(message);
        int spaces2 = (100 - traceBuilder.length()) - this.debugBits.length();
        for (int i2 = 0; i2 < spaces2; i2++) {
            traceBuilder.append(' ');
        }
        traceBuilder.append(this.debugBits);
        traceBuilder.append(" (" + val + ")");
        this.debugBits.clear();
        Debug.println(traceBuilder.toString());
    }
}
