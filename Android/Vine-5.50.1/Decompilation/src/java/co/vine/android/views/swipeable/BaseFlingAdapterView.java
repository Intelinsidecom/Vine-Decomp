package co.vine.android.views.swipeable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

/* loaded from: classes.dex */
abstract class BaseFlingAdapterView extends AdapterView {
    private int heightMeasureSpec;
    private int widthMeasureSpec;

    public BaseFlingAdapterView(Context context) {
        super(context);
    }

    public BaseFlingAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.AdapterView
    public void setSelection(int i) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

    public int getWidthMeasureSpec() {
        return this.widthMeasureSpec;
    }

    public int getHeightMeasureSpec() {
        return this.heightMeasureSpec;
    }
}
