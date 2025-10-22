package com.flurry.sdk;

/* renamed from: com.flurry.sdk.do, reason: invalid class name */
/* loaded from: classes.dex */
public enum Cdo {
    PhoneId(0, true),
    Sha1Imei(5, false),
    AndroidAdvertisingId(13, true);

    public final int d;
    public final boolean e;

    Cdo(int i, boolean z) {
        this.d = i;
        this.e = z;
    }
}
