package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class StaleFeedStartIdManager {
    public static void setStaleFeedStartPointIdAndTimestamp(Context context, long itemId) {
        SharedPreferences prefs = Util.getDefaultSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("stale_feed_start_point_id", itemId);
        editor.putLong("stale_feed_timestamp", System.currentTimeMillis());
        editor.apply();
    }

    public static long getStaleFeedStartPointId(Context context) {
        SharedPreferences prefs = Util.getDefaultSharedPrefs(context);
        return prefs.getLong("stale_feed_start_point_id", -1L);
    }

    public static long getStaleFeedStartFetchTimestamp(Context context) {
        SharedPreferences prefs = Util.getDefaultSharedPrefs(context);
        return prefs.getLong("stale_feed_timestamp", -1L);
    }
}
