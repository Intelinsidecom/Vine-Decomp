package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

/* loaded from: classes2.dex */
abstract class zzmk extends BroadcastReceiver {
    protected Context mContext;

    zzmk() {
    }

    public static <T extends zzmk> T zza(Context context, T t) {
        return (T) zza(context, t, GoogleApiAvailability.getInstance());
    }

    public static <T extends zzmk> T zza(Context context, T t, GoogleApiAvailability googleApiAvailability) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(t, intentFilter);
        t.mContext = context;
        if (googleApiAvailability.zzh(context, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE)) {
            return t;
        }
        t.zzpv();
        t.unregister();
        return null;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE.equals(data != null ? data.getSchemeSpecificPart() : null)) {
            zzpv();
            unregister();
        }
    }

    public synchronized void unregister() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this);
        }
        this.mContext = null;
    }

    protected abstract void zzpv();
}
