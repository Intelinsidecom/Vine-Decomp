package co.vine.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.ScriptIntrinsicColorMatrix;
import co.vine.android.cache.image.PhotoImagesCache;
import co.vine.android.util.ImageUtils;

/* loaded from: classes.dex */
public class RenderscriptUtils {
    public static PhotoImagesCache.BlurTool newBlurTool() {
        return new PhotoImagesCache.BlurTool() { // from class: co.vine.android.util.RenderscriptUtils.1
            @Override // co.vine.android.cache.image.PhotoImagesCache.BlurTool
            public ImageUtils.BitmapInfo blur(Context mContext, Bitmap bitmap, int blurRadius, boolean desaturated, int r, int w, int h) {
                try {
                    return new ImageUtils.BitmapInfo(RenderscriptUtils.getBlurredBitmap(mContext, bitmap, blurRadius, desaturated), r, w, h);
                } catch (RSRuntimeException e) {
                    CrashUtil.logException(e);
                    return null;
                }
            }
        };
    }

    public static Bitmap getBlurredBitmap(Context context, Bitmap bmp, int radius, boolean desaturate) {
        Allocation allocationIn;
        Allocation allocationOut;
        try {
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blur.setRadius(radius);
            if (bmp.getConfig() == null) {
                allocationIn = Allocation.createFromBitmap(rs, bmp, Allocation.MipmapControl.MIPMAP_NONE, 1);
                allocationOut = Allocation.createFromBitmap(rs, bmp, Allocation.MipmapControl.MIPMAP_NONE, 1);
            } else {
                allocationIn = Allocation.createFromBitmap(rs, bmp, Allocation.MipmapControl.MIPMAP_NONE, 128);
                allocationOut = Allocation.createFromBitmap(rs, bmp, Allocation.MipmapControl.MIPMAP_NONE, 128);
            }
            allocationIn.copyFrom(bmp);
            blur.setInput(allocationIn);
            blur.forEach(allocationOut);
            if (desaturate) {
                ScriptIntrinsicColorMatrix desat = ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs));
                desat.setGreyscale();
                allocationIn.copyFrom(bmp);
                desat.forEach(allocationOut, allocationIn);
                allocationIn.copyTo(bmp);
            } else {
                allocationOut.copyTo(bmp);
            }
        } catch (Throwable e) {
            CrashUtil.logException(e);
        }
        return bmp;
    }
}
