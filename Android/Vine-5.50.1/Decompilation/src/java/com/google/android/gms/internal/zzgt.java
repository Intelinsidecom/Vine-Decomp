package com.google.android.gms.internal;

import android.content.Context;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.internal.zzgp;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;

@zzha
/* loaded from: classes.dex */
public class zzgt extends zzgp {
    private zzen zzBf;
    private zzel zzFx;
    protected zzer zzFy;
    private final zzch zzoU;
    private zzew zzpd;

    zzgt(Context context, zzie.zza zzaVar, zzew zzewVar, zzgq.zza zzaVar2, zzch zzchVar) {
        super(context, zzaVar, zzaVar2);
        this.zzpd = zzewVar;
        this.zzBf = zzaVar.zzJF;
        this.zzoU = zzchVar;
    }

    @Override // com.google.android.gms.internal.zzgp, com.google.android.gms.internal.zzil
    public void onStop() {
        synchronized (this.zzFf) {
            super.onStop();
            if (this.zzFx != null) {
                this.zzFx.cancel();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzgp
    protected zzie zzD(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzFc.zzJK;
        return new zzie(adRequestInfoParcel.zzGq, null, this.zzFd.zzAQ, i, this.zzFd.zzAR, this.zzFd.zzGP, this.zzFd.orientation, this.zzFd.zzAU, adRequestInfoParcel.zzGt, this.zzFd.zzGN, this.zzFy != null ? this.zzFy.zzBp : null, this.zzFy != null ? this.zzFy.zzBq : null, this.zzFy != null ? this.zzFy.zzBr : AdMobAdapter.class.getName(), this.zzBf, this.zzFy != null ? this.zzFy.zzBs : null, this.zzFd.zzGO, this.zzFc.zzqV, this.zzFd.zzGM, this.zzFc.zzJH, this.zzFd.zzGR, this.zzFd.zzGS, this.zzFc.zzJE, null);
    }

    @Override // com.google.android.gms.internal.zzgp
    protected void zzh(long j) throws zzgp.zza {
        synchronized (this.zzFf) {
            this.zzFx = zzi(j);
        }
        this.zzFy = this.zzFx.zzc(this.zzBf.zzAO);
        switch (this.zzFy.zzBo) {
            case 0:
                return;
            case 1:
                throw new zzgp.zza("No fill from any mediation ad networks.", 3);
            default:
                throw new zzgp.zza("Unexpected mediation result: " + this.zzFy.zzBo, 0);
        }
    }

    zzel zzi(long j) {
        return this.zzBf.zzAX != -1 ? new zzet(this.mContext, this.zzFc.zzJK, this.zzpd, this.zzBf, this.zzFd.zztY, j, zzbz.zzwC.get().longValue(), 2) : new zzeu(this.mContext, this.zzFc.zzJK, this.zzpd, this.zzBf, this.zzFd.zztY, j, zzbz.zzwC.get().longValue(), this.zzoU);
    }
}
