package co.vine.android.model.impl;

import co.vine.android.model.ModelEvents;
import co.vine.android.model.MutableTagModel;
import co.vine.android.model.MutableTimelineItemModel;
import co.vine.android.model.MutableTimelineModel;
import co.vine.android.model.MutableVineModel;
import co.vine.android.model.TagModel;

/* loaded from: classes.dex */
final class VineModelImpl implements MutableVineModel {
    private final ModelEventsImpl mModelEvents;
    private final MutableTagModel mTagModel;
    private final MutableTimelineItemModel mTimelineItemModel;
    private final MutableTimelineModel mTimelineModel;

    VineModelImpl(ModelEventsImpl modelEvents, MutableTagModel tagModel, MutableTimelineModel timelineModel, MutableTimelineItemModel timelineItemModel) {
        this.mModelEvents = modelEvents;
        this.mTagModel = tagModel;
        this.mTimelineModel = timelineModel;
        this.mTimelineItemModel = timelineItemModel;
    }

    @Override // co.vine.android.model.VineModel
    public TagModel getTagModel() {
        return this.mTagModel;
    }

    @Override // co.vine.android.model.MutableVineModel
    public MutableTagModel getMutableTagModel() {
        return this.mTagModel;
    }

    @Override // co.vine.android.model.VineModel
    public ModelEvents getModelEvents() {
        return this.mModelEvents;
    }

    @Override // co.vine.android.model.VineModel
    public MutableTimelineModel getTimelineModel() {
        return this.mTimelineModel;
    }

    @Override // co.vine.android.model.MutableVineModel
    public MutableTimelineModel getMutableTimelineModel() {
        return this.mTimelineModel;
    }

    @Override // co.vine.android.model.MutableVineModel
    public MutableTimelineItemModel getMutableTimelineItemModel() {
        return this.mTimelineItemModel;
    }

    @Override // co.vine.android.model.VineModel
    public MutableTimelineItemModel getTimelineItemModel() {
        return this.mTimelineItemModel;
    }
}
