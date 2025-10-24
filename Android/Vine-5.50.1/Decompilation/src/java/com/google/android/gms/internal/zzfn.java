package com.google.android.gms.internal;

import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzfn {
    private final boolean zzCp;
    private final String zzCq;
    private final zzjn zzps;

    public zzfn(zzjn zzjnVar, Map<String, String> map) {
        this.zzps = zzjnVar;
        this.zzCq = map.get("forceOrientation");
        if (map.containsKey("allowOrientationChange")) {
            this.zzCp = Boolean.parseBoolean(map.get("allowOrientationChange"));
        } else {
            this.zzCp = true;
        }
    }

    public void execute() {
        if (this.zzps == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdWebView is null");
        } else {
            this.zzps.setRequestedOrientation("portrait".equalsIgnoreCase(this.zzCq) ? com.google.android.gms.ads.internal.zzp.zzbz().zzhe() : "landscape".equalsIgnoreCase(this.zzCq) ? com.google.android.gms.ads.internal.zzp.zzbz().zzhd() : this.zzCp ? -1 : com.google.android.gms.ads.internal.zzp.zzbz().zzhf());
        }
    }
}
