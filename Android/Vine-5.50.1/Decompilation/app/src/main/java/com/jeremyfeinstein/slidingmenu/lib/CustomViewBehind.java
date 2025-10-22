package com.jeremyfeinstein.slidingmenu.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.party.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/* loaded from: classes.dex */
public class CustomViewBehind extends ViewGroup {
    private boolean mChildrenEnabled;
    private View mContent;
    private float mFadeDegree;
    private boolean mFadeEnabled;
    private final Paint mFadePaint;
    private int mMarginThreshold;
    private int mMode;
    private float mScrollScale;
    private View mSecondaryContent;
    private Drawable mSecondaryShadowDrawable;
    private View mSelectedView;
    private Bitmap mSelectorDrawable;
    private boolean mSelectorEnabled;
    private Drawable mShadowDrawable;
    private int mShadowWidth;
    private int mTouchMode;
    private SlidingMenu.CanvasTransformer mTransformer;
    private CustomViewAbove mViewAbove;
    private int mWidthOffset;

    public CustomViewBehind(Context context) {
        this(context, null);
    }

    public CustomViewBehind(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTouchMode = 0;
        this.mFadePaint = new Paint();
        this.mSelectorEnabled = true;
        this.mMarginThreshold = (int) TypedValue.applyDimension(1, 48.0f, getResources().getDisplayMetrics());
    }

    public void setCustomViewAbove(CustomViewAbove customViewAbove) {
        this.mViewAbove = customViewAbove;
    }

    public void setCanvasTransformer(SlidingMenu.CanvasTransformer t) {
        this.mTransformer = t;
    }

    public void setWidthOffset(int i) {
        this.mWidthOffset = i;
        requestLayout();
    }

    public void setMarginThreshold(int marginThreshold) {
        this.mMarginThreshold = marginThreshold;
    }

    public int getMarginThreshold() {
        return this.mMarginThreshold;
    }

    public int getBehindWidth() {
        return this.mContent.getWidth();
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

    public void setSecondaryContent(View v) {
        if (this.mSecondaryContent != null) {
            removeView(this.mSecondaryContent);
        }
        this.mSecondaryContent = v;
        addView(this.mSecondaryContent);
    }

    public View getSecondaryContent() {
        return this.mSecondaryContent;
    }

    public void setChildrenEnabled(boolean enabled) {
        this.mChildrenEnabled = enabled;
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        if (this.mTransformer != null) {
            invalidate();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return !this.mChildrenEnabled;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        return !this.mChildrenEnabled;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.mTransformer != null) {
            canvas.save();
            this.mTransformer.transformCanvas(canvas, this.mViewAbove.getPercentOpen());
            super.dispatchDraw(canvas);
            canvas.restore();
            return;
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        this.mContent.layout(0, 0, width - this.mWidthOffset, height);
        if (this.mSecondaryContent != null) {
            this.mSecondaryContent.layout(0, 0, width - this.mWidthOffset, height);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width - this.mWidthOffset);
        int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
        this.mContent.measure(contentWidth, contentHeight);
        if (this.mSecondaryContent != null) {
            this.mSecondaryContent.measure(contentWidth, contentHeight);
        }
    }

    public void setMode(int mode) {
        if (mode == 0 || mode == 1) {
            if (this.mContent != null) {
                this.mContent.setVisibility(0);
            }
            if (this.mSecondaryContent != null) {
                this.mSecondaryContent.setVisibility(4);
            }
        }
        this.mMode = mode;
    }

    public int getMode() {
        return this.mMode;
    }

    public void setScrollScale(float scrollScale) {
        this.mScrollScale = scrollScale;
    }

    public float getScrollScale() {
        return this.mScrollScale;
    }

    public void setShadowDrawable(Drawable shadow) {
        this.mShadowDrawable = shadow;
        invalidate();
    }

    public void setSecondaryShadowDrawable(Drawable shadow) {
        this.mSecondaryShadowDrawable = shadow;
        invalidate();
    }

    public void setShadowWidth(int width) {
        this.mShadowWidth = width;
        invalidate();
    }

    public void setFadeEnabled(boolean b) {
        this.mFadeEnabled = b;
    }

    public void setFadeDegree(float degree) {
        if (degree > 1.0f || degree < 0.0f) {
            throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
        }
        this.mFadeDegree = degree;
    }

    public int getMenuPage(int page) {
        if (page > 1) {
            page = 2;
        } else if (page < 1) {
            page = 0;
        }
        if (this.mMode == 0 && page > 1) {
            return 0;
        }
        if (this.mMode != 1 || page >= 1) {
            return page;
        }
        return 2;
    }

    public void scrollBehindTo(View content, int x, int y) {
        if (this.mMode == 0) {
            vis = x >= content.getLeft() ? 4 : 0;
            scrollTo((int) ((getBehindWidth() + x) * this.mScrollScale), y);
        } else if (this.mMode == 1) {
            vis = x <= content.getLeft() ? 4 : 0;
            scrollTo((int) ((getBehindWidth() - getWidth()) + ((x - getBehindWidth()) * this.mScrollScale)), y);
        } else if (this.mMode == 2) {
            this.mContent.setVisibility(x >= content.getLeft() ? 4 : 0);
            this.mSecondaryContent.setVisibility(x <= content.getLeft() ? 4 : 0);
            vis = x == 0 ? 4 : 0;
            if (x <= content.getLeft()) {
                scrollTo((int) ((getBehindWidth() + x) * this.mScrollScale), y);
            } else {
                scrollTo((int) ((getBehindWidth() - getWidth()) + ((x - getBehindWidth()) * this.mScrollScale)), y);
            }
        }
        if (vis == 4) {
            Log.v("CustomViewBehind", "behind INVISIBLE");
        }
        setVisibility(vis);
    }

    public int getMenuLeft(View content, int page) {
        if (this.mMode == 0) {
            switch (page) {
                case 0:
                    return content.getLeft() - getBehindWidth();
                case 2:
                    return content.getLeft();
            }
        }
        if (this.mMode == 1) {
            switch (page) {
                case 0:
                    return content.getLeft();
                case 2:
                    return content.getLeft() + getBehindWidth();
            }
        }
        if (this.mMode == 2) {
            switch (page) {
                case 0:
                    return content.getLeft() - getBehindWidth();
                case 2:
                    return content.getLeft() + getBehindWidth();
            }
        }
        return content.getLeft();
    }

    public int getAbsLeftBound(View content) {
        if (this.mMode == 0 || this.mMode == 2) {
            return content.getLeft() - getBehindWidth();
        }
        if (this.mMode == 1) {
            return content.getLeft();
        }
        return 0;
    }

    public int getAbsRightBound(View content) {
        if (this.mMode == 0) {
            return content.getLeft();
        }
        if (this.mMode == 1 || this.mMode == 2) {
            return content.getLeft() + getBehindWidth();
        }
        return 0;
    }

    public boolean marginTouchAllowed(View content, int x) {
        int left = content.getLeft();
        int right = content.getRight();
        if (this.mMode == 0) {
            return x >= left && x <= this.mMarginThreshold + left;
        }
        if (this.mMode == 1) {
            return x <= right && x >= right - this.mMarginThreshold;
        }
        if (this.mMode != 2) {
            return false;
        }
        if (x < left || x > this.mMarginThreshold + left) {
            return x <= right && x >= right - this.mMarginThreshold;
        }
        return true;
    }

    public void setTouchMode(int i) {
        this.mTouchMode = i;
    }

    public boolean menuOpenTouchAllowed(View content, int currPage, float x) {
        switch (this.mTouchMode) {
            case 0:
                return menuTouchInQuickReturn(content, currPage, x);
            case 1:
                return true;
            default:
                return false;
        }
    }

    public boolean menuTouchInQuickReturn(View content, int currPage, float x) {
        if (this.mMode == 0 || (this.mMode == 2 && currPage == 0)) {
            return x >= ((float) content.getLeft());
        }
        if (this.mMode == 1 || (this.mMode == 2 && currPage == 2)) {
            return x <= ((float) content.getRight());
        }
        return false;
    }

    public boolean menuClosedSlideAllowed(float dx) {
        return this.mMode == 0 ? dx > 0.0f : this.mMode == 1 ? dx < 0.0f : this.mMode == 2;
    }

    public boolean menuOpenSlideAllowed(float dx) {
        return this.mMode == 0 ? dx < 0.0f : this.mMode == 1 ? dx > 0.0f : this.mMode == 2;
    }

    public void drawShadow(View content, Canvas canvas) {
        if (this.mShadowDrawable != null && this.mShadowWidth > 0) {
            int left = 0;
            if (this.mMode == 0) {
                left = content.getLeft() - this.mShadowWidth;
            } else if (this.mMode == 1) {
                left = content.getRight();
            } else if (this.mMode == 2) {
                if (this.mSecondaryShadowDrawable != null) {
                    int left2 = content.getRight();
                    this.mSecondaryShadowDrawable.setBounds(left2, 0, this.mShadowWidth + left2, getHeight());
                    this.mSecondaryShadowDrawable.draw(canvas);
                }
                left = content.getLeft() - this.mShadowWidth;
            }
            this.mShadowDrawable.setBounds(left, 0, this.mShadowWidth + left, getHeight());
            this.mShadowDrawable.draw(canvas);
        }
    }

    public void drawFade(View content, Canvas canvas, float openPercent) {
        if (this.mFadeEnabled) {
            int alpha = (int) (this.mFadeDegree * 255.0f * Math.abs(1.0f - openPercent));
            this.mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
            int left = 0;
            int right = 0;
            if (this.mMode == 0) {
                left = content.getLeft() - getBehindWidth();
                right = content.getLeft();
            } else if (this.mMode == 1) {
                left = content.getRight();
                right = content.getRight() + getBehindWidth();
            } else if (this.mMode == 2) {
                int left2 = content.getLeft() - getBehindWidth();
                int right2 = content.getLeft();
                canvas.drawRect(left2, 0.0f, right2, getHeight(), this.mFadePaint);
                left = content.getRight();
                right = content.getRight() + getBehindWidth();
            }
            canvas.drawRect(left, 0.0f, right, getHeight(), this.mFadePaint);
        }
    }

    public void drawSelector(View content, Canvas canvas, float openPercent) {
        if (this.mSelectorEnabled && this.mSelectorDrawable != null && this.mSelectedView != null) {
            String tag = (String) this.mSelectedView.getTag(R.id.selected_view);
            if (tag.equals("CustomViewBehindSelectedView")) {
                canvas.save();
                int offset = (int) (this.mSelectorDrawable.getWidth() * openPercent);
                if (this.mMode == 0) {
                    int right = content.getLeft();
                    int left = right - offset;
                    canvas.clipRect(left, 0, right, getHeight());
                    canvas.drawBitmap(this.mSelectorDrawable, left, getSelectorTop(), (Paint) null);
                } else if (this.mMode == 1) {
                    int left2 = content.getRight();
                    int right2 = left2 + offset;
                    canvas.clipRect(left2, 0, right2, getHeight());
                    canvas.drawBitmap(this.mSelectorDrawable, right2 - this.mSelectorDrawable.getWidth(), getSelectorTop(), (Paint) null);
                }
                canvas.restore();
            }
        }
    }

    public void setSelectorEnabled(boolean b) {
        this.mSelectorEnabled = b;
    }

    public void setSelectedView(View v) {
        if (this.mSelectedView != null) {
            this.mSelectedView.setTag(R.id.selected_view, null);
            this.mSelectedView = null;
        }
        if (v != null && v.getParent() != null) {
            this.mSelectedView = v;
            this.mSelectedView.setTag(R.id.selected_view, "CustomViewBehindSelectedView");
            invalidate();
        }
    }

    private int getSelectorTop() {
        int y = this.mSelectedView.getTop();
        return y + ((this.mSelectedView.getHeight() - this.mSelectorDrawable.getHeight()) / 2);
    }

    public void setSelectorBitmap(Bitmap b) {
        this.mSelectorDrawable = b;
        refreshDrawableState();
    }
}
