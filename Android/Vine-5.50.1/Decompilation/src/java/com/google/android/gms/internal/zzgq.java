package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.internal.zzie;

@zzha
/* loaded from: classes.dex */
public class zzgq {

    public interface zza {
        void zzb(zzie zzieVar);
    }

    public zzir zza(Context context, com.google.android.gms.ads.internal.zza zzaVar, zzie.zza zzaVar2, zzan zzanVar, zzjn zzjnVar, zzew zzewVar, zza zzaVar3, zzch zzchVar) {
        zzir zzgoVar;
        AdResponseParcel adResponseParcel = zzaVar2.zzJL;
        if (adResponseParcel.zzGN) {
            zzgoVar = new zzgt(context, zzaVar2, zzewVar, zzaVar3, zzchVar);
        } else if (!adResponseParcel.zztY) {
            zzgoVar = adResponseParcel.zzGT ? new zzgo(context, zzaVar2, zzjnVar, zzaVar3) : (zzbz.zzvZ.get().booleanValue() && zznx.zzrU() && !zznx.isAtLeastL() && zzjnVar.zzaP().zztW) ? new zzgs(context, zzaVar2, zzjnVar, zzaVar3) : new zzgr(context, zzaVar2, zzjnVar, zzaVar3);
        } else {
            if (!(zzaVar instanceof com.google.android.gms.ads.internal.zzn)) {
                throw new IllegalArgumentException("Invalid NativeAdManager type. Found: " + (zzaVar != null ? zzaVar.getClass().getName() : "null") + "; Required: NativeAdManager.");
            }
            zzgoVar = new zzgu(context, (com.google.android.gms.ads.internal.zzn) zzaVar, new zzbc(), zzaVar2, zzanVar, zzaVar3);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("AdRenderer: " + zzgoVar.getClass().getName());
        zzgoVar.zzfR();
        return zzgoVar;
    }
}
