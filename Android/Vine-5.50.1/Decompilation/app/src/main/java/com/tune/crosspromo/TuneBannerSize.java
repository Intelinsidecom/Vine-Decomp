package com.tune.crosspromo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/* loaded from: classes.dex */
class TuneBannerSize {
    @SuppressLint({"NewApi"})
    public static int getScreenWidthPixels(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
            return size.x;
        }
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }

    @SuppressLint({"NewApi"})
    public static int getScreenHeightPixels(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
            return size.y;
        }
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            return size.y;
        }
        return display.getHeight();
    }

    public static int getBannerHeightPixels(Context context, int orientation) {
        return getBannerHeight(context, getScreenHeightPixels(context), orientation);
    }

    private static int getBannerHeight(Context context, int screenHeightPixels, int orientation) {
        if (orientation == 1) {
            return (int) (context.getResources().getDisplayMetrics().density * 50.0f);
        }
        int i = (int) (screenHeightPixels / context.getResources().getDisplayMetrics().density);
        if (i <= 400) {
            return (int) (32.0f * context.getResources().getDisplayMetrics().density);
        }
        if (i <= 720) {
            return (int) (context.getResources().getDisplayMetrics().density * 50.0f);
        }
        return (int) (90.0f * context.getResources().getDisplayMetrics().density);
    }
}
