package org.parceler;

import co.vine.android.api.VineFeed;
import co.vine.android.api.VineFeed$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$VineFeed$$Parcelable$$0 implements Parcels.ParcelableFactory<VineFeed> {
    private Parceler$$Parcels$VineFeed$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public VineFeed$$Parcelable buildParcelable(VineFeed input) {
        return new VineFeed$$Parcelable(input);
    }
}
