package co.vine.android.views;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

/* loaded from: classes.dex */
public class LockableScrollView extends ScrollView {
    private boolean mScrollable;

    public LockableScrollView(Context context) {
        super(context);
        this.mScrollable = false;
    }

    public void toggleScroll() {
        this.mScrollable = !this.mScrollable;
    }

    public boolean getScrollable() {
        return this.mScrollable;
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mScrollable) {
            return super.onTouchEvent(ev);
        }
        return false;
    }
}
