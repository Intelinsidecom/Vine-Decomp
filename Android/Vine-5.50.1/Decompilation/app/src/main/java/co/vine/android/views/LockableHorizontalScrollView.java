package co.vine.android.views;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/* loaded from: classes.dex */
public class LockableHorizontalScrollView extends HorizontalScrollView {
    private boolean mScrollable;

    public LockableHorizontalScrollView(Context context) {
        super(context);
        this.mScrollable = false;
    }

    public void toggleScroll() {
        this.mScrollable = !this.mScrollable;
    }

    public boolean getScrollable() {
        return this.mScrollable;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mScrollable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }
}
