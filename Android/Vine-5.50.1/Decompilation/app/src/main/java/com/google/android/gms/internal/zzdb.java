package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzcp;
import com.google.android.gms.internal.zzcq;

@zzha
/* loaded from: classes.dex */
public class zzdb extends com.google.android.gms.dynamic.zzg<zzcq> {
    public zzdb() {
        super("com.google.android.gms.ads.NativeAdViewDelegateCreatorImpl");
    }

    private zzcp zzb(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        try {
            return zzcp.zza.zzu(zzaA(context).zza(com.google.android.gms.dynamic.zze.zzB(context), com.google.android.gms.dynamic.zze.zzB(frameLayout), com.google.android.gms.dynamic.zze.zzB(frameLayout2), 8298000));
        } catch (RemoteException | zzg.zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote NativeAdViewDelegate.", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.zzg
    /* renamed from: zzD, reason: merged with bridge method [inline-methods] */
    public zzcq zzd(IBinder iBinder) {
        return zzcq.zza.zzv(iBinder);
    }

    public zzcp zza(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        zzcp zzcpVarZzb;
        if (com.google.android.gms.ads.internal.client.zzl.zzcN().zzT(context) && (zzcpVarZzb = zzb(context, frameLayout, frameLayout2)) != null) {
            return zzcpVarZzb;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Using NativeAdViewDelegate from the client jar.");
        return new com.google.android.gms.ads.internal.formats.zzj(frameLayout, frameLayout2);
    }
}
