package com.google.android.gms.ads.internal;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.ThinAdSizeParcel;
import com.google.android.gms.ads.internal.client.zzs;
import com.google.android.gms.ads.internal.client.zzu;
import com.google.android.gms.ads.internal.client.zzv;
import com.google.android.gms.ads.internal.request.zza;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzay;
import com.google.android.gms.internal.zzbi;
import com.google.android.gms.internal.zzbl;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzcl;
import com.google.android.gms.internal.zzdh;
import com.google.android.gms.internal.zzgc;
import com.google.android.gms.internal.zzgg;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzii;
import com.google.android.gms.internal.zzij;
import java.util.HashSet;

@zzha
/* loaded from: classes.dex */
public abstract class zza extends zzs.zza implements com.google.android.gms.ads.internal.client.zza, com.google.android.gms.ads.internal.overlay.zzn, zza.InterfaceC0024zza, zzdh, zzgq.zza, zzii {
    protected zzch zzoU;
    protected zzcf zzoV;
    protected zzcf zzoW;
    protected boolean zzoX = false;
    protected final zzo zzoY;
    protected final zzq zzoZ;
    protected transient AdRequestParcel zzpa;
    protected final zzay zzpb;
    protected final zzd zzpc;

    zza(zzq zzqVar, zzo zzoVar, zzd zzdVar) {
        this.zzoZ = zzqVar;
        this.zzoY = zzoVar == null ? new zzo(this) : zzoVar;
        this.zzpc = zzdVar;
        zzp.zzbx().zzK(this.zzoZ.context);
        zzp.zzbA().zzb(this.zzoZ.context, this.zzoZ.zzqR);
        this.zzpb = zzp.zzbA().zzgR();
    }

    private AdRequestParcel zza(AdRequestParcel adRequestParcel) {
        return (!GooglePlayServicesUtil.zzao(this.zzoZ.context) || adRequestParcel.zzty == null) ? adRequestParcel : new com.google.android.gms.ads.internal.client.zzf(adRequestParcel).zza(null).zzcI();
    }

    private boolean zzaT() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Ad leaving application.");
        if (this.zzoZ.zzra == null) {
            return false;
        }
        try {
            this.zzoZ.zzra.onAdLeftApplication();
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call AdListener.onAdLeftApplication().", e);
            return false;
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void destroy() {
        zzx.zzcx("destroy must be called on the main UI thread.");
        this.zzoY.cancel();
        this.zzpb.zzf(this.zzoZ.zzqW);
        this.zzoZ.destroy();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean isLoading() {
        return this.zzoX;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean isReady() {
        zzx.zzcx("isLoaded must be called on the main UI thread.");
        return this.zzoZ.zzqT == null && this.zzoZ.zzqU == null && this.zzoZ.zzqW != null;
    }

    @Override // com.google.android.gms.ads.internal.client.zza
    public void onAdClicked() {
        if (this.zzoZ.zzqW == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ad state was null when trying to ping click URLs.");
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Pinging click URLs.");
        this.zzoZ.zzqY.zzgE();
        if (this.zzoZ.zzqW.zzAQ != null) {
            zzp.zzbx().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, this.zzoZ.zzqW.zzAQ);
        }
        if (this.zzoZ.zzqZ != null) {
            try {
                this.zzoZ.zzqZ.onAdClicked();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not notify onAdClicked event.", e);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzdh
    public void onAppEvent(String name, String info) {
        if (this.zzoZ.zzrb != null) {
            try {
                this.zzoZ.zzrb.onAppEvent(name, info);
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call the AppEventListener.", e);
            }
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void pause() {
        zzx.zzcx("pause must be called on the main UI thread.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void recordImpression() {
        zzc(this.zzoZ.zzqW);
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void resume() {
        zzx.zzcx("resume must be called on the main UI thread.");
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void setManualImpressionsEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Attempt to call setManualImpressionsEnabled for an unsupported ad type.");
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void stopLoading() {
        zzx.zzcx("stopLoading must be called on the main UI thread.");
        this.zzoX = false;
        this.zzoZ.zzf(true);
    }

    Bundle zza(zzbl zzblVar) {
        String strZzcu;
        if (zzblVar == null) {
            return null;
        }
        if (zzblVar.zzcF()) {
            zzblVar.wakeup();
        }
        zzbi zzbiVarZzcD = zzblVar.zzcD();
        if (zzbiVarZzcD != null) {
            strZzcu = zzbiVarZzcD.zzcu();
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("In AdManger: loadAd, " + zzbiVarZzcD.toString());
        } else {
            strZzcu = null;
        }
        if (strZzcu == null) {
            return null;
        }
        Bundle bundle = new Bundle(1);
        bundle.putString("fingerprint", strZzcu);
        bundle.putInt("v", 1);
        return bundle;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(AdSizeParcel adSizeParcel) {
        zzx.zzcx("setAdSize must be called on the main UI thread.");
        this.zzoZ.zzqV = adSizeParcel;
        if (this.zzoZ.zzqW != null && this.zzoZ.zzqW.zzDC != null && this.zzoZ.zzrp == 0) {
            this.zzoZ.zzqW.zzDC.zza(adSizeParcel);
        }
        if (this.zzoZ.zzqS == null) {
            return;
        }
        if (this.zzoZ.zzqS.getChildCount() > 1) {
            this.zzoZ.zzqS.removeView(this.zzoZ.zzqS.getNextView());
        }
        this.zzoZ.zzqS.setMinimumWidth(adSizeParcel.widthPixels);
        this.zzoZ.zzqS.setMinimumHeight(adSizeParcel.heightPixels);
        this.zzoZ.zzqS.requestLayout();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzn zznVar) {
        zzx.zzcx("setAdListener must be called on the main UI thread.");
        this.zzoZ.zzqZ = zznVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(com.google.android.gms.ads.internal.client.zzo zzoVar) {
        zzx.zzcx("setAdListener must be called on the main UI thread.");
        this.zzoZ.zzra = zzoVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzu zzuVar) {
        zzx.zzcx("setAppEventListener must be called on the main UI thread.");
        this.zzoZ.zzrb = zzuVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzv zzvVar) {
        zzx.zzcx("setCorrelationIdProvider must be called on the main UI thread");
        this.zzoZ.zzrc = zzvVar;
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzcl zzclVar) {
        throw new IllegalStateException("setOnCustomRenderedAdLoadedListener is not supported for current ad type");
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgc zzgcVar) {
        throw new IllegalStateException("setInAppPurchaseListener is not supported for current ad type");
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgg zzggVar, String str) {
        throw new IllegalStateException("setPlayStorePurchaseParams is not supported for current ad type");
    }

    @Override // com.google.android.gms.ads.internal.request.zza.InterfaceC0024zza
    public void zza(zzie.zza zzaVar) {
        if (zzaVar.zzJL.zzGR != -1 && !TextUtils.isEmpty(zzaVar.zzJL.zzHb)) {
            long jZzo = zzo(zzaVar.zzJL.zzHb);
            if (jZzo != -1) {
                this.zzoU.zza(this.zzoU.zzb(jZzo + zzaVar.zzJL.zzGR), "stc");
            }
        }
        this.zzoU.zzR(zzaVar.zzJL.zzHb);
        this.zzoU.zza(this.zzoV, "arf");
        this.zzoW = this.zzoU.zzdu();
        this.zzoU.zzd("gqi", zzaVar.zzJL.zzHc);
        this.zzoZ.zzqT = null;
        this.zzoZ.zzqX = zzaVar;
        zza(zzaVar, this.zzoU);
    }

    protected abstract void zza(zzie.zza zzaVar, zzch zzchVar);

    @Override // com.google.android.gms.internal.zzii
    public void zza(HashSet<zzif> hashSet) {
        this.zzoZ.zza(hashSet);
    }

    protected abstract boolean zza(AdRequestParcel adRequestParcel, zzch zzchVar);

    boolean zza(zzie zzieVar) {
        return false;
    }

    protected abstract boolean zza(zzie zzieVar, zzie zzieVar2);

    void zzaN() {
        this.zzoU = new zzch(zzbz.zzvL.get().booleanValue(), "load_ad", this.zzoZ.zzqV.zztV);
        this.zzoV = new zzcf(-1L, null, null);
        this.zzoW = new zzcf(-1L, null, null);
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public com.google.android.gms.dynamic.zzd zzaO() {
        zzx.zzcx("getAdFrame must be called on the main UI thread.");
        return com.google.android.gms.dynamic.zze.zzB(this.zzoZ.zzqS);
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public AdSizeParcel zzaP() {
        zzx.zzcx("getAdSize must be called on the main UI thread.");
        if (this.zzoZ.zzqV == null) {
            return null;
        }
        return new ThinAdSizeParcel(this.zzoZ.zzqV);
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzn
    public void zzaQ() {
        zzaT();
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void zzaR() {
        zzx.zzcx("recordManualImpression must be called on the main UI thread.");
        if (this.zzoZ.zzqW == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ad state was null when trying to ping manual tracking URLs.");
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Pinging manual tracking URLs.");
        if (this.zzoZ.zzqW.zzGP != null) {
            zzp.zzbx().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, this.zzoZ.zzqW.zzGP);
        }
    }

    protected boolean zzaS() {
        com.google.android.gms.ads.internal.util.client.zzb.v("Ad closing.");
        if (this.zzoZ.zzra == null) {
            return false;
        }
        try {
            this.zzoZ.zzra.onAdClosed();
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call AdListener.onAdClosed().", e);
            return false;
        }
    }

    protected boolean zzaU() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Ad opening.");
        if (this.zzoZ.zzra == null) {
            return false;
        }
        try {
            this.zzoZ.zzra.onAdOpened();
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call AdListener.onAdOpened().", e);
            return false;
        }
    }

    protected boolean zzaV() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Ad finished loading.");
        this.zzoX = false;
        if (this.zzoZ.zzra == null) {
            return false;
        }
        try {
            this.zzoZ.zzra.onAdLoaded();
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call AdListener.onAdLoaded().", e);
            return false;
        }
    }

    protected void zzb(View view) {
        this.zzoZ.zzqS.addView(view, zzp.zzbz().zzhg());
    }

    @Override // com.google.android.gms.internal.zzgq.zza
    public void zzb(zzie zzieVar) {
        this.zzoU.zza(this.zzoW, "awr");
        this.zzoZ.zzqU = null;
        if (zzieVar.errorCode != -2 && zzieVar.errorCode != 3) {
            zzp.zzbA().zzb(this.zzoZ.zzbM());
        }
        if (zzieVar.errorCode == -1) {
            this.zzoX = false;
            return;
        }
        if (zza(zzieVar)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Ad refresh scheduled.");
        }
        if (zzieVar.errorCode != -2) {
            zzf(zzieVar.errorCode);
            return;
        }
        if (this.zzoZ.zzrn == null) {
            this.zzoZ.zzrn = new zzij(this.zzoZ.zzqP);
        }
        this.zzpb.zze(this.zzoZ.zzqW);
        if (zza(this.zzoZ.zzqW, zzieVar)) {
            this.zzoZ.zzqW = zzieVar;
            this.zzoZ.zzbV();
            this.zzoU.zzd("is_mraid", this.zzoZ.zzqW.zzcb() ? "1" : "0");
            this.zzoU.zzd("is_mediation", this.zzoZ.zzqW.zzGN ? "1" : "0");
            if (this.zzoZ.zzqW.zzDC != null && this.zzoZ.zzqW.zzDC.zzhC() != null) {
                this.zzoU.zzd("is_video", this.zzoZ.zzqW.zzDC.zzhC().zzhP() ? "1" : "0");
            }
            this.zzoU.zza(this.zzoV, "ttc");
            if (zzp.zzbA().zzgM() != null) {
                zzp.zzbA().zzgM().zza(this.zzoU);
            }
            if (this.zzoZ.zzbQ()) {
                zzaV();
            }
        }
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public boolean zzb(AdRequestParcel adRequestParcel) {
        zzx.zzcx("loadAd must be called on the main UI thread.");
        AdRequestParcel adRequestParcelZza = zza(adRequestParcel);
        if (this.zzoZ.zzqT != null || this.zzoZ.zzqU != null) {
            if (this.zzpa != null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Aborting last ad request since another ad request is already in progress. The current request object will still be cached for future refreshes.");
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Loading already in progress, saving this object for future refreshes.");
            }
            this.zzpa = adRequestParcelZza;
            return false;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Starting ad request.");
        zzaN();
        this.zzoV = this.zzoU.zzdu();
        if (!adRequestParcelZza.zztt) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Use AdRequest.Builder.addTestDevice(\"" + com.google.android.gms.ads.internal.client.zzl.zzcN().zzS(this.zzoZ.context) + "\") to get test ads on this device.");
        }
        this.zzoX = zza(adRequestParcelZza, this.zzoU);
        return this.zzoX;
    }

    protected void zzc(zzie zzieVar) {
        if (zzieVar == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ad state was null when trying to ping impression URLs.");
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Pinging Impression URLs.");
        this.zzoZ.zzqY.zzgD();
        if (zzieVar.zzAR != null) {
            zzp.zzbx().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, zzieVar.zzAR);
        }
    }

    protected boolean zzc(AdRequestParcel adRequestParcel) {
        Object parent = this.zzoZ.zzqS.getParent();
        return (parent instanceof View) && ((View) parent).isShown() && zzp.zzbx().zzgY();
    }

    public void zzd(AdRequestParcel adRequestParcel) {
        if (zzc(adRequestParcel)) {
            zzb(adRequestParcel);
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Ad is not visible. Not refreshing ad.");
            this.zzoY.zzg(adRequestParcel);
        }
    }

    protected boolean zzf(int i) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Failed to load ad: " + i);
        this.zzoX = false;
        if (this.zzoZ.zzra == null) {
            return false;
        }
        try {
            this.zzoZ.zzra.onAdFailedToLoad(i);
            return true;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call AdListener.onAdFailedToLoad().", e);
            return false;
        }
    }

    long zzo(String str) {
        int iIndexOf = str.indexOf("ufe");
        int iIndexOf2 = str.indexOf(44, iIndexOf);
        if (iIndexOf2 == -1) {
            iIndexOf2 = str.length();
        }
        try {
            return Long.parseLong(str.substring(iIndexOf + 4, iIndexOf2));
        } catch (IndexOutOfBoundsException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Invalid index for Url fetch time in CSI latency info.");
            return -1L;
        } catch (NumberFormatException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Cannot find valid format of Url fetch time in CSI latency info.");
            return -1L;
        }
    }
}
