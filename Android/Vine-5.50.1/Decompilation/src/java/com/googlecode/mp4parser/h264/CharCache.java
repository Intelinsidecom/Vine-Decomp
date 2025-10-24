package com.googlecode.mp4parser.h264;

/* loaded from: classes2.dex */
public class CharCache {
    private char[] cache;
    private int pos;

    public CharCache(int capacity) {
        this.cache = new char[capacity];
    }

    public String toString() {
        return new String(this.cache, 0, this.pos);
    }

    public void clear() {
        this.pos = 0;
    }

    public void append(char c) {
        if (this.pos < this.cache.length - 1) {
            this.cache[this.pos] = c;
            this.pos++;
        }
    }

    public int length() {
        return this.pos;
    }
}
