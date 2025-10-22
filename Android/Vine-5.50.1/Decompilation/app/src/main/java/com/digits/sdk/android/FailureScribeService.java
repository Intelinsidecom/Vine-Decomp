package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* loaded from: classes.dex */
class FailureScribeService implements DigitsScribeService {
    private final DigitsScribeClient scribeClient;

    FailureScribeService(DigitsScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void impression() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("fallback").setElement("").setAction("impression").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void failure() {
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void click(DigitsScribeConstants.Element element) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent("fallback").setElement(element.toString()).setAction("click").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void success() {
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void error(DigitsException exception) {
    }
}
