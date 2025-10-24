package com.digits.sdk.android;

import retrofit.RequestInterceptor;

/* loaded from: classes.dex */
class DigitsRequestInterceptor implements RequestInterceptor {
    private final DigitsUserAgent userAgent;

    public DigitsRequestInterceptor(DigitsUserAgent userAgent) {
        this.userAgent = userAgent;
    }

    @Override // retrofit.RequestInterceptor
    public void intercept(RequestInterceptor.RequestFacade request) {
        request.addHeader("User-Agent", this.userAgent.toString());
    }
}
