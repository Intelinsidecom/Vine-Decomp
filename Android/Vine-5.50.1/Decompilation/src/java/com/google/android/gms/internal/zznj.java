package com.google.android.gms.internal;

import android.content.Context;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public final class zznj {
    private static Pattern zzamj = null;

    public static boolean zzav(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    }

    public static int zzcp(int i) {
        return i / 1000;
    }
}
