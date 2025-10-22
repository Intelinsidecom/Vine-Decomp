package co.vine.android.views.swipeable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import co.vine.android.R;
import co.vine.android.views.swipeable.FlingCardListener;

/* loaded from: classes.dex */
public class SwipeFlingAdapterView extends BaseFlingAdapterView {
    private int LAST_OBJECT_IN_STACK;
    private int MAX_VISIBLE;
    private int MIN_ADAPTER_STACK;
    private float ROTATION_DEGREES;
    private View mActiveCard;
    private Adapter mAdapter;
    private AdapterDataSetObserver mDataSetObserver;
    private FlingCardListener mFlingCardListener;
    private onFlingListener mFlingListener;
    private boolean mInLayout;
    private PointF mLastTouchPoint;
    private OnItemClickListener mOnItemClickListener;
    private boolean mSwipeEnabled;

    public interface OnItemClickListener {
        void onItemClicked(int i, Object obj);
    }

    public interface onFlingListener {
        void onAdapterAboutToEmpty(int i);

        void onLeftCardExit(Object obj);

        void onRightCardExit(Object obj);

        void onScroll(float f);

        void removeFirstObjectInAdapter();
    }

    @Override // co.vine.android.views.swipeable.BaseFlingAdapterView
    public /* bridge */ /* synthetic */ int getHeightMeasureSpec() {
        return super.getHeightMeasureSpec();
    }

    @Override // co.vine.android.views.swipeable.BaseFlingAdapterView
    public /* bridge */ /* synthetic */ int getWidthMeasureSpec() {
        return super.getWidthMeasureSpec();
    }

    @Override // co.vine.android.views.swipeable.BaseFlingAdapterView, android.widget.AdapterView
    public /* bridge */ /* synthetic */ void setSelection(int i) {
        super.setSelection(i);
    }

    public SwipeFlingAdapterView(Context context) {
        this(context, null);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeFlingStyle);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.MAX_VISIBLE = 4;
        this.MIN_ADAPTER_STACK = 6;
        this.ROTATION_DEGREES = 15.0f;
        this.LAST_OBJECT_IN_STACK = 0;
        this.mInLayout = false;
        this.mActiveCard = null;
        this.mSwipeEnabled = true;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView, defStyle, 0);
        this.MAX_VISIBLE = a.getInt(3, this.MAX_VISIBLE);
        this.MIN_ADAPTER_STACK = a.getInt(2, this.MIN_ADAPTER_STACK);
        this.ROTATION_DEGREES = a.getFloat(1, this.ROTATION_DEGREES);
        a.recycle();
    }

    @Override // android.widget.AdapterView
    public View getSelectedView() {
        return this.mActiveCard;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    @Override // android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            this.mInLayout = true;
            int adapterCount = this.mAdapter.getCount();
            if (adapterCount == 0) {
                removeAllViewsInLayout();
            } else {
                View topCard = getChildAt(this.LAST_OBJECT_IN_STACK);
                if (this.mActiveCard != null && topCard != null && topCard == this.mActiveCard && this.mFlingCardListener != null) {
                    if (this.mFlingCardListener.isTouching()) {
                        PointF lastPoint = this.mFlingCardListener.getLastPoint();
                        if (this.mLastTouchPoint == null || !this.mLastTouchPoint.equals(lastPoint)) {
                            this.mLastTouchPoint = lastPoint;
                            removeViewsInLayout(0, this.LAST_OBJECT_IN_STACK);
                            layoutChildren(1, adapterCount);
                        }
                    }
                } else {
                    removeAllViewsInLayout();
                    layoutChildren(0, adapterCount);
                    setTopView();
                }
            }
            this.mInLayout = false;
            if (adapterCount <= this.MIN_ADAPTER_STACK) {
                this.mFlingListener.onAdapterAboutToEmpty(adapterCount);
            }
        }
    }

    private void layoutChildren(int startingIndex, int adapterCount) {
        while (startingIndex < Math.min(adapterCount, this.MAX_VISIBLE)) {
            View newUnderChild = this.mAdapter.getView(startingIndex, null, this);
            if (newUnderChild.getVisibility() != 8) {
                makeAndAddView(newUnderChild);
                this.LAST_OBJECT_IN_STACK = startingIndex;
            }
            startingIndex++;
        }
    }

    @TargetApi(17)
    private void makeAndAddView(View child) {
        int childLeft;
        int childTop;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        addViewInLayout(child, 0, lp, true);
        boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(), getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(), getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = 8388659;
        }
        int layoutDirection = getLayoutDirection();
        int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        int verticalGravity = gravity & 112;
        switch (absoluteGravity & 7) {
            case 1:
                childLeft = (((((getWidth() + getPaddingLeft()) - getPaddingRight()) - w) / 2) + lp.leftMargin) - lp.rightMargin;
                break;
            case GravityCompat.END /* 8388613 */:
                childLeft = ((getWidth() + getPaddingRight()) - w) - lp.rightMargin;
                break;
            default:
                childLeft = getPaddingLeft() + lp.leftMargin;
                break;
        }
        switch (verticalGravity) {
            case 16:
                childTop = (((((getHeight() + getPaddingTop()) - getPaddingBottom()) - h) / 2) + lp.topMargin) - lp.bottomMargin;
                break;
            case 80:
                childTop = ((getHeight() - getPaddingBottom()) - h) - lp.bottomMargin;
                break;
            default:
                childTop = getPaddingTop() + lp.topMargin;
                break;
        }
        child.layout(childLeft, childTop, childLeft + w, childTop + h);
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.mSwipeEnabled = swipeEnabled;
    }

    private void setTopView() {
        if (getChildCount() > 0) {
            this.mActiveCard = getChildAt(this.LAST_OBJECT_IN_STACK);
            if (this.mActiveCard != null && this.mSwipeEnabled) {
                this.mFlingCardListener = new FlingCardListener(this.mActiveCard, this.mAdapter.getItem(0), this.ROTATION_DEGREES, new FlingCardListener.FlingListener() { // from class: co.vine.android.views.swipeable.SwipeFlingAdapterView.1
                    @Override // co.vine.android.views.swipeable.FlingCardListener.FlingListener
                    public void onCardExited() {
                        SwipeFlingAdapterView.this.mActiveCard = null;
                        SwipeFlingAdapterView.this.mFlingListener.removeFirstObjectInAdapter();
                    }

                    @Override // co.vine.android.views.swipeable.FlingCardListener.FlingListener
                    public void leftExit(Object dataObject) {
                        SwipeFlingAdapterView.this.mFlingListener.onLeftCardExit(dataObject);
                    }

                    @Override // co.vine.android.views.swipeable.FlingCardListener.FlingListener
                    public void rightExit(Object dataObject) {
                        SwipeFlingAdapterView.this.mFlingListener.onRightCardExit(dataObject);
                    }

                    @Override // co.vine.android.views.swipeable.FlingCardListener.FlingListener
                    public void onClick(Object dataObject) {
                        if (SwipeFlingAdapterView.this.mOnItemClickListener != null) {
                            SwipeFlingAdapterView.this.mOnItemClickListener.onItemClicked(0, dataObject);
                        }
                    }

                    @Override // co.vine.android.views.swipeable.FlingCardListener.FlingListener
                    public void onScroll(float scrollProgressPercent) {
                        SwipeFlingAdapterView.this.mFlingListener.onScroll(scrollProgressPercent);
                    }
                });
                View frame = this.mActiveCard.findViewById(R.id.content);
                if (frame == null) {
                    this.mActiveCard.setOnTouchListener(this.mFlingCardListener);
                } else {
                    frame.setOnTouchListener(this.mFlingCardListener);
                }
            }
        }
    }

    public FlingCardListener getTopCardListener() throws NullPointerException {
        if (this.mFlingCardListener == null) {
            throw new NullPointerException();
        }
        return this.mFlingCardListener;
    }

    public void setMaxVisible(int MAX_VISIBLE) {
        this.MAX_VISIBLE = MAX_VISIBLE;
    }

    public void setMinStackInAdapter(int MIN_ADAPTER_STACK) {
        this.MIN_ADAPTER_STACK = MIN_ADAPTER_STACK;
    }

    @Override // android.widget.AdapterView
    public Adapter getAdapter() {
        return this.mAdapter;
    }

    @Override // android.widget.AdapterView
    public void setAdapter(Adapter adapter) {
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.mDataSetObserver = null;
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null && this.mDataSetObserver == null) {
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        }
    }

    public void setFlingListener(onFlingListener onFlingListener2) {
        this.mFlingListener = onFlingListener2;
    }

    public onFlingListener getOnFlingListener() {
        return this.mFlingListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        private AdapterDataSetObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            SwipeFlingAdapterView.this.requestLayout();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            SwipeFlingAdapterView.this.requestLayout();
        }
    }
}
