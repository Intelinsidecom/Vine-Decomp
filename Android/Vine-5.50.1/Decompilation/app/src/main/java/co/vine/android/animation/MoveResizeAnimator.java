package co.vine.android.animation;

import android.view.View;
import android.widget.RelativeLayout;
import co.vine.android.animation.SmoothAnimator;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class MoveResizeAnimator extends SmoothAnimator {
    private final int mDeltaHeight;
    private final int mDeltaWidth;
    private final float mDeltaX;
    private final float mDeltaY;
    private int mId;
    private SmoothAnimator.AnimationListener mListener;
    private final int mOriginalHeight;
    private final int mOriginalWidth;
    private final int mOriginalX;
    private final int mOriginalY;
    private final RelativeLayout.LayoutParams mParam;
    private Object mTag;
    private final View mTarget;
    private final int mTargetHeight;
    private final int mTargetWidth;

    public MoveResizeAnimator(int id, View target, int targetY, double targetZoomX, double targetZoomY, int duration, SmoothAnimator.AnimationListener listener, Object tag) {
        this(id, target, target, 0, targetY, targetZoomX, targetZoomY, duration, listener, tag);
    }

    public MoveResizeAnimator(int id, View target, View measureUsingView, int targetX, int targetY, double targetZoomX, double targetZoomY, int duration, SmoothAnimator.AnimationListener listener, Object tag) {
        super(target, 0.05f, duration);
        this.mTarget = target;
        this.mParam = (RelativeLayout.LayoutParams) target.getLayoutParams();
        this.mDeltaX = this.mParam.leftMargin - targetX;
        this.mDeltaY = this.mParam.topMargin - targetY;
        this.mOriginalX = this.mParam.leftMargin;
        this.mOriginalY = this.mParam.topMargin;
        if (this.mParam.width < 3) {
            this.mParam.width = measureUsingView.getMeasuredWidth();
        }
        if (this.mParam.height < 3) {
            this.mParam.height = measureUsingView.getMeasuredHeight();
        }
        this.mTargetWidth = (int) (this.mParam.width * targetZoomX);
        this.mTargetHeight = (int) (this.mParam.height * targetZoomY);
        this.mOriginalWidth = this.mParam.width;
        this.mOriginalHeight = this.mParam.height;
        this.mDeltaWidth = this.mTargetWidth - this.mParam.width;
        this.mDeltaHeight = this.mTargetHeight - this.mParam.height;
        this.mListener = listener;
        this.mTag = tag;
        this.mId = id;
    }

    @Override // co.vine.android.animation.SmoothAnimator
    public void onUpdate(float frac, float smoothFrac) {
        float f = 1.0f - smoothFrac;
        this.mParam.leftMargin = this.mOriginalX - ((int) (this.mDeltaX * smoothFrac));
        this.mParam.topMargin = this.mOriginalY - ((int) (this.mDeltaY * smoothFrac));
        if (this.mTargetWidth != this.mParam.width) {
            this.mParam.width = (int) (this.mOriginalWidth + (this.mDeltaWidth * smoothFrac));
        }
        if (this.mTargetHeight != this.mParam.height) {
            this.mParam.height = (int) (this.mOriginalHeight + (this.mDeltaHeight * smoothFrac));
        }
        SLog.d("Modifying {}: {} {} {} {}", new Object[]{this.mTarget, Integer.valueOf(this.mParam.leftMargin), Integer.valueOf(this.mParam.topMargin), Integer.valueOf(this.mParam.width), Integer.valueOf(this.mParam.height)});
        this.mTarget.setLayoutParams(this.mParam);
    }

    @Override // co.vine.android.animation.SmoothAnimator
    public void onStop() {
        if (this.mListener != null) {
            this.mListener.onAnimationFinish(this.mId, this.mTag);
        }
    }
}
