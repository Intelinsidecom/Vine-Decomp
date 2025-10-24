package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import co.vine.android.R;
import co.vine.android.util.CrossConstants;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class CounterView extends View implements Runnable {
    private static final int ANIMATION_DURATION_MAX = (int) TimeUnit.NANOSECONDS.convert(150, TimeUnit.MILLISECONDS);
    private static final int ANIMATION_DURATION_MIN = (int) TimeUnit.NANOSECONDS.convert(20, TimeUnit.MILLISECONDS);
    private AnimationMode mAnimationMode;
    private Rect[] mBounds;
    private volatile boolean mCanAnimate;
    private final ArrayList<DigitAnimation> mDigitAnimations;
    private long mDrawingCount;
    private long mExtraCount;
    private int mInterCharacterSpacingX;
    private int mInterCharacterSpacingY;
    private boolean mIsPaused;
    private long mKnownCount;
    private long mKnownCountRefreshTime;
    private final int[] mLOCK;
    private long mLastAnimationTime;
    private int mLastDigitCount;
    private String mLastPrint;
    private long mMaxAnimationSeparation;
    private int mMaxTextHeight;
    private int mMaxTextWidth;
    private long mMinAnimationSeparation;
    private final Paint mPaint;
    private double mVelocityPerMS;

    public void setAnimationMode(AnimationMode animationMode) {
        this.mAnimationMode = animationMode;
    }

    public void setMinAnimationSeparation(long minAnimationSeparation) {
        this.mMinAnimationSeparation = minAnimationSeparation;
    }

    public void setMaxAnimationSeparation(long maxAnimationSeparation) {
        this.mMaxAnimationSeparation = maxAnimationSeparation;
    }

    private static class AnimationMode {
        public final boolean alphaAnimation;
        public final boolean continuousAnimation;
        public final boolean pedometerAnimation;

        public AnimationMode(boolean continuousAnimation, boolean pedometerAnimation, boolean alphaAnimation) {
            this.continuousAnimation = continuousAnimation;
            this.pedometerAnimation = pedometerAnimation;
            this.alphaAnimation = alphaAnimation;
        }
    }

    private class DigitAnimation {
        public long mAnimatingCount;
        public long mAnimationDuration;
        public long mAnimationStartTime;
        public int mCurrentAnimatingToDigit;
        private final double mDivider;
        public int mDrawingDigit;
        public final int mIndexFromRight;
        public boolean mIsAnimating;
        private final double mNextDivider;

        public DigitAnimation(int indexFromRight, double velocityPerMS, long drawingCount) {
            this.mIndexFromRight = indexFromRight;
            this.mDivider = Math.pow(10.0d, this.mIndexFromRight);
            this.mNextDivider = Math.pow(10.0d, this.mIndexFromRight + 1);
            if (this.mIndexFromRight > 0) {
                this.mDrawingDigit = (int) ((drawingCount / this.mDivider) % 10.0d);
            } else {
                this.mDrawingDigit = (int) (drawingCount % 10);
            }
            this.mAnimatingCount = drawingCount;
            if (velocityPerMS > 0.0d && CounterView.this.mAnimationMode.continuousAnimation) {
                if (this.mIndexFromRight > 0) {
                    this.mAnimationDuration = (long) (1.0d / (velocityPerMS / (this.mIndexFromRight * 10)));
                } else {
                    this.mAnimationDuration = (long) (1.0d / velocityPerMS);
                }
            } else {
                this.mAnimationDuration = 2147483647L;
            }
            this.mAnimationDuration *= 1000;
            this.mAnimationDuration = Math.min(this.mAnimationDuration, CounterView.ANIMATION_DURATION_MAX);
            this.mAnimationDuration = Math.max(this.mAnimationDuration, CounterView.ANIMATION_DURATION_MIN);
        }

        public boolean invalidate(long currentTime, long currentCount, boolean shouldAnimate) {
            int currentRealDigit = this.mDrawingDigit;
            if (this.mIsAnimating && currentTime - this.mAnimationStartTime > this.mAnimationDuration) {
                if (this.mIndexFromRight > 0) {
                    currentRealDigit = (int) ((this.mAnimatingCount / this.mDivider) % 10.0d);
                } else {
                    currentRealDigit = (int) (this.mAnimatingCount % 10);
                }
                if (CounterView.this.mAnimationMode.pedometerAnimation) {
                    this.mDrawingDigit = (this.mDrawingDigit + 1) % 10;
                } else {
                    this.mDrawingDigit = this.mCurrentAnimatingToDigit;
                }
                this.mIsAnimating = false;
            }
            boolean shouldAnimateNext = false;
            if ((shouldAnimate || currentRealDigit != this.mDrawingDigit) && !this.mIsAnimating) {
                this.mIsAnimating = true;
                this.mAnimationStartTime = System.nanoTime();
                int currentRest = (int) (this.mAnimatingCount / this.mNextDivider);
                int nextRest = (int) (currentCount / this.mNextDivider);
                shouldAnimateNext = currentRest != nextRest;
                this.mAnimatingCount = currentCount;
                if (CounterView.this.mAnimationMode.pedometerAnimation) {
                    this.mCurrentAnimatingToDigit = (this.mDrawingDigit + 1) % 10;
                } else if (this.mIndexFromRight > 0) {
                    this.mCurrentAnimatingToDigit = (int) ((this.mAnimatingCount / this.mDivider) % 10.0d);
                } else {
                    this.mCurrentAnimatingToDigit = (int) (this.mAnimatingCount % 10);
                }
            }
            return shouldAnimateNext;
        }
    }

    public CounterView(Context context) {
        super(context);
        this.mDigitAnimations = new ArrayList<>();
        this.mAnimationMode = new AnimationMode(false, false, true);
        this.mLOCK = new int[0];
        this.mPaint = init();
        updateTextSizes();
    }

    private Paint init() {
        Paint paint = new Paint(65);
        this.mBounds = new Rect[]{new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect(), new Rect()};
        paint.setTextSize(32.0f);
        paint.setColor(-7829368);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.RIGHT);
        return paint;
    }

    private void updateTextSizes() {
        this.mMaxTextWidth = 0;
        this.mMaxTextHeight = 0;
        for (int i = 0; i < this.mBounds.length; i++) {
            if (i != 10) {
                this.mPaint.getTextBounds(String.valueOf(i), 0, 1, this.mBounds[i]);
            } else {
                this.mPaint.getTextBounds(",", 0, 1, this.mBounds[i]);
            }
            if (this.mMaxTextWidth < this.mBounds[i].width()) {
                this.mMaxTextWidth = this.mBounds[i].width();
            }
            if (this.mMaxTextHeight < this.mBounds[i].height()) {
                this.mMaxTextHeight = this.mBounds[i].height();
            }
        }
        Resources res = getResources();
        this.mInterCharacterSpacingX = res.getDimensionPixelSize(R.dimen.counter_horizontal_text_spacing);
        this.mInterCharacterSpacingY = res.getDimensionPixelSize(R.dimen.counter_vertical_text_spacing);
    }

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDigitAnimations = new ArrayList<>();
        this.mAnimationMode = new AnimationMode(false, false, true);
        this.mLOCK = new int[0];
        this.mPaint = init();
        updateTextSizes();
    }

    public CounterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDigitAnimations = new ArrayList<>();
        this.mAnimationMode = new AnimationMode(false, false, true);
        this.mLOCK = new int[0];
        this.mPaint = init();
        updateTextSizes();
    }

    public void setTypeFace(Typeface typeFace) {
        this.mPaint.setTypeface(typeFace);
        requestLayout();
    }

    public void setTextSize(float textSize) {
        this.mPaint.setTextSize(textSize);
        requestLayout();
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
        requestLayout();
    }

    public void reset() {
        this.mLastDigitCount = 0;
        this.mKnownCount = 0L;
        this.mKnownCountRefreshTime = -1L;
        this.mVelocityPerMS = 0.0d;
        this.mDrawingCount = 0L;
        this.mDigitAnimations.clear();
    }

    public long setKnownCount(long knownCount, double velocityPerMS, long knownCountRefreshTime) {
        if (knownCountRefreshTime > 0) {
            long time = System.currentTimeMillis();
            this.mKnownCount = (long) (knownCount + ((time - knownCountRefreshTime) * velocityPerMS));
            this.mKnownCountRefreshTime = time;
        } else {
            this.mKnownCount = knownCount;
        }
        this.mVelocityPerMS = velocityPerMS;
        synchronized (this.mLOCK) {
            this.mLastDigitCount = 0;
            this.mDigitAnimations.clear();
            invalidateDigitSize(true);
        }
        return this.mKnownCount;
    }

    public long getCount() {
        return (long) ((this.mKnownCountRefreshTime > 0 ? this.mVelocityPerMS * (System.currentTimeMillis() - this.mKnownCountRefreshTime) : 0.0d) + this.mKnownCount);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int x = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        synchronized (this.mLOCK) {
            int length = this.mDigitAnimations.size();
            String num = "";
            for (int i = 0; i < length; i++) {
                if (i >= 3 && i % 3 == 0) {
                    this.mPaint.setAlpha(255);
                    canvas.drawText(",", x, (measuredHeight >> 1) + this.mBounds[10].height() + this.mInterCharacterSpacingY, this.mPaint);
                    x -= this.mBounds[10].width();
                }
                DigitAnimation digitAnimation = this.mDigitAnimations.get(i);
                int digit = digitAnimation.mDrawingDigit;
                String digitText = String.valueOf(digit);
                int y = getIntrinsicHeightForDigit(measuredHeight, digit);
                if (digitAnimation.mIsAnimating) {
                    long diff = System.nanoTime() - digitAnimation.mAnimationStartTime;
                    double progress = diff / digitAnimation.mAnimationDuration;
                    if (diff >= digitAnimation.mAnimationDuration || (digitAnimation.mCurrentAnimatingToDigit == digit && !this.mAnimationMode.pedometerAnimation)) {
                        num = num + digitAnimation.mCurrentAnimatingToDigit;
                        this.mPaint.setAlpha(255);
                        canvas.drawText(String.valueOf(digitAnimation.mCurrentAnimatingToDigit), x, y, this.mPaint);
                    } else {
                        int topY = (int) (y - ((this.mBounds[digit].height() + this.mInterCharacterSpacingY) * progress));
                        if (digit != 0 || i != length - 1 || length == 1) {
                            if (this.mAnimationMode.alphaAnimation) {
                                this.mPaint.setAlpha((int) ((1.0d - progress) * 255.0d));
                            }
                            canvas.drawText(digitText, x, topY, this.mPaint);
                        }
                        num = num + digitText;
                        int botY = this.mBounds[digit].height() + topY + this.mInterCharacterSpacingY;
                        if (this.mAnimationMode.alphaAnimation) {
                            this.mPaint.setAlpha((int) (255.0d * progress));
                        }
                        canvas.drawText(String.valueOf(digitAnimation.mCurrentAnimatingToDigit), x, botY, this.mPaint);
                    }
                } else {
                    num = num + digitText;
                    this.mPaint.setAlpha(255);
                    canvas.drawText(digitText, x, y, this.mPaint);
                }
                x -= this.mMaxTextWidth + this.mInterCharacterSpacingX;
            }
            if (!num.equals(this.mLastPrint)) {
                this.mLastPrint = num;
            }
        }
        removeCallbacks(this);
        if (!this.mIsPaused) {
            postDelayed(this, 20L);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        updateTextSizes();
        if (widthMode == 1073741824) {
            width = widthSize;
        } else {
            int digits = Math.max(this.mLastDigitCount, String.valueOf(this.mDrawingCount).length());
            width = ((this.mMaxTextWidth + this.mInterCharacterSpacingX) * digits) + ((digits / 3) * this.mBounds[10].width()) + getPaddingLeft() + getPaddingRight();
            if (heightMode == Integer.MIN_VALUE) {
                width = Math.min(width, widthSize);
            }
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = this.mMaxTextHeight + getPaddingTop() + getPaddingBottom() + this.mInterCharacterSpacingY;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int getIntrinsicHeightForDigit(int measuredHeight, int digit) {
        return (this.mBounds[digit].height() + measuredHeight) >> 1;
    }

    public void invalidateCounterUI() {
        this.mCanAnimate = true;
    }

    public void adjustExtraCount(long extraCount) {
        this.mExtraCount = extraCount;
    }

    private void invalidateDigitSize(boolean forceDigitSizeRecal) {
        synchronized (this.mLOCK) {
            if (this.mDigitAnimations.size() == 0) {
                this.mDigitAnimations.add(new DigitAnimation(0, this.mVelocityPerMS, this.mDrawingCount));
            }
            long currentCount = getCount() + this.mExtraCount;
            long currentTime = System.nanoTime();
            int digitCount = String.valueOf(this.mDrawingCount).length();
            long diffTimeMs = System.currentTimeMillis() - this.mLastAnimationTime;
            boolean shouldAnimateNext = currentCount > this.mDigitAnimations.get(0).mAnimatingCount;
            if (forceDigitSizeRecal || (diffTimeMs >= this.mMinAnimationSeparation && (this.mCanAnimate || diffTimeMs >= this.mMaxAnimationSeparation))) {
                this.mLastAnimationTime = System.currentTimeMillis();
                int i = 0;
                long newDrawingCount = 0;
                while (true) {
                    if (i >= digitCount && !shouldAnimateNext) {
                        break;
                    }
                    if (this.mDigitAnimations.size() <= i) {
                        this.mDigitAnimations.add(new DigitAnimation(i, this.mVelocityPerMS, this.mDrawingCount));
                    }
                    DigitAnimation digitAnim = this.mDigitAnimations.get(i);
                    shouldAnimateNext = digitAnim.invalidate(currentTime, currentCount, shouldAnimateNext);
                    newDrawingCount = (long) ((i == 0 ? digitAnim.mDrawingDigit : digitAnim.mDrawingDigit * Math.pow(10.0d, i)) + newDrawingCount);
                    i++;
                }
                setDrawingCount(newDrawingCount, forceDigitSizeRecal);
                if (this.mLastDigitCount != this.mDigitAnimations.size()) {
                    this.mLastDigitCount = this.mDigitAnimations.size();
                    requestLayout();
                }
                if (!this.mAnimationMode.continuousAnimation) {
                    this.mCanAnimate = false;
                }
            }
        }
    }

    private void setDrawingCount(long newDrawingCount, boolean invalidation) {
        this.mDrawingCount = newDrawingCount;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (!CrossConstants.DISABLE_ANIMATIONS) {
            invalidateDigitSize(false);
            if (!this.mIsPaused && isShown()) {
                postInvalidate();
            }
        }
    }
}
