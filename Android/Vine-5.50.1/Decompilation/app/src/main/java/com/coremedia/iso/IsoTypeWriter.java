package com.coremedia.iso;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import com.googlecode.javacv.cpp.avutil;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public final class IsoTypeWriter {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IsoTypeWriter.class.desiredAssertionStatus();
    }

    public static void writeUInt64(ByteBuffer bb, long u) {
        if (!$assertionsDisabled && u < 0) {
            throw new AssertionError("The given long is negative");
        }
        bb.putLong(u);
    }

    public static void writeUInt32(ByteBuffer bb, long u) {
        if (!$assertionsDisabled && (u < 0 || u > avutil.AV_CH_WIDE_RIGHT)) {
            throw new AssertionError("The given long is not in the range of uint32 (" + u + ")");
        }
        bb.putInt((int) u);
    }

    public static void writeUInt24(ByteBuffer bb, int i) {
        int i2 = i & ViewCompat.MEASURED_SIZE_MASK;
        writeUInt16(bb, i2 >> 8);
        writeUInt8(bb, i2);
    }

    public static void writeUInt48(ByteBuffer bb, long l) {
        long l2 = l & 281474976710655L;
        writeUInt16(bb, (int) (l2 >> 32));
        writeUInt32(bb, 4294967295L & l2);
    }

    public static void writeUInt16(ByteBuffer bb, int i) {
        int i2 = i & SupportMenu.USER_MASK;
        writeUInt8(bb, i2 >> 8);
        writeUInt8(bb, i2 & 255);
    }

    public static void writeUInt8(ByteBuffer bb, int i) {
        bb.put((byte) (i & 255));
    }

    public static void writeFixedPoint1616(ByteBuffer bb, double v) {
        int result = (int) (65536.0d * v);
        bb.put((byte) (((-16777216) & result) >> 24));
        bb.put((byte) ((16711680 & result) >> 16));
        bb.put((byte) ((65280 & result) >> 8));
        bb.put((byte) (result & 255));
    }

    public static void writeFixedPoint0230(ByteBuffer bb, double v) {
        int result = (int) (1.073741824E9d * v);
        bb.put((byte) (((-16777216) & result) >> 24));
        bb.put((byte) ((16711680 & result) >> 16));
        bb.put((byte) ((65280 & result) >> 8));
        bb.put((byte) (result & 255));
    }

    public static void writeFixedPoint88(ByteBuffer bb, double v) {
        short result = (short) (256.0d * v);
        bb.put((byte) ((65280 & result) >> 8));
        bb.put((byte) (result & 255));
    }

    public static void writeIso639(ByteBuffer bb, String language) {
        if (language.getBytes().length != 3) {
            throw new IllegalArgumentException("\"" + language + "\" language string isn't exactly 3 characters long!");
        }
        int bits = 0;
        for (int i = 0; i < 3; i++) {
            bits += (language.getBytes()[i] - 96) << ((2 - i) * 5);
        }
        writeUInt16(bb, bits);
    }

    public static void writeZeroTermUtf8String(ByteBuffer bb, String string) {
        byte[] b = Utf8.convert(string);
        bb.put(b);
        writeUInt8(bb, 0);
    }

    public static void writeUtf8String(ByteBuffer bb, String string) {
        bb.put(Utf8.convert(string));
        writeUInt8(bb, 0);
    }
}
