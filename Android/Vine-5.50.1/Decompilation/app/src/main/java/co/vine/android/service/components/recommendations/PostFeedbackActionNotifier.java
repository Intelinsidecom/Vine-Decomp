package co.vine.android.service.components.recommendations;

import android.os.Bundle;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class PostFeedbackActionNotifier implements ListenerNotifier {
    private final ArrayList<RecommendationsListener> mListeners;

    public PostFeedbackActionNotifier(ArrayList<RecommendationsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        Bundle b = notification.b;
        long postId = b.getLong("post_id");
        String recBoost = b.getString("recommendationBoost");
        Iterator<RecommendationsListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            RecommendationsListener listener = it.next();
            listener.onPostFeedbackComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, postId, recBoost);
        }
    }
}
