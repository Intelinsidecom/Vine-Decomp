package com.digits.sdk.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class InvertedStateButton extends StateButton {
    public InvertedStateButton(Context context) {
        this(context, null);
    }

    public InvertedStateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InvertedStateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // com.digits.sdk.android.StateButton
    void initView(Context context) {
        this.accentColor = ThemeUtils.getAccentColor(getResources(), context.getTheme());
        this.buttonThemer = new ButtonThemer(getResources());
        this.buttonThemer.setBackgroundAccentColorInverse(this, this.accentColor);
        this.buttonThemer.setTextAccentColorInverse(this.textView, this.accentColor);
        setImageAccentColor();
        setSpinnerAccentColor();
    }

    @Override // com.digits.sdk.android.StateButton
    int getTextColor() {
        return this.buttonThemer.getTextColorInverse(this.accentColor);
    }

    @Override // com.digits.sdk.android.StateButton
    Drawable getProgressDrawable() {
        return ThemeUtils.isLightColor(this.accentColor) ? getResources().getDrawable(R.drawable.progress_light) : getResources().getDrawable(R.drawable.progress_dark);
    }
}
