package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.internal.zzcy;

@zzha
/* loaded from: classes.dex */
public class zzdd extends zzcy.zza {
    private final NativeContentAd.OnContentAdLoadedListener zzyv;

    public zzdd(NativeContentAd.OnContentAdLoadedListener onContentAdLoadedListener) {
        this.zzyv = onContentAdLoadedListener;
    }

    @Override // com.google.android.gms.internal.zzcy
    public void zza(zzct zzctVar) {
        this.zzyv.onContentAdLoaded(zzb(zzctVar));
    }

    zzcu zzb(zzct zzctVar) {
        return new zzcu(zzctVar);
    }
}
