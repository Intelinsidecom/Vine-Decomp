package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.AdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;

@zzha
/* loaded from: classes.dex */
public final class zzfj<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    private final zzey zzBK;

    public zzfj(zzey zzeyVar) {
        this.zzBK = zzeyVar;
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onClick(MediationBannerAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onClick.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onClick must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdClicked();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClicked.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdClicked();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClicked.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onDismissScreen(MediationBannerAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onDismissScreen.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onDismissScreen must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdClosed();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClosed.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdClosed();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClosed.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onDismissScreen(MediationInterstitialAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onDismissScreen.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onDismissScreen must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.9
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdClosed();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClosed.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdClosed();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdClosed.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> adapter, final AdRequest.ErrorCode errorCode) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onFailedToReceiveAd must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.5
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdFailedToLoad(zzfk.zza(errorCode));
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdFailedToLoad.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdFailedToLoad(zzfk.zza(errorCode));
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> adapter, final AdRequest.ErrorCode errorCode) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onFailedToReceiveAd must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.10
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdFailedToLoad(zzfk.zza(errorCode));
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdFailedToLoad.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdFailedToLoad(zzfk.zza(errorCode));
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdFailedToLoad.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onLeaveApplication(MediationBannerAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onLeaveApplication.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onLeaveApplication must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdLeftApplication();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLeftApplication.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdLeftApplication();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLeftApplication.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onLeaveApplication.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onLeaveApplication must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.11
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdLeftApplication();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLeftApplication.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdLeftApplication();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLeftApplication.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onPresentScreen(MediationBannerAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onPresentScreen.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onPresentScreen must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.7
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdOpened();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdOpened.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdOpened();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdOpened.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onPresentScreen(MediationInterstitialAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onPresentScreen.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onPresentScreen must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdOpened();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdOpened.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdOpened();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdOpened.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onReceivedAd(MediationBannerAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onReceivedAd.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onReceivedAd must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.8
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdLoaded();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLoaded.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdLoaded();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLoaded.", e);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onReceivedAd(MediationInterstitialAdapter<?, ?> adapter) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter called onReceivedAd.");
        if (!com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("onReceivedAd must be called on the main UI thread.");
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.internal.zzfj.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzfj.this.zzBK.onAdLoaded();
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLoaded.", e);
                    }
                }
            });
        } else {
            try {
                this.zzBK.onAdLoaded();
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call onAdLoaded.", e);
            }
        }
    }
}
