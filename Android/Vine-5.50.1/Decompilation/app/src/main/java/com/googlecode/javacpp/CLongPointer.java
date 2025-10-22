package com.googlecode.javacpp;

import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Name;

@Name({"long"})
/* loaded from: classes.dex */
public class CLongPointer extends Pointer {
    private native void allocateArray(int i);

    @Cast({"long"})
    public native long get(int i);

    public native CLongPointer put(int i, long j);

    public CLongPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public CLongPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CLongPointer position(int position) {
        return (CLongPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CLongPointer limit(int limit) {
        return (CLongPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CLongPointer capacity(int capacity) {
        return (CLongPointer) super.capacity(capacity);
    }

    public long get() {
        return get(0);
    }

    public CLongPointer put(long l) {
        return put(0, l);
    }
}
