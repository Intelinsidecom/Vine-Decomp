package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/* loaded from: classes2.dex */
public class zzlz implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final Api<?> zzafm;
    private final int zzafn;
    private zzmi zzafo;

    public zzlz(Api<?> api, int i) {
        this.zzafm = api;
        this.zzafn = i;
    }

    private void zzoT() {
        com.google.android.gms.common.internal.zzx.zzb(this.zzafo, "Callbacks must be attached to a GoogleApiClient instance before connecting the client.");
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        zzoT();
        this.zzafo.onConnected(connectionHint);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        zzoT();
        this.zzafo.zza(result, this.zzafm, this.zzafn);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
        zzoT();
        this.zzafo.onConnectionSuspended(cause);
    }

    public void zza(zzmi zzmiVar) {
        this.zzafo = zzmiVar;
    }
}
