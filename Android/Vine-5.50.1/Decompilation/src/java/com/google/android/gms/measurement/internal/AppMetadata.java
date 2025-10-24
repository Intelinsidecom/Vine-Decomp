package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes.dex */
public class AppMetadata implements SafeParcelable {
    public static final zzb CREATOR = new zzb();
    public final String packageName;
    public final int versionCode;
    public final String zzaKi;
    public final String zzaSn;
    public final String zzaSo;
    public final long zzaSp;
    public final long zzaSq;
    public final String zzaSr;
    public final boolean zzaSs;

    AppMetadata(int versionCode, String packageName, String gmpAppId, String appVersion, String appStore, long gmpVersion, long devCertHash, String healthMonitor, boolean measurementEnabled) {
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.zzaSn = gmpAppId;
        this.zzaKi = appVersion;
        this.zzaSo = appStore;
        this.zzaSp = gmpVersion;
        this.zzaSq = devCertHash;
        this.zzaSr = healthMonitor;
        if (versionCode >= 3) {
            this.zzaSs = measurementEnabled;
        } else {
            this.zzaSs = true;
        }
    }

    AppMetadata(String packageName, String gmpAppId, String appVersion, String appStore, long gmpVersion, long devCertHash, String healthMonitor, boolean measurementEnabled) {
        com.google.android.gms.common.internal.zzx.zzcG(packageName);
        this.versionCode = 3;
        this.packageName = packageName;
        this.zzaSn = TextUtils.isEmpty(gmpAppId) ? null : gmpAppId;
        this.zzaKi = appVersion;
        this.zzaSo = appStore;
        this.zzaSp = gmpVersion;
        this.zzaSq = devCertHash;
        this.zzaSr = healthMonitor;
        this.zzaSs = measurementEnabled;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
