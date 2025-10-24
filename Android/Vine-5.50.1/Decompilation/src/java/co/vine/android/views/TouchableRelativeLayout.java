package co.vine.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class TouchableRelativeLayout extends RelativeLayout {
    public TouchListener mTouchListener;

    public interface TouchListener {
        boolean onInterceptTouchEvent(MotionEvent motionEvent);

        boolean onTouchEvent(MotionEvent motionEvent);
    }

    public TouchableRelativeLayout(Context context) {
        super(context);
    }

    public TouchableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTouchListener(TouchListener listener) {
        this.mTouchListener = listener;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mTouchListener != null ? this.mTouchListener.onInterceptTouchEvent(ev) : super.onInterceptTouchEvent(ev);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        return this.mTouchListener != null ? this.mTouchListener.onTouchEvent(ev) : super.onTouchEvent(ev);
    }
}
