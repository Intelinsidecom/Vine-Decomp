package com.google.android.gms.ads.internal.formats;

import android.os.Parcel;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class NativeAdOptionsParcel implements SafeParcelable {
    public static final zzi CREATOR = new zzi();
    public final int versionCode;
    public final boolean zzyc;
    public final int zzyd;
    public final boolean zzye;

    public NativeAdOptionsParcel(int versionCode, boolean shouldReturnUrlsForImageAssets, int imageOrientation, boolean shouldRequestMultipleImages) {
        this.versionCode = versionCode;
        this.zzyc = shouldReturnUrlsForImageAssets;
        this.zzyd = imageOrientation;
        this.zzye = shouldRequestMultipleImages;
    }

    public NativeAdOptionsParcel(NativeAdOptions options) {
        this(1, options.shouldReturnUrlsForImageAssets(), options.getImageOrientation(), options.shouldRequestMultipleImages());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }
}
