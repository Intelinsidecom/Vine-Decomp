package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes2.dex */
public interface zzmm {

    public interface zza {
        void zzbz(int i);

        void zze(ConnectionResult connectionResult);

        void zzi(Bundle bundle);
    }

    void connect();

    void disconnect();

    void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    boolean isConnected();

    <A extends Api.zzb, R extends Result, T extends zzlx.zza<R, A>> T zza(T t);

    <A extends Api.zzb, T extends zzlx.zza<? extends Result, A>> T zzb(T t);
}
