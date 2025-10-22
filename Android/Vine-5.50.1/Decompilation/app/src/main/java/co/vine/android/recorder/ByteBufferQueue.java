package co.vine.android.recorder;

import co.vine.android.util.AllocatedByteBuffer;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ByteBufferQueue {
    private static final ArrayList<WeakReference<ByteBufferQueue>> sQueues = new ArrayList<>();
    private final int[] LOCK = new int[0];
    private AllocatedByteBuffer mAllocatedByteBuffer;
    private byte[] mGetBuffer;
    private int mInitialCount;
    private boolean mPutHasEnded;
    private int mSpaceLeft;
    private int mStartIndex;
    private ArrayList<Integer> mStarts;
    private ArrayList<WeakReference<Object>> mTags;

    public interface MemoryResponder {
        void requestForMoreMemory();
    }

    public ByteBufferQueue(int count, int singleCapacity, MemoryResponder memoryResponder) {
        if (sQueues.size() > 20) {
            sQueues.clear();
        }
        sQueues.add(new WeakReference<>(this));
        int n = 0;
        Iterator<WeakReference<ByteBufferQueue>> it = sQueues.iterator();
        while (it.hasNext()) {
            WeakReference<ByteBufferQueue> queue = it.next();
            if (queue.get() != null) {
                n++;
            }
        }
        CrashUtil.log("[mem] Current byte buffer queue count: {}.", Integer.valueOf(n));
        if (SLog.sLogsOn) {
            Runtime runtime = Runtime.getRuntime();
            long usedMem = (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
            long maxHeap = runtime.maxMemory() / 1048576;
            SLog.d("[mem] Allocating: {}.", Long.valueOf((singleCapacity * count) / 1048576));
            SLog.d("[mem] Free: {}.", Long.valueOf(runtime.freeMemory() / 1048576));
            SLog.d("[mem] Used mem: {}.", Long.valueOf(usedMem));
            SLog.d("[mem] Max mem: {}.", Long.valueOf(maxHeap));
        }
        try {
            reset(count, singleCapacity);
        } catch (OutOfMemoryError e) {
            if (memoryResponder != null) {
                memoryResponder.requestForMoreMemory();
            }
            System.gc();
            reset(count, singleCapacity);
        }
    }

    public void reset(int count, int capacity) {
        synchronized (this.LOCK) {
            this.mSpaceLeft = count;
            this.mInitialCount = count;
            if (this.mAllocatedByteBuffer == null || this.mAllocatedByteBuffer.capacity() < capacity * count) {
                release();
                this.mAllocatedByteBuffer = AllocatedByteBuffer.allocate(capacity * count);
            }
            if (this.mGetBuffer == null || this.mGetBuffer.length != capacity) {
                this.mGetBuffer = new byte[capacity];
            }
            this.mAllocatedByteBuffer.getBuffer().position(0);
            this.mStarts = new ArrayList<>();
            this.mTags = new ArrayList<>();
            this.mPutHasEnded = false;
            this.mStartIndex = 0;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    public void release() {
        if (this.mAllocatedByteBuffer != null) {
            this.mAllocatedByteBuffer.deallocate();
            this.mAllocatedByteBuffer = null;
        }
    }

    public boolean put(QueueItem item) {
        boolean z = false;
        synchronized (this.LOCK) {
            this.mSpaceLeft--;
            if (this.mSpaceLeft >= 0) {
                ByteBuffer buffer = this.mAllocatedByteBuffer.getBuffer();
                this.mStarts.add(Integer.valueOf(buffer.position()));
                this.mTags.add(new WeakReference<>(item.tag));
                buffer.put(item.bytes);
                if (buffer.position() == buffer.limit()) {
                    buffer.position(0);
                }
                z = true;
            }
        }
        return z;
    }

    public void endOfPut() {
        this.mPutHasEnded = true;
    }

    public boolean isEndOfPut() {
        return this.mPutHasEnded;
    }

    public static class QueueItem {
        final byte[] bytes;
        final Object tag;

        public QueueItem(byte[] bytes, Object tag) {
            this.bytes = bytes;
            this.tag = tag;
        }
    }

    public QueueItem get() {
        QueueItem queueItem;
        synchronized (this.LOCK) {
            if (this.mStartIndex == this.mStarts.size()) {
                queueItem = null;
            } else {
                Integer next = this.mStarts.get(this.mStartIndex);
                Object tag = this.mTags.get(this.mStartIndex).get();
                this.mStartIndex++;
                this.mSpaceLeft++;
                ByteBuffer buffer = this.mAllocatedByteBuffer.getBuffer();
                int originalPosition = buffer.position();
                buffer.position(next.intValue());
                buffer.get(this.mGetBuffer);
                buffer.position(originalPosition);
                queueItem = new QueueItem(this.mGetBuffer, tag);
            }
        }
        return queueItem;
    }

    public int size() {
        return this.mInitialCount - this.mSpaceLeft;
    }

    public boolean isFresh() {
        return this.mStarts.size() == 0;
    }
}
