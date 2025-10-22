package co.vine.android.service.components.timelinefetch;

import android.os.Bundle;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemWrapper;
import co.vine.android.client.ListenerNotifier;
import co.vine.android.client.ServiceNotification;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class TimelineFetchActionNotifier implements ListenerNotifier {
    private final Iterable<TimelineFetchActionsListener> mListeners;

    TimelineFetchActionNotifier(Iterable<TimelineFetchActionsListener> listeners) {
        this.mListeners = listeners;
    }

    @Override // co.vine.android.client.ListenerNotifier
    public void notifyListeners(ServiceNotification notification) {
        if (notification.statusCode == 200) {
            Bundle b = notification.b;
            ArrayList<TimelineItem> timelineItems = TimelineItemWrapper.unbundleTimelineItemList(b, "timeline_items");
            int type = b.getInt("type", -1);
            long userId = b.getLong("profile_id");
            String sort = b.getString("sort");
            int count = b.getInt("count", 0);
            int size = b.getInt("size", 0);
            String title = b.getString("title");
            boolean userInitiated = b.getBoolean("user_init");
            boolean memory = b.getBoolean("in_memory", false);
            boolean network = b.getBoolean("network", false);
            UrlCachePolicy cachePolicy = (UrlCachePolicy) b.getParcelable("cache_policy");
            int fetchType = b.getInt("fetch_type", 1);
            VineModelFactory.getMutableModelInstance().getMutableTimelineItemModel().updateTimelineItems(timelineItems);
            TimelineDetails timelineDetails = new TimelineDetails(type, Long.valueOf(userId), sort);
            ArrayList<Long> itemIds = new ArrayList<>();
            Iterator<TimelineItem> it = timelineItems.iterator();
            while (it.hasNext()) {
                TimelineItem item = it.next();
                itemIds.add(Long.valueOf(item.getId()));
            }
            if (userInitiated && fetchType == 2) {
                VineModelFactory.getMutableModelInstance().getMutableTimelineModel().replaceTimeline(timelineDetails, itemIds);
            } else {
                VineModelFactory.getMutableModelInstance().getMutableTimelineModel().updateTimeline(timelineDetails, itemIds, fetchType == 1);
            }
            for (TimelineFetchActionsListener listener : this.mListeners) {
                listener.onTimelineFetched(notification.reqId, notification.statusCode, notification.reasonPhrase, type, count, memory, userInitiated, size, title, cachePolicy, network, b);
            }
        }
    }
}
