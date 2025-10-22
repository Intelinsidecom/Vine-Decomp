package org.parceler;

import co.vine.android.scribe.model.GPSData;
import co.vine.android.scribe.model.GPSData$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$GPSData$$Parcelable$$0 implements Parcels.ParcelableFactory<GPSData> {
    private Parceler$$Parcels$GPSData$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public GPSData$$Parcelable buildParcelable(GPSData input) {
        return new GPSData$$Parcelable(input);
    }
}
