package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import co.vine.android.R;
import co.vine.android.util.SystemUtil;

/* loaded from: classes.dex */
public class AnimatingCircle extends View {
    private static final double COSINE_45_DEGREES = Math.cos(0.8377580642700195d);
    private int mBackgroundColor;
    private final Paint mClearPaint;
    private float mHypoteneuse;
    private boolean mIsAnimating;
    private AnimatingCircleListener mListener;
    private float mNewRadius;
    private final Paint mPaint;
    private final int mRadius;
    private Runnable mRunnable;
    private float mStepDistance;
    private int mXMargin;
    private final float mXOffset;

    public interface AnimatingCircleListener {
        void onAnimationEnd();

        void onAnimationRepeat();

        void onAnimationStart();
    }

    public AnimatingCircle(Context context) {
        this(context, null);
    }

    public AnimatingCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatingCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatingCircle, defStyle, 0);
        this.mBackgroundColor = a.getColor(0, res.getColor(R.color.draft_record_bg));
        this.mRadius = a.getDimensionPixelOffset(1, res.getDimensionPixelOffset(R.dimen.rect_circle_radius));
        a.recycle();
        this.mPaint = new Paint();
        this.mClearPaint = new Paint();
        this.mClearPaint.setAntiAlias(true);
        this.mClearPaint.setDither(true);
        this.mClearPaint.setFilterBitmap(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Point size = SystemUtil.getDisplaySize(context);
        int width = size.x;
        int height = size.y;
        int determiningLength = Math.max(width, height);
        int adjacent = determiningLength / 2;
        float hypotenuse = adjacent / ((float) COSINE_45_DEGREES);
        float f = hypotenuse / this.mRadius;
        this.mHypoteneuse = hypotenuse;
        this.mXOffset = res.getDimensionPixelOffset(R.dimen.animating_circle_left_offset);
        this.mXMargin = TypedValue.complexToDimensionPixelOffset(6, res.getDisplayMetrics());
        this.mRunnable = new AnimationRunnable();
        float animationDistance = this.mHypoteneuse - this.mRadius;
        this.mStepDistance = (animationDistance / 250.0f) * 5.0f;
        this.mIsAnimating = false;
        setLayerType(1, null);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mNewRadius == 0.0f) {
            this.mNewRadius = this.mRadius;
        }
        this.mPaint.setColor(this.mBackgroundColor);
        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth() - this.mXMargin, getMeasuredHeight(), this.mPaint);
        canvas.drawCircle((getMeasuredWidth() / 2) + this.mXOffset, getMeasuredHeight() / 2, this.mNewRadius, this.mClearPaint);
    }

    public void startDefaultAnimation() {
        if (!this.mIsAnimating) {
            this.mIsAnimating = true;
            this.mListener.onAnimationStart();
        } else {
            this.mListener.onAnimationRepeat();
        }
        if (this.mNewRadius < this.mHypoteneuse) {
            Handler handler = getHandler();
            if (handler != null) {
                handler.removeCallbacks(this.mRunnable);
                handler.postDelayed(this.mRunnable, 5L);
                return;
            }
            return;
        }
        this.mIsAnimating = false;
        this.mListener.onAnimationEnd();
    }

    public void setAnimationListener(AnimatingCircleListener listener) {
        this.mListener = listener;
    }

    private class AnimationRunnable implements Runnable {
        private AnimationRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AnimatingCircle.this.mNewRadius += AnimatingCircle.this.mStepDistance;
            AnimatingCircle.this.invalidate();
            AnimatingCircle.this.startDefaultAnimation();
        }
    }
}
