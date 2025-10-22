package co.vine.android.prefetch.internal;

import co.vine.android.prefetch.PrefetchManager;

/* loaded from: classes.dex */
public interface PreftechScheduler {
    void cancel(PrefetchManager prefetchManager);

    long getNextSync(long j, PrefetchManager prefetchManager);

    void runNow(PrefetchManager prefetchManager, long j);

    int schedule(PrefetchManager prefetchManager, boolean z);

    void scheduleFailedFetch(PrefetchManager prefetchManager);
}
