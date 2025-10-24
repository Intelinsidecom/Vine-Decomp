package com.google.android.gms.internal;

import com.google.android.gms.internal.zzer;
import com.google.android.gms.internal.zzey;

@zzha
/* loaded from: classes.dex */
public final class zzep extends zzey.zza {
    private zzer.zza zzBb;
    private zzeo zzBc;
    private final Object zzpK = new Object();

    @Override // com.google.android.gms.internal.zzey
    public void onAdClicked() {
        synchronized (this.zzpK) {
            if (this.zzBc != null) {
                this.zzBc.zzaZ();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void onAdClosed() {
        synchronized (this.zzpK) {
            if (this.zzBc != null) {
                this.zzBc.zzba();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void onAdFailedToLoad(int error) {
        synchronized (this.zzpK) {
            if (this.zzBb != null) {
                this.zzBb.zzr(error == 3 ? 1 : 2);
                this.zzBb = null;
            }
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void onAdLeftApplication() {
        synchronized (this.zzpK) {
            if (this.zzBc != null) {
                this.zzBc.zzbb();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void onAdLoaded() {
        synchronized (this.zzpK) {
            if (this.zzBb != null) {
                this.zzBb.zzr(0);
                this.zzBb = null;
            } else {
                if (this.zzBc != null) {
                    this.zzBc.zzbd();
                }
            }
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void onAdOpened() {
        synchronized (this.zzpK) {
            if (this.zzBc != null) {
                this.zzBc.zzbc();
            }
        }
    }

    public void zza(zzeo zzeoVar) {
        synchronized (this.zzpK) {
            this.zzBc = zzeoVar;
        }
    }

    public void zza(zzer.zza zzaVar) {
        synchronized (this.zzpK) {
            this.zzBb = zzaVar;
        }
    }

    @Override // com.google.android.gms.internal.zzey
    public void zza(zzez zzezVar) {
        synchronized (this.zzpK) {
            if (this.zzBb != null) {
                this.zzBb.zza(0, zzezVar);
                this.zzBb = null;
            } else {
                if (this.zzBc != null) {
                    this.zzBc.zzbd();
                }
            }
        }
    }
}
