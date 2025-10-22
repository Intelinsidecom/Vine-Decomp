package co.vine.android.util;

import android.os.Environment;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class CrashUtil {
    private static final HashMap<String, Object> sEnv = new HashMap<>();
    private static final int[] sLock = new int[0];
    private static boolean sLogsOn;

    public static void setup(boolean logsOn) {
        sLogsOn = logsOn;
    }

    public static void logOrThrowInDebug(Throwable e) {
        if (sLogsOn) {
            throw new RuntimeException(e);
        }
        Crashlytics.getInstance().core.logException(e);
        Crashlytics.getInstance().core.setString("LastException", getStackTraceAsString(e));
    }

    public static void logException(Throwable e) {
        if (sLogsOn) {
            SLog.e2("Exception was logged.", e);
        } else {
            Crashlytics.getInstance().core.logException(e);
            Crashlytics.getInstance().core.setString("LastException", getStackTraceAsString(e));
        }
    }

    public static void log(String log) {
        if (sLogsOn) {
            SLog.i2(log);
        } else {
            Crashlytics.getInstance().core.log(log);
        }
    }

    public static void log(String message, Object... objects) {
        String log = MessageFormatter.format(message, objects).getMessage();
        if (sLogsOn) {
            SLog.i2(log);
        } else {
            Crashlytics.getInstance().core.log(log);
        }
    }

    public static void logException(Throwable e, String message, Object... objects) {
        String log = MessageFormatter.format(message, objects).getMessage();
        if (sLogsOn) {
            synchronized (sLock) {
                SLog.e("logException triggered: ");
                for (String key : sEnv.keySet()) {
                    SLog.i(key + ": " + sEnv.get(key));
                }
            }
            SLog.e2(log, e);
            return;
        }
        Crashlytics.getInstance().core.log(log);
        Crashlytics.getInstance().core.logException(e);
    }

    public static HashMap<String, Object> getEnv() {
        return sEnv;
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void set(String key, String value) {
        if (value == null) {
            value = "null";
        }
        if (sLogsOn) {
            setEnv(key, value);
        } else {
            Crashlytics.getInstance().core.setString(key, value);
        }
    }

    public static void set(String key, int value) {
        if (sLogsOn) {
            setEnv(key, Integer.valueOf(value));
        } else {
            Crashlytics.getInstance().core.setInt(key, value);
        }
    }

    private static void setEnv(String key, Object value) {
        synchronized (sLock) {
            Log.d("Vine", "" + key + " = " + value);
            sEnv.put(key, value);
        }
    }

    public static void collectLogs(int lineCount, int ignoreCount) {
        try {
            long start = System.currentTimeMillis();
            ArrayList<String> commandLine = new ArrayList<>();
            commandLine.add("logcat");
            commandLine.add("-d");
            commandLine.add("V");
            String[] last = new String[lineCount + ignoreCount];
            Process process = Runtime.getRuntime().exec((String[]) commandLine.toArray(new String[commandLine.size()]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int n = 0;
            boolean hasMore = false;
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    last[n] = line;
                    n++;
                    if (n == last.length) {
                        hasMore = true;
                        n = 0;
                    }
                } finally {
                    bufferedReader.close();
                }
            }
            int total = hasMore ? lineCount : n;
            if (!hasMore) {
                n = 0;
            }
            PrintStream outputStream = null;
            for (int i = 0; i < total; i++) {
                if (SLog.sLogsOn) {
                    if (outputStream == null) {
                        outputStream = new PrintStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "vine_logs.txt")));
                    }
                    outputStream.print(last[n]);
                    outputStream.println();
                } else {
                    log("[$]" + last[n]);
                }
                n++;
                if (n == last.length) {
                    n = 0;
                }
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            SLog.e("Collecting logs took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        } catch (Throwable e) {
            log("Failed to print out the last few lines: " + e);
        }
    }

    public static int getDefaultIgnoreCountForLogCollection(Throwable ex) {
        StackTraceElement[] st;
        if (ex == null || (st = ex.getStackTrace()) == null) {
            return 4;
        }
        int ignoreCount = st.length;
        return ignoreCount;
    }
}
