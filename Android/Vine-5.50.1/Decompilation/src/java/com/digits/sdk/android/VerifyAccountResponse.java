package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.TwitterAuthToken;

/* loaded from: classes.dex */
public class VerifyAccountResponse {

    @SerializedName("email_address")
    public Email email;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("access_token")
    TwitterAuthToken token;

    @SerializedName("id_str")
    public long userId;
}
