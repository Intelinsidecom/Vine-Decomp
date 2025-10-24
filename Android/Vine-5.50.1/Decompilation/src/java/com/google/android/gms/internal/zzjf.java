package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@zzha
/* loaded from: classes.dex */
class zzjf {
    private final Object zzLS = new Object();
    private final List<Runnable> zzLT = new ArrayList();
    private final List<Runnable> zzLU = new ArrayList();
    private boolean zzLV = false;

    private void zzd(Runnable runnable) {
        zzio.zza(runnable);
    }

    private void zze(Runnable runnable) {
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(runnable);
    }

    public void zzb(Runnable runnable) {
        synchronized (this.zzLS) {
            if (this.zzLV) {
                zzd(runnable);
            } else {
                this.zzLT.add(runnable);
            }
        }
    }

    public void zzc(Runnable runnable) {
        synchronized (this.zzLS) {
            if (this.zzLV) {
                zze(runnable);
            } else {
                this.zzLU.add(runnable);
            }
        }
    }

    public void zzht() {
        synchronized (this.zzLS) {
            if (this.zzLV) {
                return;
            }
            Iterator<Runnable> it = this.zzLT.iterator();
            while (it.hasNext()) {
                zzd(it.next());
            }
            Iterator<Runnable> it2 = this.zzLU.iterator();
            while (it2.hasNext()) {
                zze(it2.next());
            }
            this.zzLT.clear();
            this.zzLU.clear();
            this.zzLV = true;
        }
    }
}
