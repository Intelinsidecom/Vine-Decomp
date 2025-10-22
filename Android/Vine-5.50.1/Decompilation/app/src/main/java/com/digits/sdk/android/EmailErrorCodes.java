package com.digits.sdk.android;

import android.content.res.Resources;

/* loaded from: classes.dex */
public class EmailErrorCodes extends DigitsErrorCodes {
    @Override // com.digits.sdk.android.DigitsErrorCodes, com.digits.sdk.android.ErrorCodes
    public /* bridge */ /* synthetic */ String getDefaultMessage() {
        return super.getDefaultMessage();
    }

    @Override // com.digits.sdk.android.DigitsErrorCodes, com.digits.sdk.android.ErrorCodes
    public /* bridge */ /* synthetic */ String getMessage(int x0) {
        return super.getMessage(x0);
    }

    @Override // com.digits.sdk.android.DigitsErrorCodes, com.digits.sdk.android.ErrorCodes
    public /* bridge */ /* synthetic */ String getNetworkError() {
        return super.getNetworkError();
    }

    public EmailErrorCodes(Resources resources) {
        super(resources);
        this.codeIdMap.put(120, R.string.dgts__try_again_email);
    }
}
