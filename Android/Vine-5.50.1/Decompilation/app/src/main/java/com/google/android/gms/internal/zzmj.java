package com.google.android.gms.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public abstract class zzmj {
    private static final ExecutorService zzagQ = Executors.newFixedThreadPool(2, new zzod("GAC_Executor"));

    public static ExecutorService zzpz() {
        return zzagQ;
    }
}
