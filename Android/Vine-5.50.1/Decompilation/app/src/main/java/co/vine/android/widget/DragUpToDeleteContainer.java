package co.vine.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.animation.SmoothAnimator;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes.dex */
public class DragUpToDeleteContainer extends RelativeLayout {
    private float mCurY;
    private boolean mDestroying;
    private float mDownX;
    private float mDownY;
    private Bitmap mDragBitmap;
    private DragUpListener mDragUpListener;
    private ImageView mDragView;
    private float mDragViewAlpha;
    private final DragViewDestroyAnimator mDragViewDestroyAnimator;
    private final DropAnimator mDragViewDropAnimator;
    private int mDragViewHeight;
    private int mDragViewWidth;
    private int mDragViewX;
    private int mDragViewY;
    private boolean mDropping;
    private boolean mLockAlpha;
    private final int mTouchSlop;
    private View mViewToPickUp;
    private float mYOffsetFromTouch;

    public interface DragUpListener {
        boolean prepareForPickup();

        void viewDestroyFinished();

        boolean viewDropped(int i);

        void viewLanded();

        boolean viewMoved(int i);

        void viewPickedUp();
    }

    public DragUpToDeleteContainer(Context context) {
        this(context, null);
    }

    public DragUpToDeleteContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragUpToDeleteContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLockAlpha = false;
        this.mDragViewDestroyAnimator = new DragViewDestroyAnimator(0.05f, 350);
        this.mDragViewDropAnimator = new DropAnimator(0.05f, avcodec.AV_CODEC_ID_JV);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setView(View v, int leftMargin) {
        this.mViewToPickUp = v;
        this.mDragViewX = leftMargin;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case 0:
                this.mDownY = event.getY();
                this.mDownX = event.getX();
                break;
            case 2:
                if (this.mDownY - event.getY() >= this.mTouchSlop && Math.abs(event.getX() - this.mDownX) < this.mTouchSlop) {
                    this.mCurY = event.getY();
                    boolean canPickUp = true;
                    if (this.mDragUpListener != null) {
                        canPickUp = this.mDragUpListener.prepareForPickup();
                    }
                    if (canPickUp) {
                        createDragView();
                        if (this.mDragUpListener != null) {
                            this.mDragUpListener.viewPickedUp();
                        }
                        invalidate();
                        break;
                    }
                }
                break;
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case 1:
                boolean delete = false;
                if (this.mDragUpListener != null) {
                    delete = this.mDragUpListener.viewDropped(this.mDragViewY);
                }
                if (delete) {
                    this.mDragViewDestroyAnimator.start();
                } else {
                    this.mDragViewDropAnimator.start();
                }
                invalidate();
                break;
            case 2:
                this.mCurY = event.getY();
                this.mDragViewY = (int) (this.mCurY - this.mYOffsetFromTouch);
                int startY = (int) (this.mDownY - this.mYOffsetFromTouch);
                if (this.mDragViewY <= startY) {
                    startY = this.mDragViewY;
                }
                this.mDragViewY = startY;
                if (this.mDragUpListener != null) {
                    this.mLockAlpha = this.mDragUpListener.viewMoved(this.mDragViewY);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setInteractionListner(DragUpListener dragUpListener) {
        this.mDragUpListener = dragUpListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyDragView() {
        if (this.mDragView != null) {
            this.mDragView.setImageDrawable(null);
            this.mDragBitmap = null;
            this.mDragView = null;
        }
    }

    private void createDragView() {
        if (this.mViewToPickUp != null) {
            this.mViewToPickUp.setDrawingCacheQuality(1048576);
            this.mViewToPickUp.setDrawingCacheEnabled(true);
            this.mDragBitmap = this.mViewToPickUp.getDrawingCache();
            if (this.mDragView == null) {
                this.mDragView = new ImageView(getContext());
            }
            this.mDragViewAlpha = 255.0f;
            this.mDragView.setImageBitmap(this.mDragBitmap);
            this.mDragView.setLayoutParams(new ViewGroup.LayoutParams(this.mViewToPickUp.getWidth(), this.mViewToPickUp.getHeight()));
            int[] myPosition = new int[2];
            int[] itsPosition = new int[2];
            getLocationInWindow(myPosition);
            this.mViewToPickUp.getLocationInWindow(itsPosition);
            this.mYOffsetFromTouch = (this.mDownY - itsPosition[1]) + myPosition[1];
            this.mDragViewWidth = this.mViewToPickUp.getWidth();
            this.mDragViewHeight = this.mViewToPickUp.getHeight();
            this.mDragViewY = (int) (this.mCurY - this.mYOffsetFromTouch);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mDragView != null) {
            if (!this.mDestroying && !this.mLockAlpha) {
                int dragViewStartY = (int) (this.mDownY - this.mYOffsetFromTouch);
                this.mDragViewAlpha = (((1.0f * this.mDragViewY) / dragViewStartY) * 178.0f) + 77.0f;
            }
            if (this.mDragViewAlpha < 0.0f) {
                this.mDragViewAlpha = 0.0f;
            }
            canvas.save();
            canvas.translate(this.mDragViewX, this.mDragViewY);
            canvas.clipRect(0, 0, this.mDragViewWidth, this.mDragViewHeight);
            canvas.saveLayerAlpha(0.0f, 0.0f, this.mDragViewWidth, this.mDragViewHeight, (int) this.mDragViewAlpha, 31);
            this.mDragView.layout(0, 0, this.mDragViewWidth, this.mDragViewHeight);
            this.mDragView.draw(canvas);
            canvas.restore();
            canvas.restore();
        }
    }

    private class DragViewDestroyAnimator extends SmoothAnimator {
        private int mDeltaY;
        private int mStartY;

        public DragViewDestroyAnimator(float smoothness, int duration) {
            super(DragUpToDeleteContainer.this, smoothness, duration);
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStart() {
            this.mStartY = DragUpToDeleteContainer.this.mDragViewY;
            this.mDeltaY = DragUpToDeleteContainer.this.mDragViewY + DragUpToDeleteContainer.this.mDragViewHeight;
            DragUpToDeleteContainer.this.mDestroying = true;
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            DragUpToDeleteContainer.this.mDragViewY = (int) (this.mStartY - (this.mDeltaY * smoothFrac));
            DragUpToDeleteContainer.this.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            DragUpToDeleteContainer.this.mDragViewAlpha = 0.0f;
            DragUpToDeleteContainer.this.mDestroying = false;
            DragUpToDeleteContainer.this.destroyDragView();
            if (DragUpToDeleteContainer.this.mDragUpListener != null) {
                DragUpToDeleteContainer.this.mDragUpListener.viewDestroyFinished();
            }
            DragUpToDeleteContainer.this.invalidate();
        }
    }

    private class DropAnimator extends SmoothAnimator {
        private float mDeltaX;
        private float mDeltaY;
        private int mTargetX;
        private int mTargetY;

        public DropAnimator(float smoothness, int duration) {
            super(DragUpToDeleteContainer.this, smoothness, duration);
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStart() {
            DragUpToDeleteContainer.this.mDropping = true;
            if (DragUpToDeleteContainer.this.mDragView != null) {
                this.mTargetY = (int) (DragUpToDeleteContainer.this.mDownY - DragUpToDeleteContainer.this.mYOffsetFromTouch);
            }
            this.mTargetX = DragUpToDeleteContainer.this.mDragViewX;
            this.mDeltaX = DragUpToDeleteContainer.this.mDragViewX - this.mTargetX;
            this.mDeltaY = DragUpToDeleteContainer.this.mDragViewY - this.mTargetY;
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onUpdate(float frac, float smoothFrac) {
            float f = 1.0f - smoothFrac;
            DragUpToDeleteContainer.this.mDragViewX = this.mTargetX + ((int) (this.mDeltaX * f));
            DragUpToDeleteContainer.this.mDragViewY = this.mTargetY + ((int) (this.mDeltaY * f));
            DragUpToDeleteContainer.this.invalidate();
        }

        @Override // co.vine.android.animation.SmoothAnimator
        public void onStop() {
            if (DragUpToDeleteContainer.this.mDragUpListener != null) {
                DragUpToDeleteContainer.this.mDragUpListener.viewLanded();
            }
            DragUpToDeleteContainer.this.destroyDragView();
            DragUpToDeleteContainer.this.invalidate();
        }
    }
}
