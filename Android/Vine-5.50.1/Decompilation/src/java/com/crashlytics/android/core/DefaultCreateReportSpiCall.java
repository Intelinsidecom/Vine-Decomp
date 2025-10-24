package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import io.fabric.sdk.android.services.common.ResponseParser;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.Map;

/* loaded from: classes.dex */
class DefaultCreateReportSpiCall extends AbstractSpiCall implements CreateReportSpiCall {
    public DefaultCreateReportSpiCall(Kit kit, String protocolAndHostOverride, String url, HttpRequestFactory requestFactory) {
        super(kit, protocolAndHostOverride, url, requestFactory, HttpMethod.POST);
    }

    @Override // com.crashlytics.android.core.CreateReportSpiCall
    public boolean invoke(CreateReportRequest requestData) throws HttpRequest.HttpRequestException {
        HttpRequest httpRequest = applyMultipartDataTo(applyHeadersTo(getHttpRequest(), requestData), requestData);
        Fabric.getLogger().d("Fabric", "Sending report to: " + getUrl());
        int statusCode = httpRequest.code();
        Fabric.getLogger().d("Fabric", "Create report request ID: " + httpRequest.header("X-REQUEST-ID"));
        Fabric.getLogger().d("Fabric", "Result was: " + statusCode);
        return ResponseParser.parse(statusCode) == 0;
    }

    private HttpRequest applyHeadersTo(HttpRequest request, CreateReportRequest requestData) {
        HttpRequest request2 = request.header("X-CRASHLYTICS-API-KEY", requestData.apiKey).header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", CrashlyticsCore.getInstance().getVersion());
        Map<String, String> customHeaders = requestData.report.getCustomHeaders();
        for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
            request2 = request2.header(entry);
        }
        return request2;
    }

    private HttpRequest applyMultipartDataTo(HttpRequest request, CreateReportRequest requestData) {
        Report report = requestData.report;
        return request.part("report[file]", report.getFileName(), "application/octet-stream", report.getFile()).part("report[identifier]", report.getIdentifier());
    }
}
