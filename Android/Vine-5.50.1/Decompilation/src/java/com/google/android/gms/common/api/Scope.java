package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes2.dex */
public final class Scope implements SafeParcelable {
    public static final Parcelable.Creator<Scope> CREATOR = new zzc();
    final int mVersionCode;
    private final String zzaeW;

    Scope(int versionCode, String scopeUri) {
        zzx.zzh(scopeUri, "scopeUri must not be null or empty");
        this.mVersionCode = versionCode;
        this.zzaeW = scopeUri;
    }

    public Scope(String scopeUri) {
        this(1, scopeUri);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Scope) {
            return this.zzaeW.equals(((Scope) o).zzaeW);
        }
        return false;
    }

    public int hashCode() {
        return this.zzaeW.hashCode();
    }

    public String toString() {
        return this.zzaeW;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }

    public String zzoM() {
        return this.zzaeW;
    }
}
