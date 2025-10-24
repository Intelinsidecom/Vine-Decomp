package com.crashlytics.android.core;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.QueueFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
class LogFileManager {
    private final Context context;
    private final File filesDir;
    private QueueFile logFile;

    public LogFileManager(Context context, File filesDir) {
        this(context, filesDir, null);
    }

    LogFileManager(Context context, File filesDir, QueueFile logFile) {
        this.context = context;
        this.filesDir = filesDir;
        this.logFile = logFile;
    }

    public void writeToLog(long timestamp, String msg) throws IOException {
        if (this.logFile == null) {
            initLogFile();
        }
        doWriteToLog(65536, timestamp, msg);
    }

    private boolean initLogFile() throws IOException {
        boolean collectLogs = CommonUtils.getBooleanResourceValue(this.context, "com.crashlytics.CollectCustomLogs", true);
        if (!collectLogs) {
            Fabric.getLogger().d("Fabric", "Preferences requested no custom logs. Aborting log file creation.");
            return false;
        }
        CommonUtils.closeOrLog(this.logFile, "Could not close log file: " + this.logFile);
        File f = null;
        try {
            String filename = "crashlytics-userlog-" + UUID.randomUUID().toString() + ".temp";
            File f2 = new File(this.filesDir, filename);
            try {
                this.logFile = new QueueFile(f2);
                f2.delete();
                return true;
            } catch (Exception e) {
                e = e;
                f = f2;
                Fabric.getLogger().e("Fabric", "Could not create log file: " + f, e);
                return false;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    void doWriteToLog(int maxLogSize, long timestamp, String msg) throws UnsupportedEncodingException {
        if (this.logFile != null) {
            if (msg == null) {
                msg = "null";
            }
            try {
                int quarterMaxLogSize = maxLogSize / 4;
                if (msg.length() > quarterMaxLogSize) {
                    msg = "..." + msg.substring(msg.length() - quarterMaxLogSize);
                }
                byte[] msgBytes = String.format(Locale.US, "%d %s%n", Long.valueOf(timestamp), msg.replaceAll("\r", " ").replaceAll("\n", " ")).getBytes("UTF-8");
                this.logFile.add(msgBytes);
                while (!this.logFile.isEmpty() && this.logFile.usedBytes() > maxLogSize) {
                    this.logFile.remove();
                }
            } catch (IOException e) {
                Fabric.getLogger().e("Fabric", "There was a problem writing to the Crashlytics log.", e);
            }
        }
    }

    ByteString getByteStringForLog() {
        if (this.logFile == null) {
            return null;
        }
        final int[] offsetHolder = {0};
        final byte[] logBytes = new byte[this.logFile.usedBytes()];
        try {
            this.logFile.forEach(new QueueFile.ElementReader() { // from class: com.crashlytics.android.core.LogFileManager.1
                @Override // io.fabric.sdk.android.services.common.QueueFile.ElementReader
                public void read(InputStream in, int length) throws IOException {
                    try {
                        in.read(logBytes, offsetHolder[0], length);
                        int[] iArr = offsetHolder;
                        iArr[0] = iArr[0] + length;
                    } finally {
                        in.close();
                    }
                }
            });
        } catch (IOException e) {
            Fabric.getLogger().e("Fabric", "A problem occurred while reading the Crashlytics log file.", e);
        }
        return ByteString.copyFrom(logBytes, 0, offsetHolder[0]);
    }

    void closeLogFile() throws IOException {
        CommonUtils.closeOrLog(this.logFile, "There was a problem closing the Crashlytics log file.");
        this.logFile = null;
    }
}
