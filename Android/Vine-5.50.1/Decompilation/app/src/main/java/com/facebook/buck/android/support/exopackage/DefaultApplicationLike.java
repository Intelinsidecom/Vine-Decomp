package com.facebook.buck.android.support.exopackage;

import android.app.Application;
import android.content.res.Configuration;

/* loaded from: classes.dex */
public class DefaultApplicationLike implements ApplicationLike {
    public DefaultApplicationLike() {
    }

    public DefaultApplicationLike(Application application) {
    }

    @Override // com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onCreate() {
    }

    @Override // com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onLowMemory() {
    }

    @Override // com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onTrimMemory(int level) {
    }

    @Override // com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onTerminate() {
    }

    @Override // com.facebook.buck.android.support.exopackage.ApplicationLike
    public void onConfigurationChanged(Configuration newConfig) {
    }
}
