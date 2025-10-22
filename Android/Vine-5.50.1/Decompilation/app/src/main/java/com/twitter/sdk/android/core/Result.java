package com.twitter.sdk.android.core;

import retrofit.client.Response;

/* loaded from: classes2.dex */
public class Result<T> {
    public final T data;
    public final Response response;

    public Result(T data, Response response) {
        this.data = data;
        this.response = response;
    }
}
