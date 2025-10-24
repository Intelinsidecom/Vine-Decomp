package co.vine.android.service.components.remoterequestcontrol;

import android.content.Intent;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.util.CrossConstants;
import java.util.List;

/* loaded from: classes.dex */
public final class AbortAllRequestsNotifier implements ListenerNotifier {
    private final List<RemoteRequestControlActionListener> mListeners;

    public AbortAllRequestsNotifier(List<RemoteRequestControlActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        notification.context.sendBroadcast(new Intent("abort_all_requests"), CrossConstants.BROADCAST_PERMISSION);
        for (RemoteRequestControlActionListener listener : this.mListeners) {
            listener.onAbortAllRequestsComplete();
        }
    }
}
