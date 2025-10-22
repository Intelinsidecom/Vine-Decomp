package com.digits.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class DigitsClient {
    private final DigitsAuthRequestQueue authRequestQueue;
    private final Digits digits;
    private DigitsApiClient digitsApiClient;
    private final DigitsScribeService scribeService;
    private final SessionManager<DigitsSession> sessionManager;
    private final TwitterCore twitterCore;
    private final DigitsUserAgent userAgent;

    DigitsClient() {
        this(Digits.getInstance(), new DigitsUserAgent(), TwitterCore.getInstance(), Digits.getSessionManager(), null, new AuthScribeService(Digits.getInstance().getScribeClient()));
    }

    DigitsClient(Digits digits, DigitsUserAgent userAgent, TwitterCore twitterCore, SessionManager<DigitsSession> sessionManager, DigitsAuthRequestQueue authRequestQueue, DigitsScribeService scribeService) {
        if (twitterCore == null) {
            throw new IllegalArgumentException("twitter must not be null");
        }
        if (digits == null) {
            throw new IllegalArgumentException("digits must not be null");
        }
        if (userAgent == null) {
            throw new IllegalArgumentException("userAgent must not be null");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager must not be null");
        }
        this.twitterCore = twitterCore;
        this.digits = digits;
        this.userAgent = userAgent;
        this.sessionManager = sessionManager;
        if (authRequestQueue == null) {
            this.authRequestQueue = createAuthRequestQueue(sessionManager);
            this.authRequestQueue.sessionRestored(null);
        } else {
            this.authRequestQueue = authRequestQueue;
        }
        this.scribeService = scribeService;
    }

    protected DigitsAuthRequestQueue createAuthRequestQueue(SessionManager sessionManager) {
        List<SessionManager<? extends Session>> sessionManagers = new ArrayList<>(1);
        sessionManagers.add(sessionManager);
        DigitsGuestSessionProvider sessionProvider = new DigitsGuestSessionProvider(sessionManager, sessionManagers);
        return new DigitsAuthRequestQueue(this, sessionProvider);
    }

    protected void startSignUp(DigitsAuthConfig digitsAuthConfig) {
        this.scribeService.impression();
        DigitsSession session = (DigitsSession) this.sessionManager.getActiveSession();
        if (session != null && !session.isLoggedOutUser()) {
            digitsAuthConfig.authCallback.success(session, null);
            this.scribeService.success();
        } else {
            startPhoneNumberActivity(createBundleForAuthFlow(digitsAuthConfig));
        }
    }

    private Bundle createBundleForAuthFlow(DigitsAuthConfig digitsAuthConfig) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("receiver", createResultReceiver(digitsAuthConfig.authCallback));
        bundle.putString("phone_number", digitsAuthConfig.phoneNumber);
        bundle.putBoolean("email_enabled", digitsAuthConfig.isEmailRequired);
        return bundle;
    }

    LoginResultReceiver createResultReceiver(AuthCallback callback) {
        return new LoginResultReceiver(callback, this.sessionManager);
    }

    private void startPhoneNumberActivity(Bundle bundle) {
        Context appContext = this.twitterCore.getContext();
        Activity currentActivity = this.digits.getFabric().getCurrentActivity();
        Context selectedContext = (currentActivity == null || currentActivity.isFinishing()) ? appContext : currentActivity;
        int intentFlags = (currentActivity == null || currentActivity.isFinishing()) ? 335544320 : 0;
        Intent intent = new Intent(selectedContext, this.digits.getActivityClassManager().getPhoneNumberActivity());
        intent.putExtras(bundle);
        intent.setFlags(intentFlags);
        selectedContext.startActivity(intent);
    }

    protected void authDevice(final String phoneNumber, final Verification verificationType, Callback<AuthResponse> callback) {
        this.authRequestQueue.addClientRequest(new CallbackWrapper<AuthResponse>(callback) { // from class: com.digits.sdk.android.DigitsClient.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DigitsApiClient> result) {
                result.data.getSdkService().auth(phoneNumber, verificationType.name(), this.callback);
            }
        });
    }

    protected void createAccount(final String pin, final String phoneNumber, Callback<DigitsUser> callback) {
        this.authRequestQueue.addClientRequest(new CallbackWrapper<DigitsUser>(callback) { // from class: com.digits.sdk.android.DigitsClient.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DigitsApiClient> result) {
                result.data.getSdkService().account(phoneNumber, pin, this.callback);
            }
        });
    }

    protected void loginDevice(final String requestId, final long userId, final String code, Callback<DigitsSessionResponse> callback) {
        this.authRequestQueue.addClientRequest(new CallbackWrapper<DigitsSessionResponse>(callback) { // from class: com.digits.sdk.android.DigitsClient.3
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DigitsApiClient> result) {
                result.data.getSdkService().login(requestId, userId, code, this.callback);
            }
        });
    }

    protected void registerDevice(final String phoneNumber, final Verification verificationType, Callback<DeviceRegistrationResponse> callback) {
        this.authRequestQueue.addClientRequest(new CallbackWrapper<DeviceRegistrationResponse>(callback) { // from class: com.digits.sdk.android.DigitsClient.4
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DigitsApiClient> result) {
                result.data.getDeviceService().register(phoneNumber, "third_party_confirmation_code", true, Locale.getDefault().getLanguage(), "digits_sdk", verificationType.name(), this.callback);
            }
        });
    }

    protected void verifyPin(final String requestId, final long userId, final String pin, Callback<DigitsSessionResponse> callback) {
        this.authRequestQueue.addClientRequest(new CallbackWrapper<DigitsSessionResponse>(callback) { // from class: com.digits.sdk.android.DigitsClient.5
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<DigitsApiClient> result) {
                result.data.getSdkService().verifyPin(requestId, userId, pin, this.callback);
            }
        });
    }

    static abstract class CallbackWrapper<T> extends Callback<DigitsApiClient> {
        final Callback<T> callback;

        public CallbackWrapper(Callback<T> callback) {
            this.callback = callback;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
            if (this.callback != null) {
                this.callback.failure(exception);
            }
        }
    }

    DigitsApiClient getApiClient(Session session) {
        if (this.digitsApiClient != null && this.digitsApiClient.getSession().equals(session)) {
            return this.digitsApiClient;
        }
        this.digitsApiClient = new DigitsApiClient(session, this.twitterCore.getAuthConfig(), this.twitterCore.getSSLSocketFactory(), this.digits.getExecutorService(), this.userAgent);
        return this.digitsApiClient;
    }
}
