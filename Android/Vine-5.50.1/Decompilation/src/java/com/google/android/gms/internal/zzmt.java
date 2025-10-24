package com.google.android.gms.internal;

import android.os.Binder;

/* loaded from: classes2.dex */
public abstract class zzmt<T> {
    private T zzRi = null;
    protected final String zzuX;
    protected final T zzuY;
    private static final Object zzqf = new Object();
    private static zza zzahn = null;
    private static int zzaho = 0;
    private static String zzahp = "com.google.android.providers.gsf.permission.READ_GSERVICES";

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zza(String str, Boolean bool);

        Integer zzb(String str, Integer num);
    }

    protected zzmt(String str, T t) {
        this.zzuX = str;
        this.zzuY = t;
    }

    public static boolean isInitialized() {
        return zzahn != null;
    }

    public static zzmt<Integer> zza(String str, Integer num) {
        return new zzmt<Integer>(str, num) { // from class: com.google.android.gms.internal.zzmt.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzmt
            /* renamed from: zzcq, reason: merged with bridge method [inline-methods] */
            public Integer zzcn(String str2) {
                return zzmt.zzahn.zzb(this.zzuX, (Integer) this.zzuY);
            }
        };
    }

    public static zzmt<Long> zza(String str, Long l) {
        return new zzmt<Long>(str, l) { // from class: com.google.android.gms.internal.zzmt.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzmt
            /* renamed from: zzcp, reason: merged with bridge method [inline-methods] */
            public Long zzcn(String str2) {
                return zzmt.zzahn.getLong(this.zzuX, (Long) this.zzuY);
            }
        };
    }

    public static zzmt<Boolean> zzg(String str, boolean z) {
        return new zzmt<Boolean>(str, Boolean.valueOf(z)) { // from class: com.google.android.gms.internal.zzmt.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzmt
            /* renamed from: zzco, reason: merged with bridge method [inline-methods] */
            public Boolean zzcn(String str2) {
                return zzmt.zzahn.zza(this.zzuX, (Boolean) this.zzuY);
            }
        };
    }

    public static int zzpE() {
        return zzaho;
    }

    public static zzmt<String> zzw(String str, String str2) {
        return new zzmt<String>(str, str2) { // from class: com.google.android.gms.internal.zzmt.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.android.gms.internal.zzmt
            /* renamed from: zzcs, reason: merged with bridge method [inline-methods] */
            public String zzcn(String str3) {
                return zzmt.zzahn.getString(this.zzuX, (String) this.zzuY);
            }
        };
    }

    public final T get() {
        return this.zzRi != null ? this.zzRi : zzcn(this.zzuX);
    }

    protected abstract T zzcn(String str);

    public final T zzpF() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return get();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }
}
