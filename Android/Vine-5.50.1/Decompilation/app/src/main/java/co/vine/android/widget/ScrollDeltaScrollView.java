package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/* loaded from: classes.dex */
public class ScrollDeltaScrollView extends ScrollView {
    private int mLastT;
    private ScrollDeltaListener mScrollDeltaListener;

    public interface ScrollDeltaListener {
        void onScroll(int i);
    }

    public ScrollDeltaScrollView(Context context) {
        super(context);
        this.mScrollDeltaListener = null;
        this.mLastT = 0;
    }

    public ScrollDeltaScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScrollDeltaListener = null;
        this.mLastT = 0;
    }

    public ScrollDeltaScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mScrollDeltaListener = null;
        this.mLastT = 0;
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t < 0) {
            t = 0;
        }
        if (this.mScrollDeltaListener != null) {
            this.mScrollDeltaListener.onScroll(this.mLastT - t);
        }
        this.mLastT = t;
    }

    public void setScrollDeltaListener(ScrollDeltaListener listener) {
        this.mScrollDeltaListener = listener;
    }
}
