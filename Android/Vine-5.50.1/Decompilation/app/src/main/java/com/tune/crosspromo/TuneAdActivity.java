package com.tune.crosspromo;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class TuneAdActivity extends Activity {
    protected JSONObject adParams;
    public TuneAdView adView;
    protected TuneCloseButton closeButton;
    private boolean nativeCloseButton;
    protected TuneAdUtils utils;
    protected WebView webView;

    @Override // android.app.Activity
    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.utils = TuneAdUtils.getInstance();
        boolean showInterstitial = getIntent().getBooleanExtra("INTERSTITIAL", false);
        if (showInterstitial) {
            TuneAdOrientation orientation = TuneAdOrientation.forValue(getIntent().getStringExtra("ORIENTATION"));
            if (orientation == TuneAdOrientation.PORTRAIT_ONLY) {
                setRequestedOrientation(1);
            } else if (orientation == TuneAdOrientation.LANDSCAPE_ONLY) {
                setRequestedOrientation(0);
            }
            String placement = getIntent().getStringExtra("PLACEMENT");
            this.adView = this.utils.getPreviousView(placement);
            this.webView = this.adView.webView;
            this.utils.setAdContext(this);
            this.adView.requestId = getIntent().getStringExtra("REQUESTID");
            try {
                this.adParams = new JSONObject(getIntent().getStringExtra("ADPARAMS"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            view.setBackgroundColor(0);
            this.nativeCloseButton = getIntent().getBooleanExtra("NATIVECLOSEBUTTON", false);
            if (this.nativeCloseButton) {
                this.closeButton = new TuneCloseButton(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
                this.closeButton.setLayoutParams(params);
                view.addView(this.closeButton);
            }
            setContentView(this.webView);
            return;
        }
        String url = getIntent().getStringExtra("REDIRECT_URI");
        getWindow().requestFeature(2);
        getWindow().setFeatureInt(2, -1);
        if (url != null) {
            Uri uri = Uri.parse(url);
            if (isMarketUrl(uri)) {
                processMarketUri(uri);
            } else if (isAmazonUrl(uri)) {
                processAmazonUri(uri);
            } else {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", uri));
                } catch (ActivityNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
        finish();
    }

    @Override // android.app.Activity
    @SuppressLint({"NewApi"})
    public void onResume() {
        ActionBar actionBar;
        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(1024, 1024);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(1028);
        }
        if (Build.VERSION.SDK_INT >= 11 && (actionBar = getActionBar()) != null) {
            actionBar.hide();
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (this.adView != null) {
            TuneAdClient.logClose(this.adView, this.adParams);
        }
        super.onBackPressed();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        if (this.nativeCloseButton) {
            view.removeView(this.closeButton);
        }
        if (this.webView != null && this.webView.getParent() != null) {
            ((ViewGroup) this.webView.getParent()).removeView(this.webView);
            this.webView.loadUrl("about:blank");
        }
        this.utils.setAdContext(null);
        super.onDestroy();
    }

    protected void processMarketUri(Uri url) {
        String query = url.getQuery();
        try {
            Uri marketUri = Uri.parse(String.format("market://details?%s", query));
            startActivity(new Intent("android.intent.action.VIEW", marketUri));
        } catch (ActivityNotFoundException e) {
            Uri httpUri = Uri.parse(String.format("http://play.google.com/store/apps/details?%s", query));
            startActivity(new Intent("android.intent.action.VIEW", httpUri));
        }
    }

    protected void processAmazonUri(Uri url) {
        String query = url.getQuery();
        try {
            Uri amznUri = Uri.parse(String.format("amzn://apps/android?%s", query));
            startActivity(new Intent("android.intent.action.VIEW", amznUri));
        } catch (ActivityNotFoundException e) {
            Uri httpUri = Uri.parse(String.format("http://www.amazon.com/gp/mas/dl/android?%s", query));
            startActivity(new Intent("android.intent.action.VIEW", httpUri));
        }
    }

    protected boolean isMarketUrl(Uri url) {
        String scheme = url.getScheme();
        String host = url.getHost();
        if (scheme == null) {
            return false;
        }
        boolean isMarketScheme = scheme.equals("market");
        boolean isPlayUrl = (scheme.equals("http") || scheme.equals("https")) && (host.equals("play.google.com") || host.equals("market.android.com"));
        return isMarketScheme || isPlayUrl;
    }

    protected boolean isAmazonUrl(Uri url) {
        String scheme = url.getScheme();
        String host = url.getHost();
        if (scheme == null) {
            return false;
        }
        boolean isAmznScheme = scheme.equals("amzn");
        boolean isAmznWebUrl = (scheme.equals("http") || scheme.equals("https")) && host.equals("www.amazon.com");
        return isAmznScheme || isAmznWebUrl;
    }
}
