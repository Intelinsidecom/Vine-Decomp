package io.fabric.sdk.android.services.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class IdManager {
    AdvertisingInfo advertisingInfo;
    AdvertisingInfoProvider advertisingInfoProvider;
    private final Context appContext;
    private final String appIdentifier;
    private final String appInstallIdentifier;
    private final boolean collectHardwareIds;
    private final boolean collectUserIds;
    boolean fetchedAdvertisingInfo;
    private final ReentrantLock installationIdLock = new ReentrantLock();
    private final InstallerPackageNameProvider installerPackageNameProvider;
    private final Collection<Kit> kits;
    private static final Pattern ID_PATTERN = Pattern.compile("[^\\p{Alnum}]");
    private static final String FORWARD_SLASH_REGEX = Pattern.quote("/");

    public enum DeviceIdentifierType {
        WIFI_MAC_ADDRESS(1),
        BLUETOOTH_MAC_ADDRESS(2),
        FONT_TOKEN(53),
        ANDROID_ID(100),
        ANDROID_DEVICE_ID(101),
        ANDROID_SERIAL(102),
        ANDROID_ADVERTISING_ID(103);

        public final int protobufIndex;

        DeviceIdentifierType(int pbufIndex) {
            this.protobufIndex = pbufIndex;
        }
    }

    public IdManager(Context appContext, String appIdentifier, String appInstallIdentifier, Collection<Kit> kits) {
        if (appContext == null) {
            throw new IllegalArgumentException("appContext must not be null");
        }
        if (appIdentifier == null) {
            throw new IllegalArgumentException("appIdentifier must not be null");
        }
        if (kits == null) {
            throw new IllegalArgumentException("kits must not be null");
        }
        this.appContext = appContext;
        this.appIdentifier = appIdentifier;
        this.appInstallIdentifier = appInstallIdentifier;
        this.kits = kits;
        this.installerPackageNameProvider = new InstallerPackageNameProvider();
        this.advertisingInfoProvider = new AdvertisingInfoProvider(appContext);
        this.collectHardwareIds = CommonUtils.getBooleanResourceValue(appContext, "com.crashlytics.CollectDeviceIdentifiers", true);
        if (!this.collectHardwareIds) {
            Fabric.getLogger().d("Fabric", "Device ID collection disabled for " + appContext.getPackageName());
        }
        this.collectUserIds = CommonUtils.getBooleanResourceValue(appContext, "com.crashlytics.CollectUserIdentifiers", true);
        if (!this.collectUserIds) {
            Fabric.getLogger().d("Fabric", "User information collection disabled for " + appContext.getPackageName());
        }
    }

    @Deprecated
    public String createIdHeaderValue(String apiKey, String packageName) {
        return "";
    }

    public boolean canCollectUserIds() {
        return this.collectUserIds;
    }

    private String formatId(String id) {
        if (id == null) {
            return null;
        }
        return ID_PATTERN.matcher(id).replaceAll("").toLowerCase(Locale.US);
    }

    public String getAppInstallIdentifier() {
        String appInstallId = this.appInstallIdentifier;
        if (appInstallId == null) {
            SharedPreferences prefs = CommonUtils.getSharedPrefs(this.appContext);
            String appInstallId2 = prefs.getString("crashlytics.installation.id", null);
            if (appInstallId2 == null) {
                return createInstallationUUID(prefs);
            }
            return appInstallId2;
        }
        return appInstallId;
    }

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public String getOsVersionString() {
        return getOsDisplayVersionString() + "/" + getOsBuildVersionString();
    }

    public String getOsDisplayVersionString() {
        return removeForwardSlashesIn(Build.VERSION.RELEASE);
    }

    public String getOsBuildVersionString() {
        return removeForwardSlashesIn(Build.VERSION.INCREMENTAL);
    }

    public String getModelName() {
        return String.format(Locale.US, "%s/%s", removeForwardSlashesIn(Build.MANUFACTURER), removeForwardSlashesIn(Build.MODEL));
    }

    private String removeForwardSlashesIn(String s) {
        return s.replaceAll(FORWARD_SLASH_REGEX, "");
    }

    public String getDeviceUUID() {
        if (!this.collectHardwareIds) {
            return "";
        }
        String toReturn = getAndroidId();
        if (toReturn == null) {
            SharedPreferences prefs = CommonUtils.getSharedPrefs(this.appContext);
            String toReturn2 = prefs.getString("crashlytics.installation.id", null);
            if (toReturn2 == null) {
                return createInstallationUUID(prefs);
            }
            return toReturn2;
        }
        return toReturn;
    }

    private String createInstallationUUID(SharedPreferences prefs) {
        this.installationIdLock.lock();
        try {
            String uuid = prefs.getString("crashlytics.installation.id", null);
            if (uuid == null) {
                uuid = formatId(UUID.randomUUID().toString());
                prefs.edit().putString("crashlytics.installation.id", uuid).commit();
            }
            return uuid;
        } finally {
            this.installationIdLock.unlock();
        }
    }

    public Map<DeviceIdentifierType, String> getDeviceIdentifiers() {
        Map<DeviceIdentifierType, String> ids = new HashMap<>();
        for (Object obj : this.kits) {
            if (obj instanceof DeviceIdentifierProvider) {
                DeviceIdentifierProvider idProvider = (DeviceIdentifierProvider) obj;
                Map<DeviceIdentifierType, String> kitIds = idProvider.getDeviceIdentifiers();
                for (Map.Entry<DeviceIdentifierType, String> entry : kitIds.entrySet()) {
                    putNonNullIdInto(ids, entry.getKey(), entry.getValue());
                }
            }
        }
        putNonNullIdInto(ids, DeviceIdentifierType.ANDROID_ID, getAndroidId());
        putNonNullIdInto(ids, DeviceIdentifierType.ANDROID_ADVERTISING_ID, getAdvertisingId());
        return Collections.unmodifiableMap(ids);
    }

    public String getInstallerPackageName() {
        return this.installerPackageNameProvider.getInstallerPackageName(this.appContext);
    }

    synchronized AdvertisingInfo getAdvertisingInfo() {
        if (!this.fetchedAdvertisingInfo) {
            this.advertisingInfo = this.advertisingInfoProvider.getAdvertisingInfo();
            this.fetchedAdvertisingInfo = true;
        }
        return this.advertisingInfo;
    }

    public String getAdvertisingId() {
        AdvertisingInfo advertisingInfo;
        if (!this.collectHardwareIds || (advertisingInfo = getAdvertisingInfo()) == null) {
            return null;
        }
        String toReturn = advertisingInfo.advertisingId;
        return toReturn;
    }

    private void putNonNullIdInto(Map<DeviceIdentifierType, String> idMap, DeviceIdentifierType idKey, String idValue) {
        if (idValue != null) {
            idMap.put(idKey, idValue);
        }
    }

    public String getAndroidId() {
        if (!this.collectHardwareIds) {
            return null;
        }
        String androidId = Settings.Secure.getString(this.appContext.getContentResolver(), "android_id");
        if ("9774d56d682e549c".equals(androidId)) {
            return null;
        }
        String toReturn = formatId(androidId);
        return toReturn;
    }

    @Deprecated
    public String getBluetoothMacAddress() {
        return null;
    }
}
