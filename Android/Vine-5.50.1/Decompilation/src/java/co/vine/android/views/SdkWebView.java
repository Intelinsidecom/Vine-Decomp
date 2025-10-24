package co.vine.android.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/* loaded from: classes.dex */
public class SdkWebView extends WebView {
    public SdkWebView(Context context) {
        super(context);
        init();
    }

    public SdkWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SdkWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT <= 14) {
            setScrollBarStyle(0);
        }
    }
}
