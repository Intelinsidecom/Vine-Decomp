package co.vine.android.model.impl;

import co.vine.android.model.MutableTimelineModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class TimelineModelImpl implements MutableTimelineModel {
    private final ModelEventsImpl mModelEvents;
    private final ConcurrentHashMap<TimelineDetails, Timeline> mTimelines = new ConcurrentHashMap<>();

    TimelineModelImpl(ModelEventsImpl modelEvents) {
        this.mModelEvents = modelEvents;
    }

    @Override // co.vine.android.model.MutableTimelineModel
    public void updateTimeline(TimelineDetails timelineDetails, ArrayList<Long> itemIds, boolean olderItems) {
        if (itemIds != null) {
            if (!this.mTimelines.containsKey(timelineDetails)) {
                this.mTimelines.put(timelineDetails, new Timeline(timelineDetails, itemIds));
            } else {
                Timeline timeline = this.mTimelines.get(timelineDetails);
                ArrayList<Long> modelTimelineIds = timeline.itemIds;
                if (olderItems) {
                    timeline.itemIds = mergeItems(modelTimelineIds, itemIds);
                } else {
                    timeline.itemIds = mergeItems(itemIds, modelTimelineIds);
                }
            }
            this.mModelEvents.onTimelineUpdated(this, timelineDetails);
        }
    }

    @Override // co.vine.android.model.MutableTimelineModel
    public void replaceTimeline(TimelineDetails timelineDetails, ArrayList<Long> itemIds) {
        if (itemIds != null) {
            this.mTimelines.put(timelineDetails, new Timeline(timelineDetails, itemIds));
            this.mModelEvents.onTimelineUpdated(this, timelineDetails);
        }
    }

    @Override // co.vine.android.model.TimelineModel
    public Timeline getUserTimeline(TimelineDetails timelineDetails) {
        return this.mTimelines.get(timelineDetails);
    }

    private ArrayList<Long> mergeItems(ArrayList<Long> oldItems, ArrayList<Long> newItems) {
        HashMap<Long, Integer> usedViewIds = new HashMap<>();
        int size = oldItems.size();
        for (int i = 0; i < size; i++) {
            usedViewIds.put(oldItems.get(i), Integer.valueOf(i));
        }
        Iterator<Long> it = newItems.iterator();
        while (it.hasNext()) {
            Long newItem = it.next();
            Integer position = usedViewIds.get(newItem);
            if (position == null) {
                oldItems.add(newItem);
            } else {
                oldItems.set(position.intValue(), newItem);
            }
        }
        return oldItems;
    }
}
