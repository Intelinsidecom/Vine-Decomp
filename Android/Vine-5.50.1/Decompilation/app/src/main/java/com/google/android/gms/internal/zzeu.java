package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzeu implements zzel {
    private final Context mContext;
    private zzeq zzBD;
    private final zzen zzBf;
    private final AdRequestInfoParcel zzBu;
    private final long zzBv;
    private final long zzBw;
    private final zzch zzoU;
    private final zzew zzpd;
    private final boolean zzrF;
    private final Object zzpK = new Object();
    private boolean zzBy = false;

    public zzeu(Context context, AdRequestInfoParcel adRequestInfoParcel, zzew zzewVar, zzen zzenVar, boolean z, long j, long j2, zzch zzchVar) {
        this.mContext = context;
        this.zzBu = adRequestInfoParcel;
        this.zzpd = zzewVar;
        this.zzBf = zzenVar;
        this.zzrF = z;
        this.zzBv = j;
        this.zzBw = j2;
        this.zzoU = zzchVar;
    }

    @Override // com.google.android.gms.internal.zzel
    public void cancel() {
        synchronized (this.zzpK) {
            this.zzBy = true;
            if (this.zzBD != null) {
                this.zzBD.cancel();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzel
    public zzer zzc(List<zzem> list) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Starting mediation.");
        ArrayList arrayList = new ArrayList();
        zzcf zzcfVarZzdu = this.zzoU.zzdu();
        for (zzem zzemVar : list) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Trying mediation network: " + zzemVar.zzAF);
            for (String str : zzemVar.zzAG) {
                zzcf zzcfVarZzdu2 = this.zzoU.zzdu();
                synchronized (this.zzpK) {
                    if (this.zzBy) {
                        return new zzer(-1);
                    }
                    this.zzBD = new zzeq(this.mContext, str, this.zzpd, this.zzBf, zzemVar, this.zzBu.zzGq, this.zzBu.zzqV, this.zzBu.zzqR, this.zzrF, this.zzBu.zzrj, this.zzBu.zzrl);
                    final zzer zzerVarZza = this.zzBD.zza(this.zzBv, this.zzBw);
                    if (zzerVarZza.zzBo == 0) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Adapter succeeded.");
                        this.zzoU.zzd("mediation_network_succeed", str);
                        if (!arrayList.isEmpty()) {
                            this.zzoU.zzd("mediation_networks_fail", TextUtils.join(",", arrayList));
                        }
                        this.zzoU.zza(zzcfVarZzdu2, "mls");
                        this.zzoU.zza(zzcfVarZzdu, "ttm");
                        return zzerVarZza;
                    }
                    arrayList.add(str);
                    this.zzoU.zza(zzcfVarZzdu2, "mlf");
                    if (zzerVarZza.zzBq != null) {
                        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzeu.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    zzerVarZza.zzBq.destroy();
                                } catch (RemoteException e) {
                                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not destroy mediation adapter.", e);
                                }
                            }
                        });
                    }
                }
            }
        }
        if (!arrayList.isEmpty()) {
            this.zzoU.zzd("mediation_networks_fail", TextUtils.join(",", arrayList));
        }
        return new zzer(1);
    }
}
