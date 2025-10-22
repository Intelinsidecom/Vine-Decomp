package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import org.json.JSONObject;

/* loaded from: classes.dex */
class DefaultCachedSettingsIo implements CachedSettingsIo {
    private final Kit kit;

    public DefaultCachedSettingsIo(Kit kit) {
        this.kit = kit;
    }

    @Override // io.fabric.sdk.android.services.settings.CachedSettingsIo
    public JSONObject readCachedSettings() throws Throwable {
        Fabric.getLogger().d("Fabric", "Reading cached settings...");
        FileInputStream fis = null;
        JSONObject toReturn = null;
        try {
            try {
                File settingsFile = new File(new FileStoreImpl(this.kit).getFilesDir(), "com.crashlytics.settings.json");
                if (settingsFile.exists()) {
                    FileInputStream fis2 = new FileInputStream(settingsFile);
                    try {
                        String settingsStr = CommonUtils.streamToString(fis2);
                        toReturn = new JSONObject(settingsStr);
                        fis = fis2;
                    } catch (Exception e) {
                        e = e;
                        fis = fis2;
                        Fabric.getLogger().e("Fabric", "Failed to fetch cached settings", e);
                        CommonUtils.closeOrLog(fis, "Error while closing settings cache file.");
                        return toReturn;
                    } catch (Throwable th) {
                        th = th;
                        fis = fis2;
                        CommonUtils.closeOrLog(fis, "Error while closing settings cache file.");
                        throw th;
                    }
                } else {
                    Fabric.getLogger().d("Fabric", "No cached settings found.");
                }
                CommonUtils.closeOrLog(fis, "Error while closing settings cache file.");
            } catch (Exception e2) {
                e = e2;
            }
            return toReturn;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // io.fabric.sdk.android.services.settings.CachedSettingsIo
    public void writeCachedSettings(long expiresAtMillis, JSONObject settingsJson) throws Throwable {
        Fabric.getLogger().d("Fabric", "Writing settings to cache file...");
        if (settingsJson != null) {
            FileWriter writer = null;
            try {
                try {
                    settingsJson.put("expires_at", expiresAtMillis);
                    FileWriter writer2 = new FileWriter(new File(new FileStoreImpl(this.kit).getFilesDir(), "com.crashlytics.settings.json"));
                    try {
                        writer2.write(settingsJson.toString());
                        writer2.flush();
                        CommonUtils.closeOrLog(writer2, "Failed to close settings writer.");
                    } catch (Exception e) {
                        e = e;
                        writer = writer2;
                        Fabric.getLogger().e("Fabric", "Failed to cache settings", e);
                        CommonUtils.closeOrLog(writer, "Failed to close settings writer.");
                    } catch (Throwable th) {
                        th = th;
                        writer = writer2;
                        CommonUtils.closeOrLog(writer, "Failed to close settings writer.");
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
    }
}
