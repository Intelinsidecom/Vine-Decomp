package com.google.android.gms.measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.internal.zzae;
import com.google.android.gms.measurement.internal.zzo;
import com.google.android.gms.measurement.internal.zzt;

/* loaded from: classes.dex */
public final class AppMeasurementReceiver extends BroadcastReceiver {
    static Boolean zzNt;
    static PowerManager.WakeLock zzaQY;
    static final Object zzqf = new Object();

    public static boolean zzX(Context context) throws PackageManager.NameNotFoundException {
        zzx.zzy(context);
        if (zzNt != null) {
            return zzNt.booleanValue();
        }
        boolean zZza = zzae.zza(context, (Class<? extends BroadcastReceiver>) AppMeasurementReceiver.class, false);
        zzNt = Boolean.valueOf(zZza);
        return zZza;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        zzt zztVarZzaU = zzt.zzaU(context);
        zzo zzoVarZzzz = zztVarZzaU.zzzz();
        String action = intent.getAction();
        if (zztVarZzaU.zzAX().zzka()) {
            zzoVarZzzz.zzBr().zzj("Device AppMeasurementReceiver got", action);
        } else {
            zzoVarZzzz.zzBr().zzj("Local AppMeasurementReceiver got", action);
        }
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            boolean zZzY = AppMeasurementService.zzY(context);
            Intent intent2 = new Intent(context, (Class<?>) AppMeasurementService.class);
            intent2.setAction("com.google.android.gms.measurement.UPLOAD");
            synchronized (zzqf) {
                context.startService(intent2);
                if (zZzY) {
                    try {
                        PowerManager powerManager = (PowerManager) context.getSystemService("power");
                        if (zzaQY == null) {
                            zzaQY = powerManager.newWakeLock(1, "AppMeasurement WakeLock");
                            zzaQY.setReferenceCounted(false);
                        }
                        zzaQY.acquire(1000L);
                    } catch (SecurityException e) {
                        zzoVarZzzz.zzBm().zzez("AppMeasurementService at risk of not starting. For more reliable app measurements, add the WAKE_LOCK permission to your manifest.");
                    }
                }
            }
        }
    }
}
