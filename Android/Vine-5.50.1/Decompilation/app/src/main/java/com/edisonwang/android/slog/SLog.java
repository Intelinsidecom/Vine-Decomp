package com.edisonwang.android.slog;

import android.util.Log;
import co.vine.android.util.CrashUtil;
import java.util.Random;

/* loaded from: classes.dex */
public class SLog {
    private static String sAuthority;
    public static boolean sIsAmazon;
    public static boolean sLogsOn;
    private static Random sRandom = new Random();

    public static void setup(boolean logsOn, boolean isAmazon, String authority) {
        sLogsOn = true;
        sIsAmazon = isAmazon;
        sAuthority = authority;
        sRandom = new Random();
    }

    public static String getAuthority() {
        return sAuthority;
    }

    public static void dWithTag(String tag, String msg) {
        if (sLogsOn) {
            Log.d(tag, getCallingClass() + ":" + getCallingLine() + msg);
        }
    }

    public static void b(String format, Object arg, boolean error) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            if (error) {
                Log.e("Playback_[Benchmark]", ft.getMessage());
            } else {
                Log.d("Playback_[Benchmark]", ft.getMessage());
            }
            if (error && sRandom.nextInt(100) < 10) {
                CrashUtil.logException(new IllegalMonitorStateException(ft.getMessage()));
            }
        }
    }

    public static void b(String format, Object arg) {
        b(format, arg, false);
    }

    public static void d(String msg) {
        if (sLogsOn) {
            Log.d("Playback_" + getCallingClass(), getCallingLine() + msg);
        }
    }

    public static void d(String format, Object arg) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.d("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void d(String format, Object arg1, Object arg2, Object arg3) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2, arg3);
            Log.d("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void d(String format, Object arg1, Object arg2) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.d("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void d(String format, Object[] args) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, args);
            Log.d("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void d(String msg, Throwable throwable) {
        if (sLogsOn) {
            Log.d("Playback_" + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    public static void i(String msg) {
        if (sLogsOn) {
            Log.i("Playback_" + getCallingClass(), getCallingLine() + msg);
        }
    }

    public static void i(String format, Object arg) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.i("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void i(String format, Object arg1, Object arg2) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.i("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void i(String format, Object arg1, Object arg2, Object arg3) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2, arg3);
            Log.i("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void i(String format, Object[] args) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, args);
            Log.i("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void i(String msg, Throwable throwable) {
        if (sLogsOn) {
            Log.i("Playback_" + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    public static void w(String msg) {
        if (sLogsOn) {
            Log.w("Playback_" + getCallingClass(), getCallingLine() + msg);
        }
    }

    public static void w(String format, Object arg) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.w("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void w(String format, Object[] args) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, args);
            Log.w("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void w(String msg, Throwable throwable) {
        if (sLogsOn) {
            Log.w("Playback_" + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    public static void e(String msg) {
        if (sLogsOn) {
            Log.e("Playback_" + getCallingClass(), getCallingLine() + msg);
        }
    }

    public static void e(String format, Object arg) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.e("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void e(String format, Object arg1, Object arg2) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.e("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public static void e(String format, Object[] arg, Throwable throwable) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.e("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage(), throwable);
        }
    }

    public static void e(String format, Object arg, Throwable throwable) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.e("Playback_" + getCallingClass(), getCallingLine() + ft.getMessage(), throwable);
        }
    }

    public static void e(String format, Object[] args) {
        if (sLogsOn) {
            FormattingTuple ft = MessageFormatter.format(format, args);
            Log.e("Playback_" + getCallingClass(), ft.getMessage());
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (sLogsOn) {
            Log.e("Playback_" + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    private static String getCallingClass() {
        String className = Thread.currentThread().getStackTrace()[4].getClassName();
        int lastPeriod = className.lastIndexOf(46);
        if (lastPeriod >= className.length() - 1) {
            return "";
        }
        if (lastPeriod >= 0) {
            return className.substring(lastPeriod + 1);
        }
        return className;
    }

    private static String getCallingLine() {
        return Thread.currentThread().getStackTrace()[4].getMethodName() + "():" + Thread.currentThread().getStackTrace()[4].getLineNumber() + " - ";
    }

    private static String getCallingLine2() {
        return Thread.currentThread().getStackTrace()[5].getMethodName() + "():" + Thread.currentThread().getStackTrace()[5].getLineNumber() + " - ";
    }

    private static String getCallingClass2() {
        String className = Thread.currentThread().getStackTrace()[5].getClassName();
        int lastPeriod = className.lastIndexOf(46);
        if (lastPeriod >= className.length() - 1) {
            return "";
        }
        if (lastPeriod >= 0) {
            return className.substring(lastPeriod + 1);
        }
        return className;
    }

    public static void i2(String msg) {
        if (sLogsOn) {
            Log.i("Playback_" + getCallingClass2(), getCallingLine2() + msg);
        }
    }

    public static void e2(String msg, Throwable e) {
        if (sLogsOn) {
            Log.e("Playback_" + getCallingClass2(), getCallingLine2() + msg, e);
        }
    }
}
