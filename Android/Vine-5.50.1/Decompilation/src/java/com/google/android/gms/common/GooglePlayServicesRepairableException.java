package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: classes2.dex */
public class GooglePlayServicesRepairableException extends UserRecoverableException {
    private final int zzTQ;

    GooglePlayServicesRepairableException(int connectionStatusCode, String msg, Intent intent) {
        super(msg, intent);
        this.zzTQ = connectionStatusCode;
    }
}
