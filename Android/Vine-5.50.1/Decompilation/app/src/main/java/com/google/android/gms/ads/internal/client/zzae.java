package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzae implements Parcelable.Creator<SearchAdRequestParcel> {
    static void zza(SearchAdRequestParcel searchAdRequestParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, searchAdRequestParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, searchAdRequestParcel.zzuI);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, searchAdRequestParcel.backgroundColor);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, searchAdRequestParcel.zzuJ);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, searchAdRequestParcel.zzuK);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 6, searchAdRequestParcel.zzuL);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 7, searchAdRequestParcel.zzuM);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 8, searchAdRequestParcel.zzuN);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 9, searchAdRequestParcel.zzuO);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, searchAdRequestParcel.zzuP, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 11, searchAdRequestParcel.zzuQ);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, searchAdRequestParcel.zzuR, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 13, searchAdRequestParcel.zzuS);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 14, searchAdRequestParcel.zzuT);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, searchAdRequestParcel.zzuU, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzd, reason: merged with bridge method [inline-methods] */
    public SearchAdRequestParcel createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        int iZzg4 = 0;
        int iZzg5 = 0;
        int iZzg6 = 0;
        int iZzg7 = 0;
        int iZzg8 = 0;
        int iZzg9 = 0;
        String strZzp = null;
        int iZzg10 = 0;
        String strZzp2 = null;
        int iZzg11 = 0;
        int iZzg12 = 0;
        String strZzp3 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 3:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 4:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 5:
                    iZzg5 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 6:
                    iZzg6 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 7:
                    iZzg7 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 8:
                    iZzg8 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 9:
                    iZzg9 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 10:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 11:
                    iZzg10 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 12:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 13:
                    iZzg11 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 14:
                    iZzg12 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 15:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new SearchAdRequestParcel(iZzg, iZzg2, iZzg3, iZzg4, iZzg5, iZzg6, iZzg7, iZzg8, iZzg9, strZzp, iZzg10, strZzp2, iZzg11, iZzg12, strZzp3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzo, reason: merged with bridge method [inline-methods] */
    public SearchAdRequestParcel[] newArray(int i) {
        return new SearchAdRequestParcel[i];
    }
}
