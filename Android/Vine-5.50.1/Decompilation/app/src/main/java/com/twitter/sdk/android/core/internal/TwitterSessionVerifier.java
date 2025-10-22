package com.twitter.sdk.android.core.internal;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.internal.scribe.DefaultScribeClient;
import com.twitter.sdk.android.core.internal.scribe.EventNamespace;
import com.twitter.sdk.android.core.internal.scribe.TwitterCoreScribeClientHolder;
import com.twitter.sdk.android.core.services.AccountService;
import retrofit.RetrofitError;

/* loaded from: classes.dex */
public class TwitterSessionVerifier implements SessionVerifier {
    private final AccountServiceProvider accountServiceProvider = new AccountServiceProvider();
    private final DefaultScribeClient scribeClient = TwitterCoreScribeClientHolder.getScribeClient();

    @Override // com.twitter.sdk.android.core.internal.SessionVerifier
    public void verifySession(Session session) {
        AccountService accountService = this.accountServiceProvider.getAccountService(session);
        try {
            scribeVerifySession();
            accountService.verifyCredentials(true, false);
        } catch (RetrofitError e) {
        }
    }

    private void scribeVerifySession() {
        if (this.scribeClient != null) {
            EventNamespace ns = new EventNamespace.Builder().setClient("android").setPage("credentials").setSection("").setComponent("").setElement("").setAction("impression").builder();
            this.scribeClient.scribe(ns);
        }
    }

    /* loaded from: classes2.dex */
    protected static class AccountServiceProvider {
        protected AccountServiceProvider() {
        }

        public AccountService getAccountService(Session session) {
            return new TwitterApiClient(session).getAccountService();
        }
    }
}
