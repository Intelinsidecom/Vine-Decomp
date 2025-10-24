package com.google.android.gms.internal;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzes {
    public List<String> zza(JSONObject jSONObject, String str) throws JSONException {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(str);
        if (jSONArrayOptJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(jSONArrayOptJSONArray.length());
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            arrayList.add(jSONArrayOptJSONArray.getString(i));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void zza(Context context, String str, zzie zzieVar, String str2, boolean z, List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String str3 = z ? "1" : "0";
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String strReplaceAll = it.next().replaceAll("@gw_adlocid@", str2).replaceAll("@gw_adnetrefresh@", str3).replaceAll("@gw_qdata@", zzieVar.zzJF.zzAT).replaceAll("@gw_sdkver@", str).replaceAll("@gw_sessid@", com.google.android.gms.ads.internal.zzp.zzbA().getSessionId()).replaceAll("@gw_seqnum@", zzieVar.zzGt);
            if (zzieVar.zzBp != null) {
                strReplaceAll = strReplaceAll.replaceAll("@gw_adnetid@", zzieVar.zzBp.zzAF).replaceAll("@gw_allocid@", zzieVar.zzBp.zzAH);
            }
            new zziw(context, str, strReplaceAll).zzfR();
        }
    }
}
