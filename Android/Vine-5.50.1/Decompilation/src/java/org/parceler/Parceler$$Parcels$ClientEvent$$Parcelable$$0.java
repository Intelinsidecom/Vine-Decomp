package org.parceler;

import co.vine.android.scribe.model.ClientEvent;
import co.vine.android.scribe.model.ClientEvent$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$ClientEvent$$Parcelable$$0 implements Parcels.ParcelableFactory<ClientEvent> {
    private Parceler$$Parcels$ClientEvent$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public ClientEvent$$Parcelable buildParcelable(ClientEvent input) {
        return new ClientEvent$$Parcelable(input);
    }
}
