package com.google.android.gms.internal;

import android.os.Build;

/* loaded from: classes.dex */
public final class zznx {
    @Deprecated
    public static boolean isAtLeastL() {
        return zzrW();
    }

    private static boolean zzcr(int i) {
        return Build.VERSION.SDK_INT >= i;
    }

    public static boolean zzrN() {
        return zzcr(11);
    }

    public static boolean zzrP() {
        return zzcr(13);
    }

    public static boolean zzrQ() {
        return zzcr(14);
    }

    public static boolean zzrR() {
        return zzcr(16);
    }

    public static boolean zzrS() {
        return zzcr(17);
    }

    public static boolean zzrT() {
        return zzcr(18);
    }

    public static boolean zzrU() {
        return zzcr(19);
    }

    public static boolean zzrV() {
        return zzcr(20);
    }

    public static boolean zzrW() {
        return zzcr(21);
    }

    public static boolean zzrX() {
        return zzcr(23);
    }
}
