package org.parceler;

import co.vine.android.api.response.ServerStatus;
import co.vine.android.api.response.ServerStatus$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$ServerStatus$$Parcelable$$0 implements Parcels.ParcelableFactory<ServerStatus> {
    private Parceler$$Parcels$ServerStatus$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public ServerStatus$$Parcelable buildParcelable(ServerStatus input) {
        return new ServerStatus$$Parcelable(input);
    }
}
