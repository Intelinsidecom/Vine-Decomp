package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

/* loaded from: classes2.dex */
public final class ConnectionEvent extends zzf implements SafeParcelable {
    public static final Parcelable.Creator<ConnectionEvent> CREATOR = new zza();
    final int mVersionCode;
    private final long zzaln;
    private int zzalo;
    private final String zzalp;
    private final String zzalq;
    private final String zzalr;
    private final String zzals;
    private final String zzalt;
    private final String zzalu;
    private final long zzalv;
    private final long zzalw;
    private long zzalx;

    ConnectionEvent(int versionCode, long timeMillis, int eventType, String callingProcess, String callingService, String targetProcess, String targetService, String stackTrace, String connKey, long elapsedRealtime, long heapAlloc) {
        this.mVersionCode = versionCode;
        this.zzaln = timeMillis;
        this.zzalo = eventType;
        this.zzalp = callingProcess;
        this.zzalq = callingService;
        this.zzalr = targetProcess;
        this.zzals = targetService;
        this.zzalx = -1L;
        this.zzalt = stackTrace;
        this.zzalu = connKey;
        this.zzalv = elapsedRealtime;
        this.zzalw = heapAlloc;
    }

    public ConnectionEvent(long timeMillis, int eventType, String callingProcess, String callingService, String targetProcess, String targetService, String stackTrace, String connKey, long elapsedRealtime, long heapAlloc) {
        this(1, timeMillis, eventType, callingProcess, callingService, targetProcess, targetService, stackTrace, connKey, elapsedRealtime, heapAlloc);
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
        zza.zza(this, out, flags);
    }

    public String zzrp() {
        return this.zzalp;
    }

    public String zzrq() {
        return this.zzalq;
    }

    public String zzrr() {
        return this.zzalr;
    }

    public String zzrs() {
        return this.zzals;
    }

    public String zzrt() {
        return this.zzalt;
    }

    public String zzru() {
        return this.zzalu;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public long zzrv() {
        return this.zzalx;
    }

    public long zzrw() {
        return this.zzalw;
    }

    public long zzrx() {
        return this.zzalv;
    }

    @Override // com.google.android.gms.common.stats.zzf
    public String zzry() {
        return "\t" + zzrp() + "/" + zzrq() + "\t" + zzrr() + "/" + zzrs() + "\t" + (this.zzalt == null ? "" : this.zzalt) + "\t" + zzrw();
    }
}
