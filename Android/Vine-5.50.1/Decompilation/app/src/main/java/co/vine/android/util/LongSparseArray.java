package co.vine.android.util;

import java.io.Serializable;
import java.util.Iterator;

/* loaded from: classes.dex */
public class LongSparseArray<T> extends android.support.v4.util.LongSparseArray<T> implements Serializable, Iterable<Long> {
    public LongSparseArray() {
    }

    public LongSparseArray(int initialCapacity) {
        super(initialCapacity);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T removeKey(long key) {
        T value = get(key);
        if (value != null) {
            super.remove(key);
        }
        return value;
    }

    @Override // java.lang.Iterable
    public Iterator<Long> iterator() {
        return new SparseKeyIterator();
    }

    public class SparseKeyIterator implements Iterator<Long> {
        public int index;

        public SparseKeyIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < LongSparseArray.this.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Long next() {
            this.index++;
            return Long.valueOf(LongSparseArray.this.keyAt(this.index - 1));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
