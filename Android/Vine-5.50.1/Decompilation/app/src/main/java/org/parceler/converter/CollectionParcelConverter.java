package org.parceler.converter;

import android.os.Parcel;
import java.util.Collection;
import org.parceler.TypeRangeParcelConverter;

/* loaded from: classes.dex */
public abstract class CollectionParcelConverter<T, C extends Collection<T>> implements TypeRangeParcelConverter<Collection<T>, C> {
    public abstract C createCollection();

    public abstract T itemFromParcel(Parcel parcel);

    public abstract void itemToParcel(T t, Parcel parcel);

    @Override // org.parceler.TypeRangeParcelConverter
    public void toParcel(Collection<T> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(input.size());
        for (T item : input) {
            itemToParcel(item, parcel);
        }
    }

    @Override // org.parceler.TypeRangeParcelConverter
    public C fromParcel(Parcel parcel) {
        int i = parcel.readInt();
        if (i == -1) {
            return null;
        }
        C c = (C) createCollection();
        for (int i2 = 0; i2 < i; i2++) {
            c.add(itemFromParcel(parcel));
        }
        return c;
    }
}
