package com.google.android.gms.ads.internal.util.client;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzc implements Parcelable.Creator<VersionInfoParcel> {
    static void zza(VersionInfoParcel versionInfoParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, versionInfoParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, versionInfoParcel.afmaVersion, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, versionInfoParcel.zzLF);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, versionInfoParcel.zzLG);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, versionInfoParcel.zzLH);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzR, reason: merged with bridge method [inline-methods] */
    public VersionInfoParcel[] newArray(int i) {
        return new VersionInfoParcel[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzp, reason: merged with bridge method [inline-methods] */
    public VersionInfoParcel createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        String strZzp = null;
        int iZzg = 0;
        int iZzg2 = 0;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 4:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 5:
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
        return new VersionInfoParcel(iZzg3, strZzp, iZzg2, iZzg, zZzc);
    }
}
