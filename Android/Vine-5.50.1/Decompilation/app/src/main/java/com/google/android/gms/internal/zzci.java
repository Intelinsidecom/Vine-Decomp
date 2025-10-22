package com.google.android.gms.internal;

import android.view.View;
import com.google.android.gms.internal.zzck;

@zzha
/* loaded from: classes.dex */
public final class zzci extends zzck.zza {
    private final com.google.android.gms.ads.internal.zzg zzxg;
    private final String zzxh;
    private final String zzxi;

    public zzci(com.google.android.gms.ads.internal.zzg zzgVar, String str, String str2) {
        this.zzxg = zzgVar;
        this.zzxh = str;
        this.zzxi = str2;
    }

    @Override // com.google.android.gms.internal.zzck
    public String getContent() {
        return this.zzxi;
    }

    @Override // com.google.android.gms.internal.zzck
    public void recordClick() {
        this.zzxg.recordClick();
    }

    @Override // com.google.android.gms.internal.zzck
    public void recordImpression() {
        this.zzxg.recordImpression();
    }

    @Override // com.google.android.gms.internal.zzck
    public void zza(com.google.android.gms.dynamic.zzd zzdVar) {
        if (zzdVar == null) {
            return;
        }
        this.zzxg.zzc((View) com.google.android.gms.dynamic.zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.internal.zzck
    public String zzdy() {
        return this.zzxh;
    }
}
