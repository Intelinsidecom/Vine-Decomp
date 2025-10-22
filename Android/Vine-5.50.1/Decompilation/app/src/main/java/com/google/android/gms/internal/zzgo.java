package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;

@zzha
/* loaded from: classes.dex */
public class zzgo extends zzgm {
    private zzgn zzFr;

    zzgo(Context context, zzie.zza zzaVar, zzjn zzjnVar, zzgq.zza zzaVar2) {
        super(context, zzaVar, zzjnVar, zzaVar2);
    }

    @Override // com.google.android.gms.internal.zzgm
    protected void zzfP() {
        int i;
        int i2;
        AdSizeParcel adSizeParcelZzaP = this.zzps.zzaP();
        if (adSizeParcelZzaP.zztW) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            i = displayMetrics.widthPixels;
            i2 = displayMetrics.heightPixels;
        } else {
            i = adSizeParcelZzaP.widthPixels;
            i2 = adSizeParcelZzaP.heightPixels;
        }
        this.zzFr = new zzgn(this, this.zzps, i, i2);
        this.zzps.zzhC().zza(this);
        this.zzFr.zza(this.zzFd);
    }

    @Override // com.google.android.gms.internal.zzgm
    protected int zzfQ() {
        if (!this.zzFr.zzfU()) {
            return !this.zzFr.zzfV() ? 2 : -2;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Ad-Network indicated no fill with passback URL.");
        return 3;
    }
}
