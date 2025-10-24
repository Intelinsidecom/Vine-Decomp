package com.digits.sdk.android;

/* loaded from: classes.dex */
public class EmailRequestActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new EmailRequestActivityDelegate(new EmailRequestScribeService(Digits.getInstance().getScribeClient()));
    }
}
