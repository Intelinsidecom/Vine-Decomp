package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;

/* loaded from: classes.dex */
public final class GroundOverlayOptions implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    private final int mVersionCode;
    private float zzaQc;
    private float zzaQj;
    private boolean zzaQk;
    private BitmapDescriptor zzaQm;
    private LatLng zzaQn;
    private float zzaQo;
    private float zzaQp;
    private LatLngBounds zzaQq;
    private float zzaQr;
    private float zzaQs;
    private float zzaQt;

    public GroundOverlayOptions() {
        this.zzaQk = true;
        this.zzaQr = 0.0f;
        this.zzaQs = 0.5f;
        this.zzaQt = 0.5f;
        this.mVersionCode = 1;
    }

    GroundOverlayOptions(int versionCode, IBinder wrappedImage, LatLng location, float width, float height, LatLngBounds bounds, float bearing, float zIndex, boolean visible, float transparency, float anchorU, float anchorV) {
        this.zzaQk = true;
        this.zzaQr = 0.0f;
        this.zzaQs = 0.5f;
        this.zzaQt = 0.5f;
        this.mVersionCode = versionCode;
        this.zzaQm = new BitmapDescriptor(zzd.zza.zzbs(wrappedImage));
        this.zzaQn = location;
        this.zzaQo = width;
        this.zzaQp = height;
        this.zzaQq = bounds;
        this.zzaQc = bearing;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQr = transparency;
        this.zzaQs = anchorU;
        this.zzaQt = anchorV;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public float getAnchorU() {
        return this.zzaQs;
    }

    public float getAnchorV() {
        return this.zzaQt;
    }

    public float getBearing() {
        return this.zzaQc;
    }

    public LatLngBounds getBounds() {
        return this.zzaQq;
    }

    public float getHeight() {
        return this.zzaQp;
    }

    public LatLng getLocation() {
        return this.zzaQn;
    }

    public float getTransparency() {
        return this.zzaQr;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public float getWidth() {
        return this.zzaQo;
    }

    public float getZIndex() {
        return this.zzaQj;
    }

    public boolean isVisible() {
        return this.zzaQk;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }

    IBinder zzzu() {
        return this.zzaQm.zzyS().asBinder();
    }
}
