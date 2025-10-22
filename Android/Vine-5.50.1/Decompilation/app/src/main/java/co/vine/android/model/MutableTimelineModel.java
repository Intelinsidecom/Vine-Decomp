package co.vine.android.model;

import co.vine.android.model.impl.TimelineDetails;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface MutableTimelineModel extends TimelineModel {
    void replaceTimeline(TimelineDetails timelineDetails, ArrayList<Long> arrayList);

    void updateTimeline(TimelineDetails timelineDetails, ArrayList<Long> arrayList, boolean z);
}
