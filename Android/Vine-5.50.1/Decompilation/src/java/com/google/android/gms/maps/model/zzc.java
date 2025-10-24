package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzc implements Parcelable.Creator<GroundOverlayOptions> {
    static void zza(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, groundOverlayOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, groundOverlayOptions.zzzu(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) groundOverlayOptions.getLocation(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, groundOverlayOptions.getWidth());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, groundOverlayOptions.getHeight());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable) groundOverlayOptions.getBounds(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, groundOverlayOptions.getBearing());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, groundOverlayOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, groundOverlayOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, groundOverlayOptions.getTransparency());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, groundOverlayOptions.getAnchorU());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, groundOverlayOptions.getAnchorV());
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzfo, reason: merged with bridge method [inline-methods] */
    public GroundOverlayOptions createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        IBinder iBinderZzq = null;
        LatLng latLng = null;
        float fZzl = 0.0f;
        float fZzl2 = 0.0f;
        LatLngBounds latLngBounds = null;
        float fZzl3 = 0.0f;
        float fZzl4 = 0.0f;
        boolean zZzc = false;
        float fZzl5 = 0.0f;
        float fZzl6 = 0.0f;
        float fZzl7 = 0.0f;
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
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLng.CREATOR);
                    break;
                case 4:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 5:
                    fZzl2 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, LatLngBounds.CREATOR);
                    break;
                case 7:
                    fZzl3 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 8:
                    fZzl4 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 9:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 10:
                    fZzl5 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 11:
                    fZzl6 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 12:
                    fZzl7 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new GroundOverlayOptions(iZzg, iBinderZzq, latLng, fZzl, fZzl2, latLngBounds, fZzl3, fZzl4, zZzc, fZzl5, fZzl6, fZzl7);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzhZ, reason: merged with bridge method [inline-methods] */
    public GroundOverlayOptions[] newArray(int i) {
        return new GroundOverlayOptions[i];
    }
}
