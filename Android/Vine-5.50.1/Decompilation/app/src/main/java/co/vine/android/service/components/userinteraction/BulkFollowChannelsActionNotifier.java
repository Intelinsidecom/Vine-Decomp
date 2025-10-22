package co.vine.android.service.components.userinteraction;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BulkFollowChannelsActionNotifier implements ListenerNotifier {
    private final ArrayList<UserInteractionsListener> mListeners;

    public BulkFollowChannelsActionNotifier(ArrayList<UserInteractionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Iterator<UserInteractionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            UserInteractionsListener listener = it.next();
            listener.onBulkFollowChannelsComplete(notification.reqId, notification.statusCode, notification.reasonPhrase);
        }
    }
}
