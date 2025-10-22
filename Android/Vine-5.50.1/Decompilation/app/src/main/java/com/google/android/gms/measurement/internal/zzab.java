package com.google.android.gms.measurement.internal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.measurement.AppMeasurementReceiver;
import com.google.android.gms.measurement.AppMeasurementService;

/* loaded from: classes.dex */
public class zzab extends zzw {
    private boolean zzQn;
    private final AlarmManager zzQo;

    protected zzab(zzt zztVar) {
        super(zztVar);
        this.zzQo = (AlarmManager) getContext().getSystemService("alarm");
    }

    private PendingIntent zzkM() {
        Intent intent = new Intent(getContext(), (Class<?>) AppMeasurementReceiver.class);
        intent.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    }

    public void cancel() {
        zzje();
        this.zzQn = false;
        this.zzQo.cancel(zzkM());
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
        this.zzQo.cancel(zzkM());
    }

    public void zzt(long j) {
        zzje();
        com.google.android.gms.common.internal.zzx.zzab(j > 0);
        com.google.android.gms.common.internal.zzx.zza(AppMeasurementReceiver.zzX(getContext()), "Receiver not registered/enabled");
        com.google.android.gms.common.internal.zzx.zza(AppMeasurementService.zzY(getContext()), "Service not registered/enabled");
        cancel();
        long jElapsedRealtime = zziT().elapsedRealtime() + j;
        this.zzQn = true;
        this.zzQo.setInexactRepeating(2, jElapsedRealtime, Math.max(zzAX().zzAN(), j), zzkM());
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
