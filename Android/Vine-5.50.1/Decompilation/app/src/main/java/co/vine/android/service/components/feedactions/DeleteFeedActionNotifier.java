package co.vine.android.service.components.feedactions;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public class DeleteFeedActionNotifier implements ListenerNotifier {
    private final Iterable<FeedActionsListener> mListeners;

    DeleteFeedActionNotifier(Iterable<FeedActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long feedId = b.getLong("feed_id");
        for (FeedActionsListener listener : this.mListeners) {
            listener.onDeleteFeed(notification.reqId, notification.statusCode, notification.reasonPhrase, feedId);
        }
    }
}
