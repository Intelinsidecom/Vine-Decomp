package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimationListener;

/* loaded from: classes.dex */
public class TooltipView extends LinearLayout {
    private int mLeftMargin;
    private Listener mListener;
    private ImageView mNib;
    private int mScreenPadding;
    private Rect mTextBounds;
    private int mTextViewPadding;
    private TextView mTooltip;

    public interface Listener {
        void onTooltipTapped();
    }

    public TooltipView(Context context) throws Resources.NotFoundException {
        super(context);
        setVisibility(4);
        setOrientation(1);
        this.mTextBounds = new Rect();
        ImageView nib = new ImageView(context);
        nib.setImageResource(R.drawable.nib_up);
        nib.setScaleType(ImageView.ScaleType.CENTER);
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_small);
        int paddingLeftRight = getResources().getDimensionPixelSize(R.dimen.tooltip_left_right_padding);
        TextView tooltip = new TextView(context);
        tooltip.setBackgroundResource(R.drawable.rounded_corner_rect_5);
        tooltip.setGravity(17);
        tooltip.setPadding(paddingLeftRight, padding, paddingLeftRight, padding);
        tooltip.setTextColor(getResources().getColor(R.color.solid_black));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 3;
        addView(nib, params);
        addView(tooltip);
        setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.TooltipView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TooltipView.this.hide();
                if (TooltipView.this.mListener != null) {
                    TooltipView.this.mListener.onTooltipTapped();
                }
            }
        });
        this.mNib = nib;
        this.mTooltip = tooltip;
        this.mScreenPadding = getResources().getDimensionPixelSize(R.dimen.spacing_small) * 2;
        this.mTextViewPadding = getResources().getDimensionPixelSize(R.dimen.tooltip_left_right_padding) * 2;
    }

    public void setText(int textResId) {
        setText(getResources().getString(textResId));
    }

    public void setText(String text) {
        this.mTooltip.setText(text);
        Paint p = this.mTooltip.getPaint();
        this.mTextBounds = new Rect();
        p.getTextBounds(text, 0, text.length(), this.mTextBounds);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int width = Math.min(screenWidth - this.mScreenPadding, this.mTextBounds.width() + this.mTextViewPadding + this.mScreenPadding);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
        int heightSpec = this.mTextBounds.height();
        super.onMeasure(widthSpec, heightSpec);
        measureChildren(widthSpec, heightSpec);
    }

    public void updatePosition(View anchor, int leftMargin) {
        this.mLeftMargin = leftMargin;
        updatePosition(anchor);
    }

    public void updatePosition(View anchor) {
        int[] loc = new int[2];
        anchor.getLocationOnScreen(loc);
        updatePosition(loc[0], anchor.getWidth(), anchor.getHeight());
    }

    public void updatePosition(int xPosition, int anchorWidth, int anchorHeight) {
        int newXPos;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int xPos = xPosition + this.mLeftMargin;
        int nibOffset = ((anchorWidth / 2) - (this.mNib.getDrawable().getIntrinsicWidth() / 2)) - this.mLeftMargin;
        int tooltipFullWidth = this.mTextBounds.width() + this.mScreenPadding + this.mTextViewPadding;
        if (xPos + tooltipFullWidth > screenWidth) {
            int newXPos2 = Math.max(this.mScreenPadding / 2, screenWidth - tooltipFullWidth);
            nibOffset += xPos - newXPos2;
            xPos = newXPos2;
        }
        if (xPos > this.mScreenPadding / 2 && (newXPos = (screenWidth - tooltipFullWidth) / 2) < xPosition - this.mNib.getDrawable().getIntrinsicWidth()) {
            nibOffset += xPos - newXPos;
            xPos = newXPos;
        }
        ((RelativeLayout.LayoutParams) getLayoutParams()).setMargins(xPos, anchorHeight, 0, 0);
        ((LinearLayout.LayoutParams) this.mNib.getLayoutParams()).setMargins(nibOffset, 0, 0, 0);
        requestLayout();
    }

    public void show() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400L);
        anim.setFillAfter(true);
        anim.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.TooltipView.3
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TooltipView.this.clearAnimation();
                TooltipView.this.setVisibility(0);
            }
        });
        startAnimation(anim);
    }

    public void hide() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200L);
        anim.setAnimationListener(new SimpleAnimationListener() { // from class: co.vine.android.widget.TooltipView.4
            @Override // co.vine.android.animation.SimpleAnimationListener, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                TooltipView.this.clearAnimation();
                TooltipView.this.setVisibility(8);
                ViewGroup parent = (ViewGroup) TooltipView.this.getParent();
                if (parent != null) {
                    parent.removeView(TooltipView.this);
                }
            }
        });
        startAnimation(anim);
    }

    public boolean hasBeenHidden() {
        return getVisibility() == 8 && getParent() == null;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }
}
