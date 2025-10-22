package com.facebook;

import android.content.SharedPreferences;
import com.facebook.internal.Validate;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
final class ProfileCache {
    private final SharedPreferences sharedPreferences = FacebookSdk.getApplicationContext().getSharedPreferences("com.facebook.AccessTokenManager.SharedPreferences", 0);

    ProfileCache() {
    }

    Profile load() {
        String jsonString = this.sharedPreferences.getString("com.facebook.ProfileManager.CachedProfile", null);
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                return new Profile(jsonObject);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    void save(Profile profile) {
        Validate.notNull(profile, "profile");
        JSONObject jsonObject = profile.toJSONObject();
        if (jsonObject != null) {
            this.sharedPreferences.edit().putString("com.facebook.ProfileManager.CachedProfile", jsonObject.toString()).apply();
        }
    }

    void clear() {
        this.sharedPreferences.edit().remove("com.facebook.ProfileManager.CachedProfile").apply();
    }
}
