package com.google.android.gms.internal;

import com.google.android.gms.internal.zzei;
import java.util.Map;
import java.util.concurrent.Future;

@zzha
/* loaded from: classes.dex */
public final class zzhe {
    private String zzDX;
    private String zzHV;
    zzei.zzd zzHX;
    zzjn zzps;
    private final Object zzpK = new Object();
    private zzjb<zzhh> zzHW = new zzjb<>();
    public final zzdl zzHY = new zzdl() { // from class: com.google.android.gms.internal.zzhe.1
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            synchronized (zzhe.this.zzpK) {
                if (zzhe.this.zzHW.isDone()) {
                    return;
                }
                if (zzhe.this.zzDX.equals(map.get("request_id"))) {
                    zzhh zzhhVar = new zzhh(1, map);
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Invalid " + zzhhVar.getType() + " request error: " + zzhhVar.zzgr());
                    zzhe.this.zzHW.zzf(zzhhVar);
                }
            }
        }
    };
    public final zzdl zzHZ = new zzdl() { // from class: com.google.android.gms.internal.zzhe.2
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            synchronized (zzhe.this.zzpK) {
                if (zzhe.this.zzHW.isDone()) {
                    return;
                }
                zzhh zzhhVar = new zzhh(-2, map);
                if (!zzhe.this.zzDX.equals(zzhhVar.getRequestId())) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH(zzhhVar.getRequestId() + " ==== " + zzhe.this.zzDX);
                    return;
                }
                String url = zzhhVar.getUrl();
                if (url == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("URL missing in loadAdUrl GMSG.");
                    return;
                }
                if (url.contains("%40mediation_adapters%40")) {
                    String strReplaceAll = url.replaceAll("%40mediation_adapters%40", zzik.zza(zzjnVar.getContext(), map.get("check_adapters"), zzhe.this.zzHV));
                    zzhhVar.setUrl(strReplaceAll);
                    com.google.android.gms.ads.internal.util.client.zzb.v("Ad request URL modified to " + strReplaceAll);
                }
                zzhe.this.zzHW.zzf(zzhhVar);
            }
        }
    };

    public zzhe(String str, String str2) {
        this.zzHV = str2;
        this.zzDX = str;
    }

    public void zzb(zzei.zzd zzdVar) {
        this.zzHX = zzdVar;
    }

    public void zze(zzjn zzjnVar) {
        this.zzps = zzjnVar;
    }

    public zzei.zzd zzgo() {
        return this.zzHX;
    }

    public Future<zzhh> zzgp() {
        return this.zzHW;
    }

    public void zzgq() {
        if (this.zzps != null) {
            this.zzps.destroy();
            this.zzps = null;
        }
    }
}
