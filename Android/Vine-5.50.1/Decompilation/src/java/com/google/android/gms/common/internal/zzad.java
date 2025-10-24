package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* loaded from: classes2.dex */
public class zzad<T extends IInterface> extends zzj<T> {
    private final Api.zzd<T> zzakG;

    public zzad(Context context, Looper looper, int i, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, zzf zzfVar, Api.zzd zzdVar) {
        super(context, looper, i, zzfVar, connectionCallbacks, onConnectionFailedListener);
        this.zzakG = zzdVar;
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected T zzW(IBinder iBinder) {
        return (T) this.zzakG.zzW(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected void zzc(int i, T t) {
        this.zzakG.zza(i, t);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgh() {
        return this.zzakG.zzgh();
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgi() {
        return this.zzakG.zzgi();
    }
}
