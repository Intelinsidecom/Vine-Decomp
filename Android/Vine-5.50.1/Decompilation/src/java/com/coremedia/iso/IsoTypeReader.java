package com.coremedia.iso;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.flurry.android.Constants;
import com.googlecode.javacv.cpp.avutil;
import com.googlecode.mp4parser.util.IntHashMap;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public final class IsoTypeReader {
    private static IntHashMap codeCache = new IntHashMap();
    private static byte[] codeBytes = new byte[4];

    public static long readUInt32(ByteBuffer bb) {
        long i = bb.getInt();
        if (i < 0) {
            return i + avutil.AV_CH_WIDE_RIGHT;
        }
        return i;
    }

    public static int readUInt24(ByteBuffer bb) {
        int result = 0 + (readUInt16(bb) << 8);
        return result + byte2int(bb.get());
    }

    public static int readUInt16(ByteBuffer bb) {
        int result = 0 + (byte2int(bb.get()) << 8);
        return result + byte2int(bb.get());
    }

    public static int readUInt8(ByteBuffer bb) {
        return byte2int(bb.get());
    }

    public static int byte2int(byte b) {
        return b < 0 ? b + Constants.FEMALE : b;
    }

    public static String readString(ByteBuffer byteBuffer) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int read = byteBuffer.get();
            if (read != 0) {
                out.write(read);
            } else {
                return Utf8.convert(out.toByteArray());
            }
        }
    }

    public static String readString(ByteBuffer byteBuffer, int length) {
        byte[] buffer = new byte[length];
        byteBuffer.get(buffer);
        return Utf8.convert(buffer);
    }

    public static long readUInt64(ByteBuffer byteBuffer) {
        long result = 0 + (readUInt32(byteBuffer) << 32);
        if (result < 0) {
            throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
        }
        return result + readUInt32(byteBuffer);
    }

    public static double readFixedPoint1616(ByteBuffer bb) {
        byte[] bytes = new byte[4];
        bb.get(bytes);
        int result = 0 | ((bytes[0] << 24) & ViewCompat.MEASURED_STATE_MASK);
        return (((result | ((bytes[1] << 16) & 16711680)) | ((bytes[2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | (bytes[3] & Constants.UNKNOWN)) / 65536.0d;
    }

    public static double readFixedPoint0230(ByteBuffer bb) {
        byte[] bytes = new byte[4];
        bb.get(bytes);
        int result = 0 | ((bytes[0] << 24) & ViewCompat.MEASURED_STATE_MASK);
        return (((result | ((bytes[1] << 16) & 16711680)) | ((bytes[2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | (bytes[3] & Constants.UNKNOWN)) / 1.073741824E9d;
    }

    public static float readFixedPoint88(ByteBuffer bb) {
        byte[] bytes = new byte[2];
        bb.get(bytes);
        short result = (short) (((bytes[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | 0);
        return ((short) ((bytes[1] & Constants.UNKNOWN) | result)) / 256.0f;
    }

    public static String readIso639(ByteBuffer bb) {
        int bits = readUInt16(bb);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int c = (bits >> ((2 - i) * 5)) & 31;
            result.append((char) (c + 96));
        }
        return result.toString();
    }

    public static String read4cc(ByteBuffer bb) {
        String code;
        bb.get(codeBytes);
        int result = ((codeBytes[0] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((codeBytes[1] << 16) & 16711680) | ((codeBytes[2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (codeBytes[3] & Constants.UNKNOWN);
        String code2 = (String) codeCache.get(result);
        if (code2 != null) {
            return code2;
        }
        try {
            code = new String(codeBytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e = e;
        }
        try {
            codeCache.put(result, code);
            return code;
        } catch (UnsupportedEncodingException e2) {
            e = e2;
            throw new RuntimeException(e);
        }
    }

    public static long readUInt48(ByteBuffer byteBuffer) {
        long result = readUInt16(byteBuffer) << 32;
        if (result < 0) {
            throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
        }
        return result + readUInt32(byteBuffer);
    }
}
