package co.vine.android.util;

/* loaded from: classes.dex */
public class PlayerUtil {
    private static String sAuthority;

    public static void setup(String authority) {
        sAuthority = authority;
    }

    public static String getAuthority(String suffix) {
        return sAuthority + suffix;
    }
}
