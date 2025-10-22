package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzi;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.internal.zzf;

/* loaded from: classes.dex */
public final class GoogleMap {
    private final IGoogleMapDelegate zzaOy;
    private UiSettings zzaOz;

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    protected GoogleMap(IGoogleMapDelegate map) {
        this.zzaOy = (IGoogleMapDelegate) zzx.zzy(map);
    }

    public final Marker addMarker(MarkerOptions options) {
        try {
            zzf zzfVarAddMarker = this.zzaOy.addMarker(options);
            if (zzfVarAddMarker != null) {
                return new Marker(zzfVarAddMarker);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update) {
        try {
            this.zzaOy.animateCamera(update.zzyS());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.zzaOy.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.zzaOz == null) {
                this.zzaOz = new UiSettings(this.zzaOy.getUiSettings());
            }
            return this.zzaOz;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMyLocationEnabled(boolean enabled) {
        try {
            this.zzaOy.setMyLocationEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapClickListener(final OnMapClickListener listener) {
        try {
            if (listener == null) {
                this.zzaOy.setOnMapClickListener(null);
            } else {
                this.zzaOy.setOnMapClickListener(new zzi.zza() { // from class: com.google.android.gms.maps.GoogleMap.8
                    @Override // com.google.android.gms.maps.internal.zzi
                    public void onMapClick(LatLng point) {
                        listener.onMapClick(point);
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
