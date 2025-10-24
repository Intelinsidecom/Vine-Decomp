package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import android.support.v4.view.InputDeviceCompat;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class BitWriterBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;
    private ByteBuffer buffer;
    int initialPos;
    int position = 0;

    static {
        $assertionsDisabled = !BitWriterBuffer.class.desiredAssertionStatus();
    }

    public BitWriterBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
        this.initialPos = buffer.position();
    }

    public void writeBits(int i, int numBits) {
        if (!$assertionsDisabled && i > (1 << numBits) - 1) {
            throw new AssertionError(String.format("Trying to write a value bigger (%s) than the number bits (%s) allows. Please mask the value before writing it and make your code is really working as intended.", Integer.valueOf(i), Integer.valueOf((1 << numBits) - 1)));
        }
        int left = 8 - (this.position % 8);
        if (numBits <= left) {
            int current = this.buffer.get(this.initialPos + (this.position / 8));
            if (current < 0) {
                current += 256;
            }
            int current2 = current + (i << (left - numBits));
            ByteBuffer byteBuffer = this.buffer;
            int i2 = this.initialPos + (this.position / 8);
            if (current2 > 127) {
                current2 += InputDeviceCompat.SOURCE_ANY;
            }
            byteBuffer.put(i2, (byte) current2);
            this.position += numBits;
        } else {
            int bitsSecondWrite = numBits - left;
            writeBits(i >> bitsSecondWrite, left);
            writeBits(((1 << bitsSecondWrite) - 1) & i, bitsSecondWrite);
        }
        this.buffer.position((this.position % 8 <= 0 ? 0 : 1) + this.initialPos + (this.position / 8));
    }
}
