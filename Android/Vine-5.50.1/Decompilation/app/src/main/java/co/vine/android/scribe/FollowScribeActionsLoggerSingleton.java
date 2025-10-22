package co.vine.android.scribe;

/* loaded from: classes.dex */
public class FollowScribeActionsLoggerSingleton {
    private static FollowRecScribeActionsLogger sFollowRecScribeLogger;
    private static FollowScribeActionsLogger sFollowScribeActionsLogger;

    public static FollowScribeActionsLogger getInstance(ScribeLogger logger, AppStateProvider appStateProvider, AppNavigationProvider appNavProvider) {
        if (sFollowScribeActionsLogger == null) {
            sFollowScribeActionsLogger = new FollowScribeActionsLogger(logger, appStateProvider, appNavProvider);
        }
        return sFollowScribeActionsLogger;
    }

    public static FollowRecScribeActionsLogger getRecLoggerInstance(FollowScribeActionsLogger logger) {
        if (sFollowRecScribeLogger == null) {
            sFollowRecScribeLogger = new FollowRecScribeActionsLogger(logger);
        }
        return sFollowRecScribeLogger;
    }
}
