package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.internal.zzgv;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzgw implements zzgv.zza<com.google.android.gms.ads.internal.formats.zzd> {
    private final boolean zzFZ;
    private final boolean zzGa;

    public zzgw(boolean z, boolean z2) {
        this.zzFZ = z;
        this.zzGa = z2;
    }

    @Override // com.google.android.gms.internal.zzgv.zza
    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public com.google.android.gms.ads.internal.formats.zzd zza(zzgv zzgvVar, JSONObject jSONObject) throws ExecutionException, JSONException, InterruptedException {
        List<zzje<com.google.android.gms.ads.internal.formats.zzc>> listZza = zzgvVar.zza(jSONObject, "images", true, this.zzFZ, this.zzGa);
        zzje<com.google.android.gms.ads.internal.formats.zzc> zzjeVarZza = zzgvVar.zza(jSONObject, "app_icon", true, this.zzFZ);
        zzje<com.google.android.gms.ads.internal.formats.zza> zzjeVarZze = zzgvVar.zze(jSONObject);
        ArrayList arrayList = new ArrayList();
        Iterator<zzje<com.google.android.gms.ads.internal.formats.zzc>> it = listZza.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().get());
        }
        return new com.google.android.gms.ads.internal.formats.zzd(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), zzjeVarZza.get(), jSONObject.getString("call_to_action"), jSONObject.optDouble("rating", -1.0d), jSONObject.optString("store"), jSONObject.optString("price"), zzjeVarZze.get(), new Bundle());
    }
}
