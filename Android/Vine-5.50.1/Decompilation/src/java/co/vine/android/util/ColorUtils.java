package co.vine.android.util;

import android.graphics.Color;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public final class ColorUtils {
    public static int parseColorHex(String colorHex, int defaultColor) {
        if (colorHex == null) {
            return defaultColor;
        }
        if (!colorHex.startsWith("#")) {
            colorHex = "#" + colorHex;
        }
        try {
            try {
                int result = Color.parseColor(colorHex);
                return result;
            } catch (IllegalArgumentException e) {
                SLog.w("Unable to parse color {}", colorHex);
                return defaultColor;
            }
        } catch (Throwable th) {
            return defaultColor;
        }
    }

    public static int ensureNotBlack(int color, int defaultColor) {
        return (16777215 & color) == 0 ? defaultColor : color;
    }
}
