package org.parceler.converter;

import android.os.Parcel;
import org.parceler.ParcelConverter;

/* loaded from: classes.dex */
public class BooleanArrayParcelConverter implements ParcelConverter<boolean[]> {
    @Override // org.parceler.TypeRangeParcelConverter
    public void toParcel(boolean[] array, Parcel parcel) {
        if (array == null) {
            parcel.writeInt(-1);
        } else {
            parcel.writeInt(array.length);
            parcel.writeBooleanArray(array);
        }
    }

    @Override // org.parceler.TypeRangeParcelConverter
    public boolean[] fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size == -1) {
            return null;
        }
        boolean[] array = new boolean[size];
        parcel.readBooleanArray(array);
        return array;
    }
}
