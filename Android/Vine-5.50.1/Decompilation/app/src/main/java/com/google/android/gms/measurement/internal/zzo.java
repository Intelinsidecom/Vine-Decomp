package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.measurement.AppMeasurement;

/* loaded from: classes.dex */
public class zzo extends zzw {
    private final long zzaSk;
    private final char zzaTh;
    private final zza zzaTi;
    private final zza zzaTj;
    private final zza zzaTk;
    private final zza zzaTl;
    private final zza zzaTm;
    private final zza zzaTn;
    private final zza zzaTo;
    private final zza zzaTp;
    private final zza zzaTq;
    private final String zzakw;

    public class zza {
        private final int mPriority;
        private final boolean zzaTt;
        private final boolean zzaTu;

        zza(int i, boolean z, boolean z2) {
            this.mPriority = i;
            this.zzaTt = z;
            this.zzaTu = z2;
        }

        public void zzd(String str, Object obj, Object obj2, Object obj3) throws IllegalStateException {
            zzo.this.zza(this.mPriority, this.zzaTt, this.zzaTu, str, obj, obj2, obj3);
        }

        public void zze(String str, Object obj, Object obj2) {
            zzo.this.zza(this.mPriority, this.zzaTt, this.zzaTu, str, obj, obj2, null);
        }

        public void zzez(String str) {
            zzo.this.zza(this.mPriority, this.zzaTt, this.zzaTu, str, null, null, null);
        }

        public void zzj(String str, Object obj) {
            zzo.this.zza(this.mPriority, this.zzaTt, this.zzaTu, str, obj, null, null);
        }
    }

    zzo(zzt zztVar) {
        super(zztVar);
        this.zzakw = zzAX().zzAs();
        this.zzaSk = zzAX().zzAE();
        if (zzAX().zzkb()) {
            this.zzaTh = zzAX().zzka() ? 'P' : 'C';
        } else {
            this.zzaTh = zzAX().zzka() ? 'p' : 'c';
        }
        this.zzaTi = new zza(6, false, false);
        this.zzaTj = new zza(6, true, false);
        this.zzaTk = new zza(6, false, true);
        this.zzaTl = new zza(5, false, false);
        this.zzaTm = new zza(5, true, false);
        this.zzaTn = new zza(5, false, true);
        this.zzaTo = new zza(4, false, false);
        this.zzaTp = new zza(3, false, false);
        this.zzaTq = new zza(2, false, false);
    }

    static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String strZzc = zzc(z, obj);
        String strZzc2 = zzc(z, obj2);
        String strZzc3 = zzc(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(strZzc)) {
            sb.append(str2);
            sb.append(strZzc);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(strZzc2)) {
            sb.append(str2);
            sb.append(strZzc2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(strZzc3)) {
            sb.append(str2);
            sb.append(strZzc3);
        }
        return sb.toString();
    }

    static String zzc(boolean z, Object obj) {
        String className;
        if (obj == null) {
            return "";
        }
        Object objValueOf = obj instanceof Integer ? Long.valueOf(((Integer) obj).intValue()) : obj;
        if (objValueOf instanceof Long) {
            if (z && Math.abs(((Long) objValueOf).longValue()) >= 100) {
                String str = String.valueOf(objValueOf).charAt(0) == '-' ? "-" : "";
                String strValueOf = String.valueOf(Math.abs(((Long) objValueOf).longValue()));
                return str + Math.round(Math.pow(10.0d, strValueOf.length() - 1)) + "..." + str + Math.round(Math.pow(10.0d, strValueOf.length()) - 1.0d);
            }
            return String.valueOf(objValueOf);
        }
        if (objValueOf instanceof Boolean) {
            return String.valueOf(objValueOf);
        }
        if (!(objValueOf instanceof Throwable)) {
            return z ? "-" : String.valueOf(objValueOf);
        }
        Throwable th = (Throwable) objValueOf;
        StringBuilder sb = new StringBuilder(th.toString());
        String strZzey = zzey(AppMeasurement.class.getCanonicalName());
        String strZzey2 = zzey(zzt.class.getCanonicalName());
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null) {
                String strZzey3 = zzey(className);
                if (strZzey3.equals(strZzey) || strZzey3.equals(strZzey2)) {
                    sb.append(": ");
                    sb.append(stackTraceElement);
                    break;
                }
            }
        }
        return sb.toString();
    }

    private static String zzey(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int iLastIndexOf = str.lastIndexOf(46);
        return iLastIndexOf != -1 ? str.substring(0, iLastIndexOf) : str;
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zzAR() {
        super.zzAR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzm zzAS() {
        return super.zzAS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzz zzAT() {
        return super.zzAT();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzae zzAU() {
        return super.zzAU();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzs zzAV() {
        return super.zzAV();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzr zzAW() {
        return super.zzAW();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzc zzAX() {
        return super.zzAX();
    }

    public zza zzBl() {
        return this.zzaTi;
    }

    public zza zzBm() {
        return this.zzaTl;
    }

    public zza zzBn() {
        return this.zzaTm;
    }

    public zza zzBo() {
        return this.zzaTn;
    }

    public zza zzBp() {
        return this.zzaTo;
    }

    public zza zzBq() {
        return this.zzaTp;
    }

    public zza zzBr() {
        return this.zzaTq;
    }

    public String zzBs() {
        Pair<String, Long> pairZzlw = zzAW().zzaTE.zzlw();
        if (pairZzlw == null) {
            return null;
        }
        return String.valueOf(pairZzlw.second) + ":" + ((String) pairZzlw.first);
    }

    protected boolean zzQ(int i) {
        return Log.isLoggable(this.zzakw, i);
    }

    protected void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) throws IllegalStateException {
        if (!z && zzQ(i)) {
            zzn(i, zza(false, str, obj, obj2, obj3));
        }
        if (z2 || i < 5) {
            return;
        }
        zzb(i, str, obj, obj2, obj3);
    }

    public void zzb(int i, String str, Object obj, Object obj2, Object obj3) throws IllegalStateException {
        com.google.android.gms.common.internal.zzx.zzy(str);
        zzs zzsVarZzBA = this.zzaQX.zzBA();
        if (zzsVarZzBA == null || !zzsVarZzBA.isInitialized() || zzsVarZzBA.zzBP()) {
            zzn(6, "Scheduler not initialized or shutdown. Not logging error/warn.");
            return;
        }
        if (i < 0) {
            i = 0;
        }
        if (i >= "01VDIWEA?".length()) {
            i = "01VDIWEA?".length() - 1;
        }
        String str2 = "1" + "01VDIWEA?".charAt(i) + this.zzaTh + this.zzaSk + ":" + zza(true, str, obj, obj2, obj3);
        final String strSubstring = str2.length() > 1024 ? str.substring(0, 1024) : str2;
        zzsVarZzBA.zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzo.1
            @Override // java.lang.Runnable
            public void run() {
                zzr zzrVarZzAW = zzo.this.zzaQX.zzAW();
                if (!zzrVarZzAW.isInitialized() || zzrVarZzAW.zzBP()) {
                    zzo.this.zzn(6, "Persisted config not initialized . Not logging error/warn.");
                } else {
                    zzrVarZzAW.zzaTE.zzbn(strSubstring);
                }
            }
        });
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziR() {
        super.zziR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziS() {
        super.zziS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zznl zziT() {
        return super.zziT();
    }

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() {
    }

    protected void zzn(int i, String str) {
        Log.println(i, this.zzakw, str);
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
