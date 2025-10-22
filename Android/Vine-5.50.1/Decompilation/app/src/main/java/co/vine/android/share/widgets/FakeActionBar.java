package co.vine.android.share.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesTextView;

/* loaded from: classes.dex */
public class FakeActionBar extends RelativeLayout {
    private final ViewGroup mActionContainer;
    private final ViewGroup mBackContainer;
    private final TypefacesTextView mLabel;
    private OnActionListener mOnActionListener;

    public interface OnActionListener {
        void onBackPressed();
    }

    public FakeActionBar(Context context) {
        this(context, null);
    }

    public FakeActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.new_fake_action_bar, this);
        this.mBackContainer = (ViewGroup) findViewById(R.id.back_container);
        this.mActionContainer = (ViewGroup) findViewById(R.id.action_container);
        this.mLabel = (TypefacesTextView) findViewById(R.id.label);
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VineFakeActionBar);
            int customContentLayoutResId = attributes.getResourceId(0, -1);
            int customActionLayoutResId = attributes.getResourceId(1, -1);
            attributes.recycle();
            if (customContentLayoutResId != -1) {
                ViewGroup contentContainer = (ViewGroup) findViewById(R.id.content_container);
                View contentView = layoutInflater.inflate(customContentLayoutResId, contentContainer, false);
                contentContainer.removeAllViews();
                contentContainer.addView(contentView == null ? this.mLabel : contentView);
            }
            if (customActionLayoutResId != -1) {
                setActionView(inflateActionView(customActionLayoutResId));
            }
        }
        setBackgroundColor(0);
        this.mBackContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.widgets.FakeActionBar.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FakeActionBar.this.mOnActionListener != null) {
                    FakeActionBar.this.mOnActionListener.onBackPressed();
                }
            }
        });
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        this.mBackContainer.setEnabled(enabled);
    }

    public void setOnActionListener(OnActionListener listener) {
        this.mOnActionListener = listener;
    }

    private Animator getAnimator(final boolean isShow) {
        float[] fArr = new float[2];
        fArr[0] = 0.0f;
        fArr[1] = isShow ? 1.0f : 0.0f;
        ObjectAnimator labelAnimator = ObjectAnimator.ofFloat(this, "alpha", fArr);
        labelAnimator.setTarget(this.mLabel);
        labelAnimator.setDuration(100L);
        labelAnimator.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.widgets.FakeActionBar.2
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                FakeActionBar.this.setEnabled(false);
            }

            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                FakeActionBar.this.setEnabled(isShow);
            }
        });
        float[] fArr2 = new float[2];
        fArr2[0] = 0.0f;
        fArr2[1] = isShow ? 1.0f : 0.0f;
        ObjectAnimator actionContainerAnimator = ObjectAnimator.ofFloat(this, "alpha", fArr2);
        actionContainerAnimator.setTarget(this.mActionContainer);
        actionContainerAnimator.setDuration(100L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(labelAnimator, actionContainerAnimator);
        return animatorSet;
    }

    public Animator getShowAnimator() {
        return getAnimator(true);
    }

    public Animator getHideAnimator() {
        return getAnimator(false);
    }

    public Point calculateBounds(Point windowSize) {
        return ViewUtil.getAtMostSize(this, windowSize.x, windowSize.y);
    }

    public void setLabelText(String text) {
        this.mLabel.setText(text);
    }

    public void setLabelStyle(int styleResId, int textStyle, int textWeight) {
        Context context = getContext();
        this.mLabel.setTextAppearance(context, styleResId);
        this.mLabel.setWeight(textWeight);
        this.mLabel.setTypeface(Typefaces.get(context).getContentTypeface(textStyle, textWeight), textStyle);
    }

    public View inflateBackView(int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(layoutResId, this.mBackContainer, false);
    }

    public View inflateActionView(int layoutResId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(layoutResId, this.mActionContainer, false);
    }

    public void setBackView(View view) {
        this.mBackContainer.removeAllViews();
        this.mBackContainer.addView(view);
    }

    public void setActionView(View view) {
        this.mActionContainer.removeAllViews();
        this.mActionContainer.addView(view);
    }

    public void setTopOfWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -2;
        layoutParams.gravity = 48;
        layoutParams.flags = 8;
        layoutParams.format = 1;
        setLayoutParams(layoutParams);
    }
}
