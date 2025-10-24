package co.vine.android.model.impl;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class Timeline {
    public ArrayList<Long> itemIds;
    public TimelineDetails timelineDetails;

    public Timeline(TimelineDetails timelineDetails, ArrayList<Long> itemIds) {
        this.timelineDetails = timelineDetails;
        this.itemIds = itemIds;
    }
}
