package com.crashlytics.android.answers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.Crash;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import io.fabric.sdk.android.services.events.EventsStorage;
import io.fabric.sdk.android.services.events.GZIPQueueFileEventStorage;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public class Answers extends Kit<Boolean> {
    private long installedAt;
    private PreferenceStore preferenceStore;
    SessionAnalyticsManager sessionAnalyticsManager;
    private String versionCode;
    private String versionName;

    public static Answers getInstance() {
        return (Answers) Fabric.getKit(Answers.class);
    }

    public void onException(Crash.LoggedException exception) {
        if (this.sessionAnalyticsManager != null) {
            this.sessionAnalyticsManager.onError(exception.getSessionId());
        }
    }

    public void onException(Crash.FatalException exception) {
        if (this.sessionAnalyticsManager != null) {
            this.sessionAnalyticsManager.onCrash(exception.getSessionId());
        }
    }

    @Override // io.fabric.sdk.android.Kit
    @SuppressLint({"NewApi"})
    protected boolean onPreExecute() throws PackageManager.NameNotFoundException {
        try {
            this.preferenceStore = new PreferenceStoreImpl(this);
            Context context = getContext();
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            this.versionCode = Integer.toString(packageInfo.versionCode);
            this.versionName = packageInfo.versionName == null ? "0.0" : packageInfo.versionName;
            if (Build.VERSION.SDK_INT >= 9) {
                this.installedAt = packageInfo.firstInstallTime;
            } else {
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
                this.installedAt = new File(appInfo.sourceDir).lastModified();
            }
            return true;
        } catch (Exception e) {
            Fabric.getLogger().e("Answers", "Error setting up app properties", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.fabric.sdk.android.Kit
    public Boolean doInBackground() {
        boolean z;
        Context context = getContext();
        initializeSessionAnalytics(context);
        try {
            SettingsData settingsData = Settings.getInstance().awaitSettingsData();
            if (settingsData == null) {
                z = false;
            } else if (settingsData.featuresData.collectAnalytics) {
                this.sessionAnalyticsManager.setAnalyticsSettingsData(settingsData.analyticsSettingsData, getOverridenSpiEndpoint());
                z = true;
            } else {
                CommonUtils.logControlled(context, "Disabling analytics collection based on settings flag value.");
                this.sessionAnalyticsManager.disable();
                z = false;
            }
            return z;
        } catch (Exception e) {
            Fabric.getLogger().e("Answers", "Error dealing with settings", e);
            return false;
        }
    }

    @Override // io.fabric.sdk.android.Kit
    public String getIdentifier() {
        return "com.crashlytics.sdk.android:answers";
    }

    @Override // io.fabric.sdk.android.Kit
    public String getVersion() {
        return "1.2.2.56";
    }

    @SuppressLint({"CommitPrefEdits"})
    @TargetApi(14)
    private void initializeSessionAnalytics(Context context) {
        try {
            SessionEventTransform transform = new SessionEventTransform();
            SystemCurrentTimeProvider timeProvider = new SystemCurrentTimeProvider();
            EventsStorage storage = new GZIPQueueFileEventStorage(getContext(), getSdkDirectory(), "session_analytics.tap", "session_analytics_to_send");
            SessionAnalyticsFilesManager analyticsFilesManager = new SessionAnalyticsFilesManager(context, transform, timeProvider, storage);
            IdManager idManager = getIdManager();
            Map<IdManager.DeviceIdentifierType, String> deviceIdentifiers = idManager.getDeviceIdentifiers();
            String appBundleId = context.getPackageName();
            String installationId = idManager.getAppInstallIdentifier();
            String androidId = deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ID);
            String advertisingId = deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ADVERTISING_ID);
            String betaDeviceToken = deviceIdentifiers.get(IdManager.DeviceIdentifierType.FONT_TOKEN);
            String buildId = CommonUtils.resolveBuildId(context);
            String osVersion = idManager.getOsVersionString();
            String deviceModel = idManager.getModelName();
            String executionId = UUID.randomUUID().toString();
            SessionEventMetadata sessionEventMetadata = new SessionEventMetadata(appBundleId, executionId, installationId, androidId, advertisingId, betaDeviceToken, buildId, osVersion, deviceModel, this.versionCode, this.versionName);
            Application application = (Application) getContext().getApplicationContext();
            if (application != null && Build.VERSION.SDK_INT >= 14) {
                this.sessionAnalyticsManager = AutoSessionAnalyticsManager.build(application, sessionEventMetadata, analyticsFilesManager, (HttpRequestFactory) new DefaultHttpRequestFactory(Fabric.getLogger()));
            } else {
                this.sessionAnalyticsManager = SessionAnalyticsManager.build(context, sessionEventMetadata, analyticsFilesManager, new DefaultHttpRequestFactory(Fabric.getLogger()));
            }
            if (isFirstLaunch(this.installedAt)) {
                Fabric.getLogger().d("Answers", "First launch");
                this.sessionAnalyticsManager.onInstall();
                this.preferenceStore.save(this.preferenceStore.edit().putBoolean("analytics_launched", true));
            }
        } catch (Exception e) {
            CommonUtils.logControlledError(context, "Crashlytics failed to initialize session analytics.", e);
        }
    }

    String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), "com.crashlytics.ApiEndpoint");
    }

    boolean getAnalyticsLaunched() {
        return this.preferenceStore.get().getBoolean("analytics_launched", false);
    }

    boolean isFirstLaunch(long installedAt) {
        return !getAnalyticsLaunched() && installedRecently(installedAt);
    }

    boolean installedRecently(long installedAt) {
        long timeDifferenceInMilliseconds = System.currentTimeMillis() - installedAt;
        return timeDifferenceInMilliseconds < 3600000;
    }

    File getSdkDirectory() {
        return new FileStoreImpl(this).getFilesDir();
    }
}
