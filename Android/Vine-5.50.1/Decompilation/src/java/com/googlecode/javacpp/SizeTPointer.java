package com.googlecode.javacpp;

import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Name;

@Name({"size_t"})
/* loaded from: classes.dex */
public class SizeTPointer extends Pointer {
    private native void allocateArray(int i);

    @Cast({"size_t"})
    public native long get(int i);

    public native SizeTPointer put(int i, long j);

    public SizeTPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public SizeTPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public SizeTPointer position(int position) {
        return (SizeTPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public SizeTPointer limit(int limit) {
        return (SizeTPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public SizeTPointer capacity(int capacity) {
        return (SizeTPointer) super.capacity(capacity);
    }

    public long get() {
        return get(0);
    }

    public SizeTPointer put(long s) {
        return put(0, s);
    }
}
