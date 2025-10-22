package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import com.google.android.gms.ads.internal.overlay.AdOverlayInfoParcel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@zzha
/* loaded from: classes.dex */
public class zzjo extends WebViewClient {
    private static final String[] zzMe = {"UNKNOWN", "HOST_LOOKUP", "UNSUPPORTED_AUTH_SCHEME", "AUTHENTICATION", "PROXY_AUTHENTICATION", "CONNECT", "IO", "TIMEOUT", "REDIRECT_LOOP", "UNSUPPORTED_SCHEME", "FAILED_SSL_HANDSHAKE", "BAD_URL", "FILE", "FILE_NOT_FOUND", "TOO_MANY_REQUESTS"};
    private static final String[] zzMf = {"NOT_YET_VALID", "EXPIRED", "ID_MISMATCH", "UNTRUSTED", "DATE_INVALID", "INVALID"};
    private zzfs zzCk;
    private zza zzFl;
    private final HashMap<String, List<zzdl>> zzMg;
    private com.google.android.gms.ads.internal.overlay.zzg zzMh;
    private zzb zzMi;
    private boolean zzMj;
    private boolean zzMk;
    private com.google.android.gms.ads.internal.overlay.zzn zzMl;
    private final zzfq zzMm;
    private boolean zzMn;
    private boolean zzMo;
    private boolean zzMp;
    private boolean zzMq;
    private int zzMr;
    private final Object zzpK;
    protected zzjn zzps;
    private boolean zzrE;
    private com.google.android.gms.ads.internal.client.zza zztn;
    private zzdp zzyZ;
    private zzdh zzyy;
    private com.google.android.gms.ads.internal.zze zzzb;
    private zzfm zzzc;
    private zzdn zzze;

    public interface zza {
        void zza(zzjn zzjnVar, boolean z);
    }

    public interface zzb {
        void zzbh();
    }

    private static class zzc implements com.google.android.gms.ads.internal.overlay.zzg {
        private com.google.android.gms.ads.internal.overlay.zzg zzMh;
        private zzjn zzMt;

        public zzc(zzjn zzjnVar, com.google.android.gms.ads.internal.overlay.zzg zzgVar) {
            this.zzMt = zzjnVar;
            this.zzMh = zzgVar;
        }

        @Override // com.google.android.gms.ads.internal.overlay.zzg
        public void zzaX() {
            this.zzMh.zzaX();
            this.zzMt.zzhw();
        }

        @Override // com.google.android.gms.ads.internal.overlay.zzg
        public void zzaY() {
            this.zzMh.zzaY();
            this.zzMt.zzfg();
        }
    }

    private class zzd implements zzdl {
        private zzd() {
        }

        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            if (map.keySet().contains("start")) {
                zzjo.this.zzhR();
            } else if (map.keySet().contains("stop")) {
                zzjo.this.zzhS();
            } else if (map.keySet().contains("cancel")) {
                zzjo.this.zzhT();
            }
        }
    }

    public zzjo(zzjn zzjnVar, boolean z) {
        this(zzjnVar, z, new zzfq(zzjnVar, zzjnVar.zzhy(), new zzbr(zzjnVar.getContext())), null);
    }

    zzjo(zzjn zzjnVar, boolean z, zzfq zzfqVar, zzfm zzfmVar) {
        this.zzMg = new HashMap<>();
        this.zzpK = new Object();
        this.zzMj = false;
        this.zzps = zzjnVar;
        this.zzrE = z;
        this.zzMm = zzfqVar;
        this.zzzc = zzfmVar;
    }

    private void zza(Context context, String str, String str2, String str3) {
        if (zzbz.zzwt.get().booleanValue()) {
            Bundle bundle = new Bundle();
            bundle.putString("err", str);
            bundle.putString("code", str2);
            bundle.putString("host", zzaK(str3));
            com.google.android.gms.ads.internal.zzp.zzbx().zza(context, this.zzps.zzhF().afmaVersion, "gmob-apps", bundle, true);
        }
    }

    private String zzaK(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Uri uri = Uri.parse(str);
        return uri.getHost() != null ? uri.getHost() : "";
    }

    private static boolean zzg(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzhR() {
        synchronized (this.zzpK) {
            this.zzMk = true;
        }
        this.zzMr++;
        zzhU();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzhS() {
        this.zzMr--;
        zzhU();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzhT() {
        this.zzMq = true;
        zzhU();
    }

    @Override // android.webkit.WebViewClient
    public final void onLoadResource(WebView webView, String url) {
        com.google.android.gms.ads.internal.util.client.zzb.v("Loading resource: " + url);
        Uri uri = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(uri.getScheme()) && "mobileads.google.com".equalsIgnoreCase(uri.getHost())) {
            zzh(uri);
        }
    }

    @Override // android.webkit.WebViewClient
    public final void onPageFinished(WebView webView, String url) {
        synchronized (this.zzpK) {
            if (this.zzMo) {
                com.google.android.gms.ads.internal.util.client.zzb.v("Blank page loaded, 1...");
                this.zzps.zzhH();
            } else {
                this.zzMp = true;
                zzhU();
            }
        }
    }

    @Override // android.webkit.WebViewClient
    public final void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        zza(this.zzps.getContext(), "http_err", (errorCode >= 0 || (-errorCode) + (-1) >= zzMe.length) ? String.valueOf(errorCode) : zzMe[(-errorCode) - 1], failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override // android.webkit.WebViewClient
    public final void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (error != null) {
            int primaryError = error.getPrimaryError();
            zza(this.zzps.getContext(), "ssl_err", (primaryError < 0 || primaryError >= zzMf.length) ? String.valueOf(primaryError) : zzMf[primaryError], com.google.android.gms.ads.internal.zzp.zzbz().zza(error));
        }
        super.onReceivedSslError(view, handler, error);
    }

    public final void reset() {
        synchronized (this.zzpK) {
            this.zzMg.clear();
            this.zztn = null;
            this.zzMh = null;
            this.zzFl = null;
            this.zzyy = null;
            this.zzMj = false;
            this.zzrE = false;
            this.zzMk = false;
            this.zzze = null;
            this.zzMl = null;
            this.zzMi = null;
            if (this.zzzc != null) {
                this.zzzc.zzp(true);
                this.zzzc = null;
            }
            this.zzMn = false;
        }
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 222:
                return true;
            default:
                return false;
        }
    }

    @Override // android.webkit.WebViewClient
    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Uri uri;
        com.google.android.gms.ads.internal.util.client.zzb.v("AdWebView shouldOverrideUrlLoading: " + url);
        Uri uriZza = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(uriZza.getScheme()) && "mobileads.google.com".equalsIgnoreCase(uriZza.getHost())) {
            zzh(uriZza);
        } else {
            if (this.zzMj && webView == this.zzps.getWebView() && zzg(uriZza)) {
                if (!this.zzMn) {
                    this.zzMn = true;
                    if (this.zztn != null && zzbz.zzwb.get().booleanValue()) {
                        this.zztn.onAdClicked();
                    }
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }
            if (this.zzps.getWebView().willNotDraw()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("AdWebView unable to handle URL: " + url);
            } else {
                try {
                    zzan zzanVarZzhE = this.zzps.zzhE();
                    if (zzanVarZzhE != null && zzanVarZzhE.zzb(uriZza)) {
                        uriZza = zzanVarZzhE.zza(uriZza, this.zzps.getContext());
                    }
                    uri = uriZza;
                } catch (zzao e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unable to append parameter to URL: " + url);
                    uri = uriZza;
                }
                if (this.zzzb == null || this.zzzb.zzbg()) {
                    zza(new AdLauncherIntentInfoParcel("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
                } else {
                    this.zzzb.zzp(url);
                }
            }
        }
        return true;
    }

    public void zzG(boolean z) {
        this.zzMj = z;
    }

    public void zza(int i, int i2, boolean z) {
        this.zzMm.zzf(i, i2);
        if (this.zzzc != null) {
            this.zzzc.zza(i, i2, z);
        }
    }

    public final void zza(AdLauncherIntentInfoParcel adLauncherIntentInfoParcel) {
        boolean zZzhG = this.zzps.zzhG();
        zza(new AdOverlayInfoParcel(adLauncherIntentInfoParcel, (!zZzhG || this.zzps.zzaP().zztW) ? this.zztn : null, zZzhG ? null : this.zzMh, this.zzMl, this.zzps.zzhF()));
    }

    public void zza(AdOverlayInfoParcel adOverlayInfoParcel) {
        com.google.android.gms.ads.internal.zzp.zzbv().zza(this.zzps.getContext(), adOverlayInfoParcel, this.zzzc != null ? this.zzzc.zzeC() : false ? false : true);
    }

    public void zza(zza zzaVar) {
        this.zzFl = zzaVar;
    }

    public void zza(zzb zzbVar) {
        this.zzMi = zzbVar;
    }

    public void zza(String str, zzdl zzdlVar) {
        synchronized (this.zzpK) {
            List<zzdl> copyOnWriteArrayList = this.zzMg.get(str);
            if (copyOnWriteArrayList == null) {
                copyOnWriteArrayList = new CopyOnWriteArrayList<>();
                this.zzMg.put(str, copyOnWriteArrayList);
            }
            copyOnWriteArrayList.add(zzdlVar);
        }
    }

    public final void zza(boolean z, int i) {
        zza(new AdOverlayInfoParcel((!this.zzps.zzhG() || this.zzps.zzaP().zztW) ? this.zztn : null, this.zzMh, this.zzMl, this.zzps, z, i, this.zzps.zzhF()));
    }

    public final void zza(boolean z, int i, String str) {
        boolean zZzhG = this.zzps.zzhG();
        zza(new AdOverlayInfoParcel((!zZzhG || this.zzps.zzaP().zztW) ? this.zztn : null, zZzhG ? null : new zzc(this.zzps, this.zzMh), this.zzyy, this.zzMl, this.zzps, z, i, str, this.zzps.zzhF(), this.zzze));
    }

    public final void zza(boolean z, int i, String str, String str2) {
        boolean zZzhG = this.zzps.zzhG();
        zza(new AdOverlayInfoParcel((!zZzhG || this.zzps.zzaP().zztW) ? this.zztn : null, zZzhG ? null : new zzc(this.zzps, this.zzMh), this.zzyy, this.zzMl, this.zzps, z, i, str, str2, this.zzps.zzhF(), this.zzze));
    }

    public void zzb(com.google.android.gms.ads.internal.client.zza zzaVar, com.google.android.gms.ads.internal.overlay.zzg zzgVar, zzdh zzdhVar, com.google.android.gms.ads.internal.overlay.zzn zznVar, boolean z, zzdn zzdnVar, zzdp zzdpVar, com.google.android.gms.ads.internal.zze zzeVar, zzfs zzfsVar) {
        if (zzeVar == null) {
            zzeVar = new com.google.android.gms.ads.internal.zze(false);
        }
        this.zzzc = new zzfm(this.zzps, zzfsVar);
        zza("/appEvent", new zzdg(zzdhVar));
        zza("/backButton", zzdk.zzyI);
        zza("/canOpenURLs", zzdk.zzyA);
        zza("/canOpenIntents", zzdk.zzyB);
        zza("/click", zzdk.zzyC);
        zza("/close", zzdk.zzyD);
        zza("/customClose", zzdk.zzyE);
        zza("/instrument", zzdk.zzyL);
        zza("/delayPageLoaded", new zzd());
        zza("/httpTrack", zzdk.zzyF);
        zza("/log", zzdk.zzyG);
        zza("/mraid", new zzdr(zzeVar, this.zzzc));
        zza("/mraidLoaded", this.zzMm);
        zza("/open", new zzds(zzdnVar, zzeVar, this.zzzc));
        zza("/precache", zzdk.zzyK);
        zza("/touch", zzdk.zzyH);
        zza("/video", zzdk.zzyJ);
        if (zzdpVar != null) {
            zza("/setInterstitialProperties", new zzdo(zzdpVar));
        }
        this.zztn = zzaVar;
        this.zzMh = zzgVar;
        this.zzyy = zzdhVar;
        this.zzze = zzdnVar;
        this.zzMl = zznVar;
        this.zzzb = zzeVar;
        this.zzCk = zzfsVar;
        this.zzyZ = zzdpVar;
        zzG(z);
        this.zzMn = false;
    }

    public void zzb(String str, zzdl zzdlVar) {
        synchronized (this.zzpK) {
            List<zzdl> list = this.zzMg.get(str);
            if (list == null) {
                return;
            }
            list.remove(zzdlVar);
        }
    }

    public boolean zzcb() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzrE;
        }
        return z;
    }

    public void zze(int i, int i2) {
        if (this.zzzc != null) {
            this.zzzc.zze(i, i2);
        }
    }

    public void zze(zzjn zzjnVar) {
        this.zzps = zzjnVar;
    }

    public final void zzfd() {
        synchronized (this.zzpK) {
            this.zzMj = false;
            this.zzrE = true;
            zzip.runOnUiThread(new Runnable() { // from class: com.google.android.gms.internal.zzjo.1
                @Override // java.lang.Runnable
                public void run() {
                    zzjo.this.zzps.zzhM();
                    com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = zzjo.this.zzps.zzhA();
                    if (zzdVarZzhA != null) {
                        zzdVarZzhA.zzfd();
                    }
                    if (zzjo.this.zzMi != null) {
                        zzjo.this.zzMi.zzbh();
                        zzjo.this.zzMi = null;
                    }
                }
            });
        }
    }

    public void zzh(Uri uri) {
        String path = uri.getPath();
        List<zzdl> list = this.zzMg.get(path);
        if (list == null) {
            com.google.android.gms.ads.internal.util.client.zzb.v("No GMSG handler found for GMSG: " + uri);
            return;
        }
        Map<String, String> mapZze = com.google.android.gms.ads.internal.zzp.zzbx().zze(uri);
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Received GMSG: " + path);
            for (String str : mapZze.keySet()) {
                com.google.android.gms.ads.internal.util.client.zzb.v("  " + str + ": " + mapZze.get(str));
            }
        }
        Iterator<zzdl> it = list.iterator();
        while (it.hasNext()) {
            it.next().zza(this.zzps, mapZze);
        }
    }

    public com.google.android.gms.ads.internal.zze zzhO() {
        return this.zzzb;
    }

    public boolean zzhP() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzMk;
        }
        return z;
    }

    public void zzhQ() {
        synchronized (this.zzpK) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Loading blank page in WebView, 2...");
            this.zzMo = true;
            this.zzps.zzaI("about:blank");
        }
    }

    public final void zzhU() {
        if (this.zzFl != null && ((this.zzMp && this.zzMr <= 0) || this.zzMq)) {
            this.zzFl.zza(this.zzps, !this.zzMq);
            this.zzFl = null;
        }
        this.zzps.zzhN();
    }
}
