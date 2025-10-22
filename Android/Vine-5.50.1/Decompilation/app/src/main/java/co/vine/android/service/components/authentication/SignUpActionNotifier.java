package co.vine.android.service.components.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import co.vine.android.VineLoggingException;
import co.vine.android.api.VineLogin;
import co.vine.android.client.AppController;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.service.SessionChangedReceiver;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.analytics.FlurryUtils;
import com.mobileapptracker.MATEvent;
import java.util.List;

/* loaded from: classes.dex */
public final class SignUpActionNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public SignUpActionNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        VineLogin vineLogin = (VineLogin) b.getParcelable(MATEvent.LOGIN);
        String password = b.getString("pass");
        String accountName = b.getString("a_name");
        Uri avatar = (Uri) b.getParcelable("uri");
        String key = b.getString("s_key");
        long userId = b.getLong("s_owner_id");
        int statusCode = notification.statusCode;
        AppController appController = AppController.getInstance(notification.context);
        int status = 2;
        if (statusCode == 200) {
            if (vineLogin != null && vineLogin.userId > 0) {
                switch (vineLogin.loginType) {
                    case 1:
                        FlurryUtils.trackNuxAccountCreated("email");
                        try {
                            this.mSessionManager.loginComplete(notification.context, statusCode, accountName, password, vineLogin, null);
                            status = 0;
                            break;
                        } catch (VineLoggingException e) {
                            CrashUtil.logException(e);
                            status = 1;
                            break;
                        }
                }
            }
            if (avatar != null) {
                Session session = appController.getActiveSession();
                session.setSessionKey(key);
                session.setUserId(userId);
                appController.updateProfilePhoto(session, avatar);
            }
        }
        for (AuthenticationActionListener listener : this.mListeners) {
            listener.onSignUpComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, vineLogin, accountName, password, status);
        }
        Intent loginIntent = SessionChangedReceiver.createSessionLoginIntent();
        notification.context.sendBroadcast(loginIntent, CrossConstants.BROADCAST_PERMISSION);
    }
}
