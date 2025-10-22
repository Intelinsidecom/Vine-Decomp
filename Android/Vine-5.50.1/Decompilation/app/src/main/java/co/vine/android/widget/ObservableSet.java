package co.vine.android.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public final class ObservableSet<T> implements Iterable<T> {
    private final Set<T> mData = new HashSet();
    private final List<ChangeObserver<T>> mObservers = new ArrayList();

    public interface ChangeObserver<T> {
        void onAdd(T t);

        void onRemove(T t);
    }

    public synchronized void add(T t) {
        this.mData.add(t);
        for (ChangeObserver<T> observer : this.mObservers) {
            observer.onAdd(t);
        }
    }

    public synchronized void remove(T t) {
        this.mData.remove(t);
        for (ChangeObserver<T> observer : this.mObservers) {
            observer.onRemove(t);
        }
    }

    public synchronized boolean contains(T t) {
        return this.mData.contains(t);
    }

    public synchronized boolean isEmpty() {
        return this.mData.isEmpty();
    }

    public synchronized void addObserver(ChangeObserver<T> observer) {
        this.mObservers.add(observer);
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.mData.iterator();
    }
}
