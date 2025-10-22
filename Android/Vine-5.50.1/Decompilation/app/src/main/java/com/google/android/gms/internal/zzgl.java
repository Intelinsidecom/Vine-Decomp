package com.google.android.gms.internal;

import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.internal.zzgg;

@zzha
/* loaded from: classes.dex */
public final class zzgl extends zzgg.zza {
    private final PlayStorePurchaseListener zzuA;

    public zzgl(PlayStorePurchaseListener playStorePurchaseListener) {
        this.zzuA = playStorePurchaseListener;
    }

    @Override // com.google.android.gms.internal.zzgg
    public boolean isValidPurchase(String productId) {
        return this.zzuA.isValidPurchase(productId);
    }

    @Override // com.google.android.gms.internal.zzgg
    public void zza(zzgf zzgfVar) {
        this.zzuA.onInAppPurchaseFinished(new zzgj(zzgfVar));
    }
}
