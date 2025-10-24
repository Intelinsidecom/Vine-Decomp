package com.flurry.sdk;

import com.flurry.sdk.ey;

/* loaded from: classes.dex */
public class ex {
    private static long a = 1000;
    private static ex b = null;
    private final ey c = new ey();

    public static synchronized ex a() {
        if (b == null) {
            b = new ex();
        }
        return b;
    }

    public ex() {
        this.c.a(a);
        this.c.a(true);
    }

    public synchronized void a(ey.a aVar) {
        this.c.a(aVar);
        if (!this.c.c() && this.c.d() > 0) {
            this.c.a();
        }
    }

    public synchronized boolean b(ey.a aVar) {
        boolean zB;
        zB = this.c.b(aVar);
        if (this.c.d() == 0) {
            this.c.b();
        }
        return zB;
    }
}
