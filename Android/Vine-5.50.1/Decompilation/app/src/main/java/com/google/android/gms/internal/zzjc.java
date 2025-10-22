package com.google.android.gms.internal;

import java.util.concurrent.TimeUnit;

@zzha
/* loaded from: classes.dex */
public class zzjc<T> implements zzje<T> {
    private final T zzLI;
    private final zzjf zzLK = new zzjf();

    public zzjc(T t) {
        this.zzLI = t;
        this.zzLK.zzht();
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public T get() {
        return this.zzLI;
    }

    @Override // java.util.concurrent.Future
    public T get(long timeout, TimeUnit unit) {
        return this.zzLI;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // com.google.android.gms.internal.zzje
    public void zzb(Runnable runnable) {
        this.zzLK.zzb(runnable);
    }
}
