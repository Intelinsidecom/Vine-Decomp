package co.vine.android.service.components.authentication;

import co.vine.android.R;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.util.Util;
import java.util.List;

/* loaded from: classes.dex */
public final class DigitsVerifyNotifier implements ListenerNotifier {
    private final List<AuthenticationActionListener> mListeners;

    public DigitsVerifyNotifier(List<AuthenticationActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (notification.statusCode == 200) {
            for (AuthenticationActionListener listener : this.mListeners) {
                listener.digitVerificationSuccess();
            }
            return;
        }
        Util.showCenteredToast(notification.context, R.string.error_server);
        for (AuthenticationActionListener listener2 : this.mListeners) {
            listener2.digitVerificationFailure();
        }
    }
}
