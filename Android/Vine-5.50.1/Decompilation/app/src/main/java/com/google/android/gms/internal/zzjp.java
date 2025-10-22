package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;

@zzha
/* loaded from: classes.dex */
public class zzjp {
    public zzjn zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzanVar, VersionInfoParcel versionInfoParcel) {
        return zza(context, adSizeParcel, z, z2, zzanVar, versionInfoParcel, null, null);
    }

    public zzjn zza(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzanVar, VersionInfoParcel versionInfoParcel, zzch zzchVar, com.google.android.gms.ads.internal.zzd zzdVar) {
        zzjq zzjqVar = new zzjq(zzjr.zzb(context, adSizeParcel, z, z2, zzanVar, versionInfoParcel, zzchVar, zzdVar));
        zzjqVar.setWebViewClient(com.google.android.gms.ads.internal.zzp.zzbz().zzb(zzjqVar, z2));
        zzjqVar.setWebChromeClient(com.google.android.gms.ads.internal.zzp.zzbz().zzh(zzjqVar));
        return zzjqVar;
    }
}
