package co.vine.android;

import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;

/* loaded from: classes.dex */
public interface TimelineItemScribeActionsListener {
    void onMosaicViewed(VineMosaic vineMosaic, int i);

    void onPostLiked(VinePost vinePost, int i);

    void onPostPlayed(VinePost vinePost, int i);

    void onTimelineItemClicked(TimelineItem timelineItem, int i);

    void onTimelineItemDismissed(TimelineItem timelineItem, int i);
}
