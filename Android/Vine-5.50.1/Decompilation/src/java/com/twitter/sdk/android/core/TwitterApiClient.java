package com.twitter.sdk.android.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.models.SafeListAdapter;
import com.twitter.sdk.android.core.models.SafeMapAdapter;
import com.twitter.sdk.android.core.services.AccountService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLSocketFactory;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.converter.GsonConverter;

/* loaded from: classes.dex */
public class TwitterApiClient {
    final RestAdapter apiAdapter;
    final ConcurrentHashMap<Class, Object> services;
    final RestAdapter uploadAdapter;

    TwitterApiClient(TwitterAuthConfig authConfig, Session session, TwitterApi twitterApi, SSLSocketFactory sslSocketFactory, ExecutorService executorService) {
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null.");
        }
        this.services = new ConcurrentHashMap<>();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new SafeListAdapter()).registerTypeAdapterFactory(new SafeMapAdapter()).create();
        this.apiAdapter = new RestAdapter.Builder().setClient(new AuthenticatedClient(authConfig, session, sslSocketFactory)).setEndpoint(twitterApi.getBaseHostUrl()).setConverter(new GsonConverter(gson)).setExecutors(executorService, new MainThreadExecutor()).build();
        this.uploadAdapter = new RestAdapter.Builder().setClient(new AuthenticatedClient(authConfig, session, sslSocketFactory)).setEndpoint("https://upload.twitter.com").setConverter(new GsonConverter(gson)).setExecutors(executorService, new MainThreadExecutor()).build();
    }

    public TwitterApiClient(Session session) {
        this(TwitterCore.getInstance().getAuthConfig(), session, new TwitterApi(), TwitterCore.getInstance().getSSLSocketFactory(), TwitterCore.getInstance().getFabric().getExecutorService());
    }

    public AccountService getAccountService() {
        return (AccountService) getService(AccountService.class);
    }

    protected <T> T getService(Class<T> cls) {
        return (T) getAdapterService(this.apiAdapter, cls);
    }

    protected <T> T getAdapterService(RestAdapter restAdapter, Class<T> cls) {
        if (!this.services.contains(cls)) {
            this.services.putIfAbsent(cls, restAdapter.create(cls));
        }
        return (T) this.services.get(cls);
    }
}
