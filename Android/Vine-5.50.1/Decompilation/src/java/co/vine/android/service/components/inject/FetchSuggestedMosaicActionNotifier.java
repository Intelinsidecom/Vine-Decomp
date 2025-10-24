package co.vine.android.service.components.inject;

import co.vine.android.api.TimelineItem;
import co.vine.android.api.VinePagedData;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;

/* loaded from: classes.dex */
public final class FetchSuggestedMosaicActionNotifier implements ListenerNotifier {
    private final Iterable<InjectionFetchListener> mListeners;

    FetchSuggestedMosaicActionNotifier(Iterable<InjectionFetchListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        for (InjectionFetchListener listener : this.mListeners) {
            VinePagedData<TimelineItem> mosaicTimeline = (VinePagedData) notification.b.getParcelable("mosaic_only_timeline");
            if (mosaicTimeline != null && mosaicTimeline.items != null && mosaicTimeline.items.size() != 0) {
                listener.onFetchSuggestedMosaicComplete(notification.reqId, notification.statusCode, notification.reasonPhrase, mosaicTimeline.items.get(0));
            } else {
                return;
            }
        }
    }
}
