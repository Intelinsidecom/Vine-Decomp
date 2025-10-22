package com.flurry.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/* loaded from: classes.dex */
public class dl {
    private static dl a;
    private final Context b;
    private final Handler c = new Handler(Looper.getMainLooper());
    private final Handler d;

    public static synchronized void a(Context context) {
        if (a == null) {
            if (context == null) {
                throw new IllegalArgumentException("Context cannot be null");
            }
            a = new dl(context);
        }
    }

    public static dl a() {
        return a;
    }

    private dl(Context context) {
        this.b = context.getApplicationContext();
        HandlerThread handlerThread = new HandlerThread("FlurryAgent");
        handlerThread.start();
        this.d = new Handler(handlerThread.getLooper());
    }

    public Context b() {
        return this.b;
    }

    public PackageManager c() {
        return this.b.getPackageManager();
    }

    public void a(Runnable runnable) {
        if (runnable != null) {
            this.c.post(runnable);
        }
    }

    public void a(Runnable runnable, long j) {
        if (runnable != null) {
            this.c.postDelayed(runnable, j);
        }
    }

    public void b(Runnable runnable) {
        if (runnable != null) {
            this.c.removeCallbacks(runnable);
        }
    }

    public void c(Runnable runnable) {
        if (runnable != null) {
            this.d.post(runnable);
        }
    }

    public void b(Runnable runnable, long j) {
        if (runnable != null) {
            this.d.postDelayed(runnable, j);
        }
    }

    public void d(Runnable runnable) {
        if (runnable != null) {
            this.d.removeCallbacks(runnable);
        }
    }
}
