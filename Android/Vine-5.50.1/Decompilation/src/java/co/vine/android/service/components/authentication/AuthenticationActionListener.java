package co.vine.android.service.components.authentication;

import android.content.Context;
import co.vine.android.R;
import co.vine.android.api.VineLogin;
import co.vine.android.client.ServiceNotification;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public abstract class AuthenticationActionListener {
    public void onSignUpComplete(String reqId, int statusCode, String reasonPhrase, VineLogin vineLogin, String accountName, String password, int status) {
    }

    public void digitVerificationSuccess() {
    }

    public void digitVerificationFailure() {
    }

    public void onCheckTwitterLoginSuccess(VineLogin login) {
    }

    public void onCheckTwitterLoginBadCredentials(VineLogin login) {
    }

    public void onCheckTwitterLoginDeactivated(VineLogin login) {
    }

    public void onCheckTwitterLoginUnknownError(ServiceNotification notification) {
        Util.showCenteredToast(notification.context, notification.reasonPhrase);
    }

    public void onCheckTwitterLoginFailedToCreateLocalAccount(Context context) {
        Util.showCenteredToast(context, R.string.failed_to_add_account_on_device);
    }

    public void onLogoutComplete(String reqId) {
    }

    public void onTwitterxAuthComplete(String reqId, int statusCode, String reasonPhrase, VineLogin login) {
    }

    public void onLoginSuccess() {
    }

    public void onLoginFailed(Context context, String reasonPhrase) {
        Util.showCenteredToast(context, reasonPhrase);
    }

    public void onLoginDeactivatedAccount() {
    }
}
