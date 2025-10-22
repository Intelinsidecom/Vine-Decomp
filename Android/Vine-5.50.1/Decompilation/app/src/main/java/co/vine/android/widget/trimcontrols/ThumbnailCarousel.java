package co.vine.android.widget.trimcontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import co.vine.android.R;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ThumbnailCarousel extends View {
    private int mLeftPadding;
    private WidthMeasuredListener mListener;
    private int mThumbnailWidth;
    private ArrayList<Bitmap> mThumbnails;
    private Handler mUiHandler;
    private int mWidth;
    public int preferredHeight;

    public interface WidthMeasuredListener {
        void onWidthMeasured(int i);
    }

    public ThumbnailCarousel(Context context, int paddingLeft) {
        super(context, null);
        init(context, paddingLeft);
    }

    private void init(Context context, int paddingLeft) {
        this.mThumbnails = new ArrayList<>();
        this.preferredHeight = getResources().getDimensionPixelSize(R.dimen.carousel_height);
        this.mUiHandler = new Handler(context.getMainLooper());
        this.mLeftPadding = paddingLeft;
        setPadding(paddingLeft, 0, 0, 0);
    }

    public void updateLayout(int desiredThumbWidth, long durationMs, long maxDurationMs) {
        this.mThumbnailWidth = desiredThumbWidth;
        float thumbnailInterval = maxDurationMs / 8.0f;
        int numThumbnails = (int) (durationMs / thumbnailInterval);
        this.mWidth = (this.mThumbnailWidth * numThumbnails) + this.mLeftPadding;
        requestLayout();
    }

    public void clear() {
        this.mThumbnails.clear();
    }

    public void setListener(WidthMeasuredListener listener) {
        this.mListener = listener;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = this.mWidth > 0 ? View.MeasureSpec.makeMeasureSpec(this.mWidth + getPaddingRight(), 1073741824) : widthMeasureSpec;
        super.onMeasure(widthSpec, heightMeasureSpec);
        setMeasuredDimension(widthSpec, heightMeasureSpec);
        if (this.mWidth > 0 && this.mListener != null) {
            this.mListener.onWidthMeasured(this.mWidth);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.mThumbnails.isEmpty()) {
            int counter = 0;
            Iterator<Bitmap> it = this.mThumbnails.iterator();
            while (it.hasNext()) {
                Bitmap thumbnail = it.next();
                if (thumbnail != null && !thumbnail.isRecycled()) {
                    canvas.drawBitmap(thumbnail, getPaddingLeft() + (this.mThumbnailWidth * counter), getTop(), (Paint) null);
                }
                counter++;
            }
        }
    }

    public synchronized void addThumbnail(final Bitmap bitmap) {
        this.mUiHandler.post(new Runnable() { // from class: co.vine.android.widget.trimcontrols.ThumbnailCarousel.1
            @Override // java.lang.Runnable
            public void run() {
                if (bitmap == null) {
                    if (ThumbnailCarousel.this.mThumbnails.size() > 0) {
                        Bitmap altBitmap = (Bitmap) ThumbnailCarousel.this.mThumbnails.get(ThumbnailCarousel.this.mThumbnails.size() - 1);
                        ThumbnailCarousel.this.mThumbnails.add(altBitmap);
                    }
                } else {
                    ThumbnailCarousel.this.mThumbnails.add(bitmap);
                }
                ThumbnailCarousel.this.invalidate();
            }
        });
    }

    public void addScrolledViewPadding(int paddingLeft, int paddingRight) {
        setPadding(paddingLeft, 0, paddingRight, 0);
    }

    public void releaseBitmaps() {
        Iterator<Bitmap> it = this.mThumbnails.iterator();
        while (it.hasNext()) {
            Bitmap b = it.next();
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
        }
    }

    public int getThumbnailStripWidth() {
        return this.mWidth;
    }
}
