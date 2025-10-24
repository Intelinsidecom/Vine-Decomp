package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class MaskedImageView extends ImageView {
    private Bitmap mMaskBitmap;
    private int mMaskResId;
    private Paint mPaint;

    public MaskedImageView(Context context) {
        super(context);
        init();
    }

    public MaskedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskedImageView, defStyle, 0);
        this.mMaskResId = a.getResourceId(0, 0);
        a.recycle();
        init();
    }

    private void init() {
        if (this.mMaskResId > 0) {
            this.mMaskBitmap = BitmapFactory.decodeResource(getResources(), this.mMaskResId);
            this.mPaint = new Paint();
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mMaskResId > 0) {
            canvas.drawBitmap(this.mMaskBitmap, 0.0f, 0.0f, this.mPaint);
        }
    }
}
