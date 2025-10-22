package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zze implements Parcelable.Creator<LatLng> {
    static void zza(LatLng latLng, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, latLng.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, latLng.latitude);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, latLng.longitude);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfq, reason: merged with bridge method [inline-methods] */
    public LatLng createFromParcel(Parcel parcel) {
        double dZzn = 0.0d;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        double dZzn2 = 0.0d;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    dZzn2 = com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, iZzat);
                    break;
                case 3:
                    dZzn = com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new LatLng(iZzg, dZzn2, dZzn);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzib, reason: merged with bridge method [inline-methods] */
    public LatLng[] newArray(int i) {
        return new LatLng[i];
    }
}
