package com.googlecode.javacpp;

import java.nio.ShortBuffer;

/* loaded from: classes.dex */
public class ShortPointer extends Pointer {
    private native void allocateArray(int i);

    public native ShortPointer get(short[] sArr, int i, int i2);

    public native short get(int i);

    public native ShortPointer put(int i, short s);

    public native ShortPointer put(short[] sArr, int i, int i2);

    public ShortPointer(short... array) {
        this(array.length);
        put(array);
    }

    public ShortPointer(ShortBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            short[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public ShortPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public ShortPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public ShortPointer position(int position) {
        return (ShortPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public ShortPointer limit(int limit) {
        return (ShortPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public ShortPointer capacity(int capacity) {
        return (ShortPointer) super.capacity(capacity);
    }

    public short get() {
        return get(0);
    }

    public ShortPointer put(short s) {
        return put(0, s);
    }

    public ShortPointer get(short[] array) {
        return get(array, 0, array.length);
    }

    public ShortPointer put(short[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final ShortBuffer asBuffer() {
        return asByteBuffer().asShortBuffer();
    }
}
