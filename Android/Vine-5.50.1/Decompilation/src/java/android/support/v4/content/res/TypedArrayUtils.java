package android.support.v4.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/* loaded from: classes.dex */
public class TypedArrayUtils {
    public static boolean getBoolean(TypedArray a, int index, int fallbackIndex, boolean defaultValue) {
        boolean val = a.getBoolean(fallbackIndex, defaultValue);
        return a.getBoolean(index, val);
    }

    public static Drawable getDrawable(TypedArray a, int index, int fallbackIndex) {
        Drawable val = a.getDrawable(index);
        if (val == null) {
            return a.getDrawable(fallbackIndex);
        }
        return val;
    }

    public static int getInt(TypedArray a, int index, int fallbackIndex, int defaultValue) {
        int val = a.getInt(fallbackIndex, defaultValue);
        return a.getInt(index, val);
    }

    public static int getResourceId(TypedArray a, int index, int fallbackIndex, int defaultValue) {
        int val = a.getResourceId(fallbackIndex, defaultValue);
        return a.getResourceId(index, val);
    }

    public static String getString(TypedArray a, int index, int fallbackIndex) {
        String val = a.getString(index);
        if (val == null) {
            return a.getString(fallbackIndex);
        }
        return val;
    }

    public static CharSequence[] getTextArray(TypedArray a, int index, int fallbackIndex) {
        CharSequence[] val = a.getTextArray(index);
        if (val == null) {
            return a.getTextArray(fallbackIndex);
        }
        return val;
    }

    public static int getAttr(Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        return value.resourceId != 0 ? attr : fallbackAttr;
    }
}
