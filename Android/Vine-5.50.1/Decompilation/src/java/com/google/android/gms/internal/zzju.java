package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzha
/* loaded from: classes.dex */
public class zzju extends zzjo {
    public zzju(zzjn zzjnVar, boolean z) {
        super(zzjnVar, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (!(webView instanceof zzjn)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Tried to intercept request from a WebView that wasn't an AdWebView.");
                return super.shouldInterceptRequest(webView, url);
            }
            zzjn zzjnVar = (zzjn) webView;
            zzjnVar.zzhC().zzfd();
            String str = zzjnVar.zzaP().zztW ? zzbz.zzvK.get() : zzjnVar.zzhG() ? zzbz.zzvJ.get() : zzbz.zzvI.get();
            com.google.android.gms.ads.internal.util.client.zzb.v("shouldInterceptRequest(" + str + ")");
            return zzd(zzjnVar.getContext(), this.zzps.zzhF().afmaVersion, str);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not fetch MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }

    protected WebResourceResponse zzd(Context context, String str, String str2) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        HashMap map = new HashMap();
        map.put("User-Agent", com.google.android.gms.ads.internal.zzp.zzbx().zzd(context, str));
        map.put("Cache-Control", "max-stale=3600");
        String str3 = new zziu(context).zza(str2, map).get(60L, TimeUnit.SECONDS);
        if (str3 == null) {
            return null;
        }
        return new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(str3.getBytes("UTF-8")));
    }
}
