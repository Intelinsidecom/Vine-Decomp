package co.vine.android.feedadapter.v2;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/* loaded from: classes.dex */
public final class VideoImageTopHideAnimation extends AlphaAnimation implements Animation.AnimationListener {
    private final View mView;

    public VideoImageTopHideAnimation(View view) {
        super(1.0f, 0.0f);
        this.mView = view;
        setDuration(200L);
        setAnimationListener(this);
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationEnd(Animation animation) {
        this.mView.setVisibility(8);
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }
}
