package co.vine.android.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;

/* loaded from: classes.dex */
public class CenteredTextDrawable extends Drawable {
    private final Rect mDrawableBounds;
    private Drawable[] mDrawables;
    private int mDrawablesMinHeight;
    private int mDrawablesMinWidth;
    private final Paint mPaint;
    private String mText;
    private final Rect mTextBounds;
    private int mTextMinHeight;
    private int mTextMinWidth;
    private final Rect mTextPadding;

    public CenteredTextDrawable(Resources res, int... drawableIds) {
        this(getDrawablesFromIds(res, drawableIds));
    }

    public CenteredTextDrawable(Drawable... drawables) {
        setDrawables(drawables);
        this.mPaint = init();
        this.mTextBounds = new Rect();
        this.mTextPadding = new Rect();
        this.mDrawableBounds = new Rect();
    }

    public void setDrawables(Drawable... drawables) {
        this.mDrawables = drawables;
        int height = 0;
        int width = 0;
        for (Drawable drawable : this.mDrawables) {
            height = Math.max(height, drawable.getMinimumHeight());
            width = Math.max(width, drawable.getMinimumWidth());
        }
        this.mDrawablesMinHeight = height;
        this.mDrawablesMinWidth = width;
    }

    private static Drawable[] getDrawablesFromIds(Resources res, int... drawableIds) {
        Drawable[] drawables = new Drawable[drawableIds.length];
        int length = drawableIds.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int drawableId = drawableIds[i];
            drawables[i2] = res.getDrawable(drawableId);
            i++;
            i2++;
        }
        return drawables;
    }

    public void setColorFilter(int index, ColorFilter cf) {
        this.mDrawables[index].setColorFilter(cf);
    }

    public void setText(String text) {
        this.mText = text;
        updateTextBounds();
        invalidateSelf();
    }

    public void setTextPadding(Rect rect) {
        this.mTextPadding.left = rect.left;
        this.mTextPadding.right = rect.right;
        this.mTextPadding.top = rect.top;
        this.mTextPadding.bottom = rect.bottom;
    }

    public void setTextColor(int color) {
        this.mPaint.setColor(color);
        invalidateSelf();
    }

    private Paint init() {
        Paint paint = new Paint(65);
        paint.setTextSize(32.0f);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    public void setTypeFace(Typeface typeFace) {
        this.mPaint.setTypeface(typeFace);
        updateTextBounds();
        invalidateSelf();
    }

    public void setTextSize(float textSize) {
        this.mPaint.setTextSize(textSize);
        updateTextBounds();
        invalidateSelf();
    }

    private void updateTextBounds() {
        String text = this.mText == null ? "" : this.mText;
        this.mPaint.getTextBounds(text, 0, text.length(), this.mTextBounds);
        int height = this.mTextBounds.height() + this.mTextPadding.top + this.mTextPadding.bottom;
        this.mTextMinHeight = Math.max(height, this.mDrawablesMinHeight);
        int width = this.mTextBounds.width() + this.mTextPadding.left + this.mTextPadding.right;
        this.mTextMinWidth = Math.max(width, this.mDrawablesMinWidth);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mTextMinHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mTextMinWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.mDrawableBounds.set(getBounds());
        for (Drawable drawable : this.mDrawables) {
            drawable.setBounds(this.mDrawableBounds);
            drawable.draw(canvas);
        }
        this.mDrawableBounds.left += this.mTextPadding.left;
        this.mDrawableBounds.top += this.mTextPadding.top;
        this.mDrawableBounds.bottom -= this.mTextPadding.bottom;
        this.mDrawableBounds.right -= this.mTextPadding.right;
        canvas.drawText(this.mText, this.mDrawableBounds.exactCenterX(), this.mDrawableBounds.exactCenterY() + ((this.mTextBounds.height() / 2) - 1), this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return this.mPaint.getAlpha() < 255 ? -3 : -1;
    }
}
