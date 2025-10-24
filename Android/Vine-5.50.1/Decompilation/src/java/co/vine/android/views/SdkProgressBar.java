package co.vine.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import co.vine.android.util.CrossConstants;

/* loaded from: classes.dex */
public class SdkProgressBar extends ProgressBar {
    public SdkProgressBar(Context context) {
        super(context);
    }

    public SdkProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SdkProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.view.View
    public void invalidate() {
        if (!CrossConstants.DISABLE_ANIMATIONS) {
            super.invalidate();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void postInvalidate() {
        if (!CrossConstants.DISABLE_ANIMATIONS) {
            super.postInvalidate();
        }
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        if (!CrossConstants.DISABLE_ANIMATIONS) {
            super.onDraw(canvas);
        }
    }
}
