package com.flurry.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.flurry.sdk.cv;
import com.flurry.sdk.el;
import com.flurry.sdk.fb;

/* loaded from: classes.dex */
public final class InstallReceiver extends BroadcastReceiver {
    static final String a = InstallReceiver.class.getSimpleName();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        el.a(4, a, "Received an Install nofication of " + intent.getAction());
        String string = intent.getExtras().getString("referrer");
        el.a(4, a, "Received an Install referrer of " + string);
        if (string == null || !"com.android.vending.INSTALL_REFERRER".equals(intent.getAction())) {
            el.a(5, a, "referrer is null");
            return;
        }
        if (!string.contains("=")) {
            el.a(4, a, "referrer is before decoding: " + string);
            string = fb.c(string);
            el.a(4, a, "referrer is: " + string);
        }
        new cv(context).a(string);
    }
}
