package co.vine.android.analytics;

import android.os.SystemClock;
import co.vine.android.util.SparseArray;
import java.util.HashMap;

/* loaded from: classes.dex */
public class TimeTracker {
    private static final SparseArray<HashMap<String, Long>> sTimeTrackers = new SparseArray<>();
    private static final int[] sLOCK = new int[0];

    public static void onEventHappened(String event) {
        onEventHappened(0, event);
    }

    public static void onEventHappened(int key, String event) {
        synchronized (sLOCK) {
            HashMap<String, Long> sp = sTimeTrackers.get(key);
            if (sp == null) {
                sp = new HashMap<>();
                sTimeTrackers.put(key, sp);
            }
            sp.put(event, Long.valueOf(SystemClock.elapsedRealtime()));
        }
    }

    public static boolean onFirstEventHappened(String event) {
        return onFirstEventHappened(0, event);
    }

    public static boolean onFirstEventHappened(int key, String event) {
        boolean z;
        synchronized (sLOCK) {
            if (hasEventHappened(key, event)) {
                z = false;
            } else {
                HashMap<String, Long> sp = sTimeTrackers.get(key);
                if (sp == null) {
                    sp = new HashMap<>();
                    sTimeTrackers.put(key, sp);
                }
                sp.put(event, Long.valueOf(SystemClock.elapsedRealtime()));
                z = true;
            }
        }
        return z;
    }

    public static boolean hasEventHappened(int key, String event) {
        boolean z;
        synchronized (sLOCK) {
            HashMap<String, Long> sp = sTimeTrackers.get(key);
            z = sp != null && sp.containsKey(event);
        }
        return z;
    }

    public static long timeBetween(String event1, String event2) {
        return timeBetween(0, event1, event2);
    }

    public static long timeBetween(int key, String event1, String event2) {
        long jLongValue = -1;
        synchronized (sLOCK) {
            HashMap<String, Long> sp = sTimeTrackers.get(key);
            if (sp != null) {
                Long t1 = sp.get(event1);
                if (t1 != null) {
                    Long t2 = sp.get(event2);
                    if (t2 == null) {
                        jLongValue = -2;
                    } else {
                        jLongValue = t2.longValue() - t1.longValue();
                    }
                }
            }
        }
        return jLongValue;
    }
}
