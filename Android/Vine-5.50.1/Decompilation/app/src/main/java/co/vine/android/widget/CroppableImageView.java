package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class CroppableImageView extends MultiTouchImageView {
    private int mCropRectPadding;
    private int mCropType;
    private Paint mShadowPaint;
    private RectF mShadowRect;
    private Paint mStrokePaint;

    private void init(int strokeColor, int shadowColor) {
        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint = strokePaint;
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(shadowColor);
        this.mShadowPaint = shadowPaint;
        this.mShadowRect = new RectF();
        this.mCropType = 0;
    }

    public CroppableImageView(Context context) {
        super(context);
        Resources res = getResources();
        init(res.getColor(R.color.solid_white), res.getColor(R.color.translucent_black));
    }

    public CroppableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.croppableImageViewStyle);
    }

    public CroppableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CroppableImageView, defStyle, 0);
        int cropRectPadding = a.getDimensionPixelSize(0, 0);
        if (cropRectPadding > 0) {
            this.mCropRectPadding = cropRectPadding;
        }
        Resources res = getResources();
        int cropRectStrokeColor = a.getColor(1, res.getColor(R.color.solid_white));
        int shadowColor = a.getColor(2, res.getColor(R.color.translucent_black));
        a.recycle();
        init(cropRectStrokeColor, shadowColor);
    }

    public void setCropType(int type) {
        if (this.mCropType != type) {
            this.mCropType = type;
            invalidate();
        }
    }

    public void setCropPadding(int padding, int type) {
        int rectWidth;
        int rectHeight;
        this.mCropType = type;
        this.mCropRectPadding = padding;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float imageAr = this.mBitmapWidth / this.mBitmapHeight;
        float viewAr = width / height;
        if (viewAr > imageAr) {
            rectHeight = height - ((int) (padding / imageAr));
            float scale = this.mBitmapHeight / height;
            rectWidth = ((int) (this.mBitmapWidth / scale)) - padding;
        } else {
            rectWidth = width - padding;
            float scale2 = this.mBitmapWidth / width;
            rectHeight = ((int) (this.mBitmapHeight / scale2)) - ((int) (padding / imageAr));
        }
        if (type == 0) {
            rectHeight = rectWidth / 2;
        } else if (type == 2) {
            if (rectHeight < rectWidth) {
                rectWidth = rectHeight;
            } else {
                rectHeight = rectWidth;
            }
        }
        setTransformDimensions(rectWidth, rectHeight, ImageView.ScaleType.CENTER_CROP, false);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = this.mTransformBounds;
        canvas.drawRect(rect, this.mStrokePaint);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint shadowPaint = this.mShadowPaint;
        RectF shadowRect = this.mShadowRect;
        shadowRect.set(0.0f, rect.top, rect.left, rect.bottom);
        canvas.drawRect(shadowRect, shadowPaint);
        shadowRect.set(0.0f, 0.0f, width, rect.top);
        canvas.drawRect(shadowRect, shadowPaint);
        shadowRect.set(rect.right, rect.top, width, rect.bottom);
        canvas.drawRect(shadowRect, shadowPaint);
        shadowRect.set(0.0f, rect.bottom, width, height);
        canvas.drawRect(shadowRect, shadowPaint);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // co.vine.android.widget.MultiTouchImageView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setCropPadding(this.mCropRectPadding, this.mCropType);
    }

    public RectF getCropRect() {
        RectF rect = new RectF(this.mTransformBounds);
        Matrix invertedMatrix = new Matrix();
        this.mMatrix.invert(invertedMatrix);
        invertedMatrix.mapRect(rect);
        return rect;
    }
}
