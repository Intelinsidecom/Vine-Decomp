package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.internal.zzgq;
import com.google.android.gms.internal.zzie;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzha
/* loaded from: classes.dex */
public class zzgu extends zzil {
    private Future<zzie> zzFA;
    private final zzgq.zza zzFb;
    private final zzie.zza zzFc;
    private final AdResponseParcel zzFd;
    private final zzgv zzFz;
    private final Object zzpK;

    public zzgu(Context context, com.google.android.gms.ads.internal.zzn zznVar, zzbc zzbcVar, zzie.zza zzaVar, zzan zzanVar, zzgq.zza zzaVar2) {
        this(zzaVar, zzaVar2, new zzgv(context, zznVar, zzbcVar, new zziu(context), zzanVar, zzaVar));
    }

    zzgu(zzie.zza zzaVar, zzgq.zza zzaVar2, zzgv zzgvVar) {
        this.zzpK = new Object();
        this.zzFc = zzaVar;
        this.zzFd = zzaVar.zzJL;
        this.zzFb = zzaVar2;
        this.zzFz = zzgvVar;
    }

    private zzie zzE(int i) {
        return new zzie(this.zzFc.zzJK.zzGq, null, null, i, null, null, this.zzFd.orientation, this.zzFd.zzAU, this.zzFc.zzJK.zzGt, false, null, null, null, null, null, this.zzFd.zzGO, this.zzFc.zzqV, this.zzFd.zzGM, this.zzFc.zzJH, this.zzFd.zzGR, this.zzFd.zzGS, this.zzFc.zzJE, null);
    }

    @Override // com.google.android.gms.internal.zzil
    public void onStop() {
        synchronized (this.zzpK) {
            if (this.zzFA != null) {
                this.zzFA.cancel(true);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzil
    public void zzbp() throws ExecutionException, InterruptedException, TimeoutException {
        int i;
        final zzie zzieVarZzE;
        try {
            synchronized (this.zzpK) {
                this.zzFA = zzio.zza(this.zzFz);
            }
            zzieVarZzE = this.zzFA.get(60000L, TimeUnit.MILLISECONDS);
            i = -2;
        } catch (InterruptedException e) {
            zzieVarZzE = null;
            i = -1;
        } catch (CancellationException e2) {
            zzieVarZzE = null;
            i = -1;
        } catch (ExecutionException e3) {
            i = 0;
            zzieVarZzE = null;
        } catch (TimeoutException e4) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Timed out waiting for native ad.");
            this.zzFA.cancel(true);
            i = 2;
            zzieVarZzE = null;
        }
        if (zzieVarZzE == null) {
            zzieVarZzE = zzE(i);
        }
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzgu.1
            @Override // java.lang.Runnable
            public void run() {
                zzgu.this.zzFb.zzb(zzieVarZzE);
            }
        });
    }
}
