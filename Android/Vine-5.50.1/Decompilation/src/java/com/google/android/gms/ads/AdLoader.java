package com.google.android.gms.ads;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.internal.client.zzc;
import com.google.android.gms.ads.internal.client.zzd;
import com.google.android.gms.ads.internal.client.zzh;
import com.google.android.gms.ads.internal.client.zzp;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzy;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzdc;
import com.google.android.gms.internal.zzdd;
import com.google.android.gms.internal.zzev;

/* loaded from: classes.dex */
public class AdLoader {
    private final Context mContext;
    private final zzh zzoq;
    private final zzp zzor;

    public static class Builder {
        private final Context mContext;
        private final zzq zzos;

        Builder(Context context, zzq builder) {
            this.mContext = context;
            this.zzos = builder;
        }

        public Builder(Context context, String adUnitID) {
            this((Context) zzx.zzb(context, "context cannot be null"), zzd.zza(context, adUnitID, new zzev()));
        }

        public AdLoader build() {
            try {
                return new AdLoader(this.mContext, this.zzos.zzbm());
            } catch (RemoteException e) {
                zzb.zzb("Failed to build AdLoader.", e);
                return null;
            }
        }

        public Builder forAppInstallAd(NativeAppInstallAd.OnAppInstallAdLoadedListener listener) {
            try {
                this.zzos.zza(new zzdc(listener));
            } catch (RemoteException e) {
                zzb.zzd("Failed to add app install ad listener", e);
            }
            return this;
        }

        public Builder forContentAd(NativeContentAd.OnContentAdLoadedListener listener) {
            try {
                this.zzos.zza(new zzdd(listener));
            } catch (RemoteException e) {
                zzb.zzd("Failed to add content ad listener", e);
            }
            return this;
        }

        public Builder withAdListener(AdListener listener) {
            try {
                this.zzos.zzb(new zzc(listener));
            } catch (RemoteException e) {
                zzb.zzd("Failed to set AdListener.", e);
            }
            return this;
        }

        public Builder withNativeAdOptions(NativeAdOptions options) {
            try {
                this.zzos.zza(new NativeAdOptionsParcel(options));
            } catch (RemoteException e) {
                zzb.zzd("Failed to specify native ad options", e);
            }
            return this;
        }
    }

    AdLoader(Context context, zzp adLoader) {
        this(context, adLoader, zzh.zzcJ());
    }

    AdLoader(Context context, zzp adLoader, zzh parcelFactory) {
        this.mContext = context;
        this.zzor = adLoader;
        this.zzoq = parcelFactory;
    }

    private void zza(zzy zzyVar) {
        try {
            this.zzor.zzf(this.zzoq.zza(this.mContext, zzyVar));
        } catch (RemoteException e) {
            zzb.zzb("Failed to load ad.", e);
        }
    }

    public void loadAd(AdRequest adRequest) {
        zza(adRequest.zzaG());
    }
}
