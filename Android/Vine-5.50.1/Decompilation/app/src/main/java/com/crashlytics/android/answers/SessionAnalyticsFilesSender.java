package com.crashlytics.android.answers;

import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.ResponseParser;
import io.fabric.sdk.android.services.events.FilesSender;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
class SessionAnalyticsFilesSender extends AbstractSpiCall implements FilesSender {
    private final String apiKey;

    public SessionAnalyticsFilesSender(Kit kit, String protocolAndHostOverride, String url, HttpRequestFactory requestFactory, String apiKey) {
        super(kit, protocolAndHostOverride, url, requestFactory, HttpMethod.POST);
        this.apiKey = apiKey;
    }

    @Override // io.fabric.sdk.android.services.events.FilesSender
    public boolean send(List<File> files) throws Throwable {
        HttpRequest httpRequest = getHttpRequest();
        HttpRequest httpRequest2 = applyMultipartDataTo(applyHeadersTo(httpRequest, this.apiKey), files);
        CommonUtils.logControlled(Answers.getInstance().getContext(), "Sending " + files.size() + " analytics files to " + getUrl());
        int statusCode = httpRequest2.code();
        CommonUtils.logControlled(Answers.getInstance().getContext(), "Response code for analytics file send is " + statusCode);
        return ResponseParser.parse(statusCode) == 0;
    }

    private HttpRequest applyHeadersTo(HttpRequest request, String apiKey) {
        return request.header("X-CRASHLYTICS-API-CLIENT-TYPE", "android").header("X-CRASHLYTICS-API-CLIENT-VERSION", Answers.getInstance().getVersion()).header("X-CRASHLYTICS-API-KEY", apiKey);
    }

    private HttpRequest applyMultipartDataTo(HttpRequest request, List<File> files) throws Throwable {
        int i = 0;
        for (File file : files) {
            CommonUtils.logControlled(Answers.getInstance().getContext(), "Adding analytics session file " + file.getName() + " to multipart POST");
            request.part("session_analytics_file_" + i, file.getName(), "application/vnd.crashlytics.android.events", file);
            i++;
        }
        return request;
    }
}
