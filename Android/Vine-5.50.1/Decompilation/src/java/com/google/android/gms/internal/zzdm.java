package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzdm implements zzdl {
    private final Context mContext;
    private final VersionInfoParcel zzpI;

    @zzha
    static class zza {
        private final String mValue;
        private final String zzuX;

        public zza(String str, String str2) {
            this.zzuX = str;
            this.mValue = str2;
        }

        public String getKey() {
            return this.zzuX;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    @zzha
    static class zzb {
        private final String zzyQ;
        private final URL zzyR;
        private final ArrayList<zza> zzyS;
        private final String zzyT;

        public zzb(String str, URL url, ArrayList<zza> arrayList, String str2) {
            this.zzyQ = str;
            this.zzyR = url;
            if (arrayList == null) {
                this.zzyS = new ArrayList<>();
            } else {
                this.zzyS = arrayList;
            }
            this.zzyT = str2;
        }

        public String zzdN() {
            return this.zzyQ;
        }

        public URL zzdO() {
            return this.zzyR;
        }

        public ArrayList<zza> zzdP() {
            return this.zzyS;
        }

        public String zzdQ() {
            return this.zzyT;
        }
    }

    @zzha
    class zzc {
        private final zzd zzyU;
        private final boolean zzyV;
        private final String zzyW;

        public zzc(boolean z, zzd zzdVar, String str) {
            this.zzyV = z;
            this.zzyU = zzdVar;
            this.zzyW = str;
        }

        public String getReason() {
            return this.zzyW;
        }

        public boolean isSuccess() {
            return this.zzyV;
        }

        public zzd zzdR() {
            return this.zzyU;
        }
    }

    @zzha
    static class zzd {
        private final String zzxA;
        private final String zzyQ;
        private final int zzyX;
        private final List<zza> zzyY;

        public zzd(String str, int i, List<zza> list, String str2) {
            this.zzyQ = str;
            this.zzyX = i;
            if (list == null) {
                this.zzyY = new ArrayList();
            } else {
                this.zzyY = list;
            }
            this.zzxA = str2;
        }

        public String getBody() {
            return this.zzxA;
        }

        public int getResponseCode() {
            return this.zzyX;
        }

        public String zzdN() {
            return this.zzyQ;
        }

        public Iterable<zza> zzdS() {
            return this.zzyY;
        }
    }

    public zzdm(Context context, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzpI = versionInfoParcel;
    }

    public JSONObject zzV(String str) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = new JSONObject();
            String strOptString = "";
            try {
                strOptString = jSONObject.optString("http_request_id");
                zzc zzcVarZza = zza(zzb(jSONObject));
                if (zzcVarZza.isSuccess()) {
                    jSONObject2.put("response", zza(zzcVarZza.zzdR()));
                    jSONObject2.put("success", true);
                } else {
                    jSONObject2.put("response", new JSONObject().put("http_request_id", strOptString));
                    jSONObject2.put("success", false);
                    jSONObject2.put("reason", zzcVarZza.getReason());
                }
                return jSONObject2;
            } catch (Exception e) {
                try {
                    jSONObject2.put("response", new JSONObject().put("http_request_id", strOptString));
                    jSONObject2.put("success", false);
                    jSONObject2.put("reason", e.toString());
                    return jSONObject2;
                } catch (JSONException e2) {
                    return jSONObject2;
                }
            }
        } catch (JSONException e3) {
            com.google.android.gms.ads.internal.util.client.zzb.e("The request is not a valid JSON.");
            try {
                return new JSONObject().put("success", false);
            } catch (JSONException e4) {
                return new JSONObject();
            }
        }
    }

    protected zzc zza(zzb zzbVar) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) zzbVar.zzdO().openConnection();
            com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzpI.afmaVersion, false, httpURLConnection);
            Iterator<zza> it = zzbVar.zzdP().iterator();
            while (it.hasNext()) {
                zza next = it.next();
                httpURLConnection.addRequestProperty(next.getKey(), next.getValue());
            }
            if (!TextUtils.isEmpty(zzbVar.zzdQ())) {
                httpURLConnection.setDoOutput(true);
                byte[] bytes = zzbVar.zzdQ().getBytes();
                httpURLConnection.setFixedLengthStreamingMode(bytes.length);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
            }
            ArrayList arrayList = new ArrayList();
            if (httpURLConnection.getHeaderFields() != null) {
                for (Map.Entry<String, List<String>> entry : httpURLConnection.getHeaderFields().entrySet()) {
                    Iterator<String> it2 = entry.getValue().iterator();
                    while (it2.hasNext()) {
                        arrayList.add(new zza(entry.getKey(), it2.next()));
                    }
                }
            }
            return new zzc(true, new zzd(zzbVar.zzdN(), httpURLConnection.getResponseCode(), arrayList, com.google.android.gms.ads.internal.zzp.zzbx().zza(new InputStreamReader(httpURLConnection.getInputStream()))), null);
        } catch (Exception e) {
            return new zzc(false, null, e.toString());
        }
    }

    protected JSONObject zza(zzd zzdVar) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("http_request_id", zzdVar.zzdN());
            if (zzdVar.getBody() != null) {
                jSONObject.put("body", zzdVar.getBody());
            }
            JSONArray jSONArray = new JSONArray();
            for (zza zzaVar : zzdVar.zzdS()) {
                jSONArray.put(new JSONObject().put("key", zzaVar.getKey()).put("value", zzaVar.getValue()));
            }
            jSONObject.put("headers", jSONArray);
            jSONObject.put("response_code", zzdVar.getResponseCode());
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error constructing JSON for http response.", e);
        }
        return jSONObject;
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(final zzjn zzjnVar, final Map<String, String> map) {
        zzio.zza(new Runnable() { // from class: com.google.android.gms.internal.zzdm.1
            @Override // java.lang.Runnable
            public void run() throws JSONException {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Received Http request.");
                final JSONObject jSONObjectZzV = zzdm.this.zzV((String) map.get("http_request"));
                if (jSONObjectZzV == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.e("Response should not be null.");
                } else {
                    zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzdm.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            zzjnVar.zzb("fetchHttpRequestCompleted", jSONObjectZzV);
                            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Dispatched http response.");
                        }
                    });
                }
            }
        });
    }

    protected zzb zzb(JSONObject jSONObject) {
        URL url;
        String strOptString = jSONObject.optString("http_request_id");
        String strOptString2 = jSONObject.optString("url");
        String strOptString3 = jSONObject.optString("post_body", null);
        try {
            url = new URL(strOptString2);
        } catch (MalformedURLException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error constructing http request.", e);
            url = null;
        }
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("headers");
        if (jSONArrayOptJSONArray == null) {
            jSONArrayOptJSONArray = new JSONArray();
        }
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
            if (jSONObjectOptJSONObject != null) {
                arrayList.add(new zza(jSONObjectOptJSONObject.optString("key"), jSONObjectOptJSONObject.optString("value")));
            }
        }
        return new zzb(strOptString, url, arrayList, strOptString3);
    }
}
