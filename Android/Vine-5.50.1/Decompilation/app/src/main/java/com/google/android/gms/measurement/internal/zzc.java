package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzny;

/* loaded from: classes.dex */
public class zzc extends zzv {
    static final String zzaSt = String.valueOf(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000).replaceAll("(\\d+)(\\d)(\\d\\d)", "$1.$2.$3");
    private Boolean zzQe;

    zzc(zzt zztVar) {
        super(zztVar);
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    long zzAA() {
        return 3600000L;
    }

    long zzAB() {
        return 60000L;
    }

    long zzAC() {
        return 61000L;
    }

    long zzAD() {
        return zzk.zzaSR.get().longValue();
    }

    public long zzAE() {
        return GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000;
    }

    public long zzAF() {
        return zzk.zzaTd.get().longValue();
    }

    public long zzAG() {
        return zzk.zzaSZ.get().longValue();
    }

    public long zzAH() {
        return 20L;
    }

    public int zzAI() {
        return zzk.zzaST.get().intValue();
    }

    public int zzAJ() {
        return Math.max(0, zzk.zzaSU.get().intValue());
    }

    public String zzAK() {
        return zzk.zzaSV.get();
    }

    public long zzAL() {
        return Math.max(0L, zzk.zzaSW.get().longValue());
    }

    public long zzAM() {
        return Math.max(0L, zzk.zzaSY.get().longValue());
    }

    public long zzAN() {
        return zzk.zzaSX.get().longValue();
    }

    public long zzAO() {
        return Math.max(0L, zzk.zzaTa.get().longValue());
    }

    public long zzAP() {
        return Math.max(0L, zzk.zzaTb.get().longValue());
    }

    public int zzAQ() {
        return Math.min(20, Math.max(0, zzk.zzaTc.get().intValue()));
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

    String zzAs() {
        return zzk.zzaSQ.get();
    }

    public int zzAu() {
        return 24;
    }

    int zzAv() {
        return 36;
    }

    int zzAw() {
        return 256;
    }

    int zzAx() {
        return 36;
    }

    int zzAy() {
        return 2048;
    }

    int zzAz() {
        return 20;
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

    public String zzkA() {
        return "google_app_measurement.db";
    }

    public String zzkB() {
        return "google_app_measurement2.db";
    }

    public long zzkG() {
        return Math.max(0L, zzk.zzaSS.get().longValue());
    }

    public boolean zzka() {
        return com.google.android.gms.common.internal.zzd.zzaiU;
    }

    public boolean zzkb() {
        if (this.zzQe == null) {
            synchronized (this) {
                if (this.zzQe == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String strZzj = zzny.zzj(getContext(), Process.myPid());
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzQe = Boolean.valueOf(str != null && str.equals(strZzj));
                    }
                    if (this.zzQe == null) {
                        this.zzQe = Boolean.TRUE;
                        zzzz().zzBl().zzez("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzQe.booleanValue();
    }

    long zzkv() {
        return zzk.zzaTe.get().longValue();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
