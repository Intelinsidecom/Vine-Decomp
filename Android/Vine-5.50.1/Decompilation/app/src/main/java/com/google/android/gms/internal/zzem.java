package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzem {
    public final String zzAE;
    public final String zzAF;
    public final List<String> zzAG;
    public final String zzAH;
    public final String zzAI;
    public final List<String> zzAJ;
    public final List<String> zzAK;
    public final String zzAL;
    public final List<String> zzAM;
    public final List<String> zzAN;

    public zzem(JSONObject jSONObject) throws JSONException {
        this.zzAF = jSONObject.getString("id");
        JSONArray jSONArray = jSONObject.getJSONArray("adapters");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(jSONArray.getString(i));
        }
        this.zzAG = Collections.unmodifiableList(arrayList);
        this.zzAH = jSONObject.optString("allocation_id", null);
        this.zzAJ = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObject, "clickurl");
        this.zzAK = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObject, "imp_urls");
        this.zzAM = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObject, "video_start_urls");
        this.zzAN = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObject, "video_complete_urls");
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("ad");
        this.zzAE = jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.toString() : null;
        JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("data");
        this.zzAL = jSONObjectOptJSONObject2 != null ? jSONObjectOptJSONObject2.toString() : null;
        this.zzAI = jSONObjectOptJSONObject2 != null ? jSONObjectOptJSONObject2.optString("class_name") : null;
    }
}
