package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class zzh implements Parcelable.Creator<PolygonOptions> {
    static void zza(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, polygonOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, polygonOptions.getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel, 3, polygonOptions.zzzw(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, polygonOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, polygonOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 6, polygonOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, polygonOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, polygonOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, polygonOptions.isGeodesic());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzft, reason: merged with bridge method [inline-methods] */
    public PolygonOptions createFromParcel(Parcel parcel) {
        float fZzl = 0.0f;
        boolean zZzc = false;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        ArrayList arrayListZzc = null;
        ArrayList arrayList = new ArrayList();
        boolean zZzc2 = false;
        int iZzg = 0;
        int iZzg2 = 0;
        float fZzl2 = 0.0f;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat, LatLng.CREATOR);
                    break;
                case 3:
                    com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, arrayList, getClass().getClassLoader());
                    break;
                case 4:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 5:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 6:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 7:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 8:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new PolygonOptions(iZzg3, arrayListZzc, arrayList, fZzl2, iZzg2, iZzg, fZzl, zZzc2, zZzc);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzie, reason: merged with bridge method [inline-methods] */
    public PolygonOptions[] newArray(int i) {
        return new PolygonOptions[i];
    }
}
