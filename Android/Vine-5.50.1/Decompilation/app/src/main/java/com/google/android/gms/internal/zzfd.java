package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.MediationBannerAdapter;
import com.google.android.gms.ads.mediation.MediationInterstitialAdapter;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.NativeAdMapper;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.internal.zzex;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzfd extends zzex.zza {
    private final MediationAdapter zzBI;
    private zzfe zzBJ;

    public zzfd(MediationAdapter mediationAdapter) {
        this.zzBI = mediationAdapter;
    }

    private Bundle zza(String str, int i, String str2) throws RemoteException {
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Server parameters: " + str);
        try {
            Bundle bundle = new Bundle();
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str);
                Bundle bundle2 = new Bundle();
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    bundle2.putString(next, jSONObject.getString(next));
                }
                bundle = bundle2;
            }
            if (this.zzBI instanceof AdMobAdapter) {
                bundle.putString("adJson", str2);
                bundle.putInt("tagForChildDirectedTreatment", i);
            }
            return bundle;
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not get Server Parameters Bundle.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void destroy() throws RemoteException {
        try {
            this.zzBI.onDestroy();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not destroy adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle getInterstitialAdapterInfo() {
        if (this.zzBI instanceof zzjy) {
            return ((zzjy) this.zzBI).getInterstitialAdapterInfo();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a v2 MediationInterstitialAdapter: " + this.zzBI.getClass().getCanonicalName());
        return new Bundle();
    }

    @Override // com.google.android.gms.internal.zzex
    public com.google.android.gms.dynamic.zzd getView() throws RemoteException {
        if (!(this.zzBI instanceof MediationBannerAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationBannerAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        try {
            return com.google.android.gms.dynamic.zze.zzB(((MediationBannerAdapter) this.zzBI).getBannerView());
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not get banner view from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public boolean isInitialized() throws RemoteException {
        if (!(this.zzBI instanceof MediationRewardedVideoAdAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Check if adapter is initialized.");
        try {
            return ((MediationRewardedVideoAdAdapter) this.zzBI).isInitialized();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not check if adapter is initialized.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void pause() throws RemoteException {
        try {
            this.zzBI.onPause();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not pause adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void resume() throws RemoteException {
        try {
            this.zzBI.onResume();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not resume adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void showInterstitial() throws RemoteException {
        if (!(this.zzBI instanceof MediationInterstitialAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Showing interstitial from adapter.");
        try {
            ((MediationInterstitialAdapter) this.zzBI).showInterstitial();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not show interstitial from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void showVideo() throws RemoteException {
        if (!(this.zzBI instanceof MediationRewardedVideoAdAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Show rewarded video ad from adapter.");
        try {
            ((MediationRewardedVideoAdAdapter) this.zzBI).showVideo();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not show rewarded video ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza zzaVar, String str2) throws RemoteException {
        if (!(this.zzBI instanceof MediationRewardedVideoAdAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Initialize rewarded video adapter.");
        try {
            MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzBI;
            mediationRewardedVideoAdAdapter.initialize((Context) com.google.android.gms.dynamic.zze.zzp(zzdVar), new zzfc(adRequestParcel.zztq == -1 ? null : new Date(adRequestParcel.zztq), adRequestParcel.zztr, adRequestParcel.zzts != null ? new HashSet(adRequestParcel.zzts) : null, adRequestParcel.zzty, adRequestParcel.zztt, adRequestParcel.zztu, adRequestParcel.zztF), str, new com.google.android.gms.ads.internal.reward.mediation.client.zzb(zzaVar), zza(str2, adRequestParcel.zztu, null), adRequestParcel.zztA != null ? adRequestParcel.zztA.getBundle(mediationRewardedVideoAdAdapter.getClass().getName()) : null);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not initialize rewarded video adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, zzey zzeyVar) throws RemoteException {
        zza(zzdVar, adRequestParcel, str, (String) null, zzeyVar);
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar) throws RemoteException {
        if (!(this.zzBI instanceof MediationInterstitialAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Requesting interstitial ad from adapter.");
        try {
            MediationInterstitialAdapter mediationInterstitialAdapter = (MediationInterstitialAdapter) this.zzBI;
            mediationInterstitialAdapter.requestInterstitialAd((Context) com.google.android.gms.dynamic.zze.zzp(zzdVar), new zzfe(zzeyVar), zza(str, adRequestParcel.zztu, str2), new zzfc(adRequestParcel.zztq == -1 ? null : new Date(adRequestParcel.zztq), adRequestParcel.zztr, adRequestParcel.zzts != null ? new HashSet(adRequestParcel.zzts) : null, adRequestParcel.zzty, adRequestParcel.zztt, adRequestParcel.zztu, adRequestParcel.zztF), adRequestParcel.zztA != null ? adRequestParcel.zztA.getBundle(mediationInterstitialAdapter.getClass().getName()) : null);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request interstitial ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) throws RemoteException {
        if (!(this.zzBI instanceof MediationNativeAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationNativeAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        try {
            MediationNativeAdapter mediationNativeAdapter = (MediationNativeAdapter) this.zzBI;
            zzfh zzfhVar = new zzfh(adRequestParcel.zztq == -1 ? null : new Date(adRequestParcel.zztq), adRequestParcel.zztr, adRequestParcel.zzts != null ? new HashSet(adRequestParcel.zzts) : null, adRequestParcel.zzty, adRequestParcel.zztt, adRequestParcel.zztu, nativeAdOptionsParcel, list, adRequestParcel.zztF);
            Bundle bundle = adRequestParcel.zztA != null ? adRequestParcel.zztA.getBundle(mediationNativeAdapter.getClass().getName()) : null;
            this.zzBJ = new zzfe(zzeyVar);
            mediationNativeAdapter.requestNativeAd((Context) com.google.android.gms.dynamic.zze.zzp(zzdVar), this.zzBJ, zza(str, adRequestParcel.zztu, str2), zzfhVar, bundle);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request interstitial ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzey zzeyVar) throws RemoteException {
        zza(zzdVar, adSizeParcel, adRequestParcel, str, null, zzeyVar);
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar) throws RemoteException {
        if (!(this.zzBI instanceof MediationBannerAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationBannerAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Requesting banner ad from adapter.");
        try {
            MediationBannerAdapter mediationBannerAdapter = (MediationBannerAdapter) this.zzBI;
            mediationBannerAdapter.requestBannerAd((Context) com.google.android.gms.dynamic.zze.zzp(zzdVar), new zzfe(zzeyVar), zza(str, adRequestParcel.zztu, str2), com.google.android.gms.ads.zza.zza(adSizeParcel.width, adSizeParcel.height, adSizeParcel.zztV), new zzfc(adRequestParcel.zztq == -1 ? null : new Date(adRequestParcel.zztq), adRequestParcel.zztr, adRequestParcel.zzts != null ? new HashSet(adRequestParcel.zzts) : null, adRequestParcel.zzty, adRequestParcel.zztt, adRequestParcel.zztu, adRequestParcel.zztF), adRequestParcel.zztA != null ? adRequestParcel.zztA.getBundle(mediationBannerAdapter.getClass().getName()) : null);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request banner ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zzc(AdRequestParcel adRequestParcel, String str) throws RemoteException {
        if (!(this.zzBI instanceof MediationRewardedVideoAdAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationRewardedVideoAdAdapter: " + this.zzBI.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Requesting rewarded video ad from adapter.");
        try {
            MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter = (MediationRewardedVideoAdAdapter) this.zzBI;
            mediationRewardedVideoAdAdapter.loadAd(new zzfc(adRequestParcel.zztq == -1 ? null : new Date(adRequestParcel.zztq), adRequestParcel.zztr, adRequestParcel.zzts != null ? new HashSet(adRequestParcel.zzts) : null, adRequestParcel.zzty, adRequestParcel.zztt, adRequestParcel.zztu, adRequestParcel.zztF), zza(str, adRequestParcel.zztu, null), adRequestParcel.zztA != null ? adRequestParcel.zztA.getBundle(mediationRewardedVideoAdAdapter.getClass().getName()) : null);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not load rewarded video ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public zzfa zzeu() {
        NativeAdMapper nativeAdMapperZzey = this.zzBJ.zzey();
        if (nativeAdMapperZzey instanceof NativeAppInstallAdMapper) {
            return new zzff((NativeAppInstallAdMapper) nativeAdMapperZzey);
        }
        return null;
    }

    @Override // com.google.android.gms.internal.zzex
    public zzfb zzev() {
        NativeAdMapper nativeAdMapperZzey = this.zzBJ.zzey();
        if (nativeAdMapperZzey instanceof NativeContentAdMapper) {
            return new zzfg((NativeContentAdMapper) nativeAdMapperZzey);
        }
        return null;
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle zzew() {
        if (this.zzBI instanceof zzjx) {
            return ((zzjx) this.zzBI).zzew();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a v2 MediationBannerAdapter: " + this.zzBI.getClass().getCanonicalName());
        return new Bundle();
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle zzex() {
        return new Bundle();
    }
}
