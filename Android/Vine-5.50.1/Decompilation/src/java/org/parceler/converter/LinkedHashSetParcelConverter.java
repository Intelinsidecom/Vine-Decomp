package org.parceler.converter;

import java.util.LinkedHashSet;

/* loaded from: classes.dex */
public abstract class LinkedHashSetParcelConverter<T> extends CollectionParcelConverter<T, LinkedHashSet<T>> {
    @Override // org.parceler.converter.CollectionParcelConverter
    public LinkedHashSet<T> createCollection() {
        return new LinkedHashSet<>();
    }
}
