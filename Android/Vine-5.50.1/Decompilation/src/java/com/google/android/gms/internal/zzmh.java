package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlx;

/* loaded from: classes2.dex */
public interface zzmh {
    void begin();

    void connect();

    void disconnect();

    void onConnected(Bundle bundle);

    void onConnectionSuspended(int i);

    <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t);

    void zza(ConnectionResult connectionResult, Api<?> api, int i);

    <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t);
}
