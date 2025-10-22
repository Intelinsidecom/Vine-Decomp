package com.flurry.sdk;

import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
class dj {
    private Timer a;
    private a b;
    private b c;

    public interface b {
        void q();
    }

    class a extends TimerTask {
        private b b;

        a(b bVar) {
            this.b = bVar;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            dj.this.a();
            if (this.b != null) {
                this.b.q();
            }
        }
    }

    dj(b bVar) {
        this.c = bVar;
    }

    public synchronized void a(long j) {
        if (b()) {
            a();
        }
        this.a = new Timer("FlurrySessionTimer");
        this.b = new a(this.c);
        this.a.schedule(this.b, j);
    }

    public synchronized void a() {
        if (this.a != null) {
            this.a.cancel();
            this.a = null;
        }
        this.b = null;
    }

    public boolean b() {
        return this.a != null;
    }
}
