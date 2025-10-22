package com.google.android.gms.internal;

@zzha
/* loaded from: classes.dex */
public class zzdu extends zzil {
    final zzjn zzps;
    final zzdw zzzj;
    private final String zzzk;

    zzdu(zzjn zzjnVar, zzdw zzdwVar, String str) {
        this.zzps = zzjnVar;
        this.zzzj = zzdwVar;
        this.zzzk = str;
        com.google.android.gms.ads.internal.zzp.zzbL().zza(this);
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
        this.zzzj.abort();
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() {
        try {
            this.zzzj.zzZ(this.zzzk);
        } finally {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzdu.1
                @Override // java.lang.Runnable
                public void run() {
                    com.google.android.gms.ads.internal.zzp.zzbL().zzb(zzdu.this);
                }
            });
        }
    }
}
