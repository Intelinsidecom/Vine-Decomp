package org.parceler;

import co.vine.android.api.response.VineHomeFeedSettingsResponse;
import co.vine.android.api.response.VineHomeFeedSettingsResponse$Data$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$Data$$Parcelable$$1 implements Parcels.ParcelableFactory<VineHomeFeedSettingsResponse.Data> {
    private Parceler$$Parcels$Data$$Parcelable$$1() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public VineHomeFeedSettingsResponse$Data$$Parcelable buildParcelable(VineHomeFeedSettingsResponse.Data input) {
        return new VineHomeFeedSettingsResponse$Data$$Parcelable(input);
    }
}
