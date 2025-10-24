package com.google.android.gms.internal;

import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.internal.zzgc;

@zzha
/* loaded from: classes.dex */
public final class zzgh extends zzgc.zza {
    private final InAppPurchaseListener zzuz;

    public zzgh(InAppPurchaseListener inAppPurchaseListener) {
        this.zzuz = inAppPurchaseListener;
    }

    @Override // com.google.android.gms.internal.zzgc
    public void zza(zzgb zzgbVar) {
        this.zzuz.onInAppPurchaseRequested(new zzgk(zzgbVar));
    }
}
