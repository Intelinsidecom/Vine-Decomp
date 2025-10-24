package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.zza;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzbb;
import com.google.android.gms.internal.zzbe;
import com.google.android.gms.internal.zzbs;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzdl;
import com.google.android.gms.internal.zzdm;
import com.google.android.gms.internal.zzdq;
import com.google.android.gms.internal.zzei;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzhd;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzjg;
import com.google.android.gms.internal.zzjn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzm extends zzil {
    private final Context mContext;
    private final Object zzFf;
    private final zza.InterfaceC0024zza zzGd;
    private final AdRequestInfoParcel.zza zzGe;
    private zzei.zzd zzHr;
    static final long zzHl = TimeUnit.SECONDS.toMillis(10);
    private static final Object zzqf = new Object();
    private static boolean zzHm = false;
    private static zzei zzHn = null;
    private static zzdm zzHo = null;
    private static zzdq zzHp = null;
    private static zzdl zzHq = null;

    public static class zza implements zzei.zzb<zzbb> {
        @Override // com.google.android.gms.internal.zzei.zzb
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public void zzc(zzbb zzbbVar) {
            zzm.zzd(zzbbVar);
        }
    }

    public static class zzb implements zzei.zzb<zzbb> {
        @Override // com.google.android.gms.internal.zzei.zzb
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public void zzc(zzbb zzbbVar) {
            zzm.zzc(zzbbVar);
        }
    }

    public static class zzc implements zzdl {
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            String str = map.get("request_id");
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Invalid request: " + map.get("errors"));
            zzm.zzHp.zzX(str);
        }
    }

    public zzm(Context context, AdRequestInfoParcel.zza zzaVar, zza.InterfaceC0024zza interfaceC0024zza) {
        super(true);
        this.zzFf = new Object();
        this.zzGd = interfaceC0024zza;
        this.mContext = context;
        this.zzGe = zzaVar;
        synchronized (zzqf) {
            if (!zzHm) {
                zzHp = new zzdq();
                zzHo = new zzdm(context.getApplicationContext(), zzaVar.zzqR);
                zzHq = new zzc();
                zzHn = new zzei(this.mContext.getApplicationContext(), this.zzGe.zzqR, zzbz.zzvg.get(), new zzb(), new zza());
                zzHm = true;
            }
        }
    }

    private JSONObject zza(AdRequestInfoParcel adRequestInfoParcel, String str) {
        JSONObject jSONObjectZza;
        AdvertisingIdClient.Info advertisingIdInfo;
        Bundle bundle = adRequestInfoParcel.zzGq.extras.getBundle("sdk_less_server_data");
        String string = adRequestInfoParcel.zzGq.extras.getString("sdk_less_network_id");
        if (bundle == null || (jSONObjectZza = zzhd.zza(this.mContext, adRequestInfoParcel, zzp.zzbD().zzE(this.mContext), null, null, new zzbs(zzbz.zzvg.get()), null, null, new ArrayList(), null)) == null) {
            return null;
        }
        try {
            advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException | IOException | IllegalStateException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Cannot get advertising id info", e);
            advertisingIdInfo = null;
        }
        HashMap map = new HashMap();
        map.put("request_id", str);
        map.put("network_id", string);
        map.put("request_param", jSONObjectZza);
        map.put("data", bundle);
        if (advertisingIdInfo != null) {
            map.put("adid", advertisingIdInfo.getId());
            map.put("lat", Integer.valueOf(advertisingIdInfo.isLimitAdTrackingEnabled() ? 1 : 0));
        }
        try {
            return zzp.zzbx().zzz(map);
        } catch (JSONException e2) {
            return null;
        }
    }

    protected static void zzc(zzbb zzbbVar) {
        zzbbVar.zza("/loadAd", zzHp);
        zzbbVar.zza("/fetchHttpRequest", zzHo);
        zzbbVar.zza("/invalidRequest", zzHq);
    }

    protected static void zzd(zzbb zzbbVar) {
        zzbbVar.zzb("/loadAd", zzHp);
        zzbbVar.zzb("/fetchHttpRequest", zzHo);
        zzbbVar.zzb("/invalidRequest", zzHq);
    }

    private AdResponseParcel zze(AdRequestInfoParcel adRequestInfoParcel) throws ExecutionException, InterruptedException, TimeoutException {
        final String string = UUID.randomUUID().toString();
        final JSONObject jSONObjectZza = zza(adRequestInfoParcel, string);
        if (jSONObjectZza == null) {
            return new AdResponseParcel(0);
        }
        long jElapsedRealtime = zzp.zzbB().elapsedRealtime();
        Future<JSONObject> futureZzW = zzHp.zzW(string);
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.ads.internal.request.zzm.2
            @Override // java.lang.Runnable
            public void run() {
                zzm.this.zzHr = zzm.zzHn.zzei();
                zzm.this.zzHr.zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.ads.internal.request.zzm.2.1
                    @Override // com.google.android.gms.internal.zzjg.zzc
                    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                    public void zzc(zzbe zzbeVar) {
                        try {
                            zzbeVar.zza("AFMA_getAdapterLessMediationAd", jSONObjectZza);
                        } catch (Exception e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error requesting an ad url", e);
                            zzm.zzHp.zzX(string);
                        }
                    }
                }, new zzjg.zza() { // from class: com.google.android.gms.ads.internal.request.zzm.2.2
                    @Override // com.google.android.gms.internal.zzjg.zza
                    public void run() {
                        zzm.zzHp.zzX(string);
                    }
                });
            }
        });
        try {
            JSONObject jSONObject = futureZzW.get(zzHl - (zzp.zzbB().elapsedRealtime() - jElapsedRealtime), TimeUnit.MILLISECONDS);
            if (jSONObject == null) {
                return new AdResponseParcel(-1);
            }
            AdResponseParcel adResponseParcelZza = zzhd.zza(this.mContext, adRequestInfoParcel, jSONObject.toString());
            return (adResponseParcelZza.errorCode == -3 || !TextUtils.isEmpty(adResponseParcelZza.body)) ? adResponseParcelZza : new AdResponseParcel(3);
        } catch (InterruptedException e) {
            return new AdResponseParcel(-1);
        } catch (CancellationException e2) {
            return new AdResponseParcel(-1);
        } catch (ExecutionException e3) {
            return new AdResponseParcel(0);
        } catch (TimeoutException e4) {
            return new AdResponseParcel(2);
        }
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
        synchronized (this.zzFf) {
            com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.ads.internal.request.zzm.3
                @Override // java.lang.Runnable
                public void run() {
                    if (zzm.this.zzHr != null) {
                        zzm.this.zzHr.release();
                        zzm.this.zzHr = null;
                    }
                }
            });
        }
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() throws ExecutionException, InterruptedException, TimeoutException {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("SdkLessAdLoaderBackgroundTask started.");
        AdRequestInfoParcel adRequestInfoParcel = new AdRequestInfoParcel(this.zzGe, null, -1L);
        AdResponseParcel adResponseParcelZze = zze(adRequestInfoParcel);
        final zzie.zza zzaVar = new zzie.zza(adRequestInfoParcel, adResponseParcelZze, null, null, adResponseParcelZze.errorCode, zzp.zzbB().elapsedRealtime(), adResponseParcelZze.zzGR, null);
        com.google.android.gms.ads.internal.util.client.zza.zzLE.post(new Runnable() { // from class: com.google.android.gms.ads.internal.request.zzm.1
            @Override // java.lang.Runnable
            public void run() {
                zzm.this.zzGd.zza(zzaVar);
                if (zzm.this.zzHr != null) {
                    zzm.this.zzHr.release();
                    zzm.this.zzHr = null;
                }
            }
        });
    }
}
