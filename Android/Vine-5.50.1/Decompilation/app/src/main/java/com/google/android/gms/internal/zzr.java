package com.google.android.gms.internal;

/* loaded from: classes.dex */
public class zzr extends Exception {
    private long zzC;
    public final zzi zzaj;

    public zzr() {
        this.zzaj = null;
    }

    public zzr(zzi zziVar) {
        this.zzaj = zziVar;
    }

    public zzr(Throwable th) {
        super(th);
        this.zzaj = null;
    }

    void zza(long j) {
        this.zzC = j;
    }
}
