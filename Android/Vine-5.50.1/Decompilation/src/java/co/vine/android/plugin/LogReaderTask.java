package co.vine.android.plugin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import co.vine.android.ShowLogActivity;
import co.vine.android.plugin.DebugTasksPlugin;
import co.vine.android.util.FileLogger;
import co.vine.android.util.FileLoggers;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class LogReaderTask implements DialogInterface.OnClickListener, DebugTasksPlugin.DebugTask {
    public static final File DELETE_LOG = new File("Clear All Logs");
    private static final File[] sFiles;
    private WeakReference<Activity> mActivity;

    static {
        FileLoggers[] loggers = FileLoggers.values();
        sFiles = new File[loggers.length + 1];
        for (int i = 0; i < loggers.length; i++) {
            sFiles[i] = loggers[i].file();
        }
        sFiles[loggers.length] = DELETE_LOG;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        Activity activity = this.mActivity.get();
        if (activity != null) {
            if (sFiles[which] == DELETE_LOG) {
                clearLogs();
                return;
            }
            SLog.i("adb pull /sdcard/{}/{}", FileLoggers.NETWORK.file().getParent(), sFiles[which].getName());
            Intent intent = new Intent(activity, (Class<?>) ShowLogActivity.class);
            intent.setAction(sFiles[which].getAbsolutePath());
            activity.startActivity(intent);
        }
    }

    @Override // co.vine.android.plugin.DebugTasksPlugin.DebugTask
    public void performTask(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        CharSequence[] paths = new CharSequence[sFiles.length];
        int i = 0;
        for (File file : sFiles) {
            paths[i] = file.getName();
            i++;
        }
        builder.setItems(paths, this);
        builder.create().show();
    }

    public static void clearLogs() {
        for (File file : sFiles) {
            if (file != DELETE_LOG) {
                FileLogger.getLogger(file).clear();
            }
        }
    }
}
