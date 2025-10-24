package io.fabric.sdk.android.services.settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class DefaultSettingsController implements SettingsController {
    private final CachedSettingsIo cachedSettingsIo;
    private final CurrentTimeProvider currentTimeProvider;
    private final Kit kit;
    private final PreferenceStore preferenceStore;
    private final SettingsJsonTransform settingsJsonTransform;
    private final SettingsRequest settingsRequest;
    private final SettingsSpiCall settingsSpiCall;

    public DefaultSettingsController(Kit kit, SettingsRequest settingsRequest, CurrentTimeProvider currentTimeProvider, SettingsJsonTransform settingsJsonTransform, CachedSettingsIo cachedSettingsIo, SettingsSpiCall settingsSpiCall) {
        this.kit = kit;
        this.settingsRequest = settingsRequest;
        this.currentTimeProvider = currentTimeProvider;
        this.settingsJsonTransform = settingsJsonTransform;
        this.cachedSettingsIo = cachedSettingsIo;
        this.settingsSpiCall = settingsSpiCall;
        this.preferenceStore = new PreferenceStoreImpl(this.kit);
    }

    @Override // io.fabric.sdk.android.services.settings.SettingsController
    public SettingsData loadSettingsData() {
        return loadSettingsData(SettingsCacheBehavior.USE_CACHE);
    }

    @Override // io.fabric.sdk.android.services.settings.SettingsController
    public SettingsData loadSettingsData(SettingsCacheBehavior cacheBehavior) {
        JSONObject settingsJson;
        SettingsData toReturn = null;
        try {
            if (!Fabric.isDebuggable() && !buildInstanceIdentifierChanged()) {
                toReturn = getCachedSettingsData(cacheBehavior);
            }
            if (toReturn == null && (settingsJson = this.settingsSpiCall.invoke(this.settingsRequest)) != null) {
                toReturn = this.settingsJsonTransform.buildFromJson(this.currentTimeProvider, settingsJson);
                this.cachedSettingsIo.writeCachedSettings(toReturn.expiresAtMillis, settingsJson);
                logSettings(settingsJson, "Loaded settings: ");
                setStoredBuildInstanceIdentifier(getBuildInstanceIdentifierFromContext());
            }
            if (toReturn == null) {
                return getCachedSettingsData(SettingsCacheBehavior.IGNORE_CACHE_EXPIRATION);
            }
            return toReturn;
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "Unknown error while loading Crashlytics settings. Crashes will be cached until settings can be retrieved.", e);
            return null;
        }
    }

    private SettingsData getCachedSettingsData(SettingsCacheBehavior cacheBehavior) {
        SettingsData toReturn = null;
        try {
            if (!SettingsCacheBehavior.SKIP_CACHE_LOOKUP.equals(cacheBehavior)) {
                JSONObject settingsJson = this.cachedSettingsIo.readCachedSettings();
                if (settingsJson != null) {
                    SettingsData settingsData = this.settingsJsonTransform.buildFromJson(this.currentTimeProvider, settingsJson);
                    if (settingsData != null) {
                        logSettings(settingsJson, "Loaded cached settings: ");
                        long currentTimeMillis = this.currentTimeProvider.getCurrentTimeMillis();
                        if (SettingsCacheBehavior.IGNORE_CACHE_EXPIRATION.equals(cacheBehavior) || !settingsData.isExpired(currentTimeMillis)) {
                            toReturn = settingsData;
                            Fabric.getLogger().d("Fabric", "Returning cached settings.");
                        } else {
                            Fabric.getLogger().d("Fabric", "Cached settings have expired.");
                        }
                    } else {
                        Fabric.getLogger().e("Fabric", "Failed to transform cached settings data.", null);
                    }
                } else {
                    Fabric.getLogger().d("Fabric", "No cached settings data found.");
                }
            }
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "Failed to get cached settings", e);
        }
        return toReturn;
    }

    private void logSettings(JSONObject json, String message) throws JSONException {
        Fabric.getLogger().d("Fabric", message + json.toString());
    }

    String getBuildInstanceIdentifierFromContext() {
        return CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(this.kit.getContext()));
    }

    String getStoredBuildInstanceIdentifier() {
        SharedPreferences sharedPreferences = this.preferenceStore.get();
        return sharedPreferences.getString("existing_instance_identifier", "");
    }

    @SuppressLint({"CommitPrefEdits"})
    boolean setStoredBuildInstanceIdentifier(String buildInstanceIdentifier) {
        SharedPreferences.Editor editor = this.preferenceStore.edit();
        editor.putString("existing_instance_identifier", buildInstanceIdentifier);
        return this.preferenceStore.save(editor);
    }

    boolean buildInstanceIdentifierChanged() {
        String existingInstanceIdentifier = getStoredBuildInstanceIdentifier();
        String currentInstanceIdentifier = getBuildInstanceIdentifierFromContext();
        return !existingInstanceIdentifier.equals(currentInstanceIdentifier);
    }
}
