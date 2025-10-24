package co.vine.android.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

/* loaded from: classes.dex */
public class RecyclableBitmapDrawable extends BitmapDrawable {
    public RecyclableBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Bitmap bitmap = getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                super.draw(canvas);
            } catch (RuntimeException e) {
            }
        }
    }
}
