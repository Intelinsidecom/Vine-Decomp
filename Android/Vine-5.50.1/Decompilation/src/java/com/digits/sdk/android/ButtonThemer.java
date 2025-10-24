package com.digits.sdk.android;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes.dex */
class ButtonThemer {
    private static int[][] focussedOrPressedButEnabled = {new int[]{android.R.attr.state_focused, android.R.attr.state_enabled}, new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}};
    private final Resources resources;

    public ButtonThemer(Resources resources) {
        this.resources = resources;
    }

    @TargetApi(16)
    void setBackgroundAccentColor(View view, int accentColor) {
        StateListDrawable background = new StateListDrawable();
        float radius = TypedValue.applyDimension(1, 5.0f, this.resources.getDisplayMetrics());
        GradientDrawable tmp = new GradientDrawable();
        tmp.setCornerRadius(radius);
        tmp.setColor(getPressedColor(accentColor));
        int[][] arr$ = focussedOrPressedButEnabled;
        for (int[] state : arr$) {
            background.addState(state, tmp);
        }
        GradientDrawable tmp2 = new GradientDrawable();
        tmp2.setColor(accentColor);
        tmp2.setCornerRadius(radius);
        background.addState(StateSet.WILD_CARD, tmp2);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    @TargetApi(16)
    void setBackgroundAccentColorInverse(View view, int accentColor) {
        StateListDrawable background = new StateListDrawable();
        float radius = TypedValue.applyDimension(1, 5.0f, this.resources.getDisplayMetrics());
        float strokeWidth = TypedValue.applyDimension(1, 2.0f, this.resources.getDisplayMetrics());
        GradientDrawable tmp = new GradientDrawable();
        tmp.setCornerRadius(radius);
        tmp.setStroke((int) strokeWidth, getPressedColor(accentColor));
        int[][] arr$ = focussedOrPressedButEnabled;
        for (int[] state : arr$) {
            background.addState(state, tmp);
        }
        GradientDrawable tmp2 = new GradientDrawable();
        tmp2.setCornerRadius(radius);
        tmp2.setStroke((int) strokeWidth, getDisabledColor(accentColor));
        background.addState(new int[]{-16842910}, tmp2);
        GradientDrawable tmp3 = new GradientDrawable();
        tmp3.setCornerRadius(radius);
        tmp3.setStroke((int) strokeWidth, accentColor);
        background.addState(StateSet.WILD_CARD, tmp3);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    private int getPressedColor(int accentColor) {
        return ThemeUtils.isLightColor(accentColor) ? ThemeUtils.calculateOpacityTransform(0.2d, ViewCompat.MEASURED_STATE_MASK, accentColor) : ThemeUtils.calculateOpacityTransform(0.2d, -1, accentColor);
    }

    private int getDisabledColor(int accentColor) {
        return ThemeUtils.isLightColor(accentColor) ? ThemeUtils.calculateOpacityTransform(0.6d, ViewCompat.MEASURED_STATE_MASK, accentColor) : ThemeUtils.calculateOpacityTransform(0.6d, -1, accentColor);
    }

    void setTextAccentColor(TextView view, int accentColor) {
        int enabledColor = getTextColor(accentColor);
        int disabledColor = getDisabledColor(enabledColor);
        int[][] states = {new int[]{-16842910}, StateSet.WILD_CARD};
        int[] colors = {disabledColor, enabledColor};
        ColorStateList stateList = new ColorStateList(states, colors);
        view.setTextColor(stateList);
    }

    void setTextAccentColorInverse(TextView view, int accentColor) {
        int pressedColor = getPressedColor(accentColor);
        int disabledColor = getDisabledColor(accentColor);
        int[][] states = {new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, new int[]{android.R.attr.state_focused, android.R.attr.state_enabled}, new int[]{-16842910}, StateSet.WILD_CARD};
        int[] colors = {pressedColor, pressedColor, disabledColor, accentColor};
        ColorStateList stateList = new ColorStateList(states, colors);
        view.setTextColor(stateList);
    }

    int getTextColor(int accentColor) {
        return ThemeUtils.isLightColor(accentColor) ? this.resources.getColor(R.color.dgts__text_dark) : this.resources.getColor(R.color.dgts__text_light);
    }

    int getTextColorInverse(int accentColor) {
        return accentColor;
    }

    @TargetApi(21)
    void disableDropShadow(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setStateListAnimator(null);
            view.setElevation(0.0f);
        }
    }
}
