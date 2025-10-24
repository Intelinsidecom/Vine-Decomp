package co.vine.android.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.v4.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import co.vine.android.MergeResourceManager;
import co.vine.android.R;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.util.analytics.AnalyticsManager;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class BuildUtil {
    private static int sAppType;
    private static int sEnvironment;
    private static boolean sLogsOn;
    private static int sMarket;
    private static Boolean isOldDeviceOrLowEndDevice = null;
    private static final boolean sIsHwEncodingEnabled = RecordConfigUtils.HW_ENABLED;

    public static void setup(String market, String appType, String authority, String environment, boolean logsOn, String buildType, String user) {
        switch (market) {
            case "amazon":
                sMarket = 2;
                break;
            case "astar":
                sMarket = 3;
                break;
            default:
                sMarket = 1;
                break;
        }
        if ("production".equals(environment)) {
            sEnvironment = 1;
        } else {
            sEnvironment = 2;
        }
        if ("explore".equals(appType)) {
            sAppType = 2;
        } else {
            sAppType = 1;
        }
        if (!TextUtils.isEmpty(authority)) {
            CrossConstants.AUTHORITY = authority;
        } else {
            CrossConstants.AUTHORITY = "co.vine.android";
        }
        CrossConstants.BROADCAST_PERMISSION = CrossConstants.AUTHORITY + ".BROADCAST";
        CrossConstants.RES_RAW_CA = R.raw.cacerts;
        CrossConstants.RES_VINE_GREEN = R.color.vine_green;
        CrossConstants.RES_BASE_URL = R.string.vine_api;
        CrossConstants.RES_MEDIA_URL = R.string.vine_media_api;
        CrossConstants.RES_EXPLORE_URL = R.string.explore_url;
        CrossConstants.RES_EXPLORE_URL_PATH = R.string.explore_url_path;
        CrossConstants.RES_CONFIG_URL = R.string.vine_config;
        CrossConstants.RES_TWITTER_API = R.string.twitter_api;
        CrossConstants.RES_RTC_URL = R.string.rtc_url;
        CrossConstants.RES_APP_SECRET = R.string.app_secret;
        CrossConstants.RES_CACHE_TAG_KEY = R.id.CACHE_TAG_KEY;
        CrossConstants.IS_PRODUCTION = isProduction();
        CrossConstants.IS_EXPLORE = isExplore();
        boolean isTesting = "espresso".equals(user) || "robolectric".equals(user);
        CrossConstants.DISABLE_ANIMATIONS = "espresso".equals(user);
        CrossConstants.ENABLE_NETWORK_LOGGING = isTesting;
        CrossConstants.ENABLE_LOOP_LOGGING = isTesting;
        CrossConstants.DISABLE_REALM = "robolectric".equals(user);
        CrossConstants.IS_RELEASE_BUILD = BuildConfig.BUILD_TYPE.equals(buildType);
        CrossConstants.IS_BETA_BUILD = "beta".equals(buildType);
        sLogsOn = logsOn || "robolectric".equals(user);
    }

    public static boolean isIsHwEncodingEnabled() {
        return sIsHwEncodingEnabled;
    }

    public static boolean isAmazon() {
        return sMarket == 2;
    }

    public static boolean isGoogle() {
        return sMarket == 1;
    }

    public static boolean isAStar() {
        return sMarket == 3;
    }

    public static boolean isProduction() {
        return sEnvironment == 1;
    }

    public static boolean isRegular() {
        return sAppType == 1;
    }

    public static boolean isExplore() {
        return sAppType == 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isOldDeviceOrLowEndDevice(android.content.Context r8) {
        /*
            r1 = 1
            r2 = 0
            java.lang.Boolean r0 = co.vine.android.util.BuildUtil.isOldDeviceOrLowEndDevice
            if (r0 != 0) goto L24
            boolean r0 = isExplore()
            if (r0 != 0) goto L1d
            boolean r0 = isExplore()
            if (r0 != 0) goto L2b
            r0 = r1
        L13:
            double r4 = co.vine.android.util.SystemUtil.getMemoryRatio(r8, r0)
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 >= 0) goto L1e
        L1d:
            r2 = r1
        L1e:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
            co.vine.android.util.BuildUtil.isOldDeviceOrLowEndDevice = r0
        L24:
            java.lang.Boolean r0 = co.vine.android.util.BuildUtil.isOldDeviceOrLowEndDevice
            boolean r0 = r0.booleanValue()
            return r0
        L2b:
            r0 = r2
            goto L13
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.util.BuildUtil.isOldDeviceOrLowEndDevice(android.content.Context):boolean");
    }

    public static boolean isLogsOn() {
        return sLogsOn;
    }

    public static int getMarket() {
        return sMarket;
    }

    public static boolean isI18nOn() {
        return sLogsOn;
    }

    public static String getAuthority() {
        return getAuthority("");
    }

    public static String getAuthority(String suffix) {
        return CrossConstants.getAuthority(suffix);
    }

    public static String getWebsiteUrl(Context context) {
        return context.getString(R.string.vine_www);
    }

    public static String getStatusUrl(Context context) {
        return context.getString(R.string.status_url);
    }

    public static String getSenderId(Context context) {
        return context.getString(R.string.sender_id);
    }

    public static void bootstrap(Application appContext) {
        Log.i("bootstrap", "started.");
        setup("google", "regular", "co.vine.android", "production", false, BuildConfig.BUILD_TYPE, "human");
        CrashUtil.setup(sLogsOn);
        PlayerUtil.setup("co.vine.android");
        SLog.setup(sLogsOn, "amazon".equals("google"), "co.vine.android");
        if (isProduction()) {
            CrossConstants.DEV_HEADER = appContext.getString(R.string.dev_header);
        } else {
            CrossConstants.DEV_HEADER = appContext.getString(R.string.dev_internal_header);
        }
        AnalyticsManager.onApplicationInit();
        MergeResourceManager.onApplicationInit();
        if (isLogsOn() && SystemUtil.isRunningOnServiceProcess(appContext)) {
            FileLoggers.NETWORK.get().clear();
        }
        Log.i("bootstrap", "completed.");
    }

    public static boolean isApi21Lollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isNonPublicBuild() {
        return false;
    }
}
