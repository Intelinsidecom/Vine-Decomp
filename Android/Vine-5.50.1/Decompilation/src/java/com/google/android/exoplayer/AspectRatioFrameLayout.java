package com.google.android.exoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public final class AspectRatioFrameLayout extends FrameLayout {
    private float videoAspectRatio;

    public AspectRatioFrameLayout(Context context) {
        super(context);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(float widthHeightRatio) {
        if (this.videoAspectRatio != widthHeightRatio) {
            this.videoAspectRatio = widthHeightRatio;
            requestLayout();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.videoAspectRatio != 0.0f) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            float viewAspectRatio = width / height;
            float aspectDeformation = (this.videoAspectRatio / viewAspectRatio) - 1.0f;
            if (Math.abs(aspectDeformation) > 0.01f) {
                if (aspectDeformation > 0.0f) {
                    height = (int) (width / this.videoAspectRatio);
                } else {
                    width = (int) (height * this.videoAspectRatio);
                }
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
            }
        }
    }
}
