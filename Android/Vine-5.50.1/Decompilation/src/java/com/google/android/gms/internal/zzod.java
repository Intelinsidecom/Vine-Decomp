package com.google.android.gms.internal;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class zzod implements ThreadFactory {
    private final int mPriority;
    private final String zzamw;
    private final AtomicInteger zzamx;
    private final ThreadFactory zzamy;

    public zzod(String str) {
        this(str, 0);
    }

    public zzod(String str, int i) {
        this.zzamx = new AtomicInteger();
        this.zzamy = Executors.defaultThreadFactory();
        this.zzamw = (String) com.google.android.gms.common.internal.zzx.zzb(str, "Name must not be null");
        this.mPriority = i;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        Thread threadNewThread = this.zzamy.newThread(new zzoe(runnable, this.mPriority));
        threadNewThread.setName(this.zzamw + "[" + this.zzamx.getAndIncrement() + "]");
        return threadNewThread;
    }
}
