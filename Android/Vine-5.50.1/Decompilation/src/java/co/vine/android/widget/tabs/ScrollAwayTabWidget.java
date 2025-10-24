package co.vine.android.widget.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TabWidget;

/* loaded from: classes.dex */
public class ScrollAwayTabWidget extends TabWidget {
    private int mCurrentDelta;

    public ScrollAwayTabWidget(Context context) {
        super(context);
        this.mCurrentDelta = 0;
    }

    public ScrollAwayTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCurrentDelta = 0;
    }

    public ScrollAwayTabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentDelta = 0;
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        c.save();
        c.translate(0.0f, this.mCurrentDelta);
        super.draw(c);
        c.restore();
    }

    public void onScroll(int delta) {
        int oldDelta = this.mCurrentDelta;
        this.mCurrentDelta += delta;
        if (this.mCurrentDelta < (-getHeight())) {
            this.mCurrentDelta = -getHeight();
        } else if (this.mCurrentDelta > 0) {
            this.mCurrentDelta = 0;
        }
        if (this.mCurrentDelta != oldDelta) {
            invalidate();
        }
    }

    public void resetScroll() {
        this.mCurrentDelta = 0;
    }

    public int getNavBottom() {
        return this.mCurrentDelta + getHeight();
    }

    public int getYDrawOffset() {
        return this.mCurrentDelta;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return event.getY() > ((float) getNavBottom());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() > getNavBottom()) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
