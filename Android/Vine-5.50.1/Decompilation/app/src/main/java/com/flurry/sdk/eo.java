package com.flurry.sdk;

import android.text.TextUtils;

/* loaded from: classes.dex */
public final class eo {
    private static final String a = eo.class.getSimpleName();

    public static eq a(String str) {
        eq eqVar;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            eqVar = (eq) Class.forName(str).getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            el.a(5, a, "FlurryModule " + str + " is not available:", e);
            eqVar = null;
        }
        return eqVar;
    }
}
