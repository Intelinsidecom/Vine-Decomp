package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.zznl;

/* loaded from: classes.dex */
class zzaa {
    private long zzBv;
    private final zznl zzqD;

    public zzaa(zznl zznlVar) {
        com.google.android.gms.common.internal.zzx.zzy(zznlVar);
        this.zzqD = zznlVar;
    }

    public void clear() {
        this.zzBv = 0L;
    }

    public void start() {
        this.zzBv = this.zzqD.elapsedRealtime();
    }

    public boolean zzv(long j) {
        return this.zzBv == 0 || this.zzqD.elapsedRealtime() - this.zzBv >= j;
    }
}
