package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class OverlayImageView extends ImageView {
    private boolean mFramed;
    private int mImageHeight;
    private int mImageWidth;
    private Drawable mOverlayDrawable;

    public OverlayImageView(Context context) {
        super(context);
        init(context, null);
    }

    public OverlayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OverlayImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverlayImageView);
            Drawable d = a.getDrawable(0);
            if (d != null) {
                setOverlayDrawable(d);
            }
            a.recycle();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable d = this.mOverlayDrawable;
        if (d != null) {
            d.draw(canvas);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mOverlayDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int imageWidth = this.mImageWidth;
        int imageHeight = this.mImageHeight;
        if (imageWidth > 0 && imageHeight > 0) {
            int width = getDefaultSize(imageWidth, widthMeasureSpec);
            int height = getDefaultSize(imageHeight, heightMeasureSpec);
            int widthScale = imageWidth * height;
            int heightScale = imageHeight * width;
            if (widthScale > heightScale) {
                height = heightScale / imageWidth;
            } else if (widthScale < heightScale) {
                width = widthScale / imageHeight;
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        setDrawableBounds(this.mOverlayDrawable);
    }

    public void setOverlayDrawable(int resId) {
        setOverlayDrawable(getResources().getDrawable(resId));
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable dr) {
        if (dr == this.mOverlayDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override // android.widget.ImageView
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        this.mFramed = true;
        setDrawableBounds(this.mOverlayDrawable);
        return changed;
    }

    private void setDrawableBounds(Drawable d) {
        if (this.mFramed && d != null) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            d.setBounds(0, 0, width, height);
            invalidate();
        }
    }

    public void setOverlayDrawable(Drawable d) {
        if (this.mOverlayDrawable != d) {
            if (this.mOverlayDrawable != null) {
                this.mOverlayDrawable.setCallback(null);
                unscheduleDrawable(this.mOverlayDrawable);
            }
            this.mOverlayDrawable = d;
            if (d != null) {
                d.setCallback(this);
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
            }
            requestLayout();
        }
    }
}
