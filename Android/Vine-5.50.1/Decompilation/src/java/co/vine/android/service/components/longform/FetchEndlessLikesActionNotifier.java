package co.vine.android.service.components.longform;

import android.os.Bundle;
import co.vine.android.api.VineEndlessLikesRecord;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class FetchEndlessLikesActionNotifier implements ListenerNotifier {
    private final Iterable<LongformActionsListener> mListeners;

    FetchEndlessLikesActionNotifier(Iterable<LongformActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        ArrayList<VineEndlessLikesRecord> endlessLikes = (ArrayList) b.getSerializable("endless_likes");
        for (LongformActionsListener listener : this.mListeners) {
            listener.onFetchEndlessLikesComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, endlessLikes);
        }
    }
}
