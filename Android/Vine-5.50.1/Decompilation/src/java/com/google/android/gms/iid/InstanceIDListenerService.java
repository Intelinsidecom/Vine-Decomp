package com.google.android.gms.iid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;

/* loaded from: classes2.dex */
public class InstanceIDListenerService extends Service {
    MessengerCompat zzaKj = new MessengerCompat(new Handler(Looper.getMainLooper()) { // from class: com.google.android.gms.iid.InstanceIDListenerService.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) throws PackageManager.NameNotFoundException {
            InstanceIDListenerService.this.zza(msg, MessengerCompat.zzc(msg));
        }
    });
    BroadcastReceiver zzaKk = new BroadcastReceiver() { // from class: com.google.android.gms.iid.InstanceIDListenerService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (Log.isLoggable("InstanceID", 3)) {
                intent.getStringExtra("registration_id");
                Log.d("InstanceID", "Received GSF callback using dynamic receiver: " + intent.getExtras());
            }
            InstanceIDListenerService.this.zzp(intent);
            InstanceIDListenerService.this.stop();
        }
    };
    int zzaKn;
    int zzaKo;
    static String ACTION = "action";
    private static String zzaKl = "google.com/iid";
    private static String zzaKm = "CMD";
    private static String zzaIT = "gcm.googleapis.com/refresh";

    static void zza(Context context, zzd zzdVar) {
        zzdVar.zzxU();
        Intent intent = new Intent("com.google.android.gms.iid.InstanceID");
        intent.putExtra(zzaKm, "RST");
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(Message message, int i) throws PackageManager.NameNotFoundException {
        zzc.zzaM(this);
        getPackageManager();
        if (i == zzc.zzaKv || i == zzc.zzaKu) {
            zzp((Intent) message.obj);
        } else {
            Log.w("InstanceID", "Message from unexpected caller " + i + " mine=" + zzc.zzaKu + " appid=" + zzc.zzaKv);
        }
    }

    static void zzaL(Context context) {
        Intent intent = new Intent("com.google.android.gms.iid.InstanceID");
        intent.setPackage(context.getPackageName());
        intent.putExtra(zzaKm, "SYNC");
        context.startService(intent);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (intent == null || !"com.google.android.gms.iid.InstanceID".equals(intent.getAction())) {
            return null;
        }
        return this.zzaKj.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter("com.google.android.c2dm.intent.REGISTRATION");
        intentFilter.addCategory(getPackageName());
        registerReceiver(this.zzaKk, intentFilter, "com.google.android.c2dm.permission.RECEIVE", null);
    }

    @Override // android.app.Service
    public void onDestroy() {
        unregisterReceiver(this.zzaKk);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent2;
        zzhc(startId);
        if (intent == null) {
            return 2;
        }
        try {
            if ("com.google.android.gms.iid.InstanceID".equals(intent.getAction())) {
                if (Build.VERSION.SDK_INT <= 18 && (intent2 = (Intent) intent.getParcelableExtra("GSF")) != null) {
                    startService(intent2);
                    return 1;
                }
                zzp(intent);
            }
            stop();
            if (intent.getStringExtra("from") != null) {
                GcmReceiver.completeWakefulIntent(intent);
            }
            return 2;
        } finally {
            stop();
        }
    }

    public void onTokenRefresh() {
    }

    void stop() {
        synchronized (this) {
            this.zzaKn--;
            if (this.zzaKn == 0) {
                stopSelf(this.zzaKo);
            }
            if (Log.isLoggable("InstanceID", 3)) {
                Log.d("InstanceID", "Stop " + this.zzaKn + " " + this.zzaKo);
            }
        }
    }

    public void zzah(boolean z) {
        onTokenRefresh();
    }

    void zzhc(int i) {
        synchronized (this) {
            this.zzaKn++;
            if (i > this.zzaKo) {
                this.zzaKo = i;
            }
        }
    }

    public void zzp(Intent intent) {
        InstanceID instanceIDZza;
        String stringExtra = intent.getStringExtra("subtype");
        if (stringExtra == null) {
            instanceIDZza = InstanceID.getInstance(this);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("subtype", stringExtra);
            instanceIDZza = InstanceID.zza(this, bundle);
        }
        String stringExtra2 = intent.getStringExtra(zzaKm);
        if (intent.getStringExtra("error") != null || intent.getStringExtra("registration_id") != null) {
            if (Log.isLoggable("InstanceID", 3)) {
                Log.d("InstanceID", "Register result in service " + stringExtra);
            }
            instanceIDZza.zzxQ().zzx(intent);
            return;
        }
        if (Log.isLoggable("InstanceID", 3)) {
            Log.d("InstanceID", "Service command " + stringExtra + " " + stringExtra2 + " " + intent.getExtras());
        }
        if (intent.getStringExtra("unregistered") != null) {
            zzd zzdVarZzxP = instanceIDZza.zzxP();
            if (stringExtra == null) {
                stringExtra = "";
            }
            zzdVarZzxP.zzdP(stringExtra);
            instanceIDZza.zzxQ().zzx(intent);
            return;
        }
        if (zzaIT.equals(intent.getStringExtra("from"))) {
            instanceIDZza.zzxP().zzdP(stringExtra);
            zzah(false);
            return;
        }
        if ("RST".equals(stringExtra2)) {
            instanceIDZza.zzxO();
            zzah(true);
            return;
        }
        if ("RST_FULL".equals(stringExtra2)) {
            if (instanceIDZza.zzxP().isEmpty()) {
                return;
            }
            instanceIDZza.zzxP().zzxU();
            zzah(true);
            return;
        }
        if ("SYNC".equals(stringExtra2)) {
            instanceIDZza.zzxP().zzdP(stringExtra);
            zzah(false);
        } else if ("PING".equals(stringExtra2)) {
            try {
                GoogleCloudMessaging.getInstance(this).send(zzaKl, zzc.zzxT(), 0L, intent.getExtras());
            } catch (IOException e) {
                Log.w("InstanceID", "Failed to send ping response");
            }
        }
    }
}
