package com.google.android.gms.internal;

import android.content.MutableContextWrapper;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import java.util.Iterator;
import java.util.LinkedList;

@zzha
/* loaded from: classes.dex */
class zzef {
    private final String zzpH;
    private AdRequestParcel zzqo;
    private final LinkedList<zza> zzzW;

    class zza {
        boolean zzAa;
        boolean zzAb;
        com.google.android.gms.ads.internal.zzk zzzX;
        zzeb zzzY;
        long zzzZ;
        MutableContextWrapper zzzz;

        zza(zzea zzeaVar) {
            zzea zzeaVarZzdV = zzeaVar.zzdV();
            this.zzzz = zzeaVar.zzdW();
            this.zzzX = zzeaVarZzdV.zzac(zzef.this.zzpH);
            this.zzzY = new zzeb();
            this.zzzY.zzc(this.zzzX);
        }

        private void zzed() {
            if (this.zzAa || zzef.this.zzqo == null) {
                return;
            }
            this.zzAb = this.zzzX.zzb(zzef.this.zzqo);
            this.zzAa = true;
            this.zzzZ = com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis();
        }

        void zzc(zzea zzeaVar) {
            this.zzzz.setBaseContext(zzeaVar.zzdW().getBaseContext());
        }

        void zzh(AdRequestParcel adRequestParcel) {
            if (adRequestParcel != null) {
                zzef.this.zzqo = adRequestParcel;
            }
            zzed();
            Iterator it = zzef.this.zzzW.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzed();
            }
        }
    }

    zzef(AdRequestParcel adRequestParcel, String str) {
        com.google.android.gms.common.internal.zzx.zzy(adRequestParcel);
        com.google.android.gms.common.internal.zzx.zzy(str);
        this.zzzW = new LinkedList<>();
        this.zzqo = adRequestParcel;
        this.zzpH = str;
    }

    String getAdUnitId() {
        return this.zzpH;
    }

    int size() {
        return this.zzzW.size();
    }

    void zzb(zzea zzeaVar) {
        zza zzaVar = new zza(zzeaVar);
        this.zzzW.add(zzaVar);
        zzaVar.zzh(this.zzqo);
    }

    AdRequestParcel zzeb() {
        return this.zzqo;
    }

    zza zzec() {
        return this.zzzW.remove();
    }
}
