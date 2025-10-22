package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.Settings;

/* loaded from: classes.dex */
public class ColorPicker extends ViewGroup implements View.OnClickListener {
    private int mCircleWidth;
    private ColorClickListener mColorClickListener;
    private int mLineCount;
    private int mMinPadding;
    private ColorPickerCircle mSelected;

    public interface ColorClickListener {
        void onColorClick(int i);
    }

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources res = context.getResources();
        this.mMinPadding = res.getDimensionPixelOffset(R.dimen.color_picker_min_padding);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = Settings.PROFILE_BACKGROUND_COLORS.length;
        int lineCount = count / 2;
        this.mLineCount = lineCount;
        int thisWidth = getMeasuredWidth();
        int eachWidth = (thisWidth / lineCount) - (this.mMinPadding * 2);
        int height = (eachWidth * 2) + (this.mMinPadding * 4);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, View.MeasureSpec.makeMeasureSpec(eachWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(eachWidth, 1073741824));
        }
        this.mCircleWidth = eachWidth;
        setMeasuredDimension(thisWidth, height);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = this.mMinPadding * 2;
        int width = getMeasuredWidth();
        int leftPadding = (width - (this.mLineCount * (this.mCircleWidth + this.mMinPadding))) / 2;
        int left = leftPadding;
        boolean newline = false;
        for (int i = 0; i < getChildCount(); i++) {
            ColorPickerCircle circle = (ColorPickerCircle) getChildAt(i);
            circle.layout(left, top, this.mCircleWidth + left, this.mCircleWidth + top);
            circle.postIndexChange(i);
            circle.setOnClickListener(this);
            left += this.mCircleWidth + this.mMinPadding;
            if (i >= this.mLineCount - 1 && !newline) {
                top += this.mCircleWidth + this.mMinPadding;
                left = leftPadding;
                newline = true;
            }
        }
    }

    public void setOnColorClickListener(ColorClickListener ccl) {
        this.mColorClickListener = ccl;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        ColorPickerCircle c = (ColorPickerCircle) v;
        if (this.mSelected != null) {
            this.mSelected.setSelected(false);
        }
        this.mSelected = c;
        this.mSelected.setSelected(true);
        if (this.mColorClickListener != null) {
            this.mColorClickListener.onColorClick(c.getIndex());
        }
    }

    public void setColorIndex(int index) {
        if (index >= 0 && index < getChildCount()) {
            for (int i = 0; i < getChildCount(); i++) {
                if (index == i) {
                    View child = getChildAt(index);
                    child.setSelected(true);
                    this.mSelected = (ColorPickerCircle) child;
                } else {
                    getChildAt(i).setSelected(false);
                }
            }
        }
    }
}
