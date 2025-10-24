package co.vine.android.service.components.userinteraction;

import android.content.SharedPreferences;
import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.client.SessionManager;
import co.vine.android.util.Util;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
final class UnfollowActionNotifier implements ListenerNotifier {
    private final ArrayList<UserInteractionsListener> mListeners;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();

    public UnfollowActionNotifier(ArrayList<UserInteractionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long userId = b.getLong("follow_id");
        AppController.getInstance(notification.context).removeFollowPosts(this.mSessionManager.getCurrentSession(), userId);
        SharedPreferences prefs = Util.getDefaultSharedPrefs(notification.context);
        SharedPreferences.Editor e = prefs.edit();
        int oldCount = prefs.getInt("profile_follow_count", 0);
        e.putInt("profile_follow_count", oldCount > 0 ? oldCount - 1 : 0);
        e.commit();
        Iterator<UserInteractionsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            UserInteractionsListener listener = it.next();
            listener.onUnFollowComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, userId);
        }
    }
}
