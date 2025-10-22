package co.vine.android.search;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.widget.FakeActionBar;

/* loaded from: classes.dex */
public final class ActionBarThemeChanger {
    private final FakeActionBar mActionBar;
    private final long mDuration;

    public ActionBarThemeChanger(FakeActionBar actionBar, long duration) {
        this.mActionBar = actionBar;
        this.mDuration = duration;
    }

    public void swapToColor(int backgroundToColor, int textViewToColor, int backButtonToColor, boolean showClearButton) {
        ValueAnimator alphaAnimator;
        View actionBarBackground = this.mActionBar.getActionBar();
        if (actionBarBackground != null) {
            if (actionBarBackground.getBackground() instanceof ColorDrawable) {
                ColorDrawable bg = (ColorDrawable) actionBarBackground.getBackground();
                int fromColor = bg.getColor();
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(fromColor), Integer.valueOf(backgroundToColor));
                colorAnimator.setDuration(this.mDuration);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.search.ActionBarThemeChanger.1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ActionBarThemeChanger.this.mActionBar.setActionBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                });
                colorAnimator.start();
            }
            final TextView query = (TextView) actionBarBackground.findViewById(R.id.search_query);
            int fromColor2 = query.getCurrentTextColor();
            ValueAnimator colorAnimator2 = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(fromColor2), Integer.valueOf(textViewToColor));
            colorAnimator2.setDuration(this.mDuration / 2);
            colorAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.search.ActionBarThemeChanger.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    query.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            colorAnimator2.start();
            final ImageView backButton = (ImageView) actionBarBackground.findViewById(R.id.ab_back_button);
            if (backButton.getTag() != null) {
                int fromColor3 = ((Integer) backButton.getTag()).intValue();
                backButton.setTag(Integer.valueOf(backButtonToColor));
                ValueAnimator colorAnimator3 = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(fromColor3), Integer.valueOf(backButtonToColor));
                colorAnimator3.setDuration(this.mDuration);
                colorAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.search.ActionBarThemeChanger.3
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        backButton.setColorFilter(((Integer) valueAnimator.getAnimatedValue()).intValue(), PorterDuff.Mode.SRC_IN);
                    }
                });
                colorAnimator3.start();
            }
            final View clearButton = actionBarBackground.findViewById(R.id.clear);
            float currentAlpha = clearButton.getAlpha();
            if (showClearButton) {
                alphaAnimator = ValueAnimator.ofFloat(currentAlpha, 1.0f);
            } else {
                alphaAnimator = ValueAnimator.ofFloat(currentAlpha, 0.0f);
            }
            alphaAnimator.setDuration(0L);
            alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.search.ActionBarThemeChanger.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    clearButton.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            alphaAnimator.start();
        }
    }
}
