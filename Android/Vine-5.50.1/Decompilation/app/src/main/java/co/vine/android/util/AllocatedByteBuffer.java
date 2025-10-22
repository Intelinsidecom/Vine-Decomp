package co.vine.android.util;

import com.googlecode.javacpp.BytePointer;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class AllocatedByteBuffer {
    private final ByteBuffer mBuffer;
    private final BytePointer mPointer;

    public static AllocatedByteBuffer allocate(int capacity) {
        BytePointer pointer = new BytePointer(capacity);
        ByteBuffer buffer = pointer.asByteBuffer();
        return buffer == null ? new AllocatedByteBuffer(null, ByteBuffer.allocate(capacity)) : new AllocatedByteBuffer(pointer, buffer);
    }

    public AllocatedByteBuffer(BytePointer pointer, ByteBuffer buffer) {
        this.mPointer = pointer;
        this.mBuffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return this.mBuffer;
    }

    public int capacity() {
        return this.mBuffer.capacity();
    }

    public void deallocate() {
        if (this.mPointer != null && !this.mPointer.isNull()) {
            this.mPointer.deallocate();
        }
    }
}
