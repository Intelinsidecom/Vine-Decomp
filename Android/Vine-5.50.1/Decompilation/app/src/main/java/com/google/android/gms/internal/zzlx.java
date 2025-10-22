package com.google.android.gms.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzmg;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
public class zzlx {

    public static abstract class zza<R extends Result, A extends Api.zzb> extends zzly<R> implements zzmg.zze<A> {
        private final Api.zzc<A> zzacX;
        private AtomicReference<zzmg.zzd> zzafc;

        protected zza(Api.zzc<A> zzcVar, GoogleApiClient googleApiClient) {
            super(((GoogleApiClient) com.google.android.gms.common.internal.zzx.zzb(googleApiClient, "GoogleApiClient must not be null")).getLooper());
            this.zzafc = new AtomicReference<>();
            this.zzacX = (Api.zzc) com.google.android.gms.common.internal.zzx.zzy(zzcVar);
        }

        private void zza(RemoteException remoteException) {
            zzx(new Status(8, remoteException.getLocalizedMessage(), null));
        }

        protected abstract void zza(A a) throws RemoteException;

        @Override // com.google.android.gms.internal.zzmg.zze
        public void zza(zzmg.zzd zzdVar) {
            this.zzafc.set(zzdVar);
        }

        @Override // com.google.android.gms.internal.zzmg.zze
        public final void zzb(A a) throws DeadObjectException {
            try {
                zza((zza<R, A>) a);
            } catch (DeadObjectException e) {
                zza(e);
                throw e;
            } catch (RemoteException e2) {
                zza(e2);
            }
        }

        @Override // com.google.android.gms.internal.zzmg.zze
        public final Api.zzc<A> zzoA() {
            return this.zzacX;
        }

        @Override // com.google.android.gms.internal.zzmg.zze
        public void zzoP() {
            setResultCallback(null);
        }

        @Override // com.google.android.gms.internal.zzmg.zze
        public int zzoQ() {
            return 0;
        }

        @Override // com.google.android.gms.internal.zzly
        protected void zzoR() {
            zzmg.zzd andSet = this.zzafc.getAndSet(null);
            if (andSet != null) {
                andSet.zzc(this);
            }
        }

        @Override // com.google.android.gms.internal.zzmg.zze
        public final void zzx(Status status) {
            com.google.android.gms.common.internal.zzx.zzb(!status.isSuccess(), "Failed result must not be success");
            zzb((zza<R, A>) zzc(status));
        }
    }
}
