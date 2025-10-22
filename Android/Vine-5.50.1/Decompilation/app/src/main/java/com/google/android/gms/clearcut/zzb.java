package com.google.android.gms.clearcut;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public interface zzb {
    PendingResult<Status> zza(GoogleApiClient googleApiClient, LogEventParcelable logEventParcelable);

    boolean zza(GoogleApiClient googleApiClient, long j, TimeUnit timeUnit);
}
