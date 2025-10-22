package org.parceler.converter;

import android.os.Parcel;
import java.util.Map;
import org.parceler.TypeRangeParcelConverter;

/* loaded from: classes.dex */
public abstract class MapParcelConverter<K, V, M extends Map<K, V>> implements TypeRangeParcelConverter<Map<K, V>, M> {
    public abstract M createMap();

    public abstract K mapKeyFromParcel(Parcel parcel);

    public abstract void mapKeyToParcel(K k, Parcel parcel);

    public abstract V mapValueFromParcel(Parcel parcel);

    public abstract void mapValueToParcel(V v, Parcel parcel);

    @Override // org.parceler.TypeRangeParcelConverter
    public void toParcel(Map<K, V> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(input.size());
        for (Map.Entry<K, V> entry : input.entrySet()) {
            mapKeyToParcel(entry.getKey(), parcel);
            mapValueToParcel(entry.getValue(), parcel);
        }
    }

    @Override // org.parceler.TypeRangeParcelConverter
    public M fromParcel(Parcel parcel) {
        int i = parcel.readInt();
        if (i == -1) {
            return null;
        }
        M m = (M) createMap();
        for (int i2 = 0; i2 < i; i2++) {
            m.put(mapKeyFromParcel(parcel), mapValueFromParcel(parcel));
        }
        return m;
    }
}
