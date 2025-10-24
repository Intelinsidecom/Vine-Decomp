package com.google.android.gms.ads.internal.client;

import com.google.android.gms.internal.zzdb;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzl {
    private static final Object zzqf = new Object();
    private static zzl zzud;
    private final com.google.android.gms.ads.internal.util.client.zza zzue = new com.google.android.gms.ads.internal.util.client.zza();
    private final zze zzuf = new zze();
    private final zzad zzug = new zzad();
    private final zzdb zzuh = new zzdb();
    private final com.google.android.gms.ads.internal.reward.client.zzf zzui = new com.google.android.gms.ads.internal.reward.client.zzf();

    static {
        zza(new zzl());
    }

    protected zzl() {
    }

    protected static void zza(zzl zzlVar) {
        synchronized (zzqf) {
            zzud = zzlVar;
        }
    }

    private static zzl zzcM() {
        zzl zzlVar;
        synchronized (zzqf) {
            zzlVar = zzud;
        }
        return zzlVar;
    }

    public static com.google.android.gms.ads.internal.util.client.zza zzcN() {
        return zzcM().zzue;
    }

    public static zze zzcO() {
        return zzcM().zzuf;
    }

    public static zzdb zzcQ() {
        return zzcM().zzuh;
    }
}
