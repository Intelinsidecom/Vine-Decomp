package com.crashlytics.android.answers;

import android.annotation.TargetApi;
import android.os.Build;
import io.fabric.sdk.android.services.events.EventTransform;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class SessionEventTransform implements EventTransform<SessionEvent> {
    SessionEventTransform() {
    }

    @Override // io.fabric.sdk.android.services.events.EventTransform
    public byte[] toBytes(SessionEvent event) throws IOException {
        return buildJsonForEvent(event).toString().getBytes("UTF-8");
    }

    @TargetApi(9)
    public JSONObject buildJsonForEvent(SessionEvent event) throws JSONException, IOException {
        try {
            JSONObject jsonObject = new JSONObject();
            SessionEventMetadata sessionEventMetadata = event.sessionEventMetadata;
            jsonObject.put("appBundleId", sessionEventMetadata.appBundleId);
            jsonObject.put("executionId", sessionEventMetadata.executionId);
            jsonObject.put("installationId", sessionEventMetadata.installationId);
            jsonObject.put("androidId", sessionEventMetadata.androidId);
            jsonObject.put("advertisingId", sessionEventMetadata.advertisingId);
            jsonObject.put("betaDeviceToken", sessionEventMetadata.betaDeviceToken);
            jsonObject.put("buildId", sessionEventMetadata.buildId);
            jsonObject.put("osVersion", sessionEventMetadata.osVersion);
            jsonObject.put("deviceModel", sessionEventMetadata.deviceModel);
            jsonObject.put("appVersionCode", sessionEventMetadata.appVersionCode);
            jsonObject.put("appVersionName", sessionEventMetadata.appVersionName);
            jsonObject.put("timestamp", event.timestamp);
            jsonObject.put("type", event.type.toString());
            jsonObject.put("details", new JSONObject(event.details));
            jsonObject.put("customType", event.customType);
            jsonObject.put("customAttributes", new JSONObject(event.customAttributes));
            return jsonObject;
        } catch (JSONException e) {
            if (Build.VERSION.SDK_INT >= 9) {
                throw new IOException(e.getMessage(), e);
            }
            throw new IOException(e.getMessage());
        }
    }
}
