package co.vine.android.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class RegularProgressView extends ProgressView {
    public RegularProgressView(Context context) {
        super(context);
    }

    public RegularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegularProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // co.vine.android.recorder.ProgressView
    protected void drawProgress(Canvas canvas, float start, float end, int color) {
        this.mPaint.setColor(color);
        int width = getMeasuredWidth();
        canvas.drawRect(start * width, 0.0f, end * width, getMeasuredHeight(), this.mPaint);
    }
}
