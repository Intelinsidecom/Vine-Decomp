package com.digits.sdk.android;

import com.digits.sdk.android.DigitsApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.SessionVerifier;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
class DigitsSessionVerifier implements SessionVerifier {
    private final VerificationCallback verificationCallback;

    DigitsSessionVerifier() {
        this(new VerificationCallback(new ConcurrentHashMap(), Digits.getSessionManager()));
    }

    DigitsSessionVerifier(VerificationCallback verificationCallback) {
        this.verificationCallback = verificationCallback;
    }

    @Override // com.twitter.sdk.android.core.internal.SessionVerifier
    public void verifySession(Session session) {
        if ((session instanceof DigitsSession) && !((DigitsSession) session).isLoggedOutUser()) {
            DigitsApiClient.AccountService service = getAccountService(session);
            service.verifyAccount(this.verificationCallback);
        }
    }

    DigitsApiClient.AccountService getAccountService(Session session) {
        return new DigitsApiClient(session).getAccountService();
    }

    static class VerificationCallback extends Callback<VerifyAccountResponse> {
        private final ConcurrentHashMap<SessionListener, Boolean> sessionListenerMap;
        private final SessionManager<DigitsSession> sessionManager;

        VerificationCallback(ConcurrentHashMap<SessionListener, Boolean> sessionListenerMap, SessionManager<DigitsSession> sessionManager) {
            this.sessionListenerMap = sessionListenerMap;
            this.sessionManager = sessionManager;
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void success(Result<VerifyAccountResponse> result) {
            if (result.data != null) {
                DigitsSession newSession = DigitsSession.create(result.data);
                if (newSession.isValidUser() && !newSession.equals(this.sessionManager.getSession(newSession.getId()))) {
                    this.sessionManager.setSession(newSession.getId(), newSession);
                    for (SessionListener listener : this.sessionListenerMap.keySet()) {
                        if (listener != null) {
                            listener.changed(newSession);
                        }
                    }
                }
            }
        }

        @Override // com.twitter.sdk.android.core.Callback
        public void failure(TwitterException exception) {
        }
    }
}
