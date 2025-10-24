package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class PolylineOptions implements SafeParcelable {
    public static final zzi CREATOR = new zzi();
    private int mColor;
    private final int mVersionCode;
    private final List<LatLng> zzaQL;
    private boolean zzaQN;
    private float zzaQj;
    private boolean zzaQk;
    private float zzaQo;

    public PolylineOptions() {
        this.zzaQo = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = 1;
        this.zzaQL = new ArrayList();
    }

    PolylineOptions(int versionCode, List points, float width, int color, float zIndex, boolean visible, boolean geodesic) {
        this.zzaQo = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = versionCode;
        this.zzaQL = points;
        this.zzaQo = width;
        this.mColor = color;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQN = geodesic;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getColor() {
        return this.mColor;
    }

    public List<LatLng> getPoints() {
        return this.zzaQL;
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

    public boolean isGeodesic() {
        return this.zzaQN;
    }

    public boolean isVisible() {
        return this.zzaQk;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }
}
