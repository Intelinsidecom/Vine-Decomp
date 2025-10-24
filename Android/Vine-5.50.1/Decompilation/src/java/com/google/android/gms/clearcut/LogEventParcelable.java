package com.google.android.gms.clearcut;

import android.os.Parcel;
import com.google.android.gms.clearcut.zza;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzv;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zztp;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class LogEventParcelable implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    public final int versionCode;
    public final zztp.zzd zzadA;
    public final zza.zzb zzadB;
    public final zza.zzb zzadC;
    public PlayLoggerContext zzadx;
    public byte[] zzady;
    public int[] zzadz;

    LogEventParcelable(int versionCode, PlayLoggerContext playLoggerContext, byte[] logEventBytes, int[] testCodes) {
        this.versionCode = versionCode;
        this.zzadx = playLoggerContext;
        this.zzady = logEventBytes;
        this.zzadz = testCodes;
        this.zzadA = null;
        this.zzadB = null;
        this.zzadC = null;
    }

    public LogEventParcelable(PlayLoggerContext playLoggerContext, zztp.zzd logEvent, zza.zzb extensionProducer, zza.zzb clientVisualElementsProducer, int[] testCodes) {
        this.versionCode = 1;
        this.zzadx = playLoggerContext;
        this.zzadA = logEvent;
        this.zzadB = extensionProducer;
        this.zzadC = clientVisualElementsProducer;
        this.zzadz = testCodes;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LogEventParcelable)) {
            return false;
        }
        LogEventParcelable logEventParcelable = (LogEventParcelable) other;
        return this.versionCode == logEventParcelable.versionCode && zzw.equal(this.zzadx, logEventParcelable.zzadx) && Arrays.equals(this.zzady, logEventParcelable.zzady) && Arrays.equals(this.zzadz, logEventParcelable.zzadz) && zzw.equal(this.zzadA, logEventParcelable.zzadA) && zzw.equal(this.zzadB, logEventParcelable.zzadB) && zzw.equal(this.zzadC, logEventParcelable.zzadC);
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.versionCode), this.zzadx, this.zzady, this.zzadz, this.zzadA, this.zzadB, this.zzadC);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("LogEventParcelable[");
        sb.append(this.versionCode);
        sb.append(", ");
        sb.append(this.zzadx);
        sb.append(", ");
        sb.append(this.zzady == null ? null : new String(this.zzady));
        sb.append(", ");
        sb.append(this.zzadz == null ? (String) null : zzv.zzcF(", ").zza(Arrays.asList(this.zzadz)));
        sb.append(", ");
        sb.append(this.zzadA);
        sb.append(", ");
        sb.append(this.zzadB);
        sb.append(", ");
        sb.append(this.zzadC);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
