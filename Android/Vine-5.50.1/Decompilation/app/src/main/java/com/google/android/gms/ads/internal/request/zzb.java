package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.zza;
import com.google.android.gms.ads.internal.request.zzc;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzen;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzie;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzio;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzjg;
import com.google.android.gms.internal.zzjh;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzb extends zzil implements zzc.zza {
    private final Context mContext;
    zzen zzBf;
    private AdRequestInfoParcel zzBu;
    AdResponseParcel zzFd;
    private Runnable zzFe;
    private final Object zzFf = new Object();
    private final zza.InterfaceC0024zza zzGd;
    private final AdRequestInfoParcel.zza zzGe;
    zzir zzGf;
    private final zzan zzxV;

    @zzha
    static final class zza extends Exception {
        private final int zzFt;

        public zza(String str, int i) {
            super(str);
            this.zzFt = i;
        }

        public int getErrorCode() {
            return this.zzFt;
        }
    }

    public zzb(Context context, AdRequestInfoParcel.zza zzaVar, zzan zzanVar, zza.InterfaceC0024zza interfaceC0024zza) {
        this.zzGd = interfaceC0024zza;
        this.mContext = context;
        this.zzGe = zzaVar;
        this.zzxV = zzanVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zze(int i, String str) {
        if (i == 3 || i == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG(str);
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(str);
        }
        if (this.zzFd == null) {
            this.zzFd = new AdResponseParcel(i);
        } else {
            this.zzFd = new AdResponseParcel(i, this.zzFd.zzAU);
        }
        this.zzGd.zza(new zzie.zza(this.zzBu != null ? this.zzBu : new AdRequestInfoParcel(this.zzGe, null, -1L), this.zzFd, this.zzBf, null, i, -1L, this.zzFd.zzGR, null));
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
        synchronized (this.zzFf) {
            if (this.zzGf != null) {
                this.zzGf.cancel();
            }
        }
    }

    zzir zza(VersionInfoParcel versionInfoParcel, zzjg<AdRequestInfoParcel> zzjgVar) {
        return zzc.zza(this.mContext, versionInfoParcel, zzjgVar, this);
    }

    protected AdSizeParcel zzb(AdRequestInfoParcel adRequestInfoParcel) throws NumberFormatException, zza {
        if (this.zzFd.zzGQ == null) {
            throw new zza("The ad response must specify one of the supported ad sizes.", 0);
        }
        String[] strArrSplit = this.zzFd.zzGQ.split("x");
        if (strArrSplit.length != 2) {
            throw new zza("Invalid ad size format from the ad response: " + this.zzFd.zzGQ, 0);
        }
        try {
            int i = Integer.parseInt(strArrSplit[0]);
            int i2 = Integer.parseInt(strArrSplit[1]);
            for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzqV.zztX) {
                float f = this.mContext.getResources().getDisplayMetrics().density;
                int i3 = adSizeParcel.width == -1 ? (int) (adSizeParcel.widthPixels / f) : adSizeParcel.width;
                int i4 = adSizeParcel.height == -2 ? (int) (adSizeParcel.heightPixels / f) : adSizeParcel.height;
                if (i == i3 && i2 == i4) {
                    return new AdSizeParcel(adSizeParcel, adRequestInfoParcel.zzqV.zztX);
                }
            }
            throw new zza("The ad size from the ad response was not one of the requested sizes: " + this.zzFd.zzGQ, 0);
        } catch (NumberFormatException e) {
            throw new zza("Invalid ad size number from the ad response: " + this.zzFd.zzGQ, 0);
        }
    }

    @Override // com.google.android.gms.ads.internal.request.zzc.zza
    public void zzb(AdResponseParcel adResponseParcel) throws zza {
        JSONObject jSONObject;
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Received ad response.");
        this.zzFd = adResponseParcel;
        long jElapsedRealtime = zzp.zzbB().elapsedRealtime();
        synchronized (this.zzFf) {
            this.zzGf = null;
        }
        try {
            if (this.zzFd.errorCode != -2 && this.zzFd.errorCode != -3) {
                throw new zza("There was a problem getting an ad response. ErrorCode: " + this.zzFd.errorCode, this.zzFd.errorCode);
            }
            zzgd();
            AdSizeParcel adSizeParcelZzb = this.zzBu.zzqV.zztX != null ? zzb(this.zzBu) : null;
            zzp.zzbA().zzB(this.zzFd.zzGX);
            if (TextUtils.isEmpty(this.zzFd.zzGV)) {
                jSONObject = null;
            } else {
                try {
                    jSONObject = new JSONObject(this.zzFd.zzGV);
                } catch (Exception e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Error parsing the JSON for Active View.", e);
                }
            }
            this.zzGd.zza(new zzie.zza(this.zzBu, this.zzFd, this.zzBf, adSizeParcelZzb, -2, jElapsedRealtime, this.zzFd.zzGR, jSONObject));
            zzip.zzKO.removeCallbacks(this.zzFe);
        } catch (zza e2) {
            zze(e2.getErrorCode(), e2.getMessage());
            zzip.zzKO.removeCallbacks(this.zzFe);
        }
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("AdLoaderBackgroundTask started.");
        this.zzFe = new Runnable() { // from class: com.google.android.gms.ads.internal.request.zzb.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (zzb.this.zzFf) {
                    if (zzb.this.zzGf == null) {
                        return;
                    }
                    zzb.this.onStop();
                    zzb.this.zze(2, "Timed out waiting for ad response.");
                }
            }
        };
        zzip.zzKO.postDelayed(this.zzFe, zzbz.zzwB.get().longValue());
        final zzjh zzjhVar = new zzjh();
        long jElapsedRealtime = zzp.zzbB().elapsedRealtime();
        zzio.zza(new Runnable() { // from class: com.google.android.gms.ads.internal.request.zzb.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (zzb.this.zzFf) {
                    zzb.this.zzGf = zzb.this.zza(zzb.this.zzGe.zzqR, zzjhVar);
                    if (zzb.this.zzGf == null) {
                        zzb.this.zze(0, "Could not start the ad request service.");
                        zzip.zzKO.removeCallbacks(zzb.this.zzFe);
                    }
                }
            }
        });
        this.zzBu = new AdRequestInfoParcel(this.zzGe, this.zzxV.zzac().zzb(this.mContext), jElapsedRealtime);
        zzjhVar.zzg(this.zzBu);
    }

    protected void zzgd() throws zza {
        if (this.zzFd.errorCode == -3) {
            return;
        }
        if (TextUtils.isEmpty(this.zzFd.body)) {
            throw new zza("No fill from ad server.", 3);
        }
        zzp.zzbA().zza(this.mContext, this.zzFd.zzGy);
        if (this.zzFd.zzGN) {
            try {
                this.zzBf = new zzen(this.zzFd.body);
            } catch (JSONException e) {
                throw new zza("Could not parse mediation config: " + this.zzFd.body, 0);
            }
        }
    }
}
