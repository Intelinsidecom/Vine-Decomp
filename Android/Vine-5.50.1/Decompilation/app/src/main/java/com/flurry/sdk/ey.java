package com.flurry.sdk;

import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ey {
    private static final String a = ey.class.getSimpleName();
    private final dq<a> b = new dq<>();
    private long c = 1000;
    private boolean d = true;
    private boolean e = false;
    private fc f = new fc() { // from class: com.flurry.sdk.ey.1
        @Override // com.flurry.sdk.fc
        public void a() {
            try {
                Iterator it = ey.this.f().iterator();
                while (it.hasNext()) {
                    ((a) it.next()).a(ey.this);
                }
            } catch (Throwable th) {
                el.a(6, ey.a, "", th);
            }
            if (ey.this.d && ey.this.e) {
                dl.a().a(ey.this.f, ey.this.c);
            }
        }
    };

    public interface a {
        void a(ey eyVar);
    }

    public void a(long j) {
        this.c = j;
    }

    public void a(boolean z) {
        this.d = z;
    }

    public synchronized void a() {
        if (!this.e) {
            dl.a().a(this.f, this.c);
            this.e = true;
        }
    }

    public synchronized void b() {
        if (this.e) {
            dl.a().b(this.f);
            this.e = false;
        }
    }

    public synchronized boolean c() {
        return this.e;
    }

    public synchronized void a(a aVar) {
        this.b.a(aVar);
    }

    public synchronized boolean b(a aVar) {
        return this.b.b(aVar);
    }

    public synchronized int d() {
        return this.b.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List<a> f() {
        return this.b.a();
    }
}
