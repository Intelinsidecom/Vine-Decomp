package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzgd;
import com.google.android.gms.internal.zzge;

@zzha
/* loaded from: classes.dex */
public final class zzgi extends com.google.android.gms.dynamic.zzg<zzge> {
    private static final zzgi zzEZ = new zzgi();

    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    private zzgi() {
        super("com.google.android.gms.ads.InAppPurchaseManagerCreatorImpl");
    }

    private static boolean zzc(Activity activity) throws zza {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.purchase.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.purchase.useClientJar", false);
        }
        throw new zza("InAppPurchaseManager requires the useClientJar flag in intent extras.");
    }

    public static zzgd zze(Activity activity) {
        zzgd zzgdVarZzf;
        try {
            if (zzc(activity)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Using AdOverlay from the client jar.");
                zzgdVarZzf = new com.google.android.gms.ads.internal.purchase.zze(activity);
            } else {
                zzgdVarZzf = zzEZ.zzf(activity);
            }
            return zzgdVarZzf;
        } catch (zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(e.getMessage());
            return null;
        }
    }

    private zzgd zzf(Activity activity) {
        try {
            return zzgd.zza.zzQ(zzaA(activity).zzf(com.google.android.gms.dynamic.zze.zzB(activity)));
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote InAppPurchaseManager.", e);
            return null;
        } catch (zzg.zza e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote InAppPurchaseManager.", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.zzg
    /* renamed from: zzU, reason: merged with bridge method [inline-methods] */
    public zzge zzd(IBinder iBinder) {
        return zzge.zza.zzR(iBinder);
    }
}
