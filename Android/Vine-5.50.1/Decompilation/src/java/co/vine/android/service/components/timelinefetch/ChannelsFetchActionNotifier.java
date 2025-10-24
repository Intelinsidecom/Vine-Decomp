package co.vine.android.service.components.timelinefetch;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public class ChannelsFetchActionNotifier implements ListenerNotifier {
    private final Iterable<TimelineFetchActionsListener> mListeners;

    ChannelsFetchActionNotifier(Iterable<TimelineFetchActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (notification.statusCode == 200) {
            for (TimelineFetchActionsListener listener : this.mListeners) {
                listener.onChannelsFetched(notification.reqId, notification.statusCode, notification.reasonPhrase, notification.b);
            }
        }
    }
}
