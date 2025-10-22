package co.vine.android.prefetch.internal.alarm;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import co.vine.android.prefetch.PrefetchManager;
import co.vine.android.prefetch.internal.PreftechScheduler;
import co.vine.android.service.components.Components;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.FileLogger;
import co.vine.android.util.FileLoggers;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public class AlarmPrefetchScheduler implements PreftechScheduler {
    private final SLogger mLogger = ConsoleLoggers.PREFETCH.get();
    private final FileLogger mFileLogger = FileLoggers.PREFETCH.get();

    @Override // co.vine.android.prefetch.internal.PreftechScheduler
    public int schedule(PrefetchManager prefetchManager, boolean canSyncSoon) {
        Account account = prefetchManager.getSyncableAccount();
        if (account == null) {
            return -1;
        }
        cancel(prefetchManager);
        long timeMs = prefetchManager.getNextFetchDelay(canSyncSoon);
        schedule(prefetchManager.getApplicationContext(), timeMs);
        if (SLog.sLogsOn) {
            this.mLogger.i("Scheduled next sync to be {}s later.", Long.valueOf(timeMs / 1000));
            this.mFileLogger.write(this.mLogger, "Next sync scheduled at {}s", Long.valueOf(timeMs));
        }
        return (int) (timeMs / 1000);
    }

    private void schedule(Context context, long timeMs) {
        this.mFileLogger.write(this.mLogger, "Scheduled next sync to happen {}ms ", Long.valueOf(timeMs));
        AlarmManager am = (AlarmManager) context.getSystemService("alarm");
        am.set(2, SystemClock.elapsedRealtime() + timeMs, getPendingIntent(context, false));
    }

    private PendingIntent getPendingIntent(Context context, boolean manual) {
        return PendingIntent.getService(context, 0, Components.prefetchComponent().getPrefetchIntent(context, manual), 0);
    }

    @Override // co.vine.android.prefetch.internal.PreftechScheduler
    public void cancel(PrefetchManager prefetchManager) {
        Context context = prefetchManager.getApplicationContext();
        AlarmManager am = (AlarmManager) context.getSystemService("alarm");
        am.cancel(getPendingIntent(context, false));
        prefetchManager.onCancelled();
    }

    @Override // co.vine.android.prefetch.internal.PreftechScheduler
    public void runNow(PrefetchManager prefetchManager, long delayMs) {
        Context context = prefetchManager.getApplicationContext();
        context.startService(Components.prefetchComponent().getPrefetchIntent(context, true));
    }

    @Override // co.vine.android.prefetch.internal.PreftechScheduler
    public long getNextSync(long expected, PrefetchManager manager) {
        return expected;
    }

    @Override // co.vine.android.prefetch.internal.PreftechScheduler
    public void scheduleFailedFetch(PrefetchManager prefetchManager) {
        schedule(prefetchManager.getApplicationContext(), 300000L);
    }
}
