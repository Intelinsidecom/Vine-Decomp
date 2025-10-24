package com.google.android.gms.internal;

import android.os.Handler;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzo;
import com.google.android.gms.ads.internal.client.zzu;
import com.google.android.gms.internal.zzcl;
import com.google.android.gms.internal.zzgc;
import java.util.LinkedList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
class zzeb {
    private final List<zza> zzpw = new LinkedList();

    interface zza {
        void zzb(zzec zzecVar) throws RemoteException;
    }

    zzeb() {
    }

    void zza(final zzec zzecVar) {
        Handler handler = zzip.zzKO;
        for (final zza zzaVar : this.zzpw) {
            handler.post(new Runnable() { // from class: com.google.android.gms.internal.zzeb.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        zzaVar.zzb(zzecVar);
                    } catch (RemoteException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not propagate interstitial ad event.", e);
                    }
                }
            });
        }
    }

    void zzc(com.google.android.gms.ads.internal.zzk zzkVar) {
        zzkVar.zza(new zzo.zza() { // from class: com.google.android.gms.internal.zzeb.1
            @Override // com.google.android.gms.ads.internal.client.zzo
            public void onAdClosed() throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.1.1
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzpz != null) {
                            zzecVar.zzpz.onAdClosed();
                        }
                        com.google.android.gms.ads.internal.zzp.zzbI().zzdX();
                    }
                });
            }

            @Override // com.google.android.gms.ads.internal.client.zzo
            public void onAdFailedToLoad(final int errorCode) throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.1.2
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzpz != null) {
                            zzecVar.zzpz.onAdFailedToLoad(errorCode);
                        }
                    }
                });
                com.google.android.gms.ads.internal.util.client.zzb.v("Pooled interstitial failed to load.");
            }

            @Override // com.google.android.gms.ads.internal.client.zzo
            public void onAdLeftApplication() throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.1.3
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzpz != null) {
                            zzecVar.zzpz.onAdLeftApplication();
                        }
                    }
                });
            }

            @Override // com.google.android.gms.ads.internal.client.zzo
            public void onAdLoaded() throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.1.4
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzpz != null) {
                            zzecVar.zzpz.onAdLoaded();
                        }
                    }
                });
                com.google.android.gms.ads.internal.util.client.zzb.v("Pooled interstitial loaded.");
            }

            @Override // com.google.android.gms.ads.internal.client.zzo
            public void onAdOpened() throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.1.5
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzpz != null) {
                            zzecVar.zzpz.onAdOpened();
                        }
                    }
                });
            }
        });
        zzkVar.zza(new zzu.zza() { // from class: com.google.android.gms.internal.zzeb.2
            @Override // com.google.android.gms.ads.internal.client.zzu
            public void onAppEvent(final String name, final String info) throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.2.1
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzzM != null) {
                            zzecVar.zzzM.onAppEvent(name, info);
                        }
                    }
                });
            }
        });
        zzkVar.zza(new zzgc.zza() { // from class: com.google.android.gms.internal.zzeb.3
            @Override // com.google.android.gms.internal.zzgc
            public void zza(final zzgb zzgbVar) throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.3.1
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzzN != null) {
                            zzecVar.zzzN.zza(zzgbVar);
                        }
                    }
                });
            }
        });
        zzkVar.zza(new zzcl.zza() { // from class: com.google.android.gms.internal.zzeb.4
            @Override // com.google.android.gms.internal.zzcl
            public void zza(final zzck zzckVar) throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.4.1
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzzO != null) {
                            zzecVar.zzzO.zza(zzckVar);
                        }
                    }
                });
            }
        });
        zzkVar.zza(new zzn.zza() { // from class: com.google.android.gms.internal.zzeb.5
            @Override // com.google.android.gms.ads.internal.client.zzn
            public void onAdClicked() throws RemoteException {
                zzeb.this.zzpw.add(new zza() { // from class: com.google.android.gms.internal.zzeb.5.1
                    @Override // com.google.android.gms.internal.zzeb.zza
                    public void zzb(zzec zzecVar) throws RemoteException {
                        if (zzecVar.zzzP != null) {
                            zzecVar.zzzP.onAdClicked();
                        }
                    }
                });
            }
        });
    }
}
