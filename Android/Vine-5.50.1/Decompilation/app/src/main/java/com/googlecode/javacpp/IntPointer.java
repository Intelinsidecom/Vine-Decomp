package com.googlecode.javacpp;

import java.nio.IntBuffer;

/* loaded from: classes.dex */
public class IntPointer extends Pointer {
    private native void allocateArray(int i);

    public native int get(int i);

    public native IntPointer get(int[] iArr, int i, int i2);

    public native IntPointer put(int i, int i2);

    public native IntPointer put(int[] iArr, int i, int i2);

    public IntPointer(String s) {
        this(s.length() + 1);
        putString(s);
    }

    public IntPointer(int... array) {
        this(array.length);
        put(array);
    }

    public IntPointer(IntBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            int[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public IntPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public IntPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public IntPointer position(int position) {
        return (IntPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public IntPointer limit(int limit) {
        return (IntPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public IntPointer capacity(int capacity) {
        return (IntPointer) super.capacity(capacity);
    }

    public int[] getStringCodePoints() {
        int[] buffer = new int[16];
        int i = 0;
        int j = position();
        while (true) {
            int i2 = position(j).get();
            buffer[i] = i2;
            if (i2 != 0) {
                i++;
                j++;
                if (i >= buffer.length) {
                    int[] newbuffer = new int[buffer.length * 2];
                    System.arraycopy(buffer, 0, newbuffer, 0, buffer.length);
                    buffer = newbuffer;
                }
            } else {
                int[] newbuffer2 = new int[i];
                System.arraycopy(buffer, 0, newbuffer2, 0, i);
                return newbuffer2;
            }
        }
    }

    public String getString() {
        int[] codePoints = getStringCodePoints();
        return new String(codePoints, 0, codePoints.length);
    }

    public IntPointer putString(String s) {
        int[] codePoints = new int[s.length()];
        for (int i = 0; i < codePoints.length; i++) {
            codePoints[i] = s.codePointAt(i);
        }
        return put(codePoints).put(codePoints.length, 0);
    }

    public int get() {
        return get(0);
    }

    public IntPointer put(int j) {
        return put(0, j);
    }

    public IntPointer get(int[] array) {
        return get(array, 0, array.length);
    }

    public IntPointer put(int[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final IntBuffer asBuffer() {
        return asByteBuffer().asIntBuffer();
    }
}
