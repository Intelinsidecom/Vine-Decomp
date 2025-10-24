package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjn;

@zzha
/* loaded from: classes.dex */
public class zzl extends zzj {
    @Override // com.google.android.gms.ads.internal.overlay.zzj
    public zzi zza(Context context, zzjn zzjnVar, int i, zzch zzchVar, zzcf zzcfVar) {
        if (zzx(context)) {
            return new zzc(context, new zzp(context, zzjnVar.zzhF(), zzjnVar.getRequestId(), zzchVar, zzcfVar));
        }
        return null;
    }
}
