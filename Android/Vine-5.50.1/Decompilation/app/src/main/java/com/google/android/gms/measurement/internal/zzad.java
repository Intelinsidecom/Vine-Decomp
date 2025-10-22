package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzad implements Parcelable.Creator<UserAttributeParcel> {
    static void zza(UserAttributeParcel userAttributeParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, userAttributeParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, userAttributeParcel.name, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, userAttributeParcel.zzaVg);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, userAttributeParcel.zzaVh, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, userAttributeParcel.zzaVi, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, userAttributeParcel.zzakS, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, userAttributeParcel.zzaSM, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfF, reason: merged with bridge method [inline-methods] */
    public UserAttributeParcel createFromParcel(Parcel parcel) {
        String strZzp = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        long jZzi = 0;
        String strZzp2 = null;
        Float fZzm = null;
        Long lZzj = null;
        String strZzp3 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 4:
                    lZzj = com.google.android.gms.common.internal.safeparcel.zza.zzj(parcel, iZzat);
                    break;
                case 5:
                    fZzm = com.google.android.gms.common.internal.safeparcel.zza.zzm(parcel, iZzat);
                    break;
                case 6:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 7:
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
        return new UserAttributeParcel(iZzg, strZzp3, jZzi, lZzj, fZzm, strZzp2, strZzp);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zziy, reason: merged with bridge method [inline-methods] */
    public UserAttributeParcel[] newArray(int i) {
        return new UserAttributeParcel[i];
    }
}
