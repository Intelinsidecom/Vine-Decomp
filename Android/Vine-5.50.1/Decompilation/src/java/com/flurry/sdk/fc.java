package com.flurry.sdk;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public abstract class fc implements Runnable {
    private static final String a = fc.class.getSimpleName();
    PrintStream g;
    PrintWriter h;

    public abstract void a();

    @Override // java.lang.Runnable
    public final void run() {
        try {
            a();
        } catch (Throwable th) {
            if (this.g != null) {
                th.printStackTrace(this.g);
            } else if (this.h != null) {
                th.printStackTrace(this.h);
            } else {
                th.printStackTrace();
            }
            el.a(6, a, "", th);
        }
    }
}
