package com.crashlytics.android.beta;

import java.io.IOException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class CheckForUpdatesResponseTransform {
    CheckForUpdatesResponseTransform() {
    }

    public CheckForUpdatesResponse fromJson(JSONObject json) throws IOException {
        if (json == null) {
            return null;
        }
        String url = json.optString("url", null);
        String versionString = json.optString("version_string", null);
        String buildVersion = json.optString("build_version", null);
        String displayVersion = json.optString("display_version", null);
        String packageName = json.optString("identifier", null);
        String instanceId = json.optString("instance_identifier", null);
        return new CheckForUpdatesResponse(url, versionString, displayVersion, buildVersion, packageName, instanceId);
    }
}
