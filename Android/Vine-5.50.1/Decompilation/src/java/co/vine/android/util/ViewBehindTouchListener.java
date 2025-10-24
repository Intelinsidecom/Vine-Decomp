package co.vine.android.util;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import co.vine.android.views.TouchableRelativeLayout;
import com.edisonwang.android.slog.SLog;
import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/* loaded from: classes.dex */
public class ViewBehindTouchListener implements TouchableRelativeLayout.TouchListener {
    private int mActivePointerId;
    private float mInitialX;
    private final View mInvalidateTarget;
    private boolean mIsScrolling;
    private boolean mSetInitials;
    private final CustomViewAbove mSlideController;
    private final int mTouchSlop;

    public ViewBehindTouchListener(Context context, SlidingMenu sm) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mSlideController = sm.getViewAbove();
        this.mInvalidateTarget = sm.getViewBehind();
    }

    @Override // co.vine.android.views.TouchableRelativeLayout.TouchListener
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        SLog.d("onInterceptTouchEvent!!!!");
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == 3 || action == 1) {
            this.mIsScrolling = false;
            return false;
        }
        switch (action) {
            case 0:
                this.mInitialX = ev.getX();
                int index = MotionEventCompat.getActionIndex(ev);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                this.mSetInitials = true;
                break;
            case 2:
                if (this.mIsScrolling) {
                    SLog.d("Scrolling 2");
                    return true;
                }
                int xDiff = Math.abs((int) (ev.getX() - this.mInitialX));
                if (xDiff > this.mTouchSlop) {
                    SLog.d("Scrolling");
                    this.mIsScrolling = true;
                    return true;
                }
                break;
        }
        this.mSlideController.addMovement(ev);
        this.mInvalidateTarget.postInvalidateDelayed(100L);
        return false;
    }

    @Override // co.vine.android.views.TouchableRelativeLayout.TouchListener
    public boolean onTouchEvent(MotionEvent ev) {
        SLog.d("onTouchEvent!!");
        if (this.mSetInitials) {
            this.mSlideController.setInitialTouchEvents(ev.getX(), this.mActivePointerId);
            this.mSlideController.determineDrag(ev);
            this.mSetInitials = false;
        }
        this.mSlideController.onTouchEvent(ev);
        switch (ev.getAction()) {
            case 1:
            case 3:
                this.mIsScrolling = false;
                break;
        }
        return true;
    }
}
