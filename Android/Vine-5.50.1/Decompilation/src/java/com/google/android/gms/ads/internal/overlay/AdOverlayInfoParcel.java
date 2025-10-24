package com.google.android.gms.ads.internal.overlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.ads.internal.InterstitialAdParameterParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzdh;
import com.google.android.gms.internal.zzdn;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjn;

@zzha
/* loaded from: classes.dex */
public final class AdOverlayInfoParcel implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    public final int orientation;
    public final String url;
    public final int versionCode;
    public final com.google.android.gms.ads.internal.client.zza zzDA;
    public final zzg zzDB;
    public final zzjn zzDC;
    public final zzdh zzDD;
    public final String zzDE;
    public final boolean zzDF;
    public final String zzDG;
    public final zzn zzDH;
    public final int zzDI;
    public final zzdn zzDJ;
    public final String zzDK;
    public final InterstitialAdParameterParcel zzDL;
    public final AdLauncherIntentInfoParcel zzDz;
    public final VersionInfoParcel zzqR;

    AdOverlayInfoParcel(int versionCode, AdLauncherIntentInfoParcel adLauncherIntentInfo, IBinder wrappedAdClickListener, IBinder wrappedAdOverlayListener, IBinder wrappedAdWebView, IBinder wrappedAppEventGmsgListener, String baseUrl, boolean customClose, String html, IBinder wrappedLeaveApplicationListener, int orientation, int overlayType, String url, VersionInfoParcel versionInfo, IBinder wrappedInAppPurchaseGmsgListener, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = versionCode;
        this.zzDz = adLauncherIntentInfo;
        this.zzDA = (com.google.android.gms.ads.internal.client.zza) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedAdClickListener));
        this.zzDB = (zzg) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedAdOverlayListener));
        this.zzDC = (zzjn) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedAdWebView));
        this.zzDD = (zzdh) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedAppEventGmsgListener));
        this.zzDE = baseUrl;
        this.zzDF = customClose;
        this.zzDG = html;
        this.zzDH = (zzn) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedLeaveApplicationListener));
        this.orientation = orientation;
        this.zzDI = overlayType;
        this.url = url;
        this.zzqR = versionInfo;
        this.zzDJ = (zzdn) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedInAppPurchaseGmsgListener));
        this.zzDK = debugMessage;
        this.zzDL = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(com.google.android.gms.ads.internal.client.zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zzjn adWebView, int orientation, VersionInfoParcel versionInfo, String debugMessage, InterstitialAdParameterParcel interstitialAdParameter) {
        this.versionCode = 4;
        this.zzDz = null;
        this.zzDA = adClickListener;
        this.zzDB = adOverlayListener;
        this.zzDC = adWebView;
        this.zzDD = null;
        this.zzDE = null;
        this.zzDF = false;
        this.zzDG = null;
        this.zzDH = leaveApplicationListener;
        this.orientation = orientation;
        this.zzDI = 1;
        this.url = null;
        this.zzqR = versionInfo;
        this.zzDJ = null;
        this.zzDK = debugMessage;
        this.zzDL = interstitialAdParameter;
    }

    public AdOverlayInfoParcel(com.google.android.gms.ads.internal.client.zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, zzjn adWebView, boolean customClose, int orientation, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzDz = null;
        this.zzDA = adClickListener;
        this.zzDB = adOverlayListener;
        this.zzDC = adWebView;
        this.zzDD = null;
        this.zzDE = null;
        this.zzDF = customClose;
        this.zzDG = null;
        this.zzDH = leaveApplicationListener;
        this.orientation = orientation;
        this.zzDI = 2;
        this.url = null;
        this.zzqR = versionInfo;
        this.zzDJ = null;
        this.zzDK = null;
        this.zzDL = null;
    }

    public AdOverlayInfoParcel(com.google.android.gms.ads.internal.client.zza adClickListener, zzg adOverlayListener, zzdh appEventGmsgListener, zzn leaveApplicationListener, zzjn adWebView, boolean customClose, int orientation, String url, VersionInfoParcel versionInfo, zzdn inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzDz = null;
        this.zzDA = adClickListener;
        this.zzDB = adOverlayListener;
        this.zzDC = adWebView;
        this.zzDD = appEventGmsgListener;
        this.zzDE = null;
        this.zzDF = customClose;
        this.zzDG = null;
        this.zzDH = leaveApplicationListener;
        this.orientation = orientation;
        this.zzDI = 3;
        this.url = url;
        this.zzqR = versionInfo;
        this.zzDJ = inAppPurchaseGmsgListener;
        this.zzDK = null;
        this.zzDL = null;
    }

    public AdOverlayInfoParcel(com.google.android.gms.ads.internal.client.zza adClickListener, zzg adOverlayListener, zzdh appEventGmsgListener, zzn leaveApplicationListener, zzjn adWebView, boolean customClose, int orientation, String html, String baseUrl, VersionInfoParcel versionInfo, zzdn inAppPurchaseGmsgListener) {
        this.versionCode = 4;
        this.zzDz = null;
        this.zzDA = adClickListener;
        this.zzDB = adOverlayListener;
        this.zzDC = adWebView;
        this.zzDD = appEventGmsgListener;
        this.zzDE = baseUrl;
        this.zzDF = customClose;
        this.zzDG = html;
        this.zzDH = leaveApplicationListener;
        this.orientation = orientation;
        this.zzDI = 3;
        this.url = null;
        this.zzqR = versionInfo;
        this.zzDJ = inAppPurchaseGmsgListener;
        this.zzDK = null;
        this.zzDL = null;
    }

    public AdOverlayInfoParcel(AdLauncherIntentInfoParcel adLauncherIntentInfo, com.google.android.gms.ads.internal.client.zza adClickListener, zzg adOverlayListener, zzn leaveApplicationListener, VersionInfoParcel versionInfo) {
        this.versionCode = 4;
        this.zzDz = adLauncherIntentInfo;
        this.zzDA = adClickListener;
        this.zzDB = adOverlayListener;
        this.zzDC = null;
        this.zzDD = null;
        this.zzDE = null;
        this.zzDF = false;
        this.zzDG = null;
        this.zzDH = leaveApplicationListener;
        this.orientation = -1;
        this.zzDI = 4;
        this.url = null;
        this.zzqR = versionInfo;
        this.zzDJ = null;
        this.zzDK = null;
        this.zzDL = null;
    }

    public static void zza(Intent intent, AdOverlayInfoParcel adOverlayInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", adOverlayInfoParcel);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    public static AdOverlayInfoParcel zzb(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(AdOverlayInfoParcel.class.getClassLoader());
            return (AdOverlayInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    IBinder zzfh() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDA).asBinder();
    }

    IBinder zzfi() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDB).asBinder();
    }

    IBinder zzfj() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDC).asBinder();
    }

    IBinder zzfk() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDD).asBinder();
    }

    IBinder zzfl() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDJ).asBinder();
    }

    IBinder zzfm() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzDH).asBinder();
    }
}
