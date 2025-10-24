package com.google.android.exoplayer;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public final class SampleHolder {
    private final int bufferReplacementMode;
    public final CryptoInfo cryptoInfo = new CryptoInfo();
    public ByteBuffer data;
    public int flags;
    public int size;
    public long timeUs;

    public SampleHolder(int bufferReplacementMode) {
        this.bufferReplacementMode = bufferReplacementMode;
    }

    public void ensureSpaceForWrite(int length) throws IllegalStateException {
        if (this.data == null) {
            this.data = createReplacementBuffer(length);
            return;
        }
        int capacity = this.data.capacity();
        int position = this.data.position();
        int requiredCapacity = position + length;
        if (capacity < requiredCapacity) {
            ByteBuffer newData = createReplacementBuffer(requiredCapacity);
            if (position > 0) {
                this.data.position(0);
                this.data.limit(position);
                newData.put(this.data);
            }
            this.data = newData;
        }
    }

    public boolean isEncrypted() {
        return (this.flags & 2) != 0;
    }

    public boolean isDecodeOnly() {
        return (this.flags & 134217728) != 0;
    }

    public boolean isSyncFrame() {
        return (this.flags & 1) != 0;
    }

    public void clearData() {
        if (this.data != null) {
            this.data.clear();
        }
    }

    private ByteBuffer createReplacementBuffer(int requiredCapacity) {
        if (this.bufferReplacementMode == 1) {
            return ByteBuffer.allocate(requiredCapacity);
        }
        if (this.bufferReplacementMode == 2) {
            return ByteBuffer.allocateDirect(requiredCapacity);
        }
        int currentCapacity = this.data == null ? 0 : this.data.capacity();
        throw new IllegalStateException("Buffer too small (" + currentCapacity + " < " + requiredCapacity + ")");
    }
}
