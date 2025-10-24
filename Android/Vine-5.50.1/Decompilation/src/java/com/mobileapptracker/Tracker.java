package com.mobileapptracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* loaded from: classes.dex */
public class Tracker extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws UnsupportedEncodingException {
        String rawReferrer;
        if (intent != null) {
            try {
                if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER") && (rawReferrer = intent.getStringExtra("referrer")) != null) {
                    String referrer = URLDecoder.decode(rawReferrer, "UTF-8");
                    Log.d("MobileAppTracker", "TUNE received referrer " + referrer);
                    context.getSharedPreferences("com.mobileapptracking", 0).edit().putString("mat_referrer", referrer).commit();
                    MobileAppTracker tune = MobileAppTracker.getInstance();
                    if (tune != null) {
                        tune.setInstallReferrer(referrer);
                        if (tune.gotGaid && !tune.notifiedPool) {
                            synchronized (tune.pool) {
                                tune.pool.notifyAll();
                                tune.notifiedPool = true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
