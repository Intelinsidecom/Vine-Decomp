package co.vine.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import co.vine.android.animation.SmoothAnimator;

/* loaded from: classes.dex */
public class ToolDrawerLinearLayout extends LinearLayout {
    private Runnable mOnAnimationEndRunnable;
    private boolean mShown;
    private SmoothAnimator mSmoothDrawerHider;
    private SmoothAnimator mSmoothDrawerShower;
    private int mY;

    /* JADX INFO: Access modifiers changed from: private */
    public void callOnStopRunnable() {
        if (this.mOnAnimationEndRunnable != null) {
            post(this.mOnAnimationEndRunnable);
            this.mOnAnimationEndRunnable = null;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ToolDrawerLinearLayout(Context context) {
        super(context);
        int i = 120;
        float f = 0.05f;
        this.mY = 0;
        this.mShown = false;
        this.mOnAnimationEndRunnable = null;
        this.mSmoothDrawerHider = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.1
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                this.mDelta = ToolDrawerLinearLayout.this.mY - ToolDrawerLinearLayout.this.getMeasuredHeight();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * smoothFrac);
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.setVisibility(4);
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
        this.mSmoothDrawerShower = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.2
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                ToolDrawerLinearLayout.this.setVisibility(0);
                this.mDelta = ToolDrawerLinearLayout.this.mY;
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * (1.0f - smoothFrac));
                if (ToolDrawerLinearLayout.this.mY > 0) {
                    ToolDrawerLinearLayout.this.mY = 0;
                }
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ToolDrawerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int i = 120;
        float f = 0.05f;
        this.mY = 0;
        this.mShown = false;
        this.mOnAnimationEndRunnable = null;
        this.mSmoothDrawerHider = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.1
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                this.mDelta = ToolDrawerLinearLayout.this.mY - ToolDrawerLinearLayout.this.getMeasuredHeight();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * smoothFrac);
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.setVisibility(4);
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
        this.mSmoothDrawerShower = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.2
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                ToolDrawerLinearLayout.this.setVisibility(0);
                this.mDelta = ToolDrawerLinearLayout.this.mY;
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * (1.0f - smoothFrac));
                if (ToolDrawerLinearLayout.this.mY > 0) {
                    ToolDrawerLinearLayout.this.mY = 0;
                }
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ToolDrawerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int i = 120;
        float f = 0.05f;
        this.mY = 0;
        this.mShown = false;
        this.mOnAnimationEndRunnable = null;
        this.mSmoothDrawerHider = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.1
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                this.mDelta = ToolDrawerLinearLayout.this.mY - ToolDrawerLinearLayout.this.getMeasuredHeight();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * smoothFrac);
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.setVisibility(4);
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
        this.mSmoothDrawerShower = new SmoothAnimator(this, f, i) { // from class: co.vine.android.widget.ToolDrawerLinearLayout.2
            private int mDelta = 0;

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStart() {
                ToolDrawerLinearLayout.this.setVisibility(0);
                this.mDelta = ToolDrawerLinearLayout.this.mY;
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onUpdate(float frac, float smoothFrac) {
                ToolDrawerLinearLayout.this.mY = (int) (this.mDelta * (1.0f - smoothFrac));
                if (ToolDrawerLinearLayout.this.mY > 0) {
                    ToolDrawerLinearLayout.this.mY = 0;
                }
                ToolDrawerLinearLayout.this.invalidate();
            }

            @Override // co.vine.android.animation.SmoothAnimator
            public void onStop() {
                ToolDrawerLinearLayout.this.callOnStopRunnable();
            }
        };
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mY = -getMeasuredHeight();
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        c.save();
        c.translate(0.0f, this.mY);
        super.draw(c);
        c.restore();
    }

    public void toggle(Runnable animationEndRunnable) {
        this.mShown = !this.mShown;
        this.mOnAnimationEndRunnable = animationEndRunnable;
        if (this.mShown) {
            this.mSmoothDrawerHider.cancel();
            this.mSmoothDrawerShower.start();
        } else {
            this.mSmoothDrawerShower.cancel();
            this.mSmoothDrawerHider.start();
        }
    }

    public boolean isOpen() {
        return this.mShown;
    }
}
