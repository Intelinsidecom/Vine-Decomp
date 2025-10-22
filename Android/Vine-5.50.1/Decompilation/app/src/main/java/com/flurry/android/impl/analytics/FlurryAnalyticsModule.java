package com.flurry.android.impl.analytics;

import android.content.Context;
import com.flurry.sdk.da;
import com.flurry.sdk.dg;
import com.flurry.sdk.eq;

/* loaded from: classes.dex */
public class FlurryAnalyticsModule implements eq {
    private static FlurryAnalyticsModule a;
    private da b;

    public static synchronized FlurryAnalyticsModule getInstance() {
        if (a == null) {
            a = new FlurryAnalyticsModule();
        }
        return a;
    }

    private FlurryAnalyticsModule() {
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar, Context context) {
        if (this.b == null) {
            this.b = new da();
        }
    }

    @Override // com.flurry.sdk.eq
    public void b(dg dgVar, Context context) {
    }

    @Override // com.flurry.sdk.eq
    public void c(dg dgVar, Context context) {
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar) {
    }

    public da a() {
        return this.b;
    }
}
