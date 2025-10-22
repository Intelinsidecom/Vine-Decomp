package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* loaded from: classes.dex */
public final class UiSettings {
    private final IUiSettingsDelegate zzaPV;

    UiSettings(IUiSettingsDelegate delegate) {
        this.zzaPV = delegate;
    }

    public void setMyLocationButtonEnabled(boolean enabled) {
        try {
            this.zzaPV.setMyLocationButtonEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setZoomControlsEnabled(boolean enabled) {
        try {
            this.zzaPV.setZoomControlsEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
