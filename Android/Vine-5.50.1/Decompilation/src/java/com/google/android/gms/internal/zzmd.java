package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzlx;
import com.google.android.gms.internal.zzmg;
import com.google.android.gms.internal.zzmi;

/* loaded from: classes2.dex */
public class zzmd implements zzmh {
    private final zzmi zzafD;

    public zzmd(zzmi zzmiVar) {
        this.zzafD = zzmiVar;
    }

    private <A extends Api.zzb> void zza(zzmg.zze<A> zzeVar) throws DeadObjectException {
        this.zzafD.zzafp.zzb(zzeVar);
        Api.zzb zzbVarZza = this.zzafD.zzafp.zza(zzeVar.zzoA());
        if (zzbVarZza.isConnected() || !this.zzafD.zzagJ.containsKey(zzeVar.zzoA())) {
            zzeVar.zzb(zzbVarZza);
        } else {
            zzeVar.zzx(new Status(17));
        }
    }

    @Override // com.google.android.gms.internal.zzmh
    public void begin() {
    }

    @Override // com.google.android.gms.internal.zzmh
    public void connect() {
    }

    @Override // com.google.android.gms.internal.zzmh
    public void disconnect() {
        this.zzafD.zzj(null);
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnected(Bundle connectionHint) {
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnectionSuspended(int cause) {
        this.zzafD.zzj(null);
        this.zzafD.zzagN.zzbz(cause);
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t) {
        return (T) zzb(t);
    }

    @Override // com.google.android.gms.internal.zzmh
    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t) {
        try {
            zza((zzmg.zze) t);
        } catch (DeadObjectException e) {
            this.zzafD.zza(new zzmi.zza(this) { // from class: com.google.android.gms.internal.zzmd.1
                @Override // com.google.android.gms.internal.zzmi.zza
                public void zzpc() {
                    zzmd.this.onConnectionSuspended(1);
                }
            });
        }
        return t;
    }
}
