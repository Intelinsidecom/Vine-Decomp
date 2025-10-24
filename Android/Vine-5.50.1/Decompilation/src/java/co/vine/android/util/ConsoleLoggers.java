package co.vine.android.util;

import com.edisonwang.android.slog.SLogger;

/* loaded from: classes.dex */
public enum ConsoleLoggers {
    VINE_SERVICE("VineService", true),
    NETWORK("Network", CrossConstants.ENABLE_NETWORK_LOGGING),
    PREFETCH("Prefetch", false),
    LOOP_REPORTING("LoopReporting", CrossConstants.ENABLE_LOOP_LOGGING);

    private final SLogger mLogger;

    ConsoleLoggers(String tag, boolean enabled) {
        this.mLogger = new SLogger(tag, enabled);
    }

    public SLogger get() {
        return this.mLogger;
    }
}
