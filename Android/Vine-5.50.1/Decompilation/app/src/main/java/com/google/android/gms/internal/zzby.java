package com.google.android.gms.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.concurrent.Callable;

@zzha
/* loaded from: classes.dex */
public class zzby {
    private final Object zzpK = new Object();
    private boolean zzqh = false;
    private SharedPreferences zzvc = null;

    public void initialize(Context context) {
        synchronized (this.zzpK) {
            if (this.zzqh) {
                return;
            }
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                return;
            }
            this.zzvc = com.google.android.gms.ads.internal.zzp.zzbE().zzw(remoteContext);
            this.zzqh = true;
        }
    }

    public <T> T zzd(final zzbv<T> zzbvVar) {
        synchronized (this.zzpK) {
            if (this.zzqh) {
                return (T) zziz.zzb(new Callable<T>() { // from class: com.google.android.gms.internal.zzby.1
                    @Override // java.util.concurrent.Callable
                    public T call() {
                        return (T) zzbvVar.zza(zzby.this.zzvc);
                    }
                });
            }
            return zzbvVar.zzdk();
        }
    }
}
