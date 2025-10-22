package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zziu;
import com.google.android.gms.internal.zzjd;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzgv implements Callable<zzie> {
    private static final long zzFC = TimeUnit.SECONDS.toMillis(60);
    private final Context mContext;
    private final zziu zzFD;
    private final com.google.android.gms.ads.internal.zzn zzFE;
    private final zzbc zzFF;
    private JSONObject zzFI;
    private final zzie.zza zzFc;
    private final zzan zzxV;
    private final Object zzpK = new Object();
    private boolean zzFG = false;
    private int zzFt = -2;
    private List<String> zzFH = null;

    public interface zza<T extends zzh.zza> {
        T zza(zzgv zzgvVar, JSONObject jSONObject) throws ExecutionException, JSONException, InterruptedException;
    }

    class zzb {
        public zzdl zzFY;

        zzb() {
        }
    }

    public zzgv(Context context, com.google.android.gms.ads.internal.zzn zznVar, zzbc zzbcVar, zziu zziuVar, zzan zzanVar, zzie.zza zzaVar) {
        this.mContext = context;
        this.zzFE = zznVar;
        this.zzFD = zziuVar;
        this.zzFF = zzbcVar;
        this.zzFc = zzaVar;
        this.zzxV = zzanVar;
    }

    private zzh.zza zza(zzbb zzbbVar, zza zzaVar, JSONObject jSONObject) throws ExecutionException, JSONException, InterruptedException {
        if (zzga()) {
            return null;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("tracking_urls_and_actions");
        String[] strArrZzc = zzc(jSONObject2, "impression_tracking_urls");
        this.zzFH = strArrZzc == null ? null : Arrays.asList(strArrZzc);
        this.zzFI = jSONObject2.optJSONObject("active_view");
        zzh.zza zzaVarZza = zzaVar.zza(this, jSONObject);
        if (zzaVarZza == null) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Failed to retrieve ad assets.");
            return null;
        }
        zzaVarZza.zzb(new com.google.android.gms.ads.internal.formats.zzh(this.mContext, this.zzFE, zzbbVar, this.zzxV, jSONObject, zzaVarZza, this.zzFc.zzJK.zzqR));
        return zzaVarZza;
    }

    private zzie zza(zzh.zza zzaVar) {
        int i;
        synchronized (this.zzpK) {
            i = this.zzFt;
            if (zzaVar == null && this.zzFt == -2) {
                i = 0;
            }
        }
        return new zzie(this.zzFc.zzJK.zzGq, null, this.zzFc.zzJL.zzAQ, i, this.zzFc.zzJL.zzAR, this.zzFH, this.zzFc.zzJL.orientation, this.zzFc.zzJL.zzAU, this.zzFc.zzJK.zzGt, false, null, null, null, null, null, 0L, this.zzFc.zzqV, this.zzFc.zzJL.zzGM, this.zzFc.zzJH, this.zzFc.zzJI, this.zzFc.zzJL.zzGS, this.zzFI, i != -2 ? null : zzaVar);
    }

    private zzje<com.google.android.gms.ads.internal.formats.zzc> zza(JSONObject jSONObject, final boolean z, boolean z2) throws JSONException {
        final String string = z ? jSONObject.getString("url") : jSONObject.optString("url");
        final double dOptDouble = jSONObject.optDouble("scale", 1.0d);
        if (!TextUtils.isEmpty(string)) {
            return z2 ? new zzjc(new com.google.android.gms.ads.internal.formats.zzc(null, Uri.parse(string), dOptDouble)) : this.zzFD.zza(string, new zziu.zza<com.google.android.gms.ads.internal.formats.zzc>() { // from class: com.google.android.gms.internal.zzgv.5
                @Override // com.google.android.gms.internal.zziu.zza
                /* renamed from: zzg, reason: merged with bridge method [inline-methods] */
                public com.google.android.gms.ads.internal.formats.zzc zzh(InputStream inputStream) {
                    byte[] bArrZzk;
                    try {
                        bArrZzk = zznt.zzk(inputStream);
                    } catch (IOException e) {
                        bArrZzk = null;
                    }
                    if (bArrZzk == null) {
                        zzgv.this.zza(2, z);
                        return null;
                    }
                    Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bArrZzk, 0, bArrZzk.length);
                    if (bitmapDecodeByteArray == null) {
                        zzgv.this.zza(2, z);
                        return null;
                    }
                    bitmapDecodeByteArray.setDensity((int) (160.0d * dOptDouble));
                    return new com.google.android.gms.ads.internal.formats.zzc(new BitmapDrawable(Resources.getSystem(), bitmapDecodeByteArray), Uri.parse(string), dOptDouble);
                }

                @Override // com.google.android.gms.internal.zziu.zza
                /* renamed from: zzgb, reason: merged with bridge method [inline-methods] */
                public com.google.android.gms.ads.internal.formats.zzc zzgc() {
                    zzgv.this.zza(2, z);
                    return null;
                }
            });
        }
        zza(0, z);
        return new zzjc(null);
    }

    private void zza(zzh.zza zzaVar, zzbb zzbbVar) {
        if (zzaVar instanceof com.google.android.gms.ads.internal.formats.zzf) {
            final com.google.android.gms.ads.internal.formats.zzf zzfVar = (com.google.android.gms.ads.internal.formats.zzf) zzaVar;
            zzb zzbVar = new zzb();
            zzdl zzdlVar = new zzdl() { // from class: com.google.android.gms.internal.zzgv.3
                @Override // com.google.android.gms.internal.zzdl
                public void zza(zzjn zzjnVar, Map<String, String> map) {
                    zzgv.this.zzb(zzfVar, map.get("asset"));
                }
            };
            zzbVar.zzFY = zzdlVar;
            zzbbVar.zza("/nativeAdCustomClick", zzdlVar);
        }
    }

    private Integer zzb(JSONObject jSONObject, String str) throws JSONException {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject(str);
            return Integer.valueOf(Color.rgb(jSONObject2.getInt("r"), jSONObject2.getInt("g"), jSONObject2.getInt("b")));
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONObject zzb(final zzbb zzbbVar) throws JSONException, TimeoutException {
        if (zzga()) {
            return null;
        }
        final zzjb zzjbVar = new zzjb();
        final zzb zzbVar = new zzb();
        zzdl zzdlVar = new zzdl() { // from class: com.google.android.gms.internal.zzgv.1
            @Override // com.google.android.gms.internal.zzdl
            public void zza(zzjn zzjnVar, Map<String, String> map) {
                zzbbVar.zzb("/nativeAdPreProcess", zzbVar.zzFY);
                try {
                    String str = map.get("success");
                    if (!TextUtils.isEmpty(str)) {
                        zzjbVar.zzf(new JSONObject(str).getJSONArray("ads").getJSONObject(0));
                        return;
                    }
                } catch (JSONException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Malformed native JSON response.", e);
                }
                zzgv.this.zzF(0);
                com.google.android.gms.common.internal.zzx.zza(zzgv.this.zzga(), "Unable to set the ad state error!");
                zzjbVar.zzf(null);
            }
        };
        zzbVar.zzFY = zzdlVar;
        zzbbVar.zza("/nativeAdPreProcess", zzdlVar);
        zzbbVar.zza("google.afma.nativeAds.preProcessJsonGmsg", new JSONObject(this.zzFc.zzJL.body));
        return (JSONObject) zzjbVar.get(zzFC, TimeUnit.MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(zzcv zzcvVar, String str) {
        try {
            zzcz zzczVarZzr = this.zzFE.zzr(zzcvVar.getCustomTemplateId());
            if (zzczVarZzr != null) {
                zzczVarZzr.zza(zzcvVar, str);
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to call onCustomClick for asset " + str + ".", e);
        }
    }

    private String[] zzc(JSONObject jSONObject, String str) throws JSONException {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(str);
        if (jSONArrayOptJSONArray == null) {
            return null;
        }
        String[] strArr = new String[jSONArrayOptJSONArray.length()];
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            strArr[i] = jSONArrayOptJSONArray.getString(i);
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<Drawable> zzf(List<com.google.android.gms.ads.internal.formats.zzc> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        Iterator<com.google.android.gms.ads.internal.formats.zzc> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((Drawable) com.google.android.gms.dynamic.zze.zzp(it.next().zzdC()));
        }
        return arrayList;
    }

    private zzbb zzfZ() throws ExecutionException, InterruptedException, CancellationException, TimeoutException {
        if (zzga()) {
            return null;
        }
        zzbb zzbbVar = this.zzFF.zza(this.mContext, this.zzFc.zzJK.zzqR, (this.zzFc.zzJL.zzDE.indexOf("https") == 0 ? "https:" : "http:") + zzbz.zzwh.get(), this.zzxV).get(zzFC, TimeUnit.MILLISECONDS);
        zzbbVar.zza(this.zzFE, this.zzFE, this.zzFE, this.zzFE, false, null, null, null, null);
        return zzbbVar;
    }

    public void zzF(int i) {
        synchronized (this.zzpK) {
            this.zzFG = true;
            this.zzFt = i;
        }
    }

    public zzje<com.google.android.gms.ads.internal.formats.zzc> zza(JSONObject jSONObject, String str, boolean z, boolean z2) throws JSONException {
        JSONObject jSONObject2 = z ? jSONObject.getJSONObject(str) : jSONObject.optJSONObject(str);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, z, z2);
    }

    public List<zzje<com.google.android.gms.ads.internal.formats.zzc>> zza(JSONObject jSONObject, String str, boolean z, boolean z2, boolean z3) throws JSONException {
        JSONArray jSONArray = z ? jSONObject.getJSONArray(str) : jSONObject.optJSONArray(str);
        ArrayList arrayList = new ArrayList();
        if (jSONArray == null || jSONArray.length() == 0) {
            zza(0, z);
            return arrayList;
        }
        int length = z3 ? jSONArray.length() : 1;
        for (int i = 0; i < length; i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            if (jSONObject2 == null) {
                jSONObject2 = new JSONObject();
            }
            arrayList.add(zza(jSONObject2, z, z2));
        }
        return arrayList;
    }

    public Future<com.google.android.gms.ads.internal.formats.zzc> zza(JSONObject jSONObject, String str, boolean z) throws JSONException {
        JSONObject jSONObject2 = jSONObject.getJSONObject(str);
        boolean zOptBoolean = jSONObject2.optBoolean("require", true);
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        return zza(jSONObject2, zOptBoolean, z);
    }

    public void zza(int i, boolean z) {
        if (z) {
            zzF(i);
        }
    }

    protected zza zzd(JSONObject jSONObject) throws JSONException, TimeoutException {
        if (zzga()) {
            return null;
        }
        String string = jSONObject.getString("template_id");
        boolean z = this.zzFc.zzJK.zzrj != null ? this.zzFc.zzJK.zzrj.zzyc : false;
        boolean z2 = this.zzFc.zzJK.zzrj != null ? this.zzFc.zzJK.zzrj.zzye : false;
        if ("2".equals(string)) {
            return new zzgw(z, z2);
        }
        if ("1".equals(string)) {
            return new zzgx(z, z2);
        }
        if ("3".equals(string)) {
            final String string2 = jSONObject.getString("custom_template_id");
            final zzjb zzjbVar = new zzjb();
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzgv.2
                @Override // java.lang.Runnable
                public void run() {
                    zzjbVar.zzf(zzgv.this.zzFE.zzbq().get(string2));
                }
            });
            if (zzjbVar.get(zzFC, TimeUnit.MILLISECONDS) != null) {
                return new zzgy(z);
            }
            com.google.android.gms.ads.internal.util.client.zzb.e("No handler for custom template: " + jSONObject.getString("custom_template_id"));
        } else {
            zzF(0);
        }
        return null;
    }

    public zzje<com.google.android.gms.ads.internal.formats.zza> zze(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("attribution");
        if (jSONObjectOptJSONObject == null) {
            return new zzjc(null);
        }
        final String strOptString = jSONObjectOptJSONObject.optString("text");
        final int iOptInt = jSONObjectOptJSONObject.optInt("text_size", -1);
        final Integer numZzb = zzb(jSONObjectOptJSONObject, "text_color");
        final Integer numZzb2 = zzb(jSONObjectOptJSONObject, "bg_color");
        final int iOptInt2 = jSONObjectOptJSONObject.optInt("animation_ms", 1000);
        final int iOptInt3 = jSONObjectOptJSONObject.optInt("presentation_ms", 4000);
        List<zzje<com.google.android.gms.ads.internal.formats.zzc>> arrayList = new ArrayList<>();
        if (jSONObjectOptJSONObject.optJSONArray("images") != null) {
            arrayList = zza(jSONObjectOptJSONObject, "images", false, false, true);
        } else {
            arrayList.add(zza(jSONObjectOptJSONObject, "image", false, false));
        }
        return zzjd.zza(zzjd.zzj(arrayList), new zzjd.zza<List<com.google.android.gms.ads.internal.formats.zzc>, com.google.android.gms.ads.internal.formats.zza>() { // from class: com.google.android.gms.internal.zzgv.4
            /* JADX WARN: Removed duplicated region for block: B:6:0x0009  */
            @Override // com.google.android.gms.internal.zzjd.zza
            /* renamed from: zzh, reason: merged with bridge method [inline-methods] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public com.google.android.gms.ads.internal.formats.zza zze(java.util.List<com.google.android.gms.ads.internal.formats.zzc> r10) {
                /*
                    r9 = this;
                    r7 = 0
                    if (r10 == 0) goto L9
                    boolean r0 = r10.isEmpty()     // Catch: android.os.RemoteException -> L2b
                    if (r0 == 0) goto Lc
                L9:
                    r0 = r7
                La:
                    r7 = r0
                Lb:
                    return r7
                Lc:
                    com.google.android.gms.ads.internal.formats.zza r0 = new com.google.android.gms.ads.internal.formats.zza     // Catch: android.os.RemoteException -> L2b
                    java.lang.String r1 = r2     // Catch: android.os.RemoteException -> L2b
                    java.util.List r2 = com.google.android.gms.internal.zzgv.zzg(r10)     // Catch: android.os.RemoteException -> L2b
                    java.lang.Integer r3 = r3     // Catch: android.os.RemoteException -> L2b
                    java.lang.Integer r4 = r4     // Catch: android.os.RemoteException -> L2b
                    int r5 = r5     // Catch: android.os.RemoteException -> L2b
                    if (r5 <= 0) goto L32
                    int r5 = r5     // Catch: android.os.RemoteException -> L2b
                    java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: android.os.RemoteException -> L2b
                L22:
                    int r6 = r6     // Catch: android.os.RemoteException -> L2b
                    int r8 = r7     // Catch: android.os.RemoteException -> L2b
                    int r6 = r6 + r8
                    r0.<init>(r1, r2, r3, r4, r5, r6)     // Catch: android.os.RemoteException -> L2b
                    goto La
                L2b:
                    r0 = move-exception
                    java.lang.String r1 = "Could not get attribution icon"
                    com.google.android.gms.ads.internal.util.client.zzb.zzb(r1, r0)
                    goto Lb
                L32:
                    r5 = r7
                    goto L22
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzgv.AnonymousClass4.zze(java.util.List):com.google.android.gms.ads.internal.formats.zza");
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0022  */
    @Override // java.util.concurrent.Callable
    /* renamed from: zzfY, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.android.gms.internal.zzie call() {
        /*
            r3 = this;
            com.google.android.gms.internal.zzbb r0 = r3.zzfZ()     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
            org.json.JSONObject r1 = r3.zzb(r0)     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
            com.google.android.gms.internal.zzgv$zza r2 = r3.zzd(r1)     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
            com.google.android.gms.ads.internal.formats.zzh$zza r1 = r3.zza(r0, r2, r1)     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
            r3.zza(r1, r0)     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
            com.google.android.gms.internal.zzie r0 = r3.zza(r1)     // Catch: org.json.JSONException -> L18 java.util.concurrent.TimeoutException -> L2c java.lang.InterruptedException -> L33 java.util.concurrent.ExecutionException -> L35 java.util.concurrent.CancellationException -> L37
        L17:
            return r0
        L18:
            r0 = move-exception
            java.lang.String r1 = "Malformed native JSON response."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0)
        L1e:
            boolean r0 = r3.zzFG
            if (r0 != 0) goto L26
            r0 = 0
            r3.zzF(r0)
        L26:
            r0 = 0
            com.google.android.gms.internal.zzie r0 = r3.zza(r0)
            goto L17
        L2c:
            r0 = move-exception
            java.lang.String r1 = "Timeout when loading native ad."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0)
            goto L1e
        L33:
            r0 = move-exception
            goto L1e
        L35:
            r0 = move-exception
            goto L1e
        L37:
            r0 = move-exception
            goto L1e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzgv.call():com.google.android.gms.internal.zzie");
    }

    public boolean zzga() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzFG;
        }
        return z;
    }
}
