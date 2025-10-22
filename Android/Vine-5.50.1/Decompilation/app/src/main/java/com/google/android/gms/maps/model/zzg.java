package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzg implements Parcelable.Creator<PointOfInterest> {
    static void zza(PointOfInterest pointOfInterest, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, pointOfInterest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) pointOfInterest.zzaQI, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, pointOfInterest.zzaQJ, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, pointOfInterest.name, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfs, reason: merged with bridge method [inline-methods] */
    public PointOfInterest createFromParcel(Parcel parcel) {
        String strZzp;
        String strZzp2;
        LatLng latLng;
        int iZzg;
        String str = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        String str2 = null;
        LatLng latLng2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    String str3 = str;
                    strZzp2 = str2;
                    latLng = latLng2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    strZzp = str3;
                    break;
                case 2:
                    iZzg = i;
                    String str4 = str2;
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    strZzp = str;
                    strZzp2 = str4;
                    break;
                case 3:
                    latLng = latLng2;
                    iZzg = i;
                    String str5 = str;
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    strZzp = str5;
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    strZzp2 = str2;
                    latLng = latLng2;
                    iZzg = i;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    strZzp = str;
                    strZzp2 = str2;
                    latLng = latLng2;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            latLng2 = latLng;
            str2 = strZzp2;
            str = strZzp;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new PointOfInterest(i, latLng2, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzid, reason: merged with bridge method [inline-methods] */
    public PointOfInterest[] newArray(int i) {
        return new PointOfInterest[i];
    }
}
