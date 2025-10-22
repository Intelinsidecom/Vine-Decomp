package co.vine.android.scribe;

import android.content.Context;

/* loaded from: classes.dex */
public final class ScribeLoggerSingleton {
    private static ScribeLoggerImpl sScribeLogger;

    public static ScribeLogger getInstance(Context context) {
        if (sScribeLogger == null) {
            sScribeLogger = new ScribeLoggerImpl(context);
            sScribeLogger.initScribeService();
        }
        return sScribeLogger;
    }
}
