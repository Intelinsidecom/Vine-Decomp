package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/* loaded from: classes.dex */
public interface CollectionService {
    @GET("/1.1/collections/entries.json")
    void collection(@Query("id") String str, @Query("count") Integer num, @Query("max_position") Long l, @Query("min_position") Long l2, Callback<Object> callback);
}
