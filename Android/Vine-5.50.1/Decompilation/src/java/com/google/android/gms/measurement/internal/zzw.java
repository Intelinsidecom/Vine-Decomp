package com.google.android.gms.measurement.internal;

/* loaded from: classes.dex */
abstract class zzw extends zzv {
    private boolean zzOQ;
    private boolean zzOR;
    private boolean zzaUH;

    zzw(zzt zztVar) {
        super(zztVar);
        this.zzaQX.zzb(this);
    }

    boolean isInitialized() {
        return this.zzOQ && !this.zzOR;
    }

    boolean zzBP() {
        return this.zzaUH;
    }

    public final void zza() {
        if (this.zzOQ) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzir();
        this.zzaQX.zzBO();
        this.zzOQ = true;
    }

    protected abstract void zzir();

    protected void zzje() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
