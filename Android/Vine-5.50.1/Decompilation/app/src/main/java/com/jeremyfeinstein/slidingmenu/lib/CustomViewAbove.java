package com.jeremyfeinstein.slidingmenu.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CustomViewAbove extends ViewGroup {
    private static final Interpolator sInterpolator = new Interpolator() { // from class: com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.1
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2 * t2 * t2) + 1.0f;
        }
    };
    protected int mActivePointerId;
    private SlidingMenu.OnClosedListener mClosedListener;
    private View mContent;
    private int mCurItem;
    private boolean mEnabled;
    private int mFlingDistance;
    private List<View> mIgnoredViews;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private boolean mLastIntersecption;
    private float mLastMotionX;
    private float mLastMotionY;
    protected int mMaximumVelocity;
    private int mMinimumVelocity;
    private OnPageChangeListener mOnPageChangeListener;
    private SlidingMenu.OnOpenedListener mOpenedListener;
    private boolean mQuickReturn;
    private float mScrollX;
    private Scroller mScroller;
    private boolean mScrolling;
    private boolean mScrollingCacheEnabled;
    protected int mTouchMode;
    private int mTouchSlop;
    protected VelocityTracker mVelocityTracker;
    private CustomViewBehind mViewBehind;

    public interface OnPageChangeListener {
        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        @Override // com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.OnPageChangeListener
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override // com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.OnPageChangeListener
        public void onPageSelected(int position) {
        }
    }

    public CustomViewAbove(Context context) {
        this(context, null);
    }

    public CustomViewAbove(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivePointerId = -1;
        this.mEnabled = true;
        this.mIgnoredViews = new ArrayList();
        this.mTouchMode = 0;
        this.mQuickReturn = false;
        this.mScrollX = 0.0f;
        initCustomViewAbove();
    }

    void initCustomViewAbove() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        setInternalPageChangeListener(new SimpleOnPageChangeListener() { // from class: com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.2
            @Override // com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.SimpleOnPageChangeListener, com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove.OnPageChangeListener
            public void onPageSelected(int position) {
                if (CustomViewAbove.this.mViewBehind != null) {
                    switch (position) {
                        case 0:
                        case 2:
                            CustomViewAbove.this.mViewBehind.setChildrenEnabled(true);
                            break;
                        case 1:
                            CustomViewAbove.this.mViewBehind.setChildrenEnabled(false);
                            break;
                    }
                }
            }
        });
        float density = context.getResources().getDisplayMetrics().density;
        this.mFlingDistance = (int) (25.0f * density);
    }

    public void setCurrentItem(int item) {
        setCurrentItemInternal(item, true, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (!always && this.mCurItem == item) {
            setScrollingCacheEnabled(false);
            return;
        }
        int item2 = this.mViewBehind.getMenuPage(item);
        boolean dispatchSelected = this.mCurItem != item2;
        this.mCurItem = item2;
        int destX = getDestScrollX(this.mCurItem);
        if (dispatchSelected && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(item2);
        }
        if (dispatchSelected && this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(item2);
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
        } else {
            completeScroll();
            scrollTo(destX, 0);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setOnOpenedListener(SlidingMenu.OnOpenedListener l) {
        this.mOpenedListener = l;
    }

    public void setOnClosedListener(SlidingMenu.OnClosedListener l) {
        this.mClosedListener = l;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return FloatMath.sin((float) ((f - 0.5f) * 0.4712389167638204d));
    }

    public int getDestScrollX(int page) {
        switch (page) {
            case 0:
            case 2:
                return this.mViewBehind.getMenuLeft(this.mContent, page);
            case 1:
                return this.mContent.getLeft();
            default:
                return 0;
        }
    }

    private int getLeftBound() {
        return this.mViewBehind.getAbsLeftBound(this.mContent);
    }

    private int getRightBound() {
        return this.mViewBehind.getAbsRightBound(this.mContent);
    }

    public int getContentLeft() {
        return this.mContent.getLeft() + this.mContent.getPaddingLeft();
    }

    public boolean isMenuOpen() {
        return this.mCurItem == 0 || this.mCurItem == 2;
    }

    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : this.mIgnoredViews) {
            v.getHitRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    public int getBehindWidth() {
        if (this.mViewBehind == null) {
            return 0;
        }
        return this.mViewBehind.getBehindWidth();
    }

    public void setSlidingEnabled(boolean b) {
        this.mEnabled = b;
    }

    void smoothScrollTo(int x, int y, int velocity) {
        int duration;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx = getScrollX();
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll();
            if (isMenuOpen()) {
                if (this.mOpenedListener != null) {
                    this.mOpenedListener.onOpened();
                    return;
                }
                return;
            } else {
                if (this.mClosedListener != null) {
                    this.mClosedListener.onClosed();
                    return;
                }
                return;
            }
        }
        setScrollingCacheEnabled(true);
        this.mScrolling = true;
        int width = getBehindWidth();
        int halfWidth = width / 2;
        float distanceRatio = Math.min(1.0f, (1.0f * Math.abs(dx)) / width);
        float distance = halfWidth + (halfWidth * distanceInfluenceForSnapDuration(distanceRatio));
        int velocity2 = Math.abs(velocity);
        if (velocity2 > 0) {
            duration = Math.round(1000.0f * Math.abs(distance / velocity2)) * 4;
        } else {
            float pageDelta = Math.abs(dx) / width;
            duration = 600;
        }
        this.mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, 600));
        invalidate();
    }

    public void setContent(View v) {
        if (this.mContent != null) {
            removeView(this.mContent);
        }
        this.mContent = v;
        addView(this.mContent);
    }

    public View getContent() {
        return this.mContent;
    }

    public void setCustomViewBehind(CustomViewBehind cvb) {
        this.mViewBehind = cvb;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width);
        int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        this.mContent.measure(contentWidth, contentHeight);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            completeScroll();
            scrollTo(getDestScrollX(this.mCurItem), getScrollY());
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        this.mContent.layout(0, 0, width, height);
    }

    public void setAboveOffset(int i) {
        this.mContent.setPadding(i, this.mContent.getPaddingTop(), this.mContent.getPaddingRight(), this.mContent.getPaddingBottom());
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
                pageScrolled(x);
            }
            invalidate();
            return;
        }
        completeScroll();
    }

    private void pageScrolled(int xpos) {
        int widthWithMargin = getWidth();
        int position = xpos / widthWithMargin;
        int offsetPixels = xpos % widthWithMargin;
        float offset = offsetPixels / widthWithMargin;
        onPageScrolled(position, offset, offsetPixels);
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void completeScroll() {
        boolean needPopulate = this.mScrolling;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
            if (isMenuOpen()) {
                if (this.mOpenedListener != null) {
                    this.mOpenedListener.onOpened();
                }
            } else if (this.mClosedListener != null) {
                this.mClosedListener.onClosed();
            }
        }
        this.mScrolling = false;
    }

    public void setTouchMode(int i) {
        this.mTouchMode = i;
    }

    public int getTouchMode() {
        return this.mTouchMode;
    }

    private boolean thisTouchAllowed(MotionEvent ev) {
        int x = (int) (ev.getX() + this.mScrollX);
        if (isMenuOpen()) {
            return this.mViewBehind.menuOpenTouchAllowed(this.mContent, this.mCurItem, x);
        }
        switch (this.mTouchMode) {
            case 0:
                return this.mViewBehind.marginTouchAllowed(this.mContent, x);
            case 1:
                return !isInIgnoredView(ev);
            case 2:
            default:
                return false;
        }
    }

    private boolean thisSlideAllowed(float dx) {
        if (isMenuOpen()) {
            boolean allowed = this.mViewBehind.menuOpenSlideAllowed(dx);
            return allowed;
        }
        boolean allowed2 = this.mViewBehind.menuClosedSlideAllowed(dx);
        return allowed2;
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1) {
            this.mActivePointerId = -1;
        }
        return activePointerIndex;
    }

    public void addMovement(MotionEvent ev) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(ev);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mEnabled) {
            return false;
        }
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1 || (action != 0 && this.mIsUnableToDrag)) {
            endDrag();
            this.mLastIntersecption = false;
            return false;
        }
        switch (action) {
            case 0:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                if (this.mActivePointerId != -1) {
                    float x = MotionEventCompat.getX(ev, index);
                    this.mInitialMotionX = x;
                    this.mLastMotionX = x;
                    this.mLastMotionY = MotionEventCompat.getY(ev, index);
                    if (thisTouchAllowed(ev)) {
                        this.mIsBeingDragged = false;
                        this.mIsUnableToDrag = false;
                        if (isMenuOpen() && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, ev.getX() + this.mScrollX)) {
                            this.mQuickReturn = true;
                            break;
                        }
                    } else {
                        this.mIsUnableToDrag = true;
                        break;
                    }
                }
                break;
            case 2:
                determineDrag(ev);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        if (!this.mIsBeingDragged) {
            addMovement(ev);
        }
        this.mLastIntersecption = this.mIsBeingDragged || this.mQuickReturn;
        return this.mIsBeingDragged || this.mQuickReturn;
    }

    public void setInitialTouchEvents(float initialX, int activePointerId) {
        this.mInitialMotionX = initialX;
        this.mLastMotionX = initialX;
        this.mActivePointerId = activePointerId;
        this.mIsBeingDragged = true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mEnabled) {
            return false;
        }
        if (!this.mIsBeingDragged && !thisTouchAllowed(ev)) {
            return false;
        }
        int action = ev.getAction();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        switch (action & 255) {
            case 0:
                completeScroll();
                int index = MotionEventCompat.getActionIndex(ev);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                return true;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    float pageOffset = (getScrollX() - getDestScrollX(this.mCurItem)) / getBehindWidth();
                    int activePointerIndex = getPointerIndex(ev, this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        int totalDelta = (int) (MotionEventCompat.getX(ev, activePointerIndex) - this.mInitialMotionX);
                        int nextPage = determineTargetPage(pageOffset, initialVelocity, totalDelta);
                        setCurrentItemInternal(nextPage, true, true, initialVelocity);
                    } else {
                        setCurrentItemInternal(this.mCurItem, true, true, initialVelocity);
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                } else if (this.mQuickReturn && this.mViewBehind.menuTouchInQuickReturn(this.mContent, this.mCurItem, ev.getX() + this.mScrollX)) {
                    setCurrentItem(1);
                    endDrag();
                }
                return true;
            case 2:
                if (!this.mIsBeingDragged) {
                    determineDrag(ev);
                    if (this.mIsUnableToDrag) {
                        return false;
                    }
                }
                if (this.mIsBeingDragged) {
                    int activePointerIndex2 = getPointerIndex(ev, this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        float x2 = MotionEventCompat.getX(ev, activePointerIndex2);
                        float deltaX = this.mLastMotionX - x2;
                        this.mLastMotionX = x2;
                        float oldScrollX = getScrollX();
                        float scrollX = oldScrollX + deltaX;
                        float leftBound = getLeftBound();
                        float rightBound = getRightBound();
                        if (scrollX < leftBound) {
                            scrollX = leftBound;
                        } else if (scrollX > rightBound) {
                            scrollX = rightBound;
                        }
                        this.mLastMotionX += scrollX - ((int) scrollX);
                        scrollTo((int) scrollX, getScrollY());
                        pageScrolled((int) scrollX);
                    }
                }
                return true;
            case 3:
                if (this.mIsBeingDragged) {
                    setCurrentItemInternal(this.mCurItem, true, true);
                    this.mActivePointerId = -1;
                    endDrag();
                }
                return true;
            case 4:
            default:
                return true;
            case 5:
                int indexx = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = MotionEventCompat.getX(ev, indexx);
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, indexx);
                return true;
            case 6:
                onSecondaryPointerUp(ev);
                int pointerIndex = getPointerIndex(ev, this.mActivePointerId);
                if (this.mActivePointerId != -1) {
                    this.mLastMotionX = MotionEventCompat.getX(ev, pointerIndex);
                }
                return true;
        }
    }

    public void determineDrag(MotionEvent ev) {
        int activePointerId = this.mActivePointerId;
        int pointerIndex = getPointerIndex(ev, activePointerId);
        if (activePointerId != -1 && pointerIndex != -1) {
            float x = MotionEventCompat.getX(ev, pointerIndex);
            float dx = x - this.mLastMotionX;
            float xDiff = Math.abs(dx);
            float y = MotionEventCompat.getY(ev, pointerIndex);
            float dy = y - this.mLastMotionY;
            float yDiff = Math.abs(dy);
            if (xDiff <= (isMenuOpen() ? this.mTouchSlop / 2 : this.mTouchSlop) || xDiff <= yDiff || !thisSlideAllowed(dx)) {
                if (xDiff > this.mTouchSlop) {
                    this.mIsUnableToDrag = true;
                }
            } else {
                startDrag();
                this.mLastMotionX = x;
                this.mLastMotionY = y;
                setScrollingCacheEnabled(true);
            }
        }
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        this.mScrollX = x;
        this.mViewBehind.scrollBehindTo(this.mContent, x, y);
        ((SlidingMenu) getParent()).manageLayers(getPercentOpen());
    }

    private int determineTargetPage(float pageOffset, int velocity, int deltaX) {
        int targetPage = this.mCurItem;
        if (Math.abs(deltaX) > this.mFlingDistance && Math.abs(velocity) > this.mMinimumVelocity) {
            if (velocity > 0 && deltaX > 0) {
                return targetPage - 1;
            }
            if (velocity < 0 && deltaX < 0) {
                return targetPage + 1;
            }
            return targetPage;
        }
        return Math.round(this.mCurItem + pageOffset);
    }

    protected float getPercentOpen() {
        return Math.abs(this.mScrollX - this.mContent.getLeft()) / getBehindWidth();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mViewBehind.drawShadow(this.mContent, canvas);
        this.mViewBehind.drawFade(this.mContent, canvas, getPercentOpen());
        this.mViewBehind.drawSelector(this.mContent, canvas, getPercentOpen());
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void startDrag() {
        this.mIsBeingDragged = true;
        this.mQuickReturn = false;
    }

    private void endDrag() {
        this.mQuickReturn = false;
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                boolean handled = arrowScroll(17);
                break;
            case 22:
                boolean handled2 = arrowScroll(66);
                break;
            case 61:
                if (Build.VERSION.SDK_INT >= 11) {
                    if (KeyEventCompat.hasNoModifiers(event)) {
                        boolean handled3 = arrowScroll(2);
                        break;
                    } else if (KeyEventCompat.hasModifiers(event, 1)) {
                        boolean handled4 = arrowScroll(1);
                        break;
                    }
                }
                break;
        }
        return false;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        boolean handled = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused != null && nextFocused != currentFocused) {
            if (direction == 17) {
                handled = nextFocused.requestFocus();
            } else if (direction == 66) {
                handled = (currentFocused == null || nextFocused.getLeft() > currentFocused.getLeft()) ? nextFocused.requestFocus() : pageRight();
            }
        } else if (direction == 17 || direction == 1) {
            handled = pageLeft();
        } else if (direction == 66 || direction == 2) {
            handled = pageRight();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    boolean pageRight() {
        if (this.mCurItem >= 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }
}
