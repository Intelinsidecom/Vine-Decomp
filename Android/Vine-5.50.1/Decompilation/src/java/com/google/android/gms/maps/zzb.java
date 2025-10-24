package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/* loaded from: classes.dex */
public class zzb implements Parcelable.Creator<StreetViewPanoramaOptions> {
    static void zza(StreetViewPanoramaOptions streetViewPanoramaOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, streetViewPanoramaOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) streetViewPanoramaOptions.getStreetViewPanoramaCamera(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, streetViewPanoramaOptions.getPanoramaId(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) streetViewPanoramaOptions.getPosition(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, streetViewPanoramaOptions.getRadius(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, streetViewPanoramaOptions.zzzl());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, streetViewPanoramaOptions.zzza());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, streetViewPanoramaOptions.zzzm());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, streetViewPanoramaOptions.zzzn());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, streetViewPanoramaOptions.zzyW());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfl, reason: merged with bridge method [inline-methods] */
    public StreetViewPanoramaOptions createFromParcel(Parcel parcel) {
        Integer numZzh = null;
        byte bZze = 0;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        byte bZze2 = 0;
        byte bZze3 = 0;
        byte bZze4 = 0;
        byte bZze5 = 0;
        LatLng latLng = null;
        String strZzp = null;
        StreetViewPanoramaCamera streetViewPanoramaCamera = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    streetViewPanoramaCamera = (StreetViewPanoramaCamera) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, StreetViewPanoramaCamera.CREATOR);
                    break;
                case 3:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    break;
                case 5:
                    numZzh = com.google.android.gms.common.internal.safeparcel.zza.zzh(parcel, iZzat);
                    break;
                case 6:
                    bZze5 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 7:
                    bZze4 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 8:
                    bZze3 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 9:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 10:
                    bZze = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new StreetViewPanoramaOptions(iZzg, streetViewPanoramaCamera, strZzp, latLng, numZzh, bZze5, bZze4, bZze3, bZze2, bZze);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzhW, reason: merged with bridge method [inline-methods] */
    public StreetViewPanoramaOptions[] newArray(int i) {
        return new StreetViewPanoramaOptions[i];
    }
}
