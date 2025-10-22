package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

/* loaded from: classes2.dex */
public final class WebImage implements SafeParcelable {
    public static final Parcelable.Creator<WebImage> CREATOR = new zzb();
    private final int mVersionCode;
    private final Uri zzair;
    private final int zzov;
    private final int zzow;

    WebImage(int versionCode, Uri url, int width, int height) {
        this.mVersionCode = versionCode;
        this.zzair = url;
        this.zzov = width;
        this.zzow = height;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) other;
        return zzw.equal(this.zzair, webImage.zzair) && this.zzov == webImage.zzov && this.zzow == webImage.zzow;
    }

    public int getHeight() {
        return this.zzow;
    }

    public Uri getUrl() {
        return this.zzair;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int getWidth() {
        return this.zzov;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzair, Integer.valueOf(this.zzov), Integer.valueOf(this.zzow));
    }

    public String toString() {
        return String.format("Image %dx%d %s", Integer.valueOf(this.zzov), Integer.valueOf(this.zzow), this.zzair.toString());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
