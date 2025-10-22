package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.ads.internal.request.zzd;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjg;

@zzha
/* loaded from: classes.dex */
public final class zzc {

    public interface zza {
        void zzb(AdResponseParcel adResponseParcel);
    }

    interface zzb {
        boolean zza(VersionInfoParcel versionInfoParcel);
    }

    public static zzir zza(final Context context, VersionInfoParcel versionInfoParcel, zzjg<AdRequestInfoParcel> zzjgVar, zza zzaVar) {
        return zza(context, versionInfoParcel, zzjgVar, zzaVar, new zzb() { // from class: com.google.android.gms.ads.internal.request.zzc.1
            @Override // com.google.android.gms.ads.internal.request.zzc.zzb
            public boolean zza(VersionInfoParcel versionInfoParcel2) {
                return versionInfoParcel2.zzLH || (GooglePlayServicesUtil.zzao(context) && !zzbz.zzvG.get().booleanValue());
            }
        });
    }

    static zzir zza(Context context, VersionInfoParcel versionInfoParcel, zzjg<AdRequestInfoParcel> zzjgVar, zza zzaVar, zzb zzbVar) {
        return zzbVar.zza(versionInfoParcel) ? zza(context, zzjgVar, zzaVar) : zzb(context, versionInfoParcel, zzjgVar, zzaVar);
    }

    private static zzir zza(Context context, zzjg<AdRequestInfoParcel> zzjgVar, zza zzaVar) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Fetching ad response from local ad request service.");
        zzd.zza zzaVar2 = new zzd.zza(context, zzjgVar, zzaVar);
        zzaVar2.zzfR();
        return zzaVar2;
    }

    private static zzir zzb(Context context, VersionInfoParcel versionInfoParcel, zzjg<AdRequestInfoParcel> zzjgVar, zza zzaVar) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Fetching ad response from remote ad request service.");
        if (com.google.android.gms.ads.internal.client.zzl.zzcN().zzT(context)) {
            return new zzd.zzb(context, versionInfoParcel, zzjgVar, zzaVar);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Failed to connect to remote ad request service.");
        return null;
    }
}
