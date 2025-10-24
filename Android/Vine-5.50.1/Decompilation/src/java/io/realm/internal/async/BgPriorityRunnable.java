package io.realm.internal.async;

import android.os.Process;

/* loaded from: classes.dex */
public class BgPriorityRunnable implements Runnable {
    private final Runnable runnable;

    BgPriorityRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override // java.lang.Runnable
    public void run() throws SecurityException, IllegalArgumentException {
        Process.setThreadPriority(10);
        this.runnable.run();
    }
}
