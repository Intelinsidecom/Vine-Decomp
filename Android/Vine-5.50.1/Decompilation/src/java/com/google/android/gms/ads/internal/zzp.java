package com.google.android.gms.ads.internal;

import android.os.Build;
import com.google.android.gms.internal.zzbw;
import com.google.android.gms.internal.zzbx;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzcc;
import com.google.android.gms.internal.zzdv;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzes;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzhj;
import com.google.android.gms.internal.zzig;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zziq;
import com.google.android.gms.internal.zziv;
import com.google.android.gms.internal.zzjp;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzno;

@zzha
/* loaded from: classes.dex */
public class zzp {
    private static final Object zzqf = new Object();
    private static zzp zzqu;
    private final com.google.android.gms.ads.internal.request.zza zzqv = new com.google.android.gms.ads.internal.request.zza();
    private final com.google.android.gms.ads.internal.overlay.zza zzqw = new com.google.android.gms.ads.internal.overlay.zza();
    private final com.google.android.gms.ads.internal.overlay.zze zzqx = new com.google.android.gms.ads.internal.overlay.zze();
    private final zzgq zzqy = new zzgq();
    private final zzip zzqz = new zzip();
    private final zzjp zzqA = new zzjp();
    private final zziq zzqB = zziq.zzP(Build.VERSION.SDK_INT);
    private final zzig zzqC = new zzig(this.zzqz);
    private final zznl zzqD = new zzno();
    private final zzcc zzqE = new zzcc();
    private final zzhj zzqF = new zzhj();
    private final zzbx zzqG = new zzbx();
    private final zzbw zzqH = new zzbw();
    private final zzby zzqI = new zzby();
    private final com.google.android.gms.ads.internal.purchase.zzi zzqJ = new com.google.android.gms.ads.internal.purchase.zzi();
    private final zzed zzqK = new zzed();
    private final zziv zzqL = new zziv();
    private final zzes zzqM = new zzes();
    private final zzdv zzqN = new zzdv();

    static {
        zza(new zzp());
    }

    protected zzp() {
    }

    protected static void zza(zzp zzpVar) {
        synchronized (zzqf) {
            zzqu = zzpVar;
        }
    }

    public static zzig zzbA() {
        return zzbs().zzqC;
    }

    public static zznl zzbB() {
        return zzbs().zzqD;
    }

    public static zzcc zzbC() {
        return zzbs().zzqE;
    }

    public static zzhj zzbD() {
        return zzbs().zzqF;
    }

    public static zzbx zzbE() {
        return zzbs().zzqG;
    }

    public static zzbw zzbF() {
        return zzbs().zzqH;
    }

    public static zzby zzbG() {
        return zzbs().zzqI;
    }

    public static com.google.android.gms.ads.internal.purchase.zzi zzbH() {
        return zzbs().zzqJ;
    }

    public static zzed zzbI() {
        return zzbs().zzqK;
    }

    public static zziv zzbJ() {
        return zzbs().zzqL;
    }

    public static zzes zzbK() {
        return zzbs().zzqM;
    }

    public static zzdv zzbL() {
        return zzbs().zzqN;
    }

    private static zzp zzbs() {
        zzp zzpVar;
        synchronized (zzqf) {
            zzpVar = zzqu;
        }
        return zzpVar;
    }

    public static com.google.android.gms.ads.internal.request.zza zzbt() {
        return zzbs().zzqv;
    }

    public static com.google.android.gms.ads.internal.overlay.zza zzbu() {
        return zzbs().zzqw;
    }

    public static com.google.android.gms.ads.internal.overlay.zze zzbv() {
        return zzbs().zzqx;
    }

    public static zzgq zzbw() {
        return zzbs().zzqy;
    }

    public static zzip zzbx() {
        return zzbs().zzqz;
    }

    public static zzjp zzby() {
        return zzbs().zzqA;
    }

    public static zziq zzbz() {
        return zzbs().zzqB;
    }
}
