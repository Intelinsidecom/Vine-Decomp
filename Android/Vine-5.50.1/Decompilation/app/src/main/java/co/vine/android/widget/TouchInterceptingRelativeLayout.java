package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class TouchInterceptingRelativeLayout extends RelativeLayout {
    private boolean mInterceptTouches;
    private View.OnTouchListener mOnInterceptTouchListener;

    public TouchInterceptingRelativeLayout(Context context) {
        super(context);
        this.mInterceptTouches = false;
    }

    public TouchInterceptingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInterceptTouches = false;
    }

    public TouchInterceptingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mInterceptTouches = false;
    }

    public void setInterceptTouches(boolean intercept) {
        this.mInterceptTouches = intercept;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mInterceptTouches) {
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    public void setOnInterceptTouchesListener(View.OnTouchListener listener) {
        this.mOnInterceptTouchListener = listener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        return (!this.mInterceptTouches || this.mOnInterceptTouchListener == null) ? super.onTouchEvent(e) : this.mOnInterceptTouchListener.onTouch(this, e);
    }
}
