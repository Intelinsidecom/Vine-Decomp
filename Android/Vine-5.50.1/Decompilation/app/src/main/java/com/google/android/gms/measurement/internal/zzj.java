package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzj implements Parcelable.Creator<EventParcel> {
    static void zza(EventParcel eventParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, eventParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, eventParcel.name, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) eventParcel.zzaSL, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, eventParcel.zzaSM, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, eventParcel.zzaSN);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfE, reason: merged with bridge method [inline-methods] */
    public EventParcel createFromParcel(Parcel parcel) {
        String strZzp = null;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        long jZzi = 0;
        EventParams eventParams = null;
        String strZzp2 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    eventParams = (EventParams) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, EventParams.CREATOR);
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 5:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new EventParcel(iZzg, strZzp2, eventParams, strZzp, jZzi);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzix, reason: merged with bridge method [inline-methods] */
    public EventParcel[] newArray(int i) {
        return new EventParcel[i];
    }
}
