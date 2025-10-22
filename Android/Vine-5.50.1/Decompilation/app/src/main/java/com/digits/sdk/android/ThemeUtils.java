package com.digits.sdk.android;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import com.digits.sdk.android.R;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
class ThemeUtils {
    static TypedValue getTypedValueColor(Resources.Theme theme, int colorResId) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(colorResId, typedValue, true);
        if (typedValue.type < 28 || typedValue.type > 31) {
            return null;
        }
        return typedValue;
    }

    @TargetApi(21)
    static int getAccentColor(Resources res, Resources.Theme theme) throws NoSuchFieldException {
        TypedValue typedValue;
        TypedValue typedValue2 = getTypedValueColor(theme, R.attr.dgts__accentColor);
        if (typedValue2 != null) {
            return typedValue2.data;
        }
        if (Build.VERSION.SDK_INT >= 21 && (typedValue = getTypedValueColor(theme, android.R.attr.colorAccent)) != null) {
            return typedValue.data;
        }
        try {
            Field field = R.attr.class.getDeclaredField("colorAccent");
            TypedValue typedValue3 = getTypedValueColor(theme, field.getInt(field.getType()));
            if (typedValue3 != null) {
                return typedValue3.data;
            }
        } catch (Exception e) {
        }
        return res.getColor(R.color.dgts__default_accent);
    }

    static boolean isLightColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        double threshold = (0.21d * r) + (0.72d * g) + (0.07d * b);
        return threshold > 170.0d;
    }

    static int calculateOpacityTransform(double opacity, int overlayColor, int primaryColor) {
        int redPrimary = Color.red(primaryColor);
        int redOverlay = Color.red(overlayColor);
        int greenPrimary = Color.green(primaryColor);
        int greenOverlay = Color.green(overlayColor);
        int bluePrimary = Color.blue(primaryColor);
        int blueOverlay = Color.blue(overlayColor);
        int redCalculated = (int) (((1.0d - opacity) * redPrimary) + (redOverlay * opacity));
        int greenCalculated = (int) (((1.0d - opacity) * greenPrimary) + (greenOverlay * opacity));
        int blueCalculated = (int) (((1.0d - opacity) * bluePrimary) + (blueOverlay * opacity));
        return Color.rgb(redCalculated, greenCalculated, blueCalculated);
    }

    static Drawable getLogoDrawable(Resources.Theme theme) throws Resources.NotFoundException {
        TypedValue typedValue = new TypedValue();
        int[] drawableAttr = {R.attr.dgts__logoDrawable};
        TypedArray a = theme.obtainStyledAttributes(typedValue.data, drawableAttr);
        return a.getDrawable(0);
    }
}
