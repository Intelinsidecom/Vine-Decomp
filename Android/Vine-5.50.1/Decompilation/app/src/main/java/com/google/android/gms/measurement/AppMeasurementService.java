package com.google.android.gms.measurement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.internal.zzae;
import com.google.android.gms.measurement.internal.zzo;
import com.google.android.gms.measurement.internal.zzt;
import com.google.android.gms.measurement.internal.zzu;

/* loaded from: classes.dex */
public final class AppMeasurementService extends Service {
    private static Boolean zzNu;
    private final Handler mHandler = new Handler();

    public static boolean zzY(Context context) {
        zzx.zzy(context);
        if (zzNu != null) {
            return zzNu.booleanValue();
        }
        boolean zZza = zzae.zza(context, (Class<? extends Service>) AppMeasurementService.class);
        zzNu = Boolean.valueOf(zZza);
        return zZza;
    }

    private void zzih() {
        try {
            synchronized (AppMeasurementReceiver.zzqf) {
                PowerManager.WakeLock wakeLock = AppMeasurementReceiver.zzaQY;
                if (wakeLock != null && wakeLock.isHeld()) {
                    wakeLock.release();
                }
            }
        } catch (SecurityException e) {
        }
    }

    private zzo zzzz() {
        return zzt.zzaU(this).zzzz();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            zzzz().zzBl().zzez("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzu(zzt.zzaU(this));
        }
        zzzz().zzBm().zzj("onBind received unknown action", action);
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        zzt zztVarZzaU = zzt.zzaU(this);
        zzo zzoVarZzzz = zztVarZzaU.zzzz();
        if (zztVarZzaU.zzAX().zzka()) {
            zzoVarZzzz.zzBr().zzez("Device AppMeasurementService is starting up");
        } else {
            zzoVarZzzz.zzBr().zzez("Local AppMeasurementService is starting up");
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        zzt zztVarZzaU = zzt.zzaU(this);
        zzo zzoVarZzzz = zztVarZzaU.zzzz();
        if (zztVarZzaU.zzAX().zzka()) {
            zzoVarZzzz.zzBr().zzez("Device AppMeasurementService is shutting down");
        } else {
            zzoVarZzzz.zzBr().zzez("Local AppMeasurementService is shutting down");
        }
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        if (intent == null) {
            zzzz().zzBl().zzez("onRebind called with null intent");
        } else {
            zzzz().zzBr().zzj("onRebind called. action", intent.getAction());
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, final int startId) throws IllegalStateException {
        zzih();
        final zzt zztVarZzaU = zzt.zzaU(this);
        final zzo zzoVarZzzz = zztVarZzaU.zzzz();
        String action = intent.getAction();
        if (zztVarZzaU.zzAX().zzka()) {
            zzoVarZzzz.zzBr().zze("Device AppMeasurementService called. startId, action", Integer.valueOf(startId), action);
        } else {
            zzoVarZzzz.zzBr().zze("Local AppMeasurementService called. startId, action", Integer.valueOf(startId), action);
        }
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            zztVarZzaU.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.AppMeasurementService.1
                @Override // java.lang.Runnable
                public void run() {
                    zztVarZzaU.zzBK();
                    AppMeasurementService.this.mHandler.post(new Runnable() { // from class: com.google.android.gms.measurement.AppMeasurementService.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (AppMeasurementService.this.stopSelfResult(startId)) {
                                if (zztVarZzaU.zzAX().zzka()) {
                                    zzoVarZzzz.zzBr().zzez("Device AppMeasurementService processed last upload request");
                                } else {
                                    zzoVarZzzz.zzBr().zzez("Local AppMeasurementService processed last upload request");
                                }
                            }
                        }
                    });
                }
            });
        }
        return 2;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        if (intent == null) {
            zzzz().zzBl().zzez("onUnbind called with null intent");
        } else {
            zzzz().zzBr().zzj("onUnbind called for intent. action", intent.getAction());
        }
        return true;
    }
}
