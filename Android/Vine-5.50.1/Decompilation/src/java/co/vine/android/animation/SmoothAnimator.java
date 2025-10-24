package co.vine.android.animation;

import android.os.SystemClock;
import android.view.View;

/* loaded from: classes.dex */
public class SmoothAnimator implements Runnable {
    private float mA;
    private float mAlpha;
    private float mB;
    private float mC;
    private boolean mCanceled;
    private float mD;
    protected float mDurationF;
    private final View mPoster;
    protected long mStartTime;

    public interface AnimationListener {
        void onAnimationFinish(int i, Object obj);
    }

    public SmoothAnimator(View poster, float smoothness, int duration) {
        this.mPoster = poster;
        this.mAlpha = smoothness;
        this.mDurationF = duration;
        float f = 1.0f / ((this.mAlpha * 2.0f) * (1.0f - this.mAlpha));
        this.mD = f;
        this.mA = f;
        this.mB = this.mAlpha / ((this.mAlpha - 1.0f) * 2.0f);
        this.mC = 1.0f / (1.0f - this.mAlpha);
    }

    public float transform(float frac) {
        if (frac < this.mAlpha) {
            return this.mA * frac * frac;
        }
        if (frac < 1.0f - this.mAlpha) {
            return this.mB + (this.mC * frac);
        }
        return 1.0f - ((this.mD * (frac - 1.0f)) * (frac - 1.0f));
    }

    public void start() {
        this.mStartTime = SystemClock.uptimeMillis();
        this.mCanceled = false;
        onStart();
        this.mPoster.post(this);
    }

    public void cancel() {
        this.mCanceled = true;
    }

    public void onCancel() {
    }

    public void onStart() {
    }

    public void onUpdate(float frac, float smoothFrac) {
    }

    public void onStop() {
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mCanceled) {
            onCancel();
            return;
        }
        float fraction = (SystemClock.uptimeMillis() - this.mStartTime) / this.mDurationF;
        if (fraction >= 1.0f) {
            onUpdate(1.0f, 1.0f);
            onStop();
        } else {
            onUpdate(fraction, transform(fraction));
            this.mPoster.post(this);
        }
    }
}
