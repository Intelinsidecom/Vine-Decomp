package com.google.android.gms.internal;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzbg implements zzbf {
    private final zzbe zzsA;
    private final HashSet<AbstractMap.SimpleEntry<String, zzdl>> zzsB = new HashSet<>();

    public zzbg(zzbe zzbeVar) {
        this.zzsA = zzbeVar;
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(String str, zzdl zzdlVar) {
        this.zzsA.zza(str, zzdlVar);
        this.zzsB.add(new AbstractMap.SimpleEntry<>(str, zzdlVar));
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(String str, String str2) {
        this.zzsA.zza(str, str2);
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zza(String str, JSONObject jSONObject) {
        this.zzsA.zza(str, jSONObject);
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zzb(String str, zzdl zzdlVar) {
        this.zzsA.zzb(str, zzdlVar);
        this.zzsB.remove(new AbstractMap.SimpleEntry(str, zzdlVar));
    }

    @Override // com.google.android.gms.internal.zzbe
    public void zzb(String str, JSONObject jSONObject) {
        this.zzsA.zzb(str, jSONObject);
    }

    @Override // com.google.android.gms.internal.zzbf
    public void zzcs() {
        Iterator<AbstractMap.SimpleEntry<String, zzdl>> it = this.zzsB.iterator();
        while (it.hasNext()) {
            AbstractMap.SimpleEntry<String, zzdl> next = it.next();
            com.google.android.gms.ads.internal.util.client.zzb.v("Unregistering eventhandler: " + next.getValue().toString());
            this.zzsA.zzb(next.getKey(), next.getValue());
        }
        this.zzsB.clear();
    }
}
