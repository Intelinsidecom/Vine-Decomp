package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@zzha
/* loaded from: classes.dex */
public final class zzio {
    private static final ExecutorService zzKA = Executors.newFixedThreadPool(10, zzay("Default"));
    private static final ExecutorService zzKB = Executors.newFixedThreadPool(5, zzay("Loader"));

    public static zzje<Void> zza(int i, final Runnable runnable) {
        return i == 1 ? zza(zzKB, new Callable<Void>() { // from class: com.google.android.gms.internal.zzio.1
            @Override // java.util.concurrent.Callable
            /* renamed from: zzdm, reason: merged with bridge method [inline-methods] */
            public Void call() {
                runnable.run();
                return null;
            }
        }) : zza(zzKA, new Callable<Void>() { // from class: com.google.android.gms.internal.zzio.2
            @Override // java.util.concurrent.Callable
            /* renamed from: zzdm, reason: merged with bridge method [inline-methods] */
            public Void call() {
                runnable.run();
                return null;
            }
        });
    }

    public static zzje<Void> zza(Runnable runnable) {
        return zza(0, runnable);
    }

    public static <T> zzje<T> zza(Callable<T> callable) {
        return zza(zzKA, callable);
    }

    public static <T> zzje<T> zza(ExecutorService executorService, final Callable<T> callable) {
        final zzjb zzjbVar = new zzjb();
        try {
            final Future<?> futureSubmit = executorService.submit(new Runnable() { // from class: com.google.android.gms.internal.zzio.3
                @Override // java.lang.Runnable
                public void run() throws SecurityException, IllegalArgumentException {
                    try {
                        Process.setThreadPriority(10);
                        zzjbVar.zzf(callable.call());
                    } catch (Exception e) {
                        com.google.android.gms.ads.internal.zzp.zzbA().zzb((Throwable) e, true);
                        zzjbVar.cancel(true);
                    }
                }
            });
            zzjbVar.zzc(new Runnable() { // from class: com.google.android.gms.internal.zzio.4
                @Override // java.lang.Runnable
                public void run() {
                    if (zzjbVar.isCancelled()) {
                        futureSubmit.cancel(true);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Thread execution is rejected.", e);
            zzjbVar.cancel(true);
        }
        return zzjbVar;
    }

    private static ThreadFactory zzay(final String str) {
        return new ThreadFactory() { // from class: com.google.android.gms.internal.zzio.5
            private final AtomicInteger zzKG = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AdWorker(" + str + ") #" + this.zzKG.getAndIncrement());
            }
        };
    }
}
