package com.tune.crosspromo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* loaded from: classes.dex */
public class TuneBanner extends FrameLayout implements TuneAd {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition;
    private TuneAdParams mAdParams;
    private TuneAdView mAdView;
    private Context mContext;
    private int mDuration;
    private Handler mHandler;
    private int mLastOrientation;
    private TuneAdListener mListener;
    private TuneAdOrientation mOrientation;
    private TuneBannerPosition mPosition;
    private ScheduledThreadPoolExecutor mScheduler;
    private ViewSwitcher mViewSwitcher;
    private WebView mWebView1;
    private WebView mWebView2;
    private TuneAdUtils utils;
    private WebViewClient webViewClient;

    static /* synthetic */ int[] $SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition() {
        int[] iArr = $SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition;
        if (iArr == null) {
            iArr = new int[TuneBannerPosition.valuesCustom().length];
            try {
                iArr[TuneBannerPosition.BOTTOM_CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[TuneBannerPosition.TOP_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition = iArr;
        }
        return iArr;
    }

    public TuneBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        String advertiserId = attrs.getAttributeValue(null, "advertiserId");
        String conversionKey = attrs.getAttributeValue(null, "conversionKey");
        if (advertiserId != null && conversionKey != null) {
            init(context, advertiserId, conversionKey);
        } else {
            Log.e("TUNE", "TuneBanner XML requires advertiserId and conversionKey");
        }
    }

    public TuneBanner(Context context) {
        super(context);
        init(context, null, null);
    }

    private void init(Context context, String advertiserId, String conversionKey) {
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        this.mLastOrientation = getResources().getConfiguration().orientation;
        this.mOrientation = TuneAdOrientation.ALL;
        int forcedOrientation = ((Activity) context).getRequestedOrientation();
        if (forcedOrientation == 1) {
            this.mOrientation = TuneAdOrientation.PORTRAIT_ONLY;
        } else if (forcedOrientation == 0) {
            this.mOrientation = TuneAdOrientation.LANDSCAPE_ONLY;
        }
        this.utils = TuneAdUtils.getInstance();
        this.utils.init(context, advertiserId, conversionKey);
        this.mDuration = 60;
        this.mPosition = TuneBannerPosition.BOTTOM_CENTER;
        this.mScheduler = new ScheduledThreadPoolExecutor(1);
        this.webViewClient = new WebViewClient() { // from class: com.tune.crosspromo.TuneBanner.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                TuneBanner.this.processClick(url);
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                TuneBanner.this.notifyOnLoad();
                if (TuneBanner.this.mViewSwitcher != null) {
                    TuneBanner.this.mViewSwitcher.setVisibility(0);
                    if (TuneBanner.this.mViewSwitcher.getCurrentView() == TuneBanner.this.mWebView1) {
                        TuneBanner.this.mHandler.postDelayed(new Runnable() { // from class: com.tune.crosspromo.TuneBanner.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (TuneBanner.this.mViewSwitcher != null) {
                                    TuneBanner.this.mViewSwitcher.showNext();
                                }
                            }
                        }, 50L);
                    } else {
                        TuneBanner.this.mHandler.postDelayed(new Runnable() { // from class: com.tune.crosspromo.TuneBanner.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (TuneBanner.this.mViewSwitcher != null) {
                                    TuneBanner.this.mViewSwitcher.showPrevious();
                                }
                            }
                        }, 50L);
                    }
                    TuneAdClient.logView(TuneBanner.this.mAdView, TuneBanner.this.mAdParams.toJSON());
                    TuneBanner.this.positionAd();
                    TuneBanner.this.notifyOnShow();
                }
            }
        };
        buildViewSwitcher();
        bringToFront();
    }

    private void buildViewSwitcher() {
        this.mWebView1 = buildWebView(this.mContext);
        this.mWebView2 = buildWebView(this.mContext);
        this.mViewSwitcher = new ViewSwitcher(this.mContext);
        this.mViewSwitcher.setVisibility(8);
        FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(-1, -1);
        this.mViewSwitcher.addView(this.mWebView1, webViewParams);
        this.mViewSwitcher.addView(this.mWebView2, webViewParams);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        addView(this.mViewSwitcher, params);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private WebView buildWebView(Context context) {
        WebView view = new WebView(context);
        view.setWebViewClient(this.webViewClient);
        view.setBackgroundColor(0);
        view.setScrollBarStyle(0);
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void positionAd() {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            params.width = TuneBannerSize.getScreenWidthPixels(this.mContext);
            params.height = TuneBannerSize.getBannerHeightPixels(this.mContext, getResources().getConfiguration().orientation);
        }
        if (params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams newFrameParams = new FrameLayout.LayoutParams(params.width, params.height);
            switch ($SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition()[this.mPosition.ordinal()]) {
                case 2:
                    newFrameParams.gravity = 49;
                    break;
                default:
                    newFrameParams.gravity = 81;
                    break;
            }
            params = newFrameParams;
        } else if (params instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams newRelativeParams = new RelativeLayout.LayoutParams(params.width, params.height);
            switch ($SWITCH_TABLE$com$tune$crosspromo$TuneBannerPosition()[this.mPosition.ordinal()]) {
                case 2:
                    newRelativeParams.addRule(10);
                    newRelativeParams.addRule(14);
                    break;
                default:
                    newRelativeParams.addRule(12);
                    newRelativeParams.addRule(14);
                    break;
            }
            params = newRelativeParams;
        }
        setLayoutParams(params);
    }

    public TuneBannerPosition getPosition() {
        return this.mPosition;
    }

    public void setPosition(TuneBannerPosition position) {
        this.mPosition = position;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnLoad() {
        this.mHandler.post(new Runnable() { // from class: com.tune.crosspromo.TuneBanner.2
            @Override // java.lang.Runnable
            public void run() {
                if (TuneBanner.this.mListener != null) {
                    TuneBanner.this.mListener.onAdLoad(TuneBanner.this);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnShow() {
        this.mHandler.post(new Runnable() { // from class: com.tune.crosspromo.TuneBanner.4
            @Override // java.lang.Runnable
            public void run() {
                if (TuneBanner.this.mListener != null) {
                    TuneBanner.this.mListener.onAdShown(TuneBanner.this);
                }
            }
        });
    }

    private void notifyOnClick() {
        this.mHandler.post(new Runnable() { // from class: com.tune.crosspromo.TuneBanner.5
            @Override // java.lang.Runnable
            public void run() {
                if (TuneBanner.this.mListener != null) {
                    TuneBanner.this.mListener.onAdClick(TuneBanner.this);
                }
            }
        });
    }

    public void setListener(TuneAdListener listener) {
        this.mListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processClick(String url) {
        Intent intent = new Intent(getContext(), (Class<?>) TuneAdActivity.class);
        intent.putExtra("INTERSTITIAL", false);
        intent.putExtra("REDIRECT_URI", url);
        Activity activity = (Activity) getContext();
        activity.startActivity(intent);
        notifyOnClick();
        TuneAdClient.logClick(this.mAdView, this.mAdParams.toJSON());
    }

    public TuneAdView getCurrentAd() {
        return this.mAdView;
    }

    public TuneAdParams getParams() {
        return this.mAdParams;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation != this.mLastOrientation) {
            this.mLastOrientation = orientation;
            int widthPx = TuneBannerSize.getScreenWidthPixels(this.mContext);
            int heightPx = TuneBannerSize.getBannerHeightPixels(this.mContext, orientation);
            int newWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthPx, 1073741824);
            int newHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightPx, 1073741824);
            super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
            measureChildren(newWidthMeasureSpec, newHeightMeasureSpec);
        }
    }
}
