package co.vine.android.util;

/* loaded from: classes.dex */
public class CrossConstants {
    public static String AUTHORITY;
    public static String BROADCAST_PERMISSION;
    public static String DEV_HEADER = "";
    public static boolean DISABLE_ANIMATIONS;
    public static boolean DISABLE_REALM;
    public static boolean ENABLE_LOOP_LOGGING;
    public static boolean ENABLE_NETWORK_LOGGING;
    public static boolean IS_BETA_BUILD;
    public static boolean IS_EXPLORE;
    public static boolean IS_PRODUCTION;
    public static boolean IS_RELEASE_BUILD;
    public static int RES_APP_SECRET;
    public static int RES_BASE_URL;
    public static int RES_CACHE_TAG_KEY;
    public static int RES_CONFIG_URL;
    public static int RES_EXPLORE_URL;
    public static int RES_EXPLORE_URL_PATH;
    public static int RES_MEDIA_URL;
    public static int RES_RAW_CA;
    public static int RES_RTC_URL;
    public static int RES_TWITTER_API;
    public static int RES_VINE_GREEN;
    public static boolean SHOW_TWITTER_SCREENNAME;
    public static boolean VINE_PLAYER_ENABLED;

    public static String getAuthority(String suffix) {
        return AUTHORITY + suffix;
    }
}
