package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;

@zzha
/* loaded from: classes.dex */
public abstract class zzgp extends zzil {
    protected final Context mContext;
    protected final zzgq.zza zzFb;
    protected final zzie.zza zzFc;
    protected AdResponseParcel zzFd;
    protected final Object zzFf;
    protected final Object zzpK;

    protected static final class zza extends Exception {
        private final int zzFt;

        public zza(String str, int i) {
            super(str);
            this.zzFt = i;
        }

        public int getErrorCode() {
            return this.zzFt;
        }
    }

    protected zzgp(Context context, zzie.zza zzaVar, zzgq.zza zzaVar2) {
        super(true);
        this.zzpK = new Object();
        this.zzFf = new Object();
        this.mContext = context;
        this.zzFc = zzaVar;
        this.zzFd = zzaVar.zzJL;
        this.zzFb = zzaVar2;
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
    }

    protected abstract zzie zzD(int i);

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() {
        synchronized (this.zzpK) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("AdRendererBackgroundTask started.");
            int i = this.zzFc.errorCode;
            try {
                zzh(SystemClock.elapsedRealtime());
            } catch (zza e) {
                int errorCode = e.getErrorCode();
                if (errorCode == 3 || errorCode == -1) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaG(e.getMessage());
                } else {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH(e.getMessage());
                }
                if (this.zzFd == null) {
                    this.zzFd = new AdResponseParcel(errorCode);
                } else {
                    this.zzFd = new AdResponseParcel(errorCode, this.zzFd.zzAU);
                }
                zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzgp.1
                    @Override // java.lang.Runnable
                    public void run() {
                        zzgp.this.onStop();
                    }
                });
                i = errorCode;
            }
            final zzie zzieVarZzD = zzD(i);
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzgp.2
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (zzgp.this.zzpK) {
                        zzgp.this.zzi(zzieVarZzD);
                    }
                }
            });
        }
    }

    protected abstract void zzh(long j) throws zza;

    protected void zzi(zzie zzieVar) {
        this.zzFb.zzb(zzieVar);
    }
}
