package co.vine.android.service.components.suggestions;

import android.os.Bundle;
import co.vine.android.api.VineUser;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class FetchFriendsTypeaheadActionNotifier implements ListenerNotifier {
    private final Iterable<SuggestionsActionListener> mListeners;

    FetchFriendsTypeaheadActionNotifier(Iterable<SuggestionsActionListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        String query = b.getString("q");
        ArrayList<VineUser> users = b.getParcelableArrayList("users");
        for (SuggestionsActionListener listener : this.mListeners) {
            listener.onGetFriendsTypeAheadComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, query, users);
        }
    }
}
