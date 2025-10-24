package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.measurement.internal.zzl;

/* loaded from: classes.dex */
public class zzn extends com.google.android.gms.common.internal.zzj<zzl> {
    public zzn(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 93, zzfVar, connectionCallbacks, onConnectionFailedListener);
    }

    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzdj, reason: merged with bridge method [inline-methods] */
    public zzl zzW(IBinder iBinder) {
        return zzl.zza.zzdi(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgh() {
        return "com.google.android.gms.measurement.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgi() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }
}
