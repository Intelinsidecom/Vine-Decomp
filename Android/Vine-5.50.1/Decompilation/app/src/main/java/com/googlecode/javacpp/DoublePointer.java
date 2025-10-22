package com.googlecode.javacpp;

import java.nio.DoubleBuffer;

/* loaded from: classes.dex */
public class DoublePointer extends Pointer {
    private native void allocateArray(int i);

    public native double get(int i);

    public native DoublePointer get(double[] dArr, int i, int i2);

    public native DoublePointer put(int i, double d);

    public native DoublePointer put(double[] dArr, int i, int i2);

    public DoublePointer(double... array) {
        this(array.length);
        put(array);
    }

    public DoublePointer(DoubleBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            double[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public DoublePointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public DoublePointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public DoublePointer position(int position) {
        return (DoublePointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public DoublePointer limit(int limit) {
        return (DoublePointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public DoublePointer capacity(int capacity) {
        return (DoublePointer) super.capacity(capacity);
    }

    public double get() {
        return get(0);
    }

    public DoublePointer put(double d) {
        return put(0, d);
    }

    public DoublePointer get(double[] array) {
        return get(array, 0, array.length);
    }

    public DoublePointer put(double[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final DoubleBuffer asBuffer() {
        return asByteBuffer().asDoubleBuffer();
    }
}
