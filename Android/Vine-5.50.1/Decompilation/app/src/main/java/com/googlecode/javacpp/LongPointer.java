package com.googlecode.javacpp;

import java.nio.LongBuffer;

/* loaded from: classes.dex */
public class LongPointer extends Pointer {
    private native void allocateArray(int i);

    public native long get(int i);

    public native LongPointer get(long[] jArr, int i, int i2);

    public native LongPointer put(int i, long j);

    public native LongPointer put(long[] jArr, int i, int i2);

    public LongPointer(long... array) {
        this(array.length);
        put(array);
    }

    public LongPointer(LongBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            long[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public LongPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public LongPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public LongPointer position(int position) {
        return (LongPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public LongPointer limit(int limit) {
        return (LongPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public LongPointer capacity(int capacity) {
        return (LongPointer) super.capacity(capacity);
    }

    public long get() {
        return get(0);
    }

    public LongPointer put(long l) {
        return put(0, l);
    }

    public LongPointer get(long[] array) {
        return get(array, 0, array.length);
    }

    public LongPointer put(long[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final LongBuffer asBuffer() {
        return asByteBuffer().asLongBuffer();
    }
}
