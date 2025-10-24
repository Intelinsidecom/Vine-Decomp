package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import co.vine.android.R;

/* loaded from: classes.dex */
public class VineToggleButton extends ToggleButton {
    private ColorFilter mCheckedColorFilter;
    private CompoundButton.OnCheckedChangeListener mListener;
    private ColorFilter mUncheckedColorFilter;

    public VineToggleButton(Context context) {
        super(context);
    }

    public VineToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyStyles(context, attrs);
        initialize();
    }

    public VineToggleButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        applyStyles(context, attrs);
        initialize();
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        this.mListener = listener;
    }

    @Override // android.widget.ToggleButton, android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        ensureStyle(checked, isEnabled());
    }

    public void setCheckedWithoutEvent(boolean checked) {
        CompoundButton.OnCheckedChangeListener cachedListener = this.mListener;
        setOnCheckedChangeListener(null);
        super.setChecked(checked);
        ensureStyle(checked, isEnabled());
        setOnCheckedChangeListener(cachedListener);
    }

    public void setCheckedColorStyle(Integer color) {
        this.mCheckedColorFilter = color == null ? null : new PorterDuffColorFilter(color.intValue(), PorterDuff.Mode.SRC_IN);
        ensureStyle(isChecked(), isEnabled());
    }

    public void setUncheckedColorStyle(Integer color) {
        this.mUncheckedColorFilter = color == null ? null : new PorterDuffColorFilter(color.intValue(), PorterDuff.Mode.SRC_IN);
        ensureStyle(isChecked(), isEnabled());
    }

    private void applyStyles(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VineToggleButton);
        if (attributes.hasValue(0)) {
            int color = attributes.getColor(0, 0);
            this.mCheckedColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        attributes.recycle();
    }

    private void initialize() {
        ensureStyle(isChecked(), isEnabled());
    }

    private void ensureStyle(boolean checked, boolean enabled) {
        Drawable background = getBackground();
        if (background != null) {
            if (checked && enabled) {
                background.setColorFilter(this.mCheckedColorFilter);
            } else {
                background.setColorFilter(this.mUncheckedColorFilter);
            }
        }
    }
}
