package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public class LogEvent implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    public final String tag;
    public final int versionCode;
    public final long zzaYn;
    public final long zzaYo;
    public final byte[] zzaYp;
    public final Bundle zzaYq;

    LogEvent(int versionCode, long eventTime, long eventUptime, String tag, byte[] sourceExtensionBytes, Bundle keyValuePairs) {
        this.versionCode = versionCode;
        this.zzaYn = eventTime;
        this.zzaYo = eventUptime;
        this.tag = tag;
        this.zzaYp = sourceExtensionBytes;
        this.zzaYq = keyValuePairs;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tag=").append(this.tag).append(",");
        sb.append("eventTime=").append(this.zzaYn).append(",");
        sb.append("eventUptime=").append(this.zzaYo).append(",");
        if (this.zzaYq != null && !this.zzaYq.isEmpty()) {
            sb.append("keyValues=");
            for (String str : this.zzaYq.keySet()) {
                sb.append("(").append(str).append(",");
                sb.append(this.zzaYq.getString(str)).append(")");
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
