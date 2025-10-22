package com.flurry.sdk;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class em implements eq {
    private static em a;
    private final List<eq> b = b();

    public static synchronized em a() {
        if (a == null) {
            a = new em();
        }
        return a;
    }

    private em() {
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar, Context context) {
        Iterator<eq> it = this.b.iterator();
        while (it.hasNext()) {
            it.next().a(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void b(dg dgVar, Context context) {
        Iterator<eq> it = this.b.iterator();
        while (it.hasNext()) {
            it.next().b(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void c(dg dgVar, Context context) {
        Iterator<eq> it = this.b.iterator();
        while (it.hasNext()) {
            it.next().c(dgVar, context);
        }
    }

    @Override // com.flurry.sdk.eq
    public void a(dg dgVar) {
        Iterator<eq> it = this.b.iterator();
        while (it.hasNext()) {
            it.next().a(dgVar);
        }
    }

    private static List<eq> b() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new en("com.flurry.android.impl.analytics.FlurryAnalyticsModule", 10));
        arrayList.add(new en("com.flurry.android.impl.ads.FlurryAdModule", 10));
        return Collections.unmodifiableList(arrayList);
    }
}
