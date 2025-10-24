package com.digits.sdk.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class LogoImageView extends ImageView {
    public LogoImageView(Context context) throws Resources.NotFoundException {
        super(context);
        initImageView(context);
    }

    public LogoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoImageView(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        initImageView(context);
    }

    void initImageView(Context context) throws Resources.NotFoundException {
        Drawable logoDrawable = ThemeUtils.getLogoDrawable(context.getTheme());
        if (logoDrawable != null) {
            setVisibility(0);
            setImageDrawable(logoDrawable);
        } else {
            setVisibility(8);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = (getDrawable().getIntrinsicHeight() * width) / getDrawable().getIntrinsicWidth();
        setMeasuredDimension(width, height);
    }
}
