package com.google.android.gms.internal;

import android.app.Activity;
import android.view.ViewTreeObserver;

@zzha
/* loaded from: classes.dex */
public final class zzja {
    private boolean zzLA;
    private boolean zzLB;
    private ViewTreeObserver.OnGlobalLayoutListener zzLC;
    private ViewTreeObserver.OnScrollChangedListener zzLD;
    private Activity zzLy;
    private boolean zzLz;

    public zzja(Activity activity, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener, ViewTreeObserver.OnScrollChangedListener onScrollChangedListener) {
        this.zzLy = activity;
        this.zzLC = onGlobalLayoutListener;
        this.zzLD = onScrollChangedListener;
    }

    private void zzho() {
        if (this.zzLy == null || this.zzLz) {
            return;
        }
        if (this.zzLC != null) {
            com.google.android.gms.ads.internal.zzp.zzbx().zza(this.zzLy, this.zzLC);
        }
        if (this.zzLD != null) {
            com.google.android.gms.ads.internal.zzp.zzbx().zza(this.zzLy, this.zzLD);
        }
        this.zzLz = true;
    }

    private void zzhp() {
        if (this.zzLy != null && this.zzLz) {
            if (this.zzLC != null) {
                com.google.android.gms.ads.internal.zzp.zzbz().zzb(this.zzLy, this.zzLC);
            }
            if (this.zzLD != null) {
                com.google.android.gms.ads.internal.zzp.zzbx().zzb(this.zzLy, this.zzLD);
            }
            this.zzLz = false;
        }
    }

    public void onAttachedToWindow() {
        this.zzLA = true;
        if (this.zzLB) {
            zzho();
        }
    }

    public void onDetachedFromWindow() {
        this.zzLA = false;
        zzhp();
    }

    public void zzhm() {
        this.zzLB = true;
        if (this.zzLA) {
            zzho();
        }
    }

    public void zzhn() {
        this.zzLB = false;
        zzhp();
    }

    public void zzk(Activity activity) {
        this.zzLy = activity;
    }
}
