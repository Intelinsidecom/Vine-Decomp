package com.crashlytics.android.core.internal.models;

/* loaded from: classes2.dex */
public class SignalData {
    public final String code;
    public final long faultAddress;
    public final String name;

    public SignalData(String name, String code, long faultAddress) {
        this.name = name;
        this.code = code;
        this.faultAddress = faultAddress;
    }
}
