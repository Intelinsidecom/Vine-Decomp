package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;

/* loaded from: classes2.dex */
public class zznk {
    public static boolean zzj(Context context, String str) {
        try {
            return (context.getPackageManager().getApplicationInfo(str, 0).flags & 2097152) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean zzka() {
        return com.google.android.gms.common.internal.zzd.zzaiU && zzmt.isInitialized() && zzmt.zzpE() == Process.myUid();
    }
}
