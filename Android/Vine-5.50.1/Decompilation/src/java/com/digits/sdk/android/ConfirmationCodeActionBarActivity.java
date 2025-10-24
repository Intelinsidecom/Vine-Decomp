package com.digits.sdk.android;

/* loaded from: classes.dex */
public class ConfirmationCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new ConfirmationCodeActivityDelegate(new ConfirmationCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
