package com.google.android.gms.internal;

@zzha
/* loaded from: classes.dex */
public class zzix {
    private long zzLv;
    private long zzLw = Long.MIN_VALUE;
    private Object zzpK = new Object();

    public zzix(long j) {
        this.zzLv = j;
    }

    public boolean tryAcquire() {
        boolean z;
        synchronized (this.zzpK) {
            long jElapsedRealtime = com.google.android.gms.ads.internal.zzp.zzbB().elapsedRealtime();
            if (this.zzLw + this.zzLv > jElapsedRealtime) {
                z = false;
            } else {
                this.zzLw = jElapsedRealtime;
                z = true;
            }
        }
        return z;
    }
}
