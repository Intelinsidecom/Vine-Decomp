package android.support.v4.content.res;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/* loaded from: classes2.dex */
class ConfigurationHelperDonut {
    ConfigurationHelperDonut() {
    }

    static int getScreenHeightDp(Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (metrics.heightPixels / metrics.density);
    }

    static int getScreenWidthDp(Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (metrics.widthPixels / metrics.density);
    }

    static int getSmallestScreenWidthDp(Resources resources) {
        return Math.min(getScreenWidthDp(resources), getScreenHeightDp(resources));
    }

    static int getDensityDpi(Resources resources) {
        return resources.getDisplayMetrics().densityDpi;
    }
}
