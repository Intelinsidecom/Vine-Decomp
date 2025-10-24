package com.google.android.gms.internal;

import android.content.Context;
import android.content.MutableContextWrapper;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;

@zzha
/* loaded from: classes.dex */
public class zzea {
    private final VersionInfoParcel zzpI;
    private final com.google.android.gms.ads.internal.zzd zzpc;
    private final zzew zzpd;
    private MutableContextWrapper zzzz;

    zzea(Context context, zzew zzewVar, VersionInfoParcel versionInfoParcel, com.google.android.gms.ads.internal.zzd zzdVar) {
        this.zzzz = new MutableContextWrapper(context.getApplicationContext());
        this.zzpd = zzewVar;
        this.zzpI = versionInfoParcel;
        this.zzpc = zzdVar;
    }

    public com.google.android.gms.ads.internal.zzk zzac(String str) {
        return new com.google.android.gms.ads.internal.zzk(this.zzzz, new AdSizeParcel(), str, this.zzpd, this.zzpI, this.zzpc);
    }

    public zzea zzdV() {
        return new zzea(this.zzzz.getBaseContext(), this.zzpd, this.zzpI, this.zzpc);
    }

    MutableContextWrapper zzdW() {
        return this.zzzz;
    }
}
