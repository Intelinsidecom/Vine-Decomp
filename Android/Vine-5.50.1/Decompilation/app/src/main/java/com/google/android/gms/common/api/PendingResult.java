package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;

/* loaded from: classes2.dex */
public abstract class PendingResult<R extends Result> {

    public interface zza {
        void zzu(Status status);
    }

    public abstract void setResultCallback(ResultCallback<? super R> resultCallback);

    public void zza(zza zzaVar) {
        throw new UnsupportedOperationException();
    }

    public Integer zzoL() {
        throw new UnsupportedOperationException();
    }
}
