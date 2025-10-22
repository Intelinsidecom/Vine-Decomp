package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes.dex */
public abstract class zzg<T> {
    private final String zzatD;
    private T zzatE;

    /* loaded from: classes2.dex */
    public static class zza extends Exception {
        public zza(String str) {
            super(str);
        }

        public zza(String str, Throwable th) {
            super(str, th);
        }
    }

    protected zzg(String str) {
        this.zzatD = str;
    }

    protected final T zzaA(Context context) throws zza {
        if (this.zzatE == null) {
            zzx.zzy(context);
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                throw new zza("Could not get remote context.");
            }
            try {
                this.zzatE = zzd((IBinder) remoteContext.getClassLoader().loadClass(this.zzatD).newInstance());
            } catch (ClassNotFoundException e) {
                throw new zza("Could not load creator class.", e);
            } catch (IllegalAccessException e2) {
                throw new zza("Could not access creator.", e2);
            } catch (InstantiationException e3) {
                throw new zza("Could not instantiate creator.", e3);
            }
        }
        return this.zzatE;
    }

    protected abstract T zzd(IBinder iBinder);
}
