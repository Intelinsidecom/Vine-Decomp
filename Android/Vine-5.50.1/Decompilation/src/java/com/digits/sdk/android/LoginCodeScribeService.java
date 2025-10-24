package com.digits.sdk.android;

import com.digits.sdk.android.DigitsScribeConstants;
import com.mobileapptracker.MATEvent;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* loaded from: classes.dex */
class LoginCodeScribeService implements DigitsScribeService {
    private final DigitsScribeClient scribeClient;

    LoginCodeScribeService(DigitsScribeClient scribeClient) {
        if (scribeClient == null) {
            throw new NullPointerException("scribeClient must not be null");
        }
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void impression() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent(MATEvent.LOGIN).setElement("").setAction("impression").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void failure() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent(MATEvent.LOGIN).setElement("").setAction("failure").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void click(DigitsScribeConstants.Element element) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent(MATEvent.LOGIN).setElement(element.toString()).setAction("click").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void success() {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent(MATEvent.LOGIN).setElement("").setAction("success").builder();
        this.scribeClient.scribe(ns);
    }

    @Override // com.digits.sdk.android.DigitsScribeService
    public void error(DigitsException exception) {
        EventNamespace ns = DigitsScribeConstants.DIGITS_EVENT_BUILDER.setComponent(MATEvent.LOGIN).setElement("").setAction("error").builder();
        this.scribeClient.scribe(ns);
    }
}
