package co.vine.android.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class InlineProgressView extends ProgressView {
    private final RectF mRect;

    public InlineProgressView(Context context) {
        super(context);
        this.mRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public InlineProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public InlineProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // co.vine.android.recorder.ProgressView
    protected void drawProgress(Canvas canvas, float start, float end, int color) {
        this.mPaint.setColor(color);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int radius = height / 2;
        this.mRect.left = width * start;
        this.mRect.right = width * end;
        this.mRect.bottom = height;
        canvas.drawRoundRect(this.mRect, radius, radius, this.mPaint);
    }
}
