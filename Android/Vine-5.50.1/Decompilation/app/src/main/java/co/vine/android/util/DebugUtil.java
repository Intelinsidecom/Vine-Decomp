package co.vine.android.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;
import co.vine.android.AppImpl;
import co.vine.android.R;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.client.VineAPI;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.embed.player.VideoViewHelper;
import co.vine.android.network.NetworkOperation;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.service.GCMRegistrationService;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;
import com.googlecode.javacv.cpp.avutil;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class DebugUtil {
    private static final boolean HIDE_SENSITIVE_DATA;

    static {
        HIDE_SENSITIVE_DATA = !BuildUtil.isLogsOn();
    }

    public static void sendBugReport(Activity activity) throws PackageManager.NameNotFoundException, IOException {
        sendBugReport(activity, null);
    }

    public static void sendBugReport(Activity activity, String debugInfo) throws PackageManager.NameNotFoundException, IOException {
        File file = new File(activity.getExternalCacheDir(), "vine_log.txt");
        try {
            OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(file));
            stream.write("Environment: \n");
            HashMap<String, Object> env = new HashMap<>(CrashUtil.getEnv());
            for (String key : env.keySet()) {
                stream.write(key);
                stream.write(": ");
                stream.write(String.valueOf(env.get(key)));
                stream.write(10);
            }
            stream.write(10);
            if (debugInfo == null) {
                debugInfo = generateDebugInfo(activity);
            }
            stream.write(debugInfo);
            stream.flush();
            stream.close();
            Intent intent = new Intent("co.vine.android.intent.action.SEND_LOGS");
            activity.startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(activity, "Failed to send log.", 0).show();
        }
    }

    public static String generateDebugInfo(Activity activity) throws PackageManager.NameNotFoundException {
        AppController appController = AppController.getInstance(activity);
        Session session = appController.getActiveSession();
        StringBuilder debugInfo = new StringBuilder();
        debugInfo.append("Device: " + Build.DEVICE);
        debugInfo.append("\nModel: " + Build.MODEL);
        debugInfo.append("\nCPU ABI - ABI2: " + Build.CPU_ABI + " - " + Build.CPU_ABI2);
        debugInfo.append("\nHardware: " + Build.HARDWARE);
        debugInfo.append("\nBrand: " + Build.BRAND);
        debugInfo.append("\nProduct: " + Build.PRODUCT);
        debugInfo.append("\nManufacturer: " + Build.MANUFACTURER);
        debugInfo.append("\nBoard: " + Build.BOARD);
        debugInfo.append("\nCores: " + getNumCores() + "\n");
        debugInfo.append("\nAndroid OS Version: " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT + Build.VERSION.INCREMENTAL + " (" + Build.VERSION.CODENAME + ")");
        debugInfo.append("\nApp: ");
        if (BuildUtil.isExplore()) {
            debugInfo.append("Explore\n");
        } else if (BuildUtil.isRegular()) {
            debugInfo.append("Main\n");
        } else {
            debugInfo.append("Other\n");
        }
        long dbSize = VineDatabaseHelper.getDatabasePath(activity).length();
        debugInfo.append("Main database size: " + (dbSize / 1000000.0d) + " MB\n");
        debugInfo.append("Authority: " + BuildUtil.getAuthority("") + "\n");
        debugInfo.append("Vine Player Enabled: " + VideoViewHelper.getGlobalUseVineVideoView() + "\n");
        debugInfo.append("Artificial Audio Latency: " + AudioUtils.getArtificialLatency() + "\n");
        appendMemoryInfo(activity, debugInfo);
        try {
            PackageInfo pi = activity.getPackageManager().getPackageInfo("co.vine.android", 0);
            debugInfo.append("\nVine for Android Version: ").append(pi.versionName);
            debugInfo.append("\nVine for Android Build: ").append(pi.versionCode);
            if (!HIDE_SENSITIVE_DATA) {
                debugInfo.append("\nInstalled: ").append(Util.DATE_TIME_RFC1123.format(new Date(pi.firstInstallTime)));
                debugInfo.append("\nUpdated: ").append(Util.DATE_TIME_RFC1123.format(new Date(pi.lastUpdateTime)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            SLog.e("Cannot parse some info.", (Throwable) e);
        }
        debugInfo.append("\nHW Encoding Mode: " + BuildUtil.isIsHwEncodingEnabled() + "\n");
        debugInfo.append("Build Market: ");
        switch (BuildUtil.getMarket()) {
            case 1:
                debugInfo.append("Google\n");
                break;
            case 2:
                debugInfo.append("Amazon\n");
                break;
            case 3:
                debugInfo.append("Astar\n");
                break;
            default:
                debugInfo.append("Other\n");
                break;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        debugInfo.append("\nScreen Density: ");
        switch (metrics.densityDpi) {
            case 120:
                debugInfo.append(" LDPI");
                break;
            case avcodec.AV_CODEC_ID_CDXL /* 160 */:
                debugInfo.append(" MDPI");
                break;
            case 213:
                debugInfo.append(" TVDPI");
                break;
            case 240:
                debugInfo.append(" HDPI");
                break;
            case avutil.AV_PIX_FMT_YUVJ411P /* 320 */:
                debugInfo.append(" XHDPI");
                break;
            case 480:
                debugInfo.append(" XXHDPI");
                break;
            default:
                debugInfo.append(" UNKNOWN: " + metrics.densityDpi);
                break;
        }
        debugInfo.append("\nScreen Size: " + metrics.widthPixels + "x" + metrics.heightPixels);
        debugInfo.append("\nNormal Video Playable: " + SystemUtil.isNormalVideoPlayable(activity) + " account: " + VineAccountHelper.isNormalVideoPlayable(activity));
        debugInfo.append("\nLogged in user: ").append(session.getUsername());
        if (!HIDE_SENSITIVE_DATA) {
            debugInfo.append("\nLogged in user id: ").append(session.getUserId());
            debugInfo.append("\nLogged in session key: ").append(session.getSessionKey());
        }
        debugInfo.append("\nLogged in name: ").append(session.getScreenName());
        if (!HIDE_SENSITIVE_DATA) {
            debugInfo.append("\nLogged in avatar: ").append(session.getAvatarUrl());
        }
        AccountManager am = AccountManager.get(activity);
        Account acc = VineAccountHelper.getAccount(activity, session.getUserId(), session.getUsername());
        if (acc != null) {
            if (Integer.parseInt(am.getUserData(acc, "account_login_type")) == 2) {
                debugInfo.append("\nLogged in with Twitter.");
            } else {
                debugInfo.append("\nNot logged in with Twitter.");
            }
            if (TextUtils.isEmpty(VineAccountHelper.getTwitterToken(am, acc))) {
                debugInfo.append("\nNot connected to Twitter.");
            } else {
                debugInfo.append("\nConnected to Twitter.");
            }
        }
        if (!HIDE_SENSITIVE_DATA) {
            VineAPI api = VineAPI.getInstance(activity);
            SharedPreferences prefs = GCMRegistrationService.getGCMPreferences(activity);
            debugInfo.append("\n\nGCM: " + prefs.getString("registrationId", "Not registered"));
            debugInfo.append("\nGCM sent to vinepi: " + prefs.getBoolean("registrationIdSent", false));
            debugInfo.append("\n\nAPI: " + api.getBaseUrl());
            debugInfo.append("\nAmazon: " + activity.getString(R.string.amazon_s3));
            debugInfo.append("\nTwitter: " + activity.getString(R.string.twitter_api));
            debugInfo.append("\nRTC: " + api.getRtcUrl());
            debugInfo.append("\nMedia: " + api.getMediaUrl());
            debugInfo.append("\nExplore: " + api.getExploreUrl());
        }
        AppImpl.getInstance().appendDebugInfo(activity, debugInfo, HIDE_SENSITIVE_DATA);
        debugInfo.append("Network Usages:\n");
        debugInfo.append("Data from HTTP Stack: " + Util.formatFileSize(activity.getResources(), NetworkOperation.sNetworkDataUsed) + "\n");
        debugInfo.append("Data from CACHE Layer: " + Util.formatFileSize(activity.getResources(), NetworkOperation.sSavedDataSize) + "\n");
        return debugInfo.toString();
    }

    private static void appendMemoryInfo(Activity activity, StringBuilder debugInfo) {
        int mb;
        ActivityManager activityManager = (ActivityManager) activity.getSystemService("activity");
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        if (Build.VERSION.SDK_INT >= 11 && !BuildUtil.isExplore()) {
            mb = activityManager.getLargeMemoryClass();
        } else {
            mb = activityManager.getMemoryClass();
        }
        debugInfo.append("\nMemory Info: " + mb + "MB");
    }

    private static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() { // from class: co.vine.android.util.DebugUtil.1CpuFilter
                @Override // java.io.FileFilter
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]+", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }
}
