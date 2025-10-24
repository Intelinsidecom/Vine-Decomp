package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.Correlator;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.internal.zzcm;
import com.google.android.gms.internal.zzev;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzgl;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzaa {
    private final Context mContext;
    private final zzh zzoq;
    private String zzpH;
    private zza zztn;
    private AdListener zzto;
    private PlayStorePurchaseListener zzuA;
    private OnCustomRenderedAdLoadedListener zzuB;
    private Correlator zzuC;
    private PublisherInterstitialAd zzuE;
    private AppEventListener zzub;
    private final zzev zzuu;
    private zzs zzuw;
    private String zzux;
    private InAppPurchaseListener zzuz;

    public zzaa(Context context) {
        this(context, zzh.zzcJ(), null);
    }

    public zzaa(Context context, zzh zzhVar, PublisherInterstitialAd publisherInterstitialAd) {
        this.zzuu = new zzev();
        this.mContext = context;
        this.zzoq = zzhVar;
        this.zzuE = publisherInterstitialAd;
    }

    private void zzM(String str) throws RemoteException {
        if (this.zzpH == null) {
            zzN(str);
        }
        this.zzuw = zzl.zzcO().zzb(this.mContext, new AdSizeParcel(), this.zzpH, this.zzuu);
        if (this.zzto != null) {
            this.zzuw.zza(new zzc(this.zzto));
        }
        if (this.zztn != null) {
            this.zzuw.zza(new zzb(this.zztn));
        }
        if (this.zzub != null) {
            this.zzuw.zza(new zzj(this.zzub));
        }
        if (this.zzuz != null) {
            this.zzuw.zza(new zzgh(this.zzuz));
        }
        if (this.zzuA != null) {
            this.zzuw.zza(new zzgl(this.zzuA), this.zzux);
        }
        if (this.zzuB != null) {
            this.zzuw.zza(new zzcm(this.zzuB));
        }
        if (this.zzuC != null) {
            this.zzuw.zza(this.zzuC.zzaH());
        }
    }

    private void zzN(String str) {
        if (this.zzuw == null) {
            throw new IllegalStateException("The ad unit ID must be set on InterstitialAd before " + str + " is called.");
        }
    }

    public void setAdListener(AdListener adListener) {
        try {
            this.zzto = adListener;
            if (this.zzuw != null) {
                this.zzuw.zza(adListener != null ? new zzc(adListener) : null);
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to set the AdListener.", e);
        }
    }

    public void setAdUnitId(String adUnitId) {
        if (this.zzpH != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on InterstitialAd.");
        }
        this.zzpH = adUnitId;
    }

    public void show() {
        try {
            zzN("show");
            this.zzuw.showInterstitial();
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to show interstitial.", e);
        }
    }

    public void zza(zza zzaVar) {
        try {
            this.zztn = zzaVar;
            if (this.zzuw != null) {
                this.zzuw.zza(zzaVar != null ? new zzb(zzaVar) : null);
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to set the AdClickListener.", e);
        }
    }

    public void zza(zzy zzyVar) {
        try {
            if (this.zzuw == null) {
                zzM("loadAd");
            }
            if (this.zzuw.zzb(this.zzoq.zza(this.mContext, zzyVar))) {
                this.zzuu.zze(zzyVar.zzcV());
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to load ad.", e);
        }
    }
}
