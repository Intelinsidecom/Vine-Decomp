package org.parceler;

import co.vine.android.scribe.model.Item;
import co.vine.android.scribe.model.Item$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$Item$$Parcelable$$0 implements Parcels.ParcelableFactory<Item> {
    private Parceler$$Parcels$Item$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public Item$$Parcelable buildParcelable(Item input) {
        return new Item$$Parcelable(input);
    }
}
