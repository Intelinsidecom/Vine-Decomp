package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import co.vine.android.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* loaded from: classes.dex */
public class FloatingLikesView extends View {
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator();
    private static Random sRandom = new Random();
    private int mAnimationDurationMS;
    private float mAnimationHeight;
    private float mAnimationMaxWidth;
    private Drawable mDrawable;
    private float mIconWidth;
    private Runnable mInvalidateRunnable;
    private List<Particle> mParticles;

    public FloatingLikesView(Context context) {
        super(context);
        this.mParticles = new ArrayList();
        this.mAnimationDurationMS = 1000;
        this.mAnimationMaxWidth = -1.0f;
        this.mAnimationHeight = -1.0f;
        this.mIconWidth = -1.0f;
        this.mInvalidateRunnable = new Runnable() { // from class: co.vine.android.widget.FloatingLikesView.1
            @Override // java.lang.Runnable
            public void run() {
                FloatingLikesView.this.invalidate();
                FloatingLikesView.this.postDelayed(this, 20L);
            }
        };
        postDelayed(this.mInvalidateRunnable, 20L);
    }

    public FloatingLikesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mParticles = new ArrayList();
        this.mAnimationDurationMS = 1000;
        this.mAnimationMaxWidth = -1.0f;
        this.mAnimationHeight = -1.0f;
        this.mIconWidth = -1.0f;
        this.mInvalidateRunnable = new Runnable() { // from class: co.vine.android.widget.FloatingLikesView.1
            @Override // java.lang.Runnable
            public void run() {
                FloatingLikesView.this.invalidate();
                FloatingLikesView.this.postDelayed(this, 20L);
            }
        };
        processAttrs(attrs);
        postDelayed(this.mInvalidateRunnable, 20L);
    }

    public FloatingLikesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mParticles = new ArrayList();
        this.mAnimationDurationMS = 1000;
        this.mAnimationMaxWidth = -1.0f;
        this.mAnimationHeight = -1.0f;
        this.mIconWidth = -1.0f;
        this.mInvalidateRunnable = new Runnable() { // from class: co.vine.android.widget.FloatingLikesView.1
            @Override // java.lang.Runnable
            public void run() {
                FloatingLikesView.this.invalidate();
                FloatingLikesView.this.postDelayed(this, 20L);
            }
        };
        processAttrs(attrs);
        postDelayed(this.mInvalidateRunnable, 20L);
    }

    public void setAnimationDurationMS(int durationMS) {
        this.mAnimationDurationMS = durationMS;
    }

    public void setAnimationMaxWidth(int animationMaxWidth) {
        this.mAnimationMaxWidth = animationMaxWidth;
    }

    public void setAnimationHeight(int animationHeight) {
        this.mAnimationHeight = animationHeight;
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public void setIconWidth(int width) {
        this.mIconWidth = width;
    }

    private void processAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingLikesView, 0, 0);
        try {
            this.mAnimationMaxWidth = a.getDimension(0, -1.0f);
            this.mAnimationHeight = a.getDimension(1, -1.0f);
            this.mAnimationDurationMS = a.getInt(2, 1000);
            this.mIconWidth = a.getDimension(3, -1.0f);
            this.mDrawable = a.getDrawable(4);
        } finally {
            a.recycle();
        }
    }

    public void addLikeAt(int x, int y, int colorTint) {
        this.mParticles.add(new Particle(x, y, colorTint));
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        List<Particle> toRemove = new ArrayList<>();
        for (Particle h : this.mParticles) {
            if (!h.draw(canvas)) {
                toRemove.add(h);
            }
        }
        this.mParticles.removeAll(toRemove);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mAnimationMaxWidth == -1.0f) {
            this.mAnimationMaxWidth = getMeasuredHeight() / 4;
        }
        if (this.mAnimationHeight == -1.0f) {
            this.mAnimationHeight = (int) (getMeasuredHeight() - this.mIconWidth);
        }
    }

    public void reset() {
        this.mParticles = new ArrayList();
    }

    public int getIconHeight() {
        float iconAspectRatio = this.mDrawable.getIntrinsicWidth() / this.mDrawable.getIntrinsicHeight();
        int iconHeight = (int) (this.mIconWidth / iconAspectRatio);
        return iconHeight;
    }

    private class Particle {
        final int mColorTint;
        private PathMeasure mPathMeasure;
        final int mStartX;
        final int mStartY;
        final long mStartTime = System.currentTimeMillis();
        final float mRotation = getRandomFactor(10.0f);

        public Particle(int x, int y, int colorTint) {
            this.mStartX = x;
            this.mStartY = y;
            this.mColorTint = colorTint;
            createCurve();
        }

        private void createCurve() {
            Path curve = new Path();
            curve.cubicTo((FloatingLikesView.this.mAnimationMaxWidth / 2.0f) * getRandomFactor(1.0f), (-FloatingLikesView.this.mAnimationHeight) / 3.0f, ((-FloatingLikesView.this.mAnimationMaxWidth) / 2.0f) * getRandomFactor(1.0f), ((-2.0f) * FloatingLikesView.this.mAnimationHeight) / 3.0f, (FloatingLikesView.this.mAnimationMaxWidth / 2.0f) * getRandomFactor(1.0f), -FloatingLikesView.this.mAnimationHeight);
            this.mPathMeasure = new PathMeasure(curve, false);
        }

        private float getRandomFactor(float width) {
            float halfWidth = width / 2.0f;
            return (FloatingLikesView.sRandom.nextFloat() * width) - halfWidth;
        }

        public boolean draw(Canvas canvas) {
            float[] point = new float[2];
            int age = (int) (System.currentTimeMillis() - this.mStartTime);
            getPositionFromCurve(point);
            canvas.save();
            canvas.translate(this.mStartX + point[0], this.mStartY + point[1]);
            canvas.rotate(this.mRotation);
            FloatingLikesView.this.mDrawable.setColorFilter(this.mColorTint, PorterDuff.Mode.SRC_ATOP);
            FloatingLikesView.this.mDrawable.setAlpha(Math.min(255, (int) Math.abs((1.0d - (age / FloatingLikesView.this.mAnimationDurationMS)) * 255.0d)));
            float iconAspectRatio = FloatingLikesView.this.mDrawable.getIntrinsicWidth() / FloatingLikesView.this.mDrawable.getIntrinsicHeight();
            float adjustedGrowthTime = Math.min(1.0f, 0.2f + ((age / FloatingLikesView.this.mAnimationDurationMS) * 4.0f));
            int iconWidth = (int) (FloatingLikesView.OVERSHOOT_INTERPOLATOR.getInterpolation(adjustedGrowthTime) * FloatingLikesView.this.mIconWidth);
            int iconHeight = (int) (iconWidth / iconAspectRatio);
            FloatingLikesView.this.mDrawable.setBounds((-iconWidth) / 2, (-iconHeight) / 2, iconWidth / 2, iconHeight / 2);
            FloatingLikesView.this.mDrawable.draw(canvas);
            canvas.restore();
            return age <= 1000;
        }

        private void getPositionFromCurve(float[] point) {
            float percentDone = (System.currentTimeMillis() - this.mStartTime) / 1000.0f;
            float length = this.mPathMeasure.getLength();
            this.mPathMeasure.getPosTan(percentDone * length, point, null);
        }
    }
}
