package com.google.android.exoplayer.util;

import com.flurry.android.Constants;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public final class ParsableByteArray {
    public byte[] data;
    private int limit;
    private int position;

    public ParsableByteArray() {
    }

    public ParsableByteArray(int length) {
        this.data = new byte[length];
        this.limit = this.data.length;
    }

    public ParsableByteArray(byte[] data) {
        this.data = data;
        this.limit = data.length;
    }

    public void reset(byte[] data, int limit) {
        this.data = data;
        this.limit = limit;
        this.position = 0;
    }

    public void reset() {
        this.position = 0;
        this.limit = 0;
    }

    public int bytesLeft() {
        return this.limit - this.position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        Assertions.checkArgument(limit >= 0 && limit <= this.data.length);
        this.limit = limit;
    }

    public int getPosition() {
        return this.position;
    }

    public int capacity() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    public void setPosition(int position) {
        Assertions.checkArgument(position >= 0 && position <= this.limit);
        this.position = position;
    }

    public void skipBytes(int bytes) {
        setPosition(this.position + bytes);
    }

    public void readBytes(ParsableBitArray bitArray, int length) {
        readBytes(bitArray.data, 0, length);
        bitArray.setPosition(0);
    }

    public void readBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(this.data, this.position, buffer, offset, length);
        this.position += length;
    }

    public int readUnsignedByte() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & Constants.UNKNOWN;
    }

    public int readUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & Constants.UNKNOWN) << 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & Constants.UNKNOWN);
    }

    public short readShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & Constants.UNKNOWN) << 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return (short) (i2 | (bArr2[i3] & Constants.UNKNOWN));
    }

    public int readUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & Constants.UNKNOWN) << 16;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        int i4 = i2 | ((bArr2[i3] & Constants.UNKNOWN) << 8);
        byte[] bArr3 = this.data;
        int i5 = this.position;
        this.position = i5 + 1;
        return i4 | (bArr3[i5] & Constants.UNKNOWN);
    }

    public long readUnsignedInt() {
        byte[] bArr = this.data;
        this.position = this.position + 1;
        byte[] bArr2 = this.data;
        this.position = this.position + 1;
        long j = ((bArr[r1] & 255) << 24) | ((bArr2[r3] & 255) << 16);
        byte[] bArr3 = this.data;
        this.position = this.position + 1;
        long j2 = j | ((bArr3[r3] & 255) << 8);
        byte[] bArr4 = this.data;
        this.position = this.position + 1;
        return j2 | (bArr4[r3] & 255);
    }

    public int readInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & Constants.UNKNOWN) << 24;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        int i4 = i2 | ((bArr2[i3] & Constants.UNKNOWN) << 16);
        byte[] bArr3 = this.data;
        int i5 = this.position;
        this.position = i5 + 1;
        int i6 = i4 | ((bArr3[i5] & Constants.UNKNOWN) << 8);
        byte[] bArr4 = this.data;
        int i7 = this.position;
        this.position = i7 + 1;
        return i6 | (bArr4[i7] & Constants.UNKNOWN);
    }

    public long readLong() {
        byte[] bArr = this.data;
        this.position = this.position + 1;
        byte[] bArr2 = this.data;
        this.position = this.position + 1;
        long j = ((bArr[r1] & 255) << 56) | ((bArr2[r3] & 255) << 48);
        byte[] bArr3 = this.data;
        this.position = this.position + 1;
        long j2 = j | ((bArr3[r3] & 255) << 40);
        byte[] bArr4 = this.data;
        this.position = this.position + 1;
        long j3 = j2 | ((bArr4[r3] & 255) << 32);
        byte[] bArr5 = this.data;
        this.position = this.position + 1;
        long j4 = j3 | ((bArr5[r3] & 255) << 24);
        byte[] bArr6 = this.data;
        this.position = this.position + 1;
        long j5 = j4 | ((bArr6[r3] & 255) << 16);
        byte[] bArr7 = this.data;
        this.position = this.position + 1;
        long j6 = j5 | ((bArr7[r3] & 255) << 8);
        byte[] bArr8 = this.data;
        this.position = this.position + 1;
        return j6 | (bArr8[r3] & 255);
    }

    public int readUnsignedFixedPoint1616() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & Constants.UNKNOWN) << 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        int result = i2 | (bArr2[i3] & Constants.UNKNOWN);
        this.position += 2;
        return result;
    }

    public int readSynchSafeInt() {
        int b1 = readUnsignedByte();
        int b2 = readUnsignedByte();
        int b3 = readUnsignedByte();
        int b4 = readUnsignedByte();
        return (b1 << 21) | (b2 << 14) | (b3 << 7) | b4;
    }

    public int readUnsignedIntToInt() {
        int result = readInt();
        if (result < 0) {
            throw new IllegalStateException("Top bit not zero: " + result);
        }
        return result;
    }

    public long readUnsignedLongToLong() {
        long result = readLong();
        if (result < 0) {
            throw new IllegalStateException("Top bit not zero: " + result);
        }
        return result;
    }

    public String readString(int length, Charset charset) {
        String result = new String(this.data, this.position, length, charset);
        this.position += length;
        return result;
    }

    public String readLine() {
        if (bytesLeft() == 0) {
            return null;
        }
        int lineLimit = this.position;
        while (lineLimit < this.limit && this.data[lineLimit] != 10 && this.data[lineLimit] != 13) {
            lineLimit++;
        }
        if (lineLimit - this.position >= 3 && this.data[this.position] == -17 && this.data[this.position + 1] == -69 && this.data[this.position + 2] == -65) {
            this.position += 3;
        }
        String str = new String(this.data, this.position, lineLimit - this.position);
        this.position = lineLimit;
        if (this.position != this.limit) {
            if (this.data[this.position] == 13) {
                this.position++;
                if (this.position == this.limit) {
                    return str;
                }
            }
            if (this.data[this.position] == 10) {
                this.position++;
                return str;
            }
            return str;
        }
        return str;
    }
}
