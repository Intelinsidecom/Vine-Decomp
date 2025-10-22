package co.vine.android.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import co.vine.android.provider.Vine;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class SettingsManager {
    private static final String[] VALUE_PROJECTION = {"value"};

    @Deprecated
    public static String getCurrentAccount(Context context) {
        return getValue(context, "current_account", null);
    }

    private static synchronized String getValue(Context context, String propName, String defaultValue) {
        String value;
        Cursor cursor = context.getContentResolver().query(Vine.Settings.CONTENT_URI, VALUE_PROJECTION, "name=?", new String[]{propName}, null);
        value = defaultValue;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                value = cursor.getString(0);
            }
            cursor.close();
        }
        return value;
    }

    public static String getEdition(Context context) {
        SharedPreferences pr = Util.getDefaultSharedPrefs(context);
        return pr.getString("settings_edition", null);
    }
}
