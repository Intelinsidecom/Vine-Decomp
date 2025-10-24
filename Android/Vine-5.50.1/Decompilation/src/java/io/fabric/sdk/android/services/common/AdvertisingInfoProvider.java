package io.fabric.sdk.android.services.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;

/* loaded from: classes.dex */
class AdvertisingInfoProvider {
    private final Context context;
    private final PreferenceStore preferenceStore;

    public AdvertisingInfoProvider(Context context) {
        this.context = context.getApplicationContext();
        this.preferenceStore = new PreferenceStoreImpl(context, "TwitterAdvertisingInfoPreferences");
    }

    public AdvertisingInfo getAdvertisingInfo() {
        AdvertisingInfo infoToReturn = getInfoFromPreferences();
        if (isInfoValid(infoToReturn)) {
            Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Preference Store");
            refreshInfoIfNeededAsync(infoToReturn);
            return infoToReturn;
        }
        AdvertisingInfo infoToReturn2 = getAdvertisingInfoFromStrategies();
        storeInfoToPreferences(infoToReturn2);
        return infoToReturn2;
    }

    private void refreshInfoIfNeededAsync(final AdvertisingInfo advertisingInfo) {
        new Thread(new BackgroundPriorityRunnable() { // from class: io.fabric.sdk.android.services.common.AdvertisingInfoProvider.1
            @Override // io.fabric.sdk.android.services.common.BackgroundPriorityRunnable
            public void onRun() {
                AdvertisingInfo infoToStore = AdvertisingInfoProvider.this.getAdvertisingInfoFromStrategies();
                if (!advertisingInfo.equals(infoToStore)) {
                    Fabric.getLogger().d("Fabric", "Asychronously getting Advertising Info and storing it to preferences");
                    AdvertisingInfoProvider.this.storeInfoToPreferences(infoToStore);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"CommitPrefEdits"})
    public void storeInfoToPreferences(AdvertisingInfo infoToReturn) {
        if (isInfoValid(infoToReturn)) {
            this.preferenceStore.save(this.preferenceStore.edit().putString("advertising_id", infoToReturn.advertisingId).putBoolean("limit_ad_tracking_enabled", infoToReturn.limitAdTrackingEnabled));
        } else {
            this.preferenceStore.save(this.preferenceStore.edit().remove("advertising_id").remove("limit_ad_tracking_enabled"));
        }
    }

    protected AdvertisingInfo getInfoFromPreferences() {
        String advertisingId = this.preferenceStore.get().getString("advertising_id", "");
        boolean limitAd = this.preferenceStore.get().getBoolean("limit_ad_tracking_enabled", false);
        return new AdvertisingInfo(advertisingId, limitAd);
    }

    public AdvertisingInfoStrategy getReflectionStrategy() {
        return new AdvertisingInfoReflectionStrategy(this.context);
    }

    public AdvertisingInfoStrategy getServiceStrategy() {
        return new AdvertisingInfoServiceStrategy(this.context);
    }

    private boolean isInfoValid(AdvertisingInfo advertisingInfo) {
        return (advertisingInfo == null || TextUtils.isEmpty(advertisingInfo.advertisingId)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AdvertisingInfo getAdvertisingInfoFromStrategies() {
        AdvertisingInfoStrategy adInfoStrategy = getReflectionStrategy();
        AdvertisingInfo infoToReturn = adInfoStrategy.getAdvertisingInfo();
        if (!isInfoValid(infoToReturn)) {
            AdvertisingInfoStrategy adInfoStrategy2 = getServiceStrategy();
            infoToReturn = adInfoStrategy2.getAdvertisingInfo();
            if (!isInfoValid(infoToReturn)) {
                Fabric.getLogger().d("Fabric", "AdvertisingInfo not present");
            } else {
                Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Service Provider");
            }
        } else {
            Fabric.getLogger().d("Fabric", "Using AdvertisingInfo from Reflection Provider");
        }
        return infoToReturn;
    }
}
