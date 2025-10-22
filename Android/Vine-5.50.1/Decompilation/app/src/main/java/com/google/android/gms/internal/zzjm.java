package com.google.android.gms.internal;

import android.content.Context;
import android.view.ViewGroup;

@zzha
/* loaded from: classes.dex */
public class zzjm {
    private final Context mContext;
    private com.google.android.gms.ads.internal.overlay.zzk zzEn;
    private final ViewGroup zzMd;
    private final zzjn zzps;

    public zzjm(Context context, ViewGroup viewGroup, zzjn zzjnVar) {
        this(context, viewGroup, zzjnVar, null);
    }

    zzjm(Context context, ViewGroup viewGroup, zzjn zzjnVar, com.google.android.gms.ads.internal.overlay.zzk zzkVar) {
        this.mContext = context;
        this.zzMd = viewGroup;
        this.zzps = zzjnVar;
        this.zzEn = zzkVar;
    }

    public void onDestroy() {
        com.google.android.gms.common.internal.zzx.zzcx("onDestroy must be called from the UI thread.");
        if (this.zzEn != null) {
            this.zzEn.destroy();
        }
    }

    public void onPause() {
        com.google.android.gms.common.internal.zzx.zzcx("onPause must be called from the UI thread.");
        if (this.zzEn != null) {
            this.zzEn.pause();
        }
    }

    public void zza(int i, int i2, int i3, int i4, int i5) {
        if (this.zzEn != null) {
            return;
        }
        zzcd.zza(this.zzps.zzhL().zzdt(), this.zzps.zzhK(), "vpr");
        this.zzEn = new com.google.android.gms.ads.internal.overlay.zzk(this.mContext, this.zzps, i5, this.zzps.zzhL().zzdt(), zzcd.zzb(this.zzps.zzhL().zzdt()));
        this.zzMd.addView(this.zzEn, 0, new ViewGroup.LayoutParams(-1, -1));
        this.zzEn.zzd(i, i2, i3, i4);
        this.zzps.zzhC().zzG(false);
    }

    public void zze(int i, int i2, int i3, int i4) {
        com.google.android.gms.common.internal.zzx.zzcx("The underlay may only be modified from the UI thread.");
        if (this.zzEn != null) {
            this.zzEn.zzd(i, i2, i3, i4);
        }
    }

    public com.google.android.gms.ads.internal.overlay.zzk zzhv() {
        com.google.android.gms.common.internal.zzx.zzcx("getAdVideoUnderlay must be called from the UI thread.");
        return this.zzEn;
    }
}
