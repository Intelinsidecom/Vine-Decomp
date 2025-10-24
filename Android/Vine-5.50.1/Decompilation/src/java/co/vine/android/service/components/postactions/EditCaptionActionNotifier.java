package co.vine.android.service.components.postactions;

import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public class EditCaptionActionNotifier implements ListenerNotifier {
    private final Iterable<PostActionsListener> mListeners;

    EditCaptionActionNotifier(Iterable<PostActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        for (PostActionsListener listener : this.mListeners) {
            listener.onPostEditCaption(notification.reqId, notification.statusCode, notification.reasonPhrase);
        }
    }
}
