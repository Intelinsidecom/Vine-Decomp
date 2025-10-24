package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.internal.client.zzo;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzc extends zzo.zza {
    private final AdListener zzto;

    public zzc(AdListener adListener) {
        this.zzto = adListener;
    }

    @Override // com.google.android.gms.ads.internal.client.zzo
    public void onAdClosed() {
        this.zzto.onAdClosed();
    }

    @Override // com.google.android.gms.ads.internal.client.zzo
    public void onAdFailedToLoad(int errorCode) {
        this.zzto.onAdFailedToLoad(errorCode);
    }

    @Override // com.google.android.gms.ads.internal.client.zzo
    public void onAdLeftApplication() {
        this.zzto.onAdLeftApplication();
    }

    @Override // com.google.android.gms.ads.internal.client.zzo
    public void onAdLoaded() {
        this.zzto.onAdLoaded();
    }

    @Override // com.google.android.gms.ads.internal.client.zzo
    public void onAdOpened() {
        this.zzto.onAdOpened();
    }
}
