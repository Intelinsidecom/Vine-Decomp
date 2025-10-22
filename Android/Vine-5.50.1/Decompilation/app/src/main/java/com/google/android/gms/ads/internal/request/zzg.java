package com.google.android.gms.ads.internal.request;

import com.google.android.gms.ads.internal.request.zzc;
import com.google.android.gms.ads.internal.request.zzk;
import com.google.android.gms.internal.zzha;
import java.lang.ref.WeakReference;

@zzha
/* loaded from: classes.dex */
public final class zzg extends zzk.zza {
    private final WeakReference<zzc.zza> zzGL;

    public zzg(zzc.zza zzaVar) {
        this.zzGL = new WeakReference<>(zzaVar);
    }

    @Override // com.google.android.gms.ads.internal.request.zzk
    public void zzb(AdResponseParcel adResponseParcel) {
        zzc.zza zzaVar = this.zzGL.get();
        if (zzaVar != null) {
            zzaVar.zzb(adResponseParcel);
        }
    }
}
