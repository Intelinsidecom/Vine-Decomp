package com.digits.sdk.android;

/* loaded from: classes.dex */
public class PinCodeActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PinCodeActivityDelegate(new PinCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
