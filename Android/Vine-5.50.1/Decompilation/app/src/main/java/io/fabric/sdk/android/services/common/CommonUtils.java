package io.fabric.sdk.android.services.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.flurry.android.Constants;
import io.fabric.sdk.android.Fabric;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/* loaded from: classes.dex */
public class CommonUtils {
    private static Boolean clsTrace = null;
    private static final char[] HEX_VALUES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static long totalRamInBytes = -1;
    public static final Comparator<File> FILE_MODIFIED_COMPARATOR = new Comparator<File>() { // from class: io.fabric.sdk.android.services.common.CommonUtils.1
        @Override // java.util.Comparator
        public int compare(File file0, File file1) {
            return (int) (file0.lastModified() - file1.lastModified());
        }
    };

    public static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences("com.crashlytics.prefs", 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0032, code lost:
    
        r6 = r5[1];
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String extractFieldFromSystemFile(java.io.File r11, java.lang.String r12) throws java.lang.Throwable {
        /*
            r9 = 1
            r6 = 0
            boolean r7 = r11.exists()
            if (r7 == 0) goto L3a
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L5e
            java.io.FileReader r7 = new java.io.FileReader     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L5e
            r7.<init>(r11)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L5e
            r8 = 1024(0x400, float:1.435E-42)
            r1.<init>(r7, r8)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L5e
        L15:
            java.lang.String r3 = r1.readLine()     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            if (r3 == 0) goto L35
            java.lang.String r7 = "\\s*:\\s*"
            java.util.regex.Pattern r4 = java.util.regex.Pattern.compile(r7)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            r7 = 2
            java.lang.String[] r5 = r4.split(r3, r7)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            int r7 = r5.length     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            if (r7 <= r9) goto L15
            r7 = 0
            r7 = r5[r7]     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            boolean r7 = r7.equals(r12)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
            if (r7 == 0) goto L15
            r7 = 1
            r6 = r5[r7]     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L68
        L35:
            java.lang.String r7 = "Failed to close system file reader."
            closeOrLog(r1, r7)
        L3a:
            return r6
        L3b:
            r2 = move-exception
        L3c:
            io.fabric.sdk.android.Logger r7 = io.fabric.sdk.android.Fabric.getLogger()     // Catch: java.lang.Throwable -> L5e
            java.lang.String r8 = "Fabric"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5e
            r9.<init>()     // Catch: java.lang.Throwable -> L5e
            java.lang.String r10 = "Error parsing "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch: java.lang.Throwable -> L5e
            java.lang.StringBuilder r9 = r9.append(r11)     // Catch: java.lang.Throwable -> L5e
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> L5e
            r7.e(r8, r9, r2)     // Catch: java.lang.Throwable -> L5e
            java.lang.String r7 = "Failed to close system file reader."
            closeOrLog(r0, r7)
            goto L3a
        L5e:
            r7 = move-exception
        L5f:
            java.lang.String r8 = "Failed to close system file reader."
            closeOrLog(r0, r8)
            throw r7
        L65:
            r7 = move-exception
            r0 = r1
            goto L5f
        L68:
            r2 = move-exception
            r0 = r1
            goto L3c
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.common.CommonUtils.extractFieldFromSystemFile(java.io.File, java.lang.String):java.lang.String");
    }

    public static int getCpuArchitectureInt() {
        return Architecture.getValue().ordinal();
    }

    enum Architecture {
        X86_32,
        X86_64,
        ARM_UNKNOWN,
        PPC,
        PPC64,
        ARMV6,
        ARMV7,
        UNKNOWN,
        ARMV7S,
        ARM64;

        private static final Map<String, Architecture> matcher = new HashMap(4);

        static {
            matcher.put("armeabi-v7a", ARMV7);
            matcher.put("armeabi", ARMV6);
            matcher.put("x86", X86_32);
        }

        static Architecture getValue() {
            String arch = Build.CPU_ABI;
            if (TextUtils.isEmpty(arch)) {
                Fabric.getLogger().d("Fabric", "Architecture#getValue()::Build.CPU_ABI returned null or empty");
                return UNKNOWN;
            }
            Architecture value = matcher.get(arch.toLowerCase(Locale.US));
            if (value == null) {
                return UNKNOWN;
            }
            return value;
        }
    }

    public static synchronized long getTotalRamInBytes() {
        if (totalRamInBytes == -1) {
            long bytes = 0;
            String result = extractFieldFromSystemFile(new File("/proc/meminfo"), "MemTotal");
            if (!TextUtils.isEmpty(result)) {
                String result2 = result.toUpperCase(Locale.US);
                try {
                    if (result2.endsWith("KB")) {
                        bytes = convertMemInfoToBytes(result2, "KB", 1024);
                    } else if (result2.endsWith("MB")) {
                        bytes = convertMemInfoToBytes(result2, "MB", 1048576);
                    } else if (result2.endsWith("GB")) {
                        bytes = convertMemInfoToBytes(result2, "GB", 1073741824);
                    } else {
                        Fabric.getLogger().d("Fabric", "Unexpected meminfo format while computing RAM: " + result2);
                    }
                } catch (NumberFormatException e) {
                    Fabric.getLogger().e("Fabric", "Unexpected meminfo format while computing RAM: " + result2, e);
                }
            }
            totalRamInBytes = bytes;
        }
        return totalRamInBytes;
    }

    static long convertMemInfoToBytes(String memInfo, String notation, int notationMultiplier) {
        return Long.parseLong(memInfo.split(notation)[0].trim()) * notationMultiplier;
    }

    public static ActivityManager.RunningAppProcessInfo getAppProcessInfo(String packageName, Context context) {
        ActivityManager actman = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> processes = actman.getRunningAppProcesses();
        if (processes == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.processName.equals(packageName)) {
                return info;
            }
        }
        return null;
    }

    public static String streamToString(InputStream is) throws IOException {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String sha1(String source) {
        return hash(source, "SHA-1");
    }

    public static String sha1(InputStream source) {
        return hash(source, "SHA-1");
    }

    private static String hash(InputStream source, String sha1Instance) throws NoSuchAlgorithmException, IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            while (true) {
                int length = source.read(buffer);
                if (length != -1) {
                    digest.update(buffer, 0, length);
                } else {
                    return hexify(digest.digest());
                }
            }
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "Could not calculate hash for app icon.", e);
            return "";
        }
    }

    private static String hash(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return hexify(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            Fabric.getLogger().e("Fabric", "Could not create hashing algorithm: " + algorithm + ", returning empty string.", e);
            return "";
        }
    }

    private static String hash(String s, String algorithm) {
        return hash(s.getBytes(), algorithm);
    }

    public static String createInstanceIdFrom(String... sliceIds) {
        if (sliceIds == null || sliceIds.length == 0) {
            return null;
        }
        List<String> sliceIdList = new ArrayList<>();
        for (String id : sliceIds) {
            if (id != null) {
                sliceIdList.add(id.replace("-", "").toLowerCase(Locale.US));
            }
        }
        Collections.sort(sliceIdList);
        StringBuilder sb = new StringBuilder();
        Iterator i$ = sliceIdList.iterator();
        while (i$.hasNext()) {
            sb.append(i$.next());
        }
        String concatValue = sb.toString();
        if (concatValue.length() > 0) {
            return sha1(concatValue);
        }
        return null;
    }

    public static long calculateFreeRamInBytes(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
        return mi.availMem;
    }

    public static long calculateUsedDiskSpaceInBytes(String path) {
        StatFs statFs = new StatFs(path);
        long blockSizeBytes = statFs.getBlockSize();
        long totalSpaceBytes = blockSizeBytes * statFs.getBlockCount();
        long availableSpaceBytes = blockSizeBytes * statFs.getAvailableBlocks();
        return totalSpaceBytes - availableSpaceBytes;
    }

    public static float getBatteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent battery = context.registerReceiver(null, ifilter);
        int level = battery.getIntExtra("level", -1);
        int scale = battery.getIntExtra("scale", -1);
        return level / scale;
    }

    public static boolean getProximitySensorEnabled(Context context) {
        if (isEmulator(context)) {
            return false;
        }
        SensorManager sm = (SensorManager) context.getSystemService("sensor");
        Sensor prox = sm.getDefaultSensor(8);
        return prox != null;
    }

    public static void logControlled(Context context, String msg) {
        if (isClsTrace(context)) {
            Fabric.getLogger().d("Fabric", msg);
        }
    }

    public static void logControlledError(Context context, String msg, Throwable tr) {
        if (isClsTrace(context)) {
            Fabric.getLogger().e("Fabric", msg);
        }
    }

    public static void logControlled(Context context, int level, String tag, String msg) {
        if (isClsTrace(context)) {
            Fabric.getLogger().log(level, "Fabric", msg);
        }
    }

    public static boolean isClsTrace(Context context) {
        if (clsTrace == null) {
            clsTrace = Boolean.valueOf(getBooleanResourceValue(context, "com.crashlytics.Trace", false));
        }
        return clsTrace.booleanValue();
    }

    public static boolean getBooleanResourceValue(Context context, String key, boolean defaultValue) {
        Resources resources;
        if (context != null && (resources = context.getResources()) != null) {
            int id = getResourcesIdentifier(context, key, "bool");
            if (id > 0) {
                return resources.getBoolean(id);
            }
            int id2 = getResourcesIdentifier(context, key, "string");
            if (id2 > 0) {
                return Boolean.parseBoolean(context.getString(id2));
            }
            return defaultValue;
        }
        return defaultValue;
    }

    public static int getResourcesIdentifier(Context context, String key, String resourceType) {
        Resources resources = context.getResources();
        return resources.getIdentifier(key, resourceType, getResourcePackageName(context));
    }

    public static boolean isEmulator(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) || androidId == null;
    }

    public static boolean isRooted(Context context) {
        boolean isEmulator = isEmulator(context);
        String buildTags = Build.TAGS;
        if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        File file = new File("/system/app/Superuser.apk");
        if (file.exists()) {
            return true;
        }
        File file2 = new File("/system/xbin/su");
        return !isEmulator && file2.exists();
    }

    public static boolean isDebuggerAttached() {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
    }

    public static int getDeviceState(Context context) {
        int deviceState = 0;
        if (isEmulator(context)) {
            deviceState = 0 | 1;
        }
        if (isRooted(context)) {
            deviceState |= 2;
        }
        if (isDebuggerAttached()) {
            return deviceState | 4;
        }
        return deviceState;
    }

    public static int getBatteryVelocity(Context context, boolean powerConnected) {
        float batterLevel = getBatteryLevel(context);
        if (!powerConnected) {
            return 1;
        }
        if (powerConnected && batterLevel >= 99.0d) {
            return 3;
        }
        if (powerConnected && batterLevel < 99.0d) {
            return 2;
        }
        return 0;
    }

    public static String hexify(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & Constants.UNKNOWN;
            hexChars[i * 2] = HEX_VALUES[v >>> 4];
            hexChars[(i * 2) + 1] = HEX_VALUES[v & 15];
        }
        return new String(hexChars);
    }

    public static boolean isAppDebuggable(Context context) {
        return (context.getApplicationInfo().flags & 2) != 0;
    }

    public static String getStringsFileValue(Context context, String key) {
        int id = getResourcesIdentifier(context, key, "string");
        return id > 0 ? context.getString(id) : "";
    }

    public static void closeOrLog(Closeable c, String message) throws IOException {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                Fabric.getLogger().e("Fabric", message, e);
            }
        }
    }

    public static void flushOrLog(Flushable f, String message) throws IOException {
        if (f != null) {
            try {
                f.flush();
            } catch (IOException e) {
                Fabric.getLogger().e("Fabric", message, e);
            }
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String padWithZerosToMaxIntWidth(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be zero or greater");
        }
        return String.format(Locale.US, "%1$10s", Integer.valueOf(value)).replace(' ', '0');
    }

    public static String getResourcePackageName(Context context) {
        int iconId = context.getApplicationContext().getApplicationInfo().icon;
        return iconId > 0 ? context.getResources().getResourcePackageName(iconId) : context.getPackageName();
    }

    public static void copyStream(InputStream is, OutputStream os, byte[] buffer) throws IOException {
        while (true) {
            int count = is.read(buffer);
            if (count != -1) {
                os.write(buffer, 0, count);
            } else {
                return;
            }
        }
    }

    public static String logPriorityToString(int priority) {
        switch (priority) {
            case 2:
                return "V";
            case 3:
                return "D";
            case 4:
                return "I";
            case 5:
                return "W";
            case 6:
                return "E";
            case 7:
                return "A";
            default:
                return "?";
        }
    }

    public static String getAppIconHashOrNull(Context context) throws IOException {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(getAppIconResourceId(context));
            String sha1 = sha1(is);
            if (isNullOrEmpty(sha1)) {
                sha1 = null;
            }
            return sha1;
        } catch (Exception e) {
            Fabric.getLogger().e("Fabric", "Could not calculate hash for app icon.", e);
            return null;
        } finally {
            closeOrLog(is, "Failed to close icon input stream.");
        }
    }

    public static int getAppIconResourceId(Context context) {
        return context.getApplicationContext().getApplicationInfo().icon;
    }

    public static String resolveBuildId(Context context) throws Resources.NotFoundException {
        int id = getResourcesIdentifier(context, "io.fabric.android.build_id", "string");
        if (id == 0) {
            id = getResourcesIdentifier(context, "com.crashlytics.android.build_id", "string");
        }
        if (id == 0) {
            return null;
        }
        String buildId = context.getResources().getString(id);
        Fabric.getLogger().d("Fabric", "Build ID is: " + buildId);
        return buildId;
    }

    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return res == 0;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
        if (imm != null) {
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    @TargetApi(16)
    public static void finishAffinity(Context context, int resultCode) {
        if (context instanceof Activity) {
            finishAffinity((Activity) context, resultCode);
        }
    }

    @TargetApi(16)
    public static void finishAffinity(Activity activity, int resultCode) {
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                activity.finishAffinity();
            } else {
                activity.setResult(resultCode);
                activity.finish();
            }
        }
    }

    public static boolean canTryConnection(Context context) {
        if (!checkPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
            return true;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void logOrThrowIllegalStateException(String logTag, String errorMsg) {
        if (Fabric.isDebuggable()) {
            throw new IllegalStateException(errorMsg);
        }
        Fabric.getLogger().w(logTag, errorMsg);
    }
}
