package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public final class UnrevineActionNotifier implements ListenerNotifier {
    Iterable<PostActionsListener> mListeners;

    UnrevineActionNotifier(Iterable<PostActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        for (PostActionsListener listener : this.mListeners) {
            listener.onUnrevine(notification.reqId, notification.statusCode, notification.reasonPhrase, postId);
        }
    }
}
