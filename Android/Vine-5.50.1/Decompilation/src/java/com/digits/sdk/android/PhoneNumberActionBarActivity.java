package com.digits.sdk.android;

/* loaded from: classes.dex */
public class PhoneNumberActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PhoneNumberActivityDelegate(new PhoneNumberScribeService(Digits.getInstance().getScribeClient()));
    }
}
