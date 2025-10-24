package io.fabric.sdk.android;

import android.os.SystemClock;
import android.text.TextUtils;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
class FabricKitsFinder implements Callable<Map<String, KitInfo>> {
    final String apkFileName;

    FabricKitsFinder(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    @Override // java.util.concurrent.Callable
    public Map<String, KitInfo> call() throws Exception {
        KitInfo kitInfo;
        Map<String, KitInfo> kitInfos = new HashMap<>();
        long startScan = SystemClock.elapsedRealtime();
        int count = 0;
        ZipFile apkFile = loadApkFile();
        Enumeration<? extends ZipEntry> entries = apkFile.entries();
        while (entries.hasMoreElements()) {
            count++;
            ZipEntry entry = entries.nextElement();
            if (entry.getName().startsWith("fabric/") && entry.getName().length() > "fabric/".length() && (kitInfo = loadKitInfo(entry, apkFile)) != null) {
                kitInfos.put(kitInfo.getIdentifier(), kitInfo);
                Fabric.getLogger().v("Fabric", String.format("Found kit:[%s] version:[%s]", kitInfo.getIdentifier(), kitInfo.getVersion()));
            }
        }
        if (apkFile != null) {
            try {
                apkFile.close();
            } catch (IOException e) {
            }
        }
        Fabric.getLogger().v("Fabric", "finish scanning in " + (SystemClock.elapsedRealtime() - startScan) + " reading:" + count);
        return kitInfos;
    }

    private KitInfo loadKitInfo(ZipEntry fabricFile, ZipFile apk) throws IOException {
        InputStream inputStream = null;
        try {
            try {
                inputStream = apk.getInputStream(fabricFile);
                Properties properties = new Properties();
                properties.load(inputStream);
                String id = properties.getProperty("fabric-identifier");
                String version = properties.getProperty("fabric-version");
                String buildType = properties.getProperty("fabric-build-type");
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(version)) {
                    throw new IllegalStateException("Invalid format of fabric file," + fabricFile.getName());
                }
                return new KitInfo(id, version, buildType);
            } catch (IOException ie) {
                Fabric.getLogger().e("Fabric", "Error when parsing fabric properties " + fabricFile.getName(), ie);
                CommonUtils.closeQuietly(inputStream);
                return null;
            }
        } finally {
            CommonUtils.closeQuietly(inputStream);
        }
    }

    protected ZipFile loadApkFile() throws IOException {
        return new ZipFile(this.apkFileName);
    }
}
