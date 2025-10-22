package co.vine.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class MultiTouchImageView extends ImageView {
    protected float mBitmapHeight;
    protected float mBitmapWidth;
    private float mCurrentMinScale;
    private int mGesture;
    private float mLastDownX;
    private float mLastDownY;
    private float mLastZoomDistance;
    protected final Matrix mMatrix;
    private final RectF mMatrixBounds;
    private final float[] mMatrixValues;
    private float mMidTouchX;
    private float mMidTouchY;
    private final RectF mStartBounds;
    private float mTotalScaleFactor;
    protected RectF mTransformBounds;
    private float mViewHeight;
    private float mViewWidth;

    public MultiTouchImageView(Context context) {
        super(context);
        this.mMatrix = new Matrix();
        this.mLastZoomDistance = 1.0f;
        this.mTotalScaleFactor = 1.0f;
        this.mCurrentMinScale = 1.0f;
        this.mStartBounds = new RectF();
        this.mMatrixBounds = new RectF();
        this.mMatrixValues = new float[9];
        initMultiTouchImageView();
    }

    public MultiTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMatrix = new Matrix();
        this.mLastZoomDistance = 1.0f;
        this.mTotalScaleFactor = 1.0f;
        this.mCurrentMinScale = 1.0f;
        this.mStartBounds = new RectF();
        this.mMatrixBounds = new RectF();
        this.mMatrixValues = new float[9];
        initMultiTouchImageView();
    }

    public MultiTouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMatrix = new Matrix();
        this.mLastZoomDistance = 1.0f;
        this.mTotalScaleFactor = 1.0f;
        this.mCurrentMinScale = 1.0f;
        this.mStartBounds = new RectF();
        this.mMatrixBounds = new RectF();
        this.mMatrixValues = new float[9];
        initMultiTouchImageView();
    }

    void initMultiTouchImageView() {
        this.mGesture = 0;
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mViewWidth = getMeasuredWidth();
        this.mViewHeight = getMeasuredHeight();
        if (changed) {
            setTransformDimensions(this.mViewWidth, this.mViewHeight, ImageView.ScaleType.FIT_CENTER, true);
        }
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (bitmap != null) {
            this.mBitmapWidth = bitmap.getWidth();
            this.mBitmapHeight = bitmap.getHeight();
            setTransformDimensions(this.mViewWidth, this.mViewHeight, ImageView.ScaleType.FIT_CENTER, true);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                if (this.mTotalScaleFactor > 1.0f) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (this.mGesture == 0) {
                    this.mLastDownX = event.getX();
                    this.mLastDownY = event.getY();
                    this.mGesture = 1;
                }
                return true;
            case 1:
                if (this.mGesture == 1) {
                    resetGesture();
                }
                return true;
            case 2:
                if (this.mGesture == 1) {
                    float evX = event.getX();
                    float evY = event.getY();
                    float deltaX = evX - this.mLastDownX;
                    float deltaY = evY - this.mLastDownY;
                    float leftBound = this.mMatrixBounds.left;
                    float rightBound = this.mMatrixBounds.right;
                    float topBound = this.mMatrixBounds.top;
                    float bottomBound = this.mMatrixBounds.bottom;
                    RectF panBounds = this.mTransformBounds;
                    float panLeft = panBounds.left;
                    float panTop = panBounds.top;
                    float panRight = panBounds.right;
                    float panBottom = panBounds.bottom;
                    boolean directionDown = this.mLastDownY > evY;
                    boolean directionRight = this.mLastDownX > evX;
                    if (directionRight) {
                        if (rightBound > panRight && rightBound + deltaX < panRight) {
                            deltaX = panRight - rightBound;
                        } else if (rightBound <= panRight) {
                            deltaX = 0.0f;
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else if (leftBound < panLeft && leftBound + deltaX > panLeft) {
                        deltaX = panLeft - leftBound;
                    } else if (leftBound >= panLeft) {
                        deltaX = 0.0f;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    if (directionDown) {
                        if (bottomBound > panBottom && bottomBound + deltaY < panBottom) {
                            deltaY = panBottom - bottomBound;
                        } else if (bottomBound <= panBottom) {
                            deltaY = 0.0f;
                        }
                    } else if (topBound < panTop && topBound + deltaY > panTop) {
                        deltaY = panTop - topBound;
                    } else if (topBound >= panTop) {
                        deltaY = 0.0f;
                    }
                    if (deltaX != 0.0f || deltaY != 0.0f) {
                        this.mMatrix.postTranslate(deltaX, deltaY);
                        this.mMatrixBounds.offset(deltaX, deltaY);
                        setImageMatrix(this.mMatrix);
                    }
                    this.mLastDownX = evX;
                    this.mLastDownY = evY;
                } else if (this.mGesture == 2) {
                    float touchDistance = touchDistance(event);
                    float scaleRatio = touchDistance / this.mLastZoomDistance;
                    this.mTotalScaleFactor *= scaleRatio;
                    RectF bounds = updateBounds();
                    if (bounds.left < 0.0f && bounds.top < 0.0f && bounds.right > this.mViewWidth && bounds.bottom > this.mViewHeight) {
                        this.mMatrix.postScale(scaleRatio, scaleRatio, this.mMidTouchX, this.mMidTouchY);
                    } else {
                        this.mMatrix.postScale(scaleRatio, scaleRatio, this.mViewWidth / 2.0f, this.mViewHeight / 2.0f);
                    }
                    this.mLastZoomDistance = touchDistance;
                    setImageMatrix(this.mMatrix);
                } else {
                    return super.onTouchEvent(event);
                }
                return true;
            case 3:
                if (this.mGesture == 2) {
                    resetScale();
                }
                resetGesture();
                return true;
            case 4:
            default:
                return super.onTouchEvent(event);
            case 5:
                if (this.mGesture == 0 || this.mGesture == 1) {
                    this.mMidTouchX = (event.getX(0) + event.getX(1)) / 2.0f;
                    this.mMidTouchY = (event.getY(0) + event.getY(1)) / 2.0f;
                    this.mLastZoomDistance = touchDistance(event);
                    this.mGesture = 2;
                }
                return true;
            case 6:
                if (this.mGesture == 2) {
                    resetScale();
                    resetGesture();
                }
                return true;
        }
    }

    private void resetScale() {
        RectF bounds = updateBounds();
        RectF transformBounds = this.mTransformBounds;
        if (bounds.left > transformBounds.left || bounds.top > transformBounds.top || bounds.right < transformBounds.right || bounds.bottom < transformBounds.bottom) {
            float centerX = transformBounds.left + (transformBounds.width() / 2.0f);
            float centerY = transformBounds.top + (transformBounds.height() / 2.0f);
            float imageCenterX = bounds.left + (bounds.width() / 2.0f);
            float imageCenterY = bounds.top + (bounds.height() / 2.0f);
            float deltaX = centerX - imageCenterX;
            float deltaY = centerY - imageCenterY;
            if (deltaX != 0.0f || deltaY != 0.0f) {
                this.mMatrix.postTranslate(deltaX, deltaY);
                this.mMatrixBounds.offset(deltaX, deltaY);
            }
            float currentMinScale = this.mCurrentMinScale;
            if (this.mTotalScaleFactor < currentMinScale) {
                float scaleRatio = currentMinScale / this.mTotalScaleFactor;
                this.mTotalScaleFactor = currentMinScale;
                this.mMatrix.postScale(scaleRatio, scaleRatio, this.mViewWidth / 2.0f, this.mViewHeight / 2.0f);
                float bitmapWidth = this.mBitmapWidth;
                float bitmapHeight = this.mBitmapHeight;
                float tbWidth = this.mTransformBounds.width();
                float tbHeight = this.mTransformBounds.height();
                float viewScale = calculateScale(bitmapWidth, tbWidth, bitmapHeight, tbHeight, ImageView.ScaleType.CENTER_CROP);
                RectF newBounds = new RectF(this.mTransformBounds);
                if (((int) (bitmapWidth * viewScale)) > ((int) tbWidth)) {
                    float offset = ((bitmapWidth * viewScale) - tbWidth) / 2.0f;
                    newBounds.left -= offset;
                    newBounds.right += offset;
                }
                if (((int) (bitmapHeight * viewScale)) > ((int) tbHeight)) {
                    float offset2 = ((bitmapHeight * viewScale) - tbHeight) / 2.0f;
                    newBounds.top -= offset2;
                    newBounds.bottom += offset2;
                }
                this.mMatrixBounds.set(newBounds);
            }
            setImageMatrix(this.mMatrix);
        }
    }

    private RectF updateBounds() {
        float[] values = this.mMatrixValues;
        this.mMatrix.getValues(values);
        float tX = values[2];
        float tY = values[5];
        float scaledWidth = this.mStartBounds.width() * this.mTotalScaleFactor;
        float scaledHeight = this.mStartBounds.height() * this.mTotalScaleFactor;
        float right = tX + scaledWidth;
        float bottom = tY + scaledHeight;
        this.mMatrixBounds.set(tX, tY, right, bottom);
        return this.mMatrixBounds;
    }

    private void resetGesture() {
        this.mGesture = 0;
        this.mLastDownX = Float.MIN_VALUE;
        this.mLastDownY = Float.MIN_VALUE;
        this.mMidTouchX = Float.MIN_VALUE;
        this.mMidTouchY = Float.MIN_VALUE;
    }

    private float touchDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt((x * x) + (y * y));
    }

    private float calculateScale(float fromWidth, float toWidth, float fromHeight, float toHeight, ImageView.ScaleType scaleType) {
        if (scaleType != ImageView.ScaleType.CENTER_CROP) {
            float scale = Math.min(toWidth / fromWidth, toHeight / fromHeight);
            return scale;
        }
        if (fromWidth > fromHeight) {
            float scale2 = toHeight / fromHeight;
            return scale2;
        }
        float scale3 = toWidth / fromWidth;
        return scale3;
    }

    public void setTransformDimensions(float width, float height, ImageView.ScaleType scaleType, boolean resetImage) {
        float viewWidth = this.mViewWidth;
        float deltaX = (viewWidth - width) / 2.0f;
        float viewHeight = this.mViewHeight;
        float deltaY = (viewHeight - height) / 2.0f;
        float startWidth = this.mStartBounds.width();
        float startHeight = this.mStartBounds.height();
        if (this.mTransformBounds == null) {
            this.mTransformBounds = new RectF(deltaX, deltaY, deltaX + width, deltaY + height);
        } else {
            this.mTransformBounds.set(deltaX, deltaY, deltaX + width, deltaY + height);
        }
        if (startWidth != 0.0f && startHeight != 0.0f) {
            this.mCurrentMinScale = calculateScale(startWidth, width, startHeight, height, scaleType);
        }
        if (resetImage || !this.mMatrixBounds.contains(this.mTransformBounds)) {
            float marginX = 0.0f;
            float marginY = 0.0f;
            float bitmapWidth = this.mBitmapWidth;
            float bitmapHeight = this.mBitmapHeight;
            if (bitmapWidth > 0.0f && bitmapHeight > 0.0f) {
                float initialScale = calculateScale(bitmapWidth, width, bitmapHeight, height, scaleType);
                marginX = (viewWidth - (initialScale * bitmapWidth)) / 2.0f;
                marginY = (viewHeight - (initialScale * bitmapHeight)) / 2.0f;
                Matrix matrix = this.mMatrix;
                matrix.setScale(initialScale, initialScale);
                matrix.postTranslate(marginX, marginY);
                setImageMatrix(matrix);
            }
            this.mStartBounds.set(marginX, marginY, viewWidth - marginX, viewHeight - marginY);
            this.mMatrixBounds.set(this.mStartBounds);
            this.mTotalScaleFactor = 1.0f;
        }
    }
}
