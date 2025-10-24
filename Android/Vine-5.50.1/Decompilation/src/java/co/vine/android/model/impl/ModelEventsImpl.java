package co.vine.android.model.impl;

import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineModel;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class ModelEventsImpl implements ModelEvents {
    private final HashSet<ModelEvents.ModelListener> mListeners = new HashSet<>();

    ModelEventsImpl() {
    }

    @Override // co.vine.android.model.ModelEvents
    public void addListener(ModelEvents.ModelListener listener) {
        this.mListeners.add(listener);
    }

    @Override // co.vine.android.model.ModelEvents
    public void removeListener(ModelEvents.ModelListener listener) {
        this.mListeners.remove(listener);
    }

    void onTagsChanged(TagModel tagModel, String query) {
        Iterator<ModelEvents.ModelListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            ModelEvents.ModelListener listener = it.next();
            listener.onTagsAdded(tagModel, query);
        }
    }

    void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
        Iterator<ModelEvents.ModelListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            ModelEvents.ModelListener listener = it.next();
            listener.onTimelineUpdated(timelineModel, timelineDetails);
        }
    }
}
