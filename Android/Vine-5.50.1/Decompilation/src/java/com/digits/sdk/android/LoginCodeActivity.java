package com.digits.sdk.android;

/* loaded from: classes.dex */
public class LoginCodeActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new LoginCodeActivityDelegate(new LoginCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
