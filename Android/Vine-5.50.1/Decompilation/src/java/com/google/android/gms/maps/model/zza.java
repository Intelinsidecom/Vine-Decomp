package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zza implements Parcelable.Creator<CameraPosition> {
    static void zza(CameraPosition cameraPosition, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, cameraPosition.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) cameraPosition.target, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, cameraPosition.zoom);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, cameraPosition.tilt);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, cameraPosition.bearing);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfm, reason: merged with bridge method [inline-methods] */
    public CameraPosition createFromParcel(Parcel parcel) {
        float fZzl = 0.0f;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        LatLng latLng = null;
        float fZzl2 = 0.0f;
        float fZzl3 = 0.0f;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    break;
                case 3:
                    fZzl3 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 4:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 5:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new CameraPosition(iZzg, latLng, fZzl3, fZzl2, fZzl);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzhX, reason: merged with bridge method [inline-methods] */
    public CameraPosition[] newArray(int i) {
        return new CameraPosition[i];
    }
}
