package com.google.android.gms.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzdq implements zzdl {
    final HashMap<String, zzjb<JSONObject>> zzza = new HashMap<>();

    public Future<JSONObject> zzW(String str) {
        zzjb<JSONObject> zzjbVar = new zzjb<>();
        this.zzza.put(str, zzjbVar);
        return zzjbVar;
    }

    public void zzX(String str) {
        zzjb<JSONObject> zzjbVar = this.zzza.get(str);
        if (zzjbVar == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        if (!zzjbVar.isDone()) {
            zzjbVar.cancel(true);
        }
        this.zzza.remove(str);
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        zze(map.get("request_id"), map.get("fetched_ad"));
    }

    public void zze(String str, String str2) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Received ad from the cache.");
        zzjb<JSONObject> zzjbVar = this.zzza.get(str);
        if (zzjbVar == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        try {
            zzjbVar.zzf(new JSONObject(str2));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed constructing JSON object from value passed from javascript", e);
            zzjbVar.zzf(null);
        } finally {
            this.zzza.remove(str);
        }
    }
}
