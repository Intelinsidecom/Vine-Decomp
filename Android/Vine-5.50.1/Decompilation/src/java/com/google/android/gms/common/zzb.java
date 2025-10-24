package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes2.dex */
public class zzb implements Parcelable.Creator<ConnectionResult> {
    static void zza(ConnectionResult connectionResult, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, connectionResult.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, connectionResult.getErrorCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) connectionResult.getResolution(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, connectionResult.getErrorMessage(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzag, reason: merged with bridge method [inline-methods] */
    public ConnectionResult createFromParcel(Parcel parcel) {
        String strZzp;
        PendingIntent pendingIntent;
        int iZzg;
        int iZzg2;
        String str = null;
        int i = 0;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        PendingIntent pendingIntent2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    String str2 = str;
                    pendingIntent = pendingIntent2;
                    iZzg = i;
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    strZzp = str2;
                    break;
                case 2:
                    iZzg2 = i2;
                    PendingIntent pendingIntent3 = pendingIntent2;
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    strZzp = str;
                    pendingIntent = pendingIntent3;
                    break;
                case 3:
                    iZzg = i;
                    iZzg2 = i2;
                    String str3 = str;
                    pendingIntent = (PendingIntent) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, PendingIntent.CREATOR);
                    strZzp = str3;
                    break;
                case 4:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    pendingIntent = pendingIntent2;
                    iZzg = i;
                    iZzg2 = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    strZzp = str;
                    pendingIntent = pendingIntent2;
                    iZzg = i;
                    iZzg2 = i2;
                    break;
            }
            i2 = iZzg2;
            i = iZzg;
            pendingIntent2 = pendingIntent;
            str = strZzp;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new ConnectionResult(i2, i, pendingIntent2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbt, reason: merged with bridge method [inline-methods] */
    public ConnectionResult[] newArray(int i) {
        return new ConnectionResult[i];
    }
}
