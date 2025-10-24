package org.parceler.converter;

import java.util.LinkedList;

/* loaded from: classes.dex */
public abstract class LinkedListParcelConverter<T> extends CollectionParcelConverter<T, LinkedList<T>> {
    @Override // org.parceler.converter.CollectionParcelConverter
    public LinkedList<T> createCollection() {
        return new LinkedList<>();
    }
}
