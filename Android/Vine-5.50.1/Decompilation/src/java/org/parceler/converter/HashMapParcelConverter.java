package org.parceler.converter;

import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class HashMapParcelConverter<K, V> extends MapParcelConverter<K, V, HashMap<K, V>> {
    @Override // org.parceler.converter.MapParcelConverter
    public HashMap<K, V> createMap() {
        return new HashMap<>();
    }
}
