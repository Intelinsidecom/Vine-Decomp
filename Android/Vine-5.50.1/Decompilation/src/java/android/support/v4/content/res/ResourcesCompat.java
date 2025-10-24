package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

/* loaded from: classes.dex */
public final class ResourcesCompat {
    public static Drawable getDrawable(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 21 ? ResourcesCompatApi21.getDrawable(res, id, theme) : res.getDrawable(id);
    }

    public static Drawable getDrawableForDensity(Resources res, int id, int density, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawableForDensity(res, id, density, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return ResourcesCompatIcsMr1.getDrawableForDensity(res, id, density);
        }
        return res.getDrawable(id);
    }

    public static int getColor(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 23 ? ResourcesCompatApi23.getColor(res, id, theme) : res.getColor(id);
    }

    public static ColorStateList getColorStateList(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 23 ? ResourcesCompatApi23.getColorStateList(res, id, theme) : res.getColorStateList(id);
    }

    private ResourcesCompat() {
    }
}
