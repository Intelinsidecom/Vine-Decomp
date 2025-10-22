package co.vine.android.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import co.vine.android.R;

/* loaded from: classes.dex */
public class TrimTimeBar extends View {
    private int mCurrentTime;
    private final Listener mListener;
    private final Rect mProgressBar;
    private final Paint mProgressPaint;
    private int mScrubberCorrection;
    private int mScrubberLeft;
    private int mScrubberPadding;
    private int mScrubberTouched;
    private boolean mScrubbing;
    private final Rect mSelectedBar;
    private final Paint mSelectedPaint;
    private int mTotalTime;
    private final Bitmap mTrimEndScrubber;
    private int mTrimEndScrubberLeft;
    private int mTrimEndScrubberTop;
    private int mTrimEndTime;
    private final Bitmap mTrimStartScrubber;
    private int mTrimStartScrubberLeft;
    private int mTrimStartScrubberTop;
    private int mTrimStartTime;

    public interface Listener {
        void onScrubbingEnd(int i, int i2, int i3);

        void onScrubbingMove(int i);

        void onScrubbingStart();
    }

    private void update() {
        initTrimTimeIfNeeded();
        updatePlayedBarAndScrubberFromTime();
        invalidate();
    }

    public int getPreferredHeight() throws Resources.NotFoundException {
        int defaultHeight = getResources().getDimensionPixelSize(R.dimen.time_bar_default_height);
        if (this.mTrimStartScrubber == null) {
            return defaultHeight;
        }
        int defaultHeight2 = this.mTrimStartScrubber.getHeight();
        return defaultHeight2;
    }

    private boolean inScrubber(float touchX, float touchY, int scrubberLeft, int scrubberTop, Bitmap scrubber) {
        int scrubberRight = scrubberLeft + scrubber.getWidth();
        int scrubberBottom = scrubberTop + scrubber.getHeight();
        return touchX > ((float) (scrubberLeft - this.mScrubberPadding)) && touchX < ((float) (this.mScrubberPadding + scrubberRight)) && touchY > ((float) (scrubberTop - this.mScrubberPadding)) && touchY < ((float) (this.mScrubberPadding + scrubberBottom));
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) throws Resources.NotFoundException {
        int w = r - l;
        int topBottomMargin = getResources().getDimensionPixelSize(R.dimen.time_bar_top_bottom_padding);
        int leftRightMargin = this.mTrimStartScrubber.getWidth() / 2;
        this.mTrimStartScrubberTop = 0;
        this.mTrimEndScrubberTop = 0;
        int left = getPaddingLeft() + leftRightMargin;
        int right = (w - getPaddingRight()) - leftRightMargin;
        int bottom = this.mTrimStartScrubber.getHeight() - topBottomMargin;
        this.mProgressBar.set(left, topBottomMargin, right, bottom);
        update();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(this.mProgressBar, this.mProgressPaint);
        canvas.drawRect(this.mSelectedBar, this.mSelectedPaint);
        canvas.drawBitmap(this.mTrimStartScrubber, this.mTrimStartScrubberLeft, this.mTrimStartScrubberTop, (Paint) null);
        canvas.drawBitmap(this.mTrimEndScrubber, this.mTrimEndScrubberLeft, this.mTrimEndScrubberTop, (Paint) null);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case 0:
                this.mScrubberTouched = whichScrubber(x, y);
                switch (this.mScrubberTouched) {
                    case 1:
                        this.mScrubbing = true;
                        this.mScrubberCorrection = x - this.mTrimStartScrubberLeft;
                        break;
                    case 2:
                        this.mScrubbing = true;
                        this.mScrubberCorrection = x - this.mTrimEndScrubberLeft;
                        break;
                }
                if (this.mScrubbing) {
                    this.mListener.onScrubbingStart();
                    return true;
                }
                break;
            case 1:
            case 3:
                if (this.mScrubbing) {
                    int seekToTime = 0;
                    switch (this.mScrubberTouched) {
                        case 1:
                            seekToTime = getScrubberTime(this.mTrimStartScrubberLeft, trimStartScrubberTipOffset());
                            this.mScrubberLeft = this.mTrimStartScrubberLeft + trimStartScrubberTipOffset();
                            break;
                        case 2:
                            seekToTime = getScrubberTime(this.mTrimEndScrubberLeft, trimEndScrubberTipOffset());
                            this.mScrubberLeft = this.mTrimEndScrubberLeft + trimEndScrubberTipOffset();
                            break;
                    }
                    updateTimeFromPos();
                    this.mListener.onScrubbingEnd(seekToTime, getScrubberTime(this.mTrimStartScrubberLeft, trimStartScrubberTipOffset()), getScrubberTime(this.mTrimEndScrubberLeft, trimEndScrubberTipOffset()));
                    this.mScrubbing = false;
                    this.mScrubberTouched = 0;
                    return true;
                }
                break;
            case 2:
                if (this.mScrubbing) {
                    int seekToTime2 = -1;
                    int lowerBound = this.mTrimStartScrubberLeft + trimStartScrubberTipOffset();
                    int upperBound = this.mTrimEndScrubberLeft + trimEndScrubberTipOffset();
                    switch (this.mScrubberTouched) {
                        case 1:
                            this.mTrimStartScrubberLeft = x - this.mScrubberCorrection;
                            if (this.mTrimStartScrubberLeft > this.mTrimEndScrubberLeft) {
                                this.mTrimStartScrubberLeft = this.mTrimEndScrubberLeft;
                            }
                            int lowerBound2 = this.mProgressBar.left;
                            this.mTrimStartScrubberLeft = clampScrubber(this.mTrimStartScrubberLeft, trimStartScrubberTipOffset(), lowerBound2, upperBound);
                            seekToTime2 = getScrubberTime(this.mTrimStartScrubberLeft, trimStartScrubberTipOffset());
                            break;
                        case 2:
                            this.mTrimEndScrubberLeft = x - this.mScrubberCorrection;
                            int upperBound2 = this.mProgressBar.right;
                            this.mTrimEndScrubberLeft = clampScrubber(this.mTrimEndScrubberLeft, trimEndScrubberTipOffset(), lowerBound, upperBound2);
                            seekToTime2 = getScrubberTime(this.mTrimEndScrubberLeft, trimEndScrubberTipOffset());
                            break;
                    }
                    updateTimeFromPos();
                    updatePlayedBarAndScrubberFromTime();
                    if (seekToTime2 != -1) {
                        this.mListener.onScrubbingMove(seekToTime2);
                    }
                    invalidate();
                    return true;
                }
                break;
        }
        return false;
    }

    private int getBarPosFromTime(int time) {
        return this.mProgressBar.left + ((int) ((this.mProgressBar.width() * time) / this.mTotalTime));
    }

    private int trimStartScrubberTipOffset() {
        return (this.mTrimStartScrubber.getWidth() * 3) / 4;
    }

    private int trimEndScrubberTipOffset() {
        return this.mTrimEndScrubber.getWidth() / 4;
    }

    private void updatePlayedBarAndScrubberFromTime() {
        this.mSelectedBar.set(this.mProgressBar);
        if (this.mTotalTime > 0) {
            this.mSelectedBar.left = getBarPosFromTime(this.mTrimStartTime);
            this.mSelectedBar.right = getBarPosFromTime(this.mTrimEndTime);
            if (!this.mScrubbing) {
                this.mScrubberLeft = this.mSelectedBar.right;
                this.mTrimStartScrubberLeft = this.mSelectedBar.left - trimStartScrubberTipOffset();
                this.mTrimEndScrubberLeft = getBarPosFromTime(this.mTrimEndTime) - trimEndScrubberTipOffset();
                return;
            }
            return;
        }
        this.mSelectedBar.right = this.mProgressBar.left;
        this.mScrubberLeft = this.mProgressBar.left;
        this.mTrimStartScrubberLeft = this.mProgressBar.left - trimStartScrubberTipOffset();
        this.mTrimEndScrubberLeft = this.mProgressBar.right - trimEndScrubberTipOffset();
    }

    private void initTrimTimeIfNeeded() {
        if (this.mTotalTime > 0 && this.mTrimEndTime == 0) {
            this.mTrimEndTime = this.mTotalTime;
        }
    }

    private int whichScrubber(float x, float y) {
        if (inScrubber(x, y, this.mTrimStartScrubberLeft, this.mTrimStartScrubberTop, this.mTrimStartScrubber)) {
            return 1;
        }
        if (inScrubber(x, y, this.mTrimEndScrubberLeft, this.mTrimEndScrubberTop, this.mTrimEndScrubber)) {
            return 2;
        }
        return 0;
    }

    private int clampScrubber(int scrubberLeft, int offset, int lowerBound, int upperBound) {
        int max = upperBound - offset;
        int min = lowerBound - offset;
        return Math.min(max, Math.max(min, scrubberLeft));
    }

    private int getScrubberTime(int scrubberLeft, int offset) {
        return (int) ((((scrubberLeft + offset) - this.mProgressBar.left) * this.mTotalTime) / this.mProgressBar.width());
    }

    private void updateTimeFromPos() {
        this.mCurrentTime = getScrubberTime(this.mScrubberLeft, 0);
        this.mTrimStartTime = getScrubberTime(this.mTrimStartScrubberLeft, trimStartScrubberTipOffset());
        this.mTrimEndTime = getScrubberTime(this.mTrimEndScrubberLeft, trimEndScrubberTipOffset());
    }
}
