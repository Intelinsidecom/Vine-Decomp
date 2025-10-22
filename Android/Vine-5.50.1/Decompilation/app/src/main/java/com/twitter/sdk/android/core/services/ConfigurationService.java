package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import retrofit.http.GET;

/* loaded from: classes.dex */
public interface ConfigurationService {
    @GET("/1.1/help/configuration.json")
    void configuration(Callback<Object> callback);
}
