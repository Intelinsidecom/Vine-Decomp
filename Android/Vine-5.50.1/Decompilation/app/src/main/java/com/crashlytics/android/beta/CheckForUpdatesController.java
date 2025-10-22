package com.crashlytics.android.beta;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.settings.BetaSettingsData;

/* loaded from: classes.dex */
class CheckForUpdatesController {
    private final Beta beta;
    private final BetaSettingsData betaSettings;
    private final BuildProperties buildProps;
    private final Context context;
    private final CurrentTimeProvider currentTimeProvider;
    private final HttpRequestFactory httpRequestFactory;
    private final IdManager idManager;
    private final PreferenceStore preferenceStore;

    public CheckForUpdatesController(Context context, Beta beta, IdManager idManager, BetaSettingsData betaSettings, BuildProperties buildProps, PreferenceStore prefsStore, CurrentTimeProvider currentTimeProvider, HttpRequestFactory httpRequestFactory) {
        this.context = context;
        this.beta = beta;
        this.idManager = idManager;
        this.betaSettings = betaSettings;
        this.buildProps = buildProps;
        this.preferenceStore = prefsStore;
        this.currentTimeProvider = currentTimeProvider;
        this.httpRequestFactory = httpRequestFactory;
    }

    public void checkForUpdates() {
        long currentTimeMillis = this.currentTimeProvider.getCurrentTimeMillis();
        long updateCheckDelayMillis = this.betaSettings.updateSuspendDurationSeconds * 1000;
        Fabric.getLogger().d("Beta", "Check for updates delay: " + updateCheckDelayMillis);
        long lastCheckTimeMillis = this.preferenceStore.get().getLong("last_update_check", 0L);
        Fabric.getLogger().d("Beta", "Check for updates last check time: " + lastCheckTimeMillis);
        long nextCheckTimeMillis = lastCheckTimeMillis + updateCheckDelayMillis;
        Fabric.getLogger().d("Beta", "Check for updates current time: " + currentTimeMillis + ", next check time: " + nextCheckTimeMillis);
        if (currentTimeMillis >= nextCheckTimeMillis) {
            try {
                Fabric.getLogger().d("Beta", "Performing update check");
                String apiKey = new ApiKey().getValue(this.context);
                String idHeaderValue = this.idManager.createIdHeaderValue(apiKey, this.buildProps.packageName);
                CheckForUpdatesRequest request = new CheckForUpdatesRequest(this.beta, this.beta.getOverridenSpiEndpoint(), this.betaSettings.updateUrl, this.httpRequestFactory, new CheckForUpdatesResponseTransform());
                request.invoke(apiKey, idHeaderValue, this.buildProps);
                return;
            } finally {
                this.preferenceStore.edit().putLong("last_update_check", currentTimeMillis).commit();
            }
        }
        Fabric.getLogger().d("Beta", "Check for updates next check time was not passed");
    }
}
