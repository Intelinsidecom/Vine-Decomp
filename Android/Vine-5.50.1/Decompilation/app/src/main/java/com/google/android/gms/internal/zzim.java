package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

@zzha
/* loaded from: classes.dex */
public class zzim extends Handler {
    public zzim(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) throws Exception {
        try {
            super.handleMessage(msg);
        } catch (Exception e) {
            com.google.android.gms.ads.internal.zzp.zzbA().zzb((Throwable) e, false);
            throw e;
        }
    }
}
