package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzo implements Parcelable.Creator<TileOverlayOptions> {
    static void zza(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, tileOverlayOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, tileOverlayOptions.zzzx(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, tileOverlayOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, tileOverlayOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, tileOverlayOptions.getFadeIn());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfA, reason: merged with bridge method [inline-methods] */
    public TileOverlayOptions createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        IBinder iBinderZzq = null;
        float fZzl = 0.0f;
        boolean zZzc2 = true;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    iBinderZzq = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 3:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 4:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new TileOverlayOptions(iZzg, iBinderZzq, zZzc, fZzl, zZzc2);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzil, reason: merged with bridge method [inline-methods] */
    public TileOverlayOptions[] newArray(int i) {
        return new TileOverlayOptions[i];
    }
}
