package com.google.android.gms.gcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.zzc;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class GoogleCloudMessaging {
    static GoogleCloudMessaging zzaJb;
    private Context context;
    private PendingIntent zzaJc;
    public static int zzaIY = 5000000;
    public static int zzaIZ = 6500000;
    public static int zzaJa = 7000000;
    private static final AtomicInteger zzaJe = new AtomicInteger(1);
    private final BlockingQueue<Intent> zzaJf = new LinkedBlockingQueue();
    private Map<String, Handler> zzaJd = Collections.synchronizedMap(new HashMap());
    final Messenger zzaJg = new Messenger(new Handler(Looper.getMainLooper()) { // from class: com.google.android.gms.gcm.GoogleCloudMessaging.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg == null || !(msg.obj instanceof Intent)) {
                Log.w("GCM", "Dropping invalid message");
            }
            Intent intent = (Intent) msg.obj;
            if ("com.google.android.c2dm.intent.REGISTRATION".equals(intent.getAction())) {
                GoogleCloudMessaging.this.zzaJf.add(intent);
            } else {
                if (GoogleCloudMessaging.this.zzs(intent)) {
                    return;
                }
                intent.setPackage(GoogleCloudMessaging.this.context.getPackageName());
                GoogleCloudMessaging.this.context.sendBroadcast(intent);
            }
        }
    });

    public static synchronized GoogleCloudMessaging getInstance(Context context) {
        if (zzaJb == null) {
            zzaJb = new GoogleCloudMessaging();
            zzaJb.context = context.getApplicationContext();
        }
        return zzaJb;
    }

    static String zza(Intent intent, String str) throws IOException {
        if (intent == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String stringExtra = intent.getStringExtra(str);
        if (stringExtra != null) {
            return stringExtra;
        }
        String stringExtra2 = intent.getStringExtra("error");
        if (stringExtra2 != null) {
            throw new IOException(stringExtra2);
        }
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    private void zza(String str, String str2, long j, int i, Bundle bundle) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("Missing 'to'");
        }
        Intent intent = new Intent("com.google.android.gcm.intent.SEND");
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        zzt(intent);
        intent.setPackage(zzaI(this.context));
        intent.putExtra("google.to", str);
        intent.putExtra("google.message_id", str2);
        intent.putExtra("google.ttl", Long.toString(j));
        intent.putExtra("google.delay", Integer.toString(i));
        if (!zzaI(this.context).contains(".gsf")) {
            this.context.sendOrderedBroadcast(intent, "com.google.android.gtalkservice.permission.GTALK_SERVICE");
            return;
        }
        Bundle bundle2 = new Bundle();
        for (String str3 : bundle.keySet()) {
            Object obj = bundle.get(str3);
            if (obj instanceof String) {
                bundle2.putString("gcm." + str3, (String) obj);
            }
        }
        bundle2.putString("google.to", str);
        bundle2.putString("google.message_id", str2);
        InstanceID.getInstance(this.context).zzc("GCM", "upstream", bundle2);
    }

    public static String zzaI(Context context) {
        return zzc.zzaM(context);
    }

    public static int zzaJ(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(zzaI(context), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzs(Intent intent) {
        Handler handlerRemove;
        String stringExtra = intent.getStringExtra("In-Reply-To");
        if (stringExtra == null && intent.hasExtra("error")) {
            stringExtra = intent.getStringExtra("google.message_id");
        }
        if (stringExtra == null || (handlerRemove = this.zzaJd.remove(stringExtra)) == null) {
            return false;
        }
        Message messageObtain = Message.obtain();
        messageObtain.obj = intent;
        return handlerRemove.sendMessage(messageObtain);
    }

    private String zzxy() {
        return "google.rpc" + String.valueOf(zzaJe.getAndIncrement());
    }

    public void close() {
        zzaJb = null;
        zzb.zzaIN = null;
        zzxz();
    }

    @Deprecated
    public synchronized String register(String... senderIds) throws IOException {
        String strZza;
        String strZzc = zzc(senderIds);
        Bundle bundle = new Bundle();
        if (zzaI(this.context).contains(".gsf")) {
            bundle.putString("legacy.sender", strZzc);
            strZza = InstanceID.getInstance(this.context).getToken(strZzc, "GCM", bundle);
        } else {
            bundle.putString("sender", strZzc);
            strZza = zza(zzD(bundle), "registration_id");
        }
        return strZza;
    }

    public void send(String to, String msgId, long timeToLive, Bundle data) throws IOException {
        zza(to, msgId, timeToLive, -1, data);
    }

    @Deprecated
    Intent zzD(Bundle bundle) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        if (zzaJ(this.context) < 0) {
            throw new IOException("Google Play Services missing");
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage(zzaI(this.context));
        zzt(intent);
        intent.putExtra("google.message_id", zzxy());
        intent.putExtras(bundle);
        intent.putExtra("google.messenger", this.zzaJg);
        this.context.startService(intent);
        try {
            return this.zzaJf.poll(30000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }

    String zzc(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalArgumentException("No senderIds");
        }
        StringBuilder sb = new StringBuilder(strArr[0]);
        for (int i = 1; i < strArr.length; i++) {
            sb.append(',').append(strArr[i]);
        }
        return sb.toString();
    }

    synchronized void zzt(Intent intent) {
        if (this.zzaJc == null) {
            Intent intent2 = new Intent();
            intent2.setPackage("com.google.example.invalidpackage");
            this.zzaJc = PendingIntent.getBroadcast(this.context, 0, intent2, 0);
        }
        intent.putExtra("app", this.zzaJc);
    }

    synchronized void zzxz() {
        if (this.zzaJc != null) {
            this.zzaJc.cancel();
            this.zzaJc = null;
        }
    }
}
