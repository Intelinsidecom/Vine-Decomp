package co.vine.android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import co.vine.android.animation.SimpleAnimationListener;

/* loaded from: classes.dex */
public class ModalView extends RelativeLayout {
    private Runnable hideCallback;

    public ModalView(Context context, ViewGroup parent, int layout) {
        super(context);
        this.hideCallback = new Runnable() { // from class: co.vine.android.widget.ModalView.2
            @Override // java.lang.Runnable
            public void run() {
                Animation hide = new AlphaAnimation(1.0f, 0.0f);
                hide.setDuration(300L);
                hide.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.ModalView.2.1
                    @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        ModalView.this.setVisibility(8);
                        ViewGroup parent2 = (ViewGroup) ModalView.this.getParent();
                        if (parent2 != null) {
                            parent2.removeView(ModalView.this);
                        }
                    }
                });
                ModalView.this.startAnimation(hide);
            }
        };
        parent.addView(this);
        ViewGroup body = (ViewGroup) inflate(context, layout, this);
        body.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.ModalView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ModalView.this.getAnimation() == null) {
                    ModalView.this.removeCallbacks(ModalView.this.hideCallback);
                    ModalView.this.hideCallback.run();
                }
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation show = new AlphaAnimation(0.0f, 1.0f);
        show.setDuration(500L);
        show.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.ModalView.3
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                ModalView.this.clearAnimation();
                ModalView.this.setVisibility(0);
            }
        });
        startAnimation(show);
        postDelayed(this.hideCallback, 3000L);
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getLayoutParams().width = -1;
        getLayoutParams().height = -1;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
