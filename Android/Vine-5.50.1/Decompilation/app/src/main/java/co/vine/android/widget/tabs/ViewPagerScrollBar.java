package co.vine.android.widget.tabs;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.View;
import co.vine.android.R;
import co.vine.android.VineLoggingException;
import co.vine.android.util.CrashUtil;

/* loaded from: classes.dex */
public class ViewPagerScrollBar extends View {
    private int[] mColorIds;
    private int mHeight;
    private boolean mNavBottomSet;
    private int mOffset;
    private int mOffsetPixels;
    private final Paint mPaint;
    private int mPosition;
    private int mRange;
    private float mScrollBarWidth;
    private final RectF mTabBounds;

    public ViewPagerScrollBar(Context context) {
        this(context, null, R.attr.viewPagerScrollBarStyle);
    }

    public ViewPagerScrollBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.viewPagerScrollBarStyle);
    }

    public ViewPagerScrollBar(Context context, AttributeSet attrs, int defStyle) throws Resources.NotFoundException {
        super(context, attrs, defStyle);
        this.mHeight = 0;
        this.mRange = 1;
        this.mOffset = 0;
        this.mNavBottomSet = false;
        this.mScrollBarWidth = 0.0f;
        this.mPaint = new Paint();
        this.mTabBounds = new RectF();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerScrollBar, defStyle, 0);
        int colorArrayRes = a.getResourceId(2, 0);
        this.mHeight = a.getDimensionPixelSize(1, 0);
        if (colorArrayRes > 0) {
            TypedArray ta = context.getResources().obtainTypedArray(colorArrayRes);
            this.mColorIds = new int[ta.length()];
            for (int i = 0; i < ta.length(); i++) {
                this.mColorIds[i] = ta.getColor(i, 0);
            }
            ta.recycle();
            this.mPaint.setColor(this.mColorIds[0]);
        } else {
            this.mPaint.setColor(a.getColor(0, -1));
        }
        a.recycle();
    }

    public void setScrollBarWidth(int width) {
        this.mScrollBarWidth = TypedValue.applyDimension(1, width, getResources().getDisplayMetrics());
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!this.mNavBottomSet) {
            this.mOffset = getHeight();
        }
        updateBounds();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(this.mTabBounds, 10.0f, 10.0f, this.mPaint);
    }

    public void updateBounds() {
        float width = getWidth() / this.mRange;
        int v = this.mOffsetPixels;
        float w = width * this.mPosition;
        float left = v + w;
        if (this.mScrollBarWidth != 0.0f) {
            float addtionalOffset = (width - this.mScrollBarWidth) / 2.0f;
            left += addtionalOffset;
            width = this.mScrollBarWidth;
        }
        this.mTabBounds.set(left, this.mOffset - this.mHeight, left + width, this.mOffset);
    }

    public void setPosition(int position) {
        this.mPosition = position;
        this.mOffsetPixels = 0;
        updateBounds();
        invalidate();
    }

    public void setNavOffset(int offset) {
        this.mOffset = offset;
        this.mNavBottomSet = true;
        updateBounds();
        invalidate();
    }

    public void setRange(int range) {
        this.mRange = range;
        if (this.mRange == 0) {
            this.mRange = 1;
            CrashUtil.logException(new VineLoggingException("Someone is trying to set the range to be 0, defaulting back to 1."));
        }
        requestLayout();
        invalidate();
    }

    public void scroll(int position, int pixels) {
        this.mPosition = position;
        this.mOffsetPixels = (int) FloatMath.ceil(pixels / this.mRange);
        updateBounds();
        invalidate();
    }

    public void setPageColor(int position) {
        if (this.mColorIds != null) {
            this.mPaint.setColor(this.mColorIds[position]);
        }
    }

    public void setColorIds(int colorsResId) {
        this.mColorIds = getResources().getIntArray(colorsResId);
        this.mPaint.setColor(this.mColorIds[0]);
        requestLayout();
        invalidate();
    }

    public void setLineColor(int color) {
        this.mPaint.setColor(color);
    }
}
