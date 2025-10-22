package com.flurry.sdk;

import java.lang.Thread;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public final class dz {
    private static dz a;
    private final Map<Thread.UncaughtExceptionHandler, Void> c = new WeakHashMap();
    private final Thread.UncaughtExceptionHandler b = Thread.getDefaultUncaughtExceptionHandler();

    public static synchronized dz a() {
        if (a == null) {
            a = new dz();
        }
        return a;
    }

    public void a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        synchronized (this.c) {
            this.c.put(uncaughtExceptionHandler, null);
        }
    }

    private Set<Thread.UncaughtExceptionHandler> b() {
        Set<Thread.UncaughtExceptionHandler> setKeySet;
        synchronized (this.c) {
            setKeySet = this.c.keySet();
        }
        return setKeySet;
    }

    private dz() {
        Thread.setDefaultUncaughtExceptionHandler(new a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Thread thread, Throwable th) {
        Iterator<Thread.UncaughtExceptionHandler> it = b().iterator();
        while (it.hasNext()) {
            try {
                it.next().uncaughtException(thread, th);
            } catch (Throwable th2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Thread thread, Throwable th) {
        if (this.b != null) {
            this.b.uncaughtException(thread, th);
        }
    }

    final class a implements Thread.UncaughtExceptionHandler {
        private a() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            dz.this.a(thread, th);
            dz.this.b(thread, th);
        }
    }
}
