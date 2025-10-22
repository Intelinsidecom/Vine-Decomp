package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class ProfileSortManager {
    private static ProfileSortManager sInstance = new ProfileSortManager();
    private SharedPreferences mPrefs;
    private HashMap<Long, String> mProfilePostsSortOrder = new HashMap<>();
    private HashMap<Long, String> mProfileLikesSortOrder = new HashMap<>();

    private ProfileSortManager() {
    }

    public static ProfileSortManager getInstance() {
        return sInstance;
    }

    public String getUserProfilePostsSortOrder(Context context, long userId) {
        String sortOrder = this.mProfilePostsSortOrder.get(Long.valueOf(userId));
        if (sortOrder != null) {
            return sortOrder;
        }
        if (this.mPrefs == null) {
            this.mPrefs = StandalonePreference.PROFILE_SORTS.getPref(context);
        }
        String sortOrder2 = this.mPrefs.getString(Long.toString(userId) + "_profile_sort_posts", "recent");
        this.mProfilePostsSortOrder.put(Long.valueOf(userId), sortOrder2);
        return sortOrder2;
    }

    public void setUserProfilePostsSortOrder(Context context, long userId, String sortOrder) {
        if (this.mPrefs == null) {
            this.mPrefs = StandalonePreference.PROFILE_SORTS.getPref(context);
        }
        this.mProfilePostsSortOrder.put(Long.valueOf(userId), sortOrder);
        SharedPreferences.Editor editor = this.mPrefs.edit();
        editor.putString(Long.toString(userId) + "_profile_sort_posts", sortOrder).apply();
    }

    public String getUserProfileLikesSortOrder(Context context, long userId) {
        String sortOrder = this.mProfileLikesSortOrder.get(Long.valueOf(userId));
        if (sortOrder != null) {
            return sortOrder;
        }
        if (this.mPrefs == null) {
            this.mPrefs = StandalonePreference.PROFILE_SORTS.getPref(context);
        }
        String sortOrder2 = this.mPrefs.getString(Long.toString(userId) + "_profile_sort_likes", "recent");
        this.mProfileLikesSortOrder.put(Long.valueOf(userId), sortOrder2);
        return sortOrder2;
    }

    public void setUserProfileLikesSortOrder(Context context, long userId, String sortOrder) {
        if (this.mPrefs == null) {
            this.mPrefs = StandalonePreference.PROFILE_SORTS.getPref(context);
        }
        this.mProfileLikesSortOrder.put(Long.valueOf(userId), sortOrder);
        SharedPreferences.Editor editor = this.mPrefs.edit();
        editor.putString(Long.toString(userId) + "_profile_sort_likes", sortOrder).apply();
    }
}
