package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzfu;
import com.google.android.gms.internal.zzfv;

@zzha
/* loaded from: classes.dex */
public final class zzft extends com.google.android.gms.dynamic.zzg<zzfv> {
    private static final zzft zzEo = new zzft();

    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    private zzft() {
        super("com.google.android.gms.ads.AdOverlayCreatorImpl");
    }

    public static zzfu zzb(Activity activity) {
        zzfu zzfuVarZzd;
        try {
            if (zzc(activity)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Using AdOverlay from the client jar.");
                zzfuVarZzd = new com.google.android.gms.ads.internal.overlay.zzd(activity);
            } else {
                zzfuVarZzd = zzEo.zzd(activity);
            }
            return zzfuVarZzd;
        } catch (zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(e.getMessage());
            return null;
        }
    }

    private static boolean zzc(Activity activity) throws zza {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.overlay.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.overlay.useClientJar", false);
        }
        throw new zza("Ad overlay requires the useClientJar flag in intent extras.");
    }

    private zzfu zzd(Activity activity) {
        try {
            return zzfu.zza.zzL(zzaA(activity).zze(com.google.android.gms.dynamic.zze.zzB(activity)));
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote AdOverlay.", e);
            return null;
        } catch (zzg.zza e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote AdOverlay.", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.zzg
    /* renamed from: zzK, reason: merged with bridge method [inline-methods] */
    public zzfv zzd(IBinder iBinder) {
        return zzfv.zza.zzM(iBinder);
    }
}
