package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.clearcut.LogEventParcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzlw;

/* loaded from: classes2.dex */
public class zzlu extends com.google.android.gms.common.internal.zzj<zzlw> {
    public zzlu(Context context, Looper looper, com.google.android.gms.common.internal.zzf zzfVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 40, zzfVar, connectionCallbacks, onConnectionFailedListener);
    }

    public void zza(zzlv zzlvVar, LogEventParcelable logEventParcelable) throws RemoteException {
        zzqs().zza(zzlvVar, logEventParcelable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzaK, reason: merged with bridge method [inline-methods] */
    public zzlw zzW(IBinder iBinder) {
        return zzlw.zza.zzaM(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgh() {
        return "com.google.android.gms.clearcut.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgi() {
        return "com.google.android.gms.clearcut.internal.IClearcutLoggerService";
    }
}
