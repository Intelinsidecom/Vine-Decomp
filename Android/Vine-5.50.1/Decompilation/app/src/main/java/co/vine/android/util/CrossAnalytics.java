package co.vine.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class CrossAnalytics {
    private static CrossAnalytics sINSTANCE;
    private final SharedPreferences mPrefs;

    private CrossAnalytics(Context context) {
        this.mPrefs = context.getSharedPreferences("cross_analytics", 0);
    }

    public static CrossAnalytics getInstance(Context context) {
        if (sINSTANCE == null) {
            sINSTANCE = new CrossAnalytics(context);
        }
        return sINSTANCE;
    }

    public void clear(String key) {
        this.mPrefs.edit().remove(key).apply();
    }

    public String get(String key) {
        return this.mPrefs.getString(key, null);
    }
}
