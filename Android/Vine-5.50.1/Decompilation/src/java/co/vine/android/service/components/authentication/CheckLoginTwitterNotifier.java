package co.vine.android.service.components.authentication;

import android.content.Intent;
import android.os.Bundle;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineKnownErrors;
import co.vine.android.api.VineLogin;
import co.vine.android.api.VineUser;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.SessionManager;
import co.vine.android.service.SessionChangedReceiver;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import com.mobileapptracker.MATEvent;
import java.util.List;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class CheckLoginTwitterNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public CheckLoginTwitterNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        int statusCode = notification.statusCode;
        VineLogin login = (VineLogin) b.getParcelable(MATEvent.LOGIN);
        if (b.getBoolean("a_exists") && statusCode == 200) {
            boolean accountCreated = true;
            try {
                this.mSessionManager.loginComplete(notification.context, statusCode, login, (VineUser) b.getParcelable(PropertyConfiguration.USER));
            } catch (VineLoggingException e) {
                accountCreated = false;
                CrashUtil.logException(e);
            }
            if (accountCreated) {
                for (AuthenticationActionListener listener : this.mListeners) {
                    listener.onCheckTwitterLoginSuccess(login);
                }
            } else {
                for (AuthenticationActionListener listener2 : this.mListeners) {
                    listener2.onCheckTwitterLoginFailedToCreateLocalAccount(notification.context);
                }
            }
        } else {
            int errorCode = b.getInt("error_code", -1);
            if (errorCode == VineKnownErrors.BAD_CREDENTIALS.code) {
                for (AuthenticationActionListener listener3 : this.mListeners) {
                    listener3.onCheckTwitterLoginBadCredentials(login);
                }
            } else if (errorCode == VineKnownErrors.ACCOUNT_DEACTIVATED.code) {
                for (AuthenticationActionListener listener4 : this.mListeners) {
                    listener4.onCheckTwitterLoginDeactivated(login);
                }
            } else {
                for (AuthenticationActionListener listener5 : this.mListeners) {
                    listener5.onCheckTwitterLoginUnknownError(notification);
                }
            }
        }
        Intent loginIntent = SessionChangedReceiver.createSessionLoginIntent();
        notification.context.sendBroadcast(loginIntent, CrossConstants.BROADCAST_PERMISSION);
    }
}
