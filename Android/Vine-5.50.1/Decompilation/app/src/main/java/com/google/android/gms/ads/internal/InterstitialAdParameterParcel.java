package com.google.android.gms.ads.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class InterstitialAdParameterParcel implements SafeParcelable {
    public static final zzl CREATOR = new zzl();
    public final int versionCode;
    public final boolean zzqa;
    public final boolean zzqb;
    public final String zzqc;
    public final boolean zzqd;
    public final float zzqe;

    InterstitialAdParameterParcel(int versionCode, boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this.versionCode = versionCode;
        this.zzqa = transparentBackground;
        this.zzqb = hideStatusBar;
        this.zzqc = backgroundImage;
        this.zzqd = blur;
        this.zzqe = blurRadius;
    }

    public InterstitialAdParameterParcel(boolean transparentBackground, boolean hideStatusBar, String backgroundImage, boolean blur, float blurRadius) {
        this(2, transparentBackground, hideStatusBar, backgroundImage, blur, blurRadius);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzl.zza(this, out, flags);
    }
}
