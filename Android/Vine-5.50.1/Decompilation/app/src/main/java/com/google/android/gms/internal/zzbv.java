package com.google.android.gms.internal;

import android.content.SharedPreferences;

@zzha
/* loaded from: classes.dex */
public abstract class zzbv<T> {
    private final int zzuW;
    private final String zzuX;
    private final T zzuY;

    private zzbv(int i, String str, T t) {
        this.zzuW = i;
        this.zzuX = str;
        this.zzuY = t;
        com.google.android.gms.ads.internal.zzp.zzbF().zza(this);
    }

    public static zzbv<Integer> zza(int i, String str, int i2) {
        return new zzbv<Integer>(i, str, Integer.valueOf(i2)) { // from class: com.google.android.gms.internal.zzbv.2
            @Override // com.google.android.gms.internal.zzbv
            /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
            public Integer zza(SharedPreferences sharedPreferences) {
                return Integer.valueOf(sharedPreferences.getInt(getKey(), zzdk().intValue()));
            }
        };
    }

    public static zzbv<Long> zza(int i, String str, long j) {
        return new zzbv<Long>(i, str, Long.valueOf(j)) { // from class: com.google.android.gms.internal.zzbv.3
            @Override // com.google.android.gms.internal.zzbv
            /* renamed from: zzd, reason: merged with bridge method [inline-methods] */
            public Long zza(SharedPreferences sharedPreferences) {
                return Long.valueOf(sharedPreferences.getLong(getKey(), zzdk().longValue()));
            }
        };
    }

    public static zzbv<Boolean> zza(int i, String str, Boolean bool) {
        return new zzbv<Boolean>(i, str, bool) { // from class: com.google.android.gms.internal.zzbv.1
            @Override // com.google.android.gms.internal.zzbv
            /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
            public Boolean zza(SharedPreferences sharedPreferences) {
                return Boolean.valueOf(sharedPreferences.getBoolean(getKey(), zzdk().booleanValue()));
            }
        };
    }

    public static zzbv<String> zza(int i, String str, String str2) {
        return new zzbv<String>(i, str, str2) { // from class: com.google.android.gms.internal.zzbv.4
            @Override // com.google.android.gms.internal.zzbv
            /* renamed from: zze, reason: merged with bridge method [inline-methods] */
            public String zza(SharedPreferences sharedPreferences) {
                return sharedPreferences.getString(getKey(), zzdk());
            }
        };
    }

    public static zzbv<String> zzc(int i, String str) {
        zzbv<String> zzbvVarZza = zza(i, str, (String) null);
        com.google.android.gms.ads.internal.zzp.zzbF().zzb(zzbvVarZza);
        return zzbvVarZza;
    }

    public static zzbv<String> zzd(int i, String str) {
        zzbv<String> zzbvVarZza = zza(i, str, (String) null);
        com.google.android.gms.ads.internal.zzp.zzbF().zzc(zzbvVarZza);
        return zzbvVarZza;
    }

    public T get() {
        return (T) com.google.android.gms.ads.internal.zzp.zzbG().zzd(this);
    }

    public String getKey() {
        return this.zzuX;
    }

    protected abstract T zza(SharedPreferences sharedPreferences);

    public T zzdk() {
        return this.zzuY;
    }
}
