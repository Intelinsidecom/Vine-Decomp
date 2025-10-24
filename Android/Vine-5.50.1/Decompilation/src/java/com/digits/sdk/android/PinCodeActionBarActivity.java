package com.digits.sdk.android;

/* loaded from: classes.dex */
public class PinCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PinCodeActivityDelegate(new PinCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
