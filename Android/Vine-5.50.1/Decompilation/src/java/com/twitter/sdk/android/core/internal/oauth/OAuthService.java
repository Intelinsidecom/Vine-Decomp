package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.DefaultClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.TwitterApi;
import javax.net.ssl.SSLSocketFactory;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/* loaded from: classes.dex */
abstract class OAuthService {
    private final TwitterApi api;
    private final RestAdapter apiAdapter;
    private final SSLSocketFactory sslSocketFactory;
    private final TwitterCore twitterCore;
    private final String userAgent;

    public OAuthService(TwitterCore twitterCore, SSLSocketFactory sslSocketFactory, TwitterApi api) {
        this.twitterCore = twitterCore;
        this.sslSocketFactory = sslSocketFactory;
        this.api = api;
        this.userAgent = TwitterApi.buildUserAgent("TwitterAndroidSDK", twitterCore.getVersion());
        this.apiAdapter = new RestAdapter.Builder().setEndpoint(getApi().getBaseHostUrl()).setClient(new DefaultClient(this.sslSocketFactory)).setRequestInterceptor(new RequestInterceptor() { // from class: com.twitter.sdk.android.core.internal.oauth.OAuthService.1
            @Override // retrofit.RequestInterceptor
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("User-Agent", OAuthService.this.getUserAgent());
            }
        }).build();
    }

    protected TwitterCore getTwitterCore() {
        return this.twitterCore;
    }

    protected TwitterApi getApi() {
        return this.api;
    }

    protected String getUserAgent() {
        return this.userAgent;
    }

    protected RestAdapter getApiAdapter() {
        return this.apiAdapter;
    }
}
