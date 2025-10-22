package co.vine.android.service.components.authentication;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.SessionManager;
import java.util.List;

/* loaded from: classes.dex */
public final class LogoutNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public LogoutNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        this.mSessionManager.clearLocalDataForSessionLogout(notification.context);
        for (AuthenticationActionListener listener : this.mListeners) {
            listener.onLogoutComplete(notification.reqId);
        }
    }
}
