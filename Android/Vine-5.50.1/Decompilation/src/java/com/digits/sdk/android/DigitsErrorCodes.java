package com.digits.sdk.android;

import android.content.res.Resources;
import android.util.SparseIntArray;

/* loaded from: classes.dex */
class DigitsErrorCodes implements ErrorCodes {
    protected final SparseIntArray codeIdMap = new SparseIntArray(10);
    private final Resources resources;

    DigitsErrorCodes(Resources resources) {
        this.codeIdMap.put(88, R.string.dgts__confirmation_error_alternative);
        this.codeIdMap.put(284, R.string.dgts__network_error);
        this.codeIdMap.put(302, R.string.dgts__network_error);
        this.codeIdMap.put(240, R.string.dgts__network_error);
        this.codeIdMap.put(87, R.string.dgts__network_error);
        this.resources = resources;
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getMessage(int code) {
        int idx = this.codeIdMap.indexOfKey(code);
        return idx < 0 ? getDefaultMessage() : this.resources.getString(this.codeIdMap.valueAt(idx));
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getDefaultMessage() {
        return this.resources.getString(R.string.dgts__try_again);
    }

    @Override // com.digits.sdk.android.ErrorCodes
    public String getNetworkError() {
        return this.resources.getString(R.string.dgts__network_error);
    }
}
