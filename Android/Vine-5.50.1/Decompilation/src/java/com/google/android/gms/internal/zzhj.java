package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzhi;
import java.util.WeakHashMap;

@zzha
/* loaded from: classes.dex */
public final class zzhj {
    private WeakHashMap<Context, zza> zzIW = new WeakHashMap<>();

    private class zza {
        public final long zzIX = com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis();
        public final zzhi zzIY;

        public zza(zzhi zzhiVar) {
            this.zzIY = zzhiVar;
        }

        public boolean hasExpired() {
            return zzbz.zzwr.get().longValue() + this.zzIX < com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis();
        }
    }

    public zzhi zzE(Context context) {
        zza zzaVar = this.zzIW.get(context);
        zzhi zzhiVarZzgv = (zzaVar == null || zzaVar.hasExpired() || !zzbz.zzwq.get().booleanValue()) ? new zzhi.zza(context).zzgv() : new zzhi.zza(context, zzaVar.zzIY).zzgv();
        this.zzIW.put(context, new zza(zzhiVarZzgv));
        return zzhiVarZzgv;
    }
}
