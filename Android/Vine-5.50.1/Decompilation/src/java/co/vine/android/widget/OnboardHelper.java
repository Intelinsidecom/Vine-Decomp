package co.vine.android.widget;

import android.content.Context;
import co.vine.android.util.CommonUtil;

/* loaded from: classes.dex */
public class OnboardHelper {
    private static boolean getPref(Context context, String pref) {
        if (context != null) {
            return CommonUtil.getDefaultSharedPrefs(context).getBoolean(pref, false);
        }
        return true;
    }

    private static void setPref(Context context, String pref, boolean value) {
        if (context != null) {
            CommonUtil.getDefaultSharedPrefs(context).edit().putBoolean(pref, value).apply();
        }
    }

    public static boolean getFavoriteUserTooltipShown(Context context) {
        return getPref(context, "favorite_user_tooltip_shown");
    }

    public static void setFavoriteUserTooltipShown(Context context) {
        setPref(context, "favorite_user_tooltip_shown", true);
    }

    public static boolean hasShownFavoriteUserOverlay(Context context) {
        return getPref(context, "favorite_user_overlay_shown");
    }

    public static void setFavoriteUserOverlayShown(Context context) {
        setPref(context, "favorite_user_overlay_shown", true);
    }
}
