package co.vine.android.model.impl;

import co.vine.android.model.MutableVineModel;
import co.vine.android.model.VineModel;

/* loaded from: classes.dex */
public final class VineModelFactory {
    private static VineModelImpl mVineModel;

    public static VineModel getModelInstance() {
        return getMutableModelInstance();
    }

    public static MutableVineModel getMutableModelInstance() {
        if (mVineModel == null) {
            ModelEventsImpl modelEvents = new ModelEventsImpl();
            mVineModel = new VineModelImpl(modelEvents, new TagModelImpl(modelEvents), new TimelineModelImpl(modelEvents), new TimelineItemModelImpl(modelEvents));
        }
        return mVineModel;
    }
}
