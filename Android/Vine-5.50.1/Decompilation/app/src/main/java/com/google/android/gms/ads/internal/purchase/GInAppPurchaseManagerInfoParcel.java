package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.internal.zzgb;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class GInAppPurchaseManagerInfoParcel implements SafeParcelable {
    public static final zza CREATOR = new zza();
    public final int versionCode;
    public final zzgb zzEv;
    public final Context zzEw;
    public final zzj zzEx;
    public final zzk zzrm;

    GInAppPurchaseManagerInfoParcel(int versionCode, IBinder wrappedInAppPurchaseVerifier, IBinder wrappedInAppPurchase, IBinder wrappedAppContext, IBinder wrappedOnPlayStorePurchaseFinishedListener) {
        this.versionCode = versionCode;
        this.zzrm = (zzk) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedInAppPurchaseVerifier));
        this.zzEv = (zzgb) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedInAppPurchase));
        this.zzEw = (Context) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedAppContext));
        this.zzEx = (zzj) com.google.android.gms.dynamic.zze.zzp(zzd.zza.zzbs(wrappedOnPlayStorePurchaseFinishedListener));
    }

    public GInAppPurchaseManagerInfoParcel(Context appContext, zzk inAppPurchaseVerifier, zzgb inAppPurchase, zzj onPlayStorePurchaseFinishedListener) {
        this.versionCode = 2;
        this.zzEw = appContext;
        this.zzrm = inAppPurchaseVerifier;
        this.zzEv = inAppPurchase;
        this.zzEx = onPlayStorePurchaseFinishedListener;
    }

    public static void zza(Intent intent, GInAppPurchaseManagerInfoParcel gInAppPurchaseManagerInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo", gInAppPurchaseManagerInfoParcel);
        intent.putExtra("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo", bundle);
    }

    public static GInAppPurchaseManagerInfoParcel zzc(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo");
            bundleExtra.setClassLoader(GInAppPurchaseManagerInfoParcel.class.getClassLoader());
            return (GInAppPurchaseManagerInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo");
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
        zza.zza(this, out, flags);
    }

    IBinder zzfF() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzEx).asBinder();
    }

    IBinder zzfG() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzrm).asBinder();
    }

    IBinder zzfH() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzEv).asBinder();
    }

    IBinder zzfI() {
        return com.google.android.gms.dynamic.zze.zzB(this.zzEw).asBinder();
    }
}
