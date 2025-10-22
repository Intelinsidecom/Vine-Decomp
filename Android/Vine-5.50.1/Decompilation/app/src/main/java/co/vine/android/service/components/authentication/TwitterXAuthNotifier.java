package co.vine.android.service.components.authentication;

import co.vine.android.api.VineLogin;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import com.mobileapptracker.MATEvent;
import java.util.List;

/* loaded from: classes.dex */
public final class TwitterXAuthNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;

    public TwitterXAuthNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        VineLogin vl = (VineLogin) notification.b.getParcelable(MATEvent.LOGIN);
        for (AuthenticationActionListener listener : this.mListeners) {
            listener.onTwitterxAuthComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, vl);
        }
    }
}
