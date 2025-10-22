package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class UnsubscribeActionNotifier implements ListenerNotifier {
    private final ArrayList<UserInteractionsListener> mListeners;

    public UnsubscribeActionNotifier(ArrayList<UserInteractionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long userId = b.getLong("user_id");
        Iterator<UserInteractionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            UserInteractionsListener listener = it.next();
            listener.onUnsubscribeComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, userId);
        }
    }
}
