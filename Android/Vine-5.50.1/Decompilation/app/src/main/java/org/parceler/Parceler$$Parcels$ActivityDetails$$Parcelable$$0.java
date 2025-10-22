package org.parceler;

import co.vine.android.scribe.model.ActivityDetails;
import co.vine.android.scribe.model.ActivityDetails$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$ActivityDetails$$Parcelable$$0 implements Parcels.ParcelableFactory<ActivityDetails> {
    private Parceler$$Parcels$ActivityDetails$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public ActivityDetails$$Parcelable buildParcelable(ActivityDetails input) {
        return new ActivityDetails$$Parcelable(input);
    }
}
