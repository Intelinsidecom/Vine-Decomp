package com.flurry.sdk;

import android.content.Context;
import java.io.File;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class cv {
    private static final String b = cv.class.getSimpleName();
    boolean a;
    private final cw c;
    private final File d;
    private String e;

    public cv() {
        this(dl.a().b());
    }

    public cv(Context context) {
        this.c = new cw();
        this.d = context.getFileStreamPath(".flurryinstallreceiver.");
        el.a(3, b, "Referrer file name if it exists:  " + this.d);
    }

    public synchronized void a() {
        this.d.delete();
        this.e = null;
        this.a = true;
    }

    public synchronized Map<String, List<String>> a(boolean z) {
        Map<String, List<String>> mapA;
        b();
        mapA = this.c.a(this.e);
        if (z) {
            a();
        }
        return mapA;
    }

    public synchronized void a(String str) {
        this.a = true;
        b(str);
        c();
    }

    private void b(String str) {
        if (str != null) {
            this.e = str;
        }
    }

    private void b() throws Throwable {
        if (!this.a) {
            this.a = true;
            el.a(4, b, "Loading referrer info from file: " + this.d.getAbsolutePath());
            String strC = fa.c(this.d);
            el.a(b, "Referrer file contents: " + strC);
            b(strC);
        }
    }

    private void c() throws Throwable {
        fa.a(this.d, this.e);
    }
}
