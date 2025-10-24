package co.vine.android.prefetch;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import co.vine.android.StandalonePreference;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.prefetch.internal.PreftechScheduler;
import co.vine.android.prefetch.internal.alarm.AlarmPrefetchScheduler;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.FileLogger;
import co.vine.android.util.FileLoggers;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.UserDataHelper;
import co.vine.android.util.analytics.BehaviorManager;
import co.vine.android.util.analytics.FlurryUtils;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public class PrefetchManager {
    private static PrefetchManager sInstance;
    private final Context mAppContext;
    private final AppController mAppController;
    private final PreftechScheduler mImpl;
    private final SharedPreferences mPref;
    private final UserDataHelper mUserDataHelper;
    public static final long[] INTERVALS = {7200000, 14400000, 28800000};
    public static final long DEFAULT_INTERVAL = INTERVALS[0];
    private final SLogger mLogger = ConsoleLoggers.PREFETCH.get();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final FileLogger mFileLogger = FileLoggers.PREFETCH.get();

    public static synchronized PrefetchManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PrefetchManager(context);
        }
        return sInstance;
    }

    private PrefetchManager(Context context) {
        this.mAppContext = context;
        this.mAppController = AppController.getInstance(context);
        this.mUserDataHelper = new UserDataHelper(context);
        this.mPref = StandalonePreference.PREFETCH_STAT.getPref(context);
        if (BuildUtil.isApi21Lollipop()) {
        }
        this.mImpl = new AlarmPrefetchScheduler();
    }

    public long getNextFetchDelay(boolean canSyncSoon) {
        long lastSync = getLastSync();
        long interval = getInterval();
        long requestTime = System.currentTimeMillis();
        long[] timeTuple = findStartAndEndTimes(lastSync, interval, requestTime);
        if (timeTuple == null) {
            return 0L;
        }
        long start = timeTuple[0];
        long end = timeTuple[1];
        long searchedTime = BehaviorManager.getInstance(this.mAppContext).searchForBestDelay(start, end);
        String log = MessageFormatter.toStringMessage("Search with interval {}, start {}, end {}, searched {}, took {}ms.", Long.valueOf(interval), Long.valueOf((start - requestTime) / 1000), Long.valueOf((end - requestTime) / 1000), Long.valueOf((searchedTime - requestTime) / 1000), Long.valueOf(System.currentTimeMillis() - requestTime));
        this.mFileLogger.write(this.mLogger, log, new Object[0]);
        return Math.max(searchedTime - requestTime, 60000L);
    }

    public boolean isClientPrefetchEnabled() {
        return ClientFlagsHelper.prefetchEnabled(this.mAppContext) || BuildUtil.isLogsOn();
    }

    static long[] findStartAndEndTimes(long lastSync, long interval, long requestTime) {
        if (lastSync + interval + 0.4f < requestTime) {
            return null;
        }
        long midpoint = lastSync + interval;
        long minStart = lastSync + getMinimumDelay(interval);
        long start = Math.max(midpoint - ((long) (0.4f * interval)), minStart);
        long end = Math.max(((long) (0.8f * interval)) + start, ((long) (0.4f * interval)) + midpoint);
        return new long[]{start, end};
    }

    public static long getMinimumDelay(long interval) {
        return (long) (interval * 0.3f);
    }

    public void onPostFetchOperationComplete(boolean prefetch, NetworkOperation.NetworkOperationResult lastResult, UrlCachePolicy resolvedCachePolicy) {
        switch (lastResult) {
            case CACHED:
                if (!prefetch) {
                    long count = this.mPref.getLong("cache_count", 0L);
                    this.mPref.edit().putLong("cache_count", count + 1).apply();
                    break;
                }
                break;
            case NETWORK:
                if (prefetch) {
                    long count2 = this.mPref.getLong("fetched_count", 0L);
                    this.mPref.edit().putLong("fetched_count", count2 + 1).apply();
                    break;
                } else if (resolvedCachePolicy.mCachedResponseAllowed) {
                    long count3 = this.mPref.getLong("miss_count", 0L);
                    this.mPref.edit().putLong("miss_count", count3 + 1).apply();
                    break;
                }
                break;
            case FAILURE:
                if (!prefetch && resolvedCachePolicy.mCachedResponseAllowed) {
                    long count4 = this.mPref.getLong("miss_count", 0L);
                    this.mPref.edit().putLong("miss_count", count4 + 1).apply();
                    break;
                }
                break;
        }
    }

    public String[] getStats() {
        long count = this.mPref.getLong("miss_count", 0L);
        String[] stats = {"Total fetched " + count + " when sync happened.", "Was able to save " + count + " when loading.", "Had to fetch " + count + " when loading."};
        long count2 = this.mPref.getLong("cache_count", 0L);
        long count3 = this.mPref.getLong("fetched_count", 0L);
        return stats;
    }

    public long getNextSync() {
        return this.mImpl.getNextSync(this.mPref.getLong("Prefetch_next_expected", -2L), this);
    }

    public void onDeviceBoot() {
        long currentStart = getCurrentSyncStartTime();
        if (currentStart != -1 && System.currentTimeMillis() - currentStart > 86400000) {
            setCurrentSyncStart(-1L);
        }
        scheduleNextPrefetch(true);
    }

    public void onCancelled() {
        this.mPref.edit().putLong("Prefetch_next_expected", -2L).apply();
    }

    public void onStartActualSyncAction() {
        setCurrentSyncStart(System.currentTimeMillis());
        setLastSuccessfulStart(System.currentTimeMillis());
    }

    public static class PrefetchFilter extends IntentFilter {
        public PrefetchFilter() {
            addAction("co.vine.android.prefetch.start");
            addAction("co.vine.android.prefetch.end");
            addAction("co.vine.android.prefetch.schedule");
        }

        public boolean isEnd(String bcAction) {
            return "co.vine.android.prefetch.end".equals(bcAction);
        }

        public int getScheduledTimeInSeconds(Bundle extras) {
            if (extras != null) {
                return extras.getInt("seconds_from_now", -2);
            }
            return -2;
        }
    }

    public static PrefetchFilter getStateChangeFilter() {
        return new PrefetchFilter();
    }

    public void scheduleNextPrefetch(boolean canSyncSoon) {
        if (isClientPrefetchEnabled()) {
            int next = this.mImpl.schedule(this, canSyncSoon);
            if (next > -1) {
                int value = next * 1000;
                this.mPref.edit().putLong("Prefetch_next_expected", value + System.currentTimeMillis()).apply();
                this.mAppContext.sendBroadcast(new Intent("co.vine.android.prefetch.schedule").putExtra("seconds_from_now", value), CrossConstants.BROADCAST_PERMISSION);
                return;
            }
            return;
        }
        this.mLogger.d("Client prefetch is disabled, schedule will do nothing.");
    }

    public void cancelNextPrefetch() {
        this.mImpl.cancel(this);
        this.mPref.edit().putLong("Prefetch_next_expected", -1L).apply();
        this.mAppContext.sendBroadcast(new Intent("co.vine.android.prefetch.schedule").putExtra("seconds_from_now", -1), CrossConstants.BROADCAST_PERMISSION);
    }

    public void run(final long delayMs) {
        this.mLogger.d("Scheduling a sync {}ms from now.", Long.valueOf(delayMs));
        this.mHandler.postDelayed(new Runnable() { // from class: co.vine.android.prefetch.PrefetchManager.1
            @Override // java.lang.Runnable
            public void run() {
                PrefetchManager.this.mImpl.runNow(PrefetchManager.this, delayMs);
            }
        }, delayMs);
    }

    public static void injectPrefetchArguments(Bundle serviceBundle, long maxStaleTime) {
        serviceBundle.putParcelable("cache_policy", UrlCachePolicy.cacheAllowedPolicy(false, maxStaleTime));
        serviceBundle.putBoolean("user_init", false);
        serviceBundle.putBoolean("is_polling", true);
    }

    public boolean isEnabled() {
        Account account = getActiveAccount();
        return account != null && this.mUserDataHelper.getBoolean(account, "Prefetch_enabled", false);
    }

    public long getInterval() {
        return this.mUserDataHelper.getLong(getActiveAccount(), "Prefetch_interval", DEFAULT_INTERVAL);
    }

    public long getLastSync() {
        return this.mUserDataHelper.getLong(getActiveAccount(), "Prefetch_last_sync", -1L);
    }

    public long getCurrentSyncStartTime() {
        Account account = getActiveAccount();
        if (account != null) {
            return this.mUserDataHelper.getLong(account, "Prefetch_current_start", -1L);
        }
        return -1L;
    }

    public void delayNextPrefetch() {
        cancelNextPrefetch();
        scheduleNextPrefetch(false);
    }

    public boolean isSyncPending() {
        return getCurrentSyncStartTime() >= 0;
    }

    public boolean isWifiOnly() {
        return this.mUserDataHelper.getBoolean(getActiveAccount(), "Prefetch_wifi_only", true);
    }

    public boolean isChargingRequired() {
        return this.mUserDataHelper.getBoolean(getActiveAccount(), "Prefetch_charging_required", true);
    }

    public boolean isRoamingAllowed() {
        return this.mUserDataHelper.getBoolean(getActiveAccount(), "Prefetch_roaming_allowed", false);
    }

    public void setLastSync(long timeMillis) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setLong(account, "Prefetch_last_sync", timeMillis);
        }
    }

    public void setChargingRequired(boolean isRequired) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setBoolean(account, "Prefetch_charging_required", isRequired);
        }
    }

    public void setWifiOnly(boolean isWifiOnly) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setBoolean(account, "Prefetch_wifi_only", isWifiOnly);
        }
    }

    public void notifySyncStart(Context context) {
        FlurryUtils.trackPrefetchStart(getLastSyncStart());
        this.mPref.edit().putLong("Prefetch_last_start", System.currentTimeMillis()).apply();
        context.sendBroadcast(new Intent("co.vine.android.prefetch.start"), CrossConstants.BROADCAST_PERMISSION);
    }

    public long getLastSyncStart() {
        return this.mPref.getLong("Prefetch_last_start", -1L);
    }

    public void setLastSuccessfulStart(long currentSyncStart) {
        this.mPref.edit().putLong("Prefetch_last_successful_start", currentSyncStart).apply();
    }

    public void setCurrentSyncStart(long currentSyncStart) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setLong(account, "Prefetch_current_start", currentSyncStart);
        }
    }

    public long getLastSuccessfulSyncStart() {
        return this.mPref.getLong("Prefetch_last_successful_start", -1L);
    }

    public void notifySyncEnd(Context context, String s, boolean success, boolean manual) {
        if (success) {
            long duration = System.currentTimeMillis() - getCurrentSyncStartTime();
            this.mFileLogger.write(this.mLogger, "Sync completed, took {}ms.\n", Long.valueOf(duration));
            setLastSync(System.currentTimeMillis());
            FlurryUtils.trackPrefetchEnd(duration);
            scheduleNextPrefetch(false);
        } else {
            FlurryUtils.trackPrefetchEnd(-1L);
            if (!manual) {
                scheduleFailedFetch();
            }
        }
        setCurrentSyncStart(-1L);
        if (s != null) {
            context.sendBroadcast(new Intent("co.vine.android.prefetch.end").putExtra("error", s), CrossConstants.BROADCAST_PERMISSION);
        } else {
            context.sendBroadcast(new Intent("co.vine.android.prefetch.end"), CrossConstants.BROADCAST_PERMISSION);
        }
    }

    private void scheduleFailedFetch() {
        this.mImpl.scheduleFailedFetch(this);
    }

    public void setRoamingAllowed(boolean isRoamingAllowed) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setBoolean(account, "Prefetch_roaming_allowed", isRoamingAllowed);
        }
    }

    public void setInterval(long interval) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setLong(account, "Prefetch_interval", interval);
        }
    }

    public Account getActiveAccount() {
        String name;
        Session session = null;
        try {
            session = this.mAppController.getActiveSessionReadOnly();
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
        if (session == null || (name = session.getUsername()) == null) {
            return null;
        }
        Account account = VineAccountHelper.getAccount(this.mAppContext, session.getUserId(), name);
        return account;
    }

    public Account getSyncableAccount() {
        Account account = getActiveAccount();
        if (account != null && !this.mUserDataHelper.getBoolean(account, "Prefetch_enabled", false)) {
            this.mLogger.i("Account is not syncable.");
            return null;
        }
        return account;
    }

    public boolean setEnabled(boolean isEnabled) {
        Account account = getActiveAccount();
        if (account != null) {
            this.mUserDataHelper.setBoolean(account, "Prefetch_enabled", isEnabled);
            if (isEnabled) {
                scheduleNextPrefetch(false);
            } else {
                cancelNextPrefetch();
            }
        }
        return isEnabled();
    }

    public boolean isSuitableToSync() {
        if (!isEnabled()) {
            this.mFileLogger.write(this.mLogger, "Sync is not suitable because it may be disabled right now.", new Object[0]);
            return false;
        }
        if (isChargingRequired() && !SystemUtil.isBatteryCharging(this.mAppContext)) {
            this.mFileLogger.write(this.mLogger, "Sync is not suitable because charging is required.", new Object[0]);
            return false;
        }
        if (isWifiOnly() && !SystemUtil.isOnWifi(this.mAppContext)) {
            this.mFileLogger.write(this.mLogger, "Sync is not suitable because wifi connection is required.", new Object[0]);
            return false;
        }
        if (!isRoamingAllowed() && SystemUtil.isRoaming(this.mAppContext)) {
            this.mFileLogger.write(this.mLogger, "Sync is not suitable because device might be roaming.", new Object[0]);
            return false;
        }
        if (wasJustStarted()) {
            this.mFileLogger.write(this.mLogger, "Sync is not suitable because it was just started.", new Object[0]);
            return false;
        }
        return true;
    }

    private boolean wasJustStarted() {
        return System.currentTimeMillis() - getLastSuccessfulSyncStart() < 300000;
    }

    public Context getApplicationContext() {
        return this.mAppContext;
    }
}
