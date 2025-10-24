package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

/* loaded from: classes2.dex */
public final class WakeLockEvent extends zzf implements SafeParcelable {
    public static final Parcelable.Creator<WakeLockEvent> CREATOR = new zzh();
    private final long mTimeout;
    final int mVersionCode;
    private final String zzalZ;
    private final long zzaln;
    private int zzalo;
    private final long zzalv;
    private long zzalx = -1;
    private final int zzama;
    private final List<String> zzamb;
    private final String zzamc;
    private int zzamd;
    private final String zzame;
    private final String zzamf;
    private final float zzamg;

    WakeLockEvent(int versionCode, long timeMillis, int eventType, String wakelockName, int wakelockType, List<String> callingPackages, String eventKey, long elapsedRealtime, int deviceState, String secondaryWakeLockName, String hostPackageName, float beginPowerPercentage, long timeout) {
        this.mVersionCode = versionCode;
        this.zzaln = timeMillis;
        this.zzalo = eventType;
        this.zzalZ = wakelockName;
        this.zzame = secondaryWakeLockName;
        this.zzama = wakelockType;
        this.zzamb = callingPackages;
        this.zzamc = eventKey;
        this.zzalv = elapsedRealtime;
        this.zzamd = deviceState;
        this.zzamf = hostPackageName;
        this.zzamg = beginPowerPercentage;
        this.mTimeout = timeout;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public int getEventType() {
        return this.zzalo;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public long getTimeMillis() {
        return this.zzaln;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzh.zza(this, out, flags);
    }

    public String zzrB() {
        return this.zzalZ;
    }

    public String zzrC() {
        return this.zzame;
    }

    public int zzrD() {
        return this.zzama;
    }

    public List<String> zzrE() {
        return this.zzamb;
    }

    public int zzrF() {
        return this.zzamd;
    }

    public String zzrG() {
        return this.zzamf;
    }

    public float zzrH() {
        return this.zzamg;
    }

    public long zzrI() {
        return this.mTimeout;
    }

    public String zzru() {
        return this.zzamc;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public long zzrv() {
        return this.zzalx;
    }

    public long zzrx() {
        return this.zzalv;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public String zzry() {
        return "\t" + zzrB() + "\t" + zzrD() + "\t" + (zzrE() == null ? "" : TextUtils.join(",", zzrE())) + "\t" + zzrF() + "\t" + (zzrC() == null ? "" : zzrC()) + "\t" + (zzrG() == null ? "" : zzrG()) + "\t" + zzrH();
    }
}
