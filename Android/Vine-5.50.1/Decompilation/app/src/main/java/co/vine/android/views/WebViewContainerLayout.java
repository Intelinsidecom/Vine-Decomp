package co.vine.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class WebViewContainerLayout extends FrameLayout {
    private OnWebViewInitializedListener mInitializeListener;
    private boolean mIsResumed;
    private final Runnable mTryAddRunnable;
    private WebView mWebView;
    private int noOpCount;

    public interface OnWebViewInitializedListener {
        void onInitialized(WebView webView);
    }

    public WebViewContainerLayout(Context context) {
        super(context);
        this.mTryAddRunnable = new Runnable() { // from class: co.vine.android.views.WebViewContainerLayout.1
            @Override // java.lang.Runnable
            public void run() {
                WebViewContainerLayout.this.tryAddWebView();
            }
        };
    }

    public WebViewContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTryAddRunnable = new Runnable() { // from class: co.vine.android.views.WebViewContainerLayout.1
            @Override // java.lang.Runnable
            public void run() {
                WebViewContainerLayout.this.tryAddWebView();
            }
        };
    }

    public WebViewContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTryAddRunnable = new Runnable() { // from class: co.vine.android.views.WebViewContainerLayout.1
            @Override // java.lang.Runnable
            public void run() {
                WebViewContainerLayout.this.tryAddWebView();
            }
        };
    }

    @TargetApi(21)
    public WebViewContainerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTryAddRunnable = new Runnable() { // from class: co.vine.android.views.WebViewContainerLayout.1
            @Override // java.lang.Runnable
            public void run() {
                WebViewContainerLayout.this.tryAddWebView();
            }
        };
    }

    public WebView getWebView() {
        return this.mWebView;
    }

    public void onResume() {
        checkLooper();
        this.mIsResumed = true;
        tryAddWebView();
    }

    public void onPause() {
        checkLooper();
        this.mIsResumed = false;
    }

    public void onDestroy() {
        checkLooper();
        if (this.mWebView != null) {
            removeView(this.mWebView);
            this.mWebView.destroy();
            this.mWebView = null;
        }
    }

    private void checkLooper() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("You cannot call this method on non-UI thread.");
        }
    }

    public void onViewCreated() {
        this.mIsResumed = true;
        tryAddWebView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void tryAddWebView() {
        /*
            r6 = this;
            r6.checkLooper()
            java.lang.Runnable r3 = r6.mTryAddRunnable
            r6.removeCallbacks(r3)
            boolean r3 = r6.mIsResumed
            if (r3 == 0) goto L2e
            android.webkit.WebView r3 = r6.mWebView
            if (r3 != 0) goto L2e
            r1 = 0
            co.vine.android.views.SdkWebView r2 = new co.vine.android.views.SdkWebView     // Catch: java.lang.Exception -> L2f
            android.content.Context r3 = r6.getContext()     // Catch: java.lang.Exception -> L2f
            r2.<init>(r3)     // Catch: java.lang.Exception -> L2f
            r3 = 0
            r6.noOpCount = r3     // Catch: java.lang.Exception -> L47
            r1 = r2
        L1e:
            if (r1 == 0) goto L2e
            r6.mWebView = r1
            r6.addView(r1)
            co.vine.android.views.WebViewContainerLayout$OnWebViewInitializedListener r3 = r6.mInitializeListener
            if (r3 == 0) goto L2e
            co.vine.android.views.WebViewContainerLayout$OnWebViewInitializedListener r3 = r6.mInitializeListener
            r3.onInitialized(r1)
        L2e:
            return
        L2f:
            r0 = move-exception
        L30:
            int r3 = r6.noOpCount
            int r3 = r3 + 1
            r6.noOpCount = r3
            co.vine.android.util.CrashUtil.logException(r0)
            int r3 = r6.noOpCount
            r4 = 20
            if (r3 >= r4) goto L1e
            java.lang.Runnable r3 = r6.mTryAddRunnable
            r4 = 1000(0x3e8, double:4.94E-321)
            r6.postDelayed(r3, r4)
            goto L1e
        L47:
            r0 = move-exception
            r1 = r2
            goto L30
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.views.WebViewContainerLayout.tryAddWebView():void");
    }

    public void setInitializationListener(OnWebViewInitializedListener listener) {
        this.mInitializeListener = listener;
    }
}
