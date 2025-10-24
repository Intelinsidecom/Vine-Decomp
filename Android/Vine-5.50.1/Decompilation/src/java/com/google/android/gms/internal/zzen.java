package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzen {
    public final List<zzem> zzAO;
    public final long zzAP;
    public final List<String> zzAQ;
    public final List<String> zzAR;
    public final List<String> zzAS;
    public final String zzAT;
    public final long zzAU;
    public final String zzAV;
    public final int zzAW;
    public final int zzAX;
    public final long zzAY;
    public int zzAZ;
    public int zzBa;

    public zzen(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Mediation Response JSON: " + jSONObject.toString(2));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        int i = -1;
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            zzem zzemVar = new zzem(jSONArray.getJSONObject(i2));
            arrayList.add(zzemVar);
            if (i < 0 && zza(zzemVar)) {
                i = i2;
            }
        }
        this.zzAZ = i;
        this.zzBa = jSONArray.length();
        this.zzAO = Collections.unmodifiableList(arrayList);
        this.zzAT = jSONObject.getString("qdata");
        this.zzAX = jSONObject.optInt("fs_model_type", -1);
        this.zzAY = jSONObject.optLong("timeout_ms", -1L);
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("settings");
        if (jSONObjectOptJSONObject == null) {
            this.zzAP = -1L;
            this.zzAQ = null;
            this.zzAR = null;
            this.zzAS = null;
            this.zzAU = -1L;
            this.zzAV = null;
            this.zzAW = 0;
            return;
        }
        this.zzAP = jSONObjectOptJSONObject.optLong("ad_network_timeout_millis", -1L);
        this.zzAQ = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObjectOptJSONObject, "click_urls");
        this.zzAR = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObjectOptJSONObject, "imp_urls");
        this.zzAS = com.google.android.gms.ads.internal.zzp.zzbK().zza(jSONObjectOptJSONObject, "nofill_urls");
        long jOptLong = jSONObjectOptJSONObject.optLong("refresh", -1L);
        this.zzAU = jOptLong > 0 ? jOptLong * 1000 : -1L;
        JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("rewards");
        if (jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() == 0) {
            this.zzAV = null;
            this.zzAW = 0;
        } else {
            this.zzAV = jSONArrayOptJSONArray.getJSONObject(0).optString("rb_type");
            this.zzAW = jSONArrayOptJSONArray.getJSONObject(0).optInt("rb_amount");
        }
    }

    private boolean zza(zzem zzemVar) {
        Iterator<String> it = zzemVar.zzAG.iterator();
        while (it.hasNext()) {
            if (it.next().equals("com.google.ads.mediation.admob.AdMobAdapter")) {
                return true;
            }
        }
        return false;
    }
}
