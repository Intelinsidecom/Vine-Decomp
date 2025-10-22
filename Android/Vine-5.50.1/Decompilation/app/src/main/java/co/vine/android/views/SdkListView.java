package co.vine.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/* loaded from: classes.dex */
public class SdkListView extends ListView {
    public SdkListView(Context context) {
        super(context);
    }

    public SdkListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SdkListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
