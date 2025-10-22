package com.flurry.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import com.flurry.sdk.dr;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ds {
    private static ds b;
    private final dq<dr> a = new dq<>();

    public static synchronized ds a() {
        if (b == null) {
            b = new ds();
        }
        return b;
    }

    public boolean b() {
        return Build.VERSION.SDK_INT >= 14;
    }

    @TargetApi(14)
    private ds() {
        if (Build.VERSION.SDK_INT >= 14) {
            ((Application) dl.a().b()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.flurry.sdk.ds.1
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    a(activity, dr.a.kCreated);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity) {
                    a(activity, dr.a.kStarted);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity) {
                    a(activity, dr.a.kResumed);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity) {
                    a(activity, dr.a.kPaused);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity) {
                    a(activity, dr.a.kStopped);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                    a(activity, dr.a.kSaveState);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                    a(activity, dr.a.kDestroyed);
                }

                protected void a(Activity activity, dr.a aVar) {
                    Iterator it = ds.this.c().iterator();
                    while (it.hasNext()) {
                        ((dr) it.next()).a(activity, aVar);
                    }
                }
            });
        }
    }

    public synchronized void a(dr drVar) {
        this.a.a(drVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List<dr> c() {
        return this.a.a();
    }
}
