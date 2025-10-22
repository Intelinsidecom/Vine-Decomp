package org.parceler.converter;

import java.util.HashSet;

/* loaded from: classes.dex */
public abstract class HashSetParcelConverter<T> extends CollectionParcelConverter<T, HashSet<T>> {
    @Override // org.parceler.converter.CollectionParcelConverter
    public HashSet<T> createCollection() {
        return new HashSet<>();
    }
}
