package org.parceler;

import co.vine.android.scribe.model.EventDetails;
import co.vine.android.scribe.model.EventDetails$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$EventDetails$$Parcelable$$0 implements Parcels.ParcelableFactory<EventDetails> {
    private Parceler$$Parcels$EventDetails$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public EventDetails$$Parcelable buildParcelable(EventDetails input) {
        return new EventDetails$$Parcelable(input);
    }
}
