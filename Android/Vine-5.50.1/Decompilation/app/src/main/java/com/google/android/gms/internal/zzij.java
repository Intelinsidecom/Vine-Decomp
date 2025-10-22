package com.google.android.gms.internal;

import android.os.Bundle;

@zzha
/* loaded from: classes.dex */
public class zzij {
    private final String zzJO;
    private int zzKq;
    private int zzKr;
    private final Object zzpK;
    private final zzig zzqC;

    zzij(zzig zzigVar, String str) {
        this.zzpK = new Object();
        this.zzqC = zzigVar;
        this.zzJO = str;
    }

    public zzij(String str) {
        this(com.google.android.gms.ads.internal.zzp.zzbA(), str);
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.zzpK) {
            bundle = new Bundle();
            bundle.putInt("pmnli", this.zzKq);
            bundle.putInt("pmnll", this.zzKr);
        }
        return bundle;
    }

    public void zzg(int i, int i2) {
        synchronized (this.zzpK) {
            this.zzKq = i;
            this.zzKr = i2;
            this.zzqC.zza(this.zzJO, this);
        }
    }
}
