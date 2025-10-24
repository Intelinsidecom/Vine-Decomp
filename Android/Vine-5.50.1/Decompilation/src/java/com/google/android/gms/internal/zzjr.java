package com.google.android.gms.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
class zzjr extends WebView implements ViewTreeObserver.OnGlobalLayoutListener, DownloadListener, zzjn {
    private AdSizeParcel zzBh;
    private int zzCC;
    private int zzCD;
    private int zzCF;
    private int zzCG;
    private String zzDX;
    private Boolean zzKh;
    private boolean zzMA;
    private boolean zzMB;
    private boolean zzMC;
    private int zzMD;
    private boolean zzME;
    private zzcf zzMF;
    private zzcf zzMG;
    private zzcf zzMH;
    private zzcg zzMI;
    private com.google.android.gms.ads.internal.overlay.zzd zzMJ;
    private Map<String, zzdw> zzMK;
    private final zza zzMw;
    private zzjo zzMx;
    private com.google.android.gms.ads.internal.overlay.zzd zzMy;
    private boolean zzMz;
    private final VersionInfoParcel zzpI;
    private final Object zzpK;
    private final com.google.android.gms.ads.internal.zzd zzpc;
    private final WindowManager zzrR;
    private zzja zzrz;
    private final zzan zzxV;

    @zzha
    public static class zza extends MutableContextWrapper {
        private Activity zzLy;
        private Context zzMM;
        private Context zzrI;

        public zza(Context context) {
            super(context);
            setBaseContext(context);
        }

        @Override // android.content.ContextWrapper, android.content.Context
        public Object getSystemService(String service) {
            return this.zzMM.getSystemService(service);
        }

        @Override // android.content.MutableContextWrapper
        public void setBaseContext(Context base) {
            this.zzrI = base.getApplicationContext();
            this.zzLy = base instanceof Activity ? (Activity) base : null;
            this.zzMM = base;
            super.setBaseContext(this.zzrI);
        }

        @Override // android.content.ContextWrapper, android.content.Context
        public void startActivity(Intent intent) {
            if (this.zzLy != null && !zznx.isAtLeastL()) {
                this.zzLy.startActivity(intent);
            } else {
                intent.setFlags(268435456);
                this.zzrI.startActivity(intent);
            }
        }

        public Activity zzhx() {
            return this.zzLy;
        }

        public Context zzhy() {
            return this.zzMM;
        }
    }

    protected zzjr(zza zzaVar, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzanVar, VersionInfoParcel versionInfoParcel, zzch zzchVar, com.google.android.gms.ads.internal.zzd zzdVar) {
        super(zzaVar);
        this.zzpK = new Object();
        this.zzME = true;
        this.zzDX = "";
        this.zzCD = -1;
        this.zzCC = -1;
        this.zzCF = -1;
        this.zzCG = -1;
        this.zzMw = zzaVar;
        this.zzBh = adSizeParcel;
        this.zzMB = z;
        this.zzMD = -1;
        this.zzxV = zzanVar;
        this.zzpI = versionInfoParcel;
        this.zzpc = zzdVar;
        this.zzrR = (WindowManager) getContext().getSystemService("window");
        setBackgroundColor(0);
        WebSettings settings = getSettings();
        settings.setAllowFileAccess(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        com.google.android.gms.ads.internal.zzp.zzbx().zza(zzaVar, versionInfoParcel.afmaVersion, settings);
        com.google.android.gms.ads.internal.zzp.zzbz().zza(getContext(), settings);
        setDownloadListener(this);
        zzhY();
        if (zznx.zzrS()) {
            addJavascriptInterface(new zzjs(this), "googleAdsJsInterface");
        }
        this.zzrz = new zzja(this.zzMw.zzhx(), this, null);
        zzd(zzchVar);
    }

    static zzjr zzb(Context context, AdSizeParcel adSizeParcel, boolean z, boolean z2, zzan zzanVar, VersionInfoParcel versionInfoParcel, zzch zzchVar, com.google.android.gms.ads.internal.zzd zzdVar) {
        return new zzjr(new zza(context), adSizeParcel, z, z2, zzanVar, versionInfoParcel, zzchVar, zzdVar);
    }

    private void zzd(zzch zzchVar) {
        zzic();
        this.zzMI = new zzcg(new zzch(true, "make_wv", this.zzBh.zztV));
        this.zzMI.zzdt().zzc(zzchVar);
        this.zzMG = zzcd.zzb(this.zzMI.zzdt());
        this.zzMI.zza("native:view_create", this.zzMG);
        this.zzMH = null;
        this.zzMF = null;
    }

    private void zzhW() {
        synchronized (this.zzpK) {
            this.zzKh = com.google.android.gms.ads.internal.zzp.zzbA().zzgQ();
            if (this.zzKh == null) {
                try {
                    evaluateJavascript("(function(){})()", null);
                    zzb((Boolean) true);
                } catch (IllegalStateException e) {
                    zzb((Boolean) false);
                }
            }
        }
    }

    private void zzhX() {
        zzcd.zza(this.zzMI.zzdt(), this.zzMF, "aeh");
    }

    private void zzhY() {
        synchronized (this.zzpK) {
            if (this.zzMB || this.zzBh.zztW) {
                if (Build.VERSION.SDK_INT < 14) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("Disabling hardware acceleration on an overlay.");
                    zzhZ();
                } else {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("Enabling hardware acceleration on an overlay.");
                    zzia();
                }
            } else if (Build.VERSION.SDK_INT < 18) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Disabling hardware acceleration on an AdView.");
                zzhZ();
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Enabling hardware acceleration on an AdView.");
                zzia();
            }
        }
    }

    private void zzhZ() {
        synchronized (this.zzpK) {
            if (!this.zzMC) {
                com.google.android.gms.ads.internal.zzp.zzbz().zzn(this);
            }
            this.zzMC = true;
        }
    }

    private void zzia() {
        synchronized (this.zzpK) {
            if (this.zzMC) {
                com.google.android.gms.ads.internal.zzp.zzbz().zzm(this);
            }
            this.zzMC = false;
        }
    }

    private void zzib() {
        synchronized (this.zzpK) {
            if (this.zzMK != null) {
                Iterator<zzdw> it = this.zzMK.values().iterator();
                while (it.hasNext()) {
                    it.next().release();
                }
            }
        }
    }

    private void zzic() {
        zzch zzchVarZzdt;
        if (this.zzMI == null || (zzchVarZzdt = this.zzMI.zzdt()) == null || com.google.android.gms.ads.internal.zzp.zzbA().zzgM() == null) {
            return;
        }
        com.google.android.gms.ads.internal.zzp.zzbA().zzgM().zza(zzchVarZzdt);
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void destroy() {
        synchronized (this.zzpK) {
            zzic();
            this.zzrz.zzhn();
            if (this.zzMy != null) {
                this.zzMy.close();
                this.zzMy.onDestroy();
                this.zzMy = null;
            }
            this.zzMx.reset();
            if (this.zzMA) {
                return;
            }
            com.google.android.gms.ads.internal.zzp.zzbL().zza(this);
            zzib();
            this.zzMA = true;
            com.google.android.gms.ads.internal.util.client.zzb.v("Initiating WebView self destruct sequence in 3...");
            this.zzMx.zzhQ();
        }
    }

    @Override // android.webkit.WebView
    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        synchronized (this.zzpK) {
            if (!isDestroyed()) {
                super.evaluateJavascript(script, resultCallback);
                return;
            }
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
            if (resultCallback != null) {
                resultCallback.onReceiveValue(null);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public String getRequestId() {
        String str;
        synchronized (this.zzpK) {
            str = this.zzDX;
        }
        return str;
    }

    @Override // com.google.android.gms.internal.zzjn
    public int getRequestedOrientation() {
        int i;
        synchronized (this.zzpK) {
            i = this.zzMD;
        }
        return i;
    }

    @Override // com.google.android.gms.internal.zzjn
    public View getView() {
        return this;
    }

    @Override // com.google.android.gms.internal.zzjn
    public WebView getWebView() {
        return this;
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean isDestroyed() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzMA;
        }
        return z;
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void loadData(String data, String mimeType, String encoding) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
            } else {
                super.loadData(data, mimeType, encoding);
            }
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
            } else {
                super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
            }
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void loadUrl(String uri) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
            } else {
                try {
                    super.loadUrl(uri);
                } catch (Throwable th) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not call loadUrl. " + th);
                }
            }
        }
    }

    @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        synchronized (this.zzpK) {
            super.onAttachedToWindow();
            if (!isDestroyed()) {
                this.zzrz.onAttachedToWindow();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        synchronized (this.zzpK) {
            if (!isDestroyed()) {
                this.zzrz.onDetachedFromWindow();
            }
            super.onDetachedFromWindow();
        }
    }

    @Override // android.webkit.DownloadListener
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), mimeType);
            com.google.android.gms.ads.internal.zzp.zzbx().zzb(getContext(), intent);
        } catch (ActivityNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (isDestroyed()) {
            return;
        }
        if (Build.VERSION.SDK_INT == 21 && canvas.isHardwareAccelerated() && !isAttachedToWindow()) {
            return;
        }
        super.onDraw(canvas);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() throws JSONException {
        boolean zZzhV = zzhV();
        com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = zzhA();
        if (zzdVarZzhA == null || !zZzhV) {
            return;
        }
        zzdVarZzhA.zzff();
    }

    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                setMeasuredDimension(0, 0);
                return;
            }
            if (isInEditMode() || this.zzMB || this.zzBh.zztY || this.zzBh.zztZ) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            if (this.zzBh.zztW) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                this.zzrR.getDefaultDisplay().getMetrics(displayMetrics);
                setMeasuredDimension(displayMetrics.widthPixels, displayMetrics.heightPixels);
                return;
            }
            int mode = View.MeasureSpec.getMode(widthMeasureSpec);
            int size = View.MeasureSpec.getSize(widthMeasureSpec);
            int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
            int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
            int i = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
            int i2 = (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) ? size2 : Integer.MAX_VALUE;
            if (this.zzBh.widthPixels > i || this.zzBh.heightPixels > i2) {
                float f = this.zzMw.getResources().getDisplayMetrics().density;
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Not enough space to show ad. Needs " + ((int) (this.zzBh.widthPixels / f)) + "x" + ((int) (this.zzBh.heightPixels / f)) + " dp, but only has " + ((int) (size / f)) + "x" + ((int) (size2 / f)) + " dp.");
                if (getVisibility() != 8) {
                    setVisibility(4);
                }
                setMeasuredDimension(0, 0);
            } else {
                if (getVisibility() != 8) {
                    setVisibility(0);
                }
                setMeasuredDimension(this.zzBh.widthPixels, this.zzBh.heightPixels);
            }
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void onPause() {
        if (isDestroyed()) {
            return;
        }
        try {
            if (zznx.zzrN()) {
                super.onPause();
            }
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Could not pause webview.", e);
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void onResume() {
        if (isDestroyed()) {
            return;
        }
        try {
            if (zznx.zzrN()) {
                super.onResume();
            }
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Could not resume webview.", e);
        }
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.zzxV != null) {
            this.zzxV.zza(event);
        }
        if (isDestroyed()) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setContext(Context context) {
        this.zzMw.setBaseContext(context);
        this.zzrz.zzk(this.zzMw.zzhx());
    }

    @Override // com.google.android.gms.internal.zzjn
    public void setRequestedOrientation(int requestedOrientation) {
        synchronized (this.zzpK) {
            this.zzMD = requestedOrientation;
            if (this.zzMy != null) {
                this.zzMy.setRequestedOrientation(this.zzMD);
            }
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(webViewClient);
        if (webViewClient instanceof zzjo) {
            this.zzMx = (zzjo) webViewClient;
        }
    }

    @Override // android.webkit.WebView, com.google.android.gms.internal.zzjn
    public void stopLoading() {
        if (isDestroyed()) {
            return;
        }
        try {
            super.stopLoading();
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Could not stop loading webview.", e);
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzD(boolean z) {
        synchronized (this.zzpK) {
            this.zzMB = z;
            zzhY();
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzE(boolean z) {
        synchronized (this.zzpK) {
            if (this.zzMy != null) {
                this.zzMy.zza(this.zzMx.zzcb(), z);
            } else {
                this.zzMz = z;
            }
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzF(boolean z) {
        synchronized (this.zzpK) {
            this.zzME = z;
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(Context context, AdSizeParcel adSizeParcel, zzch zzchVar) {
        synchronized (this.zzpK) {
            this.zzrz.zzhn();
            setContext(context);
            this.zzMy = null;
            this.zzBh = adSizeParcel;
            this.zzMB = false;
            this.zzMz = false;
            this.zzDX = "";
            this.zzMD = -1;
            com.google.android.gms.ads.internal.zzp.zzbz().zzg(this);
            loadUrl("about:blank");
            this.zzMx.reset();
            setOnTouchListener(null);
            setOnClickListener(null);
            this.zzME = true;
            zzd(zzchVar);
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(AdSizeParcel adSizeParcel) {
        synchronized (this.zzpK) {
            this.zzBh = adSizeParcel;
            requestLayout();
        }
    }

    @Override // com.google.android.gms.internal.zzaw
    public void zza(zzaz zzazVar, boolean z) {
        HashMap map = new HashMap();
        map.put("isVisible", z ? "1" : "0");
        zzb("onAdVisibilityChanged", map);
    }

    protected void zza(String str, ValueCallback<String> valueCallback) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
                if (valueCallback != null) {
                    valueCallback.onReceiveValue(null);
                }
            } else {
                evaluateJavascript(str, valueCallback);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(String str, String str2) {
        zzaM(str + "(" + str2 + ");");
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zza(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        zza(str, jSONObject.toString());
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzaI(String str) {
        synchronized (this.zzpK) {
            try {
                super.loadUrl(str);
            } catch (Throwable th) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not call loadUrl. " + th);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzaJ(String str) {
        synchronized (this.zzpK) {
            if (str == null) {
                str = "";
            }
            this.zzDX = str;
        }
    }

    protected void zzaL(String str) {
        synchronized (this.zzpK) {
            if (isDestroyed()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview is destroyed. Ignoring action.");
            } else {
                loadUrl(str);
            }
        }
    }

    protected void zzaM(String str) {
        if (!zznx.zzrU()) {
            zzaL("javascript:" + str);
            return;
        }
        if (zzgQ() == null) {
            zzhW();
        }
        if (zzgQ().booleanValue()) {
            zza(str, (ValueCallback<String>) null);
        } else {
            zzaL("javascript:" + str);
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public AdSizeParcel zzaP() {
        AdSizeParcel adSizeParcel;
        synchronized (this.zzpK) {
            adSizeParcel = this.zzBh;
        }
        return adSizeParcel;
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(com.google.android.gms.ads.internal.overlay.zzd zzdVar) {
        synchronized (this.zzpK) {
            this.zzMy = zzdVar;
        }
    }

    void zzb(Boolean bool) {
        this.zzKh = bool;
        com.google.android.gms.ads.internal.zzp.zzbA().zzb(bool);
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(String str, Map<String, ?> map) {
        try {
            zzb(str, com.google.android.gms.ads.internal.zzp.zzbx().zzz(map));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not convert parameters to JSON.");
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzb(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        String string = jSONObject.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("AFMA_ReceiveMessage('");
        sb.append(str);
        sb.append("'");
        sb.append(",");
        sb.append(string);
        sb.append(");");
        com.google.android.gms.ads.internal.util.client.zzb.v("Dispatching AFMA event: " + sb.toString());
        zzaM(sb.toString());
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzc(com.google.android.gms.ads.internal.overlay.zzd zzdVar) {
        synchronized (this.zzpK) {
            this.zzMJ = zzdVar;
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzfg() {
        if (this.zzMF == null) {
            zzcd.zza(this.zzMI.zzdt(), this.zzMH, "aes");
            this.zzMF = zzcd.zzb(this.zzMI.zzdt());
            this.zzMI.zza("native:view_show", this.zzMF);
        }
        HashMap map = new HashMap(1);
        map.put("version", this.zzpI.afmaVersion);
        zzb("onshow", map);
    }

    Boolean zzgQ() {
        Boolean bool;
        synchronized (this.zzpK) {
            bool = this.zzKh;
        }
        return bool;
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.overlay.zzd zzhA() {
        com.google.android.gms.ads.internal.overlay.zzd zzdVar;
        synchronized (this.zzpK) {
            zzdVar = this.zzMy;
        }
        return zzdVar;
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.overlay.zzd zzhB() {
        com.google.android.gms.ads.internal.overlay.zzd zzdVar;
        synchronized (this.zzpK) {
            zzdVar = this.zzMJ;
        }
        return zzdVar;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzjo zzhC() {
        return this.zzMx;
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhD() {
        return this.zzMz;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzan zzhE() {
        return this.zzxV;
    }

    @Override // com.google.android.gms.internal.zzjn
    public VersionInfoParcel zzhF() {
        return this.zzpI;
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhG() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzMB;
        }
        return z;
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhH() {
        synchronized (this.zzpK) {
            com.google.android.gms.ads.internal.util.client.zzb.v("Destroying WebView!");
            zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.internal.zzjr.1
                @Override // java.lang.Runnable
                public void run() {
                    zzjr.super.destroy();
                }
            });
        }
    }

    @Override // com.google.android.gms.internal.zzjn
    public boolean zzhI() {
        boolean z;
        synchronized (this.zzpK) {
            zzcd.zza(this.zzMI.zzdt(), this.zzMF, "aebb");
            z = this.zzME;
        }
        return z;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzjm zzhJ() {
        return null;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzcf zzhK() {
        return this.zzMH;
    }

    @Override // com.google.android.gms.internal.zzjn
    public zzcg zzhL() {
        return this.zzMI;
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhM() {
        this.zzrz.zzhm();
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhN() {
        if (this.zzMH == null) {
            this.zzMH = zzcd.zzb(this.zzMI.zzdt());
            this.zzMI.zza("native:view_load", this.zzMH);
        }
    }

    public boolean zzhV() throws JSONException {
        int iZzb;
        int iZzb2;
        if (!zzhC().zzcb()) {
            return false;
        }
        DisplayMetrics displayMetricsZza = com.google.android.gms.ads.internal.zzp.zzbx().zza(this.zzrR);
        int iZzb3 = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(displayMetricsZza, displayMetricsZza.widthPixels);
        int iZzb4 = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(displayMetricsZza, displayMetricsZza.heightPixels);
        Activity activityZzhx = zzhx();
        if (activityZzhx == null || activityZzhx.getWindow() == null) {
            iZzb = iZzb4;
            iZzb2 = iZzb3;
        } else {
            int[] iArrZzg = com.google.android.gms.ads.internal.zzp.zzbx().zzg(activityZzhx);
            iZzb2 = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(displayMetricsZza, iArrZzg[0]);
            iZzb = com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(displayMetricsZza, iArrZzg[1]);
        }
        if (this.zzCC == iZzb3 && this.zzCD == iZzb4 && this.zzCF == iZzb2 && this.zzCG == iZzb) {
            return false;
        }
        boolean z = (this.zzCC == iZzb3 && this.zzCD == iZzb4) ? false : true;
        this.zzCC = iZzb3;
        this.zzCD = iZzb4;
        this.zzCF = iZzb2;
        this.zzCG = iZzb;
        new zzfr(this).zza(iZzb3, iZzb4, iZzb2, iZzb, displayMetricsZza.density, this.zzrR.getDefaultDisplay().getRotation());
        return z;
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzhw() {
        zzhX();
        HashMap map = new HashMap(1);
        map.put("version", this.zzpI.afmaVersion);
        zzb("onhide", map);
    }

    @Override // com.google.android.gms.internal.zzjn
    public Activity zzhx() {
        return this.zzMw.zzhx();
    }

    @Override // com.google.android.gms.internal.zzjn
    public Context zzhy() {
        return this.zzMw.zzhy();
    }

    @Override // com.google.android.gms.internal.zzjn
    public com.google.android.gms.ads.internal.zzd zzhz() {
        return this.zzpc;
    }

    @Override // com.google.android.gms.internal.zzjn
    public void zzy(int i) {
        zzhX();
        HashMap map = new HashMap(2);
        map.put("closetype", String.valueOf(i));
        map.put("version", this.zzpI.afmaVersion);
        zzb("onhide", map);
    }
}
