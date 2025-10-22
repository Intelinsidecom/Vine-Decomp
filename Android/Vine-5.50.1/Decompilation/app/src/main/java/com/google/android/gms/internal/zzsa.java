package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public final class zzsa {
    public static final Api.zzc<com.google.android.gms.signin.internal.zzi> zzTo = new Api.zzc<>();
    public static final Api.zzc<com.google.android.gms.signin.internal.zzi> zzatI = new Api.zzc<>();
    public static final Api.zza<com.google.android.gms.signin.internal.zzi, zzsd> zzTp = new Api.zza<com.google.android.gms.signin.internal.zzi, zzsd>() { // from class: com.google.android.gms.internal.zzsa.1
        @Override // com.google.android.gms.common.api.Api.zza
        public com.google.android.gms.signin.internal.zzi zza(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, zzsd zzsdVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new com.google.android.gms.signin.internal.zzi(context, looper, true, zzfVar, zzsdVar == null ? zzsd.zzbbH : zzsdVar, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
        }
    };
    static final Api.zza<com.google.android.gms.signin.internal.zzi, zza> zzbbE = new Api.zza<com.google.android.gms.signin.internal.zzi, zza>() { // from class: com.google.android.gms.internal.zzsa.2
        @Override // com.google.android.gms.common.api.Api.zza
        public com.google.android.gms.signin.internal.zzi zza(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, zza zzaVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new com.google.android.gms.signin.internal.zzi(context, looper, false, zzfVar, zzaVar.zzDK(), connectionCallbacks, onConnectionFailedListener);
        }
    };
    public static final Scope zzVA = new Scope("profile");
    public static final Scope zzVB = new Scope("email");
    public static final Api<zzsd> API = new Api<>("SignIn.API", zzTp, zzTo);
    public static final Api<zza> zzamM = new Api<>("SignIn.INTERNAL_API", zzbbE, zzatI);
    public static final zzsb zzbbF = new com.google.android.gms.signin.internal.zzh();

    public static class zza implements Api.ApiOptions.HasOptions {
        private final Bundle zzbbG;

        public Bundle zzDK() {
            return this.zzbbG;
        }
    }
}
