package com.google.android.gms.internal;

import android.content.res.Configuration;
import android.content.res.Resources;

/* loaded from: classes2.dex */
public final class zznp {
    public static boolean zzb(Resources resources) {
        if (resources == null) {
            return false;
        }
        return (zznx.zzrN() && ((resources.getConfiguration().screenLayout & 15) > 3)) || zzc(resources);
    }

    private static boolean zzc(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        return zznx.zzrP() && (configuration.screenLayout & 15) <= 3 && configuration.smallestScreenWidthDp >= 600;
    }
}
