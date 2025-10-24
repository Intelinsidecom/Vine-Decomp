package org.parceler;

import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.api.response.PagedActivityResponse$Data$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$Data$$Parcelable$$2 implements Parcels.ParcelableFactory<PagedActivityResponse.Data> {
    private Parceler$$Parcels$Data$$Parcelable$$2() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public PagedActivityResponse$Data$$Parcelable buildParcelable(PagedActivityResponse.Data input) {
        return new PagedActivityResponse$Data$$Parcelable(input);
    }
}
