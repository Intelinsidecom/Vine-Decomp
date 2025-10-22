package com.google.android.gms.internal;

import android.os.StrictMode;
import java.util.concurrent.Callable;

@zzha
/* loaded from: classes.dex */
public class zziz {
    public static <T> T zzb(Callable<T> callable) {
        T tCall;
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(threadPolicy).permitDiskReads().permitDiskWrites().build());
            tCall = callable.call();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unexpected exception.", th);
            com.google.android.gms.ads.internal.zzp.zzbA().zzb(th, true);
            tCall = null;
        } finally {
            StrictMode.setThreadPolicy(threadPolicy);
        }
        return tCall;
    }
}
