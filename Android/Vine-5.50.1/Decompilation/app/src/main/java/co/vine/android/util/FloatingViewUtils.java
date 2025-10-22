package co.vine.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class FloatingViewUtils {
    public static Bitmap buildFloatingViewFor(Context context, View source, View target, int oldWidth, int left, int top) {
        Resources res = context.getResources();
        int oldHeight = source.getMeasuredHeight();
        source.setDrawingCacheQuality(1048576);
        source.setDrawingCacheEnabled(true);
        Bitmap sourceCached = Bitmap.createBitmap(source.getDrawingCache(true));
        source.setDrawingCacheEnabled(false);
        ViewUtil.setBackground(target, new BitmapDrawable(res, sourceCached));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) target.getLayoutParams();
        params.width = oldWidth;
        params.height = oldHeight;
        params.leftMargin = left;
        params.topMargin = top;
        target.setLayoutParams(params);
        return sourceCached;
    }
}
