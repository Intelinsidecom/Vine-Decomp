package com.google.android.gms.ads.internal.overlay;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.ads.internal.InterstitialAdParameterParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.zza;

/* loaded from: classes.dex */
public class zzf implements Parcelable.Creator<AdOverlayInfoParcel> {
    static void zza(AdOverlayInfoParcel adOverlayInfoParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, adOverlayInfoParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, (Parcelable) adOverlayInfoParcel.zzDz, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, adOverlayInfoParcel.zzfh(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, adOverlayInfoParcel.zzfi(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, adOverlayInfoParcel.zzfj(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, adOverlayInfoParcel.zzfk(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, adOverlayInfoParcel.zzDE, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, adOverlayInfoParcel.zzDF);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, adOverlayInfoParcel.zzDG, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, adOverlayInfoParcel.zzfm(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 11, adOverlayInfoParcel.orientation);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 12, adOverlayInfoParcel.zzDI);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 13, adOverlayInfoParcel.url, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 14, (Parcelable) adOverlayInfoParcel.zzqR, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, adOverlayInfoParcel.zzfl(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 17, (Parcelable) adOverlayInfoParcel.zzDL, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 16, adOverlayInfoParcel.zzDK, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzg, reason: merged with bridge method [inline-methods] */
    public AdOverlayInfoParcel createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        AdLauncherIntentInfoParcel adLauncherIntentInfoParcel = null;
        IBinder iBinderZzq = null;
        IBinder iBinderZzq2 = null;
        IBinder iBinderZzq3 = null;
        IBinder iBinderZzq4 = null;
        String strZzp = null;
        boolean zZzc = false;
        String strZzp2 = null;
        IBinder iBinderZzq5 = null;
        int iZzg2 = 0;
        int iZzg3 = 0;
        String strZzp3 = null;
        VersionInfoParcel versionInfoParcel = null;
        IBinder iBinderZzq6 = null;
        String strZzp4 = null;
        InterstitialAdParameterParcel interstitialAdParameterParcel = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    adLauncherIntentInfoParcel = (AdLauncherIntentInfoParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, AdLauncherIntentInfoParcel.CREATOR);
                    break;
                case 3:
                    iBinderZzq = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 4:
                    iBinderZzq2 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 5:
                    iBinderZzq3 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 6:
                    iBinderZzq4 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 7:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 8:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 9:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 10:
                    iBinderZzq5 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 11:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 12:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 13:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 14:
                    versionInfoParcel = (VersionInfoParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, VersionInfoParcel.CREATOR);
                    break;
                case 15:
                    iBinderZzq6 = com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, iZzat);
                    break;
                case 16:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 17:
                    interstitialAdParameterParcel = (InterstitialAdParameterParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, InterstitialAdParameterParcel.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new AdOverlayInfoParcel(iZzg, adLauncherIntentInfoParcel, iBinderZzq, iBinderZzq2, iBinderZzq3, iBinderZzq4, strZzp, zZzc, strZzp2, iBinderZzq5, iZzg2, iZzg3, strZzp3, versionInfoParcel, iBinderZzq6, strZzp4, interstitialAdParameterParcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzz, reason: merged with bridge method [inline-methods] */
    public AdOverlayInfoParcel[] newArray(int i) {
        return new AdOverlayInfoParcel[i];
    }
}
