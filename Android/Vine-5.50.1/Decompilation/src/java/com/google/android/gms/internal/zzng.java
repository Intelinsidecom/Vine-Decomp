package com.google.android.gms.internal;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class zzng<E> extends AbstractSet<E> {
    private final ArrayMap<E, E> zzami;

    public zzng() {
        this.zzami = new ArrayMap<>();
    }

    public zzng(int i) {
        this.zzami = new ArrayMap<>(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public zzng(Collection<E> collection) {
        this(collection.size());
        addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E object) {
        if (this.zzami.containsKey(object)) {
            return false;
        }
        this.zzami.put(object, object);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        return collection instanceof zzng ? zza((zzng) collection) : super.addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.zzami.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object object) {
        return this.zzami.containsKey(object);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return this.zzami.keySet().iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object object) {
        if (!this.zzami.containsKey(object)) {
            return false;
        }
        this.zzami.remove(object);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.zzami.size();
    }

    public boolean zza(zzng<? extends E> zzngVar) {
        int size = size();
        this.zzami.putAll((SimpleArrayMap<? extends E, ? extends E>) zzngVar.zzami);
        return size() > size;
    }
}
