package co.vine.android.client;

import android.os.Build;

/* loaded from: classes.dex */
public class FacebookVineApp {
    public static final String APP_ID = transform();

    public static String transform() {
        if (Build.VERSION.SDK_INT < 10) {
            return "";
        }
        byte[] m = {-28, -28, -35, -32, -31, -34, -32, -36, -29, -28, -32, -36, -29, -28, -34, -27};
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (21 - b));
        }
        return sb.toString();
    }
}
