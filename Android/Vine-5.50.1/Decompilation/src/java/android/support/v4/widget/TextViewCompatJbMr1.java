package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/* loaded from: classes2.dex */
class TextViewCompatJbMr1 {
    TextViewCompatJbMr1() {
    }

    public static void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        Drawable drawable = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawables(drawable, top, start, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        Drawable drawable = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, top, start, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        int i = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(i, top, start, bottom);
    }
}
