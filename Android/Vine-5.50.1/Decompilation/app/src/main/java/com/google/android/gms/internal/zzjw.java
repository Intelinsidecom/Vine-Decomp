package com.google.android.gms.internal;

import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.net.URI;
import java.net.URISyntaxException;

@zzha
/* loaded from: classes.dex */
public class zzjw extends WebViewClient {
    private final zzgn zzFr;
    private final String zzMR;
    private boolean zzMS = false;
    private final zzjn zzps;

    public zzjw(zzgn zzgnVar, zzjn zzjnVar, String str) {
        this.zzMR = zzaO(str);
        this.zzps = zzjnVar;
        this.zzFr = zzgnVar;
    }

    private String zzaO(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return str.endsWith("/") ? str.substring(0, str.length() - 1) : str;
        } catch (IndexOutOfBoundsException e) {
            com.google.android.gms.ads.internal.util.client.zzb.e(e.getMessage());
            return str;
        }
    }

    @Override // android.webkit.WebViewClient
    public void onLoadResource(WebView view, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("JavascriptAdWebViewClient::onLoadResource: " + url);
        if (zzaN(url)) {
            return;
        }
        this.zzps.zzhC().onLoadResource(this.zzps.getWebView(), url);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView view, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("JavascriptAdWebViewClient::onPageFinished: " + url);
        if (this.zzMS) {
            return;
        }
        this.zzFr.zzfS();
        this.zzMS = true;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("JavascriptAdWebViewClient::shouldOverrideUrlLoading: " + url);
        if (!zzaN(url)) {
            return this.zzps.zzhC().shouldOverrideUrlLoading(this.zzps.getWebView(), url);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("shouldOverrideUrlLoading: received passback url");
        return true;
    }

    protected boolean zzaN(String str) {
        boolean z = false;
        String strZzaO = zzaO(str);
        if (!TextUtils.isEmpty(strZzaO)) {
            try {
                URI uri = new URI(strZzaO);
                if ("passback".equals(uri.getScheme())) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("Passback received");
                    this.zzFr.zzfT();
                    z = true;
                } else if (!TextUtils.isEmpty(this.zzMR)) {
                    URI uri2 = new URI(this.zzMR);
                    String host = uri2.getHost();
                    String host2 = uri.getHost();
                    String path = uri2.getPath();
                    String path2 = uri.getPath();
                    if (com.google.android.gms.common.internal.zzw.equal(host, host2) && com.google.android.gms.common.internal.zzw.equal(path, path2)) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Passback received");
                        this.zzFr.zzfT();
                        z = true;
                    }
                }
            } catch (URISyntaxException e) {
                com.google.android.gms.ads.internal.util.client.zzb.e(e.getMessage());
            }
        }
        return z;
    }
}
