package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzs;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzef;

@zzha
/* loaded from: classes.dex */
public class zzeg extends zzs.zza {
    private zzec zzAd;
    private zzgg zzAe;
    private String zzAf;
    private String zzpH;
    private zzea zzzU;
    private com.google.android.gms.ads.internal.zzk zzzX;

    public zzeg(Context context, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, com.google.android.gms.ads.internal.zzd zzdVar) {
        this(str, new zzea(context.getApplicationContext(), zzewVar, versionInfoParcel, zzdVar));
    }

    public zzeg(String str, zzea zzeaVar) {
        this.zzpH = str;
        this.zzzU = zzeaVar;
        this.zzAd = new zzec();
        com.google.android.gms.ads.internal.zzp.zzbI().zza(zzeaVar);
    }

    private void zzee() {
        if (this.zzzX == null || this.zzAe == null) {
            return;
        }
        this.zzzX.zza(this.zzAe, this.zzAf);
    }

    void abort() {
        if (this.zzzX != null) {
            return;
        }
        this.zzzX = this.zzzU.zzac(this.zzpH);
        this.zzAd.zzc(this.zzzX);
        zzee();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void destroy() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.destroy();
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public String getMediationAdapterClassName() throws RemoteException {
        if (this.zzzX != null) {
            return this.zzzX.getMediationAdapterClassName();
        }
        return null;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean isLoading() throws RemoteException {
        return this.zzzX != null && this.zzzX.isLoading();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean isReady() throws RemoteException {
        return this.zzzX != null && this.zzzX.isReady();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void pause() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.pause();
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void resume() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.resume();
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) throws RemoteException {
        abort();
        if (this.zzzX != null) {
            this.zzzX.setManualImpressionsEnabled(manualImpressionsEnabled);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void showInterstitial() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.showInterstitial();
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Interstitial ad must be loaded before showInterstitial().");
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void stopLoading() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.stopLoading();
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(AdSizeParcel adSizeParcel) throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.zza(adSizeParcel);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzn zznVar) throws RemoteException {
        this.zzAd.zzzP = zznVar;
        if (this.zzzX != null) {
            this.zzAd.zzc(this.zzzX);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzo zzoVar) throws RemoteException {
        this.zzAd.zzpz = zzoVar;
        if (this.zzzX != null) {
            this.zzAd.zzc(this.zzzX);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzu zzuVar) throws RemoteException {
        this.zzAd.zzzM = zzuVar;
        if (this.zzzX != null) {
            this.zzAd.zzc(this.zzzX);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzv zzvVar) throws RemoteException {
        abort();
        if (this.zzzX != null) {
            this.zzzX.zza(zzvVar);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzcl zzclVar) throws RemoteException {
        this.zzAd.zzzO = zzclVar;
        if (this.zzzX != null) {
            this.zzAd.zzc(this.zzzX);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgc zzgcVar) throws RemoteException {
        this.zzAd.zzzN = zzgcVar;
        if (this.zzzX != null) {
            this.zzAd.zzc(this.zzzX);
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgg zzggVar, String str) throws RemoteException {
        this.zzAe = zzggVar;
        this.zzAf = str;
        zzee();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public com.google.android.gms.dynamic.zzd zzaO() throws RemoteException {
        if (this.zzzX != null) {
            return this.zzzX.zzaO();
        }
        return null;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public AdSizeParcel zzaP() throws RemoteException {
        if (this.zzzX != null) {
            return this.zzzX.zzaP();
        }
        return null;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zzaR() throws RemoteException {
        if (this.zzzX != null) {
            this.zzzX.zzaR();
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Interstitial ad must be loaded before pingManualTrackingUrl().");
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean zzb(AdRequestParcel adRequestParcel) throws RemoteException {
        if (adRequestParcel.zztx != null) {
            abort();
        }
        if (this.zzzX != null) {
            return this.zzzX.zzb(adRequestParcel);
        }
        zzef.zza zzaVarZza = com.google.android.gms.ads.internal.zzp.zzbI().zza(adRequestParcel, this.zzpH);
        if (zzaVarZza == null) {
            this.zzzX = this.zzzU.zzac(this.zzpH);
            this.zzAd.zzc(this.zzzX);
            zzee();
            return this.zzzX.zzb(adRequestParcel);
        }
        if (!zzaVarZza.zzAa) {
            zzaVarZza.zzh(adRequestParcel);
        }
        this.zzzX = zzaVarZza.zzzX;
        zzaVarZza.zzc(this.zzzU);
        zzaVarZza.zzzY.zza(this.zzAd);
        this.zzAd.zzc(this.zzzX);
        zzee();
        return zzaVarZza.zzAb;
    }
}
