package org.parceler;

import co.vine.android.scribe.model.ItemPosition;
import co.vine.android.scribe.model.ItemPosition$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$ItemPosition$$Parcelable$$0 implements Parcels.ParcelableFactory<ItemPosition> {
    private Parceler$$Parcels$ItemPosition$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public ItemPosition$$Parcelable buildParcelable(ItemPosition input) {
        return new ItemPosition$$Parcelable(input);
    }
}
