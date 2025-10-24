package com.twitter.sdk.android.core.internal.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.twitter.sdk.android.core.R;

/* loaded from: classes2.dex */
public class AspectRatioImageView extends ImageView {
    private double aspectRatio;
    private int dimensionToAdjust;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tw__AspectRatioImageView);
        try {
            this.aspectRatio = a.getFloat(R.styleable.tw__AspectRatioImageView_tw__image_aspect_ratio, 1.0f);
            this.dimensionToAdjust = a.getInt(R.styleable.tw__AspectRatioImageView_tw__image_dimension_to_adjust, 0);
        } finally {
            a.recycle();
        }
    }

    public double getAspectRatio() {
        return this.aspectRatio;
    }

    public int getDimensionToAdjust() {
        return this.dimensionToAdjust;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (this.dimensionToAdjust == 0) {
            height = calculateHeight(width, this.aspectRatio);
        } else {
            width = calculateWidth(height, this.aspectRatio);
        }
        setMeasuredDimension(width, height);
    }

    int calculateHeight(int width, double ratio) {
        if (ratio == 0.0d) {
            return 0;
        }
        return (int) Math.round(width / ratio);
    }

    int calculateWidth(int height, double ratio) {
        return (int) Math.round(height * ratio);
    }
}
