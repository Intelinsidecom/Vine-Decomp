package co.vine.android.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* loaded from: classes.dex */
public class HeightAnimationUtil {
    public static void expand(final View v) {
        v.measure(-1, -2);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(0);
        Animation a = new Animation() { // from class: co.vine.android.animation.HeightAnimationUtil.1
            @Override // android.view.animation.Animation
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override // android.view.animation.Animation
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() { // from class: co.vine.android.animation.HeightAnimationUtil.2
            @Override // android.view.animation.Animation
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(8);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (initialHeight * interpolatedTime));
                v.requestLayout();
            }

            @Override // android.view.animation.Animation
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
