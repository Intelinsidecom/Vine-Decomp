package com.google.android.gms.maps.model;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes.dex */
public final class Marker {
    private final com.google.android.gms.maps.model.internal.zzf zzaQA;

    public Marker(com.google.android.gms.maps.model.internal.zzf delegate) {
        this.zzaQA = (com.google.android.gms.maps.model.internal.zzf) zzx.zzy(delegate);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Marker)) {
            return false;
        }
        try {
            return this.zzaQA.zzh(((Marker) other).zzaQA);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public int hashCode() {
        try {
            return this.zzaQA.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
