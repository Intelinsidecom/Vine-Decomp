package com.google.android.gms.measurement.internal;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
abstract class zze {
    private static volatile Handler zzQi;
    private volatile long zzQj;
    private final zzt zzaQX;
    private boolean zzaSy;
    private final Runnable zzx;

    zze(zzt zztVar) {
        com.google.android.gms.common.internal.zzx.zzy(zztVar);
        this.zzaQX = zztVar;
        this.zzaSy = true;
        this.zzx = new Runnable() { // from class: com.google.android.gms.measurement.internal.zze.1
            @Override // java.lang.Runnable
            public void run() throws IllegalStateException {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    zze.this.zzaQX.zzAV().zzg(this);
                    return;
                }
                boolean zZzbr = zze.this.zzbr();
                zze.this.zzQj = 0L;
                if (zZzbr && zze.this.zzaSy) {
                    zze.this.run();
                }
            }
        };
    }

    private Handler getHandler() {
        Handler handler;
        if (zzQi != null) {
            return zzQi;
        }
        synchronized (zze.class) {
            if (zzQi == null) {
                zzQi = new Handler(this.zzaQX.getContext().getMainLooper());
            }
            handler = zzQi;
        }
        return handler;
    }

    public void cancel() {
        this.zzQj = 0L;
        getHandler().removeCallbacks(this.zzx);
    }

    public abstract void run();

    public boolean zzbr() {
        return this.zzQj != 0;
    }

    public void zzt(long j) {
        cancel();
        if (j >= 0) {
            this.zzQj = this.zzaQX.zziT().currentTimeMillis();
            if (getHandler().postDelayed(this.zzx, j)) {
                return;
            }
            this.zzaQX.zzzz().zzBl().zzj("Failed to schedule delayed post. time", Long.valueOf(j));
        }
    }
}
