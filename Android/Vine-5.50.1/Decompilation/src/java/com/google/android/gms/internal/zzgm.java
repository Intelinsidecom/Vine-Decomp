package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzjo;
import java.util.concurrent.atomic.AtomicBoolean;

@zzha
/* loaded from: classes.dex */
public abstract class zzgm implements zzir<Void>, zzjo.zza {
    protected final Context mContext;
    protected final zzgq.zza zzFb;
    protected final zzie.zza zzFc;
    protected AdResponseParcel zzFd;
    private Runnable zzFe;
    protected final Object zzFf = new Object();
    private AtomicBoolean zzFg = new AtomicBoolean(true);
    protected final zzjn zzps;

    protected zzgm(Context context, zzie.zza zzaVar, zzjn zzjnVar, zzgq.zza zzaVar2) {
        this.mContext = context;
        this.zzFc = zzaVar;
        this.zzFd = this.zzFc.zzJL;
        this.zzps = zzjnVar;
        this.zzFb = zzaVar2;
    }

    private zzie zzD(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzFc.zzJK;
        return new zzie(adRequestInfoParcel.zzGq, this.zzps, this.zzFd.zzAQ, i, this.zzFd.zzAR, this.zzFd.zzGP, this.zzFd.orientation, this.zzFd.zzAU, adRequestInfoParcel.zzGt, this.zzFd.zzGN, null, null, null, null, null, this.zzFd.zzGO, this.zzFc.zzqV, this.zzFd.zzGM, this.zzFc.zzJH, this.zzFd.zzGR, this.zzFd.zzGS, this.zzFc.zzJE, null);
    }

    @Override // com.google.android.gms.internal.zzir
    public void cancel() {
        if (this.zzFg.getAndSet(false)) {
            this.zzps.stopLoading();
            com.google.android.gms.ads.internal.zzp.zzbz().zzf(this.zzps);
            zzC(-1);
            zzip.zzKO.removeCallbacks(this.zzFe);
        }
    }

    protected void zzC(int i) {
        if (i != -2) {
            this.zzFd = new AdResponseParcel(i, this.zzFd.zzAU);
        }
        this.zzFb.zzb(zzD(i));
    }

    @Override // com.google.android.gms.internal.zzjo.zza
    public void zza(zzjn zzjnVar, boolean z) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("WebView finished loading.");
        if (this.zzFg.getAndSet(false)) {
            zzC(z ? zzfQ() : -1);
            zzip.zzKO.removeCallbacks(this.zzFe);
        }
    }

    @Override // com.google.android.gms.internal.zzir
    /* renamed from: zzfO, reason: merged with bridge method [inline-methods] */
    public final Void zzfR() {
        com.google.android.gms.common.internal.zzx.zzcx("Webview render task needs to be called on UI thread.");
        this.zzFe = new Runnable() { // from class: com.google.android.gms.internal.zzgm.1
            @Override // java.lang.Runnable
            public void run() {
                if (zzgm.this.zzFg.get()) {
                    com.google.android.gms.ads.internal.util.client.zzb.e("Timed out waiting for WebView to finish loading.");
                    zzgm.this.cancel();
                }
            }
        };
        zzip.zzKO.postDelayed(this.zzFe, zzbz.zzwC.get().longValue());
        zzfP();
        return null;
    }

    protected abstract void zzfP();

    protected int zzfQ() {
        return -2;
    }
}
