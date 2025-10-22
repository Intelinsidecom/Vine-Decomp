package co.vine.android.service.components.share;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public final class NetworkShareActionNotifier implements ListenerNotifier {
    private final Iterable<ShareActionListener> mListeners;

    NetworkShareActionNotifier(Iterable<ShareActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        String network = b.getString("network");
        String message = b.getString("message");
        for (ShareActionListener listener : this.mListeners) {
            listener.onNetworkShared(notification.reqId, notification.statusCode, notification.reasonPhrase, postId, network, message);
        }
    }
}
