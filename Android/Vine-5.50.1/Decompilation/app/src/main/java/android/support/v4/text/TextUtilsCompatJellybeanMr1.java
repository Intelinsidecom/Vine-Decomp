package android.support.v4.text;

import android.text.TextUtils;
import java.util.Locale;

/* loaded from: classes2.dex */
class TextUtilsCompatJellybeanMr1 {
    TextUtilsCompatJellybeanMr1() {
    }

    public static String htmlEncode(String s) {
        return TextUtils.htmlEncode(s);
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        return TextUtils.getLayoutDirectionFromLocale(locale);
    }
}
