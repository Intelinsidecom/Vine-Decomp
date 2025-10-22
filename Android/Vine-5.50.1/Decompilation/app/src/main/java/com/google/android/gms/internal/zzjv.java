package com.google.android.gms.internal;

import android.view.View;
import android.webkit.WebChromeClient;

@zzha
/* loaded from: classes.dex */
public final class zzjv extends zzjt {
    public zzjv(zzjn zzjnVar) {
        super(zzjnVar);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback customViewCallback) {
        zza(view, requestedOrientation, customViewCallback);
    }
}
