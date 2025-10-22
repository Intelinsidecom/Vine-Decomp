package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class zzg implements Parcelable.Creator<AdRequestParcel> {
    static void zza(AdRequestParcel adRequestParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, adRequestParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, adRequestParcel.zztq);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, adRequestParcel.extras, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, adRequestParcel.zztr);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 5, adRequestParcel.zzts, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, adRequestParcel.zztt);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 7, adRequestParcel.zztu);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, adRequestParcel.zztv);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, adRequestParcel.zztw, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, (Parcelable) adRequestParcel.zztx, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, (Parcelable) adRequestParcel.zzty, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, adRequestParcel.zztz, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 13, adRequestParcel.zztA, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 14, adRequestParcel.zztB, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 15, adRequestParcel.zztC, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 17, adRequestParcel.zztE, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 16, adRequestParcel.zztD, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 18, adRequestParcel.zztF);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public AdRequestParcel createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        long jZzi = 0;
        Bundle bundleZzr = null;
        int iZzg2 = 0;
        ArrayList<String> arrayListZzD = null;
        boolean zZzc = false;
        int iZzg3 = 0;
        boolean zZzc2 = false;
        String strZzp = null;
        SearchAdRequestParcel searchAdRequestParcel = null;
        Location location = null;
        String strZzp2 = null;
        Bundle bundleZzr2 = null;
        Bundle bundleZzr3 = null;
        ArrayList<String> arrayListZzD2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        boolean zZzc3 = false;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 3:
                    bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 4:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 5:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 6:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 7:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 8:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 9:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 10:
                    searchAdRequestParcel = (SearchAdRequestParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, SearchAdRequestParcel.CREATOR);
                    break;
                case 11:
                    location = (Location) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Location.CREATOR);
                    break;
                case 12:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 13:
                    bundleZzr2 = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 14:
                    bundleZzr3 = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 15:
                    arrayListZzD2 = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 16:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 17:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 18:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new AdRequestParcel(iZzg, jZzi, bundleZzr, iZzg2, arrayListZzD, zZzc, iZzg3, zZzc2, strZzp, searchAdRequestParcel, location, strZzp2, bundleZzr2, bundleZzr3, arrayListZzD2, strZzp3, strZzp4, zZzc3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzl, reason: merged with bridge method [inline-methods] */
    public AdRequestParcel[] newArray(int i) {
        return new AdRequestParcel[i];
    }
}
