package com.google.android.exoplayer.util;

/* loaded from: classes.dex */
public final class SystemClock implements Clock {
    @Override // com.google.android.exoplayer.util.Clock
    public long elapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }
}
