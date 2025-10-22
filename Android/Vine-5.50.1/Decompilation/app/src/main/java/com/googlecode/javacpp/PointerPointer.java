package com.googlecode.javacpp;

/* loaded from: classes.dex */
public class PointerPointer extends Pointer {
    private Pointer[] pointerArray;

    private native void allocateArray(int i);

    public native Pointer get(int i);

    public native PointerPointer put(int i, Pointer pointer);

    public PointerPointer(Pointer... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(byte[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(short[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(int[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(long[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(float[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(double[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(char[]... array) {
        this(array.length);
        put(array);
    }

    public PointerPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public PointerPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public PointerPointer position(int position) {
        return (PointerPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public PointerPointer limit(int limit) {
        return (PointerPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public PointerPointer capacity(int capacity) {
        return (PointerPointer) super.capacity(capacity);
    }

    public PointerPointer put(Pointer... array) {
        for (int i = 0; i < array.length; i++) {
            put(i, array[i]);
        }
        return this;
    }

    public PointerPointer put(byte[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new BytePointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(short[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new ShortPointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(int[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new IntPointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(long[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new LongPointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(float[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new FloatPointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(double[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new DoublePointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public PointerPointer put(char[]... array) {
        this.pointerArray = new Pointer[array.length];
        for (int i = 0; i < array.length; i++) {
            this.pointerArray[i] = new CharPointer(array[i]);
        }
        return put(this.pointerArray);
    }

    public Pointer get() {
        return get(0);
    }

    @Override // com.googlecode.javacpp.Pointer
    public PointerPointer put(Pointer p) {
        return put(0, p);
    }
}
