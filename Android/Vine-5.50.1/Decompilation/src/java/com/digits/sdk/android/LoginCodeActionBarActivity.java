package com.digits.sdk.android;

/* loaded from: classes.dex */
public class LoginCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new LoginCodeActivityDelegate(new LoginCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
