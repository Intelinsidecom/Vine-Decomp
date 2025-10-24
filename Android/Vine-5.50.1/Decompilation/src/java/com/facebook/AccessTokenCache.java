package com.facebook;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.internal.Validate;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
class AccessTokenCache {
    private final SharedPreferences sharedPreferences;
    private LegacyTokenHelper tokenCachingStrategy;
    private final SharedPreferencesTokenCachingStrategyFactory tokenCachingStrategyFactory;

    AccessTokenCache(SharedPreferences sharedPreferences, SharedPreferencesTokenCachingStrategyFactory tokenCachingStrategyFactory) {
        this.sharedPreferences = sharedPreferences;
        this.tokenCachingStrategyFactory = tokenCachingStrategyFactory;
    }

    public AccessTokenCache() {
        this(FacebookSdk.getApplicationContext().getSharedPreferences("com.facebook.AccessTokenManager.SharedPreferences", 0), new SharedPreferencesTokenCachingStrategyFactory());
    }

    public AccessToken load() throws JSONException {
        if (hasCachedAccessToken()) {
            return getCachedAccessToken();
        }
        if (!shouldCheckLegacyToken()) {
            return null;
        }
        AccessToken accessToken = getLegacyAccessToken();
        if (accessToken != null) {
            save(accessToken);
            getTokenCachingStrategy().clear();
            return accessToken;
        }
        return accessToken;
    }

    public void save(AccessToken accessToken) {
        Validate.notNull(accessToken, "accessToken");
        try {
            JSONObject jsonObject = accessToken.toJSONObject();
            this.sharedPreferences.edit().putString("com.facebook.AccessTokenManager.CachedAccessToken", jsonObject.toString()).apply();
        } catch (JSONException e) {
        }
    }

    public void clear() {
        this.sharedPreferences.edit().remove("com.facebook.AccessTokenManager.CachedAccessToken").apply();
        if (shouldCheckLegacyToken()) {
            getTokenCachingStrategy().clear();
        }
    }

    private boolean hasCachedAccessToken() {
        return this.sharedPreferences.contains("com.facebook.AccessTokenManager.CachedAccessToken");
    }

    private AccessToken getCachedAccessToken() {
        String jsonString = this.sharedPreferences.getString("com.facebook.AccessTokenManager.CachedAccessToken", null);
        if (jsonString == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return AccessToken.createFromJSONObject(jsonObject);
        } catch (JSONException e) {
            return null;
        }
    }

    private boolean shouldCheckLegacyToken() {
        return FacebookSdk.isLegacyTokenUpgradeSupported();
    }

    private AccessToken getLegacyAccessToken() throws JSONException {
        Bundle bundle = getTokenCachingStrategy().load();
        if (bundle == null || !LegacyTokenHelper.hasTokenInformation(bundle)) {
            return null;
        }
        AccessToken accessToken = AccessToken.createFromLegacyCache(bundle);
        return accessToken;
    }

    private LegacyTokenHelper getTokenCachingStrategy() {
        if (this.tokenCachingStrategy == null) {
            synchronized (this) {
                if (this.tokenCachingStrategy == null) {
                    this.tokenCachingStrategy = this.tokenCachingStrategyFactory.create();
                }
            }
        }
        return this.tokenCachingStrategy;
    }

    static class SharedPreferencesTokenCachingStrategyFactory {
        SharedPreferencesTokenCachingStrategyFactory() {
        }

        public LegacyTokenHelper create() {
            return new LegacyTokenHelper(FacebookSdk.getApplicationContext());
        }
    }
}
