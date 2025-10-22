package co.vine.android.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SparseArray<T> extends android.util.SparseArray<T> implements Serializable, Iterable<Integer> {
    @Override // java.lang.Iterable
    public Iterator<Integer> iterator() {
        return new SparseKeyIterator();
    }

    public Iterator<T> valueIterator() {
        return new SparseValueIterator();
    }

    public void removeAll(Collection<Integer> keysToRemove) {
        for (Integer key : keysToRemove) {
            delete(key.intValue());
        }
    }

    public class SparseValueIterator implements Iterator<T> {
        public int index;

        public SparseValueIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < SparseArray.this.size();
        }

        @Override // java.util.Iterator
        public T next() {
            this.index++;
            return SparseArray.this.valueAt(this.index - 1);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public class SparseKeyIterator implements Iterator<Integer> {
        public int index;

        public SparseKeyIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < SparseArray.this.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Integer next() {
            this.index++;
            return Integer.valueOf(SparseArray.this.keyAt(this.index - 1));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
