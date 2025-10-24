package com.google.android.gms.internal;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzha
/* loaded from: classes.dex */
public class zzjb<T> implements zzje<T> {
    private final Object zzpK = new Object();
    private T zzLI = null;
    private boolean zzLJ = false;
    private boolean zzBy = false;
    private final zzjf zzLK = new zzjf();

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean z = false;
        if (mayInterruptIfRunning) {
            synchronized (this.zzpK) {
                if (!this.zzLJ) {
                    this.zzBy = true;
                    this.zzLJ = true;
                    this.zzpK.notifyAll();
                    this.zzLK.zzht();
                    z = true;
                }
            }
        }
        return z;
    }

    @Override // java.util.concurrent.Future
    public T get() {
        T t;
        synchronized (this.zzpK) {
            if (!this.zzLJ) {
                try {
                    this.zzpK.wait();
                } catch (InterruptedException e) {
                }
            }
            if (this.zzBy) {
                throw new CancellationException("CallbackFuture was cancelled.");
            }
            t = this.zzLI;
        }
        return t;
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) throws TimeoutException {
        T t;
        synchronized (this.zzpK) {
            if (!this.zzLJ) {
                try {
                    long millis = unit.toMillis(timeout);
                    if (millis != 0) {
                        this.zzpK.wait(millis);
                    }
                } catch (InterruptedException e) {
                }
            }
            if (!this.zzLJ) {
                throw new TimeoutException("CallbackFuture timed out.");
            }
            if (this.zzBy) {
                throw new CancellationException("CallbackFuture was cancelled.");
            }
            t = this.zzLI;
        }
        return t;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzBy;
        }
        return z;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzLJ;
        }
        return z;
    }

    @Override // com.google.android.gms.internal.zzje
    public void zzb(Runnable runnable) {
        this.zzLK.zzb(runnable);
    }

    public void zzc(Runnable runnable) {
        this.zzLK.zzc(runnable);
    }

    public void zzf(T t) {
        synchronized (this.zzpK) {
            if (this.zzBy) {
                return;
            }
            if (this.zzLJ) {
                throw new IllegalStateException("Provided CallbackFuture with multiple values.");
            }
            this.zzLJ = true;
            this.zzLI = t;
            this.zzpK.notifyAll();
            this.zzLK.zzht();
        }
    }
}
