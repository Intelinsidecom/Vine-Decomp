package com.edisonwang.android.slog;

import android.util.Log;
import java.util.HashMap;
import java.util.Random;

/* loaded from: classes.dex */
public class SLogger {
    private final boolean mEnabled;
    private final String mTag;
    private static Random sRandom = new Random();
    private static final HashMap<String, SLogger> sLoggers = new HashMap<>();
    private final HashMap<String, Long> mTimings = new HashMap<>();
    private long mStartTime = -1;

    public static synchronized SLogger getCachedEnabledLogger(String tag) {
        SLogger logger;
        logger = sLoggers.get(tag);
        if (logger == null) {
            logger = new SLogger(tag, true);
            sLoggers.put(tag, logger);
        }
        return logger;
    }

    public SLogger(String tag, boolean enabled) {
        this.mTag = tag + "_";
        this.mEnabled = enabled;
    }

    public boolean isActive() {
        return SLog.sLogsOn && this.mEnabled;
    }

    public void v(String msg) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.v(this.mTag + getCallingClass(), getCallingLine() + msg);
        }
    }

    public void timingStart() {
        this.mStartTime = System.currentTimeMillis();
    }

    public void timingStop() {
        if (SLog.sLogsOn && this.mEnabled) {
            if (this.mStartTime < 0) {
                i("Timing does not exist for this logger.");
            } else {
                i("{} took {} ms", this.mTag, Long.valueOf(System.currentTimeMillis() - this.mStartTime));
            }
        }
    }

    public void d(String msg) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.d(this.mTag + getCallingClass(), getCallingLine() + msg);
        }
    }

    public void d(String format, Object arg) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.d(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void d(String format, Object arg1, Object arg2) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.d(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void d(String msg, Throwable throwable) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.d(this.mTag + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    public void i(String msg) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.i(this.mTag + getCallingClass(), getCallingLine() + msg);
        }
    }

    public void i(String format, Object arg) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.i(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void i(String format, Object arg1, Object arg2) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.i(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void w(String format, Object arg1, Object arg2) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.w(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void e(String msg) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.e(this.mTag + getCallingClass(), getCallingLine() + msg);
        }
    }

    public void e(String format, Object arg) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            Log.e(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void e(String format, Object arg1, Object arg2) {
        if (SLog.sLogsOn && this.mEnabled) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            Log.e(this.mTag + getCallingClass(), getCallingLine() + ft.getMessage());
        }
    }

    public void e(String msg, Throwable throwable) {
        if (SLog.sLogsOn && this.mEnabled) {
            Log.e(this.mTag + getCallingClass(), getCallingLine() + msg, throwable);
        }
    }

    protected String getCallingClass() {
        return getShortClassName(Thread.currentThread().getStackTrace()[5].getClassName());
    }

    protected String getCallingLine() {
        return getLine(Thread.currentThread().getStackTrace()[5]);
    }

    protected static String getShortClassName(String className) {
        int lastPeriod = className.lastIndexOf(46);
        if (lastPeriod >= className.length() - 1) {
            return "";
        }
        if (lastPeriod >= 0) {
            return className.substring(lastPeriod + 1);
        }
        return className;
    }

    protected static String getLine(StackTraceElement element) {
        return element.getMethodName() + "():" + element.getLineNumber() + " - ";
    }
}
