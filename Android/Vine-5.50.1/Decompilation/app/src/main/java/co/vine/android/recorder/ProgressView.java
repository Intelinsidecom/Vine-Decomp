package co.vine.android.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public abstract class ProgressView extends View {
    private static int DEFAULT_PRIMARY_COLOR = -1;
    protected final Paint mPaint;
    private int mPrimaryColor;
    private volatile float mProgressRatio;
    private int mSelectedColor;
    private volatile float mSelectedEndRatio;
    private volatile float mSelectedStartRatio;

    protected abstract void drawProgress(Canvas canvas, float f, float f2, int i);

    public static void initDefaultPirmaryColor(int color) {
        DEFAULT_PRIMARY_COLOR = color;
    }

    public ProgressView(Context context) {
        super(context);
        this.mPaint = init();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = init();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPaint = init();
    }

    private Paint init() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        this.mPrimaryColor = DEFAULT_PRIMARY_COLOR != -1 ? getResources().getColor(DEFAULT_PRIMARY_COLOR) : ViewCompat.MEASURED_STATE_MASK;
        return paint;
    }

    public void setColor(int color) {
        this.mPrimaryColor = color;
    }

    public void setSelectedColor(int color) {
        this.mSelectedColor = color;
    }

    public void setSelectedSection(float start, float end) {
        if (this.mSelectedEndRatio < this.mSelectedStartRatio) {
            throw new IllegalArgumentException("End " + end + " < Start " + start);
        }
        this.mSelectedStartRatio = start;
        this.mSelectedEndRatio = end;
        postInvalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mProgressRatio > 0.0f) {
            if (this.mSelectedStartRatio > 0.0f) {
                drawProgress(canvas, 0.0f, this.mSelectedStartRatio, this.mPrimaryColor);
                drawProgress(canvas, this.mSelectedStartRatio, this.mSelectedEndRatio, this.mSelectedColor);
                if (this.mSelectedEndRatio < this.mProgressRatio) {
                    drawProgress(canvas, this.mSelectedEndRatio, this.mProgressRatio, this.mPrimaryColor);
                    return;
                }
                return;
            }
            if (this.mSelectedStartRatio == 0.0f && this.mSelectedEndRatio != 0.0f) {
                drawProgress(canvas, 0.0f, this.mSelectedEndRatio, this.mSelectedColor);
                if (this.mSelectedEndRatio < this.mProgressRatio) {
                    drawProgress(canvas, this.mSelectedEndRatio, this.mProgressRatio, this.mPrimaryColor);
                    return;
                }
                return;
            }
            drawProgress(canvas, 0.0f, this.mProgressRatio, this.mPrimaryColor);
        }
    }

    public void setProgressRatio(float ratio) {
        if (ratio > 1.0f) {
            ratio = 1.0f;
        }
        this.mProgressRatio = ratio;
        postInvalidate();
    }

    public float getProgressRatio() {
        return this.mProgressRatio;
    }
}
