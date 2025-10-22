package co.vine.android.service.components.postactions;

import android.content.SharedPreferences;
import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public final class LikeActionNotifier implements ListenerNotifier {
    private final Iterable<PostActionsListener> mListeners;

    LikeActionNotifier(Iterable<PostActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        if (notification.statusCode == 200) {
            SharedPreferences prefs = Util.getDefaultSharedPrefs(notification.context);
            SharedPreferences.Editor e = prefs.edit();
            e.putInt("profile_like_count", prefs.getInt("profile_like_count", 0) + 1);
            e.apply();
        }
        for (PostActionsListener listener : this.mListeners) {
            listener.onLikePost(notification.reqId, notification.statusCode, notification.reasonPhrase, postId);
        }
    }
}
