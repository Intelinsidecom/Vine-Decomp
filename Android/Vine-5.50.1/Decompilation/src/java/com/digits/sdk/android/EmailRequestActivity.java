package com.digits.sdk.android;

/* loaded from: classes.dex */
public class EmailRequestActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new EmailRequestActivityDelegate(new EmailRequestScribeService(Digits.getInstance().getScribeClient()));
    }
}
