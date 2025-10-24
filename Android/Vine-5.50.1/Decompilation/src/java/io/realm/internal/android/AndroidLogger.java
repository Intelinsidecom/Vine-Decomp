package io.realm.internal.android;

import android.util.Log;
import io.realm.internal.log.Logger;

/* loaded from: classes.dex */
public class AndroidLogger implements Logger {
    private int minimumLogLevel = 2;
    private String logTag = "REALM";

    public void setMinimumLogLevel(int logLevel) {
        this.minimumLogLevel = logLevel;
    }

    private void log(int logLevel, String message, Throwable t) {
        if (logLevel >= this.minimumLogLevel) {
            if (message == null || message.length() == 0) {
                if (t != null) {
                    message = Log.getStackTraceString(t);
                } else {
                    return;
                }
            } else if (t != null) {
                message = message + "\n" + Log.getStackTraceString(t);
            }
            if (message.length() < 4000) {
                Log.println(logLevel, this.logTag, message);
            } else {
                logMessageIgnoringLimit(logLevel, this.logTag, message);
            }
        }
    }

    private void logMessageIgnoringLimit(int logLevel, String tag, String message) {
        while (message.length() != 0) {
            int nextNewLineIndex = message.indexOf(10);
            int chunkLength = Math.min(nextNewLineIndex != -1 ? nextNewLineIndex : message.length(), 4000);
            String messageChunk = message.substring(0, chunkLength);
            Log.println(logLevel, tag, messageChunk);
            if (nextNewLineIndex != -1 && nextNewLineIndex == chunkLength) {
                message = message.substring(chunkLength + 1);
            } else {
                message = message.substring(chunkLength);
            }
        }
    }

    @Override // io.realm.internal.log.Logger
    public void v(String message, Throwable t) {
        log(2, message, t);
    }

    @Override // io.realm.internal.log.Logger
    public void d(String message) {
        log(3, message, null);
    }

    @Override // io.realm.internal.log.Logger
    public void w(String message) {
        log(5, message, null);
    }
}
