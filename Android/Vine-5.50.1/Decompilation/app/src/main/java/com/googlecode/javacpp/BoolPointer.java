package com.googlecode.javacpp;

import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Name;

@Name({"bool"})
/* loaded from: classes.dex */
public class BoolPointer extends Pointer {
    private native void allocateArray(int i);

    @Cast({"bool"})
    public native boolean get(int i);

    public native BoolPointer put(int i, boolean z);

    public BoolPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public BoolPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BoolPointer position(int position) {
        return (BoolPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BoolPointer limit(int limit) {
        return (BoolPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public BoolPointer capacity(int capacity) {
        return (BoolPointer) super.capacity(capacity);
    }

    public boolean get() {
        return get(0);
    }

    public BoolPointer put(boolean b) {
        return put(0, b);
    }
}
