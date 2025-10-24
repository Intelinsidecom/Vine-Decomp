package co.vine.android.widget.trimcontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import co.vine.android.R;
import co.vine.android.recorder.ThumbnailExtractorFactory;
import co.vine.android.recorder.ThumbnailExtractorInterface;
import co.vine.android.util.CrashUtil;
import co.vine.android.widget.trimcontrols.GripTrack;
import co.vine.android.widget.trimcontrols.ThumbnailCarousel;
import co.vine.android.widget.trimcontrols.ThumbnailTrimmer;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ThumbnailRangeFinderLayout extends FrameLayout implements ThumbnailExtractorInterface.OnThumbnailRetrievedListener, ThumbnailCarousel.WidthMeasuredListener, ThumbnailTrimmer.OnScrubberMovedListener {
    public long durationUsec;
    private int mCarouselPadding;
    private int mDesiredThumbWidth;
    private GripTrack mGripTrack;
    private GripTrack.GripTrackTouchListener mGripTrackTouchListener;
    private int mLastRequestedThumbnailTimestampIndex;
    private int mLayoutWidth;
    private int mLeftMarginPx;
    private boolean mLeftTrimEnabled;
    private OnVideoTrimmedListener mListener;
    private HorizontalScrollView mScrollView;
    private int mScrollViewWidth;
    private ThumbnailCarousel mThumbnailCarousel;
    private ThumbnailExtractorInterface mThumbnailExtractor;
    private ThumbnailTrimmer mThumbnailTrimmer;
    private ArrayList<Long> mTimestamps;
    public long trimEndTimeUsec;
    public long trimStartTimeUsec;

    public interface OnVideoTrimmedListener {
        void onStartEndTimeChanged(long j, long j2);

        void onVideoTrimmedByCarousel();

        void onVideoTrimmedByScrubber();
    }

    public void setListener(OnVideoTrimmedListener listener) {
        this.mListener = listener;
    }

    public ThumbnailRangeFinderLayout(Context context, int leftMarginPx, boolean leftTrimEnabled) {
        super(context);
        this.mLeftMarginPx = 0;
        this.mLastRequestedThumbnailTimestampIndex = -1;
        this.mGripTrackTouchListener = new GripTrack.GripTrackTouchListener() { // from class: co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.1
            @Override // co.vine.android.widget.trimcontrols.GripTrack.GripTrackTouchListener
            public boolean onGripTouchEvent(MotionEvent event) {
                if (ThumbnailRangeFinderLayout.this.mScrollView == null) {
                    return false;
                }
                Matrix matrix = new Matrix();
                int deltaX = ThumbnailRangeFinderLayout.this.getWidth() - ThumbnailRangeFinderLayout.this.mScrollViewWidth;
                matrix.postTranslate(deltaX, 0.0f);
                event.transform(matrix);
                return ThumbnailRangeFinderLayout.this.mScrollView.onTouchEvent(event);
            }
        };
        this.mLeftMarginPx = leftMarginPx;
        this.mLeftTrimEnabled = leftTrimEnabled;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.mDesiredThumbWidth = screenWidth / 8;
        FrameLayout.LayoutParams wrapParams = new FrameLayout.LayoutParams(-2, -2);
        this.mThumbnailCarousel = new ThumbnailCarousel(context, this.mLeftMarginPx);
        this.mThumbnailCarousel.setListener(this);
        this.mThumbnailTrimmer = new ThumbnailTrimmer(context, this.mLeftMarginPx, leftTrimEnabled);
        this.mThumbnailTrimmer.setListener(this);
        this.mGripTrack = new GripTrack(context, this.mGripTrackTouchListener);
        CarouselScrollView scrollView = new CarouselScrollView(context);
        scrollView.setLayoutParams(wrapParams);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.addView(this.mThumbnailCarousel, wrapParams);
        scrollView.setOverScrollMode(2);
        addView(scrollView);
        addView(this.mThumbnailTrimmer);
        addView(this.mGripTrack);
        this.mScrollView = scrollView;
        this.mCarouselPadding = getResources().getDimensionPixelSize(R.dimen.carousel_padding);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int h = b - t;
        this.mScrollView.layout(0, this.mCarouselPadding, this.mScrollViewWidth, h - this.mCarouselPadding);
        this.mGripTrack.layout(0, this.mCarouselPadding, r, h - this.mCarouselPadding);
        computeStartEndTimes();
        this.mGripTrack.invalidate();
        this.mLayoutWidth = w;
    }

    public void setVideoPath(Uri uri, long durationMs, long maxDurationMs) {
        this.mThumbnailCarousel.updateLayout(this.mDesiredThumbWidth, durationMs, maxDurationMs);
        computeStartEndTimes();
        float thumbnailInterval = maxDurationMs / 8.0f;
        float numCuts = durationMs / thumbnailInterval;
        ArrayList<Long> timestamps = new ArrayList<>();
        for (int i = 0; i <= ((int) numCuts); i++) {
            timestamps.add(Long.valueOf(i * ((long) ((1000 * durationMs) / numCuts))));
        }
        clear();
        this.mLastRequestedThumbnailTimestampIndex = -1;
        extractThumbs(uri, timestamps);
        this.durationUsec = 1000 * durationMs;
        this.mThumbnailTrimmer.requestLayout();
    }

    public void clear() {
        stop();
        this.mThumbnailCarousel.clear();
    }

    public void extractThumbs(Uri uri, ArrayList<Long> timestamps) {
        this.mThumbnailExtractor = ThumbnailExtractorFactory.getThumbnailExtractor();
        this.mThumbnailExtractor.start(getContext(), uri, this.mThumbnailCarousel.preferredHeight, this);
        this.mTimestamps = timestamps;
        prefetchThumbs();
    }

    @Override // co.vine.android.recorder.ThumbnailExtractorInterface.OnThumbnailRetrievedListener
    public synchronized void onThumbnailRetrieved(long timestamp, Bitmap bitmap) {
        int size = this.mTimestamps.size();
        if (size > 0) {
            if (timestamp >= this.mTimestamps.get(size - 1).longValue() && this.mThumbnailExtractor != null) {
                this.mThumbnailExtractor.stop();
                this.mThumbnailExtractor = null;
            }
        } else {
            CrashUtil.logOrThrowInDebug(new IllegalStateException("WARNING: timestamp list is empty."));
        }
        if (bitmap != null && this.mLastRequestedThumbnailTimestampIndex != -1) {
            int outHeight = this.mThumbnailCarousel.preferredHeight;
            int outWidth = (bitmap.getWidth() * outHeight) / bitmap.getHeight();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
            bitmap.recycle();
            int trimX = (outWidth - this.mDesiredThumbWidth) / 2;
            int width = this.mDesiredThumbWidth;
            if (trimX < 0) {
                trimX = 0;
                width = Math.min(this.mDesiredThumbWidth, scaledBitmap.getWidth());
            }
            Bitmap resultBitmap = Bitmap.createBitmap(scaledBitmap, trimX, 0, width, outHeight);
            this.mThumbnailCarousel.addThumbnail(resultBitmap);
            scaledBitmap.recycle();
        } else {
            this.mThumbnailCarousel.addThumbnail(null);
        }
    }

    @Override // co.vine.android.recorder.ThumbnailExtractorInterface.OnThumbnailRetrievedListener
    public void onError() {
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailCarousel.WidthMeasuredListener
    public void onWidthMeasured(int containerWidth) {
        this.mScrollViewWidth = Math.min(containerWidth, this.mLayoutWidth);
        this.mThumbnailTrimmer.layout(0, 0, this.mScrollViewWidth, getHeight());
        computeStartEndTimes();
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailTrimmer.OnScrubberMovedListener
    public void onScrubberMoved(int rangeRight, int trimmerFullWidth) {
        this.mScrollViewWidth = trimmerFullWidth;
        this.mThumbnailCarousel.addScrolledViewPadding(this.mLeftMarginPx, this.mThumbnailTrimmer.getWidth() - rangeRight);
        computeStartEndTimes();
        this.mListener.onVideoTrimmedByScrubber();
    }

    public void computeStartEndTimes() {
        int trimStartPx;
        int thumbsLeft;
        ThumbnailCarousel thumbs = this.mThumbnailCarousel;
        if (thumbs.getMeasuredWidth() > 0) {
            if (this.mLeftTrimEnabled) {
                trimStartPx = this.mThumbnailTrimmer.getOutlineRectStart();
            } else {
                trimStartPx = (this.mScrollView.getScrollX() + thumbs.getPaddingLeft()) - this.mLeftMarginPx;
            }
            int trimEndPx = trimStartPx + this.mThumbnailTrimmer.getOutlineRectWidth();
            int widthNoPadding = ((thumbs.getWidth() - thumbs.getPaddingLeft()) - thumbs.getPaddingRight()) - this.mThumbnailTrimmer.getScrubberDrawableWidth();
            this.trimStartTimeUsec = (this.durationUsec * trimStartPx) / widthNoPadding;
            this.trimEndTimeUsec = (this.durationUsec * trimEndPx) / widthNoPadding;
            if (this.mLeftTrimEnabled) {
                thumbsLeft = trimStartPx;
            } else {
                thumbsLeft = this.mLeftMarginPx - trimStartPx;
            }
            int thumbsRight = thumbs.getThumbnailStripWidth() - trimStartPx;
            this.mGripTrack.setBoundaries(thumbsLeft, thumbsRight);
            this.mThumbnailTrimmer.setDeselectedBounds(thumbsLeft, thumbsRight);
        } else {
            this.trimStartTimeUsec = 0L;
            this.trimEndTimeUsec = this.durationUsec;
        }
        this.mListener.onStartEndTimeChanged(this.trimStartTimeUsec, this.trimEndTimeUsec);
    }

    private class CarouselScrollView extends HorizontalScrollView {
        public CarouselScrollView(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            ThumbnailRangeFinderLayout.this.computeStartEndTimes();
            ThumbnailRangeFinderLayout.this.mListener.onVideoTrimmedByCarousel();
            ThumbnailRangeFinderLayout.this.prefetchThumbs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prefetchThumbs() {
        int desiredThumbCount = (this.mScrollView.getScrollX() / this.mDesiredThumbWidth) + 24;
        for (int i = this.mLastRequestedThumbnailTimestampIndex + 1; i < this.mTimestamps.size() && i <= desiredThumbCount; i++) {
            if (this.mThumbnailExtractor != null) {
                this.mThumbnailExtractor.requestThumbnail(this.mTimestamps.get(i).longValue());
            }
            this.mLastRequestedThumbnailTimestampIndex = i;
        }
    }

    public void stop() {
        if (this.mThumbnailExtractor != null) {
            this.mThumbnailExtractor.stop();
        }
        this.mThumbnailCarousel.releaseBitmaps();
    }
}
