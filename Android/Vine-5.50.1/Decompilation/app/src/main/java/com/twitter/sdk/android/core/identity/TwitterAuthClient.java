package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import android.content.Context;
import com.mobileapptracker.MATEvent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.TwitterCoreScribeClientHolder;
import io.fabric.sdk.android.Fabric;

/* loaded from: classes2.dex */
public class TwitterAuthClient {
    private final TwitterAuthConfig authConfig;
    final AuthState authState;
    private final Context context;
    final SessionManager<TwitterSession> sessionManager;

    private static class AuthStateLazyHolder {
        private static final AuthState INSTANCE = new AuthState();
    }

    public TwitterAuthClient() {
        this(TwitterCore.getInstance().getContext(), TwitterCore.getInstance().getAuthConfig(), TwitterCore.getInstance().getSessionManager(), AuthStateLazyHolder.INSTANCE);
    }

    TwitterAuthClient(Context context, TwitterAuthConfig authConfig, SessionManager<TwitterSession> sessionManager, AuthState authState) {
        this.authState = authState;
        this.context = context;
        this.authConfig = authConfig;
        this.sessionManager = sessionManager;
    }

    public void authorize(Activity activity, Callback<TwitterSession> callback) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity must not be null.");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }
        if (activity.isFinishing()) {
            Fabric.getLogger().e("Twitter", "Cannot authorize, activity is finishing.", null);
        } else {
            handleAuthorize(activity, callback);
        }
    }

    private void handleAuthorize(Activity activity, Callback<TwitterSession> callback) {
        scribeAuthorizeImpression();
        CallbackWrapper callbackWrapper = new CallbackWrapper(this.sessionManager, callback);
        if (!authorizeUsingSSO(activity, callbackWrapper) && !authorizeUsingOAuth(activity, callbackWrapper)) {
            callbackWrapper.failure(new TwitterAuthException("Authorize failed."));
        }
    }

    private boolean authorizeUsingSSO(Activity activity, CallbackWrapper callbackWrapper) {
        if (!SSOAuthHandler.isAvailable(activity)) {
            return false;
        }
        Fabric.getLogger().d("Twitter", "Using SSO");
        return this.authState.beginAuthorize(activity, new SSOAuthHandler(this.authConfig, callbackWrapper, this.authConfig.getRequestCode()));
    }

    private boolean authorizeUsingOAuth(Activity activity, CallbackWrapper callbackWrapper) {
        Fabric.getLogger().d("Twitter", "Using OAuth");
        return this.authState.beginAuthorize(activity, new OAuthHandler(this.authConfig, callbackWrapper, this.authConfig.getRequestCode()));
    }

    private void scribeAuthorizeImpression() {
        DefaultScribeClient scribeClient = getScribeClient();
        if (scribeClient != null) {
            EventNamespace ns = new EventNamespace.Builder().setClient("android").setPage(MATEvent.LOGIN).setSection("").setComponent("").setElement("").setAction("impression").builder();
            scribeClient.scribe(ns);
        }
    }

    protected DefaultScribeClient getScribeClient() {
        return TwitterCoreScribeClientHolder.getScribeClient();
    }

    static class CallbackWrapper extends Callback<TwitterSession> {
        private final Callback<TwitterSession> callback;
        private final SessionManager<TwitterSession> sessionManager;

        public CallbackWrapper(SessionManager<TwitterSession> sessionManager, Callback<TwitterSession> callback) {
            this.sessionManager = sessionManager;
            this.callback = callback;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<TwitterSession> result) {
            Fabric.getLogger().d("Twitter", "Authorization completed successfully");
            this.sessionManager.setActiveSession(result.data);
            this.callback.success(result);
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            Fabric.getLogger().e("Twitter", "Authorization completed with an error", exception);
            this.callback.failure(exception);
        }
    }
}
