package co.vine.android.util;

import android.app.Activity;
import android.content.Intent;
import co.vine.android.ConfirmationFlowActivity;
import co.vine.android.R;
import co.vine.android.client.AppController;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;

/* loaded from: classes.dex */
public final class PhoneConfirmationUtil {
    public static void confirmPhoneNumber(Activity activity, AuthCallback authCallback, String phoneNumber, int requestCodeConfirmPhone) {
        if (ClientFlagsHelper.isDigitsEnabled(activity)) {
            Digits.authenticate(authCallback, R.style.VineThemeDigits, phoneNumber, false);
            return;
        }
        ConfirmationFlowActivity.requestPhoneVerification(AppController.getInstance(activity), phoneNumber);
        Intent i = ConfirmationFlowActivity.getIntent(activity, phoneNumber, false);
        activity.startActivityForResult(i, requestCodeConfirmPhone);
    }
}
