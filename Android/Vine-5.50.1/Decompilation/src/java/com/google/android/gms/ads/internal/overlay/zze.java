package com.google.android.gms.ads.internal.overlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zznx;

@zzha
/* loaded from: classes.dex */
public class zze {
    public void zza(Context context, AdOverlayInfoParcel adOverlayInfoParcel) {
        zza(context, adOverlayInfoParcel, true);
    }

    public void zza(Context context, AdOverlayInfoParcel adOverlayInfoParcel, boolean z) {
        if (adOverlayInfoParcel.zzDI == 4 && adOverlayInfoParcel.zzDB == null) {
            if (adOverlayInfoParcel.zzDA != null) {
                adOverlayInfoParcel.zzDA.onAdClicked();
            }
            com.google.android.gms.ads.internal.zzp.zzbu().zza(context, adOverlayInfoParcel.zzDz, adOverlayInfoParcel.zzDH);
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(context, "com.google.android.gms.ads.AdActivity");
        intent.putExtra("com.google.android.gms.ads.internal.overlay.useClientJar", adOverlayInfoParcel.zzqR.zzLH);
        intent.putExtra("shouldCallOnOverlayOpened", z);
        AdOverlayInfoParcel.zza(intent, adOverlayInfoParcel);
        if (!zznx.isAtLeastL()) {
            intent.addFlags(524288);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        com.google.android.gms.ads.internal.zzp.zzbx().zzb(context, intent);
    }
}
