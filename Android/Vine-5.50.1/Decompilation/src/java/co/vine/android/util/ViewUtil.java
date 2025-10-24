package co.vine.android.util;

import android.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/* loaded from: classes.dex */
public final class ViewUtil {
    private static int sActionBarHeight = -1;
    public static final View.OnClickListener EMPTY_VIEW_CLICK_LISTENER = new View.OnClickListener() { // from class: co.vine.android.util.ViewUtil.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
        }
    };

    public enum ResizeAnimationType {
        EXPAND_WIDTH,
        EXPAND_HEIGHT,
        COLLAPSE_WIDTH,
        COLLAPSE_HEIGHT
    }

    public static int getActionBarHeight(Context context) {
        if (sActionBarHeight < 0) {
            TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
            sActionBarHeight = (int) styledAttributes.getDimension(0, -1.0f);
            styledAttributes.recycle();
        }
        return sActionBarHeight;
    }

    public static float getFontScale(Context context) {
        return context.getResources().getConfiguration().fontScale;
    }

    public static void reduceTextSizeViaFontScaleIfNeeded(Context context, float maxScaleToStart, float maxStepDown, TextView... textViews) {
        float fontScale = getFontScale(context);
        if (fontScale > maxScaleToStart) {
            for (TextView textView : textViews) {
                stepDownViaFontScale(textView, fontScale, maxStepDown);
            }
        }
    }

    public static void stepDownViaFontScale(TextView textView, float fontScale, float maxStepDown) {
        float size = textView.getTextSize();
        textView.setTextSize(0, (float) (size - (maxStepDown * (1.0d - (1.5d - fontScale)))));
    }

    public static void setActionBarHeight(AppCompatActivity actionBarActivity, View viewToSet) {
        int height = getActionBarHeight(actionBarActivity);
        if (height > 0) {
            viewToSet.getLayoutParams().height = height;
        } else {
            setActionBarHeightWhenReady(actionBarActivity, viewToSet);
        }
    }

    public static void setActionBarHeightWhenReady(final AppCompatActivity actionBarActivity, final View viewToSet) {
        final ViewTreeObserver observer = viewToSet.getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: co.vine.android.util.ViewUtil.2
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    int height;
                    ActionBar ab = actionBarActivity.getSupportActionBar();
                    if (ab != null && (height = ab.getHeight()) > 0) {
                        viewToSet.getLayoutParams().height = height;
                        if (Build.VERSION.SDK_INT < 16) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setBackground(Resources res, View view, Bitmap bitmap) {
        BitmapDrawable d = new BitmapDrawable(res, bitmap);
        setBackground(view, d);
    }

    public static void fillColor(Resources res, int color, int resId, View v) throws Resources.NotFoundException {
        if (v instanceof ImageView) {
            ((ImageView) v).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            return;
        }
        Drawable d = res.getDrawable(resId);
        d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        setBackground(v, d);
    }

    public static Rect getViewPositionInAncestorWithId(View child, int rootId) {
        float x = child.getX();
        float y = child.getY();
        Object parent = child.getParent();
        while (true) {
            View parent2 = (View) parent;
            if (parent2.getId() != rootId) {
                x += parent2.getX();
                y += parent2.getY();
                parent = parent2.getParent();
            } else {
                return new Rect((int) x, (int) y, ((int) x) + child.getWidth(), ((int) y) + child.getHeight());
            }
        }
    }

    public static Animation makeResizeAnimation(ResizeAnimationType type, View v, int targetSize) {
        return makeResizeAnimation(type, v, targetSize, -1, null);
    }

    public static Animation makeResizeAnimation(ResizeAnimationType type, final View v, final int targetSize, int duration, Animation.AnimationListener listener) {
        Animation animation;
        final ViewGroup.LayoutParams params = v.getLayoutParams();
        final boolean typeWidth = type == ResizeAnimationType.EXPAND_WIDTH || type == ResizeAnimationType.COLLAPSE_WIDTH;
        boolean typeExpand = type == ResizeAnimationType.EXPAND_WIDTH || type == ResizeAnimationType.EXPAND_HEIGHT;
        if (typeExpand) {
            if (typeWidth) {
                params.width = 0;
            } else {
                params.height = 0;
            }
            v.setVisibility(0);
            animation = new Animation() { // from class: co.vine.android.util.ViewUtil.3
                @Override // android.view.animation.Animation
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (typeWidth) {
                        params.width = interpolatedTime == 1.0f ? targetSize : (int) (targetSize * interpolatedTime);
                    } else {
                        params.height = interpolatedTime == 1.0f ? targetSize : (int) (targetSize * interpolatedTime);
                    }
                    v.requestLayout();
                }

                @Override // android.view.animation.Animation
                public boolean willChangeBounds() {
                    return true;
                }
            };
        } else {
            final int initialSize = typeWidth ? v.getMeasuredWidth() : v.getMeasuredHeight();
            animation = new Animation() { // from class: co.vine.android.util.ViewUtil.4
                @Override // android.view.animation.Animation
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1.0f) {
                        v.setVisibility(8);
                        return;
                    }
                    if (typeWidth) {
                        params.width = initialSize - ((int) (initialSize * interpolatedTime));
                    } else {
                        params.height = initialSize - ((int) (initialSize * interpolatedTime));
                    }
                    v.requestLayout();
                }

                @Override // android.view.animation.Animation
                public boolean willChangeBounds() {
                    return true;
                }
            };
        }
        if (duration > 0) {
            animation.setDuration(duration);
        }
        animation.setAnimationListener(listener);
        return animation;
    }

    @TargetApi(14)
    public static void setLowProfileSystemUiMode(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(1);
    }

    public static void disableAndHide(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(false);
                view.setVisibility(8);
            }
        }
    }

    public static void enableAndShow(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(true);
                view.setVisibility(0);
            }
        }
    }

    public static Point getAtMostSize(View view, int availableWidthPx, int availableHeightPx) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(availableWidthPx, Integer.MIN_VALUE);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(availableHeightPx, Integer.MIN_VALUE);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public static int getStatusBarHeightPx(Resources resources) throws Resources.NotFoundException {
        int statusBarHeightResourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightResourceId <= 0) {
            return 0;
        }
        int statusBarHeight = resources.getDimensionPixelSize(statusBarHeightResourceId);
        return statusBarHeight;
    }

    public static void addToViewGroup(View view, ViewGroup viewGroup) {
        if (view != null && viewGroup != null) {
            ViewParent parent = view.getParent();
            if (parent != null && (parent instanceof ViewGroup)) {
                ViewGroup parentViewGroup = (ViewGroup) parent;
                parentViewGroup.removeView(view);
            }
            viewGroup.addView(view);
        }
    }

    public static int getViewVisiblePercentVertical(ListView listView, Rect scrollBounds, View view) {
        if (listView == null || view == null) {
            return -1;
        }
        listView.getDrawingRect(scrollBounds);
        int height = view.getHeight();
        float top = view.getY();
        float bottom = top + height;
        if (scrollBounds.top >= top || scrollBounds.bottom <= bottom || !view.getGlobalVisibleRect(scrollBounds)) {
            return -1;
        }
        return (scrollBounds.height() * 100) / height;
    }
}
