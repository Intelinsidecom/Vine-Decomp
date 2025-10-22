package io.fabric.sdk.android.services.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import io.fabric.sdk.android.Fabric;

/* loaded from: classes.dex */
public class ApiKey {
    public String getValue(Context context) {
        String apiKey = getApiKeyFromManifest(context);
        if (TextUtils.isEmpty(apiKey)) {
            apiKey = getApiKeyFromStrings(context);
        }
        if (TextUtils.isEmpty(apiKey)) {
            logErrorOrThrowException(context);
        }
        return apiKey;
    }

    protected String getApiKeyFromManifest(Context context) throws PackageManager.NameNotFoundException {
        try {
            String packageName = context.getPackageName();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 128);
            Bundle bundle = ai.metaData;
            if (bundle == null) {
                return null;
            }
            String apiKey = bundle.getString("io.fabric.ApiKey");
            if (apiKey == null) {
                Fabric.getLogger().d("Fabric", "Falling back to Crashlytics key lookup from Manifest");
                return bundle.getString("com.crashlytics.ApiKey");
            }
            return apiKey;
        } catch (Exception e) {
            Fabric.getLogger().d("Fabric", "Caught non-fatal exception while retrieving apiKey: " + e);
            return null;
        }
    }

    protected String getApiKeyFromStrings(Context context) throws Resources.NotFoundException {
        int id = CommonUtils.getResourcesIdentifier(context, "io.fabric.ApiKey", "string");
        if (id == 0) {
            Fabric.getLogger().d("Fabric", "Falling back to Crashlytics key lookup from Strings");
            id = CommonUtils.getResourcesIdentifier(context, "com.crashlytics.ApiKey", "string");
        }
        if (id == 0) {
            return null;
        }
        String apiKey = context.getResources().getString(id);
        return apiKey;
    }

    protected void logErrorOrThrowException(Context context) {
        if (Fabric.isDebuggable() || CommonUtils.isAppDebuggable(context)) {
            throw new IllegalArgumentException(buildApiKeyInstructions());
        }
        Fabric.getLogger().e("Fabric", buildApiKeyInstructions());
    }

    protected String buildApiKeyInstructions() {
        return "Fabric could not be initialized, API key missing from AndroidManifest.xml. Add the following tag to your Application element \n\t<meta-data android:name=\"io.fabric.ApiKey\" android:value=\"YOUR_API_KEY\"/>";
    }
}
