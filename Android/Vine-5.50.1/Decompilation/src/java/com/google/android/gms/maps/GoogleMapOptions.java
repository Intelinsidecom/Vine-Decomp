package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.CameraPosition;

/* loaded from: classes.dex */
public final class GoogleMapOptions implements SafeParcelable {
    public static final zza CREATOR = new zza();
    private final int mVersionCode;
    private Boolean zzaOR;
    private Boolean zzaOS;
    private int zzaOT;
    private CameraPosition zzaOU;
    private Boolean zzaOV;
    private Boolean zzaOW;
    private Boolean zzaOX;
    private Boolean zzaOY;
    private Boolean zzaOZ;
    private Boolean zzaPa;
    private Boolean zzaPb;
    private Boolean zzaPc;
    private Boolean zzaPd;

    public GoogleMapOptions() {
        this.zzaOT = -1;
        this.mVersionCode = 1;
    }

    GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled, byte liteMode, byte mapToolbarEnabled, byte ambientEnabled) {
        this.zzaOT = -1;
        this.mVersionCode = versionCode;
        this.zzaOR = com.google.android.gms.maps.internal.zza.zza(zOrderOnTop);
        this.zzaOS = com.google.android.gms.maps.internal.zza.zza(useViewLifecycleInFragment);
        this.zzaOT = mapType;
        this.zzaOU = camera;
        this.zzaOV = com.google.android.gms.maps.internal.zza.zza(zoomControlsEnabled);
        this.zzaOW = com.google.android.gms.maps.internal.zza.zza(compassEnabled);
        this.zzaOX = com.google.android.gms.maps.internal.zza.zza(scrollGesturesEnabled);
        this.zzaOY = com.google.android.gms.maps.internal.zza.zza(zoomGesturesEnabled);
        this.zzaOZ = com.google.android.gms.maps.internal.zza.zza(tiltGesturesEnabled);
        this.zzaPa = com.google.android.gms.maps.internal.zza.zza(rotateGesturesEnabled);
        this.zzaPb = com.google.android.gms.maps.internal.zza.zza(liteMode);
        this.zzaPc = com.google.android.gms.maps.internal.zza.zza(mapToolbarEnabled);
        this.zzaPd = com.google.android.gms.maps.internal.zza.zza(ambientEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray typedArrayObtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_mapType)) {
            googleMapOptions.mapType(typedArrayObtainAttributes.getInt(R.styleable.MapAttrs_mapType, -1));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_zOrderOnTop)) {
            googleMapOptions.zOrderOnTop(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_zOrderOnTop, false));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_useViewLifecycle)) {
            googleMapOptions.useViewLifecycleInFragment(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_useViewLifecycle, false));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiCompass)) {
            googleMapOptions.compassEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiCompass, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiRotateGestures)) {
            googleMapOptions.rotateGesturesEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiRotateGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiScrollGestures)) {
            googleMapOptions.scrollGesturesEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiScrollGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiTiltGestures)) {
            googleMapOptions.tiltGesturesEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiTiltGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiZoomGestures)) {
            googleMapOptions.zoomGesturesEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiZoomGestures, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiZoomControls)) {
            googleMapOptions.zoomControlsEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiZoomControls, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_liteMode)) {
            googleMapOptions.liteMode(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_liteMode, false));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_uiMapToolbar)) {
            googleMapOptions.mapToolbarEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_uiMapToolbar, true));
        }
        if (typedArrayObtainAttributes.hasValue(R.styleable.MapAttrs_ambientEnabled)) {
            googleMapOptions.ambientEnabled(typedArrayObtainAttributes.getBoolean(R.styleable.MapAttrs_ambientEnabled, false));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attrs));
        typedArrayObtainAttributes.recycle();
        return googleMapOptions;
    }

    public GoogleMapOptions ambientEnabled(boolean enabled) {
        this.zzaPd = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions camera(CameraPosition camera) {
        this.zzaOU = camera;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean enabled) {
        this.zzaOW = Boolean.valueOf(enabled);
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.zzaOU;
    }

    public int getMapType() {
        return this.zzaOT;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public GoogleMapOptions liteMode(boolean enabled) {
        this.zzaPb = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions mapToolbarEnabled(boolean enabled) {
        this.zzaPc = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions mapType(int mapType) {
        this.zzaOT = mapType;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        this.zzaPa = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        this.zzaOX = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        this.zzaOZ = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        this.zzaOS = Boolean.valueOf(useViewLifecycleInFragment);
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    public GoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        this.zzaOR = Boolean.valueOf(zOrderOnTop);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean enabled) {
        this.zzaOV = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        this.zzaOY = Boolean.valueOf(enabled);
        return this;
    }

    byte zzyV() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOR);
    }

    byte zzyW() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOS);
    }

    byte zzyX() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOV);
    }

    byte zzyY() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOW);
    }

    byte zzyZ() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOX);
    }

    byte zzza() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOY);
    }

    byte zzzb() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaOZ);
    }

    byte zzzc() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPa);
    }

    byte zzzd() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPb);
    }

    byte zzze() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPc);
    }

    byte zzzf() {
        return com.google.android.gms.maps.internal.zza.zze(this.zzaPd);
    }
}
