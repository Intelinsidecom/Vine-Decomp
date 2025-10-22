package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes.dex */
public final class EventParcel implements SafeParcelable {
    public static final zzj CREATOR = new zzj();
    public final String name;
    public final int versionCode;
    public final EventParams zzaSL;
    public final String zzaSM;
    public final long zzaSN;

    EventParcel(int versionCode, String name, EventParams params, String origin, long eventTimeInMilliseconds) {
        this.versionCode = versionCode;
        this.name = name;
        this.zzaSL = params;
        this.zzaSM = origin;
        this.zzaSN = eventTimeInMilliseconds;
    }

    public EventParcel(String name, EventParams params, String origin, long eventTimeInMilliseconds) {
        this.versionCode = 1;
        this.name = name;
        this.zzaSL = params;
        this.zzaSM = origin;
        this.zzaSN = eventTimeInMilliseconds;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "origin=" + this.zzaSM + ",name=" + this.name + ",params=" + this.zzaSL;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzj.zza(this, out, flags);
    }
}
