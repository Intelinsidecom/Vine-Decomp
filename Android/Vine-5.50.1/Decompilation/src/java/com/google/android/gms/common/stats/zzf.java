package com.google.android.gms.common.stats;

/* loaded from: classes2.dex */
public abstract class zzf {
    public abstract int getEventType();

    public abstract long getTimeMillis();

    public String toString() {
        return getTimeMillis() + "\t" + getEventType() + "\t" + zzrv() + zzry();
    }

    public abstract long zzrv();

    public abstract String zzry();
}
