package org.parceler;

import android.os.Parcel;

/* loaded from: classes.dex */
public interface TypeRangeParcelConverter<L, U extends L> {
    U fromParcel(Parcel parcel);

    void toParcel(L l, Parcel parcel);
}
