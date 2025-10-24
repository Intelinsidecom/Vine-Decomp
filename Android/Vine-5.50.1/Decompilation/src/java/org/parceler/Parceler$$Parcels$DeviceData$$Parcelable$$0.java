package org.parceler;

import co.vine.android.scribe.model.DeviceData;
import co.vine.android.scribe.model.DeviceData$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$DeviceData$$Parcelable$$0 implements Parcels.ParcelableFactory<DeviceData> {
    private Parceler$$Parcels$DeviceData$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public DeviceData$$Parcelable buildParcelable(DeviceData input) {
        return new DeviceData$$Parcelable(input);
    }
}
