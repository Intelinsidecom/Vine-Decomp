package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class RoundedCornerBitmapImageView extends ImageView {
    public Bitmap mCanvasBitmap;
    public int mRadius;

    public RoundedCornerBitmapImageView(Context context) {
        super(context);
    }

    public RoundedCornerBitmapImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedCornerBitmapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornerImageView, defStyle, 0);
        this.mRadius = a.getDimensionPixelSize(0, 0);
        a.recycle();
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        Drawable d = getDrawable();
        if (d instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            if (this.mCanvasBitmap == null || width != this.mCanvasBitmap.getWidth() || height != this.mCanvasBitmap.getHeight()) {
                if (this.mCanvasBitmap != null) {
                    this.mCanvasBitmap.recycle();
                }
                this.mCanvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            Canvas bitmapCanvas = new Canvas(this.mCanvasBitmap);
            bitmapCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            super.draw(bitmapCanvas);
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(this.mCanvasBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(shader);
                RectF rect = new RectF(0.0f, 0.0f, width, height);
                c.drawRoundRect(rect, this.mRadius, this.mRadius, paint);
                return;
            }
            super.draw(c);
            return;
        }
        super.draw(c);
    }
}
