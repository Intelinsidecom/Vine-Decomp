package com.digits.sdk.android;

import android.app.Activity;

/* loaded from: classes.dex */
public interface ActivityClassManager {
    Class<? extends Activity> getConfirmationActivity();

    Class<? extends Activity> getEmailRequestActivity();

    Class<? extends Activity> getFailureActivity();

    Class<? extends Activity> getLoginCodeActivity();

    Class<? extends Activity> getPhoneNumberActivity();

    Class<? extends Activity> getPinCodeActivity();
}
