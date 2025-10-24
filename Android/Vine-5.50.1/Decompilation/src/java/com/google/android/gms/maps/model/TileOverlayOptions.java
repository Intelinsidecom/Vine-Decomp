package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.internal.zzi;

/* loaded from: classes.dex */
public final class TileOverlayOptions implements SafeParcelable {
    public static final zzo CREATOR = new zzo();
    private final int mVersionCode;
    private com.google.android.gms.maps.model.internal.zzi zzaQR;
    private TileProvider zzaQS;
    private boolean zzaQT;
    private float zzaQj;
    private boolean zzaQk;

    public TileOverlayOptions() {
        this.zzaQk = true;
        this.zzaQT = true;
        this.mVersionCode = 1;
    }

    TileOverlayOptions(int versionCode, IBinder delegate, boolean visible, float zIndex, boolean fadeIn) {
        this.zzaQk = true;
        this.zzaQT = true;
        this.mVersionCode = versionCode;
        this.zzaQR = zzi.zza.zzdh(delegate);
        this.zzaQS = this.zzaQR == null ? null : new TileProvider() { // from class: com.google.android.gms.maps.model.TileOverlayOptions.1
            private final com.google.android.gms.maps.model.internal.zzi zzaQU;

            {
                this.zzaQU = TileOverlayOptions.this.zzaQR;
            }
        };
        this.zzaQk = visible;
        this.zzaQj = zIndex;
        this.zzaQT = fadeIn;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean getFadeIn() {
        return this.zzaQT;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public float getZIndex() {
        return this.zzaQj;
    }

    public boolean isVisible() {
        return this.zzaQk;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzo.zza(this, out, flags);
    }

    IBinder zzzx() {
        return this.zzaQR.asBinder();
    }
}
