package org.parceler;

import co.vine.android.scribe.model.ClientEvents;
import co.vine.android.scribe.model.ClientEvents$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$ClientEvents$$Parcelable$$0 implements Parcels.ParcelableFactory<ClientEvents> {
    private Parceler$$Parcels$ClientEvents$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public ClientEvents$$Parcelable buildParcelable(ClientEvents input) {
        return new ClientEvents$$Parcelable(input);
    }
}
