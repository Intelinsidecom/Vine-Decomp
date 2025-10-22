package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;

@zzha
/* loaded from: classes.dex */
public abstract class zzhk {
    public abstract void zza(Context context, zzhe zzheVar, VersionInfoParcel versionInfoParcel);

    protected void zze(zzhe zzheVar) {
        zzheVar.zzgq();
        if (zzheVar.zzgo() != null) {
            zzheVar.zzgo().release();
        }
    }
}
