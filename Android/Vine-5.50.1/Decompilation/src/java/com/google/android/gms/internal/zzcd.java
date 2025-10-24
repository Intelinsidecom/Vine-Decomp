package com.google.android.gms.internal;

@zzha
/* loaded from: classes.dex */
public class zzcd {
    public static zzcf zza(zzch zzchVar, long j) {
        if (zzchVar == null) {
            return null;
        }
        return zzchVar.zzb(j);
    }

    public static boolean zza(zzch zzchVar, zzcf zzcfVar, long j, String... strArr) {
        if (zzchVar == null || zzcfVar == null) {
            return false;
        }
        return zzchVar.zza(zzcfVar, j, strArr);
    }

    public static boolean zza(zzch zzchVar, zzcf zzcfVar, String... strArr) {
        if (zzchVar == null || zzcfVar == null) {
            return false;
        }
        return zzchVar.zza(zzcfVar, strArr);
    }

    public static zzcf zzb(zzch zzchVar) {
        if (zzchVar == null) {
            return null;
        }
        return zzchVar.zzdu();
    }
}
