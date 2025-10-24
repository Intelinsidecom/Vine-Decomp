package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import co.vine.android.widget.ScrollDeltaScrollView;

/* loaded from: classes.dex */
public class ScrollAwayFrameLayout extends FrameLayout implements ScrollDeltaScrollView.ScrollDeltaListener {
    private int mCurrentDelta;

    public ScrollAwayFrameLayout(Context context) {
        super(context);
        this.mCurrentDelta = 0;
    }

    public ScrollAwayFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCurrentDelta = 0;
    }

    public ScrollAwayFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentDelta = 0;
    }

    @Override // co.vine.android.widget.ScrollDeltaScrollView.ScrollDeltaListener
    public void onScroll(int delta) {
        int oldDelta = this.mCurrentDelta;
        this.mCurrentDelta += delta;
        if (this.mCurrentDelta < (-getMeasuredHeight())) {
            this.mCurrentDelta = -getMeasuredHeight();
        } else if (this.mCurrentDelta > 0) {
            this.mCurrentDelta = 0;
        }
        if (this.mCurrentDelta != oldDelta) {
            setY(getMeasuredHeight() + this.mCurrentDelta);
            invalidate();
        }
    }
}
