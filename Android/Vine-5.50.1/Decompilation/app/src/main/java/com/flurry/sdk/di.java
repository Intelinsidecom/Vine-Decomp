package com.flurry.sdk;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class di {
    private static final String a = di.class.getSimpleName();
    private static di b;
    private final Map<String, dg> c = new HashMap();
    private final Map<Context, dg> d = new WeakHashMap();
    private final Object e = new Object();
    private dg f;

    public static synchronized di a() {
        if (b == null) {
            b = new di();
        }
        return b;
    }

    private di() {
    }

    public synchronized int b() {
        return this.d.size();
    }

    public dg c() {
        dg dgVar;
        synchronized (this.e) {
            dgVar = this.f;
        }
        return dgVar;
    }

    public void a(dg dgVar) {
        synchronized (this.e) {
            this.f = dgVar;
        }
    }

    public synchronized void a(Context context, String str) {
        dg dgVar;
        dl.a(context);
        es.a().b();
        dw.a().b();
        dg dgVar2 = this.d.get(context);
        if (dgVar2 != null) {
            el.d(a, "Session already started with context: " + context + " count:" + dgVar2.g());
        } else {
            if (this.c.containsKey(str)) {
                dgVar = this.c.get(str);
            } else {
                dgVar = new dg(str);
                this.c.put(str, dgVar);
                dgVar.a(context);
            }
            this.d.put(context, dgVar);
            a(dgVar);
            dgVar.b(context);
        }
    }

    public synchronized void a(Context context) {
        dg dgVarRemove = this.d.remove(context);
        if (dgVarRemove == null) {
            el.d(a, "Session cannot be ended, session not found for context: " + context);
        } else {
            dgVarRemove.c(context);
        }
    }

    public synchronized void a(String str) {
        if (!this.c.containsKey(str)) {
            el.a(6, a, "Ended session is not in the session map! Maybe it was already destroyed.");
        } else {
            dg dgVarC = c();
            if (dgVarC != null && TextUtils.equals(dgVarC.j(), str)) {
                a((dg) null);
            }
            this.c.remove(str);
        }
    }

    public synchronized void d() {
        for (Map.Entry<Context, dg> entry : this.d.entrySet()) {
            entry.getValue().c(entry.getKey());
        }
        this.d.clear();
        Iterator it = new ArrayList(this.c.values()).iterator();
        while (it.hasNext()) {
            ((dg) it.next()).c();
        }
        this.c.clear();
        a((dg) null);
    }
}
