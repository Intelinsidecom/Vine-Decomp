package com.crashlytics.android.beta;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
class CheckForUpdatesRequest extends AbstractSpiCall {
    private final CheckForUpdatesResponseTransform responseTransform;

    public CheckForUpdatesRequest(Kit kit, String protocolAndHostOverride, String url, HttpRequestFactory requestFactory, CheckForUpdatesResponseTransform responseTransform) {
        super(kit, protocolAndHostOverride, url, requestFactory, HttpMethod.GET);
        this.responseTransform = responseTransform;
    }

    public CheckForUpdatesResponse invoke(String apiKey, String idHeaderValue, BuildProperties buildProps) throws HttpRequest.HttpRequestException {
        HttpRequest httpRequest = null;
        try {
            try {
                Map<String, String> queryParams = getQueryParamsFor(buildProps);
                httpRequest = applyHeadersTo(getHttpRequest(queryParams), apiKey, idHeaderValue);
                Fabric.getLogger().d("Beta", "Checking for updates from " + getUrl());
                Fabric.getLogger().d("Beta", "Checking for updates query params are: " + queryParams);
            } catch (Exception e) {
                Fabric.getLogger().e("Beta", "Error while checking for updates from " + getUrl(), e);
                if (httpRequest != null) {
                    String requestId = httpRequest.header("X-REQUEST-ID");
                    Fabric.getLogger().d("Fabric", "Checking for updates request ID: " + requestId);
                }
            }
            if (httpRequest.ok()) {
                Fabric.getLogger().d("Beta", "Checking for updates was successful");
                JSONObject responseJson = new JSONObject(httpRequest.body());
                CheckForUpdatesResponse checkForUpdatesResponseFromJson = this.responseTransform.fromJson(responseJson);
            }
            Fabric.getLogger().e("Beta", "Checking for updates failed. Response code: " + httpRequest.code());
            if (httpRequest != null) {
                String requestId2 = httpRequest.header("X-REQUEST-ID");
                Fabric.getLogger().d("Fabric", "Checking for updates request ID: " + requestId2);
            }
            return null;
        } finally {
            if (httpRequest != null) {
                String requestId3 = httpRequest.header("X-REQUEST-ID");
                Fabric.getLogger().d("Fabric", "Checking for updates request ID: " + requestId3);
            }
        }
    }

    private HttpRequest applyHeadersTo(HttpRequest request, String apiKey, String idHeaderValue) {
        return request.header("Accept", "application/json").header("User-Agent", "Crashlytics Android SDK/" + this.kit.getVersion()).header("X-CRASHLYTICS-DEVELOPER-TOKEN", "bca6990fc3c15a8105800c0673517a4b579634a1").header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion()).header("X-CRASHLYTICS-API-KEY", apiKey).header("X-CRASHLYTICS-D", idHeaderValue);
    }

    private Map<String, String> getQueryParamsFor(BuildProperties buildProps) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("build_version", buildProps.versionCode);
        queryParams.put("display_version", buildProps.versionName);
        queryParams.put("instance", buildProps.buildId);
        queryParams.put("source", "3");
        return queryParams;
    }
}
