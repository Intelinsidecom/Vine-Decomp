package co.vine.android.views.swipeable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import co.vine.android.R;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class FlingCardListener implements View.OnTouchListener {
    private float BASE_ROTATION_DEGREES;
    private final Object mDataObject;
    private float mDownTouchX;
    private float mDownTouchY;
    private final FlingListener mFlingListener;
    private View mFrame;
    private final float mHalfWidth;
    private final int mObjectH;
    private final int mObjectW;
    private final float mObjectX;
    private final float mObjectY;
    private final int mParentWidth;
    private float mPosX;
    private float mPosY;
    private int mTouchPosition;
    private static final String TAG = FlingCardListener.class.getSimpleName();
    private static final float MAX_COS = (float) Math.cos(Math.toRadians(45.0d));
    private int mActivePointerId = -1;
    private boolean isAnimationRunning = false;

    protected interface FlingListener {
        void leftExit(Object obj);

        void onCardExited();

        void onClick(Object obj);

        void onScroll(float f);

        void rightExit(Object obj);
    }

    public FlingCardListener(View frame, Object itemAtPosition, float rotation_degrees, FlingListener flingListener) {
        this.mFrame = null;
        this.mFrame = frame.findViewById(R.id.content);
        if (this.mFrame == null) {
            this.mFrame = frame;
        }
        this.mObjectX = this.mFrame.getX();
        this.mObjectY = this.mFrame.getY();
        this.mObjectH = frame.getHeight();
        this.mObjectW = frame.getWidth();
        this.mHalfWidth = this.mObjectW / 2.0f;
        this.mDataObject = itemAtPosition;
        this.mParentWidth = ((ViewGroup) frame.getParent()).getWidth();
        this.BASE_ROTATION_DEGREES = rotation_degrees;
        this.mFlingListener = flingListener;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                this.mActivePointerId = event.getPointerId(0);
                float x = 0.0f;
                float y = 0.0f;
                boolean success = false;
                try {
                    x = event.getX(this.mActivePointerId);
                    y = event.getY(this.mActivePointerId);
                    success = true;
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "Exception in onTouch(view, event) : " + this.mActivePointerId, e);
                }
                if (success) {
                    this.mDownTouchX = x;
                    this.mDownTouchY = y;
                    if (this.mPosX == 0.0f) {
                        this.mPosX = this.mFrame.getX();
                    }
                    if (this.mPosY == 0.0f) {
                        this.mPosY = this.mFrame.getY();
                    }
                    if (y < this.mObjectH / 2) {
                        this.mTouchPosition = 0;
                    } else {
                        this.mTouchPosition = 1;
                    }
                }
                view.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case 1:
                this.mActivePointerId = -1;
                resetCardViewOnStack();
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case 2:
                int pointerIndexMove = event.findPointerIndex(this.mActivePointerId);
                float xMove = event.getX(pointerIndexMove);
                float yMove = event.getY(pointerIndexMove);
                float dx = xMove - this.mDownTouchX;
                float dy = yMove - this.mDownTouchY;
                this.mPosX += dx;
                this.mPosY += dy;
                float distobjectX = this.mPosX - this.mObjectX;
                float rotation = ((this.BASE_ROTATION_DEGREES * 2.0f) * distobjectX) / this.mParentWidth;
                if (this.mTouchPosition == 1) {
                    rotation = -rotation;
                }
                this.mFrame.setX(this.mPosX);
                this.mFrame.setY(this.mPosY);
                this.mFrame.setRotation(rotation);
                this.mFlingListener.onScroll(getScrollProgressPercent());
                break;
            case 3:
                this.mActivePointerId = -1;
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case 6:
                int pointerIndex = (event.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == this.mActivePointerId) {
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    this.mActivePointerId = event.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        return true;
    }

    private float getScrollProgressPercent() {
        if (movedBeyondLeftBorder()) {
            return -1.0f;
        }
        if (movedBeyondRightBorder()) {
            return 1.0f;
        }
        float zeroToOneValue = ((this.mPosX + this.mHalfWidth) - leftBorder()) / (rightBorder() - leftBorder());
        return (2.0f * zeroToOneValue) - 1.0f;
    }

    private boolean resetCardViewOnStack() {
        if (movedBeyondLeftBorder()) {
            onSelected(true, getExitPoint(-this.mObjectW), 100L);
            this.mFlingListener.onScroll(-1.0f);
        } else if (movedBeyondRightBorder()) {
            onSelected(false, getExitPoint(this.mParentWidth), 100L);
            this.mFlingListener.onScroll(1.0f);
        } else {
            float abslMoveDistance = Math.abs(this.mPosX - this.mObjectX);
            this.mPosX = 0.0f;
            this.mPosY = 0.0f;
            this.mDownTouchX = 0.0f;
            this.mDownTouchY = 0.0f;
            this.mFrame.animate().setDuration(200L).setInterpolator(new OvershootInterpolator(1.5f)).x(this.mObjectX).y(this.mObjectY).rotation(0.0f);
            this.mFlingListener.onScroll(0.0f);
            if (abslMoveDistance < 4.0d) {
                this.mFlingListener.onClick(this.mDataObject);
            }
        }
        return false;
    }

    private boolean movedBeyondLeftBorder() {
        return this.mPosX + this.mHalfWidth < leftBorder();
    }

    private boolean movedBeyondRightBorder() {
        return this.mPosX + this.mHalfWidth > rightBorder();
    }

    public float leftBorder() {
        return this.mParentWidth / 4.0f;
    }

    public float rightBorder() {
        return (this.mParentWidth * 3) / 4.0f;
    }

    public void onSelected(final boolean isLeft, float exitY, long duration) {
        float exitX;
        this.isAnimationRunning = true;
        if (isLeft) {
            exitX = (-this.mObjectW) - getRotationWidthOffset();
        } else {
            exitX = this.mParentWidth + getRotationWidthOffset();
        }
        this.mFrame.animate().setDuration(duration).setInterpolator(new AccelerateInterpolator()).x(exitX).y(exitY).setListener(new AnimatorListenerAdapter() { // from class: co.vine.android.views.swipeable.FlingCardListener.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                if (isLeft) {
                    FlingCardListener.this.mFlingListener.onCardExited();
                    FlingCardListener.this.mFlingListener.leftExit(FlingCardListener.this.mDataObject);
                } else {
                    FlingCardListener.this.mFlingListener.onCardExited();
                    FlingCardListener.this.mFlingListener.rightExit(FlingCardListener.this.mDataObject);
                }
                FlingCardListener.this.isAnimationRunning = false;
            }
        }).rotation(getExitRotation(isLeft));
    }

    public void selectLeft() {
        if (!this.isAnimationRunning) {
            onSelected(true, this.mObjectY, 200L);
        }
    }

    public void selectRight() {
        if (!this.isAnimationRunning) {
            onSelected(false, this.mObjectY, 200L);
        }
    }

    public void removeTop() {
        this.mFlingListener.onCardExited();
    }

    private float getExitPoint(int exitXPoint) {
        Float[] x = {Float.valueOf(this.mObjectX), Float.valueOf(this.mPosX)};
        Float[] y = {Float.valueOf(this.mObjectY), Float.valueOf(this.mPosY)};
        if (x[0].floatValue() - x[1].floatValue() != 0.0f) {
            float slope = (y[1].floatValue() - y[0].floatValue()) / (x[1].floatValue() - x[0].floatValue());
            float intercept = y[0].floatValue() - (x[0].floatValue() * slope);
            return (exitXPoint * slope) + intercept;
        }
        SLog.e("No relationship between x and y for to get exit point");
        return 0.0f;
    }

    private float getExitRotation(boolean isLeft) {
        float rotation = ((this.BASE_ROTATION_DEGREES * 2.0f) * (this.mParentWidth - this.mObjectX)) / this.mParentWidth;
        if (this.mTouchPosition == 1) {
            rotation = -rotation;
        }
        if (isLeft) {
            return -rotation;
        }
        return rotation;
    }

    private float getRotationWidthOffset() {
        return (this.mObjectW / MAX_COS) - this.mObjectW;
    }

    public boolean isTouching() {
        return this.mActivePointerId != -1;
    }

    public PointF getLastPoint() {
        return new PointF(this.mPosX, this.mPosY);
    }
}
