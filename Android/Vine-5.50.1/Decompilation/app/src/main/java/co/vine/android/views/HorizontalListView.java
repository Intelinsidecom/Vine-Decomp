package co.vine.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;
import co.vine.android.dragsort.DragSortWidget;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class HorizontalListView extends AdapterView<ListAdapter> {
    private ListAdapter mAdapter;
    private int mChildFillWidth;
    private ChildWidthListener mChildWidthListener;
    private boolean mClipToPadding;
    private boolean mDataChanged;
    private DataSetObserver mDataSetObserver;
    private Drawable mDivider;
    private Paint mDividerPaint;
    private int mDividerWidth;
    private int mDownPosition;
    private final int mEdgePadding;
    private final float mFillWidthHeightRatio;
    private boolean mFingerScrollEnabled;
    private int mFirstVisiblePos;
    private final FlingRunnable mFlingRunnable;
    private int mHeightMeasureSpec;
    private final boolean[] mIsRecycled;
    private int mItemCount;
    private int mLastX;
    private int mLongPressMillis;
    private final int mMaximumFlingVelocity;
    private final int mMinimumFlingVelocity;
    private int mMotionPosition;
    private int mMotionX;
    private int mMotionY;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private OnScrollListener mOnScrollListener;
    private CheckForLongPress mPendingCheckForLongPress;
    private CheckForTap mPendingCheckForTap;
    private PerformClick mPerformClick;
    private final Recycler mRecycler;
    private Drawable mSelector;
    private Rect mTouchFrame;
    private int mTouchMode;
    private Runnable mTouchModeReset;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public interface ChildWidthListener {
        void onChildWidthMeasured(int i);
    }

    public interface OnScrollListener {
        void onScroll();
    }

    protected abstract int getEdgePadding(TypedArray typedArray);

    protected abstract float getFillWidthHeightRatio(TypedArray typedArray);

    protected abstract TypedArray getStylingArray(Context context, AttributeSet attributeSet, int i);

    protected abstract void initializeDividerStyles(TypedArray typedArray);

    public HorizontalListView(Context context) {
        this(context, null);
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTouchMode = -1;
        this.mRecycler = new Recycler();
        this.mIsRecycled = new boolean[1];
        this.mFirstVisiblePos = 0;
        this.mMotionX = Integer.MIN_VALUE;
        this.mMotionY = Integer.MIN_VALUE;
        this.mFingerScrollEnabled = true;
        this.mLastX = Integer.MIN_VALUE;
        this.mFlingRunnable = new FlingRunnable();
        this.mLongPressMillis = ViewConfiguration.getLongPressTimeout();
        TypedArray a = getStylingArray(context, attrs, defStyle);
        initializeDividerStyles(a);
        this.mEdgePadding = getEdgePadding(a);
        this.mFillWidthHeightRatio = getFillWidthHeightRatio(a);
        a.recycle();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override // android.widget.AdapterView
    public int getFirstVisiblePosition() {
        return this.mFirstVisiblePos;
    }

    @Override // android.widget.AdapterView
    public int getLastVisiblePosition() {
        return (this.mFirstVisiblePos + getChildCount()) - 1;
    }

    @Override // android.widget.AdapterView
    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRecycler.clear();
        if (this.mPendingCheckForTap != null) {
            removeCallbacks(this.mPendingCheckForTap);
        }
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter listAdapter) {
        if (listAdapter != this.mAdapter) {
            if (this.mDataSetObserver == null) {
                this.mDataSetObserver = new AdapterDataSetObserver();
            }
            if (this.mAdapter != null) {
                this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            }
            this.mAdapter = listAdapter;
            if (listAdapter != null) {
                listAdapter.registerDataSetObserver(this.mDataSetObserver);
                this.mItemCount = listAdapter.getCount();
            } else {
                this.mItemCount = 0;
            }
        }
    }

    @Override // android.widget.AdapterView
    public View getSelectedView() {
        return null;
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
        this.mFirstVisiblePos = position;
        detachAllViewsFromParent();
        layoutChildren();
        invalidate();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidth;
        int childHeight;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = obtainView(0, this.mIsRecycled);
            measureChild(child, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            this.mRecycler.addRecycleView(child, -1);
        } else {
            childWidth = getSuggestedMinimumWidth();
            childHeight = getSuggestedMinimumHeight();
        }
        if (widthMode == 0) {
            widthSize = getPaddingLeft() + getPaddingRight() + childWidth;
        } else if (widthMode == Integer.MIN_VALUE) {
            widthSize = measureWidthOfChildren(heightMeasureSpec, 0, -1, widthSize, -1);
        }
        if (heightMode == 0) {
            heightSize = getPaddingTop() + getPaddingBottom() + childHeight;
        }
        this.mChildFillWidth = 0;
        if (this.mFillWidthHeightRatio > 0.0f && this.mItemCount > 0) {
            int dividersWidth = this.mDividerWidth * (this.mItemCount - 1);
            int childFillWidth = ((widthSize - (this.mEdgePadding * 2)) - dividersWidth) / this.mItemCount;
            if (childFillWidth >= heightSize && childFillWidth <= this.mFillWidthHeightRatio * heightSize) {
                this.mChildFillWidth = childFillWidth;
            }
        }
        if (this.mChildWidthListener != null) {
            this.mChildWidthListener.onChildWidthMeasured(this.mChildFillWidth);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mHeightMeasureSpec = heightMeasureSpec;
    }

    private void measureChild(View child, int heightMeasureSpec) {
        int childWidthSpec;
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), p.height);
        int lpWidth = p.width;
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, 1073741824);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    final int measureWidthOfChildren(int heightMeasureSpec, int startPosition, int endPosition, int maxWidth, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return getPaddingLeft() + getPaddingRight();
        }
        int returnedWidth = getPaddingLeft() + getPaddingRight();
        int prevWidthWithoutPartialChild = 0;
        if (endPosition == -1) {
            endPosition = adapter.getCount() - 1;
        }
        Recycler recycleBin = this.mRecycler;
        boolean[] isRecycled = this.mIsRecycled;
        int i = startPosition;
        while (i <= endPosition) {
            View child = obtainView(i, isRecycled);
            measureChild(child, heightMeasureSpec);
            recycleBin.addRecycleView(child, -1);
            returnedWidth += child.getMeasuredWidth();
            if (returnedWidth >= maxWidth) {
                return (disallowPartialChildPosition < 0 || i <= disallowPartialChildPosition || prevWidthWithoutPartialChild <= 0 || returnedWidth == maxWidth) ? maxWidth : prevWidthWithoutPartialChild;
            }
            if (disallowPartialChildPosition >= 0 && i >= disallowPartialChildPosition) {
                prevWidthWithoutPartialChild = returnedWidth;
            }
            i++;
        }
        int prevWidthWithoutPartialChild2 = returnedWidth;
        return prevWidthWithoutPartialChild2;
    }

    protected void onLayoutCustom(boolean changed) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
        }
        layoutChildren();
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onLayoutCustom(changed);
    }

    private void recycleVelocityTracker() {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0399  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r23) {
        /*
            Method dump skipped, instructions count: 1056
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.views.HorizontalListView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void disallowParentIntercept(boolean disallow) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallow);
        }
    }

    public boolean scroll(int rawDeltaX, int incrementalDeltaX) {
        int overflowSpace;
        boolean shouldScroll;
        int childCount = getChildCount();
        boolean scrollRight = incrementalDeltaX < 0;
        int listLeft = getPaddingLeft();
        int listRight = getWidth() - getPaddingRight();
        int lastChildRight = getChildAt(childCount - 1).getRight();
        int firstChildLeft = getChildAt(0).getLeft();
        if (scrollRight) {
            overflowSpace = getOverflowRight();
        } else {
            overflowSpace = getOverflowLeft();
        }
        int firstVisiblePos = this.mFirstVisiblePos;
        if (scrollRight) {
            shouldScroll = firstVisiblePos + childCount < this.mItemCount || lastChildRight + rawDeltaX >= listRight - this.mEdgePadding;
        } else {
            shouldScroll = firstVisiblePos > 0 || firstChildLeft + rawDeltaX <= this.mEdgePadding + listLeft;
        }
        if (shouldScroll) {
            int start = 0;
            int count = 0;
            if (scrollRight) {
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (child.getRight() + rawDeltaX >= listLeft) {
                        break;
                    }
                    count++;
                    this.mRecycler.addRecycleView(child, firstVisiblePos + i);
                }
            } else {
                for (int i2 = childCount - 1; i2 >= 0; i2--) {
                    View child2 = getChildAt(i2);
                    if (child2.getLeft() + rawDeltaX <= listRight) {
                        break;
                    }
                    start = i2;
                    count++;
                    this.mRecycler.addRecycleView(child2, firstVisiblePos + i2);
                }
            }
            if (count > 0) {
                detachViewsFromParent(start, count);
                if (scrollRight) {
                    this.mFirstVisiblePos += count;
                }
            }
            int childCount2 = getChildCount();
            for (int i3 = 0; i3 < childCount2; i3++) {
                getChildAt(i3).offsetLeftAndRight(rawDeltaX);
            }
            invalidate();
            if (overflowSpace < Math.abs(incrementalDeltaX)) {
                fillGap(scrollRight);
            }
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScroll();
            }
        }
        return shouldScroll;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOverflowRight() {
        int listRight = (getWidth() - getPaddingRight()) - this.mEdgePadding;
        int lastChildRight = getChildAt(getChildCount() - 1).getRight();
        return lastChildRight - listRight;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOverflowLeft() {
        int listLeft = getPaddingLeft() + this.mEdgePadding;
        int firstChildLeft = getChildAt(0).getLeft();
        return listLeft - firstChildLeft;
    }

    private void fillGap(boolean right) {
        int nextRight;
        int nextLeft;
        int count = getChildCount();
        if (right) {
            if (count > 0) {
                nextLeft = getChildAt(count - 1).getRight() + this.mDividerWidth;
            } else {
                nextLeft = getPaddingLeft();
            }
            fillRight(this.mFirstVisiblePos + count, nextLeft);
            return;
        }
        if (count > 0) {
            nextRight = getChildAt(0).getLeft() - this.mDividerWidth;
        } else {
            nextRight = 0;
        }
        fillLeft(this.mFirstVisiblePos - 1, nextRight);
    }

    private void fillLeft(int pos, int nextRight) {
        int childrenTop = getPaddingTop();
        int listLeft = getPaddingLeft();
        while (nextRight > listLeft && pos >= 0) {
            View v = attachChild(pos, nextRight, childrenTop, false);
            nextRight = v.getLeft() - this.mDividerWidth;
            pos--;
        }
        if (this.mItemCount > 0 && pos == -1 && getOverflowLeft() < 0) {
            detachAllViewsFromParent();
            this.mFirstVisiblePos = 0;
            fillRight(0, getPaddingLeft());
            return;
        }
        this.mFirstVisiblePos = pos + 1;
    }

    private void fillRight(int pos, int nextLeft) {
        int childrenTop = getPaddingTop();
        int listRight = getWidth() - getPaddingRight();
        while (nextLeft < listRight && pos < this.mItemCount) {
            View v = attachChild(pos, nextLeft, childrenTop, true);
            nextLeft = v.getRight() + this.mDividerWidth;
            pos++;
        }
        int i = nextLeft - this.mDividerWidth;
        if (this.mItemCount <= 0 || pos != this.mItemCount || getOverflowRight() >= 0) {
            return;
        }
        if (this.mFirstVisiblePos > 0 || getOverflowLeft() > 0) {
            detachAllViewsFromParent();
            fillLeft(this.mItemCount - 1, listRight - this.mEdgePadding);
        }
    }

    private View attachChild(int position, int x, int y, boolean right) {
        View child;
        if (!this.mDataChanged && (child = this.mRecycler.getActiveView(position)) != null) {
            layoutChild(child, x, y, right, true);
            return child;
        }
        View child2 = obtainView(position, this.mIsRecycled);
        layoutChild(child2, x, y, right, this.mIsRecycled[0]);
        return child2;
    }

    private View obtainView(int position, boolean[] isRecycled) {
        View convertView = this.mRecycler.removeRecycleView(position);
        if (convertView != null) {
            View child = this.mAdapter.getView(position, convertView, this);
            if (child != convertView) {
                this.mRecycler.addRecycleView(convertView, position);
                return child;
            }
            isRecycled[0] = true;
            return child;
        }
        return this.mAdapter.getView(position, null, this);
    }

    private void layoutChild(View child, int x, int y, boolean right, boolean recycled) {
        int childWidthSpec;
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        if (recycled) {
            attachViewToParent(child, right ? -1 : 0, p);
        } else {
            addViewInLayout(child, right ? -1 : 0, p, true);
        }
        boolean needToMeasure = !recycled || child.isLayoutRequested();
        if (needToMeasure) {
            int childHeightSpec = ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), p.height);
            int lpWidth = this.mChildFillWidth > 0 ? this.mChildFillWidth : p.width;
            if (lpWidth > 0) {
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, 1073741824);
            } else {
                childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = right ? x : x - w;
        if (needToMeasure) {
            int childRight = childLeft + w;
            int childBottom = y + h;
            child.layout(childLeft, y, childRight, childBottom);
        } else {
            child.offsetLeftAndRight(childLeft - child.getLeft());
            child.offsetTopAndBottom(y - child.getTop());
        }
    }

    protected void layoutChildren() {
        boolean dataChanged = this.mDataChanged;
        int firstPos = this.mFirstVisiblePos;
        Recycler recyler = this.mRecycler;
        int childCount = getChildCount();
        View prevFirst = getChildAt(0);
        if (dataChanged) {
            for (int i = 0; i < childCount; i++) {
                recyler.addRecycleView(getChildAt(i), firstPos + i);
            }
        } else {
            recyler.fillActiveViews(firstPos, childCount);
        }
        detachAllViewsFromParent();
        if (prevFirst == null) {
            fillRight(firstPos, getPaddingLeft() + this.mEdgePadding);
        } else {
            fillRight(firstPos, prevFirst.getLeft());
        }
        recyler.recycleActiveViews();
        this.mDataChanged = false;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -1);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View child = getChildAt(i);
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return this.mFirstVisiblePos + i;
            }
        }
        return -1;
    }

    @Override // android.view.View
    public void setPressed(boolean pressed) {
        if (getParent() == null) {
            super.setPressed(pressed);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        int effectivePaddingLeft;
        int effectivePaddingRight;
        int dividerWidth = this.mDividerWidth;
        boolean drawDividers = dividerWidth > 0 && this.mDivider != null;
        if (drawDividers) {
            Rect bounds = new Rect();
            bounds.left = getPaddingLeft();
            bounds.right = getWidth() - getPaddingRight();
            int count = getChildCount();
            int first = this.mFirstVisiblePos;
            ListAdapter adapter = this.mAdapter;
            int scrollX = getScrollX();
            boolean fillForMissingDividers = isOpaque() && !super.isOpaque();
            if (fillForMissingDividers && this.mDividerPaint == null) {
                this.mDividerPaint = new Paint();
            }
            Paint paint = this.mDividerPaint;
            if (this.mClipToPadding) {
                effectivePaddingLeft = getPaddingLeft();
                effectivePaddingRight = getPaddingRight();
            } else {
                effectivePaddingLeft = 0;
                effectivePaddingRight = 0;
            }
            int listRight = (getWidth() + scrollX) - effectivePaddingRight;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                if (left > effectivePaddingLeft) {
                    if (adapter.isEnabled(first + i) && (i == count - 1 || adapter.isEnabled(first + i + 1))) {
                        bounds.left = left - dividerWidth;
                        bounds.right = left;
                        drawDivider(canvas, bounds);
                    } else if (fillForMissingDividers) {
                        bounds.left = left - dividerWidth;
                        bounds.right = left;
                        canvas.drawRect(bounds, paint);
                    }
                }
            }
            if (count > 0 && scrollX > 0 && drawDividers) {
                bounds.left = listRight;
                bounds.right = listRight + dividerWidth;
                drawDivider(canvas, bounds);
            }
        }
        super.dispatchDraw(canvas);
    }

    void drawDivider(Canvas canvas, Rect bounds) {
        Drawable divider = this.mDivider;
        divider.setBounds(bounds);
        divider.draw(canvas);
    }

    public void setDivider(Drawable divider) {
        if (divider != null) {
            this.mDividerWidth = divider.getIntrinsicWidth();
        } else {
            this.mDividerWidth = 0;
        }
        this.mDivider = divider;
        requestLayout();
        invalidate();
    }

    public void setDividerWidth(int dividerWidth) {
        this.mDividerWidth = dividerWidth;
        requestLayout();
        invalidate();
    }

    @Override // android.view.ViewGroup
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.mClipToPadding = clipToPadding;
    }

    public void setOnScrollListener(DragSortWidget onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public boolean recycled;
        public int recycledPosition;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        public AdapterDataSetObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            HorizontalListView.this.mItemCount = HorizontalListView.this.mAdapter.getCount();
            HorizontalListView.this.mDataChanged = true;
            HorizontalListView.this.requestLayout();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            HorizontalListView.this.mItemCount = 0;
            HorizontalListView.this.mDataChanged = true;
            HorizontalListView.this.requestLayout();
        }
    }

    private class Recycler {
        private View[] mActiveViews;
        private int mFirstActivePos;
        private final ArrayList<View> mRecycleViews = new ArrayList<>();

        public Recycler() {
        }

        public View getActiveView(int position) {
            if (this.mActiveViews != null) {
                int index = position - this.mFirstActivePos;
                View[] activeViews = this.mActiveViews;
                if (index >= 0 && index < activeViews.length) {
                    View v = activeViews[index];
                    activeViews[index] = null;
                    return v;
                }
            }
            return null;
        }

        public void recycleActiveViews() {
            View[] activeViews = this.mActiveViews;
            if (activeViews != null) {
                ArrayList<View> recycledViews = this.mRecycleViews;
                int count = activeViews.length;
                for (int i = count - 1; i >= 0; i--) {
                    View victim = activeViews[i];
                    if (victim != null) {
                        LayoutParams lp = (LayoutParams) victim.getLayoutParams();
                        activeViews[i] = null;
                        lp.recycledPosition = this.mFirstActivePos + i;
                        recycledViews.add(victim);
                    }
                }
                pruneRecycleViews();
            }
        }

        private void pruneRecycleViews() {
            int maxViews = this.mActiveViews.length;
            ArrayList<View> recycledViews = this.mRecycleViews;
            int size = recycledViews.size();
            int sizeDiff = size - maxViews;
            int size2 = size - 1;
            for (int i = 0; i < sizeDiff; i++) {
                HorizontalListView.this.removeDetachedView(recycledViews.remove(size2), false);
                size2--;
            }
        }

        public void fillActiveViews(int position, int childCount) {
            View[] activeViews = this.mActiveViews;
            if (activeViews == null || activeViews.length < childCount) {
                this.mActiveViews = new View[childCount];
                activeViews = this.mActiveViews;
            }
            this.mFirstActivePos = position;
            for (int i = 0; i < childCount; i++) {
                activeViews[i] = HorizontalListView.this.getChildAt(i);
            }
        }

        public View removeRecycleView(int position) {
            ArrayList<View> recycledViews = this.mRecycleViews;
            if (recycledViews.isEmpty()) {
                return null;
            }
            int size = recycledViews.size();
            for (int i = 0; i < size; i++) {
                View view = recycledViews.get(i);
                if (((LayoutParams) view.getLayoutParams()).recycledPosition == position) {
                    recycledViews.remove(i);
                    return view;
                }
            }
            return recycledViews.remove(size - 1);
        }

        public void addRecycleView(View view, int position) {
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            if (lp != null) {
                lp.recycledPosition = position;
                lp.recycled = true;
                this.mRecycleViews.add(view);
            }
        }

        public void clear() {
            ArrayList<View> recycledViews = this.mRecycleViews;
            int size = recycledViews.size();
            for (int i = size - 1; i >= 0; i--) {
                HorizontalListView.this.removeDetachedView(recycledViews.remove(i), false);
            }
        }
    }

    final class CheckForTap implements Runnable {
        CheckForTap() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Drawable d;
            Drawable d2;
            if (HorizontalListView.this.mTouchMode == 0) {
                HorizontalListView.this.mTouchMode = 1;
                View child = HorizontalListView.this.getChildAt(HorizontalListView.this.mMotionPosition - HorizontalListView.this.mFirstVisiblePos);
                if (child != null && !child.hasFocusable()) {
                    if (!HorizontalListView.this.mDataChanged) {
                        child.setPressed(true);
                        HorizontalListView.this.setPressed(true);
                        HorizontalListView.this.layoutChildren();
                        HorizontalListView.this.refreshDrawableState();
                        boolean longClickable = HorizontalListView.this.isLongClickable();
                        if (HorizontalListView.this.mSelector != null && (d2 = HorizontalListView.this.mSelector.getCurrent()) != null && (d2 instanceof TransitionDrawable)) {
                            if (longClickable) {
                                ((TransitionDrawable) d2).startTransition(HorizontalListView.this.mLongPressMillis);
                            } else {
                                ((TransitionDrawable) d2).resetTransition();
                            }
                        }
                        if (longClickable) {
                            if (HorizontalListView.this.mPendingCheckForLongPress == null) {
                                HorizontalListView.this.mPendingCheckForLongPress = new CheckForLongPress();
                            }
                            HorizontalListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                            HorizontalListView.this.postDelayed(HorizontalListView.this.mPendingCheckForLongPress, HorizontalListView.this.mLongPressMillis);
                        } else {
                            HorizontalListView.this.mTouchMode = 2;
                        }
                        if (HorizontalListView.this.mSelector != null && (d = HorizontalListView.this.mSelector.getCurrent()) != null && (d instanceof TransitionDrawable)) {
                            ((TransitionDrawable) d).resetTransition();
                        }
                    }
                    HorizontalListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private class CheckForLongPress implements Runnable {
        private int mOriginalAttachCount;

        private CheckForLongPress() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = HorizontalListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return HorizontalListView.this.hasWindowFocus() && HorizontalListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }

        @Override // java.lang.Runnable
        public void run() {
            int motionPosition = HorizontalListView.this.mMotionPosition;
            View child = HorizontalListView.this.getChildAt(motionPosition - HorizontalListView.this.mFirstVisiblePos);
            if (child != null) {
                int longPressPosition = HorizontalListView.this.mMotionPosition;
                long longPressId = HorizontalListView.this.mAdapter.getItemId(HorizontalListView.this.mMotionPosition);
                boolean handled = false;
                if (sameWindow() && !HorizontalListView.this.mDataChanged) {
                    handled = HorizontalListView.this.performLongPress(child, longPressPosition, longPressId);
                }
                if (handled) {
                    HorizontalListView.this.mTouchMode = -1;
                    HorizontalListView.this.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                HorizontalListView.this.mTouchMode = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean performLongPress(View child, int longPressPosition, long longPressId) {
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, child, longPressPosition, longPressId);
        }
        if (handled) {
            performHapticFeedback(0);
        }
        return handled;
    }

    private class PerformClick implements Runnable {
        public int clickMotionPosition;
        private int mOriginalAttachCount;

        public PerformClick() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = HorizontalListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return HorizontalListView.this.hasWindowFocus() && HorizontalListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view;
            if (!HorizontalListView.this.mDataChanged) {
                ListAdapter adapter = HorizontalListView.this.mAdapter;
                int motionPosition = this.clickMotionPosition;
                if (adapter != null && HorizontalListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow() && (view = HorizontalListView.this.getChildAt(motionPosition - HorizontalListView.this.mFirstVisiblePos)) != null) {
                    HorizontalListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                }
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private int mLastFlingX;
        private int mScreenWidth;
        private final Scroller mScroller;

        public FlingRunnable() {
            this.mScroller = new Scroller(HorizontalListView.this.getContext());
        }

        public void start(int initialVelocity) {
            if (initialVelocity != 0) {
                HorizontalListView.this.mTouchMode = 4;
                this.mScreenWidth = ((HorizontalListView.this.getWidth() - HorizontalListView.this.getPaddingLeft()) - HorizontalListView.this.getPaddingRight()) - 1;
                HorizontalListView.this.removeCallbacks(this);
                int initialX = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
                this.mLastFlingX = initialX;
                this.mScroller.fling(initialX, 0, initialVelocity, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
                HorizontalListView.this.post(this);
            }
        }

        public void stop() {
            if (HorizontalListView.this.mTouchMode == 4) {
                HorizontalListView.this.mTouchMode = -1;
                HorizontalListView.this.removeCallbacks(this);
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    HorizontalListView.this.invalidate();
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            int delta;
            int childCount = HorizontalListView.this.getChildCount();
            if (HorizontalListView.this.mItemCount == 0 || childCount == 0) {
                stop();
                return;
            }
            Scroller scroller = this.mScroller;
            boolean more = scroller.computeScrollOffset();
            int x = scroller.getCurrX();
            int delta2 = this.mLastFlingX - x;
            if (delta2 > 0) {
                delta = Math.min(this.mScreenWidth, delta2);
            } else {
                delta = Math.max(-this.mScreenWidth, delta2);
            }
            boolean scrolledWithinBounds = HorizontalListView.this.scroll(delta, delta);
            if (more) {
                if (scrolledWithinBounds) {
                    this.mLastFlingX = x;
                    HorizontalListView.this.post(this);
                    return;
                } else {
                    int delta3 = delta > 0 ? HorizontalListView.this.getOverflowLeft() : -HorizontalListView.this.getOverflowRight();
                    HorizontalListView.this.scroll(delta3, delta3);
                    stop();
                    return;
                }
            }
            stop();
        }
    }

    public void setChildWidthListener(ChildWidthListener widthListener) {
        this.mChildWidthListener = widthListener;
    }

    @Override // android.widget.AdapterView
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public void setLongPressDuration(int longPresMillis) {
        this.mLongPressMillis = longPresMillis;
    }

    public void setFingerScrollEnabled(boolean shouldScroll) {
        this.mFingerScrollEnabled = shouldScroll;
    }
}
