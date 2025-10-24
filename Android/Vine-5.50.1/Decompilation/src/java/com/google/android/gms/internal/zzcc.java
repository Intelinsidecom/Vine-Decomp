package com.google.android.gms.internal;

import android.text.TextUtils;

@zzha
/* loaded from: classes.dex */
public class zzcc {
    public zzcb zza(zzca zzcaVar) {
        if (zzcaVar == null) {
            throw new IllegalArgumentException("CSI configuration can't be null. ");
        }
        if (!zzcaVar.zzdn()) {
            com.google.android.gms.ads.internal.util.client.zzb.v("CsiReporterFactory: CSI is not enabled. No CSI reporter created.");
            return null;
        }
        if (zzcaVar.getContext() == null) {
            throw new IllegalArgumentException("Context can't be null. Please set up context in CsiConfiguration.");
        }
        if (TextUtils.isEmpty(zzcaVar.zzbY())) {
            throw new IllegalArgumentException("AfmaVersion can't be null or empty. Please set up afmaVersion in CsiConfiguration.");
        }
        return new zzcb(zzcaVar.getContext(), zzcaVar.zzbY(), zzcaVar.zzdo(), zzcaVar.zzdp());
    }
}
