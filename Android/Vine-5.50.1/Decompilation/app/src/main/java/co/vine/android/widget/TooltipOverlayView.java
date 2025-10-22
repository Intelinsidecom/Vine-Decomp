package co.vine.android.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimationListener;

/* loaded from: classes.dex */
public class TooltipOverlayView extends RelativeLayout {
    private final Context mContext;
    private final Listener mListener;
    private final TooltipView mTooltip;

    public interface Listener {
        void onDismissed();
    }

    public TooltipOverlayView(Context context, String message, Listener listener) {
        super(context);
        setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.TooltipOverlayView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TooltipOverlayView.this.dismiss();
            }
        });
        setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_tooltip_overlay)));
        TooltipView tooltip = new TooltipView(context);
        tooltip.setText(message);
        tooltip.setClickable(false);
        tooltip.setVisibility(0);
        addView(tooltip);
        this.mTooltip = tooltip;
        this.mContext = context;
        this.mListener = listener;
    }

    public void startShowAnimation() {
        AlphaAnimation show = new AlphaAnimation(0.0f, 1.0f);
        show.setDuration(500L);
        show.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.TooltipOverlayView.2
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TooltipOverlayView.this.clearAnimation();
                TooltipOverlayView.this.mTooltip.setVisibility(0);
            }
        });
        startAnimation(show);
    }

    public void dismiss() {
        if (getAnimation() == null) {
            AlphaAnimation hide = new AlphaAnimation(1.0f, 0.0f);
            hide.setDuration(200L);
            hide.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.TooltipOverlayView.3
                @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    TooltipOverlayView.this.setVisibility(8);
                    ViewGroup parent = (ViewGroup) TooltipOverlayView.this.getParent();
                    if (parent != null) {
                        parent.removeView(TooltipOverlayView.this);
                    }
                    if (TooltipOverlayView.this.mListener != null) {
                        TooltipOverlayView.this.mListener.onDismissed();
                    }
                }
            });
            startAnimation(hide);
        }
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getLayoutParams().width = -1;
        getLayoutParams().height = -1;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void updatePosition(int xPosition, int anchorWidth, int anchorHeight) {
        this.mTooltip.updatePosition(xPosition, anchorWidth, anchorHeight);
    }
}
