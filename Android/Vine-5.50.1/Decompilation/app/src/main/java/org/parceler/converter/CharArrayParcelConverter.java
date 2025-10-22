package org.parceler.converter;

import android.os.Parcel;
import org.parceler.ParcelConverter;

/* loaded from: classes.dex */
public class CharArrayParcelConverter implements ParcelConverter<char[]> {
    @Override // org.parceler.TypeRangeParcelConverter
    public void toParcel(char[] array, Parcel parcel) {
        if (array == null) {
            parcel.writeInt(-1);
        } else {
            parcel.writeInt(array.length);
            parcel.writeCharArray(array);
        }
    }

    @Override // org.parceler.TypeRangeParcelConverter
    public char[] fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size == -1) {
            return null;
        }
        char[] array = new char[size];
        parcel.readCharArray(array);
        return array;
    }
}
