package com.google.android.gms.internal;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

@zzha
/* loaded from: classes.dex */
public class zziv {
    private HandlerThread zzLs = null;
    private Handler mHandler = null;
    private int zzLt = 0;
    private final Object zzpK = new Object();

    public Looper zzhk() {
        Looper looper;
        synchronized (this.zzpK) {
            if (this.zzLt != 0) {
                com.google.android.gms.common.internal.zzx.zzb(this.zzLs, "Invalid state: mHandlerThread should already been initialized.");
            } else if (this.zzLs == null) {
                com.google.android.gms.ads.internal.util.client.zzb.v("Starting the looper thread.");
                this.zzLs = new HandlerThread("LooperProvider");
                this.zzLs.start();
                this.mHandler = new Handler(this.zzLs.getLooper());
                com.google.android.gms.ads.internal.util.client.zzb.v("Looper thread started.");
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.v("Resuming the looper thread");
                this.zzpK.notifyAll();
            }
            this.zzLt++;
            looper = this.zzLs.getLooper();
        }
        return looper;
    }

    public void zzhl() {
        synchronized (this.zzpK) {
            com.google.android.gms.common.internal.zzx.zzb(this.zzLt > 0, "Invalid state: release() called more times than expected.");
            int i = this.zzLt - 1;
            this.zzLt = i;
            if (i == 0) {
                this.mHandler.post(new Runnable() { // from class: com.google.android.gms.internal.zziv.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (zziv.this.zzpK) {
                            com.google.android.gms.ads.internal.util.client.zzb.v("Suspending the looper thread");
                            while (zziv.this.zzLt == 0) {
                                try {
                                    zziv.this.zzpK.wait();
                                    com.google.android.gms.ads.internal.util.client.zzb.v("Looper thread resumed");
                                } catch (InterruptedException e) {
                                    com.google.android.gms.ads.internal.util.client.zzb.v("Looper thread interrupted.");
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
