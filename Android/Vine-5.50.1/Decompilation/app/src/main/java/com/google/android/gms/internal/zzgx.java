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
public class zzgx implements zzgv.zza<com.google.android.gms.ads.internal.formats.zze> {
    private final boolean zzFZ;
    private final boolean zzGa;

    public zzgx(boolean z, boolean z2) {
        this.zzFZ = z;
        this.zzGa = z2;
    }

    @Override // com.google.android.gms.internal.zzgv.zza
    /* renamed from: zzc, reason: merged with bridge method [inline-methods] */
    public com.google.android.gms.ads.internal.formats.zze zza(zzgv zzgvVar, JSONObject jSONObject) throws ExecutionException, JSONException, InterruptedException {
        List<zzje<com.google.android.gms.ads.internal.formats.zzc>> listZza = zzgvVar.zza(jSONObject, "images", true, this.zzFZ, this.zzGa);
        zzje<com.google.android.gms.ads.internal.formats.zzc> zzjeVarZza = zzgvVar.zza(jSONObject, "secondary_image", false, this.zzFZ);
        zzje<com.google.android.gms.ads.internal.formats.zza> zzjeVarZze = zzgvVar.zze(jSONObject);
        ArrayList arrayList = new ArrayList();
        Iterator<zzje<com.google.android.gms.ads.internal.formats.zzc>> it = listZza.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().get());
        }
        return new com.google.android.gms.ads.internal.formats.zze(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), zzjeVarZza.get(), jSONObject.getString("call_to_action"), jSONObject.getString("advertiser"), zzjeVarZze.get(), new Bundle());
    }
}
