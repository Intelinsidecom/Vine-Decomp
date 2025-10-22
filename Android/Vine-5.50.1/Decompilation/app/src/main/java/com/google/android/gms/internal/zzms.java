package com.google.android.gms.internal;

import android.util.Log;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

/* loaded from: classes2.dex */
public class zzms<R extends Result> extends com.google.android.gms.common.api.zze<R> implements ResultCallback<R> {
    private final Object zzafd;
    private com.google.android.gms.common.api.zzb<? super R, ? extends Result> zzahj;
    private zzms<? extends Result> zzahk;
    private ResultCallbacks<? super R> zzahl;
    private PendingResult<R> zzahm;

    private void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                Log.w("TransformedResultImpl", "Unable to release " + result, e);
            }
        }
    }

    private void zzpD() {
        if (this.zzahm != null) {
            if (this.zzahj == null && this.zzahl == null) {
                return;
            }
            this.zzahm.setResultCallback(this);
        }
    }

    @Override // com.google.android.gms.common.api.ResultCallback
    public void onResult(R result) {
        synchronized (this.zzafd) {
            if (!result.getStatus().isSuccess()) {
                zzz(result.getStatus());
                zzd(result);
            } else if (this.zzahj != null) {
                PendingResult<S> pendingResultZza = this.zzahj.zza(result);
                if (pendingResultZza == 0) {
                    zzz(new Status(13, "Transform returned null"));
                } else {
                    this.zzahk.zza(pendingResultZza);
                }
                zzd(result);
            } else if (this.zzahl != null) {
                this.zzahl.onSuccess(result);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void zza(PendingResult<?> pendingResult) {
        synchronized (this.zzafd) {
            this.zzahm = pendingResult;
            zzpD();
        }
    }

    public void zzz(Status status) {
        synchronized (this.zzafd) {
            if (this.zzahj != null) {
                Status statusZzw = this.zzahj.zzw(status);
                com.google.android.gms.common.internal.zzx.zzb(statusZzw, "onFailure must not return null");
                this.zzahk.zzz(statusZzw);
            } else if (this.zzahl != null) {
                this.zzahl.onFailure(status);
            }
        }
    }
}
