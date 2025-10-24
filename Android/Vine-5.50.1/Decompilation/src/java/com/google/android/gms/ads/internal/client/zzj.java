package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.internal.client.zzu;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public final class zzj extends zzu.zza {
    private final AppEventListener zzub;

    public zzj(AppEventListener appEventListener) {
        this.zzub = appEventListener;
    }

    @Override // com.google.android.gms.ads.internal.client.zzu
    public void onAppEvent(String name, String info) {
        this.zzub.onAppEvent(name, info);
    }
}
