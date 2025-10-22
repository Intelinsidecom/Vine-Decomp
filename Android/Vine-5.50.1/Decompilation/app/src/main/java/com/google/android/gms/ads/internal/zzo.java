package com.google.android.gms.ads.internal;

import android.os.Handler;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzip;
import java.lang.ref.WeakReference;

@zzha
/* loaded from: classes.dex */
public class zzo {
    private final zza zzqn;
    private AdRequestParcel zzqo;
    private boolean zzqp;
    private boolean zzqq;
    private long zzqr;
    private final Runnable zzx;

    public static class zza {
        private final Handler mHandler;

        public zza(Handler handler) {
            this.mHandler = handler;
        }

        public boolean postDelayed(Runnable runnable, long timeFromNowInMillis) {
            return this.mHandler.postDelayed(runnable, timeFromNowInMillis);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public zzo(com.google.android.gms.ads.internal.zza zzaVar) {
        this(zzaVar, new zza(zzip.zzKO));
    }

    zzo(com.google.android.gms.ads.internal.zza zzaVar, zza zzaVar2) {
        this.zzqp = false;
        this.zzqq = false;
        this.zzqr = 0L;
        this.zzqn = zzaVar2;
        final WeakReference weakReference = new WeakReference(zzaVar);
        this.zzx = new Runnable() { // from class: com.google.android.gms.ads.internal.zzo.1
            @Override // java.lang.Runnable
            public void run() {
                zzo.this.zzqp = false;
                com.google.android.gms.ads.internal.zza zzaVar3 = (com.google.android.gms.ads.internal.zza) weakReference.get();
                if (zzaVar3 != null) {
                    zzaVar3.zzd(zzo.this.zzqo);
                }
            }
        };
    }

    public void cancel() {
        this.zzqp = false;
        this.zzqn.removeCallbacks(this.zzx);
    }

    public void pause() {
        this.zzqq = true;
        if (this.zzqp) {
            this.zzqn.removeCallbacks(this.zzx);
        }
    }

    public void resume() {
        this.zzqq = false;
        if (this.zzqp) {
            this.zzqp = false;
            zza(this.zzqo, this.zzqr);
        }
    }

    public void zza(AdRequestParcel adRequestParcel, long j) {
        if (this.zzqp) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("An ad refresh is already scheduled.");
            return;
        }
        this.zzqo = adRequestParcel;
        this.zzqp = true;
        this.zzqr = j;
        if (this.zzqq) {
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Scheduling ad refresh " + j + " milliseconds from now.");
        this.zzqn.postDelayed(this.zzx, j);
    }

    public boolean zzbr() {
        return this.zzqp;
    }

    public void zzg(AdRequestParcel adRequestParcel) {
        zza(adRequestParcel, 60000L);
    }
}
