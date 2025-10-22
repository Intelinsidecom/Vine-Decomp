package com.google.android.gms.internal;

import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzdr implements zzdl {
    static final Map<String, Integer> zzzd = zznm.zza("resize", 1, "playVideo", 2, "storePicture", 3, "createCalendarEvent", 4, "setOrientationProperties", 5, "closeResizedAd", 6);
    private final com.google.android.gms.ads.internal.zze zzzb;
    private final zzfm zzzc;

    public zzdr(com.google.android.gms.ads.internal.zze zzeVar, zzfm zzfmVar) {
        this.zzzb = zzeVar;
        this.zzzc = zzfmVar;
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        int iIntValue = zzzd.get(map.get("a")).intValue();
        if (iIntValue != 5 && this.zzzb != null && !this.zzzb.zzbg()) {
            this.zzzb.zzp(null);
        }
        switch (iIntValue) {
            case 1:
                this.zzzc.zzg(map);
                break;
            case 2:
            default:
                com.google.android.gms.ads.internal.util.client.zzb.zzaG("Unknown MRAID command called.");
                break;
            case 3:
                new zzfo(zzjnVar, map).execute();
                break;
            case 4:
                new zzfl(zzjnVar, map).execute();
                break;
            case 5:
                new zzfn(zzjnVar, map).execute();
                break;
            case 6:
                this.zzzc.zzp(true);
                break;
        }
    }
}
