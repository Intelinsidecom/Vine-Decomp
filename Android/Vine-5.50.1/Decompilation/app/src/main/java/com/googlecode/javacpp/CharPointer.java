package com.googlecode.javacpp;

import java.nio.CharBuffer;

/* loaded from: classes.dex */
public class CharPointer extends Pointer {
    private native void allocateArray(int i);

    public native char get(int i);

    public native CharPointer get(char[] cArr, int i, int i2);

    public native CharPointer put(int i, char c);

    public native CharPointer put(char[] cArr, int i, int i2);

    public CharPointer(String s) {
        this(s.toCharArray().length + 1);
        putString(s);
    }

    public CharPointer(char... array) {
        this(array.length);
        put(array);
    }

    public CharPointer(CharBuffer buffer) {
        super(buffer);
        if (buffer != null && buffer.hasArray()) {
            char[] array = buffer.array();
            allocateArray(array.length);
            put(array);
            position(buffer.position());
            limit(buffer.limit());
        }
    }

    public CharPointer(int size) {
        try {
            allocateArray(size);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("No native JavaCPP library in memory. (Has Loader.load() been called?)", e);
        }
    }

    public CharPointer(Pointer p) {
        super(p);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CharPointer position(int position) {
        return (CharPointer) super.position(position);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CharPointer limit(int limit) {
        return (CharPointer) super.limit(limit);
    }

    @Override // com.googlecode.javacpp.Pointer
    public CharPointer capacity(int capacity) {
        return (CharPointer) super.capacity(capacity);
    }

    public char[] getStringChars() {
        char[] buffer = new char[16];
        int i = 0;
        int j = position();
        while (true) {
            char c = position(j).get();
            buffer[i] = c;
            if (c != 0) {
                i++;
                j++;
                if (i >= buffer.length) {
                    char[] newbuffer = new char[buffer.length * 2];
                    System.arraycopy(buffer, 0, newbuffer, 0, buffer.length);
                    buffer = newbuffer;
                }
            } else {
                char[] newbuffer2 = new char[i];
                System.arraycopy(buffer, 0, newbuffer2, 0, i);
                return newbuffer2;
            }
        }
    }

    public String getString() {
        return new String(getStringChars());
    }

    public CharPointer putString(String s) {
        char[] chars = s.toCharArray();
        return put(chars).put(chars.length, (char) 0);
    }

    public char get() {
        return get(0);
    }

    public CharPointer put(char c) {
        return put(0, c);
    }

    public CharPointer get(char[] array) {
        return get(array, 0, array.length);
    }

    public CharPointer put(char[] array) {
        return put(array, 0, array.length);
    }

    @Override // com.googlecode.javacpp.Pointer
    public final CharBuffer asBuffer() {
        return asByteBuffer().asCharBuffer();
    }
}
