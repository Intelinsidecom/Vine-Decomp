package co.vine.android.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;

/* loaded from: classes.dex */
public class IntentionalObjectCounter {
    private static final int[] LOCK = new int[0];
    private static final HashMap<String, HashSet<WeakReference<Object>>> sInstances = new HashMap<>();
    private final WeakReference<Object> mItem;
    public final String mKey;

    public IntentionalObjectCounter(String key, Object item) {
        this.mKey = key;
        this.mItem = new WeakReference<>(item);
    }

    public void add() {
        add(this.mKey, this.mItem);
    }

    public void release() {
        release(this.mKey, this.mItem);
    }

    public int getCount() {
        return getCount(this.mKey);
    }

    public static void add(String key, WeakReference<Object> item) {
        HashSet<WeakReference<Object>> counter = getCounter(key);
        synchronized (LOCK) {
            counter.add(item);
        }
    }

    public static void release(String key, WeakReference<Object> item) {
        HashSet<WeakReference<Object>> counter = getCounter(key);
        synchronized (LOCK) {
            counter.remove(item);
        }
    }

    public static int getCount(String key) {
        int size;
        HashSet<WeakReference<Object>> counter = getCounter(key);
        synchronized (LOCK) {
            size = counter.size();
        }
        return size;
    }

    private static HashSet<WeakReference<Object>> getCounter(String key) {
        HashSet<WeakReference<Object>> counter;
        synchronized (LOCK) {
            counter = sInstances.get(key);
            if (counter == null) {
                counter = new HashSet<>();
                sInstances.put(key, counter);
            }
        }
        return counter;
    }
}
