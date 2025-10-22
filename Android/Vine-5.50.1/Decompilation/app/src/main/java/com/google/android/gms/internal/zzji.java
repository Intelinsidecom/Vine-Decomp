package com.google.android.gms.internal;

import android.view.View;
import android.view.ViewTreeObserver;

@zzha
/* loaded from: classes.dex */
public class zzji {
    public static void zza(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        new zzjj(view, onGlobalLayoutListener).zzhu();
    }

    public static void zza(View view, ViewTreeObserver.OnScrollChangedListener onScrollChangedListener) {
        new zzjk(view, onScrollChangedListener).zzhu();
    }
}
