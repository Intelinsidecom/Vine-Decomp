package io.fabric.sdk.android.services.settings;

import android.content.res.Resources;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.KitInfo;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.ResponseParser;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/* loaded from: classes.dex */
abstract class AbstractAppSpiCall extends AbstractSpiCall {
    public AbstractAppSpiCall(Kit kit, String protocolAndHostOverride, String url, HttpRequestFactory requestFactory, HttpMethod method) {
        super(kit, protocolAndHostOverride, url, requestFactory, method);
    }

    public boolean invoke(AppRequestData requestData) throws HttpRequest.HttpRequestException, IOException {
        HttpRequest httpRequest = applyMultipartDataTo(applyHeadersTo(getHttpRequest(), requestData), requestData);
        Fabric.getLogger().d("Fabric", "Sending app info to " + getUrl());
        if (requestData.icon != null) {
            Fabric.getLogger().d("Fabric", "App icon hash is " + requestData.icon.hash);
            Fabric.getLogger().d("Fabric", "App icon size is " + requestData.icon.width + "x" + requestData.icon.height);
        }
        int statusCode = httpRequest.code();
        String kind = "POST".equals(httpRequest.method()) ? "Create" : "Update";
        Fabric.getLogger().d("Fabric", kind + " app request ID: " + httpRequest.header("X-REQUEST-ID"));
        Fabric.getLogger().d("Fabric", "Result was " + statusCode);
        return ResponseParser.parse(statusCode) == 0;
    }

    private HttpRequest applyHeadersTo(HttpRequest request, AppRequestData requestData) {
        return request.header("X-CRASHLYTICS-API-KEY", requestData.apiKey).header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", this.kit.getVersion());
    }

    private HttpRequest applyMultipartDataTo(HttpRequest request, AppRequestData requestData) throws IOException {
        HttpRequest request2 = request.part("app[identifier]", requestData.appId).part("app[name]", requestData.name).part("app[display_version]", requestData.displayVersion).part("app[build_version]", requestData.buildVersion).part("app[source]", Integer.valueOf(requestData.source)).part("app[minimum_sdk_version]", requestData.minSdkVersion).part("app[built_sdk_version]", requestData.builtSdkVersion);
        if (!CommonUtils.isNullOrEmpty(requestData.instanceIdentifier)) {
            request2.part("app[instance_identifier]", requestData.instanceIdentifier);
        }
        if (requestData.icon != null) {
            InputStream is = null;
            try {
                is = this.kit.getContext().getResources().openRawResource(requestData.icon.iconResourceId);
                request2.part("app[icon][hash]", requestData.icon.hash).part("app[icon][data]", "icon.png", "application/octet-stream", is).part("app[icon][width]", Integer.valueOf(requestData.icon.width)).part("app[icon][height]", Integer.valueOf(requestData.icon.height));
            } catch (Resources.NotFoundException e) {
                Fabric.getLogger().e("Fabric", "Failed to find app icon with resource ID: " + requestData.icon.iconResourceId, e);
            } finally {
                CommonUtils.closeOrLog(is, "Failed to close app icon InputStream.");
            }
        }
        if (requestData.sdkKits != null) {
            for (KitInfo kit : requestData.sdkKits) {
                request2.part(getKitVersionKey(kit), kit.getVersion());
                request2.part(getKitBuildTypeKey(kit), kit.getBuildType());
            }
        }
        return request2;
    }

    String getKitVersionKey(KitInfo kit) {
        return String.format(Locale.US, "app[build][libraries][%s][version]", kit.getIdentifier());
    }

    String getKitBuildTypeKey(KitInfo kit) {
        return String.format(Locale.US, "app[build][libraries][%s][type]", kit.getIdentifier());
    }
}
