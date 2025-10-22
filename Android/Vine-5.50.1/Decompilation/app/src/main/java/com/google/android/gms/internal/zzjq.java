package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.Map;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
class zzjq extends FrameLayout implements zzjn {
    private final zzjn zzMu;
    private final zzjm zzMv;

    public zzjq(zzjn zzjnVar) {
        super(zzjnVar.getContext());
        this.zzMu = zzjnVar;
        this.zzMv = new zzjm(zzjnVar.zzhy(), this, this);
        zzjo zzjoVarZzhC = this.zzMu.zzhC();
        if (zzjoVarZzhC != null) {
            zzjoVarZzhC.zze(this);
        }
        addView(this.zzMu.getView());
    }

    @Override // com.google.android.gms.internal.zzjn
    public void clearCache(boolean includeDiskFiles) {
        this.zzMu.clearCache(includeDiskFiles);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void destroy() {
        this.zzMu.destroy();
    }

    @Override // com.google.android.gms.internal.zzjn
    public String getRequestId() {
        return this.zzMu.getRequestId();
    }

    @Override // com.google.android.gms.internal.zzjn
    public int getRequestedOrientation() {
        return this.zzMu.getRequestedOrientation();
    }

    @Override // com.google.android.gms.internal.zzjn
    public View getView() {
        return this;
    }

    @Override // com.google.android.gms.internal.zzjn
    public WebView getWebView() {
        return this.zzMu.getWebView();
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean isDestroyed() {
        return this.zzMu.isDestroyed();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void loadData(String data, String mimeType, String encoding) {
        this.zzMu.loadData(data, mimeType, encoding);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.zzMu.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void loadUrl(String url) {
        this.zzMu.loadUrl(url);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void onPause() {
        this.zzMv.onPause();
        this.zzMu.onPause();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void onResume() {
        this.zzMu.onResume();
    }

    @Override // android.view.View, com.google.android.gms.internal.zzjn
    public void setBackgroundColor(int color) {
        this.zzMu.setBackgroundColor(color);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setContext(Context context) {
        this.zzMu.setContext(context);
    }

    @Override // android.view.View, com.google.android.gms.internal.zzjn
    public void setOnClickListener(View.OnClickListener listener) {
        this.zzMu.setOnClickListener(listener);
    }

    @Override // android.view.View, com.google.android.gms.internal.zzjn
    public void setOnTouchListener(View.OnTouchListener listener) {
        this.zzMu.setOnTouchListener(listener);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setRequestedOrientation(int requestedOrientation) {
        this.zzMu.setRequestedOrientation(requestedOrientation);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setWebChromeClient(WebChromeClient client) {
        this.zzMu.setWebChromeClient(client);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setWebViewClient(WebViewClient client) {
        this.zzMu.setWebViewClient(client);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void stopLoading() {
        this.zzMu.stopLoading();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzD(boolean z) {
        this.zzMu.zzD(z);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzE(boolean z) {
        this.zzMu.zzE(z);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzF(boolean z) {
        this.zzMu.zzF(z);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(Context context, AdSizeParcel adSizeParcel, zzch zzchVar) {
        this.zzMu.zza(context, adSizeParcel, zzchVar);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(AdSizeParcel adSizeParcel) {
        this.zzMu.zza(adSizeParcel);
    }

    @Override // com.google.android.gms.internal.zzaw
    public void zza(zzaz zzazVar, boolean z) {
        this.zzMu.zza(zzazVar, z);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(String str, String str2) {
        this.zzMu.zza(str, str2);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(String str, JSONObject jSONObject) {
        this.zzMu.zza(str, jSONObject);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzaI(String str) {
        this.zzMu.zzaI(str);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzaJ(String str) {
        this.zzMu.zzaJ(str);
    }

    @Override // com.google.android.gms.internal.zzjn
    public AdSizeParcel zzaP() {
        return this.zzMu.zzaP();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(com.google.android.gms.ads.internal.overlay.zzd zzdVar) {
        this.zzMu.zzb(zzdVar);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(String str, Map<String, ?> map) {
        this.zzMu.zzb(str, map);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(String str, JSONObject jSONObject) {
        this.zzMu.zzb(str, jSONObject);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzc(com.google.android.gms.ads.internal.overlay.zzd zzdVar) {
        this.zzMu.zzc(zzdVar);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzfg() {
        this.zzMu.zzfg();
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.overlay.zzd zzhA() {
        return this.zzMu.zzhA();
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.overlay.zzd zzhB() {
        return this.zzMu.zzhB();
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzjo zzhC() {
        return this.zzMu.zzhC();
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhD() {
        return this.zzMu.zzhD();
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzan zzhE() {
        return this.zzMu.zzhE();
    }

    @Override // com.google.android.gms.internal.zzjn
    public VersionInfoParcel zzhF() {
        return this.zzMu.zzhF();
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhG() {
        return this.zzMu.zzhG();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhH() {
        this.zzMv.onDestroy();
        this.zzMu.zzhH();
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhI() {
        return this.zzMu.zzhI();
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzjm zzhJ() {
        return this.zzMv;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzcf zzhK() {
        return this.zzMu.zzhK();
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzcg zzhL() {
        return this.zzMu.zzhL();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhM() {
        this.zzMu.zzhM();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhN() {
        this.zzMu.zzhN();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhw() {
        this.zzMu.zzhw();
    }

    @Override // com.google.android.gms.internal.zzjn
    public Activity zzhx() {
        return this.zzMu.zzhx();
    }

    @Override // com.google.android.gms.internal.zzjn
    public Context zzhy() {
        return this.zzMu.zzhy();
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.zzd zzhz() {
        return this.zzMu.zzhz();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzy(int i) {
        this.zzMu.zzy(i);
    }
}
