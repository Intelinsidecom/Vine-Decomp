package com.google.android.gms.internal;

import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.internal.zzgv;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzgy implements zzgv.zza<com.google.android.gms.ads.internal.formats.zzf> {
    private final boolean zzFZ;

    public zzgy(boolean z) {
        this.zzFZ = z;
    }

    private void zza(zzgv zzgvVar, JSONObject jSONObject, SimpleArrayMap<String, Future<com.google.android.gms.ads.internal.formats.zzc>> simpleArrayMap) throws JSONException {
        simpleArrayMap.put(jSONObject.getString("name"), zzgvVar.zza(jSONObject, "image_value", this.zzFZ));
    }

    private void zza(JSONObject jSONObject, SimpleArrayMap<String, String> simpleArrayMap) throws JSONException {
        simpleArrayMap.put(jSONObject.getString("name"), jSONObject.getString("string_value"));
    }

    private <K, V> SimpleArrayMap<K, V> zzc(SimpleArrayMap<K, Future<V>> simpleArrayMap) throws ExecutionException, InterruptedException {
        SimpleArrayMap<K, V> simpleArrayMap2 = new SimpleArrayMap<>();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= simpleArrayMap.size()) {
                return simpleArrayMap2;
            }
            simpleArrayMap2.put(simpleArrayMap.keyAt(i2), simpleArrayMap.valueAt(i2).get());
            i = i2 + 1;
        }
    }

    @Override // com.google.android.gms.internal.zzgv.zza
    /* renamed from: zzd, reason: merged with bridge method [inline-methods] */
    public com.google.android.gms.ads.internal.formats.zzf zza(zzgv zzgvVar, JSONObject jSONObject) throws ExecutionException, JSONException, InterruptedException {
        SimpleArrayMap<String, Future<com.google.android.gms.ads.internal.formats.zzc>> simpleArrayMap = new SimpleArrayMap<>();
        SimpleArrayMap<String, String> simpleArrayMap2 = new SimpleArrayMap<>();
        zzje<com.google.android.gms.ads.internal.formats.zza> zzjeVarZze = zzgvVar.zze(jSONObject);
        JSONArray jSONArray = jSONObject.getJSONArray("custom_assets");
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            String string = jSONObject2.getString("type");
            if ("string".equals(string)) {
                zza(jSONObject2, simpleArrayMap2);
            } else if ("image".equals(string)) {
                zza(zzgvVar, jSONObject2, simpleArrayMap);
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unknown custom asset type: " + string);
            }
        }
        return new com.google.android.gms.ads.internal.formats.zzf(jSONObject.getString("custom_template_id"), zzc(simpleArrayMap), simpleArrayMap2, zzjeVarZze.get());
    }
}
