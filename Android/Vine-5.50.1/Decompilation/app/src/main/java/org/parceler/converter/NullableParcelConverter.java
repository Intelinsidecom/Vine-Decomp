package org.parceler.converter;

import android.os.Parcel;
import org.parceler.ParcelConverter;

/* loaded from: classes.dex */
public abstract class NullableParcelConverter<T> implements ParcelConverter<T> {
    public abstract T nullSafeFromParcel(Parcel parcel);

    public abstract void nullSafeToParcel(T t, Parcel parcel);

    @Override // org.parceler.TypeRangeParcelConverter
    public void toParcel(T input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        } else {
            parcel.writeInt(1);
            nullSafeToParcel(input, parcel);
        }
    }

    @Override // org.parceler.TypeRangeParcelConverter
    public T fromParcel(Parcel parcel) {
        if (parcel.readInt() == -1) {
            return null;
        }
        return nullSafeFromParcel(parcel);
    }
}
