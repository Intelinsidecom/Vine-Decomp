package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* loaded from: classes2.dex */
public class zza implements Parcelable.Creator<BitmapTeleporter> {
    static void zza(BitmapTeleporter bitmapTeleporter, Parcel parcel, int i) {
        int iZzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, bitmapTeleporter.mVersionCode);
        zzb.zza(parcel, 2, (Parcelable) bitmapTeleporter.zzHf, i, false);
        zzb.zzc(parcel, 3, bitmapTeleporter.zzZU);
        zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzaj, reason: merged with bridge method [inline-methods] */
    public BitmapTeleporter createFromParcel(Parcel parcel) {
        int iZzg;
        ParcelFileDescriptor parcelFileDescriptor;
        int iZzg2;
        int i = 0;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        ParcelFileDescriptor parcelFileDescriptor2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    int i3 = i;
                    parcelFileDescriptor = parcelFileDescriptor2;
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    iZzg = i3;
                    break;
                case 2:
                    ParcelFileDescriptor parcelFileDescriptor3 = (ParcelFileDescriptor) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, ParcelFileDescriptor.CREATOR);
                    iZzg2 = i2;
                    iZzg = i;
                    parcelFileDescriptor = parcelFileDescriptor3;
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    parcelFileDescriptor = parcelFileDescriptor2;
                    iZzg2 = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    iZzg = i;
                    parcelFileDescriptor = parcelFileDescriptor2;
                    iZzg2 = i2;
                    break;
            }
            i2 = iZzg2;
            parcelFileDescriptor2 = parcelFileDescriptor;
            i = iZzg;
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new BitmapTeleporter(i2, parcelFileDescriptor2, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzbF, reason: merged with bridge method [inline-methods] */
    public BitmapTeleporter[] newArray(int i) {
        return new BitmapTeleporter[i];
    }
}
