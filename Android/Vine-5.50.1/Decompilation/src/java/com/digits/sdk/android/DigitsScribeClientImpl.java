package com.digits.sdk.android;

import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* loaded from: classes.dex */
public class DigitsScribeClientImpl implements DigitsScribeClient {
    private final DefaultScribeClient scribeClient;

    public DigitsScribeClientImpl(DefaultScribeClient scribeClient) {
        this.scribeClient = scribeClient;
    }

    @Override // com.digits.sdk.android.DigitsScribeClient
    public void scribe(EventNamespace ns) {
        if (this.scribeClient != null) {
            this.scribeClient.scribe(ns);
        }
    }
}
