package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.VineRepost;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public final class RevineActionNotifier implements ListenerNotifier {
    private final Iterable<PostActionsListener> mListeners;

    RevineActionNotifier(Iterable<PostActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        VineRepost repost = (VineRepost) b.getSerializable("repost");
        for (PostActionsListener listener : this.mListeners) {
            listener.onRevine(notification.reqId, notification.statusCode, notification.reasonPhrase, postId, repost);
        }
    }
}
