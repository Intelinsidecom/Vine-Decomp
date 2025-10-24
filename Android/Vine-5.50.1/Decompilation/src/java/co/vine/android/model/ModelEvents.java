package co.vine.android.model;

import co.vine.android.model.impl.TimelineDetails;

/* loaded from: classes.dex */
public interface ModelEvents {

    public interface ModelListener {
        void onTagsAdded(TagModel tagModel, String str);

        void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails);
    }

    void addListener(ModelListener modelListener);

    void removeListener(ModelListener modelListener);
}
