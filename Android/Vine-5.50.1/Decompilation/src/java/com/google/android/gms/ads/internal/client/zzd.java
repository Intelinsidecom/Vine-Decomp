package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzr;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzev;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzd extends com.google.android.gms.dynamic.zzg<zzr> {
    private static final zzd zztp = new zzd();

    private zzd() {
        super("com.google.android.gms.ads.AdLoaderBuilderCreatorImpl");
    }

    public static zzq zza(Context context, String str, zzev zzevVar) {
        zzq zzqVarZzb;
        if (zzl.zzcN().zzT(context) && (zzqVarZzb = zztp.zzb(context, str, zzevVar)) != null) {
            return zzqVarZzb;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Using AdLoader from the client jar.");
        return new com.google.android.gms.ads.internal.zzj(context, str, zzevVar, new VersionInfoParcel(8298000, 8298000, true));
    }

    private zzq zzb(Context context, String str, zzev zzevVar) {
        try {
            return zzq.zza.zzi(zzaA(context).zza(com.google.android.gms.dynamic.zze.zzB(context), str, zzevVar, 8298000));
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote builder for AdLoader.", e);
            return null;
        } catch (zzg.zza e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not create remote builder for AdLoader.", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.zzg
    /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
    public zzr zzd(IBinder iBinder) {
        return zzr.zza.zzj(iBinder);
    }
}
