package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.ads.internal.request.zzj;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zze extends com.google.android.gms.common.internal.zzj<zzj> {
    final int zzGo;

    public zze(Context context, Looper looper, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener, int i) {
        super(context, looper, 8, com.google.android.gms.common.internal.zzf.zzas(context), connectionCallbacks, onConnectionFailedListener);
        this.zzGo = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.zzj
    /* renamed from: zzV, reason: merged with bridge method [inline-methods] */
    public zzj zzW(IBinder iBinder) {
        return zzj.zza.zzX(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgh() {
        return "com.google.android.gms.ads.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzj
    protected String zzgi() {
        return "com.google.android.gms.ads.internal.request.IAdRequestService";
    }

    public zzj zzgj() throws DeadObjectException {
        return (zzj) super.zzqs();
    }
}
