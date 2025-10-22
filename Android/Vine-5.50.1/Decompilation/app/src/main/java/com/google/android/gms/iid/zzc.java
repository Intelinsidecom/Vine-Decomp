package com.google.android.gms.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/* loaded from: classes2.dex */
public class zzc {
    static String zzaKt = null;
    static int zzaKu = 0;
    static int zzaKv = 0;
    static int zzaKw = 0;
    Context context;
    PendingIntent zzaJc;
    Messenger zzaJg;
    long zzaKA;
    long zzaKB;
    int zzaKC;
    int zzaKD;
    long zzaKE;
    Map<String, Object> zzaKx = new HashMap();
    Messenger zzaKy;
    MessengerCompat zzaKz;

    public zzc(Context context) {
        this.context = context;
    }

    private void zzD(Object obj) {
        synchronized (getClass()) {
            for (String str : this.zzaKx.keySet()) {
                Object obj2 = this.zzaKx.get(str);
                this.zzaKx.put(str, obj);
                zzg(obj2, obj);
            }
        }
    }

    static String zza(KeyPair keyPair, String... strArr) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        try {
            byte[] bytes = TextUtils.join("\n", strArr).getBytes("UTF-8");
            try {
                PrivateKey privateKey = keyPair.getPrivate();
                Signature signature = Signature.getInstance(privateKey instanceof RSAPrivateKey ? "SHA256withRSA" : "SHA256withECDSA");
                signature.initSign(privateKey);
                signature.update(bytes);
                return InstanceID.zzn(signature.sign());
            } catch (GeneralSecurityException e) {
                Log.e("InstanceID/Rpc", "Unable to sign registration request", e);
                return null;
            }
        } catch (UnsupportedEncodingException e2) {
            Log.e("InstanceID/Rpc", "Unable to encode string", e2);
            return null;
        }
    }

    public static String zzaM(Context context) throws PackageManager.NameNotFoundException {
        if (zzaKt != null) {
            return zzaKt;
        }
        zzaKu = Process.myUid();
        PackageManager packageManager = context.getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryIntentServices(new Intent("com.google.android.c2dm.intent.REGISTER"), 0)) {
            if (packageManager.checkPermission("com.google.android.c2dm.permission.RECEIVE", resolveInfo.serviceInfo.packageName) == 0) {
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(resolveInfo.serviceInfo.packageName, 0);
                    Log.w("InstanceID/Rpc", "Found " + applicationInfo.uid);
                    zzaKv = applicationInfo.uid;
                    zzaKt = resolveInfo.serviceInfo.packageName;
                    return zzaKt;
                } catch (PackageManager.NameNotFoundException e) {
                }
            } else {
                Log.w("InstanceID/Rpc", "Possible malicious package " + resolveInfo.serviceInfo.packageName + " declares com.google.android.c2dm.intent.REGISTER without permission");
            }
        }
        Log.w("InstanceID/Rpc", "Failed to resolve REGISTER intent, falling back");
        try {
            ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, 0);
            zzaKt = applicationInfo2.packageName;
            zzaKv = applicationInfo2.uid;
            return zzaKt;
        } catch (PackageManager.NameNotFoundException e2) {
            try {
                ApplicationInfo applicationInfo3 = packageManager.getApplicationInfo("com.google.android.gsf", 0);
                zzaKt = applicationInfo3.packageName;
                zzaKv = applicationInfo3.uid;
                return zzaKt;
            } catch (PackageManager.NameNotFoundException e3) {
                Log.w("InstanceID/Rpc", "Both Google Play Services and legacy GSF package are missing");
                return null;
            }
        }
    }

    private Intent zzb(Bundle bundle, KeyPair keyPair) throws PackageManager.NameNotFoundException, IOException, RemoteException {
        Intent intent;
        ConditionVariable conditionVariable = new ConditionVariable();
        String strZzxT = zzxT();
        synchronized (getClass()) {
            this.zzaKx.put(strZzxT, conditionVariable);
        }
        zza(bundle, keyPair, strZzxT);
        conditionVariable.block(30000L);
        synchronized (getClass()) {
            Object objRemove = this.zzaKx.remove(strZzxT);
            if (!(objRemove instanceof Intent)) {
                if (objRemove instanceof String) {
                    throw new IOException((String) objRemove);
                }
                Log.w("InstanceID/Rpc", "No response " + objRemove);
                throw new IOException("TIMEOUT");
            }
            intent = (Intent) objRemove;
        }
        return intent;
    }

    private void zzdK(String str) {
        if ("com.google.android.gsf".equals(zzaKt)) {
            this.zzaKC++;
            if (this.zzaKC >= 3) {
                if (this.zzaKC == 3) {
                    this.zzaKD = new Random().nextInt(1000) + 1000;
                }
                this.zzaKD *= 2;
                this.zzaKE = SystemClock.elapsedRealtime() + this.zzaKD;
                Log.w("InstanceID/Rpc", "Backoff due to " + str + " for " + this.zzaKD);
            }
        }
    }

    private void zzg(Object obj, Object obj2) throws RemoteException {
        if (obj instanceof ConditionVariable) {
            ((ConditionVariable) obj).open();
        }
        if (obj instanceof Messenger) {
            Messenger messenger = (Messenger) obj;
            Message messageObtain = Message.obtain();
            messageObtain.obj = obj2;
            try {
                messenger.send(messageObtain);
            } catch (RemoteException e) {
                Log.w("InstanceID/Rpc", "Failed to send response " + e);
            }
        }
    }

    private void zzi(String str, Object obj) {
        synchronized (getClass()) {
            Object obj2 = this.zzaKx.get(str);
            this.zzaKx.put(str, obj);
            zzg(obj2, obj);
        }
    }

    public static synchronized String zzxT() {
        int i;
        i = zzaKw;
        zzaKw = i + 1;
        return Integer.toString(i);
    }

    Intent zza(Bundle bundle, KeyPair keyPair) throws PackageManager.NameNotFoundException, IOException, RemoteException {
        Intent intentZzb = zzb(bundle, keyPair);
        return (intentZzb == null || !intentZzb.hasExtra("google.messenger")) ? intentZzb : zzb(bundle, keyPair);
    }

    void zza(Bundle bundle, KeyPair keyPair, String str) throws PackageManager.NameNotFoundException, IOException, RemoteException {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.zzaKE != 0 && jElapsedRealtime <= this.zzaKE) {
            Log.w("InstanceID/Rpc", "Backoff mode, next request attempt: " + (this.zzaKE - jElapsedRealtime) + " interval: " + this.zzaKD);
            throw new IOException("RETRY_LATER");
        }
        zzxS();
        if (zzaKt == null) {
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
        this.zzaKA = SystemClock.elapsedRealtime();
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage(zzaKt);
        bundle.putString("gmsv", Integer.toString(GoogleCloudMessaging.zzaJ(this.context)));
        bundle.putString("osv", Integer.toString(Build.VERSION.SDK_INT));
        bundle.putString("app_ver", Integer.toString(InstanceID.zzaK(this.context)));
        bundle.putString("cliv", "1");
        bundle.putString("appid", InstanceID.zza(keyPair));
        String strZzn = InstanceID.zzn(keyPair.getPublic().getEncoded());
        bundle.putString("pub2", strZzn);
        bundle.putString("sig", zza(keyPair, this.context.getPackageName(), strZzn));
        intent.putExtras(bundle);
        zzu(intent);
        zzb(intent, str);
    }

    protected void zzb(Intent intent, String str) throws RemoteException {
        this.zzaKA = SystemClock.elapsedRealtime();
        intent.putExtra("kid", "|ID|" + str + "|");
        intent.putExtra("X-kid", "|ID|" + str + "|");
        boolean zEquals = "com.google.android.gsf".equals(zzaKt);
        String stringExtra = intent.getStringExtra("useGsf");
        if (stringExtra != null) {
            zEquals = "1".equals(stringExtra);
        }
        if (Log.isLoggable("InstanceID/Rpc", 3)) {
            Log.d("InstanceID/Rpc", "Sending " + intent.getExtras());
        }
        if (this.zzaKy != null) {
            intent.putExtra("google.messenger", this.zzaJg);
            Message messageObtain = Message.obtain();
            messageObtain.obj = intent;
            try {
                this.zzaKy.send(messageObtain);
                return;
            } catch (RemoteException e) {
                if (Log.isLoggable("InstanceID/Rpc", 3)) {
                    Log.d("InstanceID/Rpc", "Messenger failed, fallback to startService");
                }
            }
        }
        if (zEquals) {
            Intent intent2 = new Intent("com.google.android.gms.iid.InstanceID");
            intent2.setPackage(this.context.getPackageName());
            intent2.putExtra("GSF", intent);
            this.context.startService(intent2);
            return;
        }
        intent.putExtra("google.messenger", this.zzaJg);
        intent.putExtra("messenger2", "1");
        if (this.zzaKz != null) {
            Message messageObtain2 = Message.obtain();
            messageObtain2.obj = intent;
            try {
                this.zzaKz.send(messageObtain2);
                return;
            } catch (RemoteException e2) {
                if (Log.isLoggable("InstanceID/Rpc", 3)) {
                    Log.d("InstanceID/Rpc", "Messenger failed, fallback to startService");
                }
            }
        }
        this.context.startService(intent);
    }

    public void zze(Message message) {
        if (message == null) {
            return;
        }
        if (!(message.obj instanceof Intent)) {
            Log.w("InstanceID/Rpc", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(MessengerCompat.class.getClassLoader());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof MessengerCompat) {
                this.zzaKz = (MessengerCompat) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzaKy = (Messenger) parcelableExtra;
            }
        }
        zzx((Intent) message.obj);
    }

    synchronized void zzu(Intent intent) {
        if (this.zzaJc == null) {
            Intent intent2 = new Intent();
            intent2.setPackage("com.google.example.invalidpackage");
            this.zzaJc = PendingIntent.getBroadcast(this.context, 0, intent2, 0);
        }
        intent.putExtra("app", this.zzaJc);
    }

    String zzv(Intent intent) throws IOException {
        if (intent == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String stringExtra = intent.getStringExtra("registration_id");
        if (stringExtra == null) {
            stringExtra = intent.getStringExtra("unregistered");
        }
        intent.getLongExtra("Retry-After", 0L);
        if (stringExtra != null) {
        }
        if (stringExtra != null) {
            return stringExtra;
        }
        String stringExtra2 = intent.getStringExtra("error");
        if (stringExtra2 != null) {
            throw new IOException(stringExtra2);
        }
        Log.w("InstanceID/Rpc", "Unexpected response from GCM " + intent.getExtras(), new Throwable());
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    void zzw(Intent intent) {
        String stringExtra = intent.getStringExtra("error");
        if (stringExtra == null) {
            Log.w("InstanceID/Rpc", "Unexpected response, no error or registration id " + intent.getExtras());
            return;
        }
        if (Log.isLoggable("InstanceID/Rpc", 3)) {
            Log.d("InstanceID/Rpc", "Received InstanceID error " + stringExtra);
        }
        String str = null;
        if (stringExtra.startsWith("|")) {
            String[] strArrSplit = stringExtra.split("\\|");
            if (!"ID".equals(strArrSplit[1])) {
                Log.w("InstanceID/Rpc", "Unexpected structured response " + stringExtra);
            }
            if (strArrSplit.length > 2) {
                str = strArrSplit[2];
                stringExtra = strArrSplit[3];
                if (stringExtra.startsWith(":")) {
                    stringExtra = stringExtra.substring(1);
                }
            } else {
                stringExtra = "UNKNOWN";
            }
            intent.putExtra("error", stringExtra);
        }
        if (str == null) {
            zzD(stringExtra);
        } else {
            zzi(str, stringExtra);
        }
        long longExtra = intent.getLongExtra("Retry-After", 0L);
        if (longExtra > 0) {
            this.zzaKB = SystemClock.elapsedRealtime();
            this.zzaKD = ((int) longExtra) * 1000;
            this.zzaKE = SystemClock.elapsedRealtime() + this.zzaKD;
            Log.w("InstanceID/Rpc", "Explicit request from server to backoff: " + this.zzaKD);
            return;
        }
        if ("SERVICE_NOT_AVAILABLE".equals(stringExtra) || "AUTHENTICATION_FAILED".equals(stringExtra)) {
            zzdK(stringExtra);
        }
    }

    void zzx(Intent intent) {
        if (intent == null) {
            if (Log.isLoggable("InstanceID/Rpc", 3)) {
                Log.d("InstanceID/Rpc", "Unexpected response: null");
                return;
            }
            return;
        }
        String action = intent.getAction();
        if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action) && !"com.google.android.gms.iid.InstanceID".equals(action)) {
            if (Log.isLoggable("InstanceID/Rpc", 3)) {
                Log.d("InstanceID/Rpc", "Unexpected response " + intent.getAction());
                return;
            }
            return;
        }
        String stringExtra = intent.getStringExtra("registration_id");
        String stringExtra2 = stringExtra == null ? intent.getStringExtra("unregistered") : stringExtra;
        if (stringExtra2 == null) {
            zzw(intent);
            return;
        }
        this.zzaKA = SystemClock.elapsedRealtime();
        this.zzaKE = 0L;
        this.zzaKC = 0;
        this.zzaKD = 0;
        if (Log.isLoggable("InstanceID/Rpc", 3)) {
            Log.d("InstanceID/Rpc", "AppIDResponse: " + stringExtra2 + " " + intent.getExtras());
        }
        String str = null;
        if (stringExtra2.startsWith("|")) {
            String[] strArrSplit = stringExtra2.split("\\|");
            if (!"ID".equals(strArrSplit[1])) {
                Log.w("InstanceID/Rpc", "Unexpected structured response " + stringExtra2);
            }
            String str2 = strArrSplit[2];
            if (strArrSplit.length > 4) {
                if ("SYNC".equals(strArrSplit[3])) {
                    InstanceIDListenerService.zzaL(this.context);
                } else if ("RST".equals(strArrSplit[3])) {
                    InstanceIDListenerService.zza(this.context, InstanceID.getInstance(this.context).zzxP());
                    intent.removeExtra("registration_id");
                    zzi(str2, intent);
                    return;
                }
            }
            String strSubstring = strArrSplit[strArrSplit.length - 1];
            if (strSubstring.startsWith(":")) {
                strSubstring = strSubstring.substring(1);
            }
            intent.putExtra("registration_id", strSubstring);
            str = str2;
        }
        if (str == null) {
            zzD(intent);
        } else {
            zzi(str, intent);
        }
    }

    void zzxS() throws PackageManager.NameNotFoundException {
        if (this.zzaJg != null) {
            return;
        }
        zzaM(this.context);
        this.zzaJg = new Messenger(new Handler(Looper.getMainLooper()) { // from class: com.google.android.gms.iid.zzc.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                zzc.this.zze(msg);
            }
        });
    }
}
