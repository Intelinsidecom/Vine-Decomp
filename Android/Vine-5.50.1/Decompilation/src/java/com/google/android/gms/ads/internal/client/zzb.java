package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzb extends zzn.zza {
    private final zza zztn;

    public zzb(zza zzaVar) {
        this.zztn = zzaVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzn
    public void onAdClicked() {
        this.zztn.onAdClicked();
    }
}
