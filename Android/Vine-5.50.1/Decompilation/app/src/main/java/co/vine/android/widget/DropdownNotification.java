package co.vine.android.widget;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import co.vine.android.R;
import co.vine.android.util.ViewUtil;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public final class DropdownNotification {
    private final Activity mActivity;
    private final int mAnimationDuration;
    private final int mContainerViewResId;
    private final long mDismissalTimeout;
    private Dismisser mDismisser;
    private final Integer mEntryAnimation;
    private final Integer mExitAnimation;

    public DropdownNotification(Activity activity, long timeout) {
        this(activity, timeout, R.layout.dropdown_notification);
    }

    public DropdownNotification(Activity activity, long timeout, int containerViewResId) {
        this(activity, timeout, containerViewResId, null, null, HttpResponseCode.INTERNAL_SERVER_ERROR);
    }

    public DropdownNotification(Activity activity, long timeout, int containerViewResId, Integer entryAnimation, Integer exitAnimation, int animationDuration) {
        this.mActivity = activity;
        this.mDismissalTimeout = timeout;
        this.mContainerViewResId = containerViewResId;
        this.mEntryAnimation = entryAnimation;
        this.mExitAnimation = exitAnimation;
        this.mAnimationDuration = animationDuration;
    }

    private View getContainerView() {
        ViewGroup decorView = (ViewGroup) this.mActivity.getWindow().getDecorView();
        View overlayView = decorView.findViewById(R.id.dropdown_overlay);
        if (overlayView != null) {
            return overlayView;
        }
        View rootView = this.mActivity.getLayoutInflater().inflate(this.mContainerViewResId, (ViewGroup) null);
        View overlayView2 = rootView.findViewById(R.id.dropdown_overlay);
        View contentView = overlayView2.findViewById(R.id.dropdown_content);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.topMargin = ViewUtil.getStatusBarHeightPx(this.mActivity.getResources());
        contentView.setLayoutParams(params);
        LayoutTransition transition = new LayoutTransition();
        transition.disableTransitionType(0);
        transition.setStartDelay(2, 0L);
        ((ViewGroup) overlayView2).setLayoutTransition(transition);
        decorView.addView(rootView);
        return overlayView2;
    }

    public void showOverlay(View contentView) throws Resources.NotFoundException {
        showOverlay(contentView, true);
    }

    public void showOverlay(View contentView, boolean enableTouchDismiss) throws Resources.NotFoundException {
        View overlayView = getContainerView();
        ViewGroup contentGroup = (ViewGroup) overlayView.findViewById(R.id.dropdown_content);
        contentGroup.removeAllViews();
        contentGroup.addView(contentView);
        contentGroup.setVisibility(0);
        if (this.mEntryAnimation != null) {
            Animation animation = AnimationUtils.loadAnimation(this.mActivity, this.mEntryAnimation.intValue());
            animation.setDuration(this.mAnimationDuration);
            contentGroup.startAnimation(animation);
        }
        this.mDismisser = new Dismisser(overlayView, this.mActivity, this.mExitAnimation, this.mAnimationDuration);
        overlayView.setOnTouchListener(this.mDismisser);
        if (enableTouchDismiss) {
            contentGroup.setOnTouchListener(this.mDismisser);
        } else {
            contentGroup.setOnTouchListener(null);
        }
        if (this.mDismissalTimeout > 0) {
            overlayView.postDelayed(this.mDismisser, this.mDismissalTimeout);
        }
    }

    private static final class Dismisser implements View.OnTouchListener, Runnable {
        private Activity mActivity;
        private int mAnimationDuration;
        private boolean mDismissed = false;
        private Integer mExitAnimation;
        private final View mOverlayView;

        Dismisser(View overlayView, Activity activity, Integer exitAnimation, int animationDuration) {
            this.mOverlayView = overlayView;
            this.mActivity = activity;
            this.mExitAnimation = exitAnimation;
            this.mAnimationDuration = animationDuration;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) throws Resources.NotFoundException {
            dismiss();
            return false;
        }

        @Override // java.lang.Runnable
        public void run() throws Resources.NotFoundException {
            dismiss();
        }

        private void dismiss() throws Resources.NotFoundException {
            if (!this.mDismissed) {
                this.mDismissed = true;
                this.mOverlayView.removeCallbacks(this);
                this.mOverlayView.setOnTouchListener(null);
                ViewGroup contentGroup = (ViewGroup) this.mOverlayView.findViewById(R.id.dropdown_content);
                final View decorView = this.mActivity.getWindow().getDecorView();
                final View rootView = decorView.findViewById(R.id.dropdown_overlay);
                if (this.mExitAnimation != null) {
                    Animation animation = AnimationUtils.loadAnimation(this.mActivity, this.mExitAnimation.intValue());
                    animation.setDuration(this.mAnimationDuration);
                    animation.setAnimationListener(new Animation.AnimationListener() { // from class: co.vine.android.widget.DropdownNotification.Dismisser.1
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation2) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation2) {
                            if (rootView != null) {
                                ((ViewGroup) decorView).removeView(rootView);
                            }
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation2) {
                        }
                    });
                    contentGroup.startAnimation(animation);
                    contentGroup.setVisibility(8);
                    return;
                }
                contentGroup.setVisibility(8);
                if (rootView != null) {
                    ((ViewGroup) decorView).removeView(rootView);
                }
            }
        }
    }
}
