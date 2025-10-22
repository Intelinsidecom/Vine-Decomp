package com.googlecode.javacpp;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class BytePointer extends Pointer {
    private native void allocateArray(int i);

    public native byte get(int i);

    public native BytePointer get(byte[] bArr, int i, int i2);

    public native BytePointer put(int i, byte b);

    public native BytePointer put(byte[] bArr, int i, int i2);

    public BytePointer(String s, String charsetName) throws UnsupportedEncodingException {
        this(s.getBytes(charsetName).length + 1);
        putString(s, charsetName);
    }

    public BytePointer(String s) {
        this(s.getBytes().length + 1);
        putString(s);
    }

    public BytePointer(byte... array) {
        this(array.length);
        put(array);
    }

    public BytePointer(ByteBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            byte[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public BytePointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public BytePointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BytePointer position(int position) {
        return (BytePointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BytePointer limit(int limit) {
        return (BytePointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BytePointer capacity(int capacity) {
        return (BytePointer) super.capacity(capacity);
    }

    public byte[] getStringBytes() {
        byte[] buffer = new byte[16];
        int i = 0;
        int j = position();
        while (true) {
            byte b = position(j).get();
            buffer[i] = b;
            if (b != 0) {
                i++;
                j++;
                if (i >= buffer.length) {
                    byte[] newbuffer = new byte[buffer.length * 2];
                    System.arraycopy(buffer, 0, newbuffer, 0, buffer.length);
                    buffer = newbuffer;
                }
            } else {
                byte[] newbuffer2 = new byte[i];
                System.arraycopy(buffer, 0, newbuffer2, 0, i);
                return newbuffer2;
            }
        }
    }

    public String getString(String charsetName) throws UnsupportedEncodingException {
        return new String(getStringBytes(), charsetName);
    }

    public String getString() {
        return new String(getStringBytes());
    }

    public BytePointer putString(String s, String charsetName) throws UnsupportedEncodingException {
        byte[] bytes = s.getBytes(charsetName);
        put(bytes).put(bytes.length, (byte) 0);
        return this;
    }

    public BytePointer putString(String s) {
        byte[] bytes = s.getBytes();
        return put(bytes).put(bytes.length, (byte) 0);
    }

    public byte get() {
        return get(0);
    }

    public BytePointer put(byte b) {
        return put(0, b);
    }

    public BytePointer get(byte[] array) {
        return get(array, 0, array.length);
    }

    public BytePointer put(byte[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final ByteBuffer asBuffer() {
        return asByteBuffer();
    }
}
