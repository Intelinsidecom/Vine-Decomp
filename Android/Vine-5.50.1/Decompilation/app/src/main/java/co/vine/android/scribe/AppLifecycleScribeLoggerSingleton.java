package co.vine.android.scribe;

/* loaded from: classes.dex */
public class AppLifecycleScribeLoggerSingleton {
    private static AppLifecycleScribeLogger sAppLifecycleScribeLogger;

    public static AppLifecycleScribeLogger getInstance(ScribeLogger logger, AppStateProvider appStateProvider) {
        if (sAppLifecycleScribeLogger == null) {
            sAppLifecycleScribeLogger = new AppLifecycleScribeLogger(logger, appStateProvider);
        }
        return sAppLifecycleScribeLogger;
    }
}
