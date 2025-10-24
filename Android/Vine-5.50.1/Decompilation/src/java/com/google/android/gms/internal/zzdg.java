package com.google.android.gms.internal;

import java.util.Map;

@zzha
/* loaded from: classes.dex */
public final class zzdg implements zzdl {
    private final zzdh zzyy;

    public zzdg(zzdh zzdhVar) {
        this.zzyy = zzdhVar;
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("name");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("App event with no name parameter.");
        } else {
            this.zzyy.onAppEvent(str, map.get("info"));
        }
    }
}
