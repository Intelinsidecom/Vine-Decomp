package co.vine.android.client;

/* loaded from: classes.dex */
public class TumblrVineApp {
    public static final String CONSUMER_KEY = transform1();
    public static final String CONSUMER_SECRET = transform2();

    public static String transform1() {
        byte[] m = {-35, -63, -81, -63, -79, -66, -82, -35, -76, -101, -63, -85, -52, -49, -87, -98, -97, -66, -49, -68, -50, -46, -31, -95, -56, -78, -80, -66, -35, -91, -29, -56, -67, -66, -68, -97, -53, -49, -54, -93, -96, -46, -32, -66, -44, -79, -69, -29, -84, -100};
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (21 - b));
        }
        return sb.toString();
    }

    public static String transform2() {
        byte[] m = {-65, -97, -29, -58, -79, -94, -88, -97, -85, -83, -79, -52, -60, -100, -56, -96, -92, -35, -84, -65, -55, -64, -48, -47, -68, -96, -27, -88, -61, -50, -69, -83, -52, -44, -55, -58, -98, -98, -55, -27, -69, -68, -90, -62, -45, -99, -31, -97, -64, -51};
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (21 - b));
        }
        return sb.toString();
    }
}
