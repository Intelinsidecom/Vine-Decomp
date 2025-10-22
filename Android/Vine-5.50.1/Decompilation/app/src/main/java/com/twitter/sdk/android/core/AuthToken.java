package com.twitter.sdk.android.core;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.internal.oauth.AuthHeaders;

/* loaded from: classes.dex */
public abstract class AuthToken implements AuthHeaders {

    @SerializedName("created_at")
    protected final long createdAt = System.currentTimeMillis();

    public abstract boolean isExpired();
}
