package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd;

/* loaded from: classes.dex */
public final class MarkerOptions implements SafeParcelable {
    public static final zzf CREATOR = new zzf();
    private float mAlpha;
    private final int mVersionCode;
    private LatLng zzaPH;
    private String zzaQB;
    private BitmapDescriptor zzaQC;
    private boolean zzaQD;
    private boolean zzaQE;
    private float zzaQF;
    private float zzaQG;
    private float zzaQH;
    private boolean zzaQk;
    private float zzaQs;
    private float zzaQt;
    private String zzank;

    public MarkerOptions() {
        this.zzaQs = 0.5f;
        this.zzaQt = 1.0f;
        this.zzaQk = true;
        this.zzaQE = false;
        this.zzaQF = 0.0f;
        this.zzaQG = 0.5f;
        this.zzaQH = 0.0f;
        this.mAlpha = 1.0f;
        this.mVersionCode = 1;
    }

    MarkerOptions(int versionCode, LatLng position, String title, String snippet, IBinder wrappedIcon, float anchorU, float anchorV, boolean draggable, boolean visible, boolean flat, float rotation, float infoWindowAnchorU, float infoWindowAnchorV, float alpha) {
        this.zzaQs = 0.5f;
        this.zzaQt = 1.0f;
        this.zzaQk = true;
        this.zzaQE = false;
        this.zzaQF = 0.0f;
        this.zzaQG = 0.5f;
        this.zzaQH = 0.0f;
        this.mAlpha = 1.0f;
        this.mVersionCode = versionCode;
        this.zzaPH = position;
        this.zzank = title;
        this.zzaQB = snippet;
        this.zzaQC = wrappedIcon == null ? null : new BitmapDescriptor(zzd.zza.zzbs(wrappedIcon));
        this.zzaQs = anchorU;
        this.zzaQt = anchorV;
        this.zzaQD = draggable;
        this.zzaQk = visible;
        this.zzaQE = flat;
        this.zzaQF = rotation;
        this.zzaQG = infoWindowAnchorU;
        this.zzaQH = infoWindowAnchorV;
        this.mAlpha = alpha;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public float getAnchorU() {
        return this.zzaQs;
    }

    public float getAnchorV() {
        return this.zzaQt;
    }

    public float getInfoWindowAnchorU() {
        return this.zzaQG;
    }

    public float getInfoWindowAnchorV() {
        return this.zzaQH;
    }

    public LatLng getPosition() {
        return this.zzaPH;
    }

    public float getRotation() {
        return this.zzaQF;
    }

    public String getSnippet() {
        return this.zzaQB;
    }

    public String getTitle() {
        return this.zzank;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isDraggable() {
        return this.zzaQD;
    }

    public boolean isFlat() {
        return this.zzaQE;
    }

    public boolean isVisible() {
        return this.zzaQk;
    }

    public MarkerOptions position(LatLng position) {
        this.zzaPH = position;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }

    IBinder zzzv() {
        if (this.zzaQC == null) {
            return null;
        }
        return this.zzaQC.zzyS().asBinder();
    }
}
