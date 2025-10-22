package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.TintableBackgroundView;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.support.v7.internal.widget.ThemeUtils;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.support.v7.internal.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

/* loaded from: classes.dex */
public class AppCompatButton extends Button implements TintableBackgroundView {
    private static final int[] TINT_ATTRS = {R.attr.background};
    private TintInfo mBackgroundTint;

    public AppCompatButton(Context context) {
        this(context, null);
    }

    public AppCompatButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public AppCompatButton(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        int disabledTextColor;
        ColorStateList tint;
        super(context, attrs, defStyleAttr);
        if (TintManager.SHOULD_BE_USED) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, TINT_ATTRS, defStyleAttr, 0);
            if (a.hasValue(0) && (tint = a.getTintManager().getTintList(a.getResourceId(0, -1))) != null) {
                setSupportBackgroundTintList(tint);
            }
            a.recycle();
        }
        TypedArray a2 = context.obtainStyledAttributes(attrs, android.support.v7.appcompat.R.styleable.AppCompatTextView, defStyleAttr, 0);
        int ap = a2.getResourceId(android.support.v7.appcompat.R.styleable.AppCompatTextView_android_textAppearance, -1);
        a2.recycle();
        if (ap != -1) {
            TypedArray appearance = context.obtainStyledAttributes(ap, android.support.v7.appcompat.R.styleable.TextAppearance);
            if (appearance.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_textAllCaps)) {
                setAllCaps(appearance.getBoolean(android.support.v7.appcompat.R.styleable.TextAppearance_textAllCaps, false));
            }
            appearance.recycle();
        }
        TypedArray a3 = context.obtainStyledAttributes(attrs, android.support.v7.appcompat.R.styleable.AppCompatTextView, defStyleAttr, 0);
        if (a3.hasValue(android.support.v7.appcompat.R.styleable.AppCompatTextView_textAllCaps)) {
            setAllCaps(a3.getBoolean(android.support.v7.appcompat.R.styleable.AppCompatTextView_textAllCaps, false));
        }
        a3.recycle();
        ColorStateList textColors = getTextColors();
        if (textColors != null && !textColors.isStateful()) {
            if (Build.VERSION.SDK_INT < 21) {
                disabledTextColor = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.textColorSecondary);
            } else {
                disabledTextColor = ThemeUtils.getThemeAttrColor(context, R.attr.textColorSecondary);
            }
            setTextColor(ThemeUtils.createDisabledStateList(textColors.getDefaultColor(), disabledTextColor));
        }
    }

    @Override // android.support.v4.view.TintableBackgroundView
    public void setSupportBackgroundTintList(ColorStateList tint) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintList = tint;
        this.mBackgroundTint.mHasTintList = true;
        applySupportBackgroundTint();
    }

    @Override // android.support.v4.view.TintableBackgroundView
    public ColorStateList getSupportBackgroundTintList() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintList;
        }
        return null;
    }

    @Override // android.support.v4.view.TintableBackgroundView
    public void setSupportBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (this.mBackgroundTint == null) {
            this.mBackgroundTint = new TintInfo();
        }
        this.mBackgroundTint.mTintMode = tintMode;
        this.mBackgroundTint.mHasTintMode = true;
        applySupportBackgroundTint();
    }

    @Override // android.support.v4.view.TintableBackgroundView
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        if (this.mBackgroundTint != null) {
            return this.mBackgroundTint.mTintMode;
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        applySupportBackgroundTint();
    }

    private void applySupportBackgroundTint() {
        if (getBackground() != null && this.mBackgroundTint != null) {
            TintManager.tintViewBackground(this, this.mBackgroundTint);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(Button.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Button.class.getName());
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean allCaps) {
        setTransformationMethod(allCaps ? new AllCapsTransformationMethod(getContext()) : null);
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int resId) throws Resources.NotFoundException {
        super.setTextAppearance(context, resId);
        TypedArray appearance = context.obtainStyledAttributes(resId, android.support.v7.appcompat.R.styleable.TextAppearance);
        if (appearance.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(appearance.getBoolean(android.support.v7.appcompat.R.styleable.TextAppearance_textAllCaps, false));
        }
        appearance.recycle();
    }
}
