package com.google.android.gms.internal;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@zzha
/* loaded from: classes.dex */
public class zzjt extends WebChromeClient {
    private final zzjn zzps;

    /* renamed from: com.google.android.gms.internal.zzjt$7, reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] zzMQ = new int[ConsoleMessage.MessageLevel.values().length];

        static {
            try {
                zzMQ[ConsoleMessage.MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                zzMQ[ConsoleMessage.MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                zzMQ[ConsoleMessage.MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                zzMQ[ConsoleMessage.MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                zzMQ[ConsoleMessage.MessageLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public zzjt(zzjn zzjnVar) {
        this.zzps = zzjnVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Context zza(WebView webView) {
        if (!(webView instanceof zzjn)) {
            return webView.getContext();
        }
        zzjn zzjnVar = (zzjn) webView;
        Activity activityZzhx = zzjnVar.zzhx();
        return activityZzhx == null ? zzjnVar.getContext() : activityZzhx;
    }

    private static void zza(AlertDialog.Builder builder, String str, final JsResult jsResult) {
        builder.setMessage(str).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzjt.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                jsResult.confirm();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzjt.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                jsResult.cancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.android.gms.internal.zzjt.1
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                jsResult.cancel();
            }
        }).create().show();
    }

    private static void zza(Context context, AlertDialog.Builder builder, String str, String str2, final JsPromptResult jsPromptResult) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        textView.setText(str);
        final EditText editText = new EditText(context);
        editText.setText(str2);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        builder.setView(linearLayout).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzjt.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                jsPromptResult.confirm(editText.getText().toString());
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzjt.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                jsPromptResult.cancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.android.gms.internal.zzjt.4
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                jsPromptResult.cancel();
            }
        }).create().show();
    }

    private final boolean zzid() {
        return com.google.android.gms.ads.internal.zzp.zzbx().zza(this.zzps.getContext().getPackageManager(), this.zzps.getContext().getPackageName(), "android.permission.ACCESS_FINE_LOCATION") || com.google.android.gms.ads.internal.zzp.zzbx().zza(this.zzps.getContext().getPackageManager(), this.zzps.getContext().getPackageName(), "android.permission.ACCESS_COARSE_LOCATION");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.webkit.WebChromeClient
    public final void onCloseWindow(WebView webView) {
        if (!(webView instanceof zzjn)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Tried to close a WebView that wasn't an AdWebView.");
            return;
        }
        com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = ((zzjn) webView).zzhA();
        if (zzdVarZzhA == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Tried to close an AdWebView not associated with an overlay.");
        } else {
            zzdVarZzhA.close();
        }
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")";
        if (str.contains("Application Cache")) {
            return super.onConsoleMessage(consoleMessage);
        }
        switch (AnonymousClass7.zzMQ[consoleMessage.messageLevel().ordinal()]) {
            case 1:
                com.google.android.gms.ads.internal.util.client.zzb.e(str);
                break;
            case 2:
                com.google.android.gms.ads.internal.util.client.zzb.zzaH(str);
                break;
            case 3:
            case 4:
                com.google.android.gms.ads.internal.util.client.zzb.zzaG(str);
                break;
            case 5:
                com.google.android.gms.ads.internal.util.client.zzb.zzaF(str);
                break;
            default:
                com.google.android.gms.ads.internal.util.client.zzb.zzaG(str);
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport webViewTransport = (WebView.WebViewTransport) resultMsg.obj;
        WebView webView = new WebView(view.getContext());
        webView.setWebViewClient(this.zzps.zzhC());
        webViewTransport.setWebView(webView);
        resultMsg.sendToTarget();
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public final void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        long j = 5242880 - totalUsedQuota;
        if (j <= 0) {
            quotaUpdater.updateQuota(currentQuota);
            return;
        }
        if (currentQuota == 0) {
            if (estimatedSize > j || estimatedSize > 1048576) {
                estimatedSize = 0;
            }
        } else if (estimatedSize == 0) {
            estimatedSize = Math.min(Math.min(PlaybackStateCompat.ACTION_PREPARE_FROM_URI, j) + currentQuota, 1048576L);
        } else {
            if (estimatedSize <= Math.min(1048576 - currentQuota, j)) {
                currentQuota += estimatedSize;
            }
            estimatedSize = currentQuota;
        }
        quotaUpdater.updateQuota(estimatedSize);
    }

    @Override // android.webkit.WebChromeClient
    public final void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (callback != null) {
            callback.invoke(origin, zzid(), true);
        }
    }

    @Override // android.webkit.WebChromeClient
    public final void onHideCustomView() {
        com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = this.zzps.zzhA();
        if (zzdVarZzhA == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not get ad overlay when hiding custom view.");
        } else {
            zzdVarZzhA.zzfa();
        }
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    @Override // android.webkit.WebChromeClient
    public final boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
        return zza(zza(webView), url, message, defaultValue, null, result, true);
    }

    public final void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        long j = 5242880 - totalUsedQuota;
        long j2 = PlaybackStateCompat.ACTION_PREPARE_FROM_URI + spaceNeeded;
        if (j < j2) {
            quotaUpdater.updateQuota(0L);
        } else {
            quotaUpdater.updateQuota(j2);
        }
    }

    @Override // android.webkit.WebChromeClient
    public final void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        zza(view, -1, customViewCallback);
    }

    protected final void zza(View view, int i, WebChromeClient.CustomViewCallback customViewCallback) {
        com.google.android.gms.ads.internal.overlay.zzd zzdVarZzhA = this.zzps.zzhA();
        if (zzdVarZzhA == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not get ad overlay when showing custom view.");
            customViewCallback.onCustomViewHidden();
        } else {
            zzdVarZzhA.zza(view, customViewCallback);
            zzdVarZzhA.setRequestedOrientation(i);
        }
    }

    protected boolean zza(Context context, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(str);
            if (z) {
                zza(context, builder, str2, str3, jsPromptResult);
            } else {
                zza(builder, str2, jsResult);
            }
            return true;
        } catch (WindowManager.BadTokenException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to display Dialog.", e);
            return true;
        }
    }
}
