package co.vine.android.client;

import android.os.Build;

/* loaded from: classes.dex */
public class TwitterVineApp {
    public static final String API_KEY = transform1();
    public static final String API_SECRET = transform2();

    public static String transform1() {
        if (Build.VERSION.SDK_INT < 10) {
            return "";
        }
        byte[] m = {-79, -66, -66, -33, -65, -35, -84, -94, -97, -62, -50, -61, -55, -31, -92, -77, -48, -87, -93, -50, -98};
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (21 - b));
        }
        return sb.toString();
    }

    public static String transform2() {
        if (Build.VERSION.SDK_INT < 10) {
            return "";
        }
        byte[] m = {-32, -54, -65, -34, -29, -31, -27, -33, -48, -50, -63, -62, -80, -56, -94, -52, -89, -91, -53, -29, -82, -51, -47, -44, -55, -30, -50, -84, -51, -84, -52, -60, -31, -89, -80, -77, -45, -85, -97, -76, -101, -94};
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (21 - b));
        }
        return sb.toString();
    }
}
