package com.google.android.gms.ads.internal.util.client;

import android.util.Log;
import com.google.ads.AdRequest;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzb {
    public static void e(String msg) {
        if (zzQ(6)) {
            Log.e(AdRequest.LOGTAG, msg);
        }
    }

    public static void v(String msg) {
        if (zzQ(2)) {
            Log.v(AdRequest.LOGTAG, msg);
        }
    }

    public static boolean zzQ(int i) {
        return (i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i)) && (i != 2 || zzhs());
    }

    public static void zza(String str, Throwable th) {
        if (zzQ(3)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzaF(String str) {
        if (zzQ(3)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaG(String str) {
        if (zzQ(4)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void zzaH(String str) {
        if (zzQ(5)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }

    public static void zzb(String str, Throwable th) {
        if (zzQ(6)) {
            Log.e(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzc(String str, Throwable th) {
        if (zzQ(4)) {
            Log.i(AdRequest.LOGTAG, str, th);
        }
    }

    public static void zzd(String str, Throwable th) {
        if (zzQ(5)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }

    public static boolean zzhs() {
        return zzbz.zzwp.get().booleanValue();
    }
}
