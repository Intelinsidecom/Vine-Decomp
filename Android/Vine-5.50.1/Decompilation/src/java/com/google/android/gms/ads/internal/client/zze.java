package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzs;
import com.google.android.gms.ads.internal.client.zzt;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzeg;
import com.google.android.gms.internal.zzev;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zze extends com.google.android.gms.dynamic.zzg<zzt> {
    public zze() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    private zzs zza(Context context, AdSizeParcel adSizeParcel, String str, zzev zzevVar, int i) {
        try {
            return zzs.zza.zzk(zzaA(context).zza(com.google.android.gms.dynamic.zze.zzB(context), adSizeParcel, str, zzevVar, 8298000, i));
        } catch (RemoteException | zzg.zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zza("Could not create remote AdManager.", e);
            return null;
        }
    }

    public zzs zza(Context context, AdSizeParcel adSizeParcel, String str, zzev zzevVar) {
        zzs zzsVarZza;
        if (zzl.zzcN().zzT(context) && (zzsVarZza = zza(context, adSizeParcel, str, zzevVar, 1)) != null) {
            return zzsVarZza;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Using BannerAdManager from the client jar.");
        return new com.google.android.gms.ads.internal.zzf(context, adSizeParcel, str, zzevVar, new VersionInfoParcel(8298000, 8298000, true), com.google.android.gms.ads.internal.zzd.zzbf());
    }

    public zzs zzb(Context context, AdSizeParcel adSizeParcel, String str, zzev zzevVar) {
        zzs zzsVarZza;
        if (zzl.zzcN().zzT(context) && (zzsVarZza = zza(context, adSizeParcel, str, zzevVar, 2)) != null) {
            return zzsVarZza;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Using InterstitialAdManager from the client jar.");
        VersionInfoParcel versionInfoParcel = new VersionInfoParcel(8298000, 8298000, true);
        return zzbz.zzwj.get().booleanValue() ? new zzeg(context, str, zzevVar, versionInfoParcel, com.google.android.gms.ads.internal.zzd.zzbf()) : new com.google.android.gms.ads.internal.zzk(context, adSizeParcel, str, zzevVar, versionInfoParcel, com.google.android.gms.ads.internal.zzd.zzbf());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.zzg
    /* renamed from: zze, reason: merged with bridge method [inline-methods] */
    public zzt zzd(IBinder iBinder) {
        return zzt.zza.zzl(iBinder);
    }
}
