package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class zzf implements Parcelable.Creator<AdRequestInfoParcel> {
    static void zza(AdRequestInfoParcel adRequestInfoParcel, Parcel parcel, int i) {
        int iZzav = com.google.android.gms.common.internal.safeparcel.zzb.zzav(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, adRequestInfoParcel.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, adRequestInfoParcel.zzGp, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, (Parcelable) adRequestInfoParcel.zzGq, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, (Parcelable) adRequestInfoParcel.zzqV, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, adRequestInfoParcel.zzqP, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, (Parcelable) adRequestInfoParcel.applicationInfo, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, (Parcelable) adRequestInfoParcel.zzGr, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, adRequestInfoParcel.zzGs, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, adRequestInfoParcel.zzGt, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, adRequestInfoParcel.zzGu, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, (Parcelable) adRequestInfoParcel.zzqR, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 12, adRequestInfoParcel.zzGv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 13, adRequestInfoParcel.zzGw);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 14, adRequestInfoParcel.zzrl, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 15, adRequestInfoParcel.zzGx, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 17, (Parcelable) adRequestInfoParcel.zzGz, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 16, adRequestInfoParcel.zzGy);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 19, adRequestInfoParcel.zzGB);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 18, adRequestInfoParcel.zzGA);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 21, adRequestInfoParcel.zzGD, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 20, adRequestInfoParcel.zzGC);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 25, adRequestInfoParcel.zzGE);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 27, adRequestInfoParcel.zzGG, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 26, adRequestInfoParcel.zzGF, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 29, (Parcelable) adRequestInfoParcel.zzrj, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 28, adRequestInfoParcel.zzqO, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 31, adRequestInfoParcel.zzGI);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 30, adRequestInfoParcel.zzGH, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 32, (Parcelable) adRequestInfoParcel.zzGJ, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 33, adRequestInfoParcel.zzGK, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzI(parcel, iZzav);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzG, reason: merged with bridge method [inline-methods] */
    public AdRequestInfoParcel[] newArray(int i) {
        return new AdRequestInfoParcel[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: zzi, reason: merged with bridge method [inline-methods] */
    public AdRequestInfoParcel createFromParcel(Parcel parcel) {
        int iZzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int iZzg = 0;
        Bundle bundleZzr = null;
        AdRequestParcel adRequestParcel = null;
        AdSizeParcel adSizeParcel = null;
        String strZzp = null;
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        String strZzp2 = null;
        String strZzp3 = null;
        String strZzp4 = null;
        VersionInfoParcel versionInfoParcel = null;
        Bundle bundleZzr2 = null;
        int iZzg2 = 0;
        ArrayList<String> arrayListZzD = null;
        Bundle bundleZzr3 = null;
        boolean zZzc = false;
        Messenger messenger = null;
        int iZzg3 = 0;
        int iZzg4 = 0;
        float fZzl = 0.0f;
        String strZzp5 = null;
        long jZzi = 0;
        String strZzp6 = null;
        ArrayList<String> arrayListZzD2 = null;
        String strZzp7 = null;
        NativeAdOptionsParcel nativeAdOptionsParcel = null;
        ArrayList<String> arrayListZzD3 = null;
        long jZzi2 = 0;
        CapabilityParcel capabilityParcel = null;
        String strZzp8 = null;
        while (parcel.dataPosition() < iZzau) {
            int iZzat = com.google.android.gms.common.internal.safeparcel.zza.zzat(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzcc(iZzat)) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 2:
                    bundleZzr = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 3:
                    adRequestParcel = (AdRequestParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, AdRequestParcel.CREATOR);
                    break;
                case 4:
                    adSizeParcel = (AdSizeParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, AdSizeParcel.CREATOR);
                    break;
                case 5:
                    strZzp = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 6:
                    applicationInfo = (ApplicationInfo) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, ApplicationInfo.CREATOR);
                    break;
                case 7:
                    packageInfo = (PackageInfo) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, PackageInfo.CREATOR);
                    break;
                case 8:
                    strZzp2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 9:
                    strZzp3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 10:
                    strZzp4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 11:
                    versionInfoParcel = (VersionInfoParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, VersionInfoParcel.CREATOR);
                    break;
                case 12:
                    bundleZzr2 = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 13:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 14:
                    arrayListZzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 15:
                    bundleZzr3 = com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, iZzat);
                    break;
                case 16:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, iZzat);
                    break;
                case 17:
                    messenger = (Messenger) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, Messenger.CREATOR);
                    break;
                case 18:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 19:
                    iZzg4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, iZzat);
                    break;
                case 20:
                    fZzl = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, iZzat);
                    break;
                case 21:
                    strZzp5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 22:
                case 23:
                case 24:
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, iZzat);
                    break;
                case 25:
                    jZzi = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 26:
                    strZzp6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 27:
                    arrayListZzD2 = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 28:
                    strZzp7 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
                case 29:
                    nativeAdOptionsParcel = (NativeAdOptionsParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, NativeAdOptionsParcel.CREATOR);
                    break;
                case 30:
                    arrayListZzD3 = com.google.android.gms.common.internal.safeparcel.zza.zzD(parcel, iZzat);
                    break;
                case 31:
                    jZzi2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, iZzat);
                    break;
                case 32:
                    capabilityParcel = (CapabilityParcel) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, iZzat, CapabilityParcel.CREATOR);
                    break;
                case 33:
                    strZzp8 = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, iZzat);
                    break;
            }
        }
        if (parcel.dataPosition() != iZzau) {
            throw new zza.C0034zza("Overread allowed size end=" + iZzau, parcel);
        }
        return new AdRequestInfoParcel(iZzg, bundleZzr, adRequestParcel, adSizeParcel, strZzp, applicationInfo, packageInfo, strZzp2, strZzp3, strZzp4, versionInfoParcel, bundleZzr2, iZzg2, arrayListZzD, bundleZzr3, zZzc, messenger, iZzg3, iZzg4, fZzl, strZzp5, jZzi, strZzp6, arrayListZzD2, strZzp7, nativeAdOptionsParcel, arrayListZzD3, jZzi2, capabilityParcel, strZzp8);
    }
}
