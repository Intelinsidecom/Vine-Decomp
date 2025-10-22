package co.vine.android.util;

import android.os.Environment;
import com.edisonwang.android.slog.SLog;
import java.io.File;

/* loaded from: classes.dex */
public enum FileLoggers {
    PREFETCH("Prefetch"),
    VINE_SERVICE("VineService"),
    NETWORK("Network");

    private final File mFile;
    private final FileLogger mLogger;

    FileLoggers(String filename) {
        this.mFile = new File(new File(Environment.getExternalStorageDirectory(), "VineLogReader"), filename + ".txt");
        this.mLogger = FileLogger.getLogger(SLog.sLogsOn ? this.mFile : null);
    }

    public File file() {
        return this.mFile;
    }

    public FileLogger get() {
        return this.mLogger;
    }
}
