package co.vine.android.util;

import java.util.HashMap;

/* loaded from: classes.dex */
public class FlexibleStringHashMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = -1378998403357321549L;

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        if (obj instanceof String) {
            V v = (V) super.get(obj);
            if (v == null) {
                for (K k : keySet()) {
                    if (((String) obj).contains((CharSequence) k)) {
                        return (V) super.get(k);
                    }
                }
                return null;
            }
            return v;
        }
        return (V) super.get(obj);
    }
}
