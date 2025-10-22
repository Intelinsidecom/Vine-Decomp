package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public class zzga implements zzfy {
    private final Context mContext;
    final Set<WebView> zzEq = Collections.synchronizedSet(new HashSet());

    public zzga(Context context) {
        this.mContext = context;
    }

    @Override // com.google.android.gms.internal.zzfy
    public void zza(String str, final String str2, final String str3) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Fetching assets for the given html");
        zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzga.1
            @Override // java.lang.Runnable
            public void run() {
                final WebView webViewZzfE = zzga.this.zzfE();
                webViewZzfE.setWebViewClient(new WebViewClient() { // from class: com.google.android.gms.internal.zzga.1.1
                    @Override // android.webkit.WebViewClient
                    public void onPageFinished(WebView view, String url) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Loading assets have finished");
                        zzga.this.zzEq.remove(webViewZzfE);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaH("Loading assets have failed.");
                        zzga.this.zzEq.remove(webViewZzfE);
                    }
                });
                zzga.this.zzEq.add(webViewZzfE);
                webViewZzfE.loadDataWithBaseURL(str2, str3, "text/html", "UTF-8", null);
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Fetching assets finished.");
            }
        });
    }

    public WebView zzfE() {
        WebView webView = new WebView(this.mContext);
        webView.getSettings().setJavaScriptEnabled(true);
        return webView;
    }
}
