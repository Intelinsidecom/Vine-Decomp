package com.google.android.gms.internal;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.request.zzj;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzei;
import com.google.android.gms.internal.zzhm;
import com.google.android.gms.internal.zzjg;
import com.google.android.gms.internal.zzjo;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzhc extends zzj.zza {
    private static zzhc zzHF;
    private static final Object zzqf = new Object();
    private final Context mContext;
    private final zzhb zzHG;
    private final zzbs zzHH;
    private final zzei zzrO;

    zzhc(Context context, zzbs zzbsVar, zzhb zzhbVar) {
        this.mContext = context;
        this.zzHG = zzhbVar;
        this.zzHH = zzbsVar;
        this.zzrO = new zzei(context.getApplicationContext() != null ? context.getApplicationContext() : context, new VersionInfoParcel(8298000, 8298000, true), zzbsVar.zzdj(), new zzei.zzb<zzbb>() { // from class: com.google.android.gms.internal.zzhc.6
            @Override // com.google.android.gms.internal.zzei.zzb
            /* renamed from: zza, reason: merged with bridge method [inline-methods] */
            public void zzc(zzbb zzbbVar) {
                zzbbVar.zza("/log", zzdk.zzyG);
            }
        }, new zzei.zzc());
    }

    private static AdResponseParcel zza(final Context context, final zzei zzeiVar, final zzbs zzbsVar, final zzhb zzhbVar, final AdRequestInfoParcel adRequestInfoParcel) throws ExecutionException, JSONException, InterruptedException, TimeoutException {
        Bundle bundle;
        zzje zzjeVarZza;
        String string;
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Starting ad request from service.");
        zzbz.initialize(context);
        final zzch zzchVar = new zzch(zzbz.zzvL.get().booleanValue(), "load_ad", adRequestInfoParcel.zzqV.zztV);
        if (adRequestInfoParcel.versionCode > 10 && adRequestInfoParcel.zzGI != -1) {
            zzchVar.zza(zzchVar.zzb(adRequestInfoParcel.zzGI), "cts");
        }
        zzcf zzcfVarZzdu = zzchVar.zzdu();
        final Bundle bundle2 = (adRequestInfoParcel.versionCode < 4 || adRequestInfoParcel.zzGx == null) ? null : adRequestInfoParcel.zzGx;
        if (!zzbz.zzvU.get().booleanValue() || zzhbVar.zzHE == null) {
            bundle = bundle2;
            zzjeVarZza = null;
        } else {
            if (bundle2 == null && zzbz.zzvV.get().booleanValue()) {
                com.google.android.gms.ads.internal.util.client.zzb.v("contentInfo is not present, but we'll still launch the app index task");
                bundle2 = new Bundle();
            }
            if (bundle2 != null) {
                bundle = bundle2;
                zzjeVarZza = zzio.zza(new Callable<Void>() { // from class: com.google.android.gms.internal.zzhc.1
                    @Override // java.util.concurrent.Callable
                    /* renamed from: zzdm, reason: merged with bridge method [inline-methods] */
                    public Void call() throws Exception {
                        zzhbVar.zzHE.zza(context, adRequestInfoParcel.zzGr.packageName, bundle2);
                        return null;
                    }
                });
            } else {
                bundle = bundle2;
                zzjeVarZza = null;
            }
        }
        zzhbVar.zzHz.init();
        zzhi zzhiVarZzE = com.google.android.gms.ads.internal.zzp.zzbD().zzE(context);
        if (zzhiVarZzE.zzIM == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Device is offline.");
            return new AdResponseParcel(2);
        }
        String string2 = adRequestInfoParcel.versionCode >= 7 ? adRequestInfoParcel.zzGF : UUID.randomUUID().toString();
        final zzhe zzheVar = new zzhe(string2, adRequestInfoParcel.applicationInfo.packageName);
        if (adRequestInfoParcel.zzGq.extras != null && (string = adRequestInfoParcel.zzGq.extras.getString("_ad")) != null) {
            return zzhd.zza(context, adRequestInfoParcel, string);
        }
        Location locationZzd = zzhbVar.zzHz.zzd(250L);
        String token = zzhbVar.zzHA.getToken(context, adRequestInfoParcel.zzqP, adRequestInfoParcel.zzGr.packageName);
        List<String> listZza = zzhbVar.zzHx.zza(adRequestInfoParcel);
        String strZzf = zzhbVar.zzHB.zzf(adRequestInfoParcel);
        zzhm.zza zzaVarZzF = zzhbVar.zzHC.zzF(context);
        if (zzjeVarZza != null) {
            try {
                com.google.android.gms.ads.internal.util.client.zzb.v("Waiting for app index fetching task.");
                zzjeVarZza.get(zzbz.zzvW.get().longValue(), TimeUnit.MILLISECONDS);
                com.google.android.gms.ads.internal.util.client.zzb.v("App index fetching task completed.");
            } catch (InterruptedException e) {
                e = e;
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to fetch app index signal", e);
            } catch (ExecutionException e2) {
                e = e2;
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Failed to fetch app index signal", e);
            } catch (TimeoutException e3) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Timed out waiting for app index fetching task");
            }
        }
        JSONObject jSONObjectZza = zzhd.zza(context, adRequestInfoParcel, zzhiVarZzE, zzaVarZzF, locationZzd, zzbsVar, token, strZzf, listZza, bundle);
        if (adRequestInfoParcel.versionCode < 7) {
            try {
                jSONObjectZza.put("request_id", string2);
            } catch (JSONException e4) {
            }
        }
        if (jSONObjectZza == null) {
            return new AdResponseParcel(0);
        }
        final String string3 = jSONObjectZza.toString();
        zzchVar.zza(zzcfVarZzdu, "arc");
        final zzcf zzcfVarZzdu2 = zzchVar.zzdu();
        if (zzbz.zzvh.get().booleanValue()) {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzhc.2
                @Override // java.lang.Runnable
                public void run() {
                    zzei.zzd zzdVarZzei = zzeiVar.zzei();
                    zzheVar.zzb(zzdVarZzei);
                    zzchVar.zza(zzcfVarZzdu2, "rwc");
                    final zzcf zzcfVarZzdu3 = zzchVar.zzdu();
                    zzdVarZzei.zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzhc.2.1
                        @Override // com.google.android.gms.internal.zzjg.zzc
                        /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                        public void zzc(zzbe zzbeVar) {
                            zzchVar.zza(zzcfVarZzdu3, "jsf");
                            zzchVar.zzdv();
                            zzbeVar.zza("/invalidRequest", zzheVar.zzHY);
                            zzbeVar.zza("/loadAdURL", zzheVar.zzHZ);
                            try {
                                zzbeVar.zza("AFMA_buildAdURL", string3);
                            } catch (Exception e5) {
                                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e5);
                            }
                        }
                    }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzhc.2.2
                        @Override // com.google.android.gms.internal.zzjg.zza
                        public void run() {
                        }
                    });
                }
            });
        } else {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzhc.3
                @Override // java.lang.Runnable
                public void run() {
                    zzjn zzjnVarZza = com.google.android.gms.ads.internal.zzp.zzby().zza(context, new AdSizeParcel(), false, false, null, adRequestInfoParcel.zzqR);
                    if (com.google.android.gms.ads.internal.zzp.zzbA().zzgS()) {
                        zzjnVarZza.clearCache(true);
                    }
                    zzjnVarZza.getWebView().setWillNotDraw(true);
                    zzheVar.zze(zzjnVarZza);
                    zzchVar.zza(zzcfVarZzdu2, "rwc");
                    zzjo.zza zzaVarZza = zzhc.zza(string3, zzchVar, zzchVar.zzdu());
                    zzjo zzjoVarZzhC = zzjnVarZza.zzhC();
                    zzjoVarZzhC.zza("/invalidRequest", zzheVar.zzHY);
                    zzjoVarZzhC.zza("/loadAdURL", zzheVar.zzHZ);
                    zzjoVarZzhC.zza("/log", zzdk.zzyG);
                    zzjoVarZzhC.zza(zzaVarZza);
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("Loading the JS library.");
                    zzjnVarZza.loadUrl(zzbsVar.zzdj());
                }
            });
        }
        try {
            zzhh zzhhVar = zzheVar.zzgp().get(10L, TimeUnit.SECONDS);
            if (zzhhVar == null) {
                return new AdResponseParcel(0);
            }
            if (zzhhVar.getErrorCode() != -2) {
                return new AdResponseParcel(zzhhVar.getErrorCode());
            }
            if (zzchVar.zzdx() != null) {
                zzchVar.zza(zzchVar.zzdx(), "rur");
            }
            AdResponseParcel adResponseParcelZza = zza(adRequestInfoParcel, context, adRequestInfoParcel.zzqR.afmaVersion, zzhhVar.getUrl(), zzhhVar.zzgt() ? zzhbVar.zzHw.zzax(adRequestInfoParcel.zzGr.packageName) : null, zzhhVar.zzgu() ? token : null, zzhhVar, zzchVar, zzhbVar);
            if (adResponseParcelZza.zzGZ == 1) {
                zzhbVar.zzHA.clearToken(context, adRequestInfoParcel.zzGr.packageName);
            }
            zzchVar.zza(zzcfVarZzdu, "tts");
            adResponseParcelZza.zzHb = zzchVar.zzdw();
            return adResponseParcelZza;
        } catch (Exception e5) {
            return new AdResponseParcel(0);
        } finally {
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzhc.4
                @Override // java.lang.Runnable
                public void run() {
                    zzhbVar.zzHy.zza(context, zzheVar, adRequestInfoParcel.zzqR);
                }
            });
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0191, code lost:
    
        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Received error HTTP response code: " + r9);
        r3 = new com.google.android.gms.ads.internal.request.AdResponseParcel(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01ad, code lost:
    
        r2.disconnect();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01b0, code lost:
    
        if (r21 == null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01b2, code lost:
    
        r21.zzHD.zzgx();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel r13, android.content.Context r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, com.google.android.gms.internal.zzhh r19, com.google.android.gms.internal.zzch r20, com.google.android.gms.internal.zzhb r21) {
        /*
            Method dump skipped, instructions count: 469
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhc.zza(com.google.android.gms.ads.internal.request.AdRequestInfoParcel, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.google.android.gms.internal.zzhh, com.google.android.gms.internal.zzch, com.google.android.gms.internal.zzhb):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    public static zzhc zza(Context context, zzbs zzbsVar, zzhb zzhbVar) {
        zzhc zzhcVar;
        synchronized (zzqf) {
            if (zzHF == null) {
                if (context.getApplicationContext() != null) {
                    context = context.getApplicationContext();
                }
                zzHF = new zzhc(context, zzbsVar, zzhbVar);
            }
            zzhcVar = zzHF;
        }
        return zzhcVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static zzjo.zza zza(final String str, final zzch zzchVar, final zzcf zzcfVar) {
        return new zzjo.zza() { // from class: com.google.android.gms.internal.zzhc.5
            @Override // com.google.android.gms.internal.zzjo.zza
            public void zza(zzjn zzjnVar, boolean z) {
                zzchVar.zza(zzcfVar, "jsf");
                zzchVar.zzdv();
                zzjnVar.zza("AFMA_buildAdURL", str);
            }
        };
    }

    private static void zza(String str, Map<String, List<String>> map, String str2, int i) {
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    com.google.android.gms.ads.internal.util.client.zzb.v("    " + str3 + ":");
                    Iterator<String> it = map.get(str3).iterator();
                    while (it.hasNext()) {
                        com.google.android.gms.ads.internal.util.client.zzb.v("      " + it.next());
                    }
                }
            }
            com.google.android.gms.ads.internal.util.client.zzb.v("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += 1000) {
                    com.google.android.gms.ads.internal.util.client.zzb.v(str2.substring(i2, Math.min(str2.length(), i2 + 1000)));
                }
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.v("    null");
            }
            com.google.android.gms.ads.internal.util.client.zzb.v("  Response Code:\n    " + i + "\n}");
        }
    }

    @Override // com.google.android.gms.ads.internal.request.zzj
    public void zza(final AdRequestInfoParcel adRequestInfoParcel, final com.google.android.gms.ads.internal.request.zzk zzkVar) {
        com.google.android.gms.ads.internal.zzp.zzbA().zzb(this.mContext, adRequestInfoParcel.zzqR);
        zzio.zza(new Runnable() { // from class: com.google.android.gms.internal.zzhc.7
            @Override // java.lang.Runnable
            public void run() {
                AdResponseParcel adResponseParcel;
                try {
                    adResponseParcel = zzhc.this.zzd(adRequestInfoParcel);
                } catch (Exception e) {
                    com.google.android.gms.ads.internal.zzp.zzbA().zzb((Throwable) e, true);
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not fetch ad response due to an Exception.", e);
                    adResponseParcel = null;
                }
                if (adResponseParcel == null) {
                    adResponseParcel = new AdResponseParcel(0);
                }
                try {
                    zzkVar.zzb(adResponseParcel);
                } catch (RemoteException e2) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to forward ad response.", e2);
                }
            }
        });
    }

    @Override // com.google.android.gms.ads.internal.request.zzj
    public AdResponseParcel zzd(AdRequestInfoParcel adRequestInfoParcel) {
        return zza(this.mContext, this.zzrO, this.zzHH, this.zzHG, adRequestInfoParcel);
    }
}
