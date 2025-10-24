package co.vine.android.service.components.share;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public final class VMShareActionNotifier implements ListenerNotifier {
    private final Iterable<ShareActionListener> mListeners;

    VMShareActionNotifier(Iterable<ShareActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        String message = b.getString("message");
        for (ShareActionListener listener : this.mListeners) {
            listener.onVMShared(notification.reqId, notification.statusCode, notification.reasonPhrase, postId, message);
        }
    }
}
