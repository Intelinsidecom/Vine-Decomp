package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

/* loaded from: classes.dex */
public class StreetViewPanoramaCamera implements SafeParcelable {
    public static final zzj CREATOR = new zzj();
    public final float bearing;
    private final int mVersionCode;
    public final float tilt;
    public final float zoom;
    private StreetViewPanoramaOrientation zzaQP;

    StreetViewPanoramaCamera(int versionCode, float zoom, float tilt, float bearing) {
        zzx.zzb(-90.0f <= tilt && tilt <= 90.0f, "Tilt needs to be between -90 and 90 inclusive");
        this.mVersionCode = versionCode;
        this.zoom = ((double) zoom) <= 0.0d ? 0.0f : zoom;
        this.tilt = tilt + 0.0f;
        this.bearing = (((double) bearing) <= 0.0d ? (bearing % 360.0f) + 360.0f : bearing) % 360.0f;
        this.zzaQP = new StreetViewPanoramaOrientation.Builder().tilt(tilt).bearing(bearing).build();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreetViewPanoramaCamera)) {
            return false;
        }
        StreetViewPanoramaCamera streetViewPanoramaCamera = (StreetViewPanoramaCamera) o;
        return Float.floatToIntBits(this.zoom) == Float.floatToIntBits(streetViewPanoramaCamera.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(streetViewPanoramaCamera.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(streetViewPanoramaCamera.bearing);
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        return zzw.hashCode(Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing));
    }

    public String toString() {
        return zzw.zzx(this).zzg("zoom", Float.valueOf(this.zoom)).zzg("tilt", Float.valueOf(this.tilt)).zzg("bearing", Float.valueOf(this.bearing)).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzj.zza(this, out, flags);
    }
}
