package com.google.android.gms.ads.internal.reward.client;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* loaded from: classes.dex */
public class zzh implements Parcelable.Creator<RewardedVideoAdRequestParcel> {
    static void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel, Parcel parcel, int i) {
        int iZzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, rewardedVideoAdRequestParcel.versionCode);
        zzb.zza(parcel, 2, (Parcelable) rewardedVideoAdRequestParcel.zzGq, i, false);
        zzb.zza(parcel, 3, rewardedVideoAdRequestParcel.zzqP, false);
        zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzM, reason: merged with bridge method [inline-methods] */
    public RewardedVideoAdRequestParcel[] newArray(int i) {
        return new RewardedVideoAdRequestParcel[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzn, reason: merged with bridge method [inline-methods] */
    public RewardedVideoAdRequestParcel createFromParcel(Parcel parcel) {
        String strZzp;
        AdRequestParcel adRequestParcel;
        int iZzg;
        String str = null;
        int iZzau = zza.zzau(parcel);
        int i = 0;
        AdRequestParcel adRequestParcel2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = zza.zzat(parcel);
            switch (zza.zzcc(iZzat)) {
                case 1:
                    String str2 = str;
                    adRequestParcel = adRequestParcel2;
                    iZzg = zza.zzg(parcel, iZzat);
                    strZzp = str2;
                    break;
                case 2:
                    AdRequestParcel adRequestParcel3 = (AdRequestParcel) zza.zza(parcel, iZzat, AdRequestParcel.CREATOR);
                    iZzg = i;
                    strZzp = str;
                    adRequestParcel = adRequestParcel3;
                    break;
                case 3:
                    strZzp = zza.zzp(parcel, iZzat);
                    adRequestParcel = adRequestParcel2;
                    iZzg = i;
                    break;
                default:
                    zza.zzb(parcel, iZzat);
                    strZzp = str;
                    adRequestParcel = adRequestParcel2;
                    iZzg = i;
                    break;
            }
            i = iZzg;
            adRequestParcel2 = adRequestParcel;
            str = strZzp;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new RewardedVideoAdRequestParcel(i, adRequestParcel2, str);
    }
}
