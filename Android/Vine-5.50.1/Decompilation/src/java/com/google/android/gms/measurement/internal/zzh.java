package com.google.android.gms.measurement.internal;

/* loaded from: classes.dex */
class zzh {
    final String mName;
    final String zzaRd;
    final long zzaSF;
    final long zzaSG;
    final long zzaSH;

    zzh(String str, String str2, long j, long j2, long j3) {
        com.google.android.gms.common.internal.zzx.zzcG(str);
        com.google.android.gms.common.internal.zzx.zzcG(str2);
        com.google.android.gms.common.internal.zzx.zzab(j >= 0);
        com.google.android.gms.common.internal.zzx.zzab(j2 >= 0);
        this.zzaRd = str;
        this.mName = str2;
        this.zzaSF = j;
        this.zzaSG = j2;
        this.zzaSH = j3;
    }

    zzh zzQ(long j) {
        return new zzh(this.zzaRd, this.mName, this.zzaSF + 1, this.zzaSG + 1, j);
    }
}
