package com.google.android.gms.internal;

import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzdx implements zzdl {
    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) throws NumberFormatException {
        int i;
        zzdv zzdvVarZzbL = com.google.android.gms.ads.internal.zzp.zzbL();
        if (map.containsKey("abort")) {
            if (zzdvVarZzbL.zza(zzjnVar)) {
                return;
            }
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Precache abort but no preload task running.");
            return;
        }
        String str = map.get("src");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Precache video action is missing the src parameter.");
            return;
        }
        try {
            i = Integer.parseInt(map.get("player"));
        } catch (NumberFormatException e) {
            i = 0;
        }
        String str2 = map.containsKey("mimetype") ? map.get("mimetype") : "";
        if (zzdvVarZzbL.zzb(zzjnVar)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Precache task already running.");
        } else {
            com.google.android.gms.common.internal.zzb.zzu(zzjnVar.zzhz());
            new zzdu(zzjnVar, zzjnVar.zzhz().zzpm.zza(zzjnVar, i, str2), str).zzfR();
        }
    }
}
