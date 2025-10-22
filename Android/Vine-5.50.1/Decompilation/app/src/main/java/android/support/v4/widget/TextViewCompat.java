package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.TextView;

/* loaded from: classes.dex */
public final class TextViewCompat {
    static final TextViewCompatImpl IMPL;

    interface TextViewCompatImpl {
        int getMaxLines(TextView textView);

        int getMinLines(TextView textView);

        void setCompoundDrawablesRelative(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int i, int i2, int i3, int i4);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4);

        void setTextAppearance(TextView textView, int i);
    }

    private TextViewCompat() {
    }

    static class BaseTextViewCompatImpl implements TextViewCompatImpl {
        BaseTextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            textView.setCompoundDrawables(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMaxLines(TextView textView) {
            return TextViewCompatDonut.getMaxLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMinLines(TextView textView) {
            return TextViewCompatDonut.getMinLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setTextAppearance(TextView textView, int resId) {
            TextViewCompatDonut.setTextAppearance(textView, resId);
        }
    }

    static class JbTextViewCompatImpl extends BaseTextViewCompatImpl {
        JbTextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMaxLines(TextView textView) {
            return TextViewCompatJb.getMaxLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMinLines(TextView textView) {
            return TextViewCompatJb.getMinLines(textView);
        }
    }

    static class JbMr1TextViewCompatImpl extends JbTextViewCompatImpl {
        JbMr1TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }
    }

    static class JbMr2TextViewCompatImpl extends JbMr1TextViewCompatImpl {
        JbMr2TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }
    }

    static class Api23TextViewCompatImpl extends JbMr2TextViewCompatImpl {
        Api23TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setTextAppearance(TextView textView, int resId) {
            TextViewCompatApi23.setTextAppearance(textView, resId);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new Api23TextViewCompatImpl();
            return;
        }
        if (version >= 18) {
            IMPL = new JbMr2TextViewCompatImpl();
            return;
        }
        if (version >= 17) {
            IMPL = new JbMr1TextViewCompatImpl();
        } else if (version >= 16) {
            IMPL = new JbTextViewCompatImpl();
        } else {
            IMPL = new BaseTextViewCompatImpl();
        }
    }

    public static void setCompoundDrawablesRelative(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
        IMPL.setCompoundDrawablesRelative(textView, start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    public static int getMaxLines(TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static int getMinLines(TextView textView) {
        return IMPL.getMinLines(textView);
    }

    public static void setTextAppearance(TextView textView, int resId) {
        IMPL.setTextAppearance(textView, resId);
    }
}
