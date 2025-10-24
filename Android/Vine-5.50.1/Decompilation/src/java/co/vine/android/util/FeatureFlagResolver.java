package co.vine.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import co.vine.android.PersistentPreference;

/* loaded from: classes.dex */
public final class FeatureFlagResolver {
    public static boolean resolveBooleanFlag(Context context, String sharedPreferenceName, FeatureFlag featureFlag) {
        SharedPreferences sp = PersistentPreference.CLIENT_FLAGS_OVERRIDE.getPref(context);
        boolean shouldOverride = sp.getBoolean("enable_flags_override", false) && sp.contains(new StringBuilder().append("override_").append(sharedPreferenceName).toString());
        if (shouldOverride) {
            return sp.getBoolean("override_" + sharedPreferenceName, false);
        }
        switch (featureFlag) {
            case DEVELOP_OFF:
            default:
                return false;
            case DEVELOP_ON:
                return CrossConstants.IS_RELEASE_BUILD ? false : true;
            case OFF:
            case ON:
                if (CrossConstants.IS_RELEASE_BUILD || CrossConstants.IS_BETA_BUILD) {
                    if (!CommonUtil.getDefaultSharedPrefs(context).getBoolean(sharedPreferenceName, featureFlag == FeatureFlag.ON)) {
                        return false;
                    }
                }
                return true;
        }
    }
}
