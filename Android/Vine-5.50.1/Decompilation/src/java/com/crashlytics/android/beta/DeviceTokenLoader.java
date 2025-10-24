package com.crashlytics.android.beta;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.cache.ValueLoader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* loaded from: classes.dex */
public class DeviceTokenLoader implements ValueLoader<String> {
    @Override // io.fabric.sdk.android.services.cache.ValueLoader
    public String load(Context context) throws Exception {
        long start = System.nanoTime();
        String token = "";
        ZipInputStream zis = null;
        try {
            try {
                try {
                    try {
                        zis = getZipInputStreamOfAppApkFrom(context);
                        token = determineDeviceToken(zis);
                        if (zis != null) {
                            try {
                                zis.close();
                            } catch (IOException e) {
                                Fabric.getLogger().e("Beta", "Failed to close the APK file", e);
                            }
                        }
                    } catch (IOException e2) {
                        Fabric.getLogger().e("Beta", "Failed to read the APK file", e2);
                        if (zis != null) {
                            try {
                                zis.close();
                            } catch (IOException e3) {
                                Fabric.getLogger().e("Beta", "Failed to close the APK file", e3);
                            }
                        }
                    }
                } catch (PackageManager.NameNotFoundException e4) {
                    Fabric.getLogger().e("Beta", "Failed to find this app in the PackageManager", e4);
                    if (zis != null) {
                        try {
                            zis.close();
                        } catch (IOException e5) {
                            Fabric.getLogger().e("Beta", "Failed to close the APK file", e5);
                        }
                    }
                }
            } catch (FileNotFoundException e6) {
                Fabric.getLogger().e("Beta", "Failed to find the APK file", e6);
                if (zis != null) {
                    try {
                        zis.close();
                    } catch (IOException e7) {
                        Fabric.getLogger().e("Beta", "Failed to close the APK file", e7);
                    }
                }
            }
            long end = System.nanoTime();
            double millis = (end - start) / 1000000.0d;
            Fabric.getLogger().d("Beta", "Beta device token load took " + millis + "ms");
            return token;
        } catch (Throwable th) {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e8) {
                    Fabric.getLogger().e("Beta", "Failed to close the APK file", e8);
                }
            }
            throw th;
        }
    }

    ZipInputStream getZipInputStreamOfAppApkFrom(Context context) throws PackageManager.NameNotFoundException, FileNotFoundException {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
        return new ZipInputStream(new FileInputStream(info.sourceDir));
    }

    String determineDeviceToken(ZipInputStream zis) throws IOException {
        String name;
        do {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null) {
                return "";
            }
            name = entry.getName();
        } while (!name.startsWith("assets/com.crashlytics.android.beta/dirfactor-device-token="));
        return name.substring("assets/com.crashlytics.android.beta/dirfactor-device-token=".length(), name.length() - 1);
    }
}
