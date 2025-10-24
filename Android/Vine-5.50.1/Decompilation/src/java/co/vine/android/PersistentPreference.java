package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;

/* loaded from: classes.dex */
public enum PersistentPreference {
    DEFAULT(null),
    CLIENT_FLAGS_OVERRIDE("client_flags_override");

    private final String mName;

    PersistentPreference(String name) {
        this.mName = name;
    }

    public SharedPreferences getPref(Context context) {
        String key = this.mName == null ? context.getPackageName() + "_preferences" : context.getPackageName() + "_preferences_" + this.mName;
        return context.getSharedPreferences(key, 4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getPrefValue(Context context, String str, T t) {
        SharedPreferences pref = getPref(context);
        if (t.getClass().equals(Boolean.class)) {
            return (T) Boolean.valueOf(pref.getBoolean(str, ((Boolean) t).booleanValue()));
        }
        if (t.getClass().equals(Float.class)) {
            return (T) Float.valueOf(pref.getFloat(str, ((Float) t).floatValue()));
        }
        if (t.getClass().equals(Integer.class)) {
            return (T) Integer.valueOf(pref.getInt(str, ((Integer) t).intValue()));
        }
        if (t.getClass().equals(Long.class)) {
            return (T) Long.valueOf(pref.getLong(str, ((Long) t).longValue()));
        }
        if (t.getClass().equals(String.class)) {
            return (T) pref.getString(str, (String) t);
        }
        if (t.getClass().equals(Set.class)) {
            return (T) pref.getStringSet(str, (Set) t);
        }
        return t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void setPrefValue(Context context, String key, T t) {
        SharedPreferences.Editor editor = getPref(context).edit();
        if (t != 0) {
            if (t.getClass().equals(Boolean.class)) {
                editor.putBoolean(key, ((Boolean) t).booleanValue());
            } else if (t.getClass().equals(Float.class)) {
                editor.putFloat(key, ((Float) t).floatValue());
            } else if (t.getClass().equals(Integer.class)) {
                editor.putInt(key, ((Integer) t).intValue());
            } else if (t.getClass().equals(Long.class)) {
                editor.putLong(key, ((Long) t).longValue());
            } else if (t.getClass().equals(String.class)) {
                editor.putString(key, (String) t);
            } else if (t.getClass().equals(Set.class)) {
                editor.putStringSet(key, (Set) t);
            }
            editor.apply();
        }
    }

    public void clearValues(Context context, String[] values) {
        SharedPreferences.Editor editor = getPref(context).edit();
        for (String value : values) {
            editor.remove(value);
        }
        editor.apply();
    }
}
