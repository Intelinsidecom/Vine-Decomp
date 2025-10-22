package com.google.android.gms.internal;

import android.support.v4.util.ArrayMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public final class zznm {
    public static <K, V> Map<K, V> zza(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        ArrayMap arrayMap = new ArrayMap(6);
        arrayMap.put(k, v);
        arrayMap.put(k2, v2);
        arrayMap.put(k3, v3);
        arrayMap.put(k4, v4);
        arrayMap.put(k5, v5);
        arrayMap.put(k6, v6);
        return Collections.unmodifiableMap(arrayMap);
    }

    public static <T> Set<T> zza(T t, T t2, T t3) {
        zzng zzngVar = new zzng(3);
        zzngVar.add(t);
        zzngVar.add(t2);
        zzngVar.add(t3);
        return Collections.unmodifiableSet(zzngVar);
    }

    public static <T> Set<T> zza(T t, T t2, T t3, T t4) {
        zzng zzngVar = new zzng(4);
        zzngVar.add(t);
        zzngVar.add(t2);
        zzngVar.add(t3);
        zzngVar.add(t4);
        return Collections.unmodifiableSet(zzngVar);
    }

    public static <T> Set<T> zzc(T... tArr) {
        switch (tArr.length) {
            case 0:
                return zzrL();
            case 1:
                return zzz(tArr[0]);
            case 2:
                return zzd(tArr[0], tArr[1]);
            case 3:
                return zza(tArr[0], tArr[1], tArr[2]);
            case 4:
                return zza(tArr[0], tArr[1], tArr[2], tArr[3]);
            default:
                return Collections.unmodifiableSet(tArr.length <= 32 ? new zzng(Arrays.asList(tArr)) : new HashSet(Arrays.asList(tArr)));
        }
    }

    public static <T> Set<T> zzd(T t, T t2) {
        zzng zzngVar = new zzng(2);
        zzngVar.add(t);
        zzngVar.add(t2);
        return Collections.unmodifiableSet(zzngVar);
    }

    public static <T> Set<T> zzrL() {
        return Collections.emptySet();
    }

    public static <T> Set<T> zzz(T t) {
        return Collections.singleton(t);
    }
}
