package org.parceler.converter;

import java.util.LinkedHashMap;

/* loaded from: classes.dex */
public abstract class LinkedHashMapParcelConverter<K, V> extends MapParcelConverter<K, V, LinkedHashMap<K, V>> {
    @Override // org.parceler.converter.MapParcelConverter
    public LinkedHashMap<K, V> createMap() {
        return new LinkedHashMap<>();
    }
}
