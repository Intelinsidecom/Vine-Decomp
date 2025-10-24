package com.google.android.gms.internal;

import android.os.Process;

/* loaded from: classes2.dex */
class zzoe implements Runnable {
    private final int mPriority;
    private final Runnable zzx;

    public zzoe(Runnable runnable, int i) {
        this.zzx = runnable;
        this.mPriority = i;
    }

    @Override // java.lang.Runnable
    public void run() throws SecurityException, IllegalArgumentException {
        Process.setThreadPriority(this.mPriority);
        this.zzx.run();
    }
}
