package com.google.android.gms.internal;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.internal.zzex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzfi<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends zzex.zza {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> zzBO;
    private final NETWORK_EXTRAS zzBP;

    public zzfi(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.zzBO = mediationAdapter;
        this.zzBP = network_extras;
    }

    private SERVER_PARAMETERS zzb(String str, int i, String str2) throws RemoteException {
        HashMap map;
        try {
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str);
                map = new HashMap(jSONObject.length());
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, jSONObject.getString(next));
                }
            } else {
                map = new HashMap(0);
            }
            Class<SERVER_PARAMETERS> serverParametersType = this.zzBO.getServerParametersType();
            if (serverParametersType == null) {
                return null;
            }
            SERVER_PARAMETERS server_parametersNewInstance = serverParametersType.newInstance();
            server_parametersNewInstance.load(map);
            return server_parametersNewInstance;
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not get MediationServerParameters.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void destroy() throws RemoteException {
        try {
            this.zzBO.destroy();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not destroy adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle getInterstitialAdapterInfo() {
        return new Bundle();
    }

    @Override // com.google.android.gms.internal.zzex
    public com.google.android.gms.dynamic.zzd getView() throws RemoteException {
        if (!(this.zzBO instanceof MediationBannerAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationBannerAdapter: " + this.zzBO.getClass().getCanonicalName());
            throw new RemoteException();
        }
        try {
            return com.google.android.gms.dynamic.zze.zzB(((MediationBannerAdapter) this.zzBO).getBannerView());
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not get banner view from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public boolean isInitialized() {
        return true;
    }

    @Override // com.google.android.gms.internal.zzex
    public void pause() throws RemoteException {
        throw new RemoteException();
    }

    @Override // com.google.android.gms.internal.zzex
    public void resume() throws RemoteException {
        throw new RemoteException();
    }

    @Override // com.google.android.gms.internal.zzex
    public void showInterstitial() throws RemoteException {
        if (!(this.zzBO instanceof MediationInterstitialAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzBO.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Showing interstitial from adapter.");
        try {
            ((MediationInterstitialAdapter) this.zzBO).showInterstitial();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not show interstitial from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void showVideo() {
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, com.google.android.gms.ads.internal.reward.mediation.client.zza zzaVar, String str2) throws RemoteException {
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, zzey zzeyVar) throws RemoteException {
        zza(zzdVar, adRequestParcel, str, (String) null, zzeyVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar) throws RemoteException {
        if (!(this.zzBO instanceof MediationInterstitialAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationInterstitialAdapter: " + this.zzBO.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Requesting interstitial ad from adapter.");
        try {
            ((MediationInterstitialAdapter) this.zzBO).requestInterstitialAd(new zzfj(zzeyVar), (Activity) com.google.android.gms.dynamic.zze.zzp(zzdVar), zzb(str, adRequestParcel.zztu, str2), zzfk.zzi(adRequestParcel), this.zzBP);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request interstitial ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
    }

    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, zzey zzeyVar) throws RemoteException {
        zza(zzdVar, adSizeParcel, adRequestParcel, str, null, zzeyVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.zzex
    public void zza(com.google.android.gms.dynamic.zzd zzdVar, AdSizeParcel adSizeParcel, AdRequestParcel adRequestParcel, String str, String str2, zzey zzeyVar) throws RemoteException {
        if (!(this.zzBO instanceof MediationBannerAdapter)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("MediationAdapter is not a MediationBannerAdapter: " + this.zzBO.getClass().getCanonicalName());
            throw new RemoteException();
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Requesting banner ad from adapter.");
        try {
            ((MediationBannerAdapter) this.zzBO).requestBannerAd(new zzfj(zzeyVar), (Activity) com.google.android.gms.dynamic.zze.zzp(zzdVar), zzb(str, adRequestParcel.zztu, str2), zzfk.zzb(adSizeParcel), zzfk.zzi(adRequestParcel), this.zzBP);
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request banner ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzex
    public void zzc(AdRequestParcel adRequestParcel, String str) {
    }

    @Override // com.google.android.gms.internal.zzex
    public zzfa zzeu() {
        return null;
    }

    @Override // com.google.android.gms.internal.zzex
    public zzfb zzev() {
        return null;
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle zzew() {
        return new Bundle();
    }

    @Override // com.google.android.gms.internal.zzex
    public Bundle zzex() {
        return new Bundle();
    }
}
