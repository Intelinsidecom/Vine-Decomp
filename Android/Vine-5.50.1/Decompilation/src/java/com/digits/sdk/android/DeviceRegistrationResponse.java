package com.digits.sdk.android;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class DeviceRegistrationResponse {

    @SerializedName("config")
    public AuthConfig authConfig;

    @SerializedName("phone_number")
    public String normalizedPhoneNumber;
}
