package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zzb implements Parcelable.Creator<FACLData> {
    static void zza(FACLData fACLData, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLData.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) fACLData.zzWN, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, fACLData.zzWO, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLData.zzWP);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, fACLData.zzWQ, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzX, reason: merged with bridge method [inline-methods] */
    public FACLData createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        String strZzp = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        String strZzp2 = null;
        FACLConfig fACLConfig = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    fACLConfig = (FACLConfig) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, FACLConfig.CREATOR);
                    break;
                case 3:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 5:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new FACLData(iZzg, fACLConfig, strZzp2, zZzc, strZzp);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaU, reason: merged with bridge method [inline-methods] */
    public FACLData[] newArray(int i) {
        return new FACLData[i];
    }
}
