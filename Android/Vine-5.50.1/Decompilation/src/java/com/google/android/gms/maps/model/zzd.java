package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzd implements Parcelable.Creator<LatLngBounds> {
    static void zza(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, latLngBounds.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) latLngBounds.southwest, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) latLngBounds.northeast, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfp, reason: merged with bridge method [inline-methods] */
    public LatLngBounds createFromParcel(Parcel parcel) {
        LatLng latLng;
        LatLng latLng2;
        int iZzg;
        LatLng latLng3 = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    LatLng latLng5 = latLng3;
                    latLng2 = latLng4;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    latLng = latLng5;
                    break;
                case 2:
                    LatLng latLng6 = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    iZzg = i;
                    latLng = latLng3;
                    latLng2 = latLng6;
                    break;
                case 3:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    latLng2 = latLng4;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    latLng = latLng3;
                    latLng2 = latLng4;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            latLng4 = latLng2;
            latLng3 = latLng;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new LatLngBounds(i, latLng4, latLng3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzia, reason: merged with bridge method [inline-methods] */
    public LatLngBounds[] newArray(int i) {
        return new LatLngBounds[i];
    }
}
