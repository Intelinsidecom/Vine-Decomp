package co.vine.android.util.analytics;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import co.vine.android.StandalonePreference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class BehaviorManager {
    private static BehaviorManager sInstance;
    private final SharedPreferences mPref;
    private final SharedPreferences mPrefUrls;
    private final SharedPreferences mPrefUrlsRecents;
    private final BehaviorStorage mStorage = new BehaviorStorage() { // from class: co.vine.android.util.analytics.BehaviorManager.4
        @Override // co.vine.android.util.analytics.BehaviorManager.BehaviorStorage
        public long getCount(String key, long defaultValue) {
            return BehaviorManager.this.mPref.getLong(key, defaultValue);
        }
    };

    public interface BehaviorStorage {
        long getCount(String str, long j);
    }

    public static synchronized BehaviorManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BehaviorManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private BehaviorManager(Context context) {
        this.mPref = StandalonePreference.BHEAVIOR_MANAGER.getPref(context);
        long val = this.mPref.getLong("first_date", 0L);
        if (val == 0) {
            this.mPref.edit().putLong("first_date", System.currentTimeMillis()).apply();
        }
        this.mPrefUrls = StandalonePreference.BEHAVIOR_URLS.getPref(context);
        this.mPrefUrlsRecents = StandalonePreference.BEHAVIOR_URL_RECENTS.getPref(context);
    }

    public void onVideoLoaded() {
        Calendar c = Calendar.getInstance();
        String key = getKey(c.get(7), c.get(11), c.get(12) / 15);
        long val = this.mPref.getLong(key, 0L);
        this.mPref.edit().putLong(key, 1 + val).apply();
    }

    public void onFetchPosts(String url, int page) {
        truncateIfNeeded();
        String path = Uri.parse(url).getPath();
        String key = path + "?page=" + page;
        long oldValue = this.mPrefUrls.getLong(path, 0L);
        SharedPreferences.Editor edit = this.mPrefUrls.edit();
        edit.putLong(key, oldValue + 1);
        long oldValue2 = this.mPrefUrls.getLong(path, 0L);
        edit.putLong(path, oldValue2 + 1);
        edit.apply();
        this.mPrefUrlsRecents.edit().putLong(key, System.currentTimeMillis()).apply();
    }

    private void truncateIfNeeded() {
        final Map<String, ?> all = this.mPrefUrlsRecents.getAll();
        if (all.size() > 100) {
            ArrayList<String> keys = new ArrayList<>(all.keySet());
            Collections.sort(keys, new Comparator<String>() { // from class: co.vine.android.util.analytics.BehaviorManager.1
                @Override // java.util.Comparator
                public int compare(String lhs, String rhs) {
                    long diff = ((Long) all.get(lhs)).longValue() - ((Long) all.get(rhs)).longValue();
                    if (diff > 0) {
                        return 1;
                    }
                    if (diff < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
            SharedPreferences.Editor editRecents = this.mPrefUrlsRecents.edit();
            SharedPreferences.Editor edit = this.mPrefUrls.edit();
            for (int i = 0; i < 50; i++) {
                String key = keys.get(i);
                edit.remove(key);
                editRecents.remove(key);
            }
            edit.apply();
            editRecents.apply();
        }
    }

    private static String getKey(int dayOfWeek, int hourOfDay, int minuteByFifteenth) {
        return String.valueOf((dayOfWeek * 1000) + (hourOfDay * 10) + minuteByFifteenth);
    }

    public String[] getUsagesStringsByTimelineForStatView() {
        final Map<String, ?> all = this.mPrefUrls.getAll();
        ArrayList<String> keys = new ArrayList<>(all.keySet());
        Collections.sort(keys, new Comparator<String>() { // from class: co.vine.android.util.analytics.BehaviorManager.2
            @Override // java.util.Comparator
            public int compare(String lhs, String rhs) {
                long diff = ((Long) all.get(rhs)).longValue() - ((Long) all.get(lhs)).longValue();
                return diff > 0 ? 1 : -1;
            }
        });
        int size = Math.min(keys.size(), 10);
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            String key = keys.get(i);
            r[i] = key + " (" + all.get(keys.get(i)) + ")";
        }
        return r;
    }

    public String[] getTopUsagesTimelineEndpoints() {
        final Map<String, ?> all = this.mPrefUrls.getAll();
        ArrayList<String> keys = new ArrayList<>(all.keySet());
        Collections.sort(keys, new Comparator<String>() { // from class: co.vine.android.util.analytics.BehaviorManager.3
            @Override // java.util.Comparator
            public int compare(String lhs, String rhs) {
                long diff = ((Long) all.get(rhs)).longValue() - ((Long) all.get(lhs)).longValue();
                if (diff > 0) {
                    if (diff >= 2147483647L) {
                        return Integer.MAX_VALUE;
                    }
                    return (int) diff;
                }
                if (diff == 0) {
                    return 0;
                }
                if (diff <= -2147483648L) {
                    return Integer.MIN_VALUE;
                }
                return (int) diff;
            }
        });
        int size = Math.min(keys.size(), 10);
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            r[i] = keys.get(i);
        }
        return r;
    }

    public long[] getUsagesByHourOfDay() {
        long[] usages = new long[24];
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j <= 23; j++) {
                for (int k = 0; k <= 3; k++) {
                    usages[j] = usages[j] + this.mPref.getLong(getKey(i, j, k), 0L);
                }
            }
        }
        return usages;
    }

    public long[] getUsagesByDateOfWeek() {
        long[] usages = new long[7];
        for (int i = 1; i <= 7; i++) {
            int sum = 0;
            for (int j = 0; j <= 23; j++) {
                for (int k = 0; k <= 3; k++) {
                    sum = (int) (sum + this.mPref.getLong(getKey(i, j, k), 0L));
                }
            }
            usages[i - 1] = sum;
        }
        return usages;
    }

    public void showUsageView(Activity activity) {
        LinearLayout view = new LinearLayout(activity);
        view.setGravity(17);
        BehaviorUsageView behaviorView = new BehaviorUsageView(activity);
        view.addView(behaviorView);
        activity.getWindow().addContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public long searchForBestDelay(long start, long end) {
        return searchForBestDelay(start, end, this.mStorage);
    }

    public static long searchForBestDelay(long start, long end, BehaviorStorage storage) {
        Calendar cal = Calendar.getInstance();
        final HashMap<Long, Long> counts = new HashMap<>();
        ArrayList<Long> times = new ArrayList<>();
        for (long time = start; time < end; time += 900000) {
            cal.setTimeInMillis(time);
            int hourOfDay = cal.get(11);
            int minute = cal.get(12) / 15;
            int dayOfWeek = cal.get(7);
            long count = storage.getCount(getKey(dayOfWeek, hourOfDay, minute), 0L);
            counts.put(Long.valueOf(time), Long.valueOf(count));
            times.add(Long.valueOf(time));
        }
        Collections.sort(times, new Comparator<Long>() { // from class: co.vine.android.util.analytics.BehaviorManager.5
            @Override // java.util.Comparator
            public int compare(Long lhs, Long rhs) {
                long diff = ((Long) counts.get(rhs)).longValue() - ((Long) counts.get(lhs)).longValue();
                return diff > 0 ? 1 : -1;
            }
        });
        long time2 = times.get(0).longValue() - 900000;
        return Math.max(time2, start);
    }
}
