package co.vine.android;

import android.support.v4.app.Fragment;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.TimelineDetails;

/* loaded from: classes.dex */
public final class LifetimeSafeModelListener implements ModelEvents.ModelListener {
    private final Fragment mFragment;
    private final ModelEvents.ModelListener mListener;

    LifetimeSafeModelListener(Fragment fragment, ModelEvents.ModelListener listener) {
        this.mFragment = fragment;
        this.mListener = listener;
    }

    @Override // co.vine.android.model.ModelEvents.ModelListener
    public void onTagsAdded(TagModel tagModel, String query) {
        if (this.mFragment.isResumed()) {
            this.mListener.onTagsAdded(tagModel, query);
        }
    }

    @Override // co.vine.android.model.ModelEvents.ModelListener
    public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
        if (this.mFragment.isResumed()) {
            this.mListener.onTimelineUpdated(timelineModel, timelineDetails);
        }
    }
}
