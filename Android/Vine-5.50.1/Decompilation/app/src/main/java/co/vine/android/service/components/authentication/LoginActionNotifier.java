package co.vine.android.service.components.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineKnownErrors;
import co.vine.android.api.VineLogin;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.SessionManager;
import co.vine.android.util.CrashUtil;
import com.mobileapptracker.MATEvent;
import java.util.List;

/* loaded from: classes.dex */
public final class LoginActionNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public LoginActionNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        try {
            Bundle b = notification.b;
            VineLogin vl = (VineLogin) b.getParcelable(MATEvent.LOGIN);
            if (vl != null) {
                try {
                    this.mSessionManager.loginComplete(notification.context, notification.statusCode, b.getString("a_name"), b.getString("pass"), vl, b.getString("avatar_url"));
                } catch (VineLoggingException e) {
                    CrashUtil.logException(e);
                    for (AuthenticationActionListener listener : this.mListeners) {
                        listener.onLoginFailed(notification.context, notification.context.getString(R.string.failed_to_add_account_on_device));
                    }
                    return;
                }
            }
            if (notification.statusCode == 200) {
                for (AuthenticationActionListener listener2 : this.mListeners) {
                    listener2.onLoginSuccess();
                }
                return;
            }
            if (notification.statusCode == VineKnownErrors.ACCOUNT_DEACTIVATED.code) {
                for (AuthenticationActionListener listener3 : this.mListeners) {
                    listener3.onLoginDeactivatedAccount();
                }
                return;
            }
            String reasonPhrase = !TextUtils.isEmpty(notification.reasonPhrase) ? notification.reasonPhrase : notification.context.getString(R.string.error_sign_up);
            CrashUtil.logException(new VineLoggingException("Login failed because " + reasonPhrase + " and status code" + notification.statusCode));
            for (AuthenticationActionListener listener4 : this.mListeners) {
                listener4.onLoginFailed(notification.context, reasonPhrase);
            }
        } catch (Throwable e2) {
            CrashUtil.logOrThrowInDebug(new VineLoggingException(e2));
            throw e2;
        }
    }
}
