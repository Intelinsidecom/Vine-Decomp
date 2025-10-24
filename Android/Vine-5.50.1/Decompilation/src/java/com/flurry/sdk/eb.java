package com.flurry.sdk;

import java.util.Comparator;

/* loaded from: classes.dex */
public class eb implements Comparator<Runnable> {
    private static final String a = eb.class.getSimpleName();

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(Runnable runnable, Runnable runnable2) {
        int iA = a(runnable);
        int iA2 = a(runnable2);
        if (iA < iA2) {
            return -1;
        }
        if (iA > iA2) {
            return 1;
        }
        return 0;
    }

    private int a(Runnable runnable) {
        int i;
        if (runnable == null) {
            return Integer.MAX_VALUE;
        }
        if (runnable instanceof ec) {
            fd fdVar = (fd) ((ec) runnable).a();
            if (fdVar == null) {
                i = Integer.MAX_VALUE;
            } else {
                i = fdVar.i();
            }
            return i;
        }
        if (runnable instanceof fd) {
            return ((fd) runnable).i();
        }
        el.a(6, a, "Unknown runnable class: " + runnable.getClass().getName());
        return Integer.MAX_VALUE;
    }
}
