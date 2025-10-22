package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import com.mobileapptracker.MATEvent;

/* loaded from: classes.dex */
public enum StandalonePreference {
    SPAM_COMMENT_IDS("spam_comment_ids"),
    PREFETCH_STAT("prefetchStat"),
    RECENT_SEARCH(MATEvent.SEARCH),
    SHARING("preferences_sharing"),
    STRING_ANCHORS("StringAnchorManager"),
    CAMERA_ONBOARD("CameraOnBoardHelper"),
    BHEAVIOR_MANAGER("BehaviorManager"),
    BEHAVIOR_URLS("BehaviorManager_urls"),
    BEHAVIOR_URL_RECENTS("BehaviorManager_url_recents"),
    LAST_CLEANUP("last_cleanup"),
    PROFILE_SORTS("profile_sorts");

    private final String mName;

    StandalonePreference(String name) {
        this.mName = name;
    }

    public SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences_" + this.mName, 4);
    }

    public static void clearAll(Context context) {
        StandalonePreference[] userPres = values();
        for (StandalonePreference pref : userPres) {
            SharedPreferences p = pref.getPref(context);
            if (p != null) {
                p.edit().clear().apply();
            }
        }
    }
}
