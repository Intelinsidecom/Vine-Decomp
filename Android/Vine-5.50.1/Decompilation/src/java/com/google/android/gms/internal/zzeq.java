package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.ads.mediation.AdUrlAdapter;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzer;
import com.google.android.gms.internal.zzez;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzeq implements zzer.zza {
    private final Context mContext;
    private final String zzBd;
    private final long zzBe;
    private final zzen zzBf;
    private final zzem zzBg;
    private final AdSizeParcel zzBh;
    private zzex zzBi;
    private zzez zzBk;
    private final NativeAdOptionsParcel zzpE;
    private final List<String> zzpF;
    private final VersionInfoParcel zzpI;
    private final zzew zzpd;
    private final AdRequestParcel zzqo;
    private final boolean zzrF;
    private final Object zzpK = new Object();
    private int zzBj = -2;

    public zzeq(Context context, String str, zzew zzewVar, zzen zzenVar, zzem zzemVar, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, VersionInfoParcel versionInfoParcel, boolean z, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
        this.mContext = context;
        this.zzpd = zzewVar;
        this.zzBg = zzemVar;
        if ("com.google.ads.mediation.customevent.CustomEventAdapter".equals(str)) {
            this.zzBd = zzen();
        } else {
            this.zzBd = str;
        }
        this.zzBf = zzenVar;
        this.zzBe = zzenVar.zzAP != -1 ? zzenVar.zzAP : 10000L;
        this.zzqo = adRequestParcel;
        this.zzBh = adSizeParcel;
        this.zzpI = versionInfoParcel;
        this.zzrF = z;
        this.zzpE = nativeAdOptionsParcel;
        this.zzpF = list;
    }

    private void zza(long j, long j2, long j3, long j4) throws InterruptedException {
        while (this.zzBj == -2) {
            zzb(j, j2, j3, j4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(zzep zzepVar) {
        if ("com.google.ads.mediation.AdUrlAdapter".equals(this.zzBd)) {
            Bundle bundle = this.zzqo.zztA.getBundle(this.zzBd);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("sdk_less_network_id", this.zzBg.zzAF);
            this.zzqo.zztA.putBundle(this.zzBd, bundle);
        }
        String strZzae = zzae(this.zzBg.zzAL);
        try {
            if (this.zzpI.zzLG < 4100000) {
                if (this.zzBh.zztW) {
                    this.zzBi.zza(com.google.android.gms.dynamic.zze.zzB(this.mContext), this.zzqo, strZzae, zzepVar);
                } else {
                    this.zzBi.zza(com.google.android.gms.dynamic.zze.zzB(this.mContext), this.zzBh, this.zzqo, strZzae, zzepVar);
                }
            } else if (this.zzrF) {
                this.zzBi.zza(com.google.android.gms.dynamic.zze.zzB(this.mContext), this.zzqo, strZzae, this.zzBg.zzAE, zzepVar, this.zzpE, this.zzpF);
            } else if (this.zzBh.zztW) {
                this.zzBi.zza(com.google.android.gms.dynamic.zze.zzB(this.mContext), this.zzqo, strZzae, this.zzBg.zzAE, zzepVar);
            } else {
                this.zzBi.zza(com.google.android.gms.dynamic.zze.zzB(this.mContext), this.zzBh, this.zzqo, strZzae, this.zzBg.zzAE, zzepVar);
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not request ad from mediation adapter.", e);
            zzr(5);
        }
    }

    private String zzae(String str) {
        if (str == null || !zzeq() || zzs(2)) {
            return str;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.remove("cpm_floor_cents");
            return jSONObject.toString();
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not remove field. Returning the original value");
            return str;
        }
    }

    private void zzb(long j, long j2, long j3, long j4) throws InterruptedException {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - (jElapsedRealtime - j);
        long j6 = j4 - (jElapsedRealtime - j3);
        if (j5 <= 0 || j6 <= 0) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Timed out waiting for adapter.");
            this.zzBj = 3;
        } else {
            try {
                this.zzpK.wait(Math.min(j5, j6));
            } catch (InterruptedException e) {
                this.zzBj = -1;
            }
        }
    }

    private String zzen() {
        try {
            if (!TextUtils.isEmpty(this.zzBg.zzAI)) {
                return this.zzpd.zzag(this.zzBg.zzAI) ? "com.google.android.gms.ads.mediation.customevent.CustomEventAdapter" : "com.google.ads.mediation.customevent.CustomEventAdapter";
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Fail to determine the custom event's version, assuming the old one.");
        }
        return "com.google.ads.mediation.customevent.CustomEventAdapter";
    }

    private zzez zzeo() {
        if (this.zzBj != 0 || !zzeq()) {
            return null;
        }
        try {
            if (zzs(4) && this.zzBk != null && this.zzBk.zzes() != 0) {
                return this.zzBk;
            }
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not get cpm value from MediationResponseMetadata");
        }
        return zzt(zzer());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public zzex zzep() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaG("Instantiating mediation adapter: " + this.zzBd);
        if (zzbz.zzwA.get().booleanValue()) {
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzBd)) {
                return new zzfd(new AdMobAdapter());
            }
            if ("com.google.ads.mediation.AdUrlAdapter".equals(this.zzBd)) {
                return new zzfd(new AdUrlAdapter());
            }
        }
        try {
            return this.zzpd.zzaf(this.zzBd);
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zza("Could not instantiate mediation adapter: " + this.zzBd, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzeq() {
        return this.zzBf.zzAX != -1;
    }

    private int zzer() {
        if (this.zzBg.zzAL == null) {
            return 0;
        }
        try {
            JSONObject jSONObject = new JSONObject(this.zzBg.zzAL);
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzBd)) {
                return jSONObject.optInt("cpm_cents", 0);
            }
            int iOptInt = zzs(2) ? jSONObject.optInt("cpm_floor_cents", 0) : 0;
            return iOptInt == 0 ? jSONObject.optInt("penalized_average_cpm_cents", 0) : iOptInt;
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not convert to json. Returning 0");
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean zzs(int i) {
        try {
            Bundle bundleZzex = this.zzrF ? this.zzBi.zzex() : this.zzBh.zztW ? this.zzBi.getInterstitialAdapterInfo() : this.zzBi.zzew();
            if (bundleZzex != null) {
                return (bundleZzex.getInt("capabilities", 0) & i) == i;
            }
            return false;
        } catch (RemoteException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not get adapter info. Returning false");
            return false;
        }
    }

    private static zzez zzt(final int i) {
        return new zzez.zza() { // from class: com.google.android.gms.internal.zzeq.2
            @Override // com.google.android.gms.internal.zzez
            public int zzes() throws RemoteException {
                return i;
            }
        };
    }

    public void cancel() {
        synchronized (this.zzpK) {
            try {
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not destroy mediation adapter.", e);
            }
            if (this.zzBi != null) {
                this.zzBi.destroy();
                this.zzBj = -1;
                this.zzpK.notify();
            } else {
                this.zzBj = -1;
                this.zzpK.notify();
            }
        }
    }

    public zzer zza(long j, long j2) {
        zzer zzerVar;
        synchronized (this.zzpK) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            final zzep zzepVar = new zzep();
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzeq.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (zzeq.this.zzpK) {
                        if (zzeq.this.zzBj != -2) {
                            return;
                        }
                        zzeq.this.zzBi = zzeq.this.zzep();
                        if (zzeq.this.zzBi == null) {
                            zzeq.this.zzr(4);
                            return;
                        }
                        if (!zzeq.this.zzeq() || zzeq.this.zzs(1)) {
                            zzepVar.zza(zzeq.this);
                            zzeq.this.zza(zzepVar);
                        } else {
                            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Ignoring adapter " + zzeq.this.zzBd + " as delayed impression is not supported");
                            zzeq.this.zzr(2);
                        }
                    }
                }
            });
            zza(jElapsedRealtime, this.zzBe, j, j2);
            zzerVar = new zzer(this.zzBg, this.zzBi, this.zzBd, zzepVar, this.zzBj, zzeo());
        }
        return zzerVar;
    }

    @Override // com.google.android.gms.internal.zzer.zza
    public void zza(int i, zzez zzezVar) {
        synchronized (this.zzpK) {
            this.zzBj = i;
            this.zzBk = zzezVar;
            this.zzpK.notify();
        }
    }

    @Override // com.google.android.gms.internal.zzer.zza
    public void zzr(int i) {
        synchronized (this.zzpK) {
            this.zzBj = i;
            this.zzpK.notify();
        }
    }
}
