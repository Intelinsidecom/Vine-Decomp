package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zza implements Parcelable.Creator<FACLConfig> {
    static void zza(FACLConfig fACLConfig, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLConfig.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, fACLConfig.zzWH);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, fACLConfig.zzWI, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLConfig.zzWJ);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, fACLConfig.zzWK);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, fACLConfig.zzWL);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, fACLConfig.zzWM);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzW, reason: merged with bridge method [inline-methods] */
    public FACLConfig createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        String strZzp = null;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        boolean zZzc4 = false;
        boolean zZzc5 = false;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    zZzc5 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 3:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 4:
                    zZzc4 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 5:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 6:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 7:
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
        return new FACLConfig(iZzg, zZzc5, strZzp, zZzc4, zZzc3, zZzc2, zZzc);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaT, reason: merged with bridge method [inline-methods] */
    public FACLConfig[] newArray(int i) {
        return new FACLConfig[i];
    }
}
