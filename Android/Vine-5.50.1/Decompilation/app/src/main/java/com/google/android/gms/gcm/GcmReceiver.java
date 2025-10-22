package com.google.android.gms.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Base64;

/* loaded from: classes2.dex */
public class GcmReceiver extends WakefulBroadcastReceiver {
    private static String zzaIT = "gcm.googleapis.com/refresh";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        if (Build.VERSION.SDK_INT <= 18) {
            intent.removeCategory(context.getPackageName());
        }
        String stringExtra = intent.getStringExtra("from");
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(intent.getAction()) || "google.com/iid".equals(stringExtra) || zzaIT.equals(stringExtra)) {
            intent.setAction("com.google.android.gms.iid.InstanceID");
        }
        String stringExtra2 = intent.getStringExtra("gcm.rawData64");
        if (stringExtra2 != null) {
            intent.putExtra("rawData", Base64.decode(stringExtra2, 0));
            intent.removeExtra("gcm.rawData64");
        }
        if ("com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
            zzi(context, intent);
        } else {
            startWakefulService(context, intent);
        }
        if (isOrderedBroadcast()) {
            setResultCode(-1);
        }
    }

    public void zzi(Context context, Intent intent) {
        startWakefulService(context, intent);
    }
}
