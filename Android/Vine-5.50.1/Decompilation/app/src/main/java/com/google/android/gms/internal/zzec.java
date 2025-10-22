package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.zzo;

@zzha
/* loaded from: classes.dex */
class zzec {
    com.google.android.gms.ads.internal.client.zzo zzpz;
    com.google.android.gms.ads.internal.client.zzu zzzM;
    zzgc zzzN;
    zzcl zzzO;
    com.google.android.gms.ads.internal.client.zzn zzzP;

    private class zza extends zzo.zza {
        com.google.android.gms.ads.internal.client.zzo zzzQ;

        zza(com.google.android.gms.ads.internal.client.zzo zzoVar) {
            this.zzzQ = zzoVar;
        }

        @Override // com.google.android.gms.ads.internal.client.zzo
        public void onAdClosed() throws RemoteException {
            this.zzzQ.onAdClosed();
            com.google.android.gms.ads.internal.zzp.zzbI().zzdX();
        }

        @Override // com.google.android.gms.ads.internal.client.zzo
        public void onAdFailedToLoad(int errorCode) throws RemoteException {
            this.zzzQ.onAdFailedToLoad(errorCode);
        }

        @Override // com.google.android.gms.ads.internal.client.zzo
        public void onAdLeftApplication() throws RemoteException {
            this.zzzQ.onAdLeftApplication();
        }

        @Override // com.google.android.gms.ads.internal.client.zzo
        public void onAdLoaded() throws RemoteException {
            this.zzzQ.onAdLoaded();
        }

        @Override // com.google.android.gms.ads.internal.client.zzo
        public void onAdOpened() throws RemoteException {
            this.zzzQ.onAdOpened();
        }
    }

    zzec() {
    }

    void zzc(com.google.android.gms.ads.internal.zzk zzkVar) {
        if (this.zzpz != null) {
            zzkVar.zza(new zza(this.zzpz));
        }
        if (this.zzzM != null) {
            zzkVar.zza(this.zzzM);
        }
        if (this.zzzN != null) {
            zzkVar.zza(this.zzzN);
        }
        if (this.zzzO != null) {
            zzkVar.zza(this.zzzO);
        }
        if (this.zzzP != null) {
            zzkVar.zza(this.zzzP);
        }
    }
}
