package com.digits.sdk.android;

import android.os.Build;

/* loaded from: classes.dex */
class DigitsUserAgent {
    private final String androidVersion;
    private final String appName;
    private final String digitsVersion;

    DigitsUserAgent() {
        this(Digits.getInstance().getVersion(), Build.VERSION.RELEASE, Digits.getInstance().getContext().getApplicationContext().getApplicationInfo().loadLabel(Digits.getInstance().getContext().getApplicationContext().getPackageManager()).toString());
    }

    DigitsUserAgent(String digitsVersion, String androidVersion, String appName) {
        this.digitsVersion = digitsVersion;
        this.appName = appName;
        this.androidVersion = androidVersion;
    }

    public String toString() {
        return "Digits/" + this.digitsVersion + " ( " + this.appName + "; Android " + this.androidVersion + ")";
    }
}
