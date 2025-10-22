package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlx;
import java.util.Collections;

/* loaded from: classes2.dex */
public class zzmf implements zzmh {
    private final zzmi zzafD;

    public zzmf(zzmi zzmiVar) {
        this.zzafD = zzmiVar;
    }

    @Override // com.google.android.gms.internal.zzmh
    public void begin() {
        this.zzafD.zzpy();
        this.zzafD.zzafp.zzagq = Collections.emptySet();
    }

    @Override // com.google.android.gms.internal.zzmh
    public void connect() {
        this.zzafD.zzpw();
    }

    @Override // com.google.android.gms.internal.zzmh
    public void disconnect() {
        this.zzafD.zzafp.zzpn();
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnected(Bundle connectionHint) {
    }

    @Override // com.google.android.gms.internal.zzmh
    public void onConnectionSuspended(int cause) {
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t) {
        this.zzafD.zzafp.zzagj.add(t);
        return t;
    }

    @Override // com.google.android.gms.internal.zzmh
    public void zza(ConnectionResult connectionResult, Api<?> api, int i) {
    }

    @Override // com.google.android.gms.internal.zzmh
    public <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
