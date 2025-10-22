package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.flurry.android.Constants;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class BitReaderBuffer {
    private ByteBuffer buffer;
    int initialPos;
    int position;

    public BitReaderBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
        this.initialPos = buffer.position();
    }

    public int readBits(int i) {
        int rc;
        byte b = this.buffer.get(this.initialPos + (this.position / 8));
        int v = b < 0 ? b + Constants.FEMALE : b;
        int left = 8 - (this.position % 8);
        if (i <= left) {
            rc = ((v << (this.position % 8)) & 255) >> ((this.position % 8) + (left - i));
            this.position += i;
        } else {
            int then = i - left;
            int rc2 = readBits(left);
            rc = (rc2 << then) + readBits(then);
        }
        this.buffer.position(this.initialPos + ((int) Math.ceil(this.position / 8.0d)));
        return rc;
    }

    public int remainingBits() {
        return (this.buffer.limit() * 8) - this.position;
    }
}
