package com.google.android.gms.internal;

import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.internal.zzcx;

@zzha
/* loaded from: classes.dex */
public class zzdc extends zzcx.zza {
    private final NativeAppInstallAd.OnAppInstallAdLoadedListener zzyu;

    public zzdc(NativeAppInstallAd.OnAppInstallAdLoadedListener onAppInstallAdLoadedListener) {
        this.zzyu = onAppInstallAdLoadedListener;
    }

    @Override // com.google.android.gms.internal.zzcx
    public void zza(zzcr zzcrVar) {
        this.zzyu.onAppInstallAdLoaded(zzb(zzcrVar));
    }

    zzcs zzb(zzcr zzcrVar) {
        return new zzcs(zzcrVar);
    }
}
