package co.vine.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class RangeSeekBar extends View {
    private double mAbsoluteMaxValuePrim;
    private final double mAbsoluteMinValuePrim;
    private int mActivePointerId;
    private final int mBackgroundColor;
    private final float mDoublePadding;
    private float mDownMotionX;
    private final GestureDetector mGestureDetector;
    private boolean mIsDragging;
    private float mLastX;
    private OnRangeSeekBarChangeListener mListener;
    private double mNormalizedMaxPercentage;
    private double mNormalizedMaxValue;
    private double mNormalizedMinValue;
    private final float mPadding;
    private final Paint mPaint;
    private int mPressedThumb;
    private final int mPrimaryColor;
    private final RectF mRectF;
    private int mScaledTouchSlop;
    private final Rect mScrubRect;
    private final float mThumbDragWidth;
    private final float mThumbHalfWidth;
    private final NinePatchDrawable mThumbImageLeft;
    private final NinePatchDrawable mThumbImageRight;
    private final float mTopBottomPadding;

    public interface OnRangeSeekBarChangeListener {
        void onRangeSeekBarLongPressHappened();

        void onRangeSeekBarTouchDown(RangeSeekBar rangeSeekBar, int i, int i2);

        void onRangeSeekBarTouchUp(RangeSeekBar rangeSeekBar, int i, int i2);

        void onRangeSeekBarValuesChanged(RangeSeekBar rangeSeekBar, int i, int i2, boolean z);
    }

    public RangeSeekBar(NinePatchDrawable left, NinePatchDrawable right, Integer absoluteMinValue, Integer absoluteMaxValue, Context context, int primaryColor, int backgroundColor) throws IllegalArgumentException {
        super(context);
        this.mPaint = new Paint(1);
        this.mRectF = new RectF();
        this.mScrubRect = new Rect();
        this.mNormalizedMinValue = 0.0d;
        this.mNormalizedMaxValue = 1.0d;
        this.mPressedThumb = -1;
        this.mNormalizedMaxPercentage = 1.0d;
        this.mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() { // from class: co.vine.android.views.RangeSeekBar.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent e) {
                if (RangeSeekBar.this.mListener != null) {
                    RangeSeekBar.this.mListener.onRangeSeekBarLongPressHappened();
                }
            }
        });
        this.mActivePointerId = 255;
        this.mLastX = 0.0f;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        int thumbWidth = left.getIntrinsicWidth();
        this.mPrimaryColor = primaryColor;
        this.mBackgroundColor = backgroundColor;
        this.mThumbImageLeft = left;
        this.mThumbImageRight = right;
        this.mThumbDragWidth = thumbWidth * 2;
        this.mThumbHalfWidth = 0.5f * thumbWidth;
        this.mPadding = this.mThumbHalfWidth;
        this.mDoublePadding = this.mPadding * 2.0f;
        this.mTopBottomPadding = this.mPadding * 0.2f;
        this.mAbsoluteMinValuePrim = absoluteMinValue.doubleValue();
        this.mAbsoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public int getSelectedMinValue() {
        return normalizedToValue(this.mNormalizedMinValue);
    }

    public void setSelectedMinValue(int value) {
        if (0.0d == this.mAbsoluteMaxValuePrim - this.mAbsoluteMinValuePrim) {
            setNormalizedMinValue(0.0d);
        } else {
            setNormalizedMinValue(valueToNormalized(Integer.valueOf(value)));
        }
    }

    public int getSelectedMaxValue() {
        return normalizedToValue(this.mNormalizedMaxValue);
    }

    public void setSelectedMaxValue(int value) {
        if (0.0d == this.mAbsoluteMaxValuePrim - this.mAbsoluteMinValuePrim) {
            setNormalizedMaxValue(1.0d);
        } else {
            setNormalizedMaxValue(valueToNormalized(Integer.valueOf(value)));
        }
    }

    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener listener) {
        this.mListener = listener;
    }

    public boolean seekToLeft() {
        return this.mPressedThumb == 0 || this.mPressedThumb == 2;
    }

    public void notifyListenerOfValueChange() {
        if (this.mListener != null) {
            this.mListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue(), false);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        if (!this.mGestureDetector.onTouchEvent(event)) {
            int action = event.getAction();
            switch (action & 255) {
                case 0:
                    this.mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
                    int pointerIndex = event.findPointerIndex(this.mActivePointerId);
                    this.mDownMotionX = event.getX(pointerIndex);
                    this.mLastX = this.mDownMotionX;
                    this.mPressedThumb = evalPressedThumb(this.mDownMotionX);
                    if (this.mPressedThumb != -1) {
                        setPressed(true);
                        invalidate();
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                        if (this.mListener != null) {
                            this.mListener.onRangeSeekBarTouchDown(this, getSelectedMinValue(), getSelectedMaxValue());
                            break;
                        }
                    } else {
                        return true;
                    }
                    break;
                case 1:
                    if (this.mIsDragging) {
                        trackTouchEvent(event);
                        onStopTrackingTouch();
                        setPressed(false);
                    } else {
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        onStopTrackingTouch();
                    }
                    this.mPressedThumb = -1;
                    invalidate();
                    if (this.mListener != null) {
                        this.mListener.onRangeSeekBarTouchUp(this, getSelectedMinValue(), getSelectedMaxValue());
                        break;
                    }
                    break;
                case 2:
                    if (this.mPressedThumb != -1) {
                        if (this.mIsDragging) {
                            trackTouchEvent(event);
                        } else {
                            int pointerIndex2 = event.findPointerIndex(this.mActivePointerId);
                            float x = event.getX(pointerIndex2);
                            if (Math.abs(x - this.mDownMotionX) > this.mScaledTouchSlop) {
                                setPressed(true);
                                invalidate();
                                onStartTrackingTouch();
                                trackTouchEvent(event);
                                attemptClaimDrag();
                            }
                        }
                        if (this.mListener != null) {
                            this.mListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue(), true);
                            break;
                        }
                    }
                    break;
                case 3:
                    if (this.mIsDragging) {
                        onStopTrackingTouch();
                        setPressed(false);
                    }
                    invalidate();
                    break;
                case 5:
                    int index = event.getPointerCount() - 1;
                    this.mDownMotionX = event.getX(index);
                    this.mActivePointerId = event.getPointerId(index);
                    invalidate();
                    break;
                case 6:
                    onSecondaryPointerUp(event);
                    invalidate();
                    break;
            }
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mDownMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        int pointerIndex = event.findPointerIndex(this.mActivePointerId);
        float x = event.getX(pointerIndex);
        if (this.mPressedThumb == 0) {
            setNormalizedMinValue(screenToNormalized(x));
        } else if (1 == this.mPressedThumb) {
            setNormalizedMaxValue(screenToNormalized(x));
        } else if (2 == this.mPressedThumb) {
            double delta = screenToNormalized(x) - screenToNormalized(this.mLastX);
            if (this.mNormalizedMaxValue + delta <= 1.0d && this.mNormalizedMinValue + delta >= 0.0d) {
                setNormalizedMaxValue(this.mNormalizedMaxValue + delta);
                setNormalizedMinValue(this.mNormalizedMinValue + delta);
            }
        }
        this.mLastX = x;
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    @Override // android.view.View
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = HttpResponseCode.OK;
        if (View.MeasureSpec.getMode(widthMeasureSpec) != 0) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = this.mThumbImageLeft.getIntrinsicHeight();
        if (View.MeasureSpec.getMode(heightMeasureSpec) != 0) {
            height = View.MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override // android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.mBackgroundColor);
        this.mRectF.set(0.0f, this.mTopBottomPadding, normalizedToScreen(this.mNormalizedMinValue), getHeight() - this.mTopBottomPadding);
        canvas.drawRect(this.mRectF, this.mPaint);
        this.mRectF.set(normalizedToScreen(this.mNormalizedMaxValue), this.mTopBottomPadding, getWidth(), getHeight() - this.mTopBottomPadding);
        canvas.drawRect(this.mRectF, this.mPaint);
        this.mRectF.left = normalizedToScreen(this.mNormalizedMinValue);
        this.mRectF.right = normalizedToScreen(this.mNormalizedMaxValue);
        this.mPaint.setColor(this.mPrimaryColor);
        canvas.drawRect(this.mRectF, this.mPaint);
        this.mPaint.setAlpha(255);
        float left = normalizedToScreen(this.mNormalizedMinValue) - this.mThumbHalfWidth;
        float right = normalizedToScreen(this.mNormalizedMaxValue) - this.mThumbHalfWidth;
        this.mScrubRect.set((int) left, 0, (int) ((2.0f * this.mThumbHalfWidth) + left), getHeight());
        this.mThumbImageLeft.setBounds(this.mScrubRect);
        this.mThumbImageLeft.draw(canvas);
        this.mScrubRect.offset((int) (right - left), 0);
        this.mThumbImageRight.setBounds(this.mScrubRect);
        this.mThumbImageRight.draw(canvas);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", this.mNormalizedMinValue);
        bundle.putDouble("MAX", this.mNormalizedMaxValue);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcel) {
        Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        this.mNormalizedMinValue = bundle.getDouble("MIN");
        this.mNormalizedMaxValue = bundle.getDouble("MAX");
    }

    private int evalPressedThumb(float touchX) {
        boolean minThumbPressed = isInThumbRange(touchX, this.mNormalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, this.mNormalizedMaxValue);
        boolean isInCenter = isInCenter(touchX, this.mNormalizedMinValue, this.mNormalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            return touchX / ((float) getWidth()) > 0.5f ? 0 : 1;
        }
        if (minThumbPressed) {
            return 0;
        }
        if (maxThumbPressed) {
            return 1;
        }
        if (!isInCenter) {
            return -1;
        }
        return 2;
    }

    private boolean isInCenter(float touchX, double normalizedMinValue, double normalizedMaxValue) {
        return normalizedToScreen(normalizedMinValue) < touchX && normalizedToScreen(normalizedMaxValue) > touchX;
    }

    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= this.mThumbDragWidth;
    }

    public void setNormalizedMinValue(double value) {
        this.mNormalizedMinValue = Math.max(0.0d, Math.min(1.0d, Math.min(value, this.mNormalizedMaxValue)));
        double diff = (this.mNormalizedMaxValue - this.mNormalizedMinValue) - this.mNormalizedMaxPercentage;
        if (diff > 0.0d) {
            this.mNormalizedMinValue += diff;
        }
        invalidate();
    }

    public void forceResetNormalizedValues(double min, double max) {
        this.mNormalizedMinValue = Math.max(0.0d, Math.min(1.0d, Math.min(min, this.mNormalizedMaxValue)));
        this.mNormalizedMaxValue = Math.max(0.0d, Math.min(1.0d, Math.max(max, this.mNormalizedMinValue)));
        invalidate();
    }

    public void setNormalizedMaxValue(double value) {
        this.mNormalizedMaxValue = Math.max(0.0d, Math.min(1.0d, Math.max(value, this.mNormalizedMinValue)));
        double diff = (this.mNormalizedMaxValue - this.mNormalizedMinValue) - this.mNormalizedMaxPercentage;
        if (diff > 0.0d) {
            this.mNormalizedMaxValue -= diff;
        }
        invalidate();
    }

    private int normalizedToValue(double normalized) {
        return (int) (this.mAbsoluteMinValuePrim + ((this.mAbsoluteMaxValuePrim - this.mAbsoluteMinValuePrim) * normalized));
    }

    private double valueToNormalized(Integer value) {
        if (0.0d == this.mAbsoluteMaxValuePrim - this.mAbsoluteMinValuePrim) {
            return 0.0d;
        }
        return (value.doubleValue() - this.mAbsoluteMinValuePrim) / (this.mAbsoluteMaxValuePrim - this.mAbsoluteMinValuePrim);
    }

    private float normalizedToScreen(double normalizedCoord) {
        return (float) (this.mPadding + ((getWidth() - this.mDoublePadding) * normalizedCoord));
    }

    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= this.mDoublePadding) {
            return 0.0d;
        }
        double result = (screenCoord - this.mPadding) / (width - this.mDoublePadding);
        return Math.min(1.0d, Math.max(0.0d, result));
    }

    public void setAbsoluteMaxValue(Integer maxVal) {
        this.mAbsoluteMaxValuePrim = maxVal.doubleValue();
    }
}
