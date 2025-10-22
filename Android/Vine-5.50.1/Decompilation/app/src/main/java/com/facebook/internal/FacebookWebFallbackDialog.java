package com.facebook.internal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FacebookWebFallbackDialog extends WebDialog {
    private static final String TAG = FacebookWebFallbackDialog.class.getName();
    private boolean waitingForDialogToClose;

    public FacebookWebFallbackDialog(Context context, String url, String expectedRedirectUrl) {
        super(context, url);
        setExpectedRedirectUrl(expectedRedirectUrl);
    }

    @Override // com.facebook.internal.WebDialog
    protected Bundle parseResponseUri(String url) {
        Uri responseUri = Uri.parse(url);
        Bundle queryParams = Utility.parseUrlQueryString(responseUri.getQuery());
        String bridgeArgsJSONString = queryParams.getString("bridge_args");
        queryParams.remove("bridge_args");
        if (!Utility.isNullOrEmpty(bridgeArgsJSONString)) {
            try {
                JSONObject bridgeArgsJSON = new JSONObject(bridgeArgsJSONString);
                Bundle bridgeArgs = BundleJSONConverter.convertToBundle(bridgeArgsJSON);
                queryParams.putBundle("com.facebook.platform.protocol.BRIDGE_ARGS", bridgeArgs);
            } catch (JSONException je) {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", je);
            }
        }
        String methodResultsJSONString = queryParams.getString("method_results");
        queryParams.remove("method_results");
        if (!Utility.isNullOrEmpty(methodResultsJSONString)) {
            if (Utility.isNullOrEmpty(methodResultsJSONString)) {
                methodResultsJSONString = "{}";
            }
            try {
                JSONObject methodArgsJSON = new JSONObject(methodResultsJSONString);
                Bundle methodResults = BundleJSONConverter.convertToBundle(methodArgsJSON);
                queryParams.putBundle("com.facebook.platform.protocol.RESULT_ARGS", methodResults);
            } catch (JSONException je2) {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", je2);
            }
        }
        queryParams.remove("version");
        queryParams.putInt("com.facebook.platform.protocol.PROTOCOL_VERSION", NativeProtocol.getLatestKnownVersion());
        return queryParams;
    }

    @Override // com.facebook.internal.WebDialog, android.app.Dialog, android.content.DialogInterface
    public void cancel() {
        WebView webView = getWebView();
        if (!isPageFinished() || isListenerCalled() || webView == null || !webView.isShown()) {
            super.cancel();
        } else if (!this.waitingForDialogToClose) {
            this.waitingForDialogToClose = true;
            webView.loadUrl("javascript:(function() {  var event = document.createEvent('Event');  event.initEvent('fbPlatformDialogMustClose',true,true);  document.dispatchEvent(event);})();");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() { // from class: com.facebook.internal.FacebookWebFallbackDialog.1
                @Override // java.lang.Runnable
                public void run() {
                    FacebookWebFallbackDialog.super.cancel();
                }
            }, 1500L);
        }
    }
}
