package com.google.android.gms.ads.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.purchase.GInAppPurchaseManagerInfoParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.CapabilityParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzdn;
import com.google.android.gms.internal.zzeo;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfz;
import com.google.android.gms.internal.zzgc;
import com.google.android.gms.internal.zzgg;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzif;
import com.google.android.gms.internal.zzip;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONException;

@zzha
/* loaded from: classes.dex */
public abstract class zzb extends zza implements com.google.android.gms.ads.internal.overlay.zzg, com.google.android.gms.ads.internal.purchase.zzj, zzdn, zzeo {
    private final Messenger mMessenger;
    protected final zzew zzpd;
    protected transient boolean zzpe;

    public zzb(Context context, AdSizeParcel adSizeParcel, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel, zzd zzdVar) {
        this(new zzq(context, adSizeParcel, str, versionInfoParcel), zzewVar, null, zzdVar);
    }

    zzb(zzq zzqVar, zzew zzewVar, zzo zzoVar, zzd zzdVar) {
        super(zzqVar, zzoVar, zzdVar);
        this.zzpd = zzewVar;
        this.mMessenger = new Messenger(new zzfz(this.zzoZ.context));
        this.zzpe = false;
    }

    private AdRequestInfoParcel.zza zza(AdRequestParcel adRequestParcel, Bundle bundle) throws JSONException, PackageManager.NameNotFoundException {
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo = this.zzoZ.context.getApplicationInfo();
        try {
            packageInfo = this.zzoZ.context.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        DisplayMetrics displayMetrics = this.zzoZ.context.getResources().getDisplayMetrics();
        Bundle bundle2 = null;
        if (this.zzoZ.zzqS != null && this.zzoZ.zzqS.getParent() != null) {
            int[] iArr = new int[2];
            this.zzoZ.zzqS.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            int width = this.zzoZ.zzqS.getWidth();
            int height = this.zzoZ.zzqS.getHeight();
            int i3 = 0;
            if (this.zzoZ.zzqS.isShown() && i + width > 0 && i2 + height > 0 && i <= displayMetrics.widthPixels && i2 <= displayMetrics.heightPixels) {
                i3 = 1;
            }
            bundle2 = new Bundle(5);
            bundle2.putInt("x", i);
            bundle2.putInt("y", i2);
            bundle2.putInt("width", width);
            bundle2.putInt("height", height);
            bundle2.putInt("visible", i3);
        }
        String strZzgK = zzp.zzbA().zzgK();
        this.zzoZ.zzqY = new zzif(strZzgK, this.zzoZ.zzqP);
        this.zzoZ.zzqY.zzj(adRequestParcel);
        String strZza = zzp.zzbx().zza(this.zzoZ.context, this.zzoZ.zzqS, this.zzoZ.zzqV);
        long value = 0;
        if (this.zzoZ.zzrc != null) {
            try {
                value = this.zzoZ.zzrc.getValue();
            } catch (RemoteException e2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Cannot get correlation id, default to 0.");
            }
        }
        String string = UUID.randomUUID().toString();
        Bundle bundleZza = zzp.zzbA().zza(this.zzoZ.context, this, strZzgK);
        ArrayList arrayList = new ArrayList();
        for (int i4 = 0; i4 < this.zzoZ.zzri.size(); i4++) {
            arrayList.add(this.zzoZ.zzri.keyAt(i4));
        }
        return new AdRequestInfoParcel.zza(bundle2, adRequestParcel, this.zzoZ.zzqV, this.zzoZ.zzqP, applicationInfo, packageInfo, strZzgK, zzp.zzbA().getSessionId(), this.zzoZ.zzqR, bundleZza, this.zzoZ.zzrl, arrayList, bundle, zzp.zzbA().zzgO(), this.mMessenger, displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.density, strZza, value, string, zzbz.zzdl(), this.zzoZ.zzqO, this.zzoZ.zzrj, new CapabilityParcel(this.zzoZ.zzrd != null, this.zzoZ.zzre != null && zzp.zzbA().zzgT()), this.zzoZ.zzbU());
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public String getMediationAdapterClassName() {
        if (this.zzoZ.zzqW == null) {
            return null;
        }
        return this.zzoZ.zzqW.zzBr;
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zza
    public void onAdClicked() {
        if (this.zzoZ.zzqW == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ad state was null when trying to ping click URLs.");
            return;
        }
        if (this.zzoZ.zzqW.zzJF != null && this.zzoZ.zzqW.zzJF.zzAQ != null) {
            zzp.zzbK().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, this.zzoZ.zzqW, this.zzoZ.zzqP, false, this.zzoZ.zzqW.zzJF.zzAQ);
        }
        if (this.zzoZ.zzqW.zzBp != null && this.zzoZ.zzqW.zzBp.zzAJ != null) {
            zzp.zzbK().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, this.zzoZ.zzqW, this.zzoZ.zzqP, false, this.zzoZ.zzqW.zzBp.zzAJ);
        }
        super.onAdClicked();
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void pause() {
        zzx.zzcx("pause must be called on the main UI thread.");
        if (this.zzoZ.zzqW != null && this.zzoZ.zzqW.zzDC != null && this.zzoZ.zzbQ()) {
            zzp.zzbz().zzf(this.zzoZ.zzqW.zzDC);
        }
        if (this.zzoZ.zzqW != null && this.zzoZ.zzqW.zzBq != null) {
            try {
                this.zzoZ.zzqW.zzBq.pause();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not pause mediation adapter.");
            }
        }
        this.zzpb.zzg(this.zzoZ.zzqW);
        this.zzoY.pause();
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void resume() {
        zzx.zzcx("resume must be called on the main UI thread.");
        if (this.zzoZ.zzqW != null && this.zzoZ.zzqW.zzDC != null && this.zzoZ.zzbQ()) {
            zzp.zzbz().zzg(this.zzoZ.zzqW.zzDC);
        }
        if (this.zzoZ.zzqW != null && this.zzoZ.zzqW.zzBq != null) {
            try {
                this.zzoZ.zzqW.zzBq.resume();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not resume mediation adapter.");
            }
        }
        this.zzoY.resume();
        this.zzpb.zzh(this.zzoZ.zzqW);
    }

    @Override // com.google.android.gms.ads.internal.client.zzs
    public void showInterstitial() {
        throw new IllegalStateException("showInterstitial is not supported for current ad type");
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgc zzgcVar) {
        zzx.zzcx("setInAppPurchaseListener must be called on the main UI thread.");
        this.zzoZ.zzrd = zzgcVar;
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgg zzggVar, String str) {
        zzx.zzcx("setPlayStorePurchaseParams must be called on the main UI thread.");
        this.zzoZ.zzrm = new com.google.android.gms.ads.internal.purchase.zzk(str);
        this.zzoZ.zzre = zzggVar;
        if (zzp.zzbA().zzgN() || zzggVar == null) {
            return;
        }
        new com.google.android.gms.ads.internal.purchase.zzc(this.zzoZ.context, this.zzoZ.zzre, this.zzoZ.zzrm).zzfR();
    }

    protected void zza(zzie zzieVar, boolean z) {
        if (zzieVar == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ad state was null when trying to ping impression URLs.");
            return;
        }
        super.zzc(zzieVar);
        if (zzieVar.zzJF != null && zzieVar.zzJF.zzAR != null) {
            zzp.zzbK().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, zzieVar, this.zzoZ.zzqP, z, zzieVar.zzJF.zzAR);
        }
        if (zzieVar.zzBp == null || zzieVar.zzBp.zzAK == null) {
            return;
        }
        zzp.zzbK().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, zzieVar, this.zzoZ.zzqP, z, zzieVar.zzBp.zzAK);
    }

    @Override // com.google.android.gms.internal.zzdn
    public void zza(String str, ArrayList<String> arrayList) {
        com.google.android.gms.ads.internal.purchase.zzd zzdVar = new com.google.android.gms.ads.internal.purchase.zzd(str, arrayList, this.zzoZ.context, this.zzoZ.zzqR.afmaVersion);
        if (this.zzoZ.zzrd != null) {
            try {
                this.zzoZ.zzrd.zza(zzdVar);
                return;
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not start In-App purchase.");
                return;
            }
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("InAppPurchaseListener is not set. Try to launch default purchase flow.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzT(this.zzoZ.context)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Google Play Service unavailable, cannot launch default purchase flow.");
            return;
        }
        if (this.zzoZ.zzre == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("PlayStorePurchaseListener is not set.");
            return;
        }
        if (this.zzoZ.zzrm == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("PlayStorePurchaseVerifier is not initialized.");
            return;
        }
        if (this.zzoZ.zzrq) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("An in-app purchase request is already in progress, abort");
            return;
        }
        this.zzoZ.zzrq = true;
        try {
            if (this.zzoZ.zzre.isValidPurchase(str)) {
                zzp.zzbH().zza(this.zzoZ.context, this.zzoZ.zzqR.zzLH, new GInAppPurchaseManagerInfoParcel(this.zzoZ.context, this.zzoZ.zzrm, zzdVar, this));
            } else {
                this.zzoZ.zzrq = false;
            }
        } catch (RemoteException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not start In-App purchase.");
            this.zzoZ.zzrq = false;
        }
    }

    @Override // com.google.android.gms.ads.internal.purchase.zzj
    public void zza(String str, boolean z, int i, final Intent intent, com.google.android.gms.ads.internal.purchase.zzf zzfVar) {
        try {
            if (this.zzoZ.zzre != null) {
                this.zzoZ.zzre.zza(new com.google.android.gms.ads.internal.purchase.zzg(this.zzoZ.context, str, z, i, intent, zzfVar));
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Fail to invoke PlayStorePurchaseListener.");
        }
        zzip.zzKO.postDelayed(new Runnable() { // from class: com.google.android.gms.ads.internal.zzb.1
            @Override // java.lang.Runnable
            public void run() {
                int iZzd = zzp.zzbH().zzd(intent);
                zzp.zzbH();
                if (iZzd == 0 && zzb.this.zzoZ.zzqW != null && zzb.this.zzoZ.zzqW.zzDC != null && zzb.this.zzoZ.zzqW.zzDC.zzhA() != null) {
                    zzb.this.zzoZ.zzqW.zzDC.zzhA().close();
                }
                zzb.this.zzoZ.zzrq = false;
            }
        }, 500L);
    }

    @Override // com.google.android.gms.ads.internal.zza
    public boolean zza(AdRequestParcel adRequestParcel, zzch zzchVar) throws JSONException, PackageManager.NameNotFoundException {
        if (!zzaW()) {
            return false;
        }
        Bundle bundleZza = zza(zzp.zzbA().zzG(this.zzoZ.context));
        this.zzoY.cancel();
        this.zzoZ.zzrp = 0;
        AdRequestInfoParcel.zza zzaVarZza = zza(adRequestParcel, bundleZza);
        zzchVar.zzd("seq_num", zzaVarZza.zzGt);
        zzchVar.zzd("request_id", zzaVarZza.zzGF);
        zzchVar.zzd("session_id", zzaVarZza.zzGu);
        if (zzaVarZza.zzGr != null) {
            zzchVar.zzd("app_version", String.valueOf(zzaVarZza.zzGr.versionCode));
        }
        this.zzoZ.zzqT = zzp.zzbt().zza(this.zzoZ.context, zzaVarZza, this.zzoZ.zzqQ, this);
        return true;
    }

    protected boolean zza(AdRequestParcel adRequestParcel, zzie zzieVar, boolean z) {
        if (!z && this.zzoZ.zzbQ()) {
            if (zzieVar.zzAU > 0) {
                this.zzoY.zza(adRequestParcel, zzieVar.zzAU);
            } else if (zzieVar.zzJF != null && zzieVar.zzJF.zzAU > 0) {
                this.zzoY.zza(adRequestParcel, zzieVar.zzJF.zzAU);
            } else if (!zzieVar.zzGN && zzieVar.errorCode == 2) {
                this.zzoY.zzg(adRequestParcel);
            }
        }
        return this.zzoY.zzbr();
    }

    @Override // com.google.android.gms.ads.internal.zza
    boolean zza(zzie zzieVar) {
        AdRequestParcel adRequestParcel;
        boolean z = false;
        if (this.zzpa != null) {
            adRequestParcel = this.zzpa;
            this.zzpa = null;
        } else {
            adRequestParcel = zzieVar.zzGq;
            if (adRequestParcel.extras != null) {
                z = adRequestParcel.extras.getBoolean("_noRefresh", false);
            }
        }
        return zza(adRequestParcel, zzieVar, z);
    }

    @Override // com.google.android.gms.ads.internal.zza
    protected boolean zza(zzie zzieVar, zzie zzieVar2) {
        int i;
        int i2 = 0;
        if (zzieVar != null && zzieVar.zzBs != null) {
            zzieVar.zzBs.zza((zzeo) null);
        }
        if (zzieVar2.zzBs != null) {
            zzieVar2.zzBs.zza(this);
        }
        if (zzieVar2.zzJF != null) {
            i = zzieVar2.zzJF.zzAZ;
            i2 = zzieVar2.zzJF.zzBa;
        } else {
            i = 0;
        }
        this.zzoZ.zzrn.zzg(i, i2);
        return true;
    }

    protected boolean zzaW() {
        return zzp.zzbx().zza(this.zzoZ.context.getPackageManager(), this.zzoZ.context.getPackageName(), "android.permission.INTERNET") && zzp.zzbx().zzJ(this.zzoZ.context);
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzg
    public void zzaX() {
        this.zzpb.zze(this.zzoZ.zzqW);
        this.zzpe = false;
        zzaS();
        this.zzoZ.zzqY.zzgF();
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzg
    public void zzaY() {
        this.zzpe = true;
        zzaU();
    }

    @Override // com.google.android.gms.internal.zzeo
    public void zzaZ() {
        onAdClicked();
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.internal.zzgq.zza
    public void zzb(zzie zzieVar) {
        super.zzb(zzieVar);
        if (zzieVar.errorCode != 3 || zzieVar.zzJF == null || zzieVar.zzJF.zzAS == null) {
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Pinging no fill URLs.");
        zzp.zzbK().zza(this.zzoZ.context, this.zzoZ.zzqR.afmaVersion, zzieVar, this.zzoZ.zzqP, false, zzieVar.zzJF.zzAS);
    }

    @Override // com.google.android.gms.internal.zzeo
    public void zzba() {
        zzaX();
    }

    @Override // com.google.android.gms.internal.zzeo
    public void zzbb() {
        zzaQ();
    }

    @Override // com.google.android.gms.internal.zzeo
    public void zzbc() {
        zzaY();
    }

    @Override // com.google.android.gms.internal.zzeo
    public void zzbd() {
        if (this.zzoZ.zzqW != null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Mediation adapter " + this.zzoZ.zzqW.zzBr + " refreshed, but mediation adapters should never refresh.");
        }
        zza(this.zzoZ.zzqW, true);
        zzaV();
    }

    @Override // com.google.android.gms.ads.internal.zza
    protected boolean zzc(AdRequestParcel adRequestParcel) {
        return super.zzc(adRequestParcel) && !this.zzpe;
    }
}
