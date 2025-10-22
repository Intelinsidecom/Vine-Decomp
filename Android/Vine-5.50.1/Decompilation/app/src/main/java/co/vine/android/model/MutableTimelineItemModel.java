package co.vine.android.model;

import co.vine.android.api.TimelineItem;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface MutableTimelineItemModel extends TimelineItemModel {
    void updateTimelineItem(long j, TimelineItem timelineItem);

    void updateTimelineItems(ArrayList<TimelineItem> arrayList);
}
