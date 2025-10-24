package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzcl;
import com.google.android.gms.internal.zzcx;
import com.google.android.gms.internal.zzcy;
import com.google.android.gms.internal.zzcz;
import com.google.android.gms.internal.zzda;
import com.google.android.gms.internal.zzew;
import com.google.android.gms.internal.zzfa;
import com.google.android.gms.internal.zzfb;
import com.google.android.gms.internal.zzgc;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzip;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzn extends zzb {
    public zzn(Context context, AdSizeParcel adSizeParcel, String str, zzew zzewVar, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, str, zzewVar, versionInfoParcel, null);
    }

    private static com.google.android.gms.ads.internal.formats.zzd zza(zzfa zzfaVar) throws RemoteException {
        return new com.google.android.gms.ads.internal.formats.zzd(zzfaVar.getHeadline(), zzfaVar.getImages(), zzfaVar.getBody(), zzfaVar.zzdD() != null ? zzfaVar.zzdD() : null, zzfaVar.getCallToAction(), zzfaVar.getStarRating(), zzfaVar.getStore(), zzfaVar.getPrice(), null, zzfaVar.getExtras());
    }

    private static com.google.android.gms.ads.internal.formats.zze zza(zzfb zzfbVar) throws RemoteException {
        return new com.google.android.gms.ads.internal.formats.zze(zzfbVar.getHeadline(), zzfbVar.getImages(), zzfbVar.getBody(), zzfbVar.zzdH() != null ? zzfbVar.zzdH() : null, zzfbVar.getCallToAction(), zzfbVar.getAdvertiser(), null, zzfbVar.getExtras());
    }

    private void zza(final com.google.android.gms.ads.internal.formats.zzd zzdVar) {
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzn.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    zzn.this.zzoZ.zzrf.zza(zzdVar);
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call OnAppInstallAdLoadedListener.onAppInstallAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final com.google.android.gms.ads.internal.formats.zze zzeVar) {
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzn.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    zzn.this.zzoZ.zzrg.zza(zzeVar);
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call OnContentAdLoadedListener.onContentAdLoaded().", e);
                }
            }
        });
    }

    private void zza(final zzie zzieVar, final String str) {
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzn.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    zzn.this.zzoZ.zzri.get(str).zza((com.google.android.gms.ads.internal.formats.zzf) zzieVar.zzJJ);
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onCustomTemplateAdLoadedListener.onCustomTemplateAdLoaded().", e);
                }
            }
        });
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void pause() {
        throw new IllegalStateException("Native Ad DOES NOT support pause().");
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.zzg
    public void recordImpression() {
        zza(this.zzoZ.zzqW, false);
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void resume() {
        throw new IllegalStateException("Native Ad DOES NOT support resume().");
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.client.zzs
    public void showInterstitial() {
        throw new IllegalStateException("Interstitial is NOT supported by NativeAdManager.");
    }

    public void zza(SimpleArrayMap<String, zzda> simpleArrayMap) {
        zzx.zzcx("setOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        this.zzoZ.zzri = simpleArrayMap;
    }

    public void zza(com.google.android.gms.ads.internal.formats.zzh zzhVar) {
        if (this.zzoZ.zzqW.zzJE != null) {
            zzp.zzbA().zzgR().zza(this.zzoZ.zzqV, this.zzoZ.zzqW, zzhVar);
        }
    }

    @Override // com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void zza(zzcl zzclVar) {
        throw new IllegalStateException("CustomRendering is NOT supported by NativeAdManager.");
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza, com.google.android.gms.ads.internal.client.zzs
    public void zza(zzgc zzgcVar) {
        throw new IllegalStateException("In App Purchase is NOT supported by NativeAdManager.");
    }

    @Override // com.google.android.gms.ads.internal.zza
    public void zza(final zzie.zza zzaVar, zzch zzchVar) {
        if (zzaVar.zzqV != null) {
            this.zzoZ.zzqV = zzaVar.zzqV;
        }
        if (zzaVar.errorCode != -2) {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.zzn.1
                @Override // java.lang.Runnable
                public void run() {
                    zzn.this.zzb(new zzie(zzaVar, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzoZ.zzrp = 0;
        this.zzoZ.zzqU = zzp.zzbw().zza(this.zzoZ.context, this, zzaVar, this.zzoZ.zzqQ, null, this.zzpd, this, zzchVar);
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("AdRenderer: " + this.zzoZ.zzqU.getClass().getName());
    }

    public void zza(List<String> list) {
        zzx.zzcx("setNativeTemplates must be called on the main UI thread.");
        this.zzoZ.zzrl = list;
    }

    @Override // com.google.android.gms.ads.internal.zzb
    protected boolean zza(AdRequestParcel adRequestParcel, zzie zzieVar, boolean z) {
        return this.zzoY.zzbr();
    }

    @Override // com.google.android.gms.ads.internal.zzb, com.google.android.gms.ads.internal.zza
    protected boolean zza(zzie zzieVar, zzie zzieVar2) {
        zza((List<String>) null);
        if (!this.zzoZ.zzbQ()) {
            throw new IllegalStateException("Native ad DOES NOT have custom rendering mode.");
        }
        if (zzieVar2.zzGN) {
            try {
                zzfa zzfaVarZzeu = zzieVar2.zzBq.zzeu();
                zzfb zzfbVarZzev = zzieVar2.zzBq.zzev();
                if (zzfaVarZzeu != null) {
                    com.google.android.gms.ads.internal.formats.zzd zzdVarZza = zza(zzfaVarZzeu);
                    zzdVarZza.zzb(new com.google.android.gms.ads.internal.formats.zzg(this.zzoZ.context, this, this.zzoZ.zzqQ, zzfaVarZzeu));
                    zza(zzdVarZza);
                } else {
                    if (zzfbVarZzev == null) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaH("No matching mapper for retrieved native ad template.");
                        zzf(0);
                        return false;
                    }
                    com.google.android.gms.ads.internal.formats.zze zzeVarZza = zza(zzfbVarZzev);
                    zzeVarZza.zzb(new com.google.android.gms.ads.internal.formats.zzg(this.zzoZ.context, this, this.zzoZ.zzqQ, zzfbVarZzev));
                    zza(zzeVarZza);
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to get native ad mapper", e);
            }
        } else {
            zzh.zza zzaVar = zzieVar2.zzJJ;
            if ((zzaVar instanceof com.google.android.gms.ads.internal.formats.zze) && this.zzoZ.zzrg != null) {
                zza((com.google.android.gms.ads.internal.formats.zze) zzieVar2.zzJJ);
            } else if ((zzaVar instanceof com.google.android.gms.ads.internal.formats.zzd) && this.zzoZ.zzrf != null) {
                zza((com.google.android.gms.ads.internal.formats.zzd) zzieVar2.zzJJ);
            } else {
                if (!(zzaVar instanceof com.google.android.gms.ads.internal.formats.zzf) || this.zzoZ.zzri == null || this.zzoZ.zzri.get(((com.google.android.gms.ads.internal.formats.zzf) zzaVar).getCustomTemplateId()) == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("No matching listener for retrieved native ad template.");
                    zzf(0);
                    return false;
                }
                zza(zzieVar2, ((com.google.android.gms.ads.internal.formats.zzf) zzaVar).getCustomTemplateId());
            }
        }
        return super.zza(zzieVar, zzieVar2);
    }

    public void zzb(SimpleArrayMap<String, zzcz> simpleArrayMap) {
        zzx.zzcx("setOnCustomClickListener must be called on the main UI thread.");
        this.zzoZ.zzrh = simpleArrayMap;
    }

    public void zzb(NativeAdOptionsParcel nativeAdOptionsParcel) {
        zzx.zzcx("setNativeAdOptions must be called on the main UI thread.");
        this.zzoZ.zzrj = nativeAdOptionsParcel;
    }

    public void zzb(zzcx zzcxVar) {
        zzx.zzcx("setOnAppInstallAdLoadedListener must be called on the main UI thread.");
        this.zzoZ.zzrf = zzcxVar;
    }

    public void zzb(zzcy zzcyVar) {
        zzx.zzcx("setOnContentAdLoadedListener must be called on the main UI thread.");
        this.zzoZ.zzrg = zzcyVar;
    }

    public SimpleArrayMap<String, zzda> zzbq() {
        zzx.zzcx("getOnCustomTemplateAdLoadedListeners must be called on the main UI thread.");
        return this.zzoZ.zzri;
    }

    public zzcz zzr(String str) {
        zzx.zzcx("getOnCustomClickListener must be called on the main UI thread.");
        return this.zzoZ.zzrh.get(str);
    }
}
