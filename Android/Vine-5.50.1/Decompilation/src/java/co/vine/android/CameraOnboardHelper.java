package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class CameraOnboardHelper {
    public static boolean getHasShownOverlay(Context context) {
        if (context == null) {
            return true;
        }
        SharedPreferences sp = StandalonePreference.CAMERA_ONBOARD.getPref(context);
        return sp.getBoolean("did_show_camera_overlay", false);
    }

    public static String getLastCompletedStep(Context context) {
        if (context == null) {
            return "dont_onboard";
        }
        SharedPreferences sp = StandalonePreference.CAMERA_ONBOARD.getPref(context);
        return sp.getString("camera_onboard_step", null);
    }

    public static void setHasShownOverlay(Context context, boolean didShow) {
        if (context != null) {
            SharedPreferences sp = StandalonePreference.CAMERA_ONBOARD.getPref(context);
            sp.edit().putBoolean("did_show_camera_overlay", didShow).apply();
        }
    }

    public static void setLastCompletedStep(Context context, String step) {
        if (context != null) {
            SharedPreferences sp = StandalonePreference.CAMERA_ONBOARD.getPref(context);
            sp.edit().putString("camera_onboard_step", step).apply();
        }
    }
}
