package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

/* loaded from: classes2.dex */
class OAuthHandler extends AuthHandler {
    public OAuthHandler(TwitterAuthConfig authConfig, Callback<TwitterSession> callback, int requestCode) {
        super(authConfig, callback, requestCode);
    }

    @Override // com.twitter.sdk.android.core.identity.AuthHandler
    public boolean authorize(Activity activity) {
        activity.startActivityForResult(newIntent(activity), this.requestCode);
        return true;
    }

    Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, (Class<?>) OAuthActivity.class);
        intent.putExtra("auth_config", getAuthConfig());
        return intent;
    }
}
