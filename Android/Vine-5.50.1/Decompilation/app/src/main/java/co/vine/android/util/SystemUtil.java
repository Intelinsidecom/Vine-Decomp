package co.vine.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.view.Display;
import android.view.WindowManager;
import co.vine.android.VineLoggingException;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class SystemUtil {
    private static String mProcessName;
    private static String mSubProcessName;
    private static double MEMORY_RATIO = -1.0d;
    private static final android.util.SparseArray<Boolean> mTargetProcessInfo = new android.util.SparseArray<>();
    private static final String TARGET_PROCESS_DEFAULT = null;
    private static final String TARGET_VIEW_SERVER = TARGET_PROCESS_DEFAULT;

    public enum PrefBooleanState {
        UNKNOWN,
        TRUE,
        FALSE
    }

    public static boolean isViewServerEnabled(Context context) {
        return SLog.sLogsOn && isTargetProcess(context, 0, TARGET_VIEW_SERVER);
    }

    public static boolean isRunningOnServiceProcess(Context context) {
        initProcessName(context);
        return "".equals(mSubProcessName);
    }

    public static String getSubProcessName(Context context) {
        initProcessName(context);
        return mSubProcessName.replace(":", "");
    }

    public static boolean isSinglePlayerEnabled(Context context) {
        return SLog.sIsAmazon || isTargetProcess(context, 1, ":record");
    }

    @TargetApi(14)
    public static int getMemoryBudgetForLargeMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getLargeMemoryClass();
    }

    public static boolean isOnMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x001e A[Catch: Exception -> 0x0030, TRY_ENTER, TRY_LEAVE, TryCatch #0 {Exception -> 0x0030, blocks: (B:6:0x000b, B:8:0x0011, B:14:0x001e), top: B:20:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static double getMemoryRatio(android.content.Context r6, boolean r7) {
        /*
            double r2 = co.vine.android.util.SystemUtil.MEMORY_RATIO
            r4 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 != 0) goto L1b
            r0 = 0
            if (r7 == 0) goto L1e
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> L30
            r2 = 14
            if (r1 < r2) goto L1e
            int r0 = getMemoryBudgetForLargeMemoryClass(r6)     // Catch: java.lang.Exception -> L30
        L15:
            if (r0 != 0) goto L2b
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L19:
            co.vine.android.util.SystemUtil.MEMORY_RATIO = r2
        L1b:
            double r2 = co.vine.android.util.SystemUtil.MEMORY_RATIO
            return r2
        L1e:
            java.lang.String r1 = "activity"
            java.lang.Object r1 = r6.getSystemService(r1)     // Catch: java.lang.Exception -> L30
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1     // Catch: java.lang.Exception -> L30
            int r0 = r1.getMemoryClass()     // Catch: java.lang.Exception -> L30
            goto L15
        L2b:
            double r2 = (double) r0
            r4 = 4638707616191610880(0x4060000000000000, double:128.0)
            double r2 = r2 / r4
            goto L19
        L30:
            r1 = move-exception
            goto L15
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.util.SystemUtil.getMemoryRatio(android.content.Context, boolean):double");
    }

    private static synchronized void initProcessName(Context context) {
        if (mProcessName == null) {
            mProcessName = context.getPackageName();
            ActivityManager am = (ActivityManager) context.getSystemService("activity");
            List<ActivityManager.RunningAppProcessInfo> procInfos = am.getRunningAppProcesses();
            if (procInfos != null) {
                for (ActivityManager.RunningAppProcessInfo procInfo : procInfos) {
                    if (Process.myPid() == procInfo.pid) {
                        mProcessName = procInfo.processName;
                    }
                }
            }
            int index = mProcessName.indexOf(58);
            if (index != -1) {
                mSubProcessName = mProcessName.substring(index);
            } else {
                mSubProcessName = "";
            }
        }
    }

    public static String getPathFromResourceRaw(Context context, int res) {
        return "android.resource://" + context.getPackageName() + "/" + res;
    }

    public static boolean isOnWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            return false;
        }
        int type = activeNetwork.getType();
        return type == 9 || type == 1;
    }

    public static boolean isRoaming(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isRoaming();
    }

    public static PrefBooleanState isNormalVideoPlayable(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("normalVideoTestPlayable", 0);
        return PrefBooleanState.valueOf(sharedPref.getString("normalVideoTestPlayable", PrefBooleanState.UNKNOWN.name()));
    }

    public static void setNormalVideoPlayable(Context context, boolean isPlayable) {
        context.getSharedPreferences("normalVideoTestPlayable", 0).edit().putString("normalVideoTestPlayable", (isPlayable ? PrefBooleanState.TRUE : PrefBooleanState.FALSE).name()).apply();
    }

    private static boolean isTargetProcess(Context context, int targetKey, String targetProcess) {
        Boolean oldInfo = mTargetProcessInfo.get(targetKey, null);
        if (oldInfo == null) {
            initProcessName(context);
            if (targetProcess == null) {
                oldInfo = Boolean.valueOf(SLog.getAuthority().equals(mProcessName));
            } else {
                oldInfo = Boolean.valueOf(targetProcess.equals(mSubProcessName));
            }
            mTargetProcessInfo.put(targetKey, oldInfo);
            SLog.i("Is target process for {} a {}: {}.", Integer.valueOf(targetKey), targetProcess, oldInfo);
        }
        return oldInfo.booleanValue();
    }

    @TargetApi(13)
    public static Point getDisplaySize(Display display) {
        if (Build.VERSION.SDK_INT >= 13) {
            Point p = new Point();
            display.getSize(p);
            return p;
        }
        Point p2 = new Point();
        p2.x = display.getWidth();
        p2.y = display.getHeight();
        return p2;
    }

    public static Point getDisplaySize(Context context) {
        if (context instanceof Activity) {
            return getDisplaySize(((Activity) context).getWindowManager().getDefaultDisplay());
        }
        return getDisplaySize(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
    }

    public static void quietlyEnsureParentExists(File f) {
        File parent;
        try {
            if (!f.exists() && (parent = f.getParentFile()) != null && !parent.exists()) {
                CrashUtil.logException(new VineLoggingException("Invalid folder, but we caught it."));
                parent.mkdirs();
            }
        } catch (Exception e) {
            CrashUtil.logException(new VineLoggingException("Failed to make parent folder."));
        }
    }

    public static boolean isBatteryCharging(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (batteryStatus == null) {
            return false;
        }
        int status = batteryStatus.getIntExtra("status", -1);
        return status == 2 || status == 5;
    }
}
