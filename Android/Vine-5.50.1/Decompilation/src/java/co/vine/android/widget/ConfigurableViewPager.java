package co.vine.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class ConfigurableViewPager extends ViewPager {
    private boolean mSwipingEnabled;

    public ConfigurableViewPager(Context context) {
        this(context, null);
    }

    public ConfigurableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSwipingEnabled = true;
    }

    public void setSwipingEnabled(boolean enabled) {
        this.mSwipingEnabled = enabled;
    }

    @Override // android.support.v4.view.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mSwipingEnabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mSwipingEnabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }
}
