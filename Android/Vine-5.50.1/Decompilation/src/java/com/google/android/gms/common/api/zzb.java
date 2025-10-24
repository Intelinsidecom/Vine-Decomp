package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;

/* loaded from: classes2.dex */
public abstract class zzb<R extends Result, S extends Result> {
    public abstract PendingResult<S> zza(R r);

    public Status zzw(Status status) {
        return status;
    }
}
