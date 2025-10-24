package com.google.android.gms.common.internal;

import java.util.Iterator;

/* loaded from: classes2.dex */
public class zzv {
    private final String separator;

    private zzv(String str) {
        this.separator = str;
    }

    public static zzv zzcF(String str) {
        return new zzv(str);
    }

    public final String zza(Iterable<?> iterable) {
        return zza(new StringBuilder(), iterable).toString();
    }

    public final StringBuilder zza(StringBuilder sb, Iterable<?> iterable) {
        Iterator<?> it = iterable.iterator();
        if (it.hasNext()) {
            sb.append(zzw(it.next()));
            while (it.hasNext()) {
                sb.append(this.separator);
                sb.append(zzw(it.next()));
            }
        }
        return sb;
    }

    CharSequence zzw(Object obj) {
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
