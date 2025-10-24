package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@zzha
/* loaded from: classes.dex */
public class zzet implements zzel {
    private final Context mContext;
    private final zzen zzBf;
    private final AdRequestInfoParcel zzBu;
    private final long zzBv;
    private final long zzBw;
    private final int zzBx;
    private final zzew zzpd;
    private final boolean zzrF;
    private final Object zzpK = new Object();
    private boolean zzBy = false;
    private final Map<zzje<zzer>, zzeq> zzBz = new HashMap();

    public zzet(Context context, AdRequestInfoParcel adRequestInfoParcel, zzew zzewVar, zzen zzenVar, boolean z, long j, long j2, int i) {
        this.mContext = context;
        this.zzBu = adRequestInfoParcel;
        this.zzpd = zzewVar;
        this.zzBf = zzenVar;
        this.zzrF = z;
        this.zzBv = j;
        this.zzBw = j2;
        this.zzBx = i;
    }

    private void zza(final zzje<zzer> zzjeVar) {
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzet.2
            @Override // java.lang.Runnable
            public void run() {
                for (zzje zzjeVar2 : zzet.this.zzBz.keySet()) {
                    if (zzjeVar2 != zzjeVar) {
                        ((zzeq) zzet.this.zzBz.get(zzjeVar2)).cancel();
                    }
                }
            }
        });
    }

    private zzer zzd(List<zzje<zzer>> list) {
        zzer zzerVar;
        synchronized (this.zzpK) {
            if (this.zzBy) {
                return new zzer(-1);
            }
            for (zzje<zzer> zzjeVar : list) {
                try {
                    zzerVar = zzjeVar.get();
                } catch (InterruptedException | ExecutionException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Exception while processing an adapter; continuing with other adapters", e);
                }
                if (zzerVar != null && zzerVar.zzBo == 0) {
                    zza(zzjeVar);
                    return zzerVar;
                }
            }
            zza((zzje<zzer>) null);
            return new zzer(1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0081 A[Catch: RemoteException -> 0x008a, all -> 0x00a2, InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x00c5, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x00a2, blocks: (B:18:0x0040, B:20:0x0046, B:22:0x004e, B:24:0x0052, B:26:0x0056, B:28:0x005c, B:36:0x0081, B:39:0x008b), top: B:54:0x0040 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.google.android.gms.internal.zzer zze(java.util.List<com.google.android.gms.internal.zzje<com.google.android.gms.internal.zzer>> r16) {
        /*
            Method dump skipped, instructions count: 203
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzet.zze(java.util.List):com.google.android.gms.internal.zzer");
    }

    @Override // com.google.android.gms.internal.zzel
    public void cancel() {
        synchronized (this.zzpK) {
            this.zzBy = true;
            Iterator<zzeq> it = this.zzBz.values().iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
        }
    }

    @Override // com.google.android.gms.internal.zzel
    public zzer zzc(List<zzem> list) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Starting mediation.");
        ExecutorService executorServiceNewCachedThreadPool = Executors.newCachedThreadPool();
        ArrayList arrayList = new ArrayList();
        for (zzem zzemVar : list) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaG("Trying mediation network: " + zzemVar.zzAF);
            Iterator<String> it = zzemVar.zzAG.iterator();
            while (it.hasNext()) {
                final zzeq zzeqVar = new zzeq(this.mContext, it.next(), this.zzpd, this.zzBf, zzemVar, this.zzBu.zzGq, this.zzBu.zzqV, this.zzBu.zzqR, this.zzrF, this.zzBu.zzrj, this.zzBu.zzrl);
                zzje<zzer> zzjeVarZza = zzio.zza(executorServiceNewCachedThreadPool, new Callable<zzer>() { // from class: com.google.android.gms.internal.zzet.1
                    @Override // java.util.concurrent.Callable
                    /* renamed from: zzet, reason: merged with bridge method [inline-methods] */
                    public zzer call() throws Exception {
                        synchronized (zzet.this.zzpK) {
                            if (zzet.this.zzBy) {
                                return null;
                            }
                            return zzeqVar.zza(zzet.this.zzBv, zzet.this.zzBw);
                        }
                    }
                });
                this.zzBz.put(zzjeVarZza, zzeqVar);
                arrayList.add(zzjeVarZza);
            }
        }
        switch (this.zzBx) {
            case 2:
                return zze(arrayList);
            default:
                return zzd(arrayList);
        }
    }
}
