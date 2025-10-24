package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

/* loaded from: classes2.dex */
public abstract class AuthHandler {
    private final Callback<TwitterSession> callback;
    private final TwitterAuthConfig config;
    protected final int requestCode;

    public abstract boolean authorize(Activity activity);

    AuthHandler(TwitterAuthConfig authConfig, Callback<TwitterSession> callback, int requestCode) {
        this.config = authConfig;
        this.callback = callback;
        this.requestCode = requestCode;
    }

    TwitterAuthConfig getAuthConfig() {
        return this.config;
    }
}
