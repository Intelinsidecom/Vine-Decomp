package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class PolygonOptions implements SafeParcelable {
    public static final zzh CREATOR = new zzh();
    private final int mVersionCode;
    private final List<LatLng> zzaQL;
    private final List<List<LatLng>> zzaQM;
    private boolean zzaQN;
    private float zzaQg;
    private int zzaQh;
    private int zzaQi;
    private float zzaQj;
    private boolean zzaQk;

    public PolygonOptions() {
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = 1;
        this.zzaQL = new ArrayList();
        this.zzaQM = new ArrayList();
    }

    PolygonOptions(int versionCode, List<LatLng> points, List holes, float strokeWidth, int strokeColor, int fillColor, float zIndex, boolean visible, boolean geodesic) {
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = versionCode;
        this.zzaQL = points;
        this.zzaQM = holes;
        this.zzaQg = strokeWidth;
        this.zzaQh = strokeColor;
        this.zzaQi = fillColor;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQN = geodesic;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getFillColor() {
        return this.zzaQi;
    }

    public List<LatLng> getPoints() {
        return this.zzaQL;
    }

    public int getStrokeColor() {
        return this.zzaQh;
    }

    public float getStrokeWidth() {
        return this.zzaQg;
    }

    int getVersionCode() {
        return this.mVersionCode;
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
        zzh.zza(this, out, flags);
    }

    List zzzw() {
        return this.zzaQM;
    }
}
