package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zza implements Parcelable.Creator<ConverterWrapper> {
    static void zza(ConverterWrapper converterWrapper, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, converterWrapper.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) converterWrapper.zzqQ(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzax, reason: merged with bridge method [inline-methods] */
    public ConverterWrapper createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        StringToIntConverter stringToIntConverter = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    stringToIntConverter = (StringToIntConverter) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, StringToIntConverter.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new ConverterWrapper(iZzg, stringToIntConverter);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzcf, reason: merged with bridge method [inline-methods] */
    public ConverterWrapper[] newArray(int i) {
        return new ConverterWrapper[i];
    }
}
