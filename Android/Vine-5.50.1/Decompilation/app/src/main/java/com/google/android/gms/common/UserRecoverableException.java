package com.google.android.gms.common;

import android.content.Intent;

/* loaded from: classes2.dex */
public class UserRecoverableException extends Exception {
    private final Intent mIntent;

    public UserRecoverableException(String msg, Intent intent) {
        super(msg);
        this.mIntent = intent;
    }
}
