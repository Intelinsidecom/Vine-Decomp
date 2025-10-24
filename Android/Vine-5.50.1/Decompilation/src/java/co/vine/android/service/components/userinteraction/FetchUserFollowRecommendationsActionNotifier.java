package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineUser;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
final class FetchUserFollowRecommendationsActionNotifier implements ListenerNotifier {
    private final ArrayList<UserInteractionsListener> mListeners;

    public FetchUserFollowRecommendationsActionNotifier(ArrayList<UserInteractionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        ArrayList<VineUser> users = b.getParcelableArrayList("users");
        Iterator<UserInteractionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            UserInteractionsListener listener = it.next();
            listener.onGetUserFollowRecommendationsComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, users);
        }
    }
}
