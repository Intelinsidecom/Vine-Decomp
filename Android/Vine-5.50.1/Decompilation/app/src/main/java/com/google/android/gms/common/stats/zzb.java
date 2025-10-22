package com.google.android.gms.common.stats;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Debug;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.stats.zzc;
import com.google.android.gms.internal.zznk;
import com.google.android.gms.internal.zzny;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class zzb {
    private static final Object zzakg = new Object();
    private static Integer zzalE;
    private static zzb zzaly;
    private final List<String> zzalA;
    private final List<String> zzalB;
    private final List<String> zzalC;
    private zze zzalD;
    private zze zzalF;
    private final List<String> zzalz;

    private zzb() {
        if (getLogLevel() == zzd.LOG_LEVEL_OFF) {
            this.zzalz = Collections.EMPTY_LIST;
            this.zzalA = Collections.EMPTY_LIST;
            this.zzalB = Collections.EMPTY_LIST;
            this.zzalC = Collections.EMPTY_LIST;
            return;
        }
        String str = zzc.zza.zzalJ.get();
        this.zzalz = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        String str2 = zzc.zza.zzalK.get();
        this.zzalA = str2 == null ? Collections.EMPTY_LIST : Arrays.asList(str2.split(","));
        String str3 = zzc.zza.zzalL.get();
        this.zzalB = str3 == null ? Collections.EMPTY_LIST : Arrays.asList(str3.split(","));
        String str4 = zzc.zza.zzalM.get();
        this.zzalC = str4 == null ? Collections.EMPTY_LIST : Arrays.asList(str4.split(","));
        this.zzalD = new zze(1024, zzc.zza.zzalN.get().longValue());
        this.zzalF = new zze(1024, zzc.zza.zzalN.get().longValue());
    }

    private static int getLogLevel() {
        if (zzalE == null) {
            try {
                zzalE = Integer.valueOf(zznk.zzka() ? zzc.zza.zzalI.get().intValue() : zzd.LOG_LEVEL_OFF);
            } catch (SecurityException e) {
                zzalE = Integer.valueOf(zzd.LOG_LEVEL_OFF);
            }
        }
        return zzalE.intValue();
    }

    private void zza(Context context, String str, int i, String str2, String str3, String str4, String str5) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        String strZzm = null;
        if ((getLogLevel() & zzd.zzalS) != 0 && i != 13) {
            strZzm = zzny.zzm(3, 5);
        }
        long nativeHeapAllocatedSize = (getLogLevel() & zzd.zzalU) != 0 ? Debug.getNativeHeapAllocatedSize() : 0L;
        context.startService(new Intent().setComponent(zzd.zzalO).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", (i == 1 || i == 4 || i == 14) ? new ConnectionEvent(jCurrentTimeMillis, i, null, null, null, null, strZzm, str, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize) : new ConnectionEvent(jCurrentTimeMillis, i, str2, str3, str4, str5, strZzm, str, SystemClock.elapsedRealtime(), nativeHeapAllocatedSize)));
    }

    private void zza(Context context, String str, String str2, Intent intent, int i) {
        String str3;
        String str4;
        String strZzay = null;
        if (!zzrA() || this.zzalD == null) {
            return;
        }
        if (i != 4 && i != 1) {
            ServiceInfo serviceInfoZzd = zzd(context, intent);
            if (serviceInfoZzd == null) {
                Log.w("ConnectionTracker", String.format("Client %s made an invalid request %s", str2, intent.toUri(0)));
                return;
            }
            str4 = serviceInfoZzd.processName;
            str3 = serviceInfoZzd.name;
            strZzay = zzny.zzay(context);
            if (!zzb(strZzay, str2, str4, str3)) {
                return;
            } else {
                this.zzalD.zzcM(str);
            }
        } else {
            if (!this.zzalD.zzcN(str)) {
                return;
            }
            str3 = null;
            str4 = null;
        }
        zza(context, str, i, strZzay, str2, str4, str3);
    }

    private String zzb(ServiceConnection serviceConnection) {
        return String.valueOf((Process.myPid() << 32) | System.identityHashCode(serviceConnection));
    }

    private boolean zzb(String str, String str2, String str3, String str4) {
        return (this.zzalz.contains(str) || this.zzalA.contains(str2) || this.zzalB.contains(str3) || this.zzalC.contains(str4) || (str3.equals(str) && (getLogLevel() & zzd.zzalT) != 0)) ? false : true;
    }

    private boolean zzc(Context context, Intent intent) {
        ComponentName component = intent.getComponent();
        if (component == null || (com.google.android.gms.common.internal.zzd.zzaiU && GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE.equals(component.getPackageName()))) {
            return false;
        }
        return zznk.zzj(context, component.getPackageName());
    }

    private static ServiceInfo zzd(Context context, Intent intent) {
        List<ResolveInfo> listQueryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        if (listQueryIntentServices == null || listQueryIntentServices.size() == 0) {
            Log.w("ConnectionTracker", String.format("There are no handler of this intent: %s\n Stack trace: %s", intent.toUri(0), zzny.zzm(3, 20)));
            return null;
        }
        if (listQueryIntentServices.size() > 1) {
            Log.w("ConnectionTracker", String.format("Multiple handlers found for this intent: %s\n Stack trace: %s", intent.toUri(0), zzny.zzm(3, 20)));
            Iterator<ResolveInfo> it = listQueryIntentServices.iterator();
            if (it.hasNext()) {
                Log.w("ConnectionTracker", it.next().serviceInfo.name);
                return null;
            }
        }
        return listQueryIntentServices.get(0).serviceInfo;
    }

    private boolean zzrA() {
        return com.google.android.gms.common.internal.zzd.zzaiU && getLogLevel() != zzd.LOG_LEVEL_OFF;
    }

    public static zzb zzrz() {
        synchronized (zzakg) {
            if (zzaly == null) {
                zzaly = new zzb();
            }
        }
        return zzaly;
    }

    public void zza(Context context, ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 1);
    }

    public void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent) {
        zza(context, zzb(serviceConnection), str, intent, 3);
    }

    public boolean zza(Context context, Intent intent, ServiceConnection serviceConnection, int i) {
        return zza(context, context.getClass().getName(), intent, serviceConnection, i);
    }

    public boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i) {
        if (zzc(context, intent)) {
            Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
            return false;
        }
        boolean zBindService = context.bindService(intent, serviceConnection, i);
        if (zBindService) {
            zza(context, zzb(serviceConnection), str, intent, 2);
        }
        return zBindService;
    }

    public void zzb(Context context, ServiceConnection serviceConnection) {
        zza(context, zzb(serviceConnection), (String) null, (Intent) null, 4);
    }
}
