package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class ApiError {

    @SerializedName("code")
    private final int code;

    public int getCode() {
        return this.code;
    }
}
