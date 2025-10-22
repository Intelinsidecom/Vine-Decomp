package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes2.dex */
public class PlayLoggerContext implements SafeParcelable {
    public static final zze CREATOR = new zze();
    public final String packageName;
    public final int versionCode;
    public final String zzaYA;
    public final String zzaYB;
    public final boolean zzaYC;
    public final String zzaYD;
    public final boolean zzaYE;
    public final int zzaYF;
    public final int zzaYy;
    public final int zzaYz;

    public PlayLoggerContext(int versionCode, String packageName, int packageVersionCode, int logSource, String uploadAccountName, String loggingId, boolean logAndroidId, String logSourceName, boolean isAnonymous, int qosTier) {
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.zzaYy = packageVersionCode;
        this.zzaYz = logSource;
        this.zzaYA = uploadAccountName;
        this.zzaYB = loggingId;
        this.zzaYC = logAndroidId;
        this.zzaYD = logSourceName;
        this.zzaYE = isAnonymous;
        this.zzaYF = qosTier;
    }

    public PlayLoggerContext(String packageName, int packageVersionCode, int logSource, String logSourceName, String uploadAccountName, String loggingId, boolean isAnonymous, int qosTier) {
        this.versionCode = 1;
        this.packageName = (String) zzx.zzy(packageName);
        this.zzaYy = packageVersionCode;
        this.zzaYz = logSource;
        this.zzaYD = logSourceName;
        this.zzaYA = uploadAccountName;
        this.zzaYB = loggingId;
        this.zzaYC = !isAnonymous;
        this.zzaYE = isAnonymous;
        this.zzaYF = qosTier;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PlayLoggerContext)) {
            return false;
        }
        PlayLoggerContext playLoggerContext = (PlayLoggerContext) object;
        return this.versionCode == playLoggerContext.versionCode && this.packageName.equals(playLoggerContext.packageName) && this.zzaYy == playLoggerContext.zzaYy && this.zzaYz == playLoggerContext.zzaYz && zzw.equal(this.zzaYD, playLoggerContext.zzaYD) && zzw.equal(this.zzaYA, playLoggerContext.zzaYA) && zzw.equal(this.zzaYB, playLoggerContext.zzaYB) && this.zzaYC == playLoggerContext.zzaYC && this.zzaYE == playLoggerContext.zzaYE && this.zzaYF == playLoggerContext.zzaYF;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), this.packageName, Integer.valueOf(this.zzaYy), Integer.valueOf(this.zzaYz), this.zzaYD, this.zzaYA, this.zzaYB, Boolean.valueOf(this.zzaYC), Boolean.valueOf(this.zzaYE), Integer.valueOf(this.zzaYF));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayLoggerContext[");
        sb.append("versionCode=").append(this.versionCode).append(',');
        sb.append("package=").append(this.packageName).append(',');
        sb.append("packageVersionCode=").append(this.zzaYy).append(',');
        sb.append("logSource=").append(this.zzaYz).append(',');
        sb.append("logSourceName=").append(this.zzaYD).append(',');
        sb.append("uploadAccount=").append(this.zzaYA).append(',');
        sb.append("loggingId=").append(this.zzaYB).append(',');
        sb.append("logAndroidId=").append(this.zzaYC).append(',');
        sb.append("isAnonymous=").append(this.zzaYE).append(',');
        sb.append("qosTier=").append(this.zzaYF);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zze.zza(this, out, flags);
    }
}
