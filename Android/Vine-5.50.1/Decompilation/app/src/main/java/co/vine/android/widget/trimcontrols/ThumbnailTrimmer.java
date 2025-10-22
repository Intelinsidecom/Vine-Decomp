package co.vine.android.widget.trimcontrols;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import co.vine.android.R;

/* loaded from: classes.dex */
public class ThumbnailTrimmer extends View {
    private int mCarouselPadding;
    private int mContainerLeft;
    private int mContainerRight;
    private Paint mDeselectedPaint;
    private Rect mDeselectedRect;
    private Rect mFrontDeselectedRect;
    private NinePatchDrawable mLeftScrubber;
    private int mLeftScrubberOffset;
    private boolean mLeftScrubbing;
    private boolean mLeftTrimEnabled;
    private OnScrubberMovedListener mListener;
    private Paint mOutlinePaint;
    private Rect mOutlineRect;
    private int mOutlineRectStart;
    private int mOutlineRectWidth;
    private int mPaddingLeft;
    private int mScrubberDrawableWidth;
    private int mScrubberOffset;
    private int mScrubberPadding;
    private Paint mScrubberPaint;
    private Rect mScrubberRect;
    private boolean mScrubbing;
    private Rect mStartRect;
    private int mStrokeWidth;
    private NinePatchDrawable mTrimScrubber;

    public interface OnScrubberMovedListener {
        void onScrubberMoved(int i, int i2);
    }

    public ThumbnailTrimmer(Context context, int paddingLeft, boolean leftTrimEnabled) {
        super(context);
        init(paddingLeft, leftTrimEnabled);
    }

    private void init(int paddingLeft, boolean leftTrimEnabled) {
        this.mLeftTrimEnabled = leftTrimEnabled;
        this.mScrubberPadding = getResources().getDimensionPixelSize(R.dimen.scrubber_padding);
        this.mCarouselPadding = getResources().getDimensionPixelSize(R.dimen.carousel_padding);
        this.mScrubberRect = new Rect();
        this.mStartRect = new Rect();
        this.mScrubberPaint = new Paint();
        this.mScrubberPaint.setColor(0);
        this.mStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.trimmer_stroke_width);
        this.mOutlineRect = new Rect();
        this.mOutlinePaint = new Paint();
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        this.mOutlinePaint.setStrokeWidth(this.mStrokeWidth);
        if (this.mLeftTrimEnabled) {
            this.mTrimScrubber = (NinePatchDrawable) getResources().getDrawable(R.drawable.green_right_trimmer);
            this.mLeftScrubber = (NinePatchDrawable) getResources().getDrawable(R.drawable.green_left_trimmer);
            this.mScrubberDrawableWidth = this.mTrimScrubber.getMinimumWidth() * 3;
            this.mOutlinePaint.setColor(getResources().getColor(R.color.vine_green));
        } else {
            this.mTrimScrubber = (NinePatchDrawable) getResources().getDrawable(R.drawable.scrub_right);
            this.mScrubberDrawableWidth = this.mTrimScrubber.getMinimumWidth();
            this.mOutlinePaint.setColor(getResources().getColor(R.color.trim_yellow));
        }
        this.mDeselectedRect = new Rect();
        this.mFrontDeselectedRect = new Rect();
        this.mDeselectedPaint = new Paint();
        this.mDeselectedPaint.setColor(-1879048192);
        setPadding(paddingLeft, 0, 0, 0);
        this.mPaddingLeft = paddingLeft;
    }

    public void setListener(OnScrubberMovedListener listener) {
        this.mListener = listener;
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int paddedTop = top + this.mCarouselPadding;
            int paddedBottom = bottom - this.mCarouselPadding;
            int paddedLeft = this.mPaddingLeft + left + (this.mStrokeWidth / 2);
            this.mContainerRight = right;
            this.mContainerLeft = left;
            if (this.mLeftTrimEnabled) {
                this.mScrubberRect.set(right - this.mScrubberDrawableWidth, paddedTop - this.mStrokeWidth, right, this.mStrokeWidth + paddedBottom);
                this.mStartRect.set(0, paddedTop - this.mStrokeWidth, this.mScrubberDrawableWidth, this.mStrokeWidth + paddedBottom);
                this.mOutlineRect.set(this.mScrubberDrawableWidth + paddedLeft, paddedTop, right - this.mScrubberDrawableWidth, paddedBottom);
            } else {
                this.mScrubberRect.set(right - this.mScrubberDrawableWidth, top, right, bottom);
                this.mStartRect.set(0, top, this.mScrubberDrawableWidth, bottom);
                this.mOutlineRect.set(paddedLeft, paddedTop, right, paddedBottom);
            }
            this.mDeselectedRect.set(right, paddedTop, right, paddedBottom);
            this.mFrontDeselectedRect.set(0, paddedTop, paddedLeft - (this.mStrokeWidth / 2), paddedBottom);
            updateOutlineWidth();
        }
    }

    public void setDeselectedBounds(int leftPx, int rightPx) {
        if (this.mLeftTrimEnabled) {
            this.mFrontDeselectedRect.right = leftPx;
        } else {
            this.mFrontDeselectedRect.left = leftPx;
        }
        this.mDeselectedRect.right = rightPx;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(this.mScrubberRect, this.mScrubberPaint);
        canvas.drawRect(this.mOutlineRect, this.mOutlinePaint);
        canvas.drawRect(this.mDeselectedRect, this.mDeselectedPaint);
        canvas.drawRect(this.mFrontDeselectedRect, this.mDeselectedPaint);
        NinePatchDrawable trimScrubber = this.mTrimScrubber;
        if (trimScrubber != null) {
            trimScrubber.setBounds(this.mScrubberRect);
            trimScrubber.draw(canvas);
        }
        NinePatchDrawable leftScrubber = this.mLeftScrubber;
        if (leftScrubber != null && this.mLeftTrimEnabled) {
            leftScrubber.setBounds(this.mStartRect);
            leftScrubber.draw(canvas);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0102  */
    @Override // android.view.View
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
            Method dump skipped, instructions count: 274
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.widget.trimcontrols.ThumbnailTrimmer.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public int getScrubberDrawableWidth() {
        return this.mScrubberDrawableWidth;
    }

    public int getOutlineRectStart() {
        return this.mOutlineRectStart;
    }

    public int getOutlineRectWidth() {
        return this.mOutlineRectWidth;
    }

    private void updateOutlineWidth() {
        this.mOutlineRectWidth = this.mOutlineRect.width();
        this.mOutlineRectStart = this.mOutlineRect.left;
    }

    private boolean inScrubber(int touchX, int touchY) {
        return touchX < this.mScrubberRect.right + this.mScrubberPadding && touchX > this.mScrubberRect.left - this.mScrubberPadding && touchY < this.mScrubberRect.bottom + this.mScrubberPadding && touchY > this.mScrubberRect.top - this.mScrubberPadding;
    }

    private boolean inLeftScrubber(int touchX, int touchY) {
        return touchX < this.mStartRect.right + this.mScrubberPadding && touchX > this.mStartRect.left - this.mScrubberPadding && touchY < this.mStartRect.bottom + this.mScrubberPadding && touchY > this.mStartRect.top - this.mScrubberPadding;
    }
}
