package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/* loaded from: classes.dex */
public final class StreetViewPanoramaOptions implements SafeParcelable {
    public static final zzb CREATOR = new zzb();
    private final int mVersionCode;
    private Boolean zzaOS;
    private Boolean zzaOY;
    private StreetViewPanoramaCamera zzaPF;
    private String zzaPG;
    private LatLng zzaPH;
    private Integer zzaPI;
    private Boolean zzaPJ;
    private Boolean zzaPK;
    private Boolean zzaPL;

    public StreetViewPanoramaOptions() {
        this.zzaPJ = true;
        this.zzaOY = true;
        this.zzaPK = true;
        this.zzaPL = true;
        this.mVersionCode = 1;
    }

    StreetViewPanoramaOptions(int versionCode, StreetViewPanoramaCamera camera, String panoId, LatLng position, Integer radius, byte userNavigationEnabled, byte zoomGesturesEnabled, byte panningGesturesEnabled, byte streetNamesEnabled, byte useViewLifecycleInFragment) {
        this.zzaPJ = true;
        this.zzaOY = true;
        this.zzaPK = true;
        this.zzaPL = true;
        this.mVersionCode = versionCode;
        this.zzaPF = camera;
        this.zzaPH = position;
        this.zzaPI = radius;
        this.zzaPG = panoId;
        this.zzaPJ = com.google.android.gms.maps.internal.zza.zza(userNavigationEnabled);
        this.zzaOY = com.google.android.gms.maps.internal.zza.zza(zoomGesturesEnabled);
        this.zzaPK = com.google.android.gms.maps.internal.zza.zza(panningGesturesEnabled);
        this.zzaPL = com.google.android.gms.maps.internal.zza.zza(streetNamesEnabled);
        this.zzaOS = com.google.android.gms.maps.internal.zza.zza(useViewLifecycleInFragment);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getPanoramaId() {
        return this.zzaPG;
    }

    public LatLng getPosition() {
        return this.zzaPH;
    }

    public Integer getRadius() {
        return this.zzaPI;
    }

    public StreetViewPanoramaCamera getStreetViewPanoramaCamera() {
        return this.zzaPF;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }

    byte zzyW() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOS);
    }

    byte zzza() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOY);
    }

    byte zzzl() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPJ);
    }

    byte zzzm() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPK);
    }

    byte zzzn() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPL);
    }
}
