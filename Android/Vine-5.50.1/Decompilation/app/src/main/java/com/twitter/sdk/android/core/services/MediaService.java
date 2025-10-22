package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/* loaded from: classes.dex */
public interface MediaService {
    @POST("/1.1/media/upload.json")
    @Multipart
    void upload(@Part("media") TypedFile typedFile, @Part("media_data") TypedFile typedFile2, @Part("additional_owners") TypedString typedString, Callback<Object> callback);
}
