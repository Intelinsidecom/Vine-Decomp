package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.internal.client.zzaa;

/* loaded from: classes.dex */
public final class InterstitialAd {
    private final zzaa zzoA;

    public InterstitialAd(Context context) {
        this.zzoA = new zzaa(context);
    }

    public void loadAd(AdRequest adRequest) {
        this.zzoA.zza(adRequest.zzaG());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setAdListener(AdListener adListener) {
        this.zzoA.setAdListener(adListener);
        if (adListener != 0 && (adListener instanceof com.google.android.gms.ads.internal.client.zza)) {
            this.zzoA.zza((com.google.android.gms.ads.internal.client.zza) adListener);
        } else if (adListener == 0) {
            this.zzoA.zza((com.google.android.gms.ads.internal.client.zza) null);
        }
    }

    public void setAdUnitId(String adUnitId) {
        this.zzoA.setAdUnitId(adUnitId);
    }

    public void show() {
        this.zzoA.show();
    }
}
