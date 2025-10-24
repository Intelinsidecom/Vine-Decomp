package org.parceler;

import co.vine.android.scribe.model.UserDetails;
import co.vine.android.scribe.model.UserDetails$$Parcelable;
import org.parceler.Parcels;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class Parceler$$Parcels$UserDetails$$Parcelable$$0 implements Parcels.ParcelableFactory<UserDetails> {
    private Parceler$$Parcels$UserDetails$$Parcelable$$0() {
    }

    @Override // org.parceler.Parcels.ParcelableFactory
    public UserDetails$$Parcelable buildParcelable(UserDetails input) {
        return new UserDetails$$Parcelable(input);
    }
}
