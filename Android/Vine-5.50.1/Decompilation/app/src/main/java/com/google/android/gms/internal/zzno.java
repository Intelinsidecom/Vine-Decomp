package com.google.android.gms.internal;

import android.os.SystemClock;

/* loaded from: classes2.dex */
public final class zzno implements zznl {
    private static zzno zzamk;

    public static synchronized zznl zzrM() {
        if (zzamk == null) {
            zzamk = new zzno();
        }
        return zzamk;
    }

    @Override // com.google.android.gms.internal.zznl
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.google.android.gms.internal.zznl
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.google.android.gms.internal.zznl
    public long nanoTime() {
        return System.nanoTime();
    }
}
