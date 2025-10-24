package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.os.TraceCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.AdapterHelper;
import android.support.v7.widget.ChildHelper;
import android.support.v7.widget.ViewInfoStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class RecyclerView extends ViewGroup implements NestedScrollingChild, ScrollingView {
    private static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    private static final Interpolator sQuinticInterpolator;
    private RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    private Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    private boolean mAdapterUpdateDuringMeasure;
    private EdgeEffectCompat mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    private boolean mClipToPadding;
    private boolean mDataSetHasChangedAfterLayout;
    private int mEatRequestLayout;
    private int mEatenAccessibilityChangeFlags;
    private boolean mFirstLayoutComplete;
    private boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    private final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    LayoutManager mLayout;
    private boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    private boolean mLayoutRequestEaten;
    private EdgeEffectCompat mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    private SavedState mPendingSavedState;
    private final boolean mPostUpdatesOnAnimation;
    private boolean mPostedAnimatorRunner;
    final Recycler mRecycler;
    private RecyclerListener mRecyclerListener;
    private EdgeEffectCompat mRightGlow;
    private final int[] mScrollConsumed;
    private float mScrollFactor;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private final NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    private final Rect mTempRect;
    private EdgeEffectCompat mTopGlow;
    private int mTouchSlop;
    private final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    private final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;

    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder(int i, int i2);
    }

    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View view);

        void onChildViewDetachedFromWindow(View view);
    }

    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);

        void onRequestDisallowInterceptTouchEvent(boolean z);

        void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);
    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler recycler, int i, int i2);
    }

    static {
        FORCE_INVALIDATE_DISPLAY_LIST = Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20;
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
        sQuinticInterpolator = new Interpolator() { // from class: android.support.v7.widget.RecyclerView.3
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float t) {
                float t2 = t - 1.0f;
                return (t2 * t2 * t2 * t2 * t2) + 1.0f;
            }
        };
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RecyclerView(Context context, AttributeSet attrs, int defStyle) throws NoSuchMethodException, SecurityException {
        super(context, attrs, defStyle);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() { // from class: android.support.v7.widget.RecyclerView.1
            @Override // java.lang.Runnable
            public void run() {
                if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
                    if (RecyclerView.this.mLayoutFrozen) {
                        RecyclerView.this.mLayoutRequestEaten = true;
                    } else {
                        RecyclerView.this.consumePendingUpdateOperations();
                    }
                }
            }
        };
        this.mTempRect = new Rect();
        this.mItemDecorations = new ArrayList<>();
        this.mOnItemTouchListeners = new ArrayList<>();
        this.mEatRequestLayout = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mLayoutOrScrollCounter = 0;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScrollFactor = Float.MIN_VALUE;
        this.mViewFlinger = new ViewFlinger();
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mItemAnimatorRunner = new Runnable() { // from class: android.support.v7.widget.RecyclerView.2
            @Override // java.lang.Runnable
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() { // from class: android.support.v7.widget.RecyclerView.4
            @Override // android.support.v7.widget.ViewInfoStore.ProcessCallback
            public void processDisappeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo info, ItemAnimator.ItemHolderInfo postInfo) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, info, postInfo);
            }

            @Override // android.support.v7.widget.ViewInfoStore.ProcessCallback
            public void processAppeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo info) {
                RecyclerView.this.animateAppearance(viewHolder, preInfo, info);
            }

            @Override // android.support.v7.widget.ViewInfoStore.ProcessCallback
            public void processPersistent(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo postInfo) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, preInfo, postInfo)) {
                        RecyclerView.this.postAnimationRunner();
                    }
                } else if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, preInfo, postInfo)) {
                    RecyclerView.this.postAnimationRunner();
                }
            }

            @Override // android.support.v7.widget.ViewInfoStore.ProcessCallback
            public void unused(ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        int version = Build.VERSION.SDK_INT;
        this.mPostUpdatesOnAnimation = version >= 16;
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        setWillNotDraw(ViewCompat.getOverScrollMode(this) == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyle, 0);
            String layoutManagerName = a.getString(R.styleable.RecyclerView_layoutManager);
            a.recycle();
            createLayoutManager(context, layoutManagerName, attrs, defStyle, 0);
        }
        this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate) {
        this.mAccessibilityDelegate = accessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
    }

    private void createLayoutManager(Context context, String className, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws NoSuchMethodException, SecurityException {
        ClassLoader classLoader;
        Constructor<? extends LayoutManager> constructor;
        if (className != null) {
            String className2 = className.trim();
            if (className2.length() != 0) {
                String className3 = getFullClassName(context, className2);
                try {
                    if (isInEditMode()) {
                        classLoader = getClass().getClassLoader();
                    } else {
                        classLoader = context.getClassLoader();
                    }
                    Class<? extends U> clsAsSubclass = classLoader.loadClass(className3).asSubclass(LayoutManager.class);
                    Object[] constructorArgs = null;
                    try {
                        constructor = clsAsSubclass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        Object[] constructorArgs2 = {context, attrs, Integer.valueOf(defStyleAttr), Integer.valueOf(defStyleRes)};
                        constructorArgs = constructorArgs2;
                    } catch (NoSuchMethodException e) {
                        try {
                            constructor = clsAsSubclass.getConstructor(new Class[0]);
                        } catch (NoSuchMethodException e1) {
                            e1.initCause(e);
                            throw new IllegalStateException(attrs.getPositionDescription() + ": Error creating LayoutManager " + className3, e1);
                        }
                    }
                    constructor.setAccessible(true);
                    setLayoutManager((LayoutManager) constructor.newInstance(constructorArgs));
                } catch (ClassCastException e2) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Class is not a LayoutManager " + className3, e2);
                } catch (ClassNotFoundException e3) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Unable to find LayoutManager " + className3, e3);
                } catch (IllegalAccessException e4) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Cannot access non-public constructor " + className3, e4);
                } catch (InstantiationException e5) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className3, e5);
                } catch (InvocationTargetException e6) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className3, e6);
                }
            }
        }
    }

    private String getFullClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        return !className.contains(".") ? RecyclerView.class.getPackage().getName() + '.' + className : className;
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new ChildHelper.Callback() { // from class: android.support.v7.widget.RecyclerView.5
            @Override // android.support.v7.widget.ChildHelper.Callback
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void addView(View child, int index) {
                RecyclerView.this.addView(child, index);
                RecyclerView.this.dispatchChildAttached(child);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public int indexOfChild(View view) {
                return RecyclerView.this.indexOfChild(view);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void removeViewAt(int index) {
                View child = RecyclerView.this.getChildAt(index);
                if (child != null) {
                    RecyclerView.this.dispatchChildDetached(child);
                }
                RecyclerView.this.removeViewAt(index);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public View getChildAt(int offset) {
                return RecyclerView.this.getChildAt(offset);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void removeAllViews() {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    RecyclerView.this.dispatchChildDetached(getChildAt(i));
                }
                RecyclerView.this.removeAllViews();
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public ViewHolder getChildViewHolder(View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams) {
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh != null) {
                    if (!vh.isTmpDetached() && !vh.shouldIgnore()) {
                        throw new IllegalArgumentException("Called attach on a child which is not detached: " + vh);
                    }
                    vh.clearTmpDetachFlag();
                }
                RecyclerView.this.attachViewToParent(child, index, layoutParams);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void detachViewFromParent(int offset) {
                ViewHolder vh;
                View view = getChildAt(offset);
                if (view != null && (vh = RecyclerView.getChildViewHolderInt(view)) != null) {
                    if (vh.isTmpDetached() && !vh.shouldIgnore()) {
                        throw new IllegalArgumentException("called detach on an already detached child " + vh);
                    }
                    vh.addFlags(256);
                }
                RecyclerView.this.detachViewFromParent(offset);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void onEnteredHiddenState(View child) {
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh == null) {
                    return;
                }
                vh.onEnteredHiddenState();
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void onLeftHiddenState(View child) {
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh == null) {
                    return;
                }
                vh.onLeftHiddenState();
            }
        });
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() { // from class: android.support.v7.widget.RecyclerView.6
            @Override // android.support.v7.widget.AdapterHelper.Callback
            public ViewHolder findViewHolder(int position) {
                ViewHolder vh = RecyclerView.this.findViewHolderForPosition(position, true);
                if (vh == null || RecyclerView.this.mChildHelper.isHidden(vh.itemView)) {
                    return null;
                }
                return vh;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForRemovingInvisible(int start, int count) {
                RecyclerView.this.offsetPositionRecordsForRemove(start, count, true);
                RecyclerView.this.mItemsAddedOrRemoved = true;
                State.access$1712(RecyclerView.this.mState, count);
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForRemove(positionStart, itemCount, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void markViewHoldersUpdated(int positionStart, int itemCount, Object payload) {
                RecyclerView.this.viewRangeUpdate(positionStart, itemCount, payload);
                RecyclerView.this.mItemsChanged = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void onDispatchFirstPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            void dispatchUpdate(AdapterHelper.UpdateOp op) {
                switch (op.cmd) {
                    case 1:
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, op.positionStart, op.itemCount);
                        break;
                    case 2:
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, op.positionStart, op.itemCount);
                        break;
                    case 4:
                        RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, op.positionStart, op.itemCount, op.payload);
                        break;
                    case 8:
                        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, op.positionStart, op.itemCount, 1);
                        break;
                }
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void onDispatchSecondPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForAdd(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForInsert(positionStart, itemCount);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForMove(int from, int to) {
                RecyclerView.this.offsetPositionRecordsForMove(from, to);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
        });
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        this.mHasFixedSize = hasFixedSize;
    }

    @Override // android.view.ViewGroup
    public void setClipToPadding(boolean clipToPadding) {
        if (clipToPadding != this.mClipToPadding) {
            invalidateGlows();
        }
        this.mClipToPadding = clipToPadding;
        super.setClipToPadding(clipToPadding);
        if (this.mFirstLayoutComplete) {
            requestLayout();
        }
    }

    public void setScrollingTouchSlop(int slopConstant) {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            case 0:
                break;
            case 1:
                this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(vc);
                return;
            default:
                Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
                break;
        }
        this.mTouchSlop = vc.getScaledTouchSlop();
    }

    public void setAdapter(Adapter adapter) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, false, true);
        requestLayout();
    }

    private void setAdapterInternal(Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleViews) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!compatibleWithPrevious || removeAndRecycleViews) {
            if (this.mItemAnimator != null) {
                this.mItemAnimator.endAnimations();
            }
            if (this.mLayout != null) {
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            }
            this.mRecycler.clear();
        }
        this.mAdapterHelper.reset();
        Adapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(oldAdapter, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(oldAdapter, this.mAdapter, compatibleWithPrevious);
        this.mState.mStructureChanged = true;
        markKnownViewsInvalid();
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecyclerListener = listener;
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.mLayout != null ? this.mLayout.getBaseline() : super.getBaseline();
    }

    public void setLayoutManager(LayoutManager layout) {
        if (layout != this.mLayout) {
            stopScroll();
            if (this.mLayout != null) {
                if (this.mIsAttached) {
                    this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
                }
                this.mLayout.setRecyclerView(null);
            }
            this.mRecycler.clear();
            this.mChildHelper.removeAllViewsUnfiltered();
            this.mLayout = layout;
            if (layout != null) {
                if (layout.mRecyclerView != null) {
                    throw new IllegalArgumentException("LayoutManager " + layout + " is already attached to a RecyclerView: " + layout.mRecyclerView);
                }
                this.mLayout.setRecyclerView(this);
                if (this.mIsAttached) {
                    this.mLayout.dispatchAttachedToWindow(this);
                }
            }
            requestLayout();
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState == null) {
            if (this.mLayout != null) {
                state.mLayoutState = this.mLayout.onSaveInstanceState();
            } else {
                state.mLayoutState = null;
            }
        } else {
            state.copyFrom(this.mPendingSavedState);
        }
        return state;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        this.mPendingSavedState = (SavedState) state;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean alreadyParented = view.getParent() == this;
        this.mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (!alreadyParented) {
            this.mChildHelper.addView(view, true);
        } else {
            this.mChildHelper.hide(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAnimatingView(View view) {
        eatRequestLayout();
        boolean removed = this.mChildHelper.removeViewIfHidden(view);
        if (removed) {
            ViewHolder viewHolder = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(viewHolder);
            this.mRecycler.recycleViewHolderInternal(viewHolder);
        }
        resumeRequestLayout(!removed);
        return removed;
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public void setRecycledViewPool(RecycledViewPool pool) {
        this.mRecycler.setRecycledViewPool(pool);
    }

    public void setViewCacheExtension(ViewCacheExtension extension) {
        this.mRecycler.setViewCacheExtension(extension);
    }

    public void setItemViewCacheSize(int size) {
        this.mRecycler.setViewCacheSize(size);
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollState(int state) {
        if (state != this.mScrollState) {
            this.mScrollState = state;
            if (state != 2) {
                stopScrollersInternal();
            }
            dispatchOnScrollStateChanged(state);
        }
    }

    public void addItemDecoration(ItemDecoration decor, int index) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(decor);
        } else {
            this.mItemDecorations.add(index, decor);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void addItemDecoration(ItemDecoration decor) {
        addItemDecoration(decor, -1);
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback != this.mChildDrawingOrderCallback) {
            this.mChildDrawingOrderCallback = childDrawingOrderCallback;
            setChildrenDrawingOrderEnabled(this.mChildDrawingOrderCallback != null);
        }
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList();
        }
        this.mScrollListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void jumpToPositionForSmoothScroller(int position) {
        if (this.mLayout != null) {
            this.mLayout.scrollToPosition(position);
            awakenScrollBars();
        }
    }

    public void smoothScrollToPosition(int position) {
        if (!this.mLayoutFrozen) {
            if (this.mLayout == null) {
                Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            } else {
                this.mLayout.smoothScrollToPosition(this, this.mState, position);
            }
        }
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    @Override // android.view.View
    public void scrollBy(int x, int y) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (!this.mLayoutFrozen) {
            boolean canScrollHorizontal = this.mLayout.canScrollHorizontally();
            boolean canScrollVertical = this.mLayout.canScrollVertically();
            if (canScrollHorizontal || canScrollVertical) {
                if (!canScrollHorizontal) {
                    x = 0;
                }
                if (!canScrollVertical) {
                    y = 0;
                }
                scrollByInternal(x, y, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumePendingUpdateOperations() {
        if (this.mFirstLayoutComplete) {
            if (this.mDataSetHasChangedAfterLayout) {
                TraceCompat.beginSection("RV FullInvalidate");
                dispatchLayout();
                TraceCompat.endSection();
                return;
            }
            if (this.mAdapterHelper.hasPendingUpdates()) {
                if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
                    TraceCompat.beginSection("RV PartialInvalidate");
                    eatRequestLayout();
                    this.mAdapterHelper.preProcess();
                    if (!this.mLayoutRequestEaten) {
                        if (hasUpdatedView()) {
                            dispatchLayout();
                        } else {
                            this.mAdapterHelper.consumePostponedUpdates();
                        }
                    }
                    resumeRequestLayout(true);
                    TraceCompat.endSection();
                    return;
                }
                if (this.mAdapterHelper.hasPendingUpdates()) {
                    TraceCompat.beginSection("RV FullInvalidate");
                    dispatchLayout();
                    TraceCompat.endSection();
                }
            }
        }
    }

    private boolean hasUpdatedView() {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (holder != null && !holder.shouldIgnore() && holder.isUpdated()) {
                return true;
            }
        }
        return false;
    }

    boolean scrollByInternal(int x, int y, MotionEvent ev) {
        int unconsumedX = 0;
        int unconsumedY = 0;
        int consumedX = 0;
        int consumedY = 0;
        consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            eatRequestLayout();
            onEnterLayoutOrScroll();
            TraceCompat.beginSection("RV Scroll");
            if (x != 0) {
                consumedX = this.mLayout.scrollHorizontallyBy(x, this.mRecycler, this.mState);
                unconsumedX = x - consumedX;
            }
            if (y != 0) {
                consumedY = this.mLayout.scrollVerticallyBy(y, this.mRecycler, this.mState);
                unconsumedY = y - consumedY;
            }
            TraceCompat.endSection();
            repositionShadowingViews();
            onExitLayoutOrScroll();
            resumeRequestLayout(false);
        }
        if (!this.mItemDecorations.isEmpty()) {
            invalidate();
        }
        if (dispatchNestedScroll(consumedX, consumedY, unconsumedX, unconsumedY, this.mScrollOffset)) {
            this.mLastTouchX -= this.mScrollOffset[0];
            this.mLastTouchY -= this.mScrollOffset[1];
            if (ev != null) {
                ev.offsetLocation(this.mScrollOffset[0], this.mScrollOffset[1]);
            }
            int[] iArr = this.mNestedOffsets;
            iArr[0] = iArr[0] + this.mScrollOffset[0];
            int[] iArr2 = this.mNestedOffsets;
            iArr2[1] = iArr2[1] + this.mScrollOffset[1];
        } else if (ViewCompat.getOverScrollMode(this) != 2) {
            if (ev != null) {
                pullGlows(ev.getX(), unconsumedX, ev.getY(), unconsumedY);
            }
            considerReleasingGlowsOnScroll(x, y);
        }
        if (consumedX != 0 || consumedY != 0) {
            dispatchOnScrolled(consumedX, consumedY);
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        return (consumedX == 0 && consumedY == 0) ? false : true;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeHorizontalScrollOffset() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeHorizontalScrollExtent() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeHorizontalScrollRange() {
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeVerticalScrollOffset() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeVerticalScrollExtent() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View, android.support.v4.view.ScrollingView
    public int computeVerticalScrollRange() {
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    void eatRequestLayout() {
        this.mEatRequestLayout++;
        if (this.mEatRequestLayout == 1 && !this.mLayoutFrozen) {
            this.mLayoutRequestEaten = false;
        }
    }

    void resumeRequestLayout(boolean performLayoutChildren) {
        if (this.mEatRequestLayout < 1) {
            this.mEatRequestLayout = 1;
        }
        if (!performLayoutChildren) {
            this.mLayoutRequestEaten = false;
        }
        if (this.mEatRequestLayout == 1) {
            if (performLayoutChildren && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
        this.mEatRequestLayout--;
    }

    public void setLayoutFrozen(boolean frozen) {
        if (frozen != this.mLayoutFrozen) {
            assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (!frozen) {
                this.mLayoutFrozen = false;
                if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                    requestLayout();
                }
                this.mLayoutRequestEaten = false;
                return;
            }
            long now = SystemClock.uptimeMillis();
            MotionEvent cancelEvent = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
            onTouchEvent(cancelEvent);
            this.mLayoutFrozen = true;
            this.mIgnoreMotionEventTillDown = true;
            stopScroll();
        }
    }

    public void smoothScrollBy(int dx, int dy) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (!this.mLayoutFrozen) {
            if (!this.mLayout.canScrollHorizontally()) {
                dx = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                dy = 0;
            }
            if (dx != 0 || dy != 0) {
                this.mViewFlinger.smoothScrollBy(dx, dy);
            }
        }
    }

    public boolean fling(int velocityX, int velocityY) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        boolean canScrollHorizontal = this.mLayout.canScrollHorizontally();
        boolean canScrollVertical = this.mLayout.canScrollVertically();
        if (!canScrollHorizontal || Math.abs(velocityX) < this.mMinFlingVelocity) {
            velocityX = 0;
        }
        if (!canScrollVertical || Math.abs(velocityY) < this.mMinFlingVelocity) {
            velocityY = 0;
        }
        if ((velocityX == 0 && velocityY == 0) || dispatchNestedPreFling(velocityX, velocityY)) {
            return false;
        }
        boolean canScroll = canScrollHorizontal || canScrollVertical;
        dispatchNestedFling(velocityX, velocityY, canScroll);
        if (!canScroll) {
            return false;
        }
        this.mViewFlinger.fling(Math.max(-this.mMaxFlingVelocity, Math.min(velocityX, this.mMaxFlingVelocity)), Math.max(-this.mMaxFlingVelocity, Math.min(velocityY, this.mMaxFlingVelocity)));
        return true;
    }

    public void stopScroll() {
        setScrollState(0);
        stopScrollersInternal();
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout != null) {
            this.mLayout.stopSmoothScroller();
        }
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    private void pullGlows(float x, float overscrollX, float y, float overscrollY) {
        boolean invalidate = false;
        if (overscrollX < 0.0f) {
            ensureLeftGlow();
            if (this.mLeftGlow.onPull((-overscrollX) / getWidth(), 1.0f - (y / getHeight()))) {
                invalidate = true;
            }
        } else if (overscrollX > 0.0f) {
            ensureRightGlow();
            if (this.mRightGlow.onPull(overscrollX / getWidth(), y / getHeight())) {
                invalidate = true;
            }
        }
        if (overscrollY < 0.0f) {
            ensureTopGlow();
            if (this.mTopGlow.onPull((-overscrollY) / getHeight(), x / getWidth())) {
                invalidate = true;
            }
        } else if (overscrollY > 0.0f) {
            ensureBottomGlow();
            if (this.mBottomGlow.onPull(overscrollY / getHeight(), 1.0f - (x / getWidth()))) {
                invalidate = true;
            }
        }
        if (invalidate || overscrollX != 0.0f || overscrollY != 0.0f) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void releaseGlows() {
        boolean needsInvalidate = this.mLeftGlow != null ? this.mLeftGlow.onRelease() : false;
        if (this.mTopGlow != null) {
            needsInvalidate |= this.mTopGlow.onRelease();
        }
        if (this.mRightGlow != null) {
            needsInvalidate |= this.mRightGlow.onRelease();
        }
        if (this.mBottomGlow != null) {
            needsInvalidate |= this.mBottomGlow.onRelease();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void considerReleasingGlowsOnScroll(int dx, int dy) {
        boolean needsInvalidate = false;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished() && dx > 0) {
            needsInvalidate = this.mLeftGlow.onRelease();
        }
        if (this.mRightGlow != null && !this.mRightGlow.isFinished() && dx < 0) {
            needsInvalidate |= this.mRightGlow.onRelease();
        }
        if (this.mTopGlow != null && !this.mTopGlow.isFinished() && dy > 0) {
            needsInvalidate |= this.mTopGlow.onRelease();
        }
        if (this.mBottomGlow != null && !this.mBottomGlow.isFinished() && dy < 0) {
            needsInvalidate |= this.mBottomGlow.onRelease();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void absorbGlows(int velocityX, int velocityY) {
        if (velocityX < 0) {
            ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-velocityX);
        } else if (velocityX > 0) {
            ensureRightGlow();
            this.mRightGlow.onAbsorb(velocityX);
        }
        if (velocityY < 0) {
            ensureTopGlow();
            this.mTopGlow.onAbsorb(-velocityY);
        } else if (velocityY > 0) {
            ensureBottomGlow();
            this.mBottomGlow.onAbsorb(velocityY);
        }
        if (velocityX != 0 || velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            this.mLeftGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mLeftGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureRightGlow() {
        if (this.mRightGlow == null) {
            this.mRightGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mRightGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureTopGlow() {
        if (this.mTopGlow == null) {
            this.mTopGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mTopGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            this.mBottomGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mBottomGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        View result = this.mLayout.onInterceptFocusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        FocusFinder ff = FocusFinder.getInstance();
        View result2 = ff.findNextFocus(this, focused, direction);
        if (result2 == null && this.mAdapter != null && this.mLayout != null && !isComputingLayout() && !this.mLayoutFrozen) {
            eatRequestLayout();
            result2 = this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
            resumeRequestLayout(false);
        }
        return result2 != null ? result2 : super.focusSearch(focused, direction);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, child, focused) && focused != null) {
            this.mTempRect.set(0, 0, focused.getWidth(), focused.getHeight());
            ViewGroup.LayoutParams focusedLayoutParams = focused.getLayoutParams();
            if (focusedLayoutParams instanceof LayoutParams) {
                LayoutParams lp = (LayoutParams) focusedLayoutParams;
                if (!lp.mInsetsDirty) {
                    Rect insets = lp.mDecorInsets;
                    this.mTempRect.left -= insets.left;
                    this.mTempRect.right += insets.right;
                    this.mTempRect.top -= insets.top;
                    this.mTempRect.bottom += insets.bottom;
                }
            }
            offsetDescendantRectToMyCoords(focused, this.mTempRect);
            offsetRectIntoDescendantCoords(child, this.mTempRect);
            requestChildRectangleOnScreen(child, this.mTempRect, this.mFirstLayoutComplete ? false : true);
        }
        super.requestChildFocus(child, focused);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        return this.mLayout.requestChildRectangleOnScreen(this, child, rect, immediate);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.mLayout == null || !this.mLayout.onAddFocusables(this, views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        this.mIsAttached = true;
        this.mFirstLayoutComplete = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        this.mFirstLayoutComplete = false;
        stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    void assertNotInLayoutOrScroll(String message) {
        if (isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
            }
            throw new IllegalStateException(message);
        }
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent e) {
        int action = e.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        int listenerCount = this.mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            OnItemTouchListener listener = this.mOnItemTouchListeners.get(i);
            if (listener.onInterceptTouchEvent(this, e) && action != 3) {
                this.mActiveOnItemTouchListener = listener;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchOnItemTouch(MotionEvent e) {
        int action = e.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (action == 0) {
                this.mActiveOnItemTouchListener = null;
            } else {
                this.mActiveOnItemTouchListener.onTouchEvent(this, e);
                if (action != 3 && action != 1) {
                    return true;
                }
                this.mActiveOnItemTouchListener = null;
                return true;
            }
        }
        if (action != 0) {
            int listenerCount = this.mOnItemTouchListeners.size();
            for (int i = 0; i < listenerCount; i++) {
                OnItemTouchListener listener = this.mOnItemTouchListeners.get(i);
                if (listener.onInterceptTouchEvent(this, e)) {
                    this.mActiveOnItemTouchListener = listener;
                    return true;
                }
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mLayoutFrozen) {
            return false;
        }
        if (dispatchOnItemTouchIntercept(e)) {
            cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        switch (action) {
            case 0:
                if (this.mIgnoreMotionEventTillDown) {
                    this.mIgnoreMotionEventTillDown = false;
                }
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, 0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                if (this.mScrollState == 2) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                }
                int[] iArr = this.mNestedOffsets;
                this.mNestedOffsets[1] = 0;
                iArr[0] = 0;
                int nestedScrollAxis = 0;
                if (canScrollHorizontally) {
                    nestedScrollAxis = 0 | 1;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= 2;
                }
                startNestedScroll(nestedScrollAxis);
                break;
            case 1:
                this.mVelocityTracker.clear();
                stopNestedScroll();
                break;
            case 2:
                int index = MotionEventCompat.findPointerIndex(e, this.mScrollPointerId);
                if (index < 0) {
                    Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                int x2 = (int) (MotionEventCompat.getX(e, index) + 0.5f);
                int y2 = (int) (MotionEventCompat.getY(e, index) + 0.5f);
                if (this.mScrollState != 1) {
                    int dx = x2 - this.mInitialTouchX;
                    int dy = y2 - this.mInitialTouchY;
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop) {
                        this.mLastTouchX = ((dx < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchX;
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop) {
                        this.mLastTouchY = ((dy < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchY;
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(1);
                        break;
                    }
                }
                break;
            case 3:
                cancelTouch();
                break;
            case 5:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                int x3 = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        return this.mScrollState == 1;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        int listenerCount = this.mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            OnItemTouchListener listener = this.mOnItemTouchListeners.get(i);
            listener.onRequestDisallowInterceptTouchEvent(disallowIntercept);
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        if (this.mLayoutFrozen || this.mIgnoreMotionEventTillDown) {
            return false;
        }
        if (dispatchOnItemTouch(e)) {
            cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        MotionEvent vtev = MotionEvent.obtain(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        if (action == 0) {
            int[] iArr = this.mNestedOffsets;
            this.mNestedOffsets[1] = 0;
            iArr[0] = 0;
        }
        vtev.offsetLocation(this.mNestedOffsets[0], this.mNestedOffsets[1]);
        switch (action) {
            case 0:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, 0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                int nestedScrollAxis = 0;
                if (canScrollHorizontally) {
                    nestedScrollAxis = 0 | 1;
                }
                if (canScrollVertically) {
                    nestedScrollAxis |= 2;
                }
                startNestedScroll(nestedScrollAxis);
                break;
            case 1:
                this.mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxFlingVelocity);
                float xvel = canScrollHorizontally ? -VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                float yvel = canScrollVertically ? -VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                if ((xvel == 0.0f && yvel == 0.0f) || !fling((int) xvel, (int) yvel)) {
                    setScrollState(0);
                }
                resetTouch();
                break;
            case 2:
                int index = MotionEventCompat.findPointerIndex(e, this.mScrollPointerId);
                if (index < 0) {
                    Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                int x2 = (int) (MotionEventCompat.getX(e, index) + 0.5f);
                int y2 = (int) (MotionEventCompat.getY(e, index) + 0.5f);
                int dx = this.mLastTouchX - x2;
                int dy = this.mLastTouchY - y2;
                if (dispatchNestedPreScroll(dx, dy, this.mScrollConsumed, this.mScrollOffset)) {
                    dx -= this.mScrollConsumed[0];
                    dy -= this.mScrollConsumed[1];
                    vtev.offsetLocation(this.mScrollOffset[0], this.mScrollOffset[1]);
                    int[] iArr2 = this.mNestedOffsets;
                    iArr2[0] = iArr2[0] + this.mScrollOffset[0];
                    int[] iArr3 = this.mNestedOffsets;
                    iArr3[1] = iArr3[1] + this.mScrollOffset[1];
                }
                if (this.mScrollState != 1) {
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop) {
                        if (dx > 0) {
                            dx -= this.mTouchSlop;
                        } else {
                            dx += this.mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop) {
                        if (dy > 0) {
                            dy -= this.mTouchSlop;
                        } else {
                            dy += this.mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(1);
                    }
                }
                if (this.mScrollState == 1) {
                    this.mLastTouchX = x2 - this.mScrollOffset[0];
                    this.mLastTouchY = y2 - this.mScrollOffset[1];
                    if (!canScrollHorizontally) {
                        dx = 0;
                    }
                    if (!canScrollVertically) {
                        dy = 0;
                    }
                    if (scrollByInternal(dx, dy, vtev)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                }
                break;
            case 3:
                cancelTouch();
                break;
            case 5:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                int x3 = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        if (!eventAddedToVelocityTracker) {
            this.mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void resetTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        stopNestedScroll();
        releaseGlows();
    }

    private void cancelTouch() {
        resetTouch();
        setScrollState(0);
    }

    private void onPointerUp(MotionEvent e) {
        int actionIndex = MotionEventCompat.getActionIndex(e);
        if (MotionEventCompat.getPointerId(e, actionIndex) == this.mScrollPointerId) {
            int newIndex = actionIndex == 0 ? 1 : 0;
            this.mScrollPointerId = MotionEventCompat.getPointerId(e, newIndex);
            int x = (int) (MotionEventCompat.getX(e, newIndex) + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            int y = (int) (MotionEventCompat.getY(e, newIndex) + 0.5f);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
        }
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        float vScroll;
        float hScroll;
        if (this.mLayout != null && !this.mLayoutFrozen && (MotionEventCompat.getSource(event) & 2) != 0 && event.getAction() == 8) {
            if (this.mLayout.canScrollVertically()) {
                vScroll = -MotionEventCompat.getAxisValue(event, 9);
            } else {
                vScroll = 0.0f;
            }
            if (this.mLayout.canScrollHorizontally()) {
                hScroll = MotionEventCompat.getAxisValue(event, 10);
            } else {
                hScroll = 0.0f;
            }
            if (vScroll != 0.0f || hScroll != 0.0f) {
                float scrollFactor = getScrollFactor();
                scrollByInternal((int) (hScroll * scrollFactor), (int) (vScroll * scrollFactor), event);
            }
        }
        return false;
    }

    private float getScrollFactor() {
        if (this.mScrollFactor == Float.MIN_VALUE) {
            TypedValue outValue = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, outValue, true)) {
                this.mScrollFactor = outValue.getDimension(getContext().getResources().getDisplayMetrics());
            } else {
                return 0.0f;
            }
        }
        return this.mScrollFactor;
    }

    @Override // android.view.View
    protected void onMeasure(int widthSpec, int heightSpec) {
        boolean skipMeasure = false;
        if (this.mLayout == null) {
            defaultOnMeasure(widthSpec, heightSpec);
            return;
        }
        if (this.mLayout.mAutoMeasure) {
            int widthMode = View.MeasureSpec.getMode(widthSpec);
            int heightMode = View.MeasureSpec.getMode(heightSpec);
            if (widthMode == 1073741824 && heightMode == 1073741824) {
                skipMeasure = true;
            }
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            if (skipMeasure || this.mAdapter == null) {
                return;
            }
            if (this.mState.mLayoutStep == 1) {
                dispatchLayoutStep1();
            }
            this.mLayout.setMeasureSpecs(widthSpec, heightSpec);
            this.mState.mIsMeasuring = true;
            dispatchLayoutStep2();
            this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
            if (this.mLayout.shouldMeasureTwice()) {
                this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
                this.mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
                return;
            }
            return;
        }
        if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            return;
        }
        if (this.mAdapterUpdateDuringMeasure) {
            eatRequestLayout();
            processAdapterUpdatesAndSetAnimationFlags();
            if (!this.mState.mRunPredictiveAnimations) {
                this.mAdapterHelper.consumeUpdatesInOnePass();
                this.mState.mInPreLayout = false;
            } else {
                this.mState.mInPreLayout = true;
            }
            this.mAdapterUpdateDuringMeasure = false;
            resumeRequestLayout(false);
        }
        if (this.mAdapter != null) {
            this.mState.mItemCount = this.mAdapter.getItemCount();
        } else {
            this.mState.mItemCount = 0;
        }
        eatRequestLayout();
        this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
        resumeRequestLayout(false);
        this.mState.mInPreLayout = false;
    }

    void defaultOnMeasure(int widthSpec, int heightSpec) {
        int width = LayoutManager.chooseSize(widthSpec, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth(this));
        int height = LayoutManager.chooseSize(heightSpec, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight(this));
        setMeasuredDimension(width, height);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            invalidateGlows();
        }
    }

    public void setItemAnimator(ItemAnimator animator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = animator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onEnterLayoutOrScroll() {
        this.mLayoutOrScrollCounter++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExitLayoutOrScroll() {
        this.mLayoutOrScrollCounter--;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            dispatchContentChangedIfNecessary();
        }
    }

    boolean isAccessibilityEnabled() {
        return this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled();
    }

    private void dispatchContentChangedIfNecessary() {
        int flags = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (flags != 0 && isAccessibilityEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(event, flags);
            sendAccessibilityEventUnchecked(event);
        }
    }

    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }

    boolean shouldDeferAccessibilityEvent(AccessibilityEvent event) {
        if (!isComputingLayout()) {
            return false;
        }
        int type = 0;
        if (event != null) {
            type = AccessibilityEventCompat.getContentChangeTypes(event);
        }
        if (type == 0) {
            type = 0;
        }
        this.mEatenAccessibilityChangeFlags |= type;
        return true;
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (!shouldDeferAccessibilityEvent(event)) {
            super.sendAccessibilityEventUnchecked(event);
        }
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            markKnownViewsInvalid();
            this.mLayout.onItemsChanged(this);
        }
        if (predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean animationTypeSupported = this.mItemsAddedOrRemoved || this.mItemsChanged;
        this.mState.mRunSimpleAnimations = this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || animationTypeSupported || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds());
        this.mState.mRunPredictiveAnimations = this.mState.mRunSimpleAnimations && animationTypeSupported && !this.mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled();
    }

    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e("RecyclerView", "No adapter attached; skipping layout");
            return;
        }
        if (this.mLayout == null) {
            Log.e("RecyclerView", "No layout manager attached; skipping layout");
            return;
        }
        this.mState.mIsMeasuring = false;
        if (this.mState.mLayoutStep == 1) {
            dispatchLayoutStep1();
            this.mLayout.setExactMeasureSpecsFrom(this);
            dispatchLayoutStep2();
        } else if (this.mAdapterHelper.hasUpdates() || this.mLayout.getWidth() != getWidth() || this.mLayout.getHeight() != getHeight()) {
            this.mLayout.setExactMeasureSpecsFrom(this);
            dispatchLayoutStep2();
        } else {
            this.mLayout.setExactMeasureSpecsFrom(this);
        }
        dispatchLayoutStep3();
    }

    private void dispatchLayoutStep1() {
        this.mState.assertLayoutStep(1);
        this.mState.mIsMeasuring = false;
        eatRequestLayout();
        this.mViewInfoStore.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        this.mState.mTrackOldChangeHolders = this.mState.mRunSimpleAnimations && this.mItemsChanged;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            int count = this.mChildHelper.getChildCount();
            for (int i = 0; i < count; i++) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!holder.shouldIgnore() && (!holder.isInvalid() || this.mAdapter.hasStableIds())) {
                    this.mViewInfoStore.addToPreLayout(holder, this.mItemAnimator.recordPreLayoutInformation(this.mState, holder, ItemAnimator.buildAdapterChangeFlagsForAnimations(holder), holder.getUnmodifiedPayloads()));
                    if (this.mState.mTrackOldChangeHolders && holder.isUpdated() && !holder.isRemoved() && !holder.shouldIgnore() && !holder.isInvalid()) {
                        long key = getChangedHolderKey(holder);
                        this.mViewInfoStore.addToOldChangeHolders(key, holder);
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            saveOldPositions();
            boolean didStructureChange = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = didStructureChange;
            for (int i2 = 0; i2 < this.mChildHelper.getChildCount(); i2++) {
                View child = this.mChildHelper.getChildAt(i2);
                ViewHolder viewHolder = getChildViewHolderInt(child);
                if (!viewHolder.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(viewHolder)) {
                    int flags = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
                    boolean wasHidden = viewHolder.hasAnyOfTheFlags(8192);
                    if (!wasHidden) {
                        flags |= 4096;
                    }
                    ItemAnimator.ItemHolderInfo animationInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, flags, viewHolder.getUnmodifiedPayloads());
                    if (wasHidden) {
                        recordAnimationInfoIfBouncedHiddenView(viewHolder, animationInfo);
                    } else {
                        this.mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, animationInfo);
                    }
                }
            }
            clearOldPositions();
        } else {
            clearOldPositions();
        }
        onExitLayoutOrScroll();
        resumeRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }

    private void dispatchLayoutStep2() {
        eatRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        this.mState.mRunSimpleAnimations = this.mState.mRunSimpleAnimations && this.mItemAnimator != null;
        this.mState.mLayoutStep = 4;
        onExitLayoutOrScroll();
        resumeRequestLayout(false);
    }

    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        eatRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!holder.shouldIgnore()) {
                    long key = getChangedHolderKey(holder);
                    ItemAnimator.ItemHolderInfo animationInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, holder);
                    ViewHolder oldChangeViewHolder = this.mViewInfoStore.getFromOldChangeHolders(key);
                    if (oldChangeViewHolder != null && !oldChangeViewHolder.shouldIgnore()) {
                        boolean oldDisappearing = this.mViewInfoStore.isDisappearing(oldChangeViewHolder);
                        boolean newDisappearing = this.mViewInfoStore.isDisappearing(holder);
                        if (oldDisappearing && oldChangeViewHolder == holder) {
                            this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                        } else {
                            ItemAnimator.ItemHolderInfo preInfo = this.mViewInfoStore.popFromPreLayout(oldChangeViewHolder);
                            this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                            ItemAnimator.ItemHolderInfo postInfo = this.mViewInfoStore.popFromPostLayout(holder);
                            if (preInfo == null) {
                                handleMissingPreInfoForChangeError(key, holder, oldChangeViewHolder);
                            } else {
                                animateChange(oldChangeViewHolder, holder, preInfo, postInfo, oldDisappearing, newDisappearing);
                            }
                        }
                    } else {
                        this.mViewInfoStore.addToPostLayout(holder, animationInfo);
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        onExitLayoutOrScroll();
        resumeRequestLayout(false);
        this.mViewInfoStore.clear();
        if (didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1])) {
            dispatchOnScrolled(0, 0);
        }
    }

    private void handleMissingPreInfoForChangeError(long key, ViewHolder holder, ViewHolder oldChangeViewHolder) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.mChildHelper.getChildAt(i);
            ViewHolder other = getChildViewHolderInt(view);
            if (other != holder) {
                long otherKey = getChangedHolderKey(other);
                if (otherKey == key) {
                    if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
                        throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + other + " \n View Holder 2:" + holder);
                    }
                    throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + other + " \n View Holder 2:" + holder);
                }
            }
        }
        Log.e("RecyclerView", "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + oldChangeViewHolder + " cannot be found but it is necessary for " + holder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo animationInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            long key = getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(key, viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, animationInfo);
    }

    private void findMinMaxChildLayoutPositions(int[] into) {
        int count = this.mChildHelper.getChildCount();
        if (count == 0) {
            into[0] = 0;
            into[1] = 0;
            return;
        }
        int minPositionPreLayout = Integer.MAX_VALUE;
        int maxPositionPreLayout = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (!holder.shouldIgnore()) {
                int pos = holder.getLayoutPosition();
                if (pos < minPositionPreLayout) {
                    minPositionPreLayout = pos;
                }
                if (pos > maxPositionPreLayout) {
                    maxPositionPreLayout = pos;
                }
            }
        }
        into[0] = minPositionPreLayout;
        into[1] = maxPositionPreLayout;
    }

    private boolean didChildRangeChange(int minPositionPreLayout, int maxPositionPreLayout) {
        int pos;
        int count = this.mChildHelper.getChildCount();
        if (count == 0) {
            return (minPositionPreLayout == 0 && maxPositionPreLayout == 0) ? false : true;
        }
        for (int i = 0; i < count; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (!holder.shouldIgnore() && ((pos = holder.getLayoutPosition()) < minPositionPreLayout || pos > maxPositionPreLayout)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    protected void removeDetachedView(View child, boolean animate) {
        ViewHolder vh = getChildViewHolderInt(child);
        if (vh != null) {
            if (vh.isTmpDetached()) {
                vh.clearTmpDetachFlag();
            } else if (!vh.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + vh);
            }
        }
        dispatchChildDetached(child);
        super.removeDetachedView(child, animate);
    }

    long getChangedHolderKey(ViewHolder holder) {
        return this.mAdapter.hasStableIds() ? holder.getItemId() : holder.mPosition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateAppearance(ViewHolder itemHolder, ItemAnimator.ItemHolderInfo preLayoutInfo, ItemAnimator.ItemHolderInfo postLayoutInfo) {
        itemHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(itemHolder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateDisappearance(ViewHolder holder, ItemAnimator.ItemHolderInfo preLayoutInfo, ItemAnimator.ItemHolderInfo postLayoutInfo) {
        addAnimatingView(holder);
        holder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(holder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    private void animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo postInfo, boolean oldHolderDisappearing, boolean newHolderDisappearing) {
        oldHolder.setIsRecyclable(false);
        if (oldHolderDisappearing) {
            addAnimatingView(oldHolder);
        }
        if (oldHolder != newHolder) {
            if (newHolderDisappearing) {
                addAnimatingView(newHolder);
            }
            oldHolder.mShadowedHolder = newHolder;
            addAnimatingView(oldHolder);
            this.mRecycler.unscrapView(oldHolder);
            newHolder.setIsRecyclable(false);
            newHolder.mShadowingHolder = oldHolder;
        }
        if (this.mItemAnimator.animateChange(oldHolder, newHolder, preInfo, postInfo)) {
            postAnimationRunner();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        TraceCompat.beginSection("RV OnLayout");
        dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mEatRequestLayout == 0 && !this.mLayoutFrozen) {
            super.requestLayout();
        } else {
            this.mLayoutRequestEaten = true;
        }
    }

    void markItemDecorInsetsDirty() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.mChildHelper.getUnfilteredChildAt(i);
            ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        super.draw(c);
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            this.mItemDecorations.get(i).onDrawOver(c, this, this.mState);
        }
        boolean needsInvalidate = false;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished()) {
            int restore = c.save();
            int padding = this.mClipToPadding ? getPaddingBottom() : 0;
            c.rotate(270.0f);
            c.translate((-getHeight()) + padding, 0.0f);
            needsInvalidate = this.mLeftGlow != null && this.mLeftGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (this.mTopGlow != null && !this.mTopGlow.isFinished()) {
            int restore2 = c.save();
            if (this.mClipToPadding) {
                c.translate(getPaddingLeft(), getPaddingTop());
            }
            needsInvalidate |= this.mTopGlow != null && this.mTopGlow.draw(c);
            c.restoreToCount(restore2);
        }
        if (this.mRightGlow != null && !this.mRightGlow.isFinished()) {
            int restore3 = c.save();
            int width = getWidth();
            int padding2 = this.mClipToPadding ? getPaddingTop() : 0;
            c.rotate(90.0f);
            c.translate(-padding2, -width);
            needsInvalidate |= this.mRightGlow != null && this.mRightGlow.draw(c);
            c.restoreToCount(restore3);
        }
        if (this.mBottomGlow != null && !this.mBottomGlow.isFinished()) {
            int restore4 = c.save();
            c.rotate(180.0f);
            if (this.mClipToPadding) {
                c.translate((-getWidth()) + getPaddingRight(), (-getHeight()) + getPaddingBottom());
            } else {
                c.translate(-getWidth(), -getHeight());
            }
            needsInvalidate |= this.mBottomGlow != null && this.mBottomGlow.draw(c);
            c.restoreToCount(restore4);
        }
        if (!needsInvalidate && this.mItemAnimator != null && this.mItemDecorations.size() > 0 && this.mItemAnimator.isRunning()) {
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas c) {
        super.onDraw(c);
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            this.mItemDecorations.get(i).onDraw(c, this, this.mState);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && this.mLayout.checkLayoutParams((LayoutParams) p);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateDefaultLayoutParams();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(p);
    }

    void saveOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.saveOldPosition();
            }
        }
    }

    void clearOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }

    void offsetPositionRecordsForMove(int from, int to) {
        int start;
        int end;
        int inBetweenOffset;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        if (from < to) {
            start = from;
            end = to;
            inBetweenOffset = -1;
        } else {
            start = to;
            end = from;
            inBetweenOffset = 1;
        }
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                if (holder.mPosition == from) {
                    holder.offsetPosition(to - from, false);
                } else {
                    holder.offsetPosition(inBetweenOffset, false);
                }
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(from, to);
        requestLayout();
    }

    void offsetPositionRecordsForInsert(int positionStart, int itemCount) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart) {
                holder.offsetPosition(itemCount, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(positionStart, itemCount);
        requestLayout();
    }

    void offsetPositionRecordsForRemove(int positionStart, int itemCount, boolean applyToPreLayout) {
        int positionEnd = positionStart + itemCount;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                if (holder.mPosition >= positionEnd) {
                    holder.offsetPosition(-itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                } else if (holder.mPosition >= positionStart) {
                    holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(positionStart, itemCount, applyToPreLayout);
        requestLayout();
    }

    void viewRangeUpdate(int positionStart, int itemCount, Object payload) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        int positionEnd = positionStart + itemCount;
        for (int i = 0; i < childCount; i++) {
            View child = this.mChildHelper.getUnfilteredChildAt(i);
            ViewHolder holder = getChildViewHolderInt(child);
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart && holder.mPosition < positionEnd) {
                holder.addFlags(2);
                holder.addChangePayload(payload);
                ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
            }
        }
        this.mRecycler.viewRangeUpdate(positionStart, itemCount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        return this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDataSetChangedAfterLayout() {
        if (!this.mDataSetHasChangedAfterLayout) {
            this.mDataSetHasChangedAfterLayout = true;
            int childCount = this.mChildHelper.getUnfilteredChildCount();
            for (int i = 0; i < childCount; i++) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                if (holder != null && !holder.shouldIgnore()) {
                    holder.addFlags(512);
                }
            }
            this.mRecycler.setAdapterPositionsAsUnknown();
        }
    }

    void markKnownViewsInvalid() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                holder.addFlags(6);
            }
        }
        markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public ViewHolder getChildViewHolder(View child) {
        ViewParent parent = child.getParent();
        if (parent != null && parent != this) {
            throw new IllegalArgumentException("View " + child + " is not a direct child of " + this);
        }
        return getChildViewHolderInt(child);
    }

    public View findContainingItemView(View view) {
        ViewParent parent = view.getParent();
        while (parent != null && parent != this && (parent instanceof View)) {
            view = parent;
            parent = view.getParent();
        }
        if (parent == this) {
            return view;
        }
        return null;
    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;
    }

    public int getChildAdapterPosition(View child) {
        ViewHolder holder = getChildViewHolderInt(child);
        if (holder != null) {
            return holder.getAdapterPosition();
        }
        return -1;
    }

    public int getChildLayoutPosition(View child) {
        ViewHolder holder = getChildViewHolderInt(child);
        if (holder != null) {
            return holder.getLayoutPosition();
        }
        return -1;
    }

    ViewHolder findViewHolderForPosition(int position, boolean checkNewPosition) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved()) {
                if (checkNewPosition) {
                    if (holder.mPosition == position) {
                        return holder;
                    }
                } else if (holder.getLayoutPosition() == position) {
                    return holder;
                }
            }
        }
        return null;
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    public void offsetChildrenVertical(int dy) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    public void onChildAttachedToWindow(View child) {
    }

    public void onChildDetachedFromWindow(View child) {
    }

    public void offsetChildrenHorizontal(int dx) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(dx);
        }
    }

    Rect getItemDecorInsetsForChild(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (!lp.mInsetsDirty) {
            return lp.mDecorInsets;
        }
        Rect insets = lp.mDecorInsets;
        insets.set(0, 0, 0, 0);
        int decorCount = this.mItemDecorations.size();
        for (int i = 0; i < decorCount; i++) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, child, this, this.mState);
            insets.left += this.mTempRect.left;
            insets.top += this.mTempRect.top;
            insets.right += this.mTempRect.right;
            insets.bottom += this.mTempRect.bottom;
        }
        lp.mInsetsDirty = false;
        return insets;
    }

    public void onScrolled(int dx, int dy) {
    }

    void dispatchOnScrolled(int hresult, int vresult) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX, scrollY);
        onScrolled(hresult, vresult);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrolled(this, hresult, vresult);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; i--) {
                this.mScrollListeners.get(i).onScrolled(this, hresult, vresult);
            }
        }
    }

    public void onScrollStateChanged(int state) {
    }

    void dispatchOnScrollStateChanged(int state) {
        if (this.mLayout != null) {
            this.mLayout.onScrollStateChanged(state);
        }
        onScrollStateChanged(state);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(this, state);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; i--) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, state);
            }
        }
    }

    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }

    private class ViewFlinger implements Runnable {
        private int mLastFlingX;
        private int mLastFlingY;
        private ScrollerCompat mScroller;
        private Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger() {
            this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (RecyclerView.this.mLayout == null) {
                stop();
                return;
            }
            disableRunOnAnimationRequests();
            RecyclerView.this.consumePendingUpdateOperations();
            ScrollerCompat scroller = this.mScroller;
            SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
            if (scroller.computeScrollOffset()) {
                int x = scroller.getCurrX();
                int y = scroller.getCurrY();
                int dx = x - this.mLastFlingX;
                int dy = y - this.mLastFlingY;
                int hresult = 0;
                int vresult = 0;
                this.mLastFlingX = x;
                this.mLastFlingY = y;
                int overscrollX = 0;
                int overscrollY = 0;
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.eatRequestLayout();
                    RecyclerView.this.onEnterLayoutOrScroll();
                    TraceCompat.beginSection("RV Scroll");
                    if (dx != 0) {
                        hresult = RecyclerView.this.mLayout.scrollHorizontallyBy(dx, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollX = dx - hresult;
                    }
                    if (dy != 0) {
                        vresult = RecyclerView.this.mLayout.scrollVerticallyBy(dy, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollY = dy - vresult;
                    }
                    TraceCompat.endSection();
                    RecyclerView.this.repositionShadowingViews();
                    RecyclerView.this.onExitLayoutOrScroll();
                    RecyclerView.this.resumeRequestLayout(false);
                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        int adapterSize = RecyclerView.this.mState.getItemCount();
                        if (adapterSize == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= adapterSize) {
                            smoothScroller.setTargetPosition(adapterSize - 1);
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        } else {
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        }
                    }
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(dx, dy);
                }
                if (overscrollX != 0 || overscrollY != 0) {
                    int vel = (int) scroller.getCurrVelocity();
                    int velX = 0;
                    if (overscrollX != x) {
                        if (overscrollX < 0) {
                            velX = -vel;
                        } else {
                            velX = overscrollX > 0 ? vel : 0;
                        }
                    }
                    int velY = 0;
                    if (overscrollY != y) {
                        if (overscrollY < 0) {
                            velY = -vel;
                        } else {
                            velY = overscrollY > 0 ? vel : 0;
                        }
                    }
                    if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2) {
                        RecyclerView.this.absorbGlows(velX, velY);
                    }
                    if ((velX != 0 || overscrollX == x || scroller.getFinalX() == 0) && (velY != 0 || overscrollY == y || scroller.getFinalY() == 0)) {
                        scroller.abortAnimation();
                    }
                }
                if (hresult != 0 || vresult != 0) {
                    RecyclerView.this.dispatchOnScrolled(hresult, vresult);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                boolean fullyConsumedVertical = dy != 0 && RecyclerView.this.mLayout.canScrollVertically() && vresult == dy;
                boolean fullyConsumedHorizontal = dx != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && hresult == dx;
                boolean fullyConsumedAny = (dx == 0 && dy == 0) || fullyConsumedHorizontal || fullyConsumedVertical;
                if (scroller.isFinished() || !fullyConsumedAny) {
                    RecyclerView.this.setScrollState(0);
                } else {
                    postOnAnimation();
                }
            }
            if (smoothScroller != null) {
                if (smoothScroller.isPendingInitialRun()) {
                    smoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    smoothScroller.stop();
                }
            }
            enableRunOnAnimationRequests();
        }

        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            } else {
                RecyclerView.this.removeCallbacks(this);
                ViewCompat.postOnAnimation(RecyclerView.this, this);
            }
        }

        public void fling(int velocityX, int velocityY) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void smoothScrollBy(int dx, int dy) {
            smoothScrollBy(dx, dy, 0, 0);
        }

        public void smoothScrollBy(int dx, int dy, int vx, int vy) {
            smoothScrollBy(dx, dy, computeScrollDuration(dx, dy, vx, vy));
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((float) ((f - 0.5f) * 0.4712389167638204d));
        }

        private int computeScrollDuration(int dx, int dy, int vx, int vy) {
            int duration;
            int absDx = Math.abs(dx);
            int absDy = Math.abs(dy);
            boolean horizontal = absDx > absDy;
            int velocity = (int) Math.sqrt((vx * vx) + (vy * vy));
            int delta = (int) Math.sqrt((dx * dx) + (dy * dy));
            int containerSize = horizontal ? RecyclerView.this.getWidth() : RecyclerView.this.getHeight();
            int halfContainerSize = containerSize / 2;
            float distanceRatio = Math.min(1.0f, (1.0f * delta) / containerSize);
            float distance = halfContainerSize + (halfContainerSize * distanceInfluenceForSnapDuration(distanceRatio));
            if (velocity > 0) {
                duration = Math.round(1000.0f * Math.abs(distance / velocity)) * 4;
            } else {
                if (!horizontal) {
                    absDx = absDy;
                }
                float absDelta = absDx;
                duration = (int) (((absDelta / containerSize) + 1.0f) * 300.0f);
            }
            return Math.min(duration, 2000);
        }

        public void smoothScrollBy(int dx, int dy, int duration) {
            smoothScrollBy(dx, dy, duration, RecyclerView.sQuinticInterpolator);
        }

        public void smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator) {
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), interpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, dx, dy, duration);
            postOnAnimation();
        }

        public void stop() {
            RecyclerView.this.removeCallbacks(this);
            this.mScroller.abortAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repositionShadowingViews() {
        int count = this.mChildHelper.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.mChildHelper.getChildAt(i);
            ViewHolder holder = getChildViewHolder(view);
            if (holder != null && holder.mShadowingHolder != null) {
                View shadowingView = holder.mShadowingHolder.itemView;
                int left = view.getLeft();
                int top = view.getTop();
                if (left != shadowingView.getLeft() || top != shadowingView.getTop()) {
                    shadowingView.layout(left, top, shadowingView.getWidth() + left, shadowingView.getHeight() + top);
                }
            }
        }
    }

    private class RecyclerViewDataObserver extends AdapterDataObserver {
        private RecyclerViewDataObserver() {
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapter.hasStableIds()) {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            } else {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            }
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }
    }

    public static class RecycledViewPool {
        private SparseArray<ArrayList<ViewHolder>> mScrap = new SparseArray<>();
        private SparseIntArray mMaxScrap = new SparseIntArray();
        private int mAttachCount = 0;

        public void clear() {
            this.mScrap.clear();
        }

        public ViewHolder getRecycledView(int viewType) {
            ArrayList<ViewHolder> scrapHeap = this.mScrap.get(viewType);
            if (scrapHeap == null || scrapHeap.isEmpty()) {
                return null;
            }
            int index = scrapHeap.size() - 1;
            ViewHolder scrap = scrapHeap.get(index);
            scrapHeap.remove(index);
            return scrap;
        }

        public void putRecycledView(ViewHolder scrap) {
            int viewType = scrap.getItemViewType();
            ArrayList scrapHeap = getScrapHeapForType(viewType);
            if (this.mMaxScrap.get(viewType) > scrapHeap.size()) {
                scrap.resetInternal();
                scrapHeap.add(scrap);
            }
        }

        void attach(Adapter adapter) {
            this.mAttachCount++;
        }

        void detach() {
            this.mAttachCount--;
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            if (oldAdapter != null) {
                detach();
            }
            if (!compatibleWithPrevious && this.mAttachCount == 0) {
                clear();
            }
            if (newAdapter != null) {
                attach(newAdapter);
            }
        }

        private ArrayList<ViewHolder> getScrapHeapForType(int viewType) {
            ArrayList<ViewHolder> scrap = this.mScrap.get(viewType);
            if (scrap == null) {
                scrap = new ArrayList<>();
                this.mScrap.put(viewType, scrap);
                if (this.mMaxScrap.indexOfKey(viewType) < 0) {
                    this.mMaxScrap.put(viewType, 5);
                }
            }
            return scrap;
        }
    }

    public final class Recycler {
        private RecycledViewPool mRecyclerPool;
        private ViewCacheExtension mViewCacheExtension;
        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
        private ArrayList<ViewHolder> mChangedScrap = null;
        final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();
        private final List<ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
        private int mViewCacheMax = 2;

        public Recycler() {
        }

        public void clear() {
            this.mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        public void setViewCacheSize(int viewCount) {
            this.mViewCacheMax = viewCount;
            for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > viewCount; i--) {
                recycleCachedViewAt(i);
            }
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder holder) {
            if (holder.isRemoved()) {
                return RecyclerView.this.mState.isPreLayout();
            }
            if (holder.mPosition < 0 || holder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + holder);
            }
            if (!RecyclerView.this.mState.isPreLayout()) {
                int type = RecyclerView.this.mAdapter.getItemViewType(holder.mPosition);
                if (type != holder.getItemViewType()) {
                    return false;
                }
            }
            return !RecyclerView.this.mAdapter.hasStableIds() || holder.getItemId() == RecyclerView.this.mAdapter.getItemId(holder.mPosition);
        }

        public int convertPreLayoutPositionToPostLayout(int position) {
            if (position < 0 || position >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("invalid position " + position + ". State item count is " + RecyclerView.this.mState.getItemCount());
            }
            return !RecyclerView.this.mState.isPreLayout() ? position : RecyclerView.this.mAdapterHelper.findPositionOffset(position);
        }

        public View getViewForPosition(int position) {
            return getViewForPosition(position, false);
        }

        View getViewForPosition(int position, boolean dryRun) {
            LayoutParams rvLayoutParams;
            View view;
            if (position < 0 || position >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + position + "(" + position + "). Item count:" + RecyclerView.this.mState.getItemCount());
            }
            boolean fromScrap = false;
            ViewHolder holder = null;
            if (RecyclerView.this.mState.isPreLayout()) {
                holder = getChangedScrapViewForPosition(position);
                fromScrap = holder != null;
            }
            if (holder == null && (holder = getScrapViewForPosition(position, -1, dryRun)) != null) {
                if (!validateViewHolderForOffsetPosition(holder)) {
                    if (!dryRun) {
                        holder.addFlags(4);
                        if (holder.isScrap()) {
                            RecyclerView.this.removeDetachedView(holder.itemView, false);
                            holder.unScrap();
                        } else if (holder.wasReturnedFromScrap()) {
                            holder.clearReturnedFromScrapFlag();
                        }
                        recycleViewHolderInternal(holder);
                    }
                    holder = null;
                } else {
                    fromScrap = true;
                }
            }
            if (holder == null) {
                int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                if (offsetPosition >= 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                    int type = RecyclerView.this.mAdapter.getItemViewType(offsetPosition);
                    if (RecyclerView.this.mAdapter.hasStableIds() && (holder = getScrapViewForId(RecyclerView.this.mAdapter.getItemId(offsetPosition), type, dryRun)) != null) {
                        holder.mPosition = offsetPosition;
                        fromScrap = true;
                    }
                    if (holder == null && this.mViewCacheExtension != null && (view = this.mViewCacheExtension.getViewForPositionAndType(this, position, type)) != null) {
                        holder = RecyclerView.this.getChildViewHolder(view);
                        if (holder == null) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
                        }
                        if (holder.shouldIgnore()) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                        }
                    }
                    if (holder == null && (holder = getRecycledViewPool().getRecycledView(type)) != null) {
                        holder.resetInternal();
                        if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                            invalidateDisplayListInt(holder);
                        }
                    }
                    if (holder == null) {
                        holder = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, type);
                    }
                } else {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + offsetPosition + ").state:" + RecyclerView.this.mState.getItemCount());
                }
            }
            if (fromScrap && !RecyclerView.this.mState.isPreLayout() && holder.hasAnyOfTheFlags(8192)) {
                holder.setFlags(0, 8192);
                if (RecyclerView.this.mState.mRunSimpleAnimations) {
                    int changeFlags = ItemAnimator.buildAdapterChangeFlagsForAnimations(holder);
                    ItemAnimator.ItemHolderInfo info = RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, holder, changeFlags | 4096, holder.getUnmodifiedPayloads());
                    RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(holder, info);
                }
            }
            boolean bound = false;
            if (RecyclerView.this.mState.isPreLayout() && holder.isBound()) {
                holder.mPreLayoutPosition = position;
            } else if (!holder.isBound() || holder.needsUpdate() || holder.isInvalid()) {
                int offsetPosition2 = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                holder.mOwnerRecyclerView = RecyclerView.this;
                RecyclerView.this.mAdapter.bindViewHolder(holder, offsetPosition2);
                attachAccessibilityDelegate(holder.itemView);
                bound = true;
                if (RecyclerView.this.mState.isPreLayout()) {
                    holder.mPreLayoutPosition = position;
                }
            }
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp == null) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else if (!RecyclerView.this.checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(lp);
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else {
                rvLayoutParams = (LayoutParams) lp;
            }
            rvLayoutParams.mViewHolder = holder;
            rvLayoutParams.mPendingInvalidate = fromScrap && bound;
            return holder.itemView;
        }

        private void attachAccessibilityDelegate(View itemView) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                if (ViewCompat.getImportantForAccessibility(itemView) == 0) {
                    ViewCompat.setImportantForAccessibility(itemView, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(itemView)) {
                    ViewCompat.setAccessibilityDelegate(itemView, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }

        private void invalidateDisplayListInt(ViewHolder holder) {
            if (holder.itemView instanceof ViewGroup) {
                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    invalidateDisplayListInt((ViewGroup) view, true);
                }
            }
            if (invalidateThis) {
                if (viewGroup.getVisibility() == 4) {
                    viewGroup.setVisibility(0);
                    viewGroup.setVisibility(4);
                } else {
                    int visibility = viewGroup.getVisibility();
                    viewGroup.setVisibility(4);
                    viewGroup.setVisibility(visibility);
                }
            }
        }

        public void recycleView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (holder.isScrap()) {
                holder.unScrap();
            } else if (holder.wasReturnedFromScrap()) {
                holder.clearReturnedFromScrapFlag();
            }
            recycleViewHolderInternal(holder);
        }

        void recycleAndClearCachedViews() {
            int count = this.mCachedViews.size();
            for (int i = count - 1; i >= 0; i--) {
                recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
        }

        void recycleCachedViewAt(int cachedViewIndex) {
            ViewHolder viewHolder = this.mCachedViews.get(cachedViewIndex);
            addViewHolderToRecycledViewPool(viewHolder);
            this.mCachedViews.remove(cachedViewIndex);
        }

        void recycleViewHolderInternal(ViewHolder holder) {
            if (holder.isScrap() || holder.itemView.getParent() != null) {
                throw new IllegalArgumentException("Scrapped or attached views may not be recycled. isScrap:" + holder.isScrap() + " isAttached:" + (holder.itemView.getParent() != null));
            }
            if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + holder);
            }
            if (holder.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
            }
            boolean transientStatePreventsRecycling = holder.doesTransientStatePreventRecycling();
            boolean forceRecycle = RecyclerView.this.mAdapter != null && transientStatePreventsRecycling && RecyclerView.this.mAdapter.onFailedToRecycleView(holder);
            boolean cached = false;
            boolean recycled = false;
            if (forceRecycle || holder.isRecyclable()) {
                if (!holder.hasAnyOfTheFlags(14)) {
                    int cachedViewSize = this.mCachedViews.size();
                    if (cachedViewSize == this.mViewCacheMax && cachedViewSize > 0) {
                        recycleCachedViewAt(0);
                    }
                    if (cachedViewSize < this.mViewCacheMax) {
                        this.mCachedViews.add(holder);
                        cached = true;
                    }
                }
                if (!cached) {
                    addViewHolderToRecycledViewPool(holder);
                    recycled = true;
                }
            }
            RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
            if (!cached && !recycled && transientStatePreventsRecycling) {
                holder.mOwnerRecyclerView = null;
            }
        }

        void addViewHolderToRecycledViewPool(ViewHolder holder) {
            ViewCompat.setAccessibilityDelegate(holder.itemView, null);
            dispatchViewRecycled(holder);
            holder.mOwnerRecyclerView = null;
            getRecycledViewPool().putRecycledView(holder);
        }

        void quickRecycleScrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(holder);
        }

        void scrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder.hasAnyOfTheFlags(12) || !holder.isUpdated() || RecyclerView.this.canReuseUpdatedViewHolder(holder)) {
                if (holder.isInvalid() && !holder.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                    throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                }
                holder.setScrapContainer(this, false);
                this.mAttachedScrap.add(holder);
                return;
            }
            if (this.mChangedScrap == null) {
                this.mChangedScrap = new ArrayList<>();
            }
            holder.setScrapContainer(this, true);
            this.mChangedScrap.add(holder);
        }

        void unscrapView(ViewHolder holder) {
            if (holder.mInChangeScrap) {
                this.mChangedScrap.remove(holder);
            } else {
                this.mAttachedScrap.remove(holder);
            }
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        View getScrapViewAt(int index) {
            return this.mAttachedScrap.get(index).itemView;
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap != null) {
                this.mChangedScrap.clear();
            }
        }

        ViewHolder getChangedScrapViewForPosition(int position) {
            int changedScrapSize;
            int offsetPosition;
            if (this.mChangedScrap == null || (changedScrapSize = this.mChangedScrap.size()) == 0) {
                return null;
            }
            for (int i = 0; i < changedScrapSize; i++) {
                ViewHolder holder = this.mChangedScrap.get(i);
                if (!holder.wasReturnedFromScrap() && holder.getLayoutPosition() == position) {
                    holder.addFlags(32);
                    return holder;
                }
            }
            if (RecyclerView.this.mAdapter.hasStableIds() && (offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position)) > 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                long id = RecyclerView.this.mAdapter.getItemId(offsetPosition);
                for (int i2 = 0; i2 < changedScrapSize; i2++) {
                    ViewHolder holder2 = this.mChangedScrap.get(i2);
                    if (!holder2.wasReturnedFromScrap() && holder2.getItemId() == id) {
                        holder2.addFlags(32);
                        return holder2;
                    }
                }
            }
            return null;
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x00d6  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        android.support.v7.widget.RecyclerView.ViewHolder getScrapViewForPosition(int r12, int r13, boolean r14) {
            /*
                r11 = this;
                r10 = -1
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r7 = r11.mAttachedScrap
                int r4 = r7.size()
                r2 = 0
            L8:
                if (r2 >= r4) goto L78
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r7 = r11.mAttachedScrap
                java.lang.Object r1 = r7.get(r2)
                android.support.v7.widget.RecyclerView$ViewHolder r1 = (android.support.v7.widget.RecyclerView.ViewHolder) r1
                boolean r7 = r1.wasReturnedFromScrap()
                if (r7 != 0) goto Lb8
                int r7 = r1.getLayoutPosition()
                if (r7 != r12) goto Lb8
                boolean r7 = r1.isInvalid()
                if (r7 != 0) goto Lb8
                android.support.v7.widget.RecyclerView r7 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.RecyclerView$State r7 = r7.mState
                boolean r7 = android.support.v7.widget.RecyclerView.State.access$2400(r7)
                if (r7 != 0) goto L34
                boolean r7 = r1.isRemoved()
                if (r7 != 0) goto Lb8
            L34:
                if (r13 == r10) goto Lb2
                int r7 = r1.getItemViewType()
                if (r7 == r13) goto Lb2
                java.lang.String r7 = "RecyclerView"
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                r8.<init>()
                java.lang.String r9 = "Scrap view for position "
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.StringBuilder r8 = r8.append(r12)
                java.lang.String r9 = " isn't dirty but has"
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.String r9 = " wrong view type! (found "
                java.lang.StringBuilder r8 = r8.append(r9)
                int r9 = r1.getItemViewType()
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.String r9 = " but expected "
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.StringBuilder r8 = r8.append(r13)
                java.lang.String r9 = ")"
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.String r8 = r8.toString()
                android.util.Log.e(r7, r8)
            L78:
                if (r14 != 0) goto Lcd
                android.support.v7.widget.RecyclerView r7 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.ChildHelper r7 = r7.mChildHelper
                android.view.View r6 = r7.findHiddenNonRemovedView(r12, r13)
                if (r6 == 0) goto Lcd
                android.support.v7.widget.RecyclerView$ViewHolder r5 = android.support.v7.widget.RecyclerView.getChildViewHolderInt(r6)
                android.support.v7.widget.RecyclerView r7 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.ChildHelper r7 = r7.mChildHelper
                r7.unhide(r6)
                android.support.v7.widget.RecyclerView r7 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.ChildHelper r7 = r7.mChildHelper
                int r3 = r7.indexOfChild(r6)
                if (r3 != r10) goto Lbc
                java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                r8.<init>()
                java.lang.String r9 = "layout index should not be -1 after unhiding a view:"
                java.lang.StringBuilder r8 = r8.append(r9)
                java.lang.StringBuilder r8 = r8.append(r5)
                java.lang.String r8 = r8.toString()
                r7.<init>(r8)
                throw r7
            Lb2:
                r7 = 32
                r1.addFlags(r7)
            Lb7:
                return r1
            Lb8:
                int r2 = r2 + 1
                goto L8
            Lbc:
                android.support.v7.widget.RecyclerView r7 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.ChildHelper r7 = r7.mChildHelper
                r7.detachViewFromParent(r3)
                r11.scrapView(r6)
                r7 = 8224(0x2020, float:1.1524E-41)
                r5.addFlags(r7)
                r1 = r5
                goto Lb7
            Lcd:
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r7 = r11.mCachedViews
                int r0 = r7.size()
                r2 = 0
            Ld4:
                if (r2 >= r0) goto Lf5
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r7 = r11.mCachedViews
                java.lang.Object r1 = r7.get(r2)
                android.support.v7.widget.RecyclerView$ViewHolder r1 = (android.support.v7.widget.RecyclerView.ViewHolder) r1
                boolean r7 = r1.isInvalid()
                if (r7 != 0) goto Lf2
                int r7 = r1.getLayoutPosition()
                if (r7 != r12) goto Lf2
                if (r14 != 0) goto Lb7
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r7 = r11.mCachedViews
                r7.remove(r2)
                goto Lb7
            Lf2:
                int r2 = r2 + 1
                goto Ld4
            Lf5:
                r1 = 0
                goto Lb7
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.RecyclerView.Recycler.getScrapViewForPosition(int, int, boolean):android.support.v7.widget.RecyclerView$ViewHolder");
        }

        ViewHolder getScrapViewForId(long id, int type, boolean dryRun) {
            int count = this.mAttachedScrap.size();
            for (int i = count - 1; i >= 0; i--) {
                ViewHolder holder = this.mAttachedScrap.get(i);
                if (holder.getItemId() == id && !holder.wasReturnedFromScrap()) {
                    if (type == holder.getItemViewType()) {
                        holder.addFlags(32);
                        if (holder.isRemoved() && !RecyclerView.this.mState.isPreLayout()) {
                            holder.setFlags(2, 14);
                            return holder;
                        }
                        return holder;
                    }
                    if (!dryRun) {
                        this.mAttachedScrap.remove(i);
                        RecyclerView.this.removeDetachedView(holder.itemView, false);
                        quickRecycleScrapView(holder.itemView);
                    }
                }
            }
            int cacheSize = this.mCachedViews.size();
            for (int i2 = cacheSize - 1; i2 >= 0; i2--) {
                ViewHolder holder2 = this.mCachedViews.get(i2);
                if (holder2.getItemId() == id) {
                    if (type == holder2.getItemViewType()) {
                        if (!dryRun) {
                            this.mCachedViews.remove(i2);
                            return holder2;
                        }
                        return holder2;
                    }
                    if (!dryRun) {
                        recycleCachedViewAt(i2);
                    }
                }
            }
            return null;
        }

        void dispatchViewRecycled(ViewHolder holder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(holder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(holder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
            }
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            clear();
            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
        }

        void offsetPositionRecordsForMove(int from, int to) {
            int start;
            int end;
            int inBetweenOffset;
            if (from < to) {
                start = from;
                end = to;
                inBetweenOffset = -1;
            } else {
                start = to;
                end = from;
                inBetweenOffset = 1;
            }
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                    if (holder.mPosition == from) {
                        holder.offsetPosition(to - from, false);
                    } else {
                        holder.offsetPosition(inBetweenOffset, false);
                    }
                }
            }
        }

        void offsetPositionRecordsForInsert(int insertedAt, int count) {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && holder.mPosition >= insertedAt) {
                    holder.offsetPosition(count, true);
                }
            }
        }

        void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
            int removedEnd = removedFrom + count;
            int cachedCount = this.mCachedViews.size();
            for (int i = cachedCount - 1; i >= 0; i--) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null) {
                    if (holder.mPosition >= removedEnd) {
                        holder.offsetPosition(-count, applyToPreLayout);
                    } else if (holder.mPosition >= removedFrom) {
                        holder.addFlags(8);
                        recycleCachedViewAt(i);
                    }
                }
            }
        }

        void setViewCacheExtension(ViewCacheExtension extension) {
            this.mViewCacheExtension = extension;
        }

        void setRecycledViewPool(RecycledViewPool pool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            this.mRecyclerPool = pool;
            if (pool != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        void viewRangeUpdate(int positionStart, int itemCount) {
            int pos;
            int positionEnd = positionStart + itemCount;
            int cachedCount = this.mCachedViews.size();
            for (int i = cachedCount - 1; i >= 0; i--) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && (pos = holder.getLayoutPosition()) >= positionStart && pos < positionEnd) {
                    holder.addFlags(2);
                    recycleCachedViewAt(i);
                }
            }
        }

        void setAdapterPositionsAsUnknown() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null) {
                    holder.addFlags(512);
                }
            }
        }

        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                int cachedCount = this.mCachedViews.size();
                for (int i = 0; i < cachedCount; i++) {
                    ViewHolder holder = this.mCachedViews.get(i);
                    if (holder != null) {
                        holder.addFlags(6);
                        holder.addChangePayload(null);
                    }
                }
                return;
            }
            recycleAndClearCachedViews();
        }

        void clearOldPositions() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                holder.clearOldPosition();
            }
            int scrapCount = this.mAttachedScrap.size();
            for (int i2 = 0; i2 < scrapCount; i2++) {
                this.mAttachedScrap.get(i2).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                int changedScrapCount = this.mChangedScrap.size();
                for (int i3 = 0; i3 < changedScrapCount; i3++) {
                    this.mChangedScrap.get(i3).clearOldPosition();
                }
            }
        }

        void markItemDecorInsetsDirty() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                LayoutParams layoutParams = (LayoutParams) holder.itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();
        private boolean mHasStableIds = false;

        public abstract int getItemCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
            onBindViewHolder(holder, position);
        }

        public final VH createViewHolder(ViewGroup viewGroup, int i) {
            TraceCompat.beginSection("RV CreateView");
            VH vh = (VH) onCreateViewHolder(viewGroup, i);
            vh.mItemViewType = i;
            TraceCompat.endSection();
            return vh;
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.mPosition = position;
            if (hasStableIds()) {
                holder.mItemId = getItemId(position);
            }
            holder.setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            onBindViewHolder(holder, position, holder.getUnmodifiedPayloads());
            holder.clearPayload();
            TraceCompat.endSection();
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public long getItemId(int position) {
            return -1L;
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public void onViewRecycled(VH holder) {
        }

        public boolean onFailedToRecycleView(VH holder) {
            return false;
        }

        public void onViewAttachedToWindow(VH holder) {
        }

        public void onViewDetachedFromWindow(VH holder) {
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.unregisterObserver(observer);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchChildDetached(View child) {
        ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildDetachedFromWindow(child);
        if (this.mAdapter != null && viewHolder != null) {
            this.mAdapter.onViewDetachedFromWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            int cnt = this.mOnChildAttachStateListeners.size();
            for (int i = cnt - 1; i >= 0; i--) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(child);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchChildAttached(View child) {
        ViewHolder viewHolder = getChildViewHolderInt(child);
        onChildAttachedToWindow(child);
        if (this.mAdapter != null && viewHolder != null) {
            this.mAdapter.onViewAttachedToWindow(viewHolder);
        }
        if (this.mOnChildAttachStateListeners != null) {
            int cnt = this.mOnChildAttachStateListeners.size();
            for (int i = cnt - 1; i >= 0; i--) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(child);
            }
        }
    }

    public static abstract class LayoutManager {
        ChildHelper mChildHelper;
        private int mHeightSpec;
        RecyclerView mRecyclerView;
        SmoothScroller mSmoothScroller;
        private int mWidthSpec;
        private boolean mRequestedSimpleAnimations = false;
        boolean mIsAttachedToWindow = false;
        private boolean mAutoMeasure = false;
        private boolean mMeasurementCacheEnabled = true;

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 1073741824);
                this.mHeightSpec = View.MeasureSpec.makeMeasureSpec(0, 1073741824);
                return;
            }
            this.mRecyclerView = recyclerView;
            this.mChildHelper = recyclerView.mChildHelper;
            this.mWidthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824);
            this.mHeightSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824);
        }

        void setMeasureSpecs(int wSpec, int hSpec) {
            this.mWidthSpec = wSpec;
            this.mHeightSpec = hSpec;
        }

        void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
            int count = getChildCount();
            if (count == 0) {
                this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
                return;
            }
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int left = getDecoratedLeft(child) - lp.leftMargin;
                int right = getDecoratedRight(child) + lp.rightMargin;
                int top = getDecoratedTop(child) - lp.topMargin;
                int bottom = getDecoratedBottom(child) + lp.bottomMargin;
                if (left < minX) {
                    minX = left;
                }
                if (right > maxX) {
                    maxX = right;
                }
                if (top < minY) {
                    minY = top;
                }
                if (bottom > maxY) {
                    maxY = bottom;
                }
            }
            this.mRecyclerView.mTempRect.set(minX, minY, maxX, maxY);
            setMeasuredDimension(this.mRecyclerView.mTempRect, widthSpec, heightSpec);
        }

        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            int usedWidth = childrenBounds.width() + getPaddingLeft() + getPaddingRight();
            int usedHeight = childrenBounds.height() + getPaddingTop() + getPaddingBottom();
            int width = chooseSize(wSpec, usedWidth, getMinimumWidth());
            int height = chooseSize(hSpec, usedHeight, getMinimumHeight());
            setMeasuredDimension(width, height);
        }

        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }

        public static int chooseSize(int spec, int desired, int min) {
            int mode = View.MeasureSpec.getMode(spec);
            int size = View.MeasureSpec.getSize(spec);
            switch (mode) {
                case Integer.MIN_VALUE:
                    return Math.min(size, Math.max(desired, min));
                case 1073741824:
                    return size;
                default:
                    return Math.max(desired, min);
            }
        }

        public void assertNotInLayoutOrScroll(String message) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(message);
            }
        }

        public void setAutoMeasureEnabled(boolean enabled) {
            this.mAutoMeasure = enabled;
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        void dispatchAttachedToWindow(RecyclerView view) {
            this.mIsAttachedToWindow = true;
            onAttachedToWindow(view);
        }

        void dispatchDetachedFromWindow(RecyclerView view, Recycler recycler) {
            this.mIsAttachedToWindow = false;
            onDetachedFromWindow(view, recycler);
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public boolean removeCallbacks(Runnable action) {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.removeCallbacks(action);
            }
            return false;
        }

        public void onAttachedToWindow(RecyclerView view) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView view) {
        }

        public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
            onDetachedFromWindow(view);
        }

        public boolean getClipToPadding() {
            return this.mRecyclerView != null && this.mRecyclerView.mClipToPadding;
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public boolean checkLayoutParams(LayoutParams lp) {
            return lp != null;
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
            }
            return new LayoutParams(lp);
        }

        public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return new LayoutParams(c, attrs);
        }

        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            return 0;
        }

        public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
            return 0;
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public void scrollToPosition(int position) {
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller != null && smoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning()) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            this.mSmoothScroller.start(this.mRecyclerView, this);
        }

        public boolean isSmoothScrolling() {
            return this.mSmoothScroller != null && this.mSmoothScroller.isRunning();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(this.mRecyclerView);
        }

        public void addDisappearingView(View child) {
            addDisappearingView(child, -1);
        }

        public void addDisappearingView(View child, int index) {
            addViewInt(child, index, true);
        }

        public void addView(View child) {
            addView(child, -1);
        }

        public void addView(View child, int index) {
            addViewInt(child, index, false);
        }

        private void addViewInt(View child, int index, boolean disappearing) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(child);
            if (disappearing || holder.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(holder);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(holder);
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (holder.wasReturnedFromScrap() || holder.isScrap()) {
                if (holder.isScrap()) {
                    holder.unScrap();
                } else {
                    holder.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
            } else if (child.getParent() == this.mRecyclerView) {
                int currentIndex = this.mChildHelper.indexOfChild(child);
                if (index == -1) {
                    index = this.mChildHelper.getChildCount();
                }
                if (currentIndex == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(child));
                }
                if (currentIndex != index) {
                    this.mRecyclerView.mLayout.moveView(currentIndex, index);
                }
            } else {
                this.mChildHelper.addView(child, index, false);
                lp.mInsetsDirty = true;
                if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(child);
                }
            }
            if (lp.mPendingInvalidate) {
                holder.itemView.invalidate();
                lp.mPendingInvalidate = false;
            }
        }

        public void removeView(View child) {
            this.mChildHelper.removeView(child);
        }

        public void removeViewAt(int index) {
            View child = getChildAt(index);
            if (child != null) {
                this.mChildHelper.removeViewAt(index);
            }
        }

        public int getBaseline() {
            return -1;
        }

        public int getPosition(View view) {
            return ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        }

        public View findContainingItemView(View view) {
            View found;
            if (this.mRecyclerView == null || (found = this.mRecyclerView.findContainingItemView(view)) == null || this.mChildHelper.isHidden(found)) {
                return null;
            }
            return found;
        }

        public View findViewByPosition(int position) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh != null && vh.getLayoutPosition() == position && !vh.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !vh.isRemoved())) {
                    return child;
                }
            }
            return null;
        }

        public void detachViewAt(int index) {
            detachViewInternal(index, getChildAt(index));
        }

        private void detachViewInternal(int index, View view) {
            this.mChildHelper.detachViewFromParent(index);
        }

        public void attachView(View child, int index, LayoutParams lp) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(vh);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(vh);
            }
            this.mChildHelper.attachViewToParent(child, index, lp, vh.isRemoved());
        }

        public void attachView(View child, int index) {
            attachView(child, index, (LayoutParams) child.getLayoutParams());
        }

        public void moveView(int fromIndex, int toIndex) {
            View view = getChildAt(fromIndex);
            if (view == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + fromIndex);
            }
            detachViewAt(fromIndex);
            attachView(view, toIndex);
        }

        public void removeAndRecycleView(View child, Recycler recycler) {
            removeView(child);
            recycler.recycleView(child);
        }

        public void removeAndRecycleViewAt(int index, Recycler recycler) {
            View view = getChildAt(index);
            removeViewAt(index);
            recycler.recycleView(view);
        }

        public int getChildCount() {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildCount();
            }
            return 0;
        }

        public View getChildAt(int index) {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildAt(index);
            }
            return null;
        }

        public int getWidthMode() {
            return View.MeasureSpec.getMode(this.mWidthSpec);
        }

        public int getHeightMode() {
            return View.MeasureSpec.getMode(this.mHeightSpec);
        }

        public int getWidth() {
            return View.MeasureSpec.getSize(this.mWidthSpec);
        }

        public int getHeight() {
            return View.MeasureSpec.getSize(this.mHeightSpec);
        }

        public int getPaddingLeft() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingLeft();
            }
            return 0;
        }

        public int getPaddingTop() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingTop();
            }
            return 0;
        }

        public int getPaddingRight() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingRight();
            }
            return 0;
        }

        public int getPaddingBottom() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingBottom();
            }
            return 0;
        }

        public View getFocusedChild() {
            if (this.mRecyclerView == null) {
                return null;
            }
            View focused = this.mRecyclerView.getFocusedChild();
            if (focused == null || this.mChildHelper.isHidden(focused)) {
                return null;
            }
            return focused;
        }

        public void offsetChildrenHorizontal(int dx) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(dx);
            }
        }

        public void offsetChildrenVertical(int dy) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(dy);
            }
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            int childCount = getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View v = getChildAt(i);
                scrapOrRecycleView(recycler, i, v);
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int index, View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (!viewHolder.shouldIgnore()) {
                if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                    removeViewAt(index);
                    recycler.recycleViewHolderInternal(viewHolder);
                } else {
                    detachViewAt(index);
                    recycler.scrapView(view);
                    this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
                }
            }
        }

        void removeAndRecycleScrapInt(Recycler recycler) {
            int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; i--) {
                View scrap = recycler.getScrapViewAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(scrap);
                if (!vh.shouldIgnore()) {
                    vh.setIsRecyclable(false);
                    if (vh.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrap, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(vh);
                    }
                    vh.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrap);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return (this.mMeasurementCacheEnabled && isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width) && isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height)) ? false : true;
        }

        boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return (!child.isLayoutRequested() && this.mMeasurementCacheEnabled && isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width) && isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height)) ? false : true;
        }

        private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
            int specMode = View.MeasureSpec.getMode(spec);
            int specSize = View.MeasureSpec.getSize(spec);
            if (dimension > 0 && childSize != dimension) {
                return false;
            }
            switch (specMode) {
                case Integer.MIN_VALUE:
                    return specSize >= childSize;
                case 0:
                    return true;
                case 1073741824:
                    return specSize == childSize;
                default:
                    return false;
            }
        }

        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            int widthUsed2 = widthUsed + insets.left + insets.right;
            int heightUsed2 = heightUsed + insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed2, lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed2, lp.height, canScrollVertically());
            if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
                child.measure(widthSpec, heightSpec);
            }
        }

        public static int getChildMeasureSpec(int parentSize, int parentMode, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else if (childDimension == -1) {
                    switch (parentMode) {
                        case Integer.MIN_VALUE:
                        case 1073741824:
                            resultSize = size;
                            resultMode = parentMode;
                            break;
                        case 0:
                            resultSize = 0;
                            resultMode = 0;
                            break;
                    }
                } else if (childDimension == -2) {
                    resultSize = 0;
                    resultMode = 0;
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                resultSize = size;
                resultMode = parentMode;
            } else if (childDimension == -2) {
                resultSize = size;
                resultMode = (parentMode == Integer.MIN_VALUE || parentMode == 1073741824) ? Integer.MIN_VALUE : 0;
            }
            return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public int getDecoratedMeasuredWidth(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredWidth() + insets.left + insets.right;
        }

        public int getDecoratedMeasuredHeight(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredHeight() + insets.top + insets.bottom;
        }

        public void layoutDecorated(View child, int left, int top, int right, int bottom) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            child.layout(insets.left + left, insets.top + top, right - insets.right, bottom - insets.bottom);
        }

        public int getDecoratedLeft(View child) {
            return child.getLeft() - getLeftDecorationWidth(child);
        }

        public int getDecoratedTop(View child) {
            return child.getTop() - getTopDecorationHeight(child);
        }

        public int getDecoratedRight(View child) {
            return child.getRight() + getRightDecorationWidth(child);
        }

        public int getDecoratedBottom(View child) {
            return child.getBottom() + getBottomDecorationHeight(child);
        }

        public void calculateItemDecorationsForChild(View child, Rect outRect) {
            if (this.mRecyclerView == null) {
                outRect.set(0, 0, 0, 0);
            } else {
                Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
                outRect.set(insets);
            }
        }

        public int getTopDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.top;
        }

        public int getBottomDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
        }

        public int getLeftDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.left;
        }

        public int getRightDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.right;
        }

        public View onFocusSearchFailed(View focused, int direction, Recycler recycler, State state) {
            return null;
        }

        public View onInterceptFocusSearch(View focused, int direction) {
            return null;
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
            int dx;
            int parentLeft = getPaddingLeft();
            int parentTop = getPaddingTop();
            int parentRight = getWidth() - getPaddingRight();
            int parentBottom = getHeight() - getPaddingBottom();
            int childLeft = (child.getLeft() + rect.left) - child.getScrollX();
            int childTop = (child.getTop() + rect.top) - child.getScrollY();
            int childRight = childLeft + rect.width();
            int childBottom = childTop + rect.height();
            int offScreenLeft = Math.min(0, childLeft - parentLeft);
            int offScreenTop = Math.min(0, childTop - parentTop);
            int offScreenRight = Math.max(0, childRight - parentRight);
            int offScreenBottom = Math.max(0, childBottom - parentBottom);
            if (getLayoutDirection() == 1) {
                dx = offScreenRight != 0 ? offScreenRight : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft : Math.min(childLeft - parentLeft, offScreenRight);
            }
            int dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
            if (dx != 0 || dy != 0) {
                if (immediate) {
                    parent.scrollBy(dx, dy);
                } else {
                    parent.smoothScrollBy(dx, dy);
                }
                return true;
            }
            return false;
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView parent, View child, View focused) {
            return isSmoothScrolling() || parent.isComputingLayout();
        }

        public boolean onRequestChildFocus(RecyclerView parent, State state, View child, View focused) {
            return onRequestChildFocus(parent, child, focused);
        }

        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> views, int direction, int focusableMode) {
            return false;
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
            onItemsUpdated(recyclerView, positionStart, itemCount);
        }

        public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
        }

        public void setMeasuredDimension(int widthSize, int heightSize) {
            this.mRecyclerView.setMeasuredDimension(widthSize, heightSize);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(this.mRecyclerView);
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(this.mRecyclerView);
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }

        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }

        public void onScrollStateChanged(int state) {
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View view = getChildAt(i);
                if (!RecyclerView.getChildViewHolderInt(view).shouldIgnore()) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
        }

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, info);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat info) {
            if (ViewCompat.canScrollVertically(this.mRecyclerView, -1) || ViewCompat.canScrollHorizontally(this.mRecyclerView, -1)) {
                info.addAction(8192);
                info.setScrollable(true);
            }
            if (ViewCompat.canScrollVertically(this.mRecyclerView, 1) || ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                info.addAction(4096);
                info.setScrollable(true);
            }
            AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo = AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state));
            info.setCollectionInfo(collectionInfo);
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, event);
        }

        public void onInitializeAccessibilityEvent(Recycler recycler, State state, AccessibilityEvent event) {
            boolean z = true;
            AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
            if (this.mRecyclerView != null && record != null) {
                if (!ViewCompat.canScrollVertically(this.mRecyclerView, 1) && !ViewCompat.canScrollVertically(this.mRecyclerView, -1) && !ViewCompat.canScrollHorizontally(this.mRecyclerView, -1) && !ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                    z = false;
                }
                record.setScrollable(z);
                if (this.mRecyclerView.mAdapter != null) {
                    record.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }

        void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(host);
            if (vh != null && !vh.isRemoved() && !this.mChildHelper.isHidden(vh.itemView)) {
                onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, host, info);
            }
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View host, AccessibilityNodeInfoCompat info) {
            int rowIndexGuess = canScrollVertically() ? getPosition(host) : 0;
            int columnIndexGuess = canScrollHorizontally() ? getPosition(host) : 0;
            AccessibilityNodeInfoCompat.CollectionItemInfoCompat itemInfo = AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndexGuess, 1, columnIndexGuess, 1, false, false);
            info.setCollectionItemInfo(itemInfo);
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollVertically()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollHorizontally()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        boolean performAccessibilityAction(int action, Bundle args) {
            return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, action, args);
        }

        public boolean performAccessibilityAction(Recycler recycler, State state, int action, Bundle args) {
            if (this.mRecyclerView == null) {
                return false;
            }
            int vScroll = 0;
            int hScroll = 0;
            switch (action) {
                case 4096:
                    if (ViewCompat.canScrollVertically(this.mRecyclerView, 1)) {
                        vScroll = (getHeight() - getPaddingTop()) - getPaddingBottom();
                    }
                    if (ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                        hScroll = (getWidth() - getPaddingLeft()) - getPaddingRight();
                        break;
                    }
                    break;
                case 8192:
                    if (ViewCompat.canScrollVertically(this.mRecyclerView, -1)) {
                        vScroll = -((getHeight() - getPaddingTop()) - getPaddingBottom());
                    }
                    if (ViewCompat.canScrollHorizontally(this.mRecyclerView, -1)) {
                        hScroll = -((getWidth() - getPaddingLeft()) - getPaddingRight());
                        break;
                    }
                    break;
            }
            if (vScroll == 0 && hScroll == 0) {
                return false;
            }
            this.mRecyclerView.scrollBy(hScroll, vScroll);
            return true;
        }

        boolean performAccessibilityActionForItem(View view, int action, Bundle args) {
            return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, action, args);
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int action, Bundle args) {
            return false;
        }

        public static Properties getProperties(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            Properties properties = new Properties();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, defStyleRes);
            properties.orientation = a.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = a.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = a.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = a.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            a.recycle();
            return properties;
        }

        void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
            setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }

        boolean shouldMeasureTwice() {
            return false;
        }

        boolean hasFlexibleChildInBothOrientations() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                if (lp.width < 0 && lp.height < 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public static abstract class ItemDecoration {
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            onDraw(c, parent);
        }

        @Deprecated
        public void onDraw(Canvas c, RecyclerView parent) {
        }

        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            onDrawOver(c, parent);
        }

        @Deprecated
        public void onDrawOver(Canvas c, RecyclerView parent) {
        }

        @Deprecated
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), parent);
        }
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

    public static abstract class ViewHolder {
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        public final View itemView;
        private int mFlags;
        RecyclerView mOwnerRecyclerView;
        int mPosition = -1;
        int mOldPosition = -1;
        long mItemId = -1;
        int mItemViewType = -1;
        int mPreLayoutPosition = -1;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mPayloads = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mIsRecyclableCount = 0;
        private Recycler mScrapContainer = null;
        private boolean mInChangeScrap = false;
        private int mWasImportantForAccessibilityBeforeHidden = 0;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(8);
            offsetPosition(offset, applyToPreLayout);
            this.mPosition = mNewPosition;
        }

        void offsetPosition(int offset, boolean applyToPreLayout) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (applyToPreLayout) {
                this.mPreLayoutPosition += offset;
            }
            this.mPosition += offset;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }

        boolean shouldIgnore() {
            return (this.mFlags & 128) != 0;
        }

        public final int getLayoutPosition() {
            return this.mPreLayoutPosition == -1 ? this.mPosition : this.mPreLayoutPosition;
        }

        public final int getAdapterPosition() {
            if (this.mOwnerRecyclerView != null) {
                return this.mOwnerRecyclerView.getAdapterPositionFor(this);
            }
            return -1;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        boolean isScrap() {
            return this.mScrapContainer != null;
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            return (this.mFlags & 32) != 0;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
            this.mScrapContainer = recycler;
            this.mInChangeScrap = isChangeScrap;
        }

        boolean isInvalid() {
            return (this.mFlags & 4) != 0;
        }

        boolean needsUpdate() {
            return (this.mFlags & 2) != 0;
        }

        boolean isBound() {
            return (this.mFlags & 1) != 0;
        }

        boolean isRemoved() {
            return (this.mFlags & 8) != 0;
        }

        boolean hasAnyOfTheFlags(int flags) {
            return (this.mFlags & flags) != 0;
        }

        boolean isTmpDetached() {
            return (this.mFlags & 256) != 0;
        }

        boolean isAdapterPositionUnknown() {
            return (this.mFlags & 512) != 0 || isInvalid();
        }

        void setFlags(int flags, int mask) {
            this.mFlags = (this.mFlags & (mask ^ (-1))) | (flags & mask);
        }

        void addFlags(int flags) {
            this.mFlags |= flags;
        }

        void addChangePayload(Object payload) {
            if (payload == null) {
                addFlags(1024);
            } else if ((this.mFlags & 1024) == 0) {
                createPayloadsIfNeeded();
                this.mPayloads.add(payload);
            }
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList();
                this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
            }
        }

        void clearPayload() {
            if (this.mPayloads != null) {
                this.mPayloads.clear();
            }
            this.mFlags &= -1025;
        }

        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) == 0) {
                if (this.mPayloads == null || this.mPayloads.size() == 0) {
                    return FULLUPDATE_PAYLOADS;
                }
                return this.mUnmodifiedPayloads;
            }
            return FULLUPDATE_PAYLOADS;
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onEnteredHiddenState() {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            ViewCompat.setImportantForAccessibility(this.itemView, 4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onLeftHiddenState() {
            ViewCompat.setImportantForAccessibility(this.itemView, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (isScrap()) {
                sb.append(" scrap ").append(this.mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
            }
            if (isInvalid()) {
                sb.append(" invalid");
            }
            if (!isBound()) {
                sb.append(" unbound");
            }
            if (needsUpdate()) {
                sb.append(" update");
            }
            if (isRemoved()) {
                sb.append(" removed");
            }
            if (shouldIgnore()) {
                sb.append(" ignored");
            }
            if (isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!isRecyclable()) {
                sb.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (isAdapterPositionUnknown()) {
                sb.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }

        public final void setIsRecyclable(boolean recyclable) {
            this.mIsRecyclableCount = recyclable ? this.mIsRecyclableCount - 1 : this.mIsRecyclableCount + 1;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && this.mIsRecyclableCount == 1) {
                this.mFlags |= 16;
            } else if (recyclable && this.mIsRecyclableCount == 0) {
                this.mFlags &= -17;
            }
        }

        public final boolean isRecyclable() {
            return (this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldBeKeptAsChild() {
            return (this.mFlags & 16) != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView);
        }

        boolean isUpdated() {
            return (this.mFlags & 2) != 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(524) || !viewHolder.isBound()) {
            return -1;
        }
        return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public void setNestedScrollingEnabled(boolean enabled) {
        this.mScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean isNestedScrollingEnabled() {
        return this.mScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean startNestedScroll(int axes) {
        return this.mScrollingChildHelper.startNestedScroll(axes);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public void stopNestedScroll() {
        this.mScrollingChildHelper.stopNestedScroll();
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean hasNestedScrollingParent() {
        return this.mScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return this.mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return this.mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return this.mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override // android.view.View, android.support.v4.view.NestedScrollingChild
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return this.mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.LayoutParams) source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private boolean mRunning;
        private View mTargetView;
        private int mTargetPosition = -1;
        private final Action mRecyclingAction = new Action(0, 0);

        protected abstract void onSeekTargetStep(int i, int i2, State state, Action action);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View view, State state, Action action);

        void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = findViewByPosition(getTargetPosition());
            onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
        }

        public void setTargetPosition(int targetPosition) {
            this.mTargetPosition = targetPosition;
        }

        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        protected final void stop() {
            if (this.mRunning) {
                onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                this.mRunning = false;
                this.mLayoutManager.onSmoothScrollerStopped(this);
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAnimation(int dx, int dy) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || recyclerView == null) {
                stop();
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    stop();
                } else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                onSeekTargetStep(dx, dy, recyclerView.mState, this.mRecyclingAction);
                boolean hadJumpTarget = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (hadJumpTarget) {
                    if (this.mRunning) {
                        this.mPendingInitialRun = true;
                        recyclerView.mViewFlinger.postOnAnimation();
                    } else {
                        stop();
                    }
                }
            }
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public View findViewByPosition(int position) {
            return this.mRecyclerView.mLayout.findViewByPosition(position);
        }

        protected void onChildAttachedToWindow(View child) {
            if (getChildPosition(child) == getTargetPosition()) {
                this.mTargetView = child;
            }
        }

        protected void normalize(PointF scrollVector) {
            double magnitute = Math.sqrt((scrollVector.x * scrollVector.x) + (scrollVector.y * scrollVector.y));
            scrollVector.x = (float) (scrollVector.x / magnitute);
            scrollVector.y = (float) (scrollVector.y / magnitute);
        }

        public static class Action {
            private boolean changed;
            private int consecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;

            public Action(int dx, int dy) {
                this(dx, dy, Integer.MIN_VALUE, null);
            }

            public Action(int dx, int dy, int duration, Interpolator interpolator) {
                this.mJumpToPosition = -1;
                this.changed = false;
                this.consecutiveUpdates = 0;
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
            }

            public void jumpTo(int targetPosition) {
                this.mJumpToPosition = targetPosition;
            }

            boolean hasJumpTarget() {
                return this.mJumpToPosition >= 0;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void runIfNecessary(RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    int position = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(position);
                    this.changed = false;
                    return;
                }
                if (this.changed) {
                    validate();
                    if (this.mInterpolator != null) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    } else if (this.mDuration == Integer.MIN_VALUE) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                    }
                    this.consecutiveUpdates++;
                    if (this.consecutiveUpdates > 10) {
                        Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.changed = false;
                    return;
                }
                this.consecutiveUpdates = 0;
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public void update(int dx, int dy, int duration, Interpolator interpolator) {
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
                this.changed = true;
            }
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onChanged();
            }
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.support.v7.widget.RecyclerView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable mLayoutState;

        SavedState(Parcel in) {
            super(in);
            this.mLayoutState = in.readParcelable(LayoutManager.class.getClassLoader());
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.mLayoutState, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void copyFrom(SavedState other) {
            this.mLayoutState = other.mLayoutState;
        }
    }

    public static class State {
        private SparseArray<Object> mData;
        private int mTargetPosition = -1;
        private int mLayoutStep = 1;
        int mItemCount = 0;
        private int mPreviousLayoutItemCount = 0;
        private int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        private boolean mStructureChanged = false;
        private boolean mInPreLayout = false;
        private boolean mRunSimpleAnimations = false;
        private boolean mRunPredictiveAnimations = false;
        private boolean mTrackOldChangeHolders = false;
        private boolean mIsMeasuring = false;

        static /* synthetic */ int access$1712(State x0, int x1) {
            int i = x0.mDeletedInvisibleItemCountSincePreviousLayout + x1;
            x0.mDeletedInvisibleItemCountSincePreviousLayout = i;
            return i;
        }

        void assertLayoutStep(int accepted) {
            if ((this.mLayoutStep & accepted) == 0) {
                throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(accepted) + " but it is " + Integer.toBinaryString(this.mLayoutStep));
            }
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }

        public int getItemCount() {
            return this.mInPreLayout ? this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout : this.mItemCount;
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }
    }

    private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        private ItemAnimatorRestoreListener() {
        }

        @Override // android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onAnimationFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.mShadowedHolder != null && item.mShadowingHolder == null) {
                item.mShadowedHolder = null;
            }
            item.mShadowingHolder = null;
            if (!item.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(item.itemView) && item.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(item.itemView, false);
            }
        }
    }

    public static abstract class ItemAnimator {
        private ItemAnimatorListener mListener = null;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
        private long mAddDuration = 120;
        private long mRemoveDuration = 120;
        private long mMoveDuration = 250;
        private long mChangeDuration = 250;

        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        interface ItemAnimatorListener {
            void onAnimationFinished(ViewHolder viewHolder);
        }

        public abstract boolean animateAppearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateDisappearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animatePersistence(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public abstract boolean isRunning();

        public abstract void runPendingAnimations();

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        void setListener(ItemAnimatorListener listener) {
            this.mListener = listener;
        }

        public ItemHolderInfo recordPreLayoutInformation(State state, ViewHolder viewHolder, int changeFlags, List<Object> payloads) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        public ItemHolderInfo recordPostLayoutInformation(State state, ViewHolder viewHolder) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int flags = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((flags & 4) == 0) {
                int oldPos = viewHolder.getOldPosition();
                int pos = viewHolder.getAdapterPosition();
                if (oldPos != -1 && pos != -1 && oldPos != pos) {
                    flags |= 2048;
                }
            }
            return flags;
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            onAnimationFinished(viewHolder);
            if (this.mListener != null) {
                this.mListener.onAnimationFinished(viewHolder);
            }
        }

        public void onAnimationFinished(ViewHolder viewHolder) {
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder, List<Object> payloads) {
            return canReuseUpdatedViewHolder(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            int count = this.mFinishedListeners.size();
            for (int i = 0; i < count; i++) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder holder) {
                return setFrom(holder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder holder, int flags) {
                View view = holder.itemView;
                this.left = view.getLeft();
                this.top = view.getTop();
                this.right = view.getRight();
                this.bottom = view.getBottom();
                return this;
            }
        }
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int childCount, int i) {
        return this.mChildDrawingOrderCallback == null ? super.getChildDrawingOrder(childCount, i) : this.mChildDrawingOrderCallback.onGetChildDrawingOrder(childCount, i);
    }
}
