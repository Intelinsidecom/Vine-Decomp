package co.vine.android.recorder;

import android.animation.Animator;
import android.view.View;
import co.vine.android.animation.SimpleAnimatorListener;

/* loaded from: classes.dex */
public class ViewGoneAnimationListener extends SimpleAnimatorListener {
    private View mView;

    public ViewGoneAnimationListener(View toRemove) {
        this.mView = toRemove;
    }

    @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animation) {
        if (this.mView != null) {
            this.mView.setVisibility(4);
            this.mView.animate().setListener(null);
            this.mView.setAlpha(1.0f);
            this.mView = null;
        }
    }
}
