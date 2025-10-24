package com.twitter.sdk.android.core;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import java.util.List;
import retrofit.client.Header;

/* loaded from: classes2.dex */
class TwitterRateLimit {
    private final long epochSeconds;
    private int remainingRequest;
    private int requestLimit;
    private long resetSeconds;

    TwitterRateLimit(List<Header> headers) {
        this(headers, new SystemCurrentTimeProvider());
    }

    TwitterRateLimit(List<Header> headers, CurrentTimeProvider timeProvider) {
        if (headers == null) {
            throw new IllegalArgumentException("headers must not be null");
        }
        this.epochSeconds = timeProvider.getCurrentTimeMillis() / 1000;
        for (Header header : headers) {
            if ("x-rate-limit-limit".equals(header.getName())) {
                this.requestLimit = Integer.valueOf(header.getValue()).intValue();
            } else if ("x-rate-limit-remaining".equals(header.getName())) {
                this.remainingRequest = Integer.valueOf(header.getValue()).intValue();
            } else if ("x-rate-limit-reset".equals(header.getName())) {
                this.resetSeconds = Long.valueOf(header.getValue()).longValue();
            }
        }
    }
}
