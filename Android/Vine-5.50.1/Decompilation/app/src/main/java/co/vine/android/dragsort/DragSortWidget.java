package co.vine.android.dragsort;

import android.R;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import co.vine.android.animation.SmoothAnimator;
import co.vine.android.views.HorizontalListView;
import com.googlecode.javacv.cpp.avcodec;
import twitter4j.internal.http.HttpResponseCode;

@TargetApi(14)
/* loaded from: classes.dex */
public abstract class DragSortWidget extends RelativeLayout implements View.OnTouchListener, AdapterView.OnItemClickListener, HorizontalListView.OnScrollListener {
    private static final int PICK_UP_DURATION = ViewConfiguration.getTapTimeout() + 1;
    private Drawable mActivatedDrawable;
    private int mActivePointerId;
    private DragSortAdapterWrapper mAdapterWrapper;
    private CheckForLongPress mCheckForLongPress;
    private int mDownPosition;
    private DragListener mDragListener;
    private DragScroller mDragScroller;
    private DropAnimator mDropAnimator;
    boolean mDropping;
    private Bitmap mFloatBitmap;
    private ImageView mFloatView;
    private Drawable mFloatViewActivatedDrawable;
    private float mFloatViewAlpha;
    private FloatViewDestroyAnimator mFloatViewDestroyAnimator;
    private int mFloatViewHeight;
    private FloatViewInteractionListener mFloatViewInteractionListener;
    private int mFloatViewListPosition;
    private int mFloatViewWidth;
    private int mFloatViewX;
    private int mFloatViewY;
    private int mFocusedPosition;
    private HorizontalListView mListView;
    private float mMaxScrollSpeed;
    private RemoveAnimator mRemoveAnimator;
    private RemoveListener mRemoveListener;
    private int mRemovePosition;
    boolean mRemoving;
    private SelectionChangedListener mSelectionChangedListener;
    private int mSelectionPosition;
    private int mTouchDownX;
    private int mTouchDownY;
    private int mTouchListPosition;
    private final int mTouchSlop;
    private int mTouchX;
    private int mTouchY;
    private int mXOffsetFromTouch;
    private int mYOffsetFromTouch;

    public interface DragListener {
        void drag(int i, int i2);
    }

    public interface FloatViewInteractionListener {
        boolean canPickUpFloatView();

        boolean floatViewDropped(int i);

        void floatViewLanded(int i);

        void floatViewMoved();

        void floatViewPickedUp(int i);
    }

    public interface RemoveListener {
        void remove(int i);
    }

    public interface SelectionChangedListener {
        void onSelectionChanged(int i, boolean z);
    }

    public interface ShouldAnimateInProvider {
        boolean shouldAnimateIn(int i);
    }

    protected abstract Drawable getActiveDrawable(TypedArray typedArray);

    protected abstract Drawable getFloatViewActivatedDrawable(TypedArray typedArray);

    protected abstract TypedArray getStylingArray(Context context, AttributeSet attributeSet, int i);

    public DragSortWidget(Context context) {
        this(context, null);
    }

    public DragSortWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDragScroller = new DragScroller();
        this.mDropAnimator = new DropAnimator(0.05f, avcodec.AV_CODEC_ID_JV);
        this.mRemoveAnimator = new RemoveAnimator(0.05f, 100);
        this.mFloatViewDestroyAnimator = new FloatViewDestroyAnimator(0.05f, 100);
        this.mFloatView = null;
        this.mFloatViewListPosition = -1;
        this.mSelectionPosition = -1;
        this.mFloatViewAlpha = 255.0f;
        this.mDropping = false;
        this.mRemoving = false;
        TypedArray a = getStylingArray(context, attrs, defStyle);
        this.mActivatedDrawable = getActiveDrawable(a);
        this.mFloatViewActivatedDrawable = getFloatViewActivatedDrawable(a);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        a.recycle();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        this.mListView = (HorizontalListView) findViewById(R.id.list);
        this.mListView.setOnTouchListener(this);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setFingerScrollEnabled(true);
        this.mListView.setOnScrollListener(this);
        this.mCheckForLongPress = new CheckForLongPress();
        this.mMaxScrollSpeed = getResources().getDisplayMetrics().xdpi * 0.1f;
    }

    public void setFirstItem(int position) {
        this.mListView.setSelection(position);
        if (this.mFloatView != null && !this.mDropping) {
            dragFloatView(this.mFloatViewListPosition, getClosestItemPosition());
        }
    }

    public void setSelection(int selection) {
        this.mSelectionPosition = selection;
        if (selection != -1) {
            this.mFocusedPosition = selection;
        }
        adjustItems();
        invalidate();
    }

    public void setFocused(int position) {
        this.mFocusedPosition = position;
        adjustItems();
        this.mListView.invalidate();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean handleListViewTouch(MotionEvent event) {
        int action = event.getAction() & 255;
        this.mTouchListPosition = getClosestItemPosition();
        switch (action) {
            case 0:
                this.mTouchDownX = this.mTouchX;
                this.mTouchDownY = this.mTouchY;
                this.mActivePointerId = event.getPointerId(0);
                this.mDownPosition = this.mTouchListPosition;
                postDelayed(this.mCheckForLongPress, PICK_UP_DURATION);
                return false;
            case 1:
                break;
            case 2:
                int oldHoverPosition = this.mFloatViewListPosition;
                if (this.mTouchListPosition != -1) {
                    this.mFloatViewListPosition = this.mTouchListPosition;
                }
                int deltaY = Math.abs(this.mTouchY - this.mTouchDownY);
                int deltaX = Math.abs(this.mTouchX - this.mTouchDownX);
                boolean canPickupFloatView = this.mFloatViewInteractionListener != null && this.mFloatViewInteractionListener.canPickUpFloatView();
                if (this.mFloatView == null && deltaX < this.mTouchSlop && deltaY >= this.mTouchSlop && this.mTouchY - this.mTouchDownY < 0 && this.mSelectionPosition != this.mTouchListPosition && canPickupFloatView) {
                    handleClick(this.mTouchListPosition, false);
                }
                if (this.mFloatView == null && (deltaX >= this.mTouchSlop || deltaY >= this.mTouchSlop)) {
                    removeCallbacks(this.mCheckForLongPress);
                    if (canPickupFloatView && pickUpView(this.mFloatViewListPosition)) {
                        invalidate();
                        return true;
                    }
                } else if (this.mFloatView != null && !this.mDropping) {
                    this.mFloatViewX = this.mTouchX - this.mXOffsetFromTouch;
                    this.mFloatViewY = this.mTouchY - this.mYOffsetFromTouch;
                    if (this.mFloatViewInteractionListener != null) {
                        this.mFloatViewInteractionListener.floatViewMoved();
                    }
                    dragFloatView(oldHoverPosition, this.mFloatViewListPosition);
                    return true;
                }
                return false;
            case 3:
            case 4:
            case 5:
            default:
                return false;
            case 6:
                int pointerIndex = (event.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == this.mActivePointerId) {
                }
                return false;
        }
        this.mDownPosition = -1;
        removeCallbacks(this.mCheckForLongPress);
        if (this.mFloatView != null && !this.mDropping) {
            if (this.mFloatViewInteractionListener != null && this.mFloatViewInteractionListener.floatViewDropped(this.mFloatViewListPosition)) {
                this.mDropAnimator.start();
            }
            this.mDragScroller.stopScrolling(true);
            invalidate();
            return true;
        }
        return false;
    }

    public void deleteSelection() {
        this.mFloatViewDestroyAnimator.start();
        removeSelection();
    }

    public boolean onTouch(View v, MotionEvent event, int xOffset, int yOffset) {
        this.mTouchX = ((int) event.getX()) + v.getLeft() + xOffset;
        this.mTouchY = ((int) event.getY()) + v.getTop() + yOffset;
        if (v == this.mListView) {
            return handleListViewTouch(event);
        }
        return false;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        return onTouch(v, event, 0, 0);
    }

    private boolean floatViewIntersectsList() {
        return this.mFloatViewX <= this.mListView.getRight() && this.mFloatViewY <= this.mListView.getBottom() && this.mFloatViewX + this.mFloatViewWidth >= this.mListView.getLeft() && this.mFloatViewY + this.mFloatViewHeight >= this.mListView.getTop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dragFloatView(int oldFloatViewPosition, int newFloatViewPosition) {
        if (oldFloatViewPosition > -1 && newFloatViewPosition > -1 && oldFloatViewPosition != newFloatViewPosition && floatViewIntersectsList()) {
            this.mFloatViewListPosition = newFloatViewPosition;
            finishSwap(oldFloatViewPosition, newFloatViewPosition);
        } else if (floatViewIntersectsList() && oldFloatViewPosition == newFloatViewPosition) {
            View last = this.mListView.getChildAt(this.mListView.getLastVisiblePosition() - this.mListView.getFirstVisiblePosition());
            if (last != null && this.mFloatViewX > last.getRight()) {
                this.mFloatViewListPosition = this.mListView.getLastVisiblePosition();
                finishSwap(oldFloatViewPosition, this.mFloatViewListPosition);
            }
        } else {
            this.mFloatViewListPosition = oldFloatViewPosition;
        }
        handleDragScroll();
        invalidate();
    }

    private void triggerSwapAnimation(final int to, int from) {
        View oldSwapView = this.mListView.getChildAt(from - this.mListView.getFirstVisiblePosition());
        final int swapViewStartLeft = oldSwapView == null ? 0 : oldSwapView.getLeft();
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: co.vine.android.dragsort.DragSortWidget.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                View switchView = DragSortWidget.this.mListView.getChildAt(to - DragSortWidget.this.mListView.getFirstVisiblePosition());
                if (switchView != null) {
                    int swapViewNewLeft = switchView.getLeft();
                    int delta = swapViewStartLeft - swapViewNewLeft;
                    switchView.setTranslationX(delta);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(switchView, (Property<View, Float>) View.TRANSLATION_X, 0.0f);
                    animator.setDuration(100L);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.dragsort.DragSortWidget.1.1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public void onAnimationUpdate(ValueAnimator animation) {
                            DragSortWidget.this.mListView.invalidate();
                        }
                    });
                    animator.start();
                }
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishDrop() {
        this.mDropping = false;
        if (this.mFloatViewInteractionListener != null) {
            this.mFloatViewInteractionListener.floatViewLanded(this.mFloatViewListPosition);
        }
        destroyFloatView();
        adjustItems();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishRemoveSelection() {
        if (this.mRemovePosition != -1) {
            if (this.mRemoveListener != null) {
                this.mRemoveListener.remove(this.mRemovePosition);
            }
            this.mRemovePosition = -1;
            this.mSelectionPosition = getNextPositionForSelection(this.mSelectionPosition);
            this.mFocusedPosition = this.mSelectionPosition;
            if (this.mSelectionChangedListener != null) {
                this.mSelectionChangedListener.onSelectionChanged(this.mSelectionPosition, false);
            }
        }
    }

    private int getNextPositionForSelection(int mSelectionPosition) {
        return mSelectionPosition < this.mAdapterWrapper.getCount() ? mSelectionPosition : this.mAdapterWrapper.getCount() - 1;
    }

    public void removeSelection() {
        this.mRemoveAnimator.start();
    }

    private void finishSwap(int oldFloatViewPosition, int newFloatViewPosition) {
        if (this.mDragListener != null) {
            if (oldFloatViewPosition < newFloatViewPosition) {
                for (int i = oldFloatViewPosition; i < newFloatViewPosition; i++) {
                    if (i >= 0) {
                        this.mDragListener.drag(i, i + 1);
                        triggerSwapAnimation(i, i + 1);
                    }
                }
            } else {
                for (int i2 = oldFloatViewPosition; i2 > newFloatViewPosition; i2--) {
                    if (i2 >= 1) {
                        this.mDragListener.drag(i2, i2 - 1);
                        triggerSwapAnimation(i2, i2 - 1);
                    }
                }
            }
        }
        if (newFloatViewPosition != -1) {
            this.mSelectionPosition = newFloatViewPosition;
            this.mFocusedPosition = newFloatViewPosition;
        }
        adjustItems();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustItems() {
        for (int i = 0; i < this.mListView.getChildCount(); i++) {
            adjustItem(this.mListView.getFirstVisiblePosition() + i);
        }
    }

    private void adjustItem(int position) {
        View childView = this.mListView.getChildAt(position - this.mListView.getFirstVisiblePosition());
        if (childView != null) {
            if (this.mFloatView != null && position == this.mFloatViewListPosition) {
                childView.setVisibility(4);
                childView.setActivated(true);
            } else if (position == this.mSelectionPosition) {
                childView.setActivated(true);
                childView.setVisibility(0);
            } else {
                childView.setActivated(false);
                childView.setVisibility(0);
            }
            childView.setAlpha(position == this.mFocusedPosition ? 1.0f : 0.3f);
            childView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getClosestItemPosition() {
        for (int i = 0; i < this.mListView.getChildCount(); i++) {
            if (this.mTouchX - this.mListView.getLeft() < this.mListView.getChildAt(i).getRight()) {
                return this.mListView.getFirstVisiblePosition() + i;
            }
        }
        return -1;
    }

    private void handleDragScroll() {
        boolean intersectsList = floatViewIntersectsList();
        if (this.mTouchX - this.mListView.getLeft() > this.mListView.getWidth() * 2 * 0.33f && intersectsList) {
            this.mDragScroller.startScrolling(1);
        } else if (this.mTouchX - this.mListView.getLeft() < this.mListView.getWidth() * 0.33f && intersectsList) {
            this.mDragScroller.startScrolling(0);
        } else {
            this.mDragScroller.stopScrolling(true);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        View selection;
        super.dispatchDraw(canvas);
        if (this.mFloatView != null) {
            canvas.save();
            canvas.translate(this.mFloatViewX, this.mFloatViewY);
            canvas.clipRect(0, 0, this.mFloatViewWidth, this.mFloatViewHeight);
            canvas.saveLayerAlpha(0.0f, 0.0f, this.mFloatViewWidth, this.mFloatViewHeight, (int) this.mFloatViewAlpha, 31);
            this.mFloatView.layout(0, 0, this.mFloatViewWidth, this.mFloatViewHeight);
            this.mFloatView.draw(canvas);
            canvas.restore();
            canvas.restore();
            this.mFloatViewActivatedDrawable.setBounds(this.mFloatViewX, this.mFloatViewY, this.mFloatViewX + this.mFloatViewWidth, this.mFloatViewY + this.mFloatViewHeight);
            this.mFloatViewActivatedDrawable.setAlpha((int) this.mFloatViewAlpha);
            this.mFloatViewActivatedDrawable.draw(canvas);
            return;
        }
        if (this.mSelectionPosition != -1 && (selection = this.mListView.getChildAt(this.mSelectionPosition - this.mListView.getFirstVisiblePosition())) != null && !this.mRemoving) {
            this.mActivatedDrawable.setBounds(selection.getLeft() + this.mListView.getLeft(), selection.getTop() + this.mListView.getTop(), selection.getRight() + this.mListView.getLeft(), selection.getBottom() + this.mListView.getTop());
            this.mActivatedDrawable.setAlpha(255);
            this.mActivatedDrawable.draw(canvas);
        }
    }

    private void createFloatView(int position) {
        View v = this.mListView.getChildAt(position - this.mListView.getFirstVisiblePosition());
        if (v != null && this.mTouchDownX - this.mListView.getLeft() >= v.getLeft() && this.mTouchDownY - this.mListView.getTop() >= v.getTop() && this.mTouchDownX - this.mListView.getLeft() <= v.getRight() && this.mTouchDownY - this.mListView.getTop() <= v.getBottom()) {
            v.setDrawingCacheQuality(1048576);
            v.setDrawingCacheEnabled(true);
            this.mFloatBitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            if (this.mFloatView == null) {
                this.mFloatView = new ImageView(this.mListView.getContext());
            }
            this.mFloatView.setImageBitmap(this.mFloatBitmap);
            this.mFloatView.setLayoutParams(new ViewGroup.LayoutParams(v.getWidth(), v.getHeight()));
            this.mXOffsetFromTouch = this.mTouchDownX - (v.getLeft() + this.mListView.getLeft());
            this.mYOffsetFromTouch = this.mTouchDownY - (v.getTop() + this.mListView.getTop());
            this.mFloatViewWidth = v.getWidth();
            this.mFloatViewHeight = v.getHeight();
            this.mFloatViewX = this.mTouchX - this.mXOffsetFromTouch;
            this.mFloatViewY = this.mTouchY - this.mYOffsetFromTouch;
            this.mFloatViewAlpha = 255.0f;
            adjustItem(position);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyFloatView() {
        if (this.mFloatView != null) {
            this.mFloatView.setImageDrawable(null);
            this.mFloatBitmap = null;
            this.mFloatView = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean pickUpView(int position) {
        if (position != this.mSelectionPosition || position == -1) {
            return false;
        }
        createFloatView(position);
        if (this.mFloatViewInteractionListener != null) {
            this.mFloatViewInteractionListener.floatViewPickedUp(position);
        }
        invalidate();
        return true;
    }

    public void releaseReferences() {
        setAdapter(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleClick(int position, boolean listViewClick) {
        View oldChild;
        View view = this.mListView.getChildAt(position - this.mListView.getFirstVisiblePosition());
        if (view == null) {
            return false;
        }
        if (this.mSelectionPosition == position) {
            view.setActivated(false);
            this.mSelectionPosition = -1;
            this.mFocusedPosition = -1;
        } else {
            view.setActivated(true);
            if (this.mSelectionPosition != -1 && (oldChild = this.mListView.getChildAt(this.mSelectionPosition - this.mListView.getFirstVisiblePosition())) != null) {
                oldChild.setActivated(false);
            }
            this.mSelectionPosition = position;
            this.mFocusedPosition = position;
        }
        if (this.mSelectionChangedListener != null) {
            this.mSelectionChangedListener.onSelectionChanged(this.mSelectionPosition, listViewClick);
        }
        adjustItems();
        invalidate();
        return true;
    }

    public boolean hasFloatView() {
        return this.mFloatView != null;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.mTouchX - this.mListView.getLeft() >= 0 && this.mTouchX - this.mListView.getLeft() < this.mListView.getWidth() && this.mTouchY - this.mListView.getTop() >= 0 && this.mTouchY - this.mListView.getTop() < this.mListView.getHeight()) {
            handleClick(position, true);
        }
    }

    public Rect getFloatViewBounds(Rect r) {
        if (this.mFloatView == null) {
            return null;
        }
        r.set(this.mFloatViewX, this.mFloatViewY, this.mFloatViewX + this.mFloatViewWidth, this.mFloatViewY + this.mFloatViewHeight);
        return r;
    }

    public int[] getFloatViewPosition(int[] intersectLoc) {
        if (this.mFloatView == null) {
            return null;
        }
        intersectLoc[0] = this.mFloatViewX;
        intersectLoc[1] = this.mFloatViewY;
        return intersectLoc;
    }

    @Override // co.vine.android.views.HorizontalListView.OnScrollListener
    public void onScroll() {
        invalidate();
        removeCallbacks(this.mCheckForLongPress);
    }

    public void forceListLayout() {
        if (this.mListView != null) {
            this.mListView.forceLayout();
            this.mListView.invalidate();
        }
    }

    public int getSelection() {
        return this.mSelectionPosition;
    }

    public class DragScroller implements Runnable {
        private float dt;
        private int dx;
        private boolean mAbort;
        private long mCurrTime;
        private long mPrevTime;
        private float mScrollSpeed = 0.0f;
        private boolean mScrolling = false;
        private int scrollDir;
        private long tStart;

        public DragScroller() {
        }

        public void startScrolling(int dir) {
            if (!this.mScrolling) {
                this.mAbort = false;
                this.mScrolling = true;
                this.tStart = SystemClock.uptimeMillis();
                this.mPrevTime = this.tStart;
                this.scrollDir = dir;
                DragSortWidget.this.post(this);
            }
        }

        public void stopScrolling(boolean now) {
            if (now) {
                DragSortWidget.this.mListView.removeCallbacks(this);
                this.mScrolling = false;
            } else {
                this.mAbort = true;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.mAbort) {
                int oldHoverPosition = DragSortWidget.this.mFloatViewListPosition;
                int first = DragSortWidget.this.mListView.getFirstVisiblePosition();
                int last = DragSortWidget.this.mListView.getLastVisiblePosition();
                int count = DragSortWidget.this.mListView.getCount();
                int listWidth = DragSortWidget.this.mListView.getWidth();
                if (this.scrollDir == 0) {
                    View v = DragSortWidget.this.mListView.getChildAt(0);
                    if (v == null) {
                        this.mScrolling = false;
                        return;
                    }
                    if (first != 0 || v.getLeft() != 0) {
                        int denominator = DragSortWidget.this.mTouchX - DragSortWidget.this.mListView.getLeft();
                        if (denominator < 0) {
                            denominator = 0;
                        }
                        this.mScrollSpeed = DragSortWidget.this.mMaxScrollSpeed / (denominator + 1);
                    } else {
                        this.mScrolling = false;
                        return;
                    }
                } else {
                    View v2 = DragSortWidget.this.mListView.getChildAt(last - first);
                    if (v2 == null) {
                        this.mScrolling = false;
                        return;
                    }
                    if (last != count - 1 || v2.getRight() > listWidth) {
                        int denominator2 = (listWidth - DragSortWidget.this.mTouchX) + DragSortWidget.this.mListView.getLeft();
                        if (denominator2 < 0) {
                            denominator2 = 0;
                        }
                        this.mScrollSpeed = (-DragSortWidget.this.mMaxScrollSpeed) / (denominator2 + 1);
                    } else {
                        this.mScrolling = false;
                        return;
                    }
                }
                this.mCurrTime = SystemClock.uptimeMillis();
                this.dt = this.mCurrTime - this.mPrevTime;
                this.dx = Math.round(this.mScrollSpeed * this.dt);
                DragSortWidget.this.mListView.scroll(this.dx, this.dx);
                this.mPrevTime = this.mCurrTime;
                DragSortWidget.this.mFloatViewListPosition = DragSortWidget.this.getClosestItemPosition();
                if (DragSortWidget.this.mFloatViewListPosition == -1) {
                    DragSortWidget.this.mFloatViewListPosition = oldHoverPosition;
                }
                DragSortWidget.this.dragFloatView(oldHoverPosition, DragSortWidget.this.mFloatViewListPosition);
                DragSortWidget.this.post(this);
                return;
            }
            this.mScrolling = false;
        }
    }

    private class DropAnimator extends SmoothAnimator {
        private float mDeltaX;
        private float mDeltaY;
        private int mTargetPosition;
        private int mTargetX;
        private int mTargetY;

        public DropAnimator(float smoothness, int duration) {
            super(DragSortWidget.this, smoothness, duration);
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStart() {
            this.mTargetPosition = DragSortWidget.this.mFloatViewListPosition;
            View hoverItem = DragSortWidget.this.mListView.getChildAt(this.mTargetPosition - DragSortWidget.this.mListView.getFirstVisiblePosition());
            DragSortWidget.this.mDropping = true;
            if (hoverItem != null) {
                this.mTargetY = hoverItem.getTop() + DragSortWidget.this.mListView.getTop();
                this.mTargetX = hoverItem.getLeft() + DragSortWidget.this.mListView.getLeft();
            } else {
                DragSortWidget.this.mDropping = false;
                DragSortWidget.this.mFloatViewDestroyAnimator.start();
                cancel();
            }
            this.mDeltaX = DragSortWidget.this.mFloatViewX - this.mTargetX;
            this.mDeltaY = DragSortWidget.this.mFloatViewY - this.mTargetY;
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            int newTargetX = this.mTargetX;
            int newTargetY = this.mTargetY;
            View hoverItem = DragSortWidget.this.mListView.getChildAt(this.mTargetPosition - DragSortWidget.this.mListView.getFirstVisiblePosition());
            if (hoverItem != null) {
                newTargetY = hoverItem.getTop() + DragSortWidget.this.mListView.getTop();
                newTargetX = hoverItem.getLeft() + DragSortWidget.this.mListView.getLeft();
            }
            this.mTargetX = newTargetX;
            this.mTargetY = newTargetY;
            float f = 1.0f - smoothFrac;
            DragSortWidget.this.mFloatViewX = this.mTargetX + ((int) (this.mDeltaX * f));
            DragSortWidget.this.mFloatViewY = this.mTargetY + ((int) (this.mDeltaY * f));
            DragSortWidget.this.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            DragSortWidget.this.finishDrop();
        }
    }

    private class FloatViewDestroyAnimator extends SmoothAnimator {
        public FloatViewDestroyAnimator(float smoothness, int duration) {
            super(DragSortWidget.this, smoothness, duration);
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            DragSortWidget.this.mFloatViewAlpha = ((1.0f - smoothFrac) * 255.0f) - 50.0f;
            DragSortWidget.this.mFloatViewAlpha = DragSortWidget.this.mFloatViewAlpha >= 0.0f ? DragSortWidget.this.mFloatViewAlpha : 0.0f;
            DragSortWidget.this.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            DragSortWidget.this.mFloatViewAlpha = 0.0f;
            DragSortWidget.this.mFloatViewListPosition = -1;
            DragSortWidget.this.destroyFloatView();
            DragSortWidget.this.adjustItems();
            DragSortWidget.this.invalidate();
        }
    }

    private class RemoveAnimator extends SmoothAnimator {
        private int mWidth;

        public RemoveAnimator(float smoothness, int duration) {
            super(DragSortWidget.this, smoothness, duration);
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStart() {
            DragSortWidget.this.mRemovePosition = DragSortWidget.this.mSelectionPosition;
            View toMeasure = DragSortWidget.this.mListView.getChildAt(DragSortWidget.this.mRemovePosition - DragSortWidget.this.mListView.getFirstVisiblePosition());
            if (toMeasure == null) {
                DragSortWidget.this.finishRemoveSelection();
                cancel();
            } else {
                this.mWidth = toMeasure.getWidth();
                toMeasure.animate().alpha(0.0f).setDuration((long) this.mDurationF).start();
                ((DragSortListItem) toMeasure).hasTransientState = true;
                DragSortWidget.this.mRemoving = true;
            }
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            View v = DragSortWidget.this.mListView.getChildAt(DragSortWidget.this.mRemovePosition - DragSortWidget.this.mListView.getFirstVisiblePosition());
            if (v == null) {
                DragSortWidget.this.finishRemoveSelection();
                DragSortWidget.this.mRemoving = false;
                cancel();
            } else {
                float f = 1.0f - smoothFrac;
                this.mWidth = Math.round(this.mWidth * f);
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                lp.width = this.mWidth;
                v.setLayoutParams(lp);
                DragSortWidget.this.mListView.forceLayout();
            }
            DragSortWidget.this.mListView.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            View v = DragSortWidget.this.mListView.getChildAt(DragSortWidget.this.mRemovePosition - DragSortWidget.this.mListView.getFirstVisiblePosition());
            if (v != null) {
                v.setVisibility(8);
                v.animate().cancel();
                v.setAlpha(1.0f);
                ((DragSortListItem) v).hasTransientState = false;
            }
            DragSortWidget.this.finishRemoveSelection();
            DragSortWidget.this.mRemoving = false;
        }
    }

    private class DuplicateAnimator extends SmoothAnimator {
        private View mView;

        public DuplicateAnimator(View view) {
            super(DragSortWidget.this, 0.05f, HttpResponseCode.OK);
            this.mView = view;
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStart() {
            if (this.mView != null) {
                this.mView.setAlpha(1.0f);
            } else {
                cancel();
            }
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            if (this.mView != null) {
                this.mView.setAlpha((0.7f * (1.0f - smoothFrac)) + 0.3f);
            } else {
                cancel();
            }
            DragSortWidget.this.mListView.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            if (this.mView != null) {
                this.mView.setAlpha(0.3f);
            }
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            if (adapter instanceof FloatViewInteractionListener) {
                setFloatViewInteractionListener((FloatViewInteractionListener) adapter);
            }
            if (adapter instanceof DragListener) {
                setDragListener((DragListener) adapter);
            }
            if (adapter instanceof RemoveListener) {
                setRemoveListener((RemoveListener) adapter);
            }
            if (adapter instanceof SelectionChangedListener) {
                setSelectionChangedListener((SelectionChangedListener) adapter);
            }
            if (this.mAdapterWrapper == null) {
                this.mAdapterWrapper = new DragSortAdapterWrapper(getContext());
            }
            this.mAdapterWrapper.setAdapter(adapter);
            this.mListView.setAdapter((ListAdapter) this.mAdapterWrapper);
            return;
        }
        this.mListView.setAdapter((ListAdapter) null);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.setAdapter(null);
        }
    }

    public void setFloatViewInteractionListener(FloatViewInteractionListener l) {
        this.mFloatViewInteractionListener = l;
    }

    public void setRemoveListener(RemoveListener l) {
        this.mRemoveListener = l;
    }

    public void setDragListener(DragListener l) {
        this.mDragListener = l;
    }

    public void setSelectionChangedListener(SelectionChangedListener l) {
        this.mSelectionChangedListener = l;
    }

    public class DragSortListItem extends ViewGroup {
        public boolean hasTransientState;

        public DragSortListItem(Context context) {
            super(context);
            this.hasTransientState = false;
            setLayoutParams(new HorizontalListView.LayoutParams(-2, -1));
        }

        public Bitmap getBitmap() {
            BitmapDrawable bd;
            View v = getChildAt(0);
            if (v == null || (bd = (BitmapDrawable) v.getBackground()) == null) {
                return null;
            }
            return bd.getBitmap();
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            if (DragSortWidget.this.mActivatedDrawable != null) {
                DragSortWidget.this.mActivatedDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
            View child = getChildAt(0);
            if (child != null) {
                child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }

        @Override // android.view.View
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            View child = getChildAt(0);
            if (child == null) {
                setMeasuredDimension(0, 0);
                return;
            }
            if (child.isLayoutRequested()) {
                measureChild(child, View.MeasureSpec.makeMeasureSpec(0, 0), heightMeasureSpec);
            }
            if (widthMode == 0) {
                ViewGroup.LayoutParams lp = getLayoutParams();
                if (lp.width > 0 || this.hasTransientState) {
                    width = lp.width;
                } else {
                    width = child.getMeasuredWidth();
                }
            }
            setMeasuredDimension(width, height);
        }
    }

    public class DragSortAdapterWrapper extends BaseAdapter implements ListAdapter {
        private ListAdapter mAdapter;
        private Context mContext;
        private DataSetObserver mObserver = new DataSetObserver() { // from class: co.vine.android.dragsort.DragSortWidget.DragSortAdapterWrapper.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                DragSortAdapterWrapper.this.notifyDataSetChanged();
                DragSortWidget.this.adjustItems();
            }
        };

        public DragSortAdapterWrapper(Context context) {
            this.mContext = context;
        }

        public void setAdapter(ListAdapter adapter) {
            this.mAdapter = adapter;
            if (adapter != null) {
                adapter.registerDataSetObserver(this.mObserver);
            }
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.mAdapter != null) {
                return this.mAdapter.getCount();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.mAdapter.getItem(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return this.mAdapter.getItemId(position);
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public boolean isEnabled(int position) {
            return this.mAdapter.isEnabled(position);
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View child;
            DragSortListItem item;
            if (convertView != null) {
                item = (DragSortListItem) convertView;
                View oldChild = item.getChildAt(position);
                child = this.mAdapter.getView(position, oldChild, parent);
            } else {
                child = this.mAdapter.getView(position, null, parent);
                item = DragSortWidget.this.new DragSortListItem(this.mContext);
            }
            item.removeAllViews();
            item.addView(child);
            item.setActivated(position == DragSortWidget.this.mSelectionPosition);
            item.setAlpha(position == DragSortWidget.this.mFocusedPosition ? 1.0f : 0.3f);
            ViewGroup.LayoutParams lp = item.getLayoutParams();
            if (DragSortWidget.this.mRemoving && position == DragSortWidget.this.mRemovePosition) {
                lp.width = DragSortWidget.this.mRemoveAnimator.mWidth;
            } else {
                lp.width = 0;
            }
            item.setLayoutParams(lp);
            if (DragSortWidget.this.mFloatView != null && position == DragSortWidget.this.mFloatViewListPosition) {
                item.setVisibility(4);
            } else {
                item.setVisibility(0);
            }
            if (((ShouldAnimateInProvider) this.mAdapter).shouldAnimateIn(position)) {
                DragSortWidget.this.new DuplicateAnimator(item).start();
            }
            return item;
        }
    }

    public class CheckForLongPress implements Runnable {
        public CheckForLongPress() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DragSortWidget.this.mTouchListPosition == DragSortWidget.this.mDownPosition) {
                if (DragSortWidget.this.mTouchListPosition != DragSortWidget.this.mSelectionPosition) {
                    DragSortWidget.this.handleClick(DragSortWidget.this.mTouchListPosition, false);
                }
                DragSortWidget.this.mFloatViewListPosition = DragSortWidget.this.mTouchListPosition;
                if (DragSortWidget.this.pickUpView(DragSortWidget.this.mTouchListPosition)) {
                    DragSortWidget.this.mFloatViewX = DragSortWidget.this.mTouchX - DragSortWidget.this.mXOffsetFromTouch;
                    DragSortWidget.this.mFloatViewY = DragSortWidget.this.mTouchY - DragSortWidget.this.mYOffsetFromTouch;
                }
            }
        }
    }

    public void setContentView(View view, Rect bounds) {
        view.setOnTouchListener(this);
    }

    public void cleanUp() {
        this.mSelectionPosition = -1;
        this.mFocusedPosition = -1;
        this.mRemovePosition = -1;
        this.mDropping = false;
        this.mRemoving = false;
        destroyFloatView();
    }

    public boolean isVisible(int position) {
        return this.mListView != null && position >= this.mListView.getFirstVisiblePosition() && position <= this.mListView.getFirstVisiblePosition() + this.mListView.getChildCount();
    }
}
