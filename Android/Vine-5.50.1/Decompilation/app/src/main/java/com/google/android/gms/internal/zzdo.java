package com.google.android.gms.internal;

import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzdo implements zzdl {
    private final zzdp zzyZ;

    public zzdo(zzdp zzdpVar) {
        this.zzyZ = zzdpVar;
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        boolean zEquals = "1".equals(map.get("transparentBackground"));
        boolean zEquals2 = "1".equals(map.get("blur"));
        try {
        } catch (NumberFormatException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Fail to parse float", e);
        }
        float f = map.get("blurRadius") != null ? Float.parseFloat(map.get("blurRadius")) : 0.0f;
        this.zzyZ.zzd(zEquals);
        this.zzyZ.zza(zEquals2, f);
    }
}
