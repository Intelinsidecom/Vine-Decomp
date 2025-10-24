package com.google.android.gms.internal;

import java.util.HashMap;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzcg {
    private final zzch zzoU;
    private final Map<String, zzcf> zzxa = new HashMap();

    public zzcg(zzch zzchVar) {
        this.zzoU = zzchVar;
    }

    public void zza(String str, zzcf zzcfVar) {
        this.zzxa.put(str, zzcfVar);
    }

    public void zza(String str, String str2, long j) {
        zzcd.zza(this.zzoU, this.zzxa.get(str2), j, str);
        this.zzxa.put(str, zzcd.zza(this.zzoU, j));
    }

    public zzch zzdt() {
        return this.zzoU;
    }
}
