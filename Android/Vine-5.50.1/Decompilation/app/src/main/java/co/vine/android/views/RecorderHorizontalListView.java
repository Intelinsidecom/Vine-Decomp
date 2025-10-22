package co.vine.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import co.vine.android.R;

/* loaded from: classes.dex */
public class RecorderHorizontalListView extends HorizontalListView {
    public RecorderHorizontalListView(Context context) {
        super(context);
    }

    public RecorderHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecorderHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // co.vine.android.views.HorizontalListView
    protected float getFillWidthHeightRatio(TypedArray a) {
        return a.getFloat(3, 0.0f);
    }

    @Override // co.vine.android.views.HorizontalListView
    protected int getEdgePadding(TypedArray a) {
        return a.getDimensionPixelSize(2, 0);
    }

    @Override // co.vine.android.views.HorizontalListView
    protected void initializeDividerStyles(TypedArray a) {
        Drawable divider = a.getDrawable(0);
        if (divider != null) {
            setDivider(divider);
        }
        int dividerWidth = a.getDimensionPixelSize(1, 0);
        if (dividerWidth > 0) {
            setDividerWidth(dividerWidth);
        }
    }

    @Override // co.vine.android.views.HorizontalListView
    protected TypedArray getStylingArray(Context context, AttributeSet attrs, int defStyle) {
        if (defStyle == 0) {
            defStyle = R.attr.horizontalListViewStyle;
        }
        return context.obtainStyledAttributes(attrs, R.styleable.HorizontalListView, defStyle, 0);
    }
}
