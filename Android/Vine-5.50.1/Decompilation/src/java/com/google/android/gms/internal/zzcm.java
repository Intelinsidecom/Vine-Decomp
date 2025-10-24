package com.google.android.gms.internal;

import com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener;
import com.google.android.gms.internal.zzcl;

@zzha
/* loaded from: classes.dex */
public final class zzcm extends zzcl.zza {
    private final OnCustomRenderedAdLoadedListener zzuB;

    public zzcm(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zzuB = onCustomRenderedAdLoadedListener;
    }

    @Override // com.google.android.gms.internal.zzcl
    public void zza(zzck zzckVar) {
        this.zzuB.onCustomRenderedAdLoaded(new zzcj(zzckVar));
    }
}
