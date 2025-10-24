package com.google.android.gms.internal;

import java.util.concurrent.Future;

@zzha
/* loaded from: classes.dex */
public abstract class zzil implements zzir<Future> {
    private volatile Thread zzKt;
    private boolean zzKu;
    private final Runnable zzx;

    public zzil() {
        this.zzx = new Runnable() { // from class: com.google.android.gms.internal.zzil.1
            @Override // java.lang.Runnable
            public final void run() {
                zzil.this.zzKt = Thread.currentThread();
                zzil.this.zzbp();
            }
        };
        this.zzKu = false;
    }

    public zzil(boolean z) {
        this.zzx = new Runnable() { // from class: com.google.android.gms.internal.zzil.1
            @Override // java.lang.Runnable
            public final void run() {
                zzil.this.zzKt = Thread.currentThread();
                zzil.this.zzbp();
            }
        };
        this.zzKu = z;
    }

    @Override // com.google.android.gms.internal.zzir
    public final void cancel() {
        onStop();
        if (this.zzKt != null) {
            this.zzKt.interrupt();
        }
    }

    public abstract void onStop();

    public abstract void zzbp();

    @Override // com.google.android.gms.internal.zzir
    /* renamed from: zzgX, reason: merged with bridge method [inline-methods] */
    public final Future zzfR() {
        return this.zzKu ? zzio.zza(1, this.zzx) : zzio.zza(this.zzx);
    }
}
