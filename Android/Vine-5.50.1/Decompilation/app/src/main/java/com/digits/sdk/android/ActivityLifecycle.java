package com.digits.sdk.android;

import android.app.Activity;

/* loaded from: classes.dex */
interface ActivityLifecycle {
    void onActivityResult(int i, int i2, Activity activity);

    void onDestroy();

    void onResume();
}
