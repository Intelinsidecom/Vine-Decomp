package co.vine.android.util;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

/* loaded from: classes.dex */
public class MathUtil {
    public static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        return x < min ? min : x;
    }

    public static void rectFToRect(RectF rectF, Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation, int viewWidth, int viewHeight) {
        matrix.setScale(mirror ? -1.0f : 1.0f, 1.0f);
        matrix.postRotate(displayOrientation);
        matrix.postScale(viewWidth / 2000.0f, viewHeight / 2000.0f);
        matrix.postTranslate(viewWidth / 2.0f, viewHeight / 2.0f);
    }
}
