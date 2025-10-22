package com.google.android.gms.ads.internal.overlay;

import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzip;

@zzha
/* loaded from: classes.dex */
class zzq implements Runnable {
    private boolean mCancelled = false;
    private zzk zzEn;

    zzq(zzk zzkVar) {
        this.zzEn = zzkVar;
    }

    public void cancel() {
        this.mCancelled = true;
        zzip.zzKO.removeCallbacks(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mCancelled) {
            return;
        }
        this.zzEn.zzfu();
        zzfD();
    }

    public void zzfD() {
        zzip.zzKO.postDelayed(this, 250L);
    }
}
