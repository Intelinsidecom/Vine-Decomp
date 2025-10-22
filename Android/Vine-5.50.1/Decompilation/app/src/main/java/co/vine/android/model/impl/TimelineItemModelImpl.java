package co.vine.android.model.impl;

import co.vine.android.api.TimelineItem;
import co.vine.android.model.MutableTimelineItemModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class TimelineItemModelImpl implements MutableTimelineItemModel {
    private final ModelEventsImpl mModelEvents;
    private final ConcurrentHashMap<Long, TimelineItem> mTimelineItems = new ConcurrentHashMap<>();

    TimelineItemModelImpl(ModelEventsImpl modelEvents) {
        this.mModelEvents = modelEvents;
    }

    @Override // co.vine.android.model.TimelineItemModel
    public TimelineItem getTimelineItem(long id) {
        return this.mTimelineItems.get(Long.valueOf(id));
    }

    @Override // co.vine.android.model.MutableTimelineItemModel
    public void updateTimelineItem(long id, TimelineItem timelineItem) {
        this.mTimelineItems.put(Long.valueOf(id), timelineItem);
    }

    @Override // co.vine.android.model.MutableTimelineItemModel
    public void updateTimelineItems(ArrayList<TimelineItem> timelineItems) {
        Iterator<TimelineItem> it = timelineItems.iterator();
        while (it.hasNext()) {
            TimelineItem item = it.next();
            updateTimelineItem(item.getId(), item);
        }
    }
}
