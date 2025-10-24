package co.vine.android.service.components.feedactions;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public class ShareFeedActionNotifier implements ListenerNotifier {
    private final Iterable<FeedActionsListener> mListeners;

    ShareFeedActionNotifier(Iterable<FeedActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        for (FeedActionsListener listener : this.mListeners) {
            listener.onShareFeed(notification.reqId, notification.statusCode, notification.reasonPhrase);
        }
    }
}
