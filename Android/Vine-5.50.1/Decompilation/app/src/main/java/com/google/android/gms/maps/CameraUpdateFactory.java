package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* loaded from: classes.dex */
public final class CameraUpdateFactory {
    private static ICameraUpdateFactoryDelegate zzaOx;

    public static CameraUpdate newLatLngZoom(LatLng latLng, float zoom) {
        try {
            return new CameraUpdate(zzyT().newLatLngZoom(latLng, zoom));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static void zza(ICameraUpdateFactoryDelegate iCameraUpdateFactoryDelegate) {
        zzaOx = (ICameraUpdateFactoryDelegate) zzx.zzy(iCameraUpdateFactoryDelegate);
    }

    private static ICameraUpdateFactoryDelegate zzyT() {
        return (ICameraUpdateFactoryDelegate) zzx.zzb(zzaOx, "CameraUpdateFactory is not initialized");
    }
}
