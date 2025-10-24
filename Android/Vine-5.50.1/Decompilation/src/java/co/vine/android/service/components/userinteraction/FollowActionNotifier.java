package co.vine.android.service.components.userinteraction;

import android.content.SharedPreferences;
import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.util.Util;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
final class FollowActionNotifier implements ListenerNotifier {
    private final ArrayList<UserInteractionsListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public FollowActionNotifier(ArrayList<UserInteractionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long userId = b.getLong("follow_id");
        Session session = this.mSessionManager.getCurrentSession();
        AppController.getInstance(notification.context).fetchNewPosts(session, 20, userId, 1, 1, null, null, false, String.valueOf(session.getUserId()), null, null);
        SharedPreferences prefs = Util.getDefaultSharedPrefs(notification.context);
        SharedPreferences.Editor e = prefs.edit();
        e.putInt("profile_follow_count", prefs.getInt("profile_follow_count", 0) + 1);
        e.commit();
        Iterator<UserInteractionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            UserInteractionsListener listener = it.next();
            listener.onFollowComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, userId);
        }
    }
}
