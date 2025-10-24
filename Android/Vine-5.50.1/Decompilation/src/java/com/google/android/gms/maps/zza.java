package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.maps.model.CameraPosition;

/* loaded from: classes.dex */
public class zza implements Parcelable.Creator<GoogleMapOptions> {
    static void zza(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, googleMapOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, googleMapOptions.zzyV());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, googleMapOptions.zzyW());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, googleMapOptions.getMapType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, (Parcelable) googleMapOptions.getCamera(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, googleMapOptions.zzyX());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, googleMapOptions.zzyY());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, googleMapOptions.zzyZ());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, googleMapOptions.zzza());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, googleMapOptions.zzzb());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, googleMapOptions.zzzc());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, googleMapOptions.zzzd());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 14, googleMapOptions.zzze());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, googleMapOptions.zzzf());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfk, reason: merged with bridge method [inline-methods] */
    public GoogleMapOptions createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        byte bZze = -1;
        byte bZze2 = -1;
        int iZzg2 = 0;
        CameraPosition cameraPosition = null;
        byte bZze3 = -1;
        byte bZze4 = -1;
        byte bZze5 = -1;
        byte bZze6 = -1;
        byte bZze7 = -1;
        byte bZze8 = -1;
        byte bZze9 = -1;
        byte bZze10 = -1;
        byte bZze11 = -1;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    bZze = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 3:
                    bZze2 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 4:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, CameraPosition.CREATOR);
                    break;
                case 6:
                    bZze3 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 7:
                    bZze4 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 8:
                    bZze5 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 9:
                    bZze6 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 10:
                    bZze7 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 11:
                    bZze8 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 12:
                    bZze9 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 13:
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
                case 14:
                    bZze10 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
                case 15:
                    bZze11 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new GoogleMapOptions(iZzg, bZze, bZze2, iZzg2, cameraPosition, bZze3, bZze4, bZze5, bZze6, bZze7, bZze8, bZze9, bZze10, bZze11);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzhV, reason: merged with bridge method [inline-methods] */
    public GoogleMapOptions[] newArray(int i) {
        return new GoogleMapOptions[i];
    }
}
