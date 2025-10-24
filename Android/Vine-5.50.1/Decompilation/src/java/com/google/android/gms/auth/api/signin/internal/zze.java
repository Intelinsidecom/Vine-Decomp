package com.google.android.gms.auth.api.signin.internal;

/* loaded from: classes2.dex */
public class zze {
    static int zzWa = 31;
    private int zzWb = 1;

    public zze zzP(boolean z) {
        this.zzWb = (z ? 1 : 0) + (this.zzWb * zzWa);
        return this;
    }

    public int zzmM() {
        return this.zzWb;
    }

    public zze zzo(Object obj) {
        this.zzWb = (obj == null ? 0 : obj.hashCode()) + (this.zzWb * zzWa);
        return this;
    }
}
