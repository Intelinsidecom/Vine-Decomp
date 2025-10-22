package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzdk {
    public static final zzdl zzyz = new zzdl() { // from class: com.google.android.gms.internal.zzdk.1
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
        }
    };
    public static final zzdl zzyA = new zzdl() { // from class: com.google.android.gms.internal.zzdk.3
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            String str = map.get("urls");
            if (TextUtils.isEmpty(str)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] strArrSplit = str.split(",");
            HashMap map2 = new HashMap();
            PackageManager packageManager = zzjnVar.getContext().getPackageManager();
            for (String str2 : strArrSplit) {
                String[] strArrSplit2 = str2.split(";", 2);
                map2.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(strArrSplit2.length > 1 ? strArrSplit2[1].trim() : "android.intent.action.VIEW", Uri.parse(strArrSplit2[0].trim())), 65536) != null));
            }
            zzjnVar.zzb("openableURLs", map2);
        }
    };
    public static final zzdl zzyB = new zzdl() { // from class: com.google.android.gms.internal.zzdk.4
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) throws JSONException {
            PackageManager packageManager = zzjnVar.getContext().getPackageManager();
            try {
                try {
                    JSONArray jSONArray = new JSONObject(map.get("data")).getJSONArray("intents");
                    JSONObject jSONObject = new JSONObject();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            String strOptString = jSONObject2.optString("id");
                            String strOptString2 = jSONObject2.optString("u");
                            String strOptString3 = jSONObject2.optString("i");
                            String strOptString4 = jSONObject2.optString("m");
                            String strOptString5 = jSONObject2.optString("p");
                            String strOptString6 = jSONObject2.optString("c");
                            jSONObject2.optString("f");
                            jSONObject2.optString("e");
                            Intent intent = new Intent();
                            if (!TextUtils.isEmpty(strOptString2)) {
                                intent.setData(Uri.parse(strOptString2));
                            }
                            if (!TextUtils.isEmpty(strOptString3)) {
                                intent.setAction(strOptString3);
                            }
                            if (!TextUtils.isEmpty(strOptString4)) {
                                intent.setType(strOptString4);
                            }
                            if (!TextUtils.isEmpty(strOptString5)) {
                                intent.setPackage(strOptString5);
                            }
                            if (!TextUtils.isEmpty(strOptString6)) {
                                String[] strArrSplit = strOptString6.split("/", 2);
                                if (strArrSplit.length == 2) {
                                    intent.setComponent(new ComponentName(strArrSplit[0], strArrSplit[1]));
                                }
                            }
                            try {
                                jSONObject.put(strOptString, packageManager.resolveActivity(intent, 65536) != null);
                            } catch (JSONException e) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error constructing openable urls response.", e);
                            }
                        } catch (JSONException e2) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error parsing the intent data.", e2);
                        }
                    }
                    zzjnVar.zzb("openableIntents", jSONObject);
                } catch (JSONException e3) {
                    zzjnVar.zzb("openableIntents", new JSONObject());
                }
            } catch (JSONException e4) {
                zzjnVar.zzb("openableIntents", new JSONObject());
            }
        }
    };
    public static final zzdl zzyC = new zzdl() { // from class: com.google.android.gms.internal.zzdk.5
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            zzan zzanVarZzhE;
            String str = map.get("u");
            if (str == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("URL missing from click GMSG.");
                return;
            }
            Uri uri = Uri.parse(str);
            try {
                zzanVarZzhE = zzjnVar.zzhE();
            } catch (zzao e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unable to append parameter to URL: " + str);
            }
            Uri uriZza = (zzanVarZzhE == null || !zzanVarZzhE.zzb(uri)) ? uri : zzanVarZzhE.zza(uri, zzjnVar.getContext());
            new zziw(zzjnVar.getContext(), zzjnVar.zzhF().afmaVersion, uriZza.toString()).zzfR();
        }
    };
    public static final zzdl zzyD = new zzdl() { // from class: com.google.android.gms.internal.zzdk.6
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = zzjnVar.zzhA();
            if (zzdVarZzhA != null) {
                zzdVarZzhA.close();
                return;
            }
            com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhB = zzjnVar.zzhB();
            if (zzdVarZzhB != null) {
                zzdVarZzhB.close();
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("A GMSG tried to close something that wasn't an overlay.");
            }
        }
    };
    public static final zzdl zzyE = new zzdl() { // from class: com.google.android.gms.internal.zzdk.7
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            zzjnVar.zzE("1".equals(map.get("custom_close")));
        }
    };
    public static final zzdl zzyF = new zzdl() { // from class: com.google.android.gms.internal.zzdk.8
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            String str = map.get("u");
            if (str == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("URL missing from httpTrack GMSG.");
            } else {
                new zziw(zzjnVar.getContext(), zzjnVar.zzhF().afmaVersion, str).zzfR();
            }
        }
    };
    public static final zzdl zzyG = new zzdl() { // from class: com.google.android.gms.internal.zzdk.9
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Received log message: " + map.get("string"));
        }
    };
    public static final zzdl zzyH = new zzdl() { // from class: com.google.android.gms.internal.zzdk.10
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) throws NumberFormatException {
            String str = map.get("tx");
            String str2 = map.get("ty");
            String str3 = map.get("td");
            try {
                int i = Integer.parseInt(str);
                int i2 = Integer.parseInt(str2);
                int i3 = Integer.parseInt(str3);
                zzan zzanVarZzhE = zzjnVar.zzhE();
                if (zzanVarZzhE != null) {
                    zzanVarZzhE.zzac().zza(i, i2, i3);
                }
            } catch (NumberFormatException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final zzdl zzyI = new zzdl() { // from class: com.google.android.gms.internal.zzdk.2
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            if (zzbz.zzwy.get().booleanValue()) {
                zzjnVar.zzF(!Boolean.parseBoolean(map.get("disabled")));
            }
        }
    };
    public static final zzdl zzyJ = new zzdt();
    public static final zzdl zzyK = new zzdx();
    public static final zzdl zzyL = new zzdj();
}
