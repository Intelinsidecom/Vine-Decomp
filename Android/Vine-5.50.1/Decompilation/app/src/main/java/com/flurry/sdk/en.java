package com.flurry.sdk;

import android.os.Build;
import android.text.TextUtils;

/* loaded from: classes.dex */
public final class en extends ep {
    private final int a;

    public en(String str, int i) {
        super(a(str, i));
        this.a = i;
    }

    private static eq a(String str, int i) {
        if (!b(str, i)) {
            return null;
        }
        return eo.a(str);
    }

    private static boolean b(String str, int i) {
        return !TextUtils.isEmpty(str) && Build.VERSION.SDK_INT >= i;
    }
}
