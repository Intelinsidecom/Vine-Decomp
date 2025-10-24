package io.realm.internal;

import java.util.IdentityHashMap;

/* loaded from: classes.dex */
public class IdentitySet<K> extends IdentityHashMap<K, Integer> {
    private static final Integer PLACE_HOLDER = 0;

    public void add(K key) {
        put(key, PLACE_HOLDER);
    }
}
