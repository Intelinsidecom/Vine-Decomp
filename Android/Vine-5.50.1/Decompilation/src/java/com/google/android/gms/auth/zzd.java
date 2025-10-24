package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class zzd implements Parcelable.Creator<TokenData> {
    static void zza(TokenData tokenData, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, tokenData.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, tokenData.getToken(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, tokenData.zzlW(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, tokenData.zzlX());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, tokenData.zzlY());
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 6, tokenData.zzlZ(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzC, reason: merged with bridge method [inline-methods] */
    public TokenData createFromParcel(Parcel parcel) {
        ArrayList<String> arrayListZzD = null;
        boolean zZzc = false;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        boolean zZzc2 = false;
        Long lZzj = null;
        String strZzp = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 3:
                    lZzj = com.google.android.gms.common.internal.safeparcel.zza.zzj(parcel, iZzat);
                    break;
                case 4:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 5:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 6:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new TokenData(iZzg, strZzp, lZzj, zZzc2, zZzc, arrayListZzD);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzax, reason: merged with bridge method [inline-methods] */
    public TokenData[] newArray(int i) {
        return new TokenData[i];
    }
}
