package co.vine.android.model.impl;

import co.vine.android.model.MutableTagModel;
import co.vine.android.model.VineTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
final class TagModelImpl implements MutableTagModel {
    private final ModelEventsImpl mModelEvents;
    private final ConcurrentHashMap<String, ArrayList<VineTag>> mTags = new ConcurrentHashMap<>();

    TagModelImpl(ModelEventsImpl modelEvents) {
        this.mModelEvents = modelEvents;
    }

    @Override // co.vine.android.model.MutableTagModel
    public void addTagsForQuery(String query, Collection<VineTag> tags) {
        if (tags != null && query != null) {
            boolean modelChanged = false;
            if (!this.mTags.containsKey(query)) {
                modelChanged = true;
                this.mTags.put(query, new ArrayList<>());
            }
            ArrayList<VineTag> modelTags = this.mTags.get(query);
            for (VineTag tag : tags) {
                if (!modelTags.contains(tag)) {
                    modelTags.add(tag);
                    modelChanged = true;
                }
            }
            if (modelChanged) {
                this.mModelEvents.onTagsChanged(this, query);
            }
        }
    }

    @Override // co.vine.android.model.TagModel
    public List<VineTag> getTagsForQuery(String query) {
        if (query == null) {
            return null;
        }
        return this.mTags.get(query);
    }
}
