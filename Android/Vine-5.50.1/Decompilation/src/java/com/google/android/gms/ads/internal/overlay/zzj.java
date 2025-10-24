package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzjn;
import com.google.android.gms.internal.zznx;

/* loaded from: classes.dex */
public abstract class zzj {
    public abstract zzi zza(Context context, zzjn zzjnVar, int i, zzch zzchVar, zzcf zzcfVar);

    protected boolean zzx(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return zznx.zzrQ() && (applicationInfo == null || applicationInfo.targetSdkVersion >= 11);
    }
}
